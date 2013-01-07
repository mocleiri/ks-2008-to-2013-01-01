package com.sigmasys.kuali.ksa.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import javax.persistence.Query;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.sigmasys.kuali.ksa.util.JaxbUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.sigmasys.kuali.ksa.exception.InvalidAchAmountException;
import com.sigmasys.kuali.ksa.exception.InvalidRefundTypeException;
import com.sigmasys.kuali.ksa.exception.NoAchInformationException;
import com.sigmasys.kuali.ksa.exception.RefundCancelledException;
import com.sigmasys.kuali.ksa.exception.RefundFailedException;
import com.sigmasys.kuali.ksa.exception.RefundNotFoundException;
import com.sigmasys.kuali.ksa.exception.RefundNotVerifiedException;
import com.sigmasys.kuali.ksa.exception.UserNotFoundException;
import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Activity;
import com.sigmasys.kuali.ksa.model.Allocation;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.CreditType;
import com.sigmasys.kuali.ksa.model.Payment;
import com.sigmasys.kuali.ksa.model.PostalAddress;
import com.sigmasys.kuali.ksa.model.Refund;
import com.sigmasys.kuali.ksa.model.RefundManifest;
import com.sigmasys.kuali.ksa.model.RefundStatus;
import com.sigmasys.kuali.ksa.model.RefundType;
import com.sigmasys.kuali.ksa.model.Rollup;
import com.sigmasys.kuali.ksa.model.Transaction;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.RefundService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.service.UserPreferenceService;
import com.sigmasys.kuali.ksa.transform.Ach;
import com.sigmasys.kuali.ksa.transform.BatchAch;
import com.sigmasys.kuali.ksa.transform.BatchCheck;
import com.sigmasys.kuali.ksa.transform.Check;
import com.sigmasys.kuali.ksa.util.RequestUtils;
import com.sigmasys.kuali.ksa.util.TransactionUtils;

/**
 * The refund service is the core of KSA-RM-RM. It handles the production of refunds from the system.
 * At its heart is the Refund object, which acts as a queue for refunds to be processed.
 * <p/>
 * The refund service will try to determine if a transaction can be paid as "cash"
 * (that is, without restrictions, not going to a third party, or being repaid in a specific way.)
 * If a refund (or part of a refund) can be paid as "cash" then it will be paid with the refund type as
 * defined in "refund.method" system property, which is a student-specific attribute, unless
 * the "override.refund.method" property is set to a different refund type, in which case, this takes precedence.
 * If neither are set, then the "default.refund.method" property is used.
 * <p/>
 * This allows a default method for all students (likely paper check) with students able to sign up for other
 * refund types (bank transfer, etc.) and it allows a counselor to override that choice in certain cases
 * (for example, they may require certain students to pick up their refund check at the office.)
 * <p/>
 *
 * @author Sergey Godunov
 * @version 1.0
 */
@Service("refundService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class RefundServiceImpl extends GenericPersistenceService implements RefundService {

    /**
     * The logger.
     */
    private static final Log logger = LogFactory.getLog(RefundServiceImpl.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserPreferenceService userPreferenceService;


    /**
     * Creates a list of unverified Refunds in the specified date range.
     * <p/>
     * Per Paul H's description (from "Process Diagrams").
     * It is expected that once the system has produced this refund list, an institution-specific set of rules
     * would then check for use cases specific to the school in question and ensure that the list is ready for
     * human validation. As an example, for an account refund (a refund from one KSA account to another KSA account)
     * the refundAttribute can be used to override the statement text of the refund. The rules engine could be used
     * to insert this text, based on institution-specific preferences.
     *
     * @param accountId The ID of an account for which to create a List of unverified Refunds.
     * @param dateFrom  Beginning of the Date range in which to search for refunds.
     * @param dateTo    End of the Date range in which to search for refunds.
     * @return List of unverified refunds.
     */
    @Override
    @Transactional(readOnly=false)
    public List<Refund> checkForRefund(String accountId, Date dateFrom, Date dateTo) {
        // Get all payment transactions on account accountId, where effectiveDate > dateFrom and < dateTo:
        boolean isRefundable = true;
        BigDecimal amountThreshold = new BigDecimal(0);
        String sql = "select p from Payment p where p.effectiveDate between :dateFrom and :dateTo and p.refundable = :isRefundable and p.clearDate <= current_date() and "
                + "(p.amount - p.lockedAllocatedAmount - p.allocatedAmount <= :amountThreshold) and (p.refundRule is not null or p.transactionType.refundRule is not null)";
        Query query = em.createQuery(sql)
                .setParameter("dateFrom", dateFrom)
                .setParameter("dateTo", dateTo)
                .setParameter("isRefundable", isRefundable)
                .setParameter("amountThreshold", amountThreshold);
        List<Payment> payments = query.getResultList();
        List<Refund> allRefunds = new ArrayList<Refund>();
        Date refundRequestDate = null;
        Account refundRequestedBy = null;

        // Iterate through the Payments:
        for (Payment payment : payments) {
            // Get the refund rule:
            String refundRule = StringUtils.isNotBlank(payment.getRefundRule()) ? payment.getRefundRule() : ((CreditType) payment.getTransactionType()).getRefundRule();
            boolean processAsCash = false;

            if (StringUtils.isBlank(refundRule)) {
                processAsCash = true;
            } else if (isRefundRuleValid(refundRule)) {
                // Check the clearing period from the refund rule. Add it to the effectiveDate of the transaction and compare to current date:
                String clearingPeriodStr = StringUtils.substringBetween(refundRule, "(", ")");
                int clearingPeriod = Integer.parseInt(clearingPeriodStr);

                if (clearingPeriod != 0) {
                    Calendar effectiveDate = Calendar.getInstance();

                    effectiveDate.setTime(payment.getEffectiveDate());
                    effectiveDate.add(Calendar.DAY_OF_YEAR, clearingPeriod);

                    // Uncleared payments can only be credited to the same source:
                    if (effectiveDate.getTimeInMillis() < System.currentTimeMillis()) {
                        processAsCash = true;
                    }
                }
            } else {
                // Invalid refund rule. Get next Payment:
                continue;
            }

            // Lazily initialize the values required to create Refunds:
            if (refundRequestDate == null) {
                String currentUserId = userSessionManager.getUserId(RequestUtils.getThreadRequest());

                refundRequestDate = new Date();
                refundRequestedBy = accountService.getOrCreateAccount(currentUserId);
            }

            // Create a new Refund:
            Refund refund = processAsCash
                    ? createCashRefund(payment, refundRequestDate, refundRequestedBy)
                    : createSourceRefund(payment, refundRequestDate, refundRequestedBy, refundRule);

            allRefunds.add(refund);
        }

        return allRefunds;
    }

    /**
     * A convenience methods that checks for unverified Refunds for each of the specified accounts in the specified date range.
     *
     * @param accountIds Student accounts for which to look for unverified refunds.
     * @param dateFrom   Beginning of the Date range in which to search for refunds.
     * @param dateTo     End of the Date range in which to search for refunds.
     * @return List of unverified refunds for all specified accounts.
     */
    @Override
    @Transactional(readOnly=false)
    public List<Refund> checkForRefund(List<String> accountIds, Date dateFrom, Date dateTo) {
        List<Refund> allRefunds = new ArrayList<Refund>();

        for (String accountId : accountIds) {
            List<Refund> refunds = checkForRefund(accountId, dateFrom, dateTo);

            allRefunds.addAll(refunds);
        }

        return allRefunds;
    }

    /**
     * For each account in the system, creates a list of Refunds within the specified date range.
     *
     * @param dateFrom Beginning of the Date range in which to search for refunds.
     * @param dateTo   End of the Date range in which to search for refunds.
     * @return List of unverified refunds for all accounts in the given date range.
     */
    @Override
    @Transactional(readOnly=false)
    public List<Refund> checkForRefunds(Date dateFrom, Date dateTo) {
        // Get all Accounts and IDs:
        Query query = em.createQuery("select a from Account a");
        List<Account> allAccounts = query.getResultList();
        List<String> allAccountIds = new ArrayList<String>();

        for (Account account : allAccounts) {
            allAccountIds.add(account.getId());
        }

        // Get all Refunds:
        return checkForRefund(allAccountIds, dateFrom, dateTo);
    }

    /**
     * For each account in the system, searches for refunds for the entire life.
     *
     * @return List of all Refunds for all accounts in the system for the entire life.
     */
    @Override
    @Transactional(readOnly=false)
    public List<Refund> checkForRefunds() {
        return checkForRefunds(new Date(0), new Date(Long.MAX_VALUE));
    }

    /**
     * This actually creates the refund transaction, in addition to allocating the refund to the original charge.
     * It marks the refund object as refunded.
     *
     * @param refundId ID of a Refund object.
     * @return The Refund object.
     */
    @Override
    public Refund performRefund(Long refundId) {
        return performRefund(refundId, null);
    }

    /**
     * This actually creates the refund transaction as a part of a batch transaction, in addition to allocating the refund to the original charge.
     * It marks the refund object as refunded.
     *
     * @param refundId ID of a Refund object.
     * @param batch    ID of a batch transaction.
     * @return The Refund object.
     */
    @Override
    public Refund performRefund(Long refundId, String batch) {
        return performRefundInternal(refundId, batch, null);
    }

    /**
     * Performs refund validation.
     * Sets the refundStatus to {@link RefundStatus#VERIFIED} and the authorizedBy to the current user.
     *
     * @param refundId
     * @return
     */
    @Override
    public Refund validateRefund(Long refundId) {
        // Get the Refund object by its identifier:
        Refund refund = getRefund(refundId, false);

        // Set the Refund status to VERIFIED:
        String currentUserId = userSessionManager.getUserId(RequestUtils.getThreadRequest());
        Account currentUserAccount = accountService.getOrCreateAccount(currentUserId);

        refund.setStatus(RefundStatus.VERIFIED);
        refund.setAuthorizedBy(currentUserAccount);
        persistEntity(refund);

        return refund;
    }

    /**
     * Performs a refund to a student account. Calls <code>performRefund</code> for the actual refund action.
     *
     * @param refundId ID of a refund to apply to a student account.
     * @return The <code>Refund</code> object that was generated during this process.
     */
    @Override
    @Transactional(readOnly=false)
    public Refund doAccountRefund(Long refundId) {
        return doAccountRefund(refundId, null);
    }

    /**
     * Performs a batched refund to a student account. Calls <code>performRefund</code> for the actual refund action.
     *
     * @param refundId ID of a refund to apply to a student account.
     * @param batch    Batch of transactions.
     * @return The <code>Refund</code> object that was generated during this process.
     */
    @Override
    @Transactional(readOnly=false)
    public Refund doAccountRefund(Long refundId, String batch) {
        // Get the Refund object and check that it's in the VERIFIED status:
        Refund refund = getRefund(refundId, true);

        // Check that it's an Account refund type using the Refund's "attribute" attribute:
        String refundRule = refund.getAttribute();

        if (!StringUtils.startsWith(refundRule, "A")) {
            throw new InvalidRefundTypeException("Invalid Refund type for an Account refund. Refund rule (attribute) is [" + refundRule + "].");
        }

        // Validate the refund rule:
        if (!isRefundRuleValid(refundRule)) {
            // Check if the refund rule is invalid because of a non-existing Account:
            String[] refundRuleParams = StringUtils.substringsBetween(refundRule, "(", ")");

            if ((refundRuleParams != null) && (refundRuleParams.length == 2)) {
                String accountId = refundRuleParams[1];
                boolean accountExists = accountService.accountExists(accountId);

                if (!accountExists) {
                    // Fail the Refund:
                    refund.setStatus(RefundStatus.FAILED);

                    // Create an Activity to record this occurrence:
                    createInvalidAccountActivity(accountId);

                    throw new UserNotFoundException("Refund attribute contains an invalid Account identifier. Refund attribute was [" + refundRule + "]");
                }
            }

            // If, however, the refund rule is invalid for another reason, throw an exception:
            throw new InvalidRefundTypeException("Invalid Refund attribute [" + refundRule + "].");
        }

        // Perform refund:
        String accountId = StringUtils.substringsBetween(refundRule, "(", ")")[1];

        refund = performRefundInternal(refundId, batch, accountId);

        // Create another payment Transaction for the Account to be credited:
        String paymentTypeId = refund.getRefundType().getCreditTypeId();
        Transaction paymentTransaction = transactionService.createTransaction(paymentTypeId, accountId, new Date(), refund.getAmount());

        if (StringUtils.isNotBlank(refund.getStatement())) {
            paymentTransaction.setStatementText(refund.getStatement());
        }

        persistEntity(paymentTransaction);

        // Set the Refund system:
        String systemName = configService.getInitialParameter(Constants.REFUND_ACCOUNT_SYSTEM_NAME);

        refund.setSystem(systemName);

        // Add Refund Manifest:
        addAccountRefundManifest(refund, accountId, paymentTransaction);

        return refund;
    }

    /**
     * Go through the Refund objects in a batch. For each validated refund with type set to account refund
     * (account.refund.type). For each one that is found, call doAccountRefund (refundId, batch).
     *
     * @param batch ID of a transaction batch.
     * @return A <code>List</code> of <code>Refund</code> objects created as a result of this operation.
     */
    @Override
    @Transactional(readOnly=false)
    public List<Refund> doAccountRefunds(String batch) {
        // Find all Refund object with the given value of "batch":
        String sql = "select r from Refund r where r.batchId = :batchId and r.status = :status and r.refundType.debitTypeId = :refundTypeId";
        String refundTypeId = configService.getInitialParameter(Constants.REFUND_ACCOUNT_TYPE);
        Query query = em.createQuery(sql)
                .setParameter("batchId", batch)
                .setParameter("status", RefundStatus.VERIFIED)
                .setParameter("refundTypeId", refundTypeId);
        List<Refund> match = query.getResultList();

        // For each matching Refund, perform account refund:
        for (Refund refund : match) {
            doAccountRefund(refund.getId(), batch);
        }

        return match;
    }

    /**
     * Performs refund as a check.
     *
     * @param refundId  Refund identifier.
     * @param checkDate Date on the issued check.
     * @param checkMemo Memo section on the issued check.
     * @return String An XML form of the issued check.
     */
    @Override
    @Transactional(readOnly=false)
    public String doCheckRefund(Long refundId, Date checkDate, String checkMemo) {
        return doCheckRefund(refundId, null, checkDate, checkMemo);
    }

    /**
     * Performs a batch refund as a check.
     *
     * @param refundId  Refund identifier.
     * @param batch     ID of a transaction batch.
     * @param checkDate Date on the issued check.
     * @param checkMemo Memo section on the issued check.
     * @return String An XML form of the issued check.
     * @see RefundService#produceXMLCheck(String, String, PostalAddress, BigDecimal, Date, String)
     */
    @Override
    @Transactional(readOnly=false)
    public String doCheckRefund(Long refundId, String batch, Date checkDate, String checkMemo) {
        // Check the flag to consolidate same account checks:
        boolean consolidateSameAccountRefunds = BooleanUtils.toBoolean(configService.getInitialParameter(Constants.REFUND_CHECK_GROUP));
        Check check = doCheckRefundInternal(refundId, batch, checkDate, checkMemo, consolidateSameAccountRefunds);

        return JaxbUtils.toXml(check);
    }

    /**
     * Performs check type refunds as a part of a batch.
     *
     * @param batch     Batch of transactions.
     * @param checkDate Date on the refund checks.
     * @param checkMemo Memo section on the refund checks.
     * @return A batch of checks in form of an XML.
     * @see RefundService#produceXMLCheck(String, String, PostalAddress, BigDecimal, Date, String)
     */
    @Override
    @Transactional(readOnly=false)
    public String doCheckRefunds(String batch, Date checkDate, String checkMemo) {
        // Find all VERIFIED Check Refunds in the batch:
        String sql = "select r from Refund r where r.batchId = :batchId and r.status = :status and r.refundType.debitTypeId = :refundTypeId";
        String refundTypeId = configService.getInitialParameter(Constants.REFUND_CHECK_TYPE);
        Query query = em.createQuery(sql)
                .setParameter("batchId", batch)
                .setParameter("status", RefundStatus.VERIFIED)
                .setParameter("refundTypeId", refundTypeId);
        List<Refund> match = query.getResultList();

        // Create a new BatchCheck and a BatchControl:
        BatchCheck batchCheck = new BatchCheck();
        BatchCheck.BatchControl batchControl = new BatchCheck.BatchControl();
        BigDecimal totalValue = new BigDecimal(0);

        batchCheck.setBatchControl(batchControl);
        batchCheck.setBatchIdentifier(batch);
        batchControl.setNumberOfChecks(match.size());

        // For each matching Refund, perform refund and record its XML check:
        for (Refund refund : match) {
            Check check = doCheckRefundInternal(refund.getId(), batch, checkDate, checkMemo, false);

            batchCheck.getCheck().add(check);
            totalValue = totalValue.add(check.getAmount());
        }

        // Set the total value of checks:
        batchControl.setValueOfChecks(totalValue);

        // Marshal the batch check:
        return JaxbUtils.toXml(batchCheck);
    }

    /**
     * Produces an XML form of a refund check.
     *
     * @param identifier    Check identifier.
     * @param payee         To whom the check is made out to.
     * @param postalAddress Address of the payee.
     * @param amount        Amount on the check.
     * @param checkDate     Date on the check.
     * @param memo          Memo section on the check.
     * @return A refund check in an XML form.
     * @see RefundService#produceXMLCheck(String, String, PostalAddress, BigDecimal, Date, String)
     */
    @Override
    @Transactional(readOnly=true)
    public String produceXMLCheck(String identifier, String payee, PostalAddress postalAddress, BigDecimal amount, Date checkDate, String memo) {
        // Create a new Check object:
        Check check = produceCheckInternal(identifier, payee, postalAddress, amount, checkDate, memo);

        // Marshal the Check object:
        return JaxbUtils.toXml(check);
    }

    /**
     * Performs refund as a bank account credit.
     *
     * @param refundId Refund identifier.
     * @return String An XML form of the bank account information (Ach).
     * @see RefundService#produceAchTransmission(Ach, BigDecimal, String)
     */
    @Override
    @Transactional(readOnly=false)
    public String doAchRefund(Long refundId) {
        return doAchRefund(refundId, null);
    }

    /**
     * Performs a batch refund as a bank account credit.
     *
     * @param refundId Refund identifier.
     * @param batch    ID of a transaction batch.
     * @return String An XML form of the bank account information (Ach).
     * @see RefundService#produceAchTransmission(Ach, BigDecimal, String)
     */
    @Override
    @Transactional(readOnly=false)
    public String doAchRefund(Long refundId, String batch) {
        // Check the flag to consolidate same account checks:
        boolean consolidateSameAccountRefunds = BooleanUtils.toBoolean(configService.getInitialParameter(Constants.REFUND_ACH_GROUP));
        Ach achTransmission = doAchRefundInternal(refundId, batch, consolidateSameAccountRefunds);

        return JaxbUtils.toXml(achTransmission);
    }

    /**
     * Performs bank account credit type refunds as a part of a batch.
     * Go through the Refund objects. For each validated refund with type set to ach refund (refund.ach.type).
     * For each one that is found, call doAchRefund (refundId, batch).
     *
     * @param batch Batch of transactions.
     * @return String An XML form of a batch of the bank account informations (Ach).
     * @see RefundService#produceAchTransmission(Ach, BigDecimal, String)
     */
    @Override
    @Transactional(readOnly=false)
    public String doAchRefunds(String batch) {
        // Find all VERIFIED Ach Refunds in the batch:
        String sql = "select r from Refund r where r.batchId = :batchId and r.status = :status and r.refundType.debitTypeId = :refundTypeId";
        String refundTypeId = configService.getInitialParameter(Constants.REFUND_ACH_TYPE);
        Query query = em.createQuery(sql)
                .setParameter("batchId", batch)
                .setParameter("status", RefundStatus.VERIFIED)
                .setParameter("refundTypeId", refundTypeId);
        List<Refund> match = query.getResultList();

        // Create a new BatchAch and a BatchControl:
        BatchAch batchAch = new BatchAch();
        BatchAch.BatchControl batchControl = new BatchAch.BatchControl();
        BigDecimal totalValue = new BigDecimal(0);

        batchAch.setBatchControl(batchControl);
        batchAch.setBatchIdentifier(batch);
        batchControl.setNumberOfAchs(match.size());

        // For each matching Refund, perform refund and record its XML check:
        for (Refund refund : match) {
            Ach ach = doAchRefundInternal(refund.getId(), batch, false);

            batchAch.getAch().add(ach);
            totalValue = totalValue.add(ach.getAmount());
        }

        // Set the total value of checks:
        batchControl.setValueOfAchs(totalValue);

        // Marshal the batch check:
        return JaxbUtils.toXml(batchAch);
    }

    /**
     * Produces a bank account transmission as an XML document.
     *
     * @param ach       <code>Ach</code> that contains bank account information.
     * @param amount    Amount to be transmitted to the bank account.
     * @param reference Reference information.
     * @return Bank account transmission as an XML document.
     */
    @Override
    @Transactional(readOnly=true)
    public String produceAchTransmission(Ach ach, BigDecimal amount, String reference) {
        // Create a new Ach:
        Ach achTransmission = produceAchTransmissionInternal(amount, reference, ach);

        return JaxbUtils.toXml(achTransmission);
    }

    /**
     * Checks if a refund rule is valid.
     * Refer to the "Process Diagrams" for the details.
     *
     * @param refundRule A refund rule.
     * @return boolean Whether the specified refund rule is valid.
     */
    @Override
    @Transactional(readOnly=true)
    public boolean isRefundRuleValid(String refundRule) {
        if (StringUtils.isBlank(refundRule)) {
            return true;
        }

        // example A(100)(pheald) or S(45)
        // transactionService.isRefundRule(refundRule);
        // applies to credit types
        // drill down on the refund rule provided
        if (refundRule.startsWith("A") || refundRule.startsWith("S")) {
            // does the rule have a number 0 - 65535 after/in parenthesis
            int openRuleIndex = refundRule.indexOf("(");
            int closeRuleIndex = refundRule.indexOf(")");
            if ((openRuleIndex > 0) && (closeRuleIndex > 0) && (openRuleIndex < closeRuleIndex)) {
                try {
                    int value = Integer.valueOf(refundRule.substring(openRuleIndex + 1, closeRuleIndex));
                    if ((value < 0) || (value > 0xffff)) {
                        return false;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return false;
                }
                if (refundRule.startsWith("S")) {
                    return true;
                } else {
                    // There should be a second parameter in parenthesis for "A" which appears to be the accountId
                    // which must exist in KSA
                    int openAccountIndex = refundRule.indexOf("(", closeRuleIndex + 1);
                    int closeAccountIndex = refundRule.indexOf(")", closeRuleIndex + 1);
                    if ((openAccountIndex > 0) && (closeAccountIndex > 0) && (openAccountIndex < closeAccountIndex)) {
                        try {
                            String accountId = refundRule.substring(openAccountIndex + 1, closeAccountIndex);
                            return (accountService.getOrCreateAccount(accountId) != null);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            return false;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Performs refund cancellation.
     * Reverses the associated <code>Transaction</code> and sets the appropriate refund status.
     * Refer to the "Process Diagrams" document for the refund cancellation logic.
     *
     * @param refundId ID of a <code>Refund</code> to be cancelled.
     * @param memo     An explanation of the reasons for refund cancellation.
     * @return <code>Refund</code> that was cancelled.
     */
    @Override
    @Transactional(readOnly=false)
    public Refund cancelRefund(Long refundId, String memo) {
        // Get the Refund object:
        Refund refund = getRefund(refundId, false);
        RefundStatus status = refund.getStatus();

        // Check different statuses that lead to different results:
        if (status == RefundStatus.CANCELED) {
            throw new RefundCancelledException("Refund with ID [" + refundId + "] is already canceled.");
        } else if (status == RefundStatus.FAILED) {
            throw new RefundFailedException("Refund with ID [" + refundId + "] is already failed.");
        } else if ((status == RefundStatus.VERIFIED) || (status == RefundStatus.UNVERIFIED)) {
            refund.setStatus(RefundStatus.CANCELED);
        } else if (status == RefundStatus.REFUNDED) {
            // Check all Refunds belonging to the same refund group, cancel them and reverse all Transactions.
            // If there is no refund group, cancel only the refund and reverse its Transaction:
            String refundGroup = refund.getRefundGroup();
            List<Refund> refundsToCancel = new ArrayList<Refund>();

            refundsToCancel.add(refund);

            if (StringUtils.isNotBlank(refundGroup)) {
                // Get all Refunds with the same group ID:
                String sql = "select r from Refund r where r.refundGroup = :refundGroup and r.id <> :id";
                Query query = em.createQuery(sql).setParameter("refundGroup", refundGroup).setParameter("id", refund.getId());

                refundsToCancel.addAll(query.getResultList());
            }

            // Cancel all Refunds and reverse their Transactions:
            for (Refund refundToCancel : refundsToCancel) {
                Transaction refundTransaction = refundToCancel.getRefundTransaction();
                BigDecimal amount = refund.getAmount();

                transactionService.reverseTransaction(refundTransaction.getId(), memo, amount, "Refund Cancellation");
                refundToCancel.setStatus(RefundStatus.CANCELED);
                persistEntity(refundToCancel);
            }
        }

        return refund;
    }

    /**
     * Either retrieves an existing <code>RefundType</code> with the matching values of
     * the debit and credit payment types, or creates and persists a new one.
     *
     * @param debitTypeId  Debit type ID.
     * @param creditTypeId Credit type ID.
     * @return An existing <code>RefundType</code> or a newly created one if there is no existing one.
     */
    @Override
    @Transactional(readOnly=false)
    public RefundType getOrCreateRefundType(String debitTypeId, String creditTypeId) {
    	// Validate the parameters:
    	if (StringUtils.isBlank(debitTypeId) || StringUtils.isBlank(creditTypeId)) {
    		throw new IllegalArgumentException("Debit and Credit types cannot be null when requesting RefundTypes.");
    	}
    	
        // Create a query:
        String sql = "select rt from RefundType rt where rt.debitTypeId = :debitTypeId and rt.creditTypeId = :creditTypeId";
        Query query = em.createQuery(sql)
                .setParameter("debitTypeId", debitTypeId)
                .setParameter("creditTypeId", creditTypeId)
                .setMaxResults(1);
        List<RefundType> result = query.getResultList();
        RefundType refundType;

        if (CollectionUtils.isEmpty(result)) {
            // Create a new RefundType:
            refundType = new RefundType();
            refundType.setDebitTypeId(debitTypeId);
            refundType.setCreditTypeId(creditTypeId);
            refundType.setCode(debitTypeId + ":" + creditTypeId);
            refundType.setName("Autogenerated");
            persistEntity(refundType);
        } else {
            refundType = result.get(0);
        }

        return refundType;
    }
    
	/**
	 * Deletes a <code>RefundType</code> from the persistent storage.
	 * 
	 * @param refundType A <code>RefundType</code> to delete from the storage.
	 */
    @Override
    @Transactional(readOnly=false)
	public void deleteRefundType(RefundType refundType) {
		deleteEntity(refundType.getId(), RefundType.class);
	}

    @Override
    @Transactional(readOnly=false)
    public Refund payoffWithRefund(String accountId, BigDecimal maxPayoff) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional(readOnly=false)
    public Refund doPayoffRefund(Long refundId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional(readOnly=false)
    public Refund doPayoffRefund(Long refundId, String batch) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional(readOnly=false)
    public List<Refund> doPayoffRefunds(String batch) {
        // TODO Auto-generated method stub
        return null;
    }


    /* ****************************************************************
      *
      * Utility methods.
      *
      * ****************************************************************/

    /**
     * Performs a refund to a bank account. Returns the bank account transmission object.
     *
     * @param refundId                      ID of a Refund to process.
     * @param batch                         Batch of refunds.
     * @param consolidateSameAccountRefunds Whether to consolidate refunds into one transmission.
     * @return Ach transmission as a JAXB object.
     */
    private Ach doAchRefundInternal(Long refundId, String batch, boolean consolidateSameAccountRefunds) {
        // Get the Refund object:
        Refund refund = getRefund(refundId, true);

        // Check the refund is of an ACH type. Compare the valueof the system setting to RefundType:
        String refundTypeId = refund.getRefundType().getDebitTypeId();
        String systemPropRefundType = configService.getInitialParameter(Constants.REFUND_ACH_TYPE);

        if (!StringUtils.equalsIgnoreCase(refundTypeId, systemPropRefundType)) {
            throw new InvalidRefundTypeException("Invalid Refund type for an Ach refund. Refund type is [" + refundTypeId + "].");
        }

        // Check that the account in question has bank type listed under protected information type of type refund.ach.banktype available.
        // If so, get the bank information.
        String accountId = refund.getTransaction().getAccountId();
        Ach ach = null;

        try {
            ach = accountService.getAch(accountId);
        } catch (Exception e) {/*ignored. null value of "ach" assumed*/}

        if (ach == null) {
            // Mark refund as FAILED:
            refund.setStatus(RefundStatus.FAILED);
            persistEntity(refund);

            throw new NoAchInformationException("No ACH information found for account " + accountId);
        }

        // Perform either an individual or consolidate refund:
        List<Refund> allDueRefunds = new ArrayList<Refund>();
        String systemName = configService.getInitialParameter(Constants.REFUND_ACH_SYSTEM_NAME);
        String achReference;
        BigDecimal amount;

        // Add the original Refund to the list of all Refunds:
        allDueRefunds.add(refund);

        if (consolidateSameAccountRefunds) {
            // Find all VALIDATED Ach Refunds for the same account, EXCEPT for the current Refund:
            String sql = "select r from Refund r where r.status = :refundStatus and r.transaction.accountId = :accountId and r.refundType.debitTypeId = :refundType and r.id <> :id";
            Query query = em.createQuery(sql)
                    .setParameter("refundStatus", RefundStatus.VERIFIED)
                    .setParameter("accountId", accountId)
                    .setParameter("refundType", refundTypeId)
                    .setParameter("id", refund.getId());
            String refundGroup = UUID.randomUUID().toString();

            allDueRefunds.addAll(query.getResultList());
            amount = new BigDecimal(0);
            achReference = refundGroup;

            // Get the new Rollup:
            String achRoolupCode = configService.getInitialParameter(Constants.REFUND_ACH_GROUP_ROLLUP);
            Rollup achRollup = getAuditableEntityByCode(achRoolupCode, Rollup.class);

            // Sum all due Refunds into one Ach transmission and perform refund:
            for (Refund dueRefund : allDueRefunds) {
                // Add the current refund amount to the total:
                amount = amount.add(dueRefund.getAmount());

                // Perform refund:
                dueRefund = performRefundInternal(dueRefund.getId(), batch, null);
                dueRefund.setSystem(systemName);
                dueRefund.setRefundGroup(refundGroup);
                dueRefund.getRefundTransaction().setRollup(achRollup);
                persistEntity(dueRefund.getRefundTransaction());
            }
        } else {
            // Produce a single refund Ach transmission:
            amount = refund.getAmount();
            achReference = refundId.toString();

            // Perform refund:
            refund = performRefundInternal(refund.getId(), batch, null);
            refund.setSystem(systemName);
        }

        // Produce an ACH transmission:
        Ach achTransmission = produceAchTransmissionInternal(amount, achReference, ach);

        // Add Refund Manifest:
        addAchRefundManifest(allDueRefunds, achTransmission);

        return achTransmission;
    }

    /**
     * Performs a Check refund. Optionally, consolidates all refunds for the same Account into one refund check.
     *
     * @param refundId                      Id of a Refund to perform a check refund on.
     * @param batch                         Batch ID.
     * @param checkDate                     Date of the refund check.
     * @param checkMemo                     Memo section on the refund check.
     * @param consolidateSameAccountRefunds Whether to consolidate refund checks into one large sum.
     * @return Refund check as a JAXB object.
     */
    private Check doCheckRefundInternal(Long refundId, String batch, Date checkDate, String checkMemo, boolean consolidateSameAccountRefunds) {
        // Get the Refund object:
        Refund refund = getRefund(refundId, true);

        // Check that this refund of of a Check type. Compare the value of the system setting to RefundType:
        String refundTypeId = refund.getRefundType().getDebitTypeId();
        String systemPropRefundType = configService.getInitialParameter(Constants.REFUND_CHECK_TYPE);

        if (!StringUtils.equalsIgnoreCase(refundTypeId, systemPropRefundType)) {
            throw new InvalidRefundTypeException("Invalid Refund type for a Check refund. Refund type is [" + refundTypeId + "].");
        }

        // Perform either an individual refund or a consolidated check refund:
        List<Refund> allDueRefunds = new ArrayList<Refund>();
        String systemName = configService.getInitialParameter(Constants.REFUND_CHECK_SYSTEM_NAME);
        String checkId;
        BigDecimal amount;

        // Add the original refund to the list of all Refunds:
        allDueRefunds.add(refund);

        // Work on Refunds. Check if we need to consolidate refund amounts:
        if (consolidateSameAccountRefunds) {
            // Find all VALIDATED cash/check Refunds for the same account, EXCEPT for the current Refund:
            String accountId = refund.getTransaction().getAccountId();
            String sql = "select r from Refund r where r.status = :refundStatus and r.transaction.accountId = :accountId and r.refundType.debitTypeId = :refundType and r.id <> :id";
            Query query = em.createQuery(sql)
                    .setParameter("refundStatus", RefundStatus.VERIFIED)
                    .setParameter("accountId", accountId)
                    .setParameter("refundType", refundTypeId)
                    .setParameter("id", refund.getId());

            allDueRefunds.addAll(query.getResultList());
            checkId = UUID.randomUUID().toString();
            amount = new BigDecimal(0);

            // Get the new Rollup:
            String checkRoolupCode = configService.getInitialParameter(Constants.REFUND_CHECK_GROUP_ROLLUP);
            Rollup checkRollup = getAuditableEntityByCode(checkRoolupCode, Rollup.class);

            // Sum all due Refunds into one check and perform refund:
            for (Refund dueRefund : allDueRefunds) {
                // Add the current refund amount to the total:
                amount = amount.add(dueRefund.getAmount());

                // Perform refund:
                dueRefund = performRefundInternal(dueRefund.getId(), batch, null);
                dueRefund.setSystem(systemName);
                dueRefund.setRefundGroup(checkId);
                dueRefund.getRefundTransaction().setRollup(checkRollup);
                persistEntity(dueRefund.getRefundTransaction());
            }
        } else {
            // Produce a single refund check:
            checkId = refundId.toString() + ObjectUtils.toString(batch);
            amount = refund.getAmount();

            // Perform refund:
            refund = performRefundInternal(refund.getId(), batch, null);
            refund.setSystem(systemName);
        }

        // Produce an XML check:
        Account refundAccount = refund.getTransaction().getAccount();
        String payee = refundAccount.getDefaultPersonName().getDisplayValue();
        PostalAddress address = refundAccount.getDefaultPostalAddress();
        Check check = produceCheckInternal(checkId, payee, address, amount, checkDate, checkMemo);

        // Add Refund Manifest:
        addCheckRefundManifest(allDueRefunds, checkId);

        return check;
    }

    /**
     * Creates a <code>RefundManifest</code> for an Account refund and adds it to the specified <code>Refund</code>.
     *
     * @param refund            A <code>Refund</code> for which to add an Account refund manifest.
     * @param accountId         ID of an Account into which a refund was made.
     * @param refundTransaction Transaction that actually refunded the funds.
     */
    private void addAccountRefundManifest(Refund refund, String accountId, Transaction refundTransaction) {
        // Create an Account refund Manifest and add it to the Refund:
        RefundManifest refundManifest = new RefundManifest();
        Account refundAccount = accountService.getOrCreateAccount(accountId);

        refundManifest.setRefundAccount(refundAccount);
        refundManifest.setRefundTransaction(refundTransaction);
        persistEntity(refundManifest);
        refund.setRefundManifest(refundManifest);
        persistEntity(refund);
    }

    /**
     * Creates a <code>RefundManifest</code> for a Check refund and adds it to the specified <code>Refund</code>.
     *
     * @param allDueRefunds   Either a single or grouped <code>Refund</code> objects.
     * @param checkIdentifier Refund check identifier.
     */
    private void addCheckRefundManifest(List<Refund> allDueRefunds, String checkIdentifier) {
        // Create a Check refund manifest:
        RefundManifest refundManifest = new RefundManifest();

        refundManifest.setCheckIdentifier(checkIdentifier);
        persistEntity(refundManifest);

        // Add the manifest all Refunds and persist them:
        for (Refund refund : allDueRefunds) {
            refund.setRefundManifest(refundManifest);
            persistEntity(refund);
        }
    }

    /**
     * Creates a <code>RefundManifest</code> for a Bank transfer refund and adds it to the specified <code>Refund</code>.
     *
     * @param allDueRefunds   Either a single or grouped <code>Refund</code> objects.
     * @param achTransmission Bank transmission that defined the refund.
     */
    private void addAchRefundManifest(List<Refund> allDueRefunds, Ach achTransmission) {
        // Create an Ach refund manifest:
        RefundManifest refundManifest = new RefundManifest();
        String bankTransferIdentifier = achTransmission.getReference();

        refundManifest.setBankTransferIdentifier(bankTransferIdentifier);
        persistEntity(refundManifest);

        // Add the manifest all Refunds and persist them:
        for (Refund refund : allDueRefunds) {
            refund.setRefundManifest(refundManifest);
            persistEntity(refund);
        }
    }

    /**
     * Finds a <code>Refund</code> object. Optionally, asserts that it's in the VERIFIED state.
     *
     * @param refundId      ID of a <code>Refund</code> to be located.
     * @param checkVerified Flag to check if the <code>Refund</code> is in the VERIFIED state.
     * @return The <code>Refund</code> object with the matching ID.
     * @throws RefundNotFoundException    If no such <code>Refund</code> is found by the given ID.
     * @throws RefundNotVerifiedException If <code>checkVerified</code> flag is <code>true</code>
     *                                    and the <code>Refund</code> has a status different than {@link RefundStatus#VERIFIED}
     */
    private Refund getRefund(Long refundId, boolean checkVerified) {
        // Get the Refund object by its identifier:
        Refund refund = getEntity(refundId, Refund.class);

        if (refund == null) {
            throw new RefundNotFoundException(refundId, "Refund with ID [" + refundId + "] was found in the system.");
        }

        // Check the status of the Refund:
        if (checkVerified) {
            RefundStatus refundStatus = refund.getStatus();

            if (refundStatus != RefundStatus.VERIFIED) {
                throw new RefundNotVerifiedException("Refund with ID [" + refundId + "] is not verified.");
            }
        }

        return refund;
    }

    /**
     * A utility method that performs refund and accepts an Account ID optionally.
     *
     * @param refundId  Refund ID.
     * @param batch     Batch ID.
     * @param accountId Optional Account ID. If null, the original Transaction's Account ID is used.
     * @return The Refund object.
     */
    private Refund performRefundInternal(Long refundId, String batch, String accountId) {
        // Get the Refund object and check that it's in the VERIFIED status:
        Refund refund = getRefund(refundId, true);

        // Create a charge transaction on the account referenced through the original transaction
        // Of type referenced in the refundType, of the amount of the refund:
        Transaction originalTransaction = refund.getTransaction();
        Account account = originalTransaction.getAccount();
        String userId = StringUtils.isNotBlank(accountId) ? accountId : account.getId();
        String refundTransactionTypeId = refund.getRefundType().getDebitTypeId();
        Date effectiveDate = new Date();
        BigDecimal amount = refund.getAmount();
        Transaction refundTransaction = transactionService.createTransaction(refundTransactionTypeId, userId, effectiveDate, amount);

        // Create a lockedAllocation between the refund and the original transaction in the amount of the refund:
        Allocation allocation = new Allocation();

        allocation.setAccount(account);
        allocation.setFirstTransaction(originalTransaction);
        allocation.setSecondTransaction(refundTransaction);
        allocation.setAmount(amount);
        allocation.setLocked(true);
        persistEntity(allocation);

        // Update the Refund object with the modified values:
        refund.setRefundDate(effectiveDate);
        refund.setRefundTransaction(refundTransaction);
        refund.setStatus(RefundStatus.REFUNDED);

        if (StringUtils.isNotBlank(batch)) {
            refund.setBatchId(batch);
        }

        persistEntity(refund);

        return refund;
    }

    /**
     * Creates a Cash type Refund.
     *
     * @param payment     Original payment.
     * @param requestDate Date the new <code>Refund</code> is requested. Defaults to current date.
     * @param requestedBy User who requested the refund. Defaults to the current system user.
     * @return A newly created <code>Refund</code>.
     */
    private Refund createCashRefund(Payment payment, Date requestDate, Account requestedBy) {
        // Figure out the refund type method. Get the overriden value first:
        String refundTypeMethod = configService.getInitialParameter(Constants.OVERRIDE_REFUND_METHOD);

        if (StringUtils.isBlank(refundTypeMethod)) {
            // If there is no overriden value, get the configured value:
            refundTypeMethod = configService.getInitialParameter(Constants.REFUND_METHOD);

            // If there is still no value, use the User preference:
            if (StringUtils.isBlank(refundTypeMethod)) {
                refundTypeMethod = userPreferenceService.getUserPreferenceValue(requestedBy.getId(), Constants.DEFAULT_REFUND_METHOD);
            }
        }

        // If there is a valid Refund type, create a new Refund:
        if (StringUtils.isNotBlank(refundTypeMethod)) {
            // Create a new Refund:
            Refund refund = new Refund();
            RefundType refundType = getOrCreateRefundType(refundTypeMethod, refundTypeMethod);
            BigDecimal refundAmount = TransactionUtils.getUnallocatedAmount(payment);

            refund.setAmount(refundAmount);
            refund.setRequestDate(requestDate);
            refund.setRequestedBy(requestedBy);
            refund.setTransaction(payment);
            refund.setRefundType(refundType);
            persistEntity(refund);

            return refund;
        } else {
            throw new InvalidRefundTypeException("No default Refund Type in system configuration or User Preferences.");
        }
    }

    /**
     * Creates a <code>Refund</code> to the original payment source.
     *
     * @param payment     Original payment.
     * @param requestDate Date the new <code>Refund</code> is requested. Defaults to current date.
     * @param requestedBy User who requested the refund. Defaults to the current system user.
     * @param refundRule  Refund rule from the original payment.
     * @return A newly created <code>Refund</code>.
     */
    private Refund createSourceRefund(Payment payment, Date requestDate, Account requestedBy, String refundRule) {
        // Create a new Refund:
        String refundTypeMethod = configService.getInitialParameter(Constants.REFUND_SOURCE_TYPE);
        RefundType refundType = getOrCreateRefundType(refundTypeMethod, refundTypeMethod);
        BigDecimal refundAmount = TransactionUtils.getUnallocatedAmount(payment);
        Refund refund = new Refund();

        refund.setAmount(refundAmount);
        refund.setRequestDate(requestDate);
        refund.setRequestedBy(requestedBy);
        refund.setTransaction(payment);
        refund.setRefundType(refundType);

        // If an Account refund, set the "attribute" to the Account ID:
        if (StringUtils.startsWith(refundRule, "A")) {
            String refundAccountId = StringUtils.substringsBetween(refundRule, "(", ")")[1];

            refund.setAttribute(refundAccountId);
        }

        persistEntity(refund);

        return refund;
    }


    /**
     * Creates an <code>Activity</code> that records an occurrence of an invalid <code>Account</code>
     *
     * @param accountId An invalid <code>Account</code> identifier.
     */
    private void createInvalidAccountActivity(String accountId) {
        Activity activity = new Activity();
        String currentUserId = userSessionManager.getUserId(RequestUtils.getThreadRequest());

        activity.setAccountId(accountId);
        activity.setEntityId(accountId);
        activity.setEntityType(Account.class.getSimpleName());
        activity.setCreatorId(currentUserId);
        activity.setIpAddress("127.0.0.1");
        activity.setLogDetail("Invalid account identifier [" + accountId + "] in processing Account Refunds.");
        activity.setTimestamp(new Date());

        persistEntity(activity);
    }

    /**
     * Produces an Ach transmission using the specified parameters.
     *
     * @param amount    Amount of transmission.
     * @param reference Reference info.
     * @param ach       Original Ach.
     * @return Newly created Ach transmission.
     */
    private Ach produceAchTransmissionInternal(BigDecimal amount, String reference, Ach ach) {
        // Validate the Amount:
        if (amount.intValue() == 0) {
            throw new InvalidAchAmountException("ACH transmission amount is zero.");
        }

        // Create a new Ach:
        Ach achTransmission = new Ach();

        achTransmission.setReference(reference);
        achTransmission.setAmount(amount);
        achTransmission.setRoutingNumber(ach.getRoutingNumber());
        achTransmission.setAccountNumber(ach.getAccountNumber());
        achTransmission.setAccountType(ach.getAccountType());

        return achTransmission;
    }

    /**
     * Creates a JAXB Check object.
     *
     * @param identifier    Check identifier.
     * @param payee         Payee
     * @param postalAddress Postal address on check.
     * @param amount        Amount of check to produce.
     * @param checkDate     Date on check.
     * @param memo          Memo section on check.
     * @return A new Check object.
     */
    private Check produceCheckInternal(String identifier, String payee, PostalAddress postalAddress, BigDecimal amount, Date checkDate, String memo) {
        // Create a new Check object:
        Check check = new Check();

        check.setIdentifier(identifier);
        check.setPayee(payee);
        check.setPostalAddress(convertToXMLAddress(postalAddress));
        check.setAmount(amount);
        check.setMemo(StringUtils.isNotBlank(memo) ? memo : null);
        check.setDate(produceXMLCheckDate(checkDate));

        return check;
    }


    /**
     * Converts a persistent entity <code>PostalAddress</code> into an XML type.
     *
     * @param address A persistent entity.
     * @return XML type.
     */
    private com.sigmasys.kuali.ksa.transform.PostalAddress convertToXMLAddress(PostalAddress address) {
        // Create a new XML Postal Address:
        com.sigmasys.kuali.ksa.transform.PostalAddress xmlAddress = new com.sigmasys.kuali.ksa.transform.PostalAddress();

        xmlAddress.setAddressLine1(address.getStreetAddress1());
        xmlAddress.setAddressLine2(address.getStreetAddress2());
        xmlAddress.setAddressLine3(address.getStreetAddress3());
        xmlAddress.setCity(address.getCity());
        xmlAddress.setCountryCode(address.getCountry());
        xmlAddress.setPostalCode(address.getPostalCode());
        xmlAddress.setStateCode(address.getState());

        return xmlAddress;
    }

    /**
     * Produces an XML type of a date from a check date. Discourages future checks converting them to today's date.
     *
     * @param checkDate Proposed check date.
     * @return XML check date no earlier than today.
     */
    private XMLGregorianCalendar produceXMLCheckDate(Date checkDate) {
        // If a future date, change to the current date:
        Date dateOnCheck = (checkDate.getTime() < System.currentTimeMillis()) ? checkDate : new Date();

        // Create a new calendar and convert to an XML calendar:
        GregorianCalendar c = new GregorianCalendar();

        c.setTime(dateOnCheck);

        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (Exception e) {/* ignored. returning null */}

        return null;
    }
}