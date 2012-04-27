package com.sigmasys.kuali.ksa.gwt.server;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.sigmasys.kuali.ksa.gwt.client.model.GwtError;
import com.sigmasys.kuali.ksa.gwt.client.model.PagingLoadResultImpl;
import com.sigmasys.kuali.ksa.gwt.client.model.SearchCriteria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * AbstractSearchService
 *
 * @author Michael Ivanov
 */
@SuppressWarnings("unchecked")
public abstract class AbstractSearchService extends AbstractRemoteService {

    protected static final int UNLIMITED_ITEMS_NUMBER = -1;

    protected static final Log logger = LogFactory.getLog(AbstractSearchService.class);

    /**
     * It is supposed to be overriden by subclasses to support search.
     *
     * @return EntityManager instance
     */
    protected abstract EntityManager getEntityManager();


    protected <T> List<T> populateListFromResult(List<T> result) {
        return result;
    }

    protected int countEntities(SearchCriteria searchCriteria,
                                                   SearchQueryBuilder queryBuilder) throws GwtError {

        SearchQueryBuilder builder = queryBuilder.copy();

            String hql = builder.buildCountQuery(searchCriteria);

        logger.debug("count query = " + hql);

        Query query = getQuery(builder, hql);

            Object countResult = query.getSingleResult();
            return  countResult instanceof BigDecimal ? ((BigDecimal) countResult).intValue() :
                    ((Long) countResult).intValue();

    }

    protected <T> List<T> searchEntities(SearchQueryBuilder queryBuilder, SearchCriteria sc, String sortDir,
                                         String sortField, int offset, int limit) throws GwtError {
        return searchEntities(queryBuilder, sc, sortDir, sortField, offset, limit, false);
    }
    
    protected <T> List<T> searchEntities(SearchQueryBuilder queryBuilder, SearchCriteria sc, String sortDir,
            String sortField, int offset, int limit, boolean readOnly) throws GwtError {


        String hql = queryBuilder.buildQuery(sc, sortDir, sortField);
        logger.debug("searchEntities HQL:" + hql);
        Query query = getQuery(queryBuilder, hql);

        if (limit != UNLIMITED_ITEMS_NUMBER || !sc.isUnlimitedRows()) {

            if (!sc.isUnlimitedRows()) {
                if (limit == UNLIMITED_ITEMS_NUMBER) {
                    limit = sc.getMaxRows();
                } else {
                    // will not move further then searchCriteria.getMaxRows number of records
                    offset = Math.max(0, Math.min(offset, sc.getMaxRows() - limit));
                }
            }

            query.setFirstResult(offset);
            query.setMaxResults(limit);
        }
        
        if (readOnly) {
            query.setHint("org.hibernate.readOnly", true);
        }

        long statsBegin = System.currentTimeMillis();

        List<T> result = populateListFromResult(query.getResultList());
        
        logger.info("searchEntities done in: " + (System.currentTimeMillis() - statsBegin) + " ms");

        return result;
    }

    protected Query getQuery(SearchQueryBuilder queryBuilder, String hql) {
        EntityManager em = getEntityManager();
        return queryBuilder.isUseNativeSql() ? em.createNativeQuery(hql) : em.createQuery(hql);
    }

    protected <T> List<T> searchEntities(SearchQueryBuilder queryBuilder, SearchCriteria searchCriteria) throws GwtError {
        return searchEntities(queryBuilder, searchCriteria, null, null, 0, UNLIMITED_ITEMS_NUMBER);
    }

    protected <T> PagingLoadResult<T> buildPagingLoadResult(SearchQueryBuilder queryBuilder, SearchCriteria sc, String sortDir,
            String sortField, int offset, int limit) throws GwtError {
        return buildPagingLoadResult(queryBuilder, sc, sortDir, sortField, offset, limit, false);
    }
    
    protected <T> PagingLoadResult<T> buildPagingLoadResult(SearchQueryBuilder queryBuilder, SearchCriteria sc, String sortDir,
                                                            String sortField, int offset, int limit, boolean readOnly) throws GwtError {
        int numberOfItems = 0;
        if (sc.isRunCount()) {
            numberOfItems = countEntities(sc, queryBuilder);
            logger.info("numberOfItems = " + numberOfItems);
            int numberOfPages = numberOfItems / limit;
            logger.info("numberOfPages = " + numberOfPages);
            logger.info("offset before adjustment = " + offset);
            while (numberOfItems <= offset && --numberOfPages >= 0) {
                offset = numberOfPages * limit;
            }
        }
        logger.info("offset after adjustment = " + offset);
        List<T> items = searchEntities(queryBuilder, sc, sortDir, sortField, offset, limit, readOnly);
        return (sc.isRunCount()) ?
                new PagingLoadResultImpl(items, offset, numberOfItems) : new PagingLoadResultImpl(items);
    }
}
