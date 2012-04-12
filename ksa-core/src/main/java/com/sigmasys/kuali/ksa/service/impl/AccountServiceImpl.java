package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * Currency service JPA implementation.
 * <p/>
 *
 * @author Tim Bornholtz
 */
@Service("accountService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class AccountServiceImpl extends GenericPersistenceService implements AccountService {

    private static final Log logger = LogFactory.getLog(AccountServiceImpl.class);

    @Override
    public void rebalance(Boolean ignoreDeferment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void ageDebt(Boolean ignoreDeferment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BigDecimal getDueBalance(Boolean ignoreDeferment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BigDecimal getOutstandingBalance(Boolean ignoreDeferment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BigDecimal getUnallocatedBalance() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BigDecimal getDeferredAmount() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void makeEffective() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void paymentApplication() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createAllocation(Transaction transaction1, Transaction transaction2, BigDecimal allocationAmount) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createLockedAllocation(Transaction transaction1, Transaction transaction2, BigDecimal allocationAmount) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This method is used to verify that an account exists before a transaction or other operations are
     * performed on the account. There is an initial inquiry into the KSA store. If no account exist, then there is
     * an inquiry into KIM. If KIM also returns no result, then false is returned. If a KIM account does exist, then
     * a KSA account is created, using the KIM information as a template.
     *
     * @param accountId
     * @return the account instance or null if the account does not exist
     */
    @Override
    public Account getAccount(String accountId) {
        Account account = getEntity(accountId, Account.class);
        if (account == null) {
            PersonService personService = KimApiServiceLocator.getPersonService();
            Person person = personService.getPersonByPrincipalName(accountId);
            if (person == null) {
                String errMsg = "The user '" + person + "' does not exist";
                logger.error(errMsg);
                throw new IllegalStateException(errMsg);
            }
            // If the person exists in KIM we have to create a new KSA account based on that
            account = createAccount(person);
        }
        return account;
    }

    private Account createAccount(Person person) {

        // TODO - populate the missing fields
        // TODO: figure out how to distinguish Delegate and DirectCharge account types

        Account account = new DirectChargeAccount();
        account.setId(person.getPrincipalName());
        account.setCreationDate(new Date());
        account.setAbleToAuthenticate(true);
        account.setEntityId(person.getEntityId());
        account.setKimAccount(true);

        PersonName personName = new PersonName();
        personName.setCreatorId("system");
        personName.setLastUpdate(new Date());
        personName.setDefault(true);
        personName.setFirstName(person.getFirstName());
        personName.setMiddleName(person.getMiddleName());
        personName.setLastName(person.getLastName());
        personName.setKimNameType(person.getEntityTypeCode());
        personName.setDefault(true);

        // Making PersonName persistent and generate ID
        personName = persistEntity(personName);

        PostalAddress address = new PostalAddress();
        address.setCreatorId("system");
        address.setLastUpdate(new Date());
        address.setDefault(true);
        address.setPostalCode(person.getAddressPostalCode());
        address.setCountry(person.getAddressCountryCode());
        address.setState(person.getAddressStateProvinceCode());
        // TODO: no city ??
        address.setStreetAddress1(person.getAddressLine1());
        address.setStreetAddress2(person.getAddressLine2());
        address.setStreetAddress3(person.getAddressLine3());

        // Making PostalAddress persistent and generate ID
        address = persistEntity(address);

        account.setPersonNames(Arrays.asList(personName));
        account.setPostalAddresses(Arrays.asList(address));

        // "Account is in good standing" (Paul) ID = 1
        AccountStatusType statusType = getEntity(1, AccountStatusType.class);
        account.setStatusType(statusType);

        // Making Account persistent
        account = persistEntity(account);

        return account;

    }
}
