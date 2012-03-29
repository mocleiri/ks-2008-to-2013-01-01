package com.sigmasys.kuali.ksa.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * Transaction is an abstract class, used to generate different types of transactions within the system.
 *
 * @author Paul Heald, Michael Ivanov
 */
@Entity
@Table(name = "KSSA_TRANSACTION")
@DiscriminatorColumn(name = "TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Transaction implements Identifiable {

    /**
     * The unique transaction identifier for the KSA product.
     */
    protected Long id;

    /**
     * Transaction type. For example, DEBIT or CREDIT
     */
    protected TransactionType transactionType;

    /**
     * the rollup is an institutionally specified group of transactions that are rolled up on the initial view of an account, if the transactions fall in to a specified academic
     * time period. For example, setting this flag on all tuition charges to TUITION would cause all tuition charges to roll up into a single line. If a school charges mandatory fees
     * as single transactions, this could also be rolled up in this way. This can be set on a per-charge basis, or can be pulled from the default in the TransactionType.
     */
    protected Rollup rollup;

    /**
     * the identifier of the transaction as generated by another external system. If the external system does not generate a transaction identifier, this will be the same as the internal transaction identifier. For credits, this might take the form of the credit card authorization, etc.
     */
    protected String externalId;

    /**
     * This is the date that the transaction is entered in the ledger. It is set when the transaction is instantiated.
     */
    protected Date ledgerDate;

    /**
     * This is the date that the transaction is considered effective, allowing, for example, a college to assess fees in summer that are not 'due' until the fall. It is also the date that payment application will use to order the transactions. This date is also used as the defining date for a transaction if its GL code has multiple effective accounts.
     */
    protected Date effectiveDate;

    /**
     * This is the date that the transaction originated, so, for example, housing might bill on a certain day, but upload its transactions only once a week. The purpose of this date is purely for tracking transactions from external systems.
     */
    protected Date originationDate;

    /**
     * This is the value of the transaction in the system currency. This is the number that is used as the core value of the transaction. All calculations are performed against this number.
     */
    protected BigDecimal amount;

    /**
     * This is the native amount of the transaction in the currency in which it was created. For most transactions, this will be identical to the localAmount, as most transactions occur in the local currency.
     */
    protected BigDecimal nativeAmount;

    /**
     * This is the identifier for the currency that is used in nativeAmount. Most often, it will be set to the same value as the system currency. This is a 3-letter ISO4217 code. For example, USD, GBP, EUR.
     */
    protected Currency currency;

    /**
     * This flag, when true, indicates that the transaction is 'internal', and will not, in most cases, be displayed to the customer. In most cases, internal transactions will be allocated and locked against an equivalent transaction. An example of this would be if a charge is incorrectly applied to an account, a reversal transaction would be created, the transactions could then be allocated together, and marked as internal.
     */
    protected Boolean internal;

    /**
     * This is the amount of the transaction that has been allocated. For example, if a $1000 payment is put towards a $2000 charge, the $1000 will have a $1000 allocation amount, and the $2000 charge will have a $1000 allocation amount. The PA module is responsible for allocating charges to payments.
     */
    protected BigDecimal allocatedAmount;

    /**
     * This value is set to a value (up to the maximum of localAmount) and is the value of the transaction that has been allocated, and may not be reallocated by the payment application routine. Most commonly, the entire transaction will be locked, by setting the lockedAllocationAmount to the localValue.
     * Schools will also likely lock their allocations of transactions before the current period of time (for example, if an allocation was made with a payment in a pervious year, it will not
     * be allowed to be deallocated by the system.
     */
    protected BigDecimal lockedAllocatedAmount;

    /**
     * The responsible entity is the entity that created the transaction. It is assumed the identity will be derived from KIM.
     */
    protected String responsibleEntity;

    /**
     * The text for the transaction that will be displayed as a summary of the transaction on statements. For example "Bookstore Purchase"  If this string is null during instantiation, the default text will
     * be populated from the transaction code.
     */
    protected String statementText;

    /**
     * A reference to an external document that gives more granular information about the transaction. This information will be different depending on the type of transaction. The information will be available when a user "drills down" into a transaction. It will contain information relating to the charge or payment, such as the contact details for the department who generated a charge, and other information as made available by the system. For example, a charge from the bookstore may list the items purchased. The document referenced will be an XML document, allowing flexible creation of different document types by the institutions themselves. Some basic document types will be defined by the project.
     */
    protected Document document;

    /**
     * Reference to the corresponding account
     */
    private Account account;


    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @TableGenerator(name = "TABLE_GEN_TRAN",
            table = "SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "TRANSACTION_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN_TRAN")
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID"),
            @JoinColumn(name = "TYPE_SUB_CODE", referencedColumnName = "SUB_CODE")
    })
    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Column(name = "EXTN_ID", length = 45)
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Column(name = "LEDGER_DATE")
    public Date getLedgerDate() {
        return ledgerDate;
    }

    public void setLedgerDate(Date ledgerDate) {
        this.ledgerDate = ledgerDate;
    }

    @Column(name = "EFFECTIVE_DATE")
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Column(name = "ORIG_DATE")
    public Date getOriginationDate() {
        return originationDate;
    }

    public void setOriginationDate(Date originationDate) {
        this.originationDate = originationDate;
    }

    @Column(name = "NATIVE_AMNT")
    public BigDecimal getNativeAmount() {
        return nativeAmount;
    }

    public void setNativeAmount(BigDecimal nativeAmount) {
        this.nativeAmount = nativeAmount;
    }

    @Column(name = "LOCKED_ALLOCATED")
    public BigDecimal getLockedAllocatedAmount() {
        return lockedAllocatedAmount;
    }

    public void setLockedAllocatedAmount(BigDecimal lockedAllocatedAmount) {
        this.lockedAllocatedAmount = lockedAllocatedAmount;
    }

    @Column(name = "RESP_ENTITY", length = 45)
    public String getResponsibleEntity() {
        return responsibleEntity;
    }

    public void setResponsibleEntity(String responsibleEntity) {
        this.responsibleEntity = responsibleEntity;
    }

    @Column(name = "STATEMENT_TXT", length = 100)
    public String getStatementText() {
        return statementText;
    }

    public void setStatementText(String statementText) {
        this.statementText = statementText;
    }

    /**
     * This method can set the internal switch on or off for a transaction. I cannot think of a time when an internal transaction would
     * not also be locked (tbd) so we should ensure that if a transaction is being set to internal, its lockedAllocationAmount == ledgerAmount.
     * Internal transactions are not generally displayed to the user, and are only displayed to the CSR when asked for.
     *
     * @param internal true/false
     */
    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    @Column(name = "IS_INTERNAL_TRN", length = 1)
    public Boolean isInternal() {
        return internal;
    }

    /**
     * If a document reference is altered, then a new one can be re-referenced. The documents associated with each transaction give more details about the transaction, and are
     * for information for both the student and the CSR. For example, if the bookstore wanted to, it could lists the books it had sold in a transaction so the student would be
     * able to drill down and see what books made up a line on their statement.
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCUMENT_ID_FK")
    public Document getDocument() {
        return document;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEF_ROLLUP_ID_FK")
    public Rollup getRollup() {
        return rollup;
    }

    public void setRollup(Rollup rollup) {
        this.rollup = rollup;
    }


    @Column(name = "AMNT")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_ID_FK")
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Column(name = "ALLOCATED")
    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACNT_ID_FK")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
	


