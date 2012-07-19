package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.model.Pair;
import com.sigmasys.kuali.ksa.model.SortOrder;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

/**
 * Currency service JPA implementation.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("currencyService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class CurrencyServiceImpl extends GenericPersistenceService implements CurrencyService {

    /**
     * Returns Currency by ID
     *
     * @param id Currency ID
     * @return Currency instance
     */
    @Override
    public Currency getCurrency(Long id) {
        return getEntity(id, Currency.class);
    }

    /**
     * Returns Currency by ISO symbol
     *
     * @param code ISO currency code
     * @return Currency instance
     */
    @Override
    public Currency getCurrency(String code) {
        Query query = em.createQuery("select c from Currency c where upper(c.code) = upper(:code)");
        query.setParameter("code", code);
        List<Currency> currencies = query.getResultList();
        if (currencies != null && !currencies.isEmpty()) {
            return currencies.get(0);
        }
        throw new IllegalArgumentException("Currency with ISO = '" + code + "' does not exist");
    }

    /**
     * Returns all currencies sorted by ISO in the ascending order
     *
     * @return List of currencies
     */
    @Override
    public List<Currency> getCurrencies() {
        return getEntities(Currency.class, new Pair<String, SortOrder>("code", SortOrder.ASC));
    }

    /**
     * Persists the currency in the database.
     * Creates a new entity when ID is null and updates the existing one otherwise.
     *
     * @param currency Currency instance
     * @return Currency ID
     */
    @Override
    @Transactional(readOnly = false)
    public Long persistCurrency(Currency currency) {
        return persistEntity(currency);
    }

    /**
     * Removes the currency from the database.
     *
     * @param id Currency ID
     * @return true if the Currency entity has been deleted
     */
    @Override
    @Transactional(readOnly = false)
    public boolean deleteCurrency(Long id) {
        return deleteEntity(id, Currency.class);
    }

}
