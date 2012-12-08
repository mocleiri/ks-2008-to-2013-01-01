package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.transform.Ach;

import java.math.BigDecimal;
import java.util.List;

/**
 * Account service declares business operations on Student Accounts and related
 * objects
 * <p/>
 *
 * @author Michael Ivanov
 */
public interface AccountService {

    /**
     * This process creates a temporary subset of the account as if the account were being administered
     * as a balance forward account. This permits aging the account in a way that is not affected by the
     * payment application methodology. This temporary array is passed to the ageDebt() method.
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return a list of pairs [Debit, BigDecimal]
     */
    List<Pair<Debit, BigDecimal>> rebalance(String userId, boolean ignoreDeferment);

    /**
     * Aging debts for all chargeable accounts in KSA
     *
     * @param ignoreDeferment boolean value
     */
    void ageDebt(boolean ignoreDeferment);

    /**
     * Aging debts for a chargeable account.
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return a chargeable account being updated
     */
    ChargeableAccount ageDebt(String userId, boolean ignoreDeferment);

    /**
     * Returns the total balance due of all active transactions.
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return total amount of balance due
     */
    BigDecimal getDueBalance(String userId, boolean ignoreDeferment);

    /**
     * Returns the outstanding balance for the given account
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return total amount of outstanding balance
     */
    BigDecimal getOutstandingBalance(String userId, boolean ignoreDeferment);

    /**
     * Returns unallocated balance for the given Account ID
     *
     * @param userId Account ID
     * @return unallocated balance
     */
    BigDecimal getUnallocatedBalance(String userId);

    /**
     * Returns the deferred amount
     *
     * @param userId Account ID
     * @return deferred amount
     */
    BigDecimal getDeferredAmount(String userId);


    /**
     * Simple Balance Forward Only
     * <p/>
     * Notes:
     * This is a simple, balance-forward payment application routine.
     * “Allocate” requires both the transaction allocationAmount to be updated, as well as updating/instantiating
     * the Allocation objects.
     * Deferments, when created, are allocated to their charge using lockedAllocationAmount.
     * A more complex, rule-based payment application system, that takes into account payment
     * divisions, priority codes, etc. will be developed to supplement this algorithm.
     *
     * @param userId Account ID
     */
    void paymentApplication(String userId);


    /**
     * This method is used to verify that an account exists before a transaction or other operations are
     * performed on the account. There is an initial inquiry into the KSA store. If no account exists, then there is
     * an inquiry into KIM. If KIM also returns no result, then false is returned. If a KIM account does exist, then
     * a KSA account is created, using the KIM information as a template.
     *
     * @param userId Account ID
     * @return the account instance or null if the account does not exist
     */
    Account getOrCreateAccount(String userId);


    /**
     * Checks if KSA account exists
     *
     * @param userId Account ID
     * @return true if the account exists, false otherwise
     */
    boolean ksaAccountExists(String userId);

    /**
     * Checks if KSA account exists. If the KSA account does not exist, it tries to look for the existing KIM account
     * and create a new KSA account, if the account does not exist returns false, otherwise true.
     *
     * @param userId Account ID
     * @return true if the account exists, false otherwise
     */
    boolean accountExists(String userId);

    /**
     * This methods fetches Account and all its associations by account ID.
     *
     * @param userId Account ID
     * @return the account instance or null if the account does not exist
     */
    Account getFullAccount(String userId);

    /**
     * This methods fetches all KSA accounts and all their associations.
     *
     * @return the list account instances
     */
    List<Account> getFullAccounts();


    /**
     * Creates and associates a new person name object with the given Account ID.
     *
     * @param userId     Account ID
     * @param personName Person name
     * @return new PersonName instance with ID
     */
    PersonName addPersonName(String userId, PersonName personName);

    /**
     * Creates and associates a new postal address with the given Account ID.
     *
     * @param userId        Account ID
     * @param postalAddress Postal address
     * @return new PostalAddress instance with ID
     */
    PostalAddress addPostalAddress(String userId, PostalAddress postalAddress);

    /**
     * Creates and associates a new electronic contact with the given Account ID.
     *
     * @param userId            Account ID
     * @param electronicContact Electronic contact
     * @return new ElectronicContact instance with ID
     */
    ElectronicContact addElectronicContact(String userId, ElectronicContact electronicContact);

    /**
     * Get ACH looks into the AccountProtectedInformation class (which triggers a system event) to look for
     * the ACH information for the user
     *
     * @param userId Account ID
     * @return Ach for associated ID.
     */
    Ach getAch(String userId);

    /**
     * Returns the list of matching accounts for the given name pattern.
     *
     * @param namePattern Name pattern
     * @return List of Account instances
     */
    List<Account> findAccountsByNamePattern(String namePattern);

}