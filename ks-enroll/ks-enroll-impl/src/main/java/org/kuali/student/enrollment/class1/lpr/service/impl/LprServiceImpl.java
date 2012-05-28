/*
 * Copyright 2009 The Kuali Foundation Licensed under the Educational Community License, Version 1.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.opensource.org/licenses/ecl1.php Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package org.kuali.student.enrollment.class1.lpr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.student.common.util.UUIDHelper;
import org.kuali.student.enrollment.class1.lpr.dao.LprDao;
import org.kuali.student.enrollment.class1.lpr.dao.LprRosterDao;
import org.kuali.student.enrollment.class1.lpr.dao.LprRosterEntryDao;
import org.kuali.student.enrollment.class1.lpr.dao.LprTransactionDao;
import org.kuali.student.enrollment.class1.lpr.dao.LprTransactionItemDao;
import org.kuali.student.enrollment.class1.lpr.model.LprTransactionEntity;
import org.kuali.student.enrollment.class1.lpr.model.LprTransactionItemEntity;
import org.kuali.student.enrollment.class1.lpr.model.LprEntity;
import org.kuali.student.enrollment.class1.roster.model.LprRichTextEntity;
import org.kuali.student.enrollment.lpr.dto.LprTransactionInfo;
import org.kuali.student.enrollment.lpr.dto.LprTransactionItemInfo;
import org.kuali.student.enrollment.lpr.dto.LprTransactionItemResultInfo;
import org.kuali.student.enrollment.lpr.dto.LprInfo;
import org.kuali.student.enrollment.lpr.service.LprService;
import org.kuali.student.r2.common.dto.BulkStatusInfo;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.RichTextInfo;
import org.kuali.student.r2.common.dto.StatusInfo;
import org.kuali.student.r2.common.dto.ValidationResultInfo;
import org.kuali.student.r2.common.exceptions.AlreadyExistsException;
import org.kuali.student.r2.common.exceptions.DataValidationErrorException;
import org.kuali.student.r2.common.exceptions.DisabledIdentifierException;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.exceptions.ReadOnlyException;
import org.kuali.student.r2.common.util.constants.LprServiceConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sambit
 */
//@Transactional(readOnly = true, noRollbackFor = {DoesNotExistException.class}, rollbackFor = {Throwable.class})
public class LprServiceImpl implements LprService {

    private LprDao lprDao;
    private LprTransactionDao lprTransDao;
    private LprTransactionItemDao lprTransItemDao;

    public void setLprTransItemDao(LprTransactionItemDao lprTransItemDao) {
        this.lprTransItemDao = lprTransItemDao;
    }

    public LprTransactionDao getLprTransDao() {
        return lprTransDao;
    }

    public void setLprTransDao(LprTransactionDao lprTransDao) {
        this.lprTransDao = lprTransDao;
    }

    public LprDao getLprDao() {
        return lprDao;
    }

    public void setLprDao(LprDao lprDao) {
        this.lprDao = lprDao;
    }
  
    private List<LprInfo> getLprsByLuiPersonAndState(String personId, String luiId, String stateKey, ContextInfo context) throws
            DoesNotExistException, DisabledIdentifierException, InvalidParameterException, MissingParameterException,
            OperationFailedException, PermissionDeniedException {
        List<LprInfo> lprs = this.getLprsByPersonAndLui(personId, luiId, context);
        List<LprInfo> list = new ArrayList<LprInfo>(lprs.size());
        for (LprInfo lpr : lprs) {
            if (lpr.getStateKey().equals(stateKey)) {
                list.add(lpr);
            }
        }
        return list;
    }
    /*
    @Transactional
    private String createLprFromLprTransactionItem(LprTransactionItemInfo lprTransactionItemInfo, ContextInfo context) throws AlreadyExistsException, DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
    LprInfo luiPersonRelation = new LprInfo();
    luiPersonRelation.setCommitmentPercent(100.00F);
    luiPersonRelation.setEffectiveDate(new Date());
    luiPersonRelation.setLuiId(lprTransactionItemInfo.getNewLuiId());
    luiPersonRelation.setPersonId(lprTransactionItemInfo.getPersonId());
    luiPersonRelation.setStateKey(LprServiceConstants.REGISTERED_STATE_KEY);
    luiPersonRelation.setTypeKey(LprServiceConstants.REGISTRANT_TYPE_KEY);
    String createdLpr;
    try {
    createdLpr = createLpr(lprTransactionItemInfo.getPersonId(), lprTransactionItemInfo.getNewLuiId(), LprServiceConstants.REGISTRANT_TYPE_KEY, luiPersonRelation, context);
    
    } catch (DisabledIdentifierException e) {
    throw new OperationFailedException(e.getMessage(), e);
    } catch (ReadOnlyException e) {
    throw new OperationFailedException(e.getMessage(), e);
    }
    
    return createdLpr;
    }
     */

    private void _checkForMissingParameter(Object param, String paramName) throws MissingParameterException {
        if (null == param) {
            throw new MissingParameterException("Parameter '" + paramName + "' cannot be null");
        }
    }

    @Override
    public List<LprInfo> getLprsByLui(String luiId, ContextInfo context) throws DoesNotExistException, InvalidParameterException,
            MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<LprEntity> luiPersonRelations = lprDao.getByLuiId(luiId);
        List<LprInfo> dtos = new ArrayList<LprInfo>();
        for (LprEntity entity : luiPersonRelations) {
            dtos.add(entity.toDto());
        }
        return dtos;
    }

    @Override
    @Transactional
    public List<BulkStatusInfo> createLprsForPerson(String personId,
            String lprTypeKey,
            List<LprInfo> lprInfos,
            ContextInfo contextInfo)
            throws DataValidationErrorException,
            DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException,
            PermissionDeniedException, ReadOnlyException {
        // TODO Implement method
        return null;
    }

    @Override
    public LprInfo getLpr(String luiPersonRelationId, ContextInfo context) throws DoesNotExistException, InvalidParameterException,
            MissingParameterException, OperationFailedException, PermissionDeniedException {
        LprEntity lpr = lprDao.find(luiPersonRelationId);
        return null != lpr ? lpr.toDto() : null;
    }

    @Override
    public List<LprInfo> getLprsByIds(List<String> luiPersonRelationIds, ContextInfo context) throws DoesNotExistException,
            InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<LprInfo> lprInfos = new ArrayList<LprInfo>();
        List<LprEntity> lprEntities = lprDao.findByIds(luiPersonRelationIds);
        for (LprEntity lprEntity : lprEntities) {
            LprInfo lprInfo = lprEntity.toDto();
            lprInfos.add(lprInfo);
        }
        return lprInfos;
    }

    @Override
    public List<String> getLuiIdsByPersonAndTypeAndState(String personId, String luiPersonRelationType, String relationState, ContextInfo context) throws
            DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException,
            PermissionDeniedException {
        // TODO Kamal - THIS METHOD NEEDS JAVADOCS
        return null;
    }

    @Override
    public List<String> getPersonIdsByLuiAndTypeAndState(String luiId, String luiPersonRelationType, String relationState, ContextInfo context) throws
            DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException,
            PermissionDeniedException {
        List<String> returnVals = new ArrayList<String>();

        returnVals.addAll(lprDao.getPersonIdsByLui(luiId, luiPersonRelationType, relationState));
        return returnVals;

    }

    @Override
    public List<LprInfo> getLprsByPersonAndLui(String personId, String luiId, ContextInfo context) throws DoesNotExistException,
            InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {

        List<LprEntity> entityList = lprDao.getLprByLuiAndPerson(personId, luiId);

        List<LprInfo> infoList = new ArrayList<LprInfo>();

        if (entityList != null && !entityList.isEmpty()) {
            for (LprEntity entity : entityList) {
                infoList.add(entity.toDto());
            }

        }
        return infoList;

    }

    @Override
    public List<LprInfo> getLprsByPerson(String personId, ContextInfo context) throws DoesNotExistException,
            InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<LprEntity> entityList = lprDao.getLprsByPerson(personId);

        List<LprInfo> infoList = new ArrayList<LprInfo>();
        if (entityList != null && !entityList.isEmpty()) {
            for (LprEntity entity : entityList) {
                infoList.add(entity.toDto());
            }

        }
        return infoList;
    }

    @Override
    public List<String> searchForLprIds(QueryByCriteria criteria, ContextInfo context) throws InvalidParameterException,
            MissingParameterException, OperationFailedException, PermissionDeniedException {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    @Transactional
    public LprInfo createLpr(String personId, String luiId, String luiPersonRelationType, LprInfo luiPersonRelationInfo, ContextInfo context)
            throws DataValidationErrorException,
            DoesNotExistException, InvalidParameterException,
            MissingParameterException, OperationFailedException,
            PermissionDeniedException, ReadOnlyException {

        // make sure params are consistent with lprInfo:
        luiPersonRelationInfo.setPersonId(personId);
        luiPersonRelationInfo.setLuiId(luiId);
        luiPersonRelationInfo.setTypeKey(luiPersonRelationType);
        
        LprEntity lpr = new LprEntity(luiPersonRelationInfo);
        
        lpr.setEntityCreated(context);
        
        lprDao.persist(lpr);
        
        return lpr.toDto();
    }

    @Override
    @Transactional
    public LprInfo updateLpr(String luiPersonRelationId, LprInfo luiPersonRelationInfo, ContextInfo context) throws
            DoesNotExistException, InvalidParameterException, MissingParameterException, ReadOnlyException,
            OperationFailedException, PermissionDeniedException {
        LprEntity lprEntity = lprDao.find(luiPersonRelationId);

        if (lprEntity != null) {
            LprEntity modifiedLpr = new LprEntity(luiPersonRelationInfo);

            if (luiPersonRelationInfo.getStateKey() != null) {
                modifiedLpr.setPersonRelationStateId(luiPersonRelationInfo.getStateKey());
            }

            if (luiPersonRelationInfo.getTypeKey() != null) {
                modifiedLpr.setPersonRelationTypeId(luiPersonRelationInfo.getTypeKey());
            }

            modifiedLpr.setEntityUpdated(context);
            
            lprDao.merge(modifiedLpr);
            
            return lprDao.find(modifiedLpr.getId()).toDto();
        } else {
            throw new DoesNotExistException(luiPersonRelationId);
        }
    }

    @Override
    @Transactional
    public StatusInfo deleteLpr(String luiPersonRelationId, ContextInfo context) throws DoesNotExistException,
            InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        LprEntity lprEntity = lprDao.find(luiPersonRelationId);
        lprEntity.setPersonRelationStateId(LprServiceConstants.DROPPED_STATE_KEY);
        
        lprEntity.setUpdateId(context.getPrincipalId());
        lprEntity.setUpdateTime(context.getCurrentDate());
        
        lprDao.merge(lprEntity);
        StatusInfo status = new StatusInfo();
        status.setSuccess(Boolean.TRUE);
        return status;
    }

    @Override
    public List<LprInfo> searchForLprs(QueryByCriteria criteria, ContextInfo context) throws InvalidParameterException,
            MissingParameterException, OperationFailedException, PermissionDeniedException {
        throw new UnsupportedOperationException();
    }

    /**
     * This overridden method ...
     * 
     * @see org.kuali.student.enrollment.lpr.service.LprService#getLprTransaction(java.lang.String,
     *      org.kuali.student.r2.common.dto.ContextInfo)
     */
    @Override
    @Transactional
    public LprTransactionInfo getLprTransaction(String lprTransactionId, ContextInfo context) throws DoesNotExistException,
            InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {

        LprTransactionEntity transactionEntity = lprTransDao.find(lprTransactionId);

        return transactionEntity.toDto();
    }

    /**
     * This overridden method ...
     * 
     * @see org.kuali.student.enrollment.lpr.service.LprService#deleteLprTransaction(java.lang.String,
     *      org.kuali.student.r2.common.dto.ContextInfo)
     */
    @Override
    @Transactional
    public StatusInfo deleteLprTransaction(String lprTransactionId, ContextInfo context) throws DoesNotExistException,
            InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {

        StatusInfo status = new StatusInfo();

        LprTransactionEntity lprTrans = lprTransDao.find(lprTransactionId);
        if (null != lprTrans) {

            lprTransDao.remove(lprTrans);

            status.setSuccess(Boolean.TRUE);

        } else {
            status.setSuccess(Boolean.FALSE);
        }

        return status;
    }

    /**
     * This overridden method ...
     * 
     * @see org.kuali.student.enrollment.lpr.service.LprService#getLprsByPersonForAtp(java.lang.String,
     *      java.lang.String, org.kuali.student.r2.common.dto.ContextInfo)
     */
    @Override
    public List<LprInfo> getLprsByPersonForAtp(String personId, String atpId, ContextInfo context) throws DoesNotExistException,
            InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        // TODO sambitpatnaik - THIS METHOD NEEDS JAVADOCS
        return null;
    }

    @Override
    @Transactional
    public LprTransactionInfo createLprTransaction(String lprTransactionType, LprTransactionInfo lprTransactionInfo, ContextInfo context)
            throws DataValidationErrorException, DoesNotExistException, InvalidParameterException, MissingParameterException,
            OperationFailedException, PermissionDeniedException {

        LprTransactionEntity lprTransactionEntity = new LprTransactionEntity(lprTransactionInfo);
        if (lprTransactionEntity.getId() == null) {
            lprTransactionEntity.setId(UUIDHelper.genStringUUID());
        }
        if (null != lprTransactionInfo.getStateKey()) {
            lprTransactionEntity.setLprTransState(lprTransactionInfo.getStateKey());
        }

        if (null != lprTransactionInfo.getTypeKey()) {
            lprTransactionEntity.setLprTransType(lprTransactionInfo.getTypeKey());
        }
        if (null != lprTransactionInfo.getDescr()) {
        	RichTextInfo descr = lprTransactionInfo.getDescr();
            
        	lprTransactionEntity.setDescrFormatted(descr.getFormatted());
        	lprTransactionEntity.setDescrPlain(descr.getPlain());
        }

        List<LprTransactionItemEntity> lprTransItemEntities = new ArrayList<LprTransactionItemEntity>();

        for (LprTransactionItemInfo lprTransItemInfo : lprTransactionInfo.getLprTransactionItems()) {

            LprTransactionItemEntity lprTransItemEntity = createLprTransactionItem(lprTransItemInfo, context);

            lprTransItemEntities.add(lprTransItemEntity);
        }

        lprTransactionEntity.setLprTransactionItems(lprTransItemEntities);
        LprTransactionEntity existing = lprTransDao.find(lprTransactionEntity.getId());

        if (existing != null) {
            // throw new AlreadyExistsException();
            // TODO Mezba - decide what to do here
        }
        
        lprTransactionEntity.setEntityCreated(context);

        lprTransDao.persist(lprTransactionEntity);

        LprTransactionEntity retrived = lprTransDao.find(lprTransactionEntity.getId());

        LprTransactionInfo info = null;
        if (retrived != null) {
            info = retrived.toDto();
        }

        return info;

    }

    @Override
    @Transactional
    public LprTransactionInfo createLprTransactionFromExisting(String lprTransactionId, ContextInfo context) throws
            DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException,
            PermissionDeniedException {

        LprTransactionEntity existingLprTransactionEntity = lprTransDao.find(lprTransactionId);
        LprTransactionEntity newLprTransactionEntity = new LprTransactionEntity();
        if (existingLprTransactionEntity != null) {
            newLprTransactionEntity.setId(UUIDHelper.genStringUUID());
            newLprTransactionEntity.setAttributes(existingLprTransactionEntity.getAttributes());
            newLprTransactionEntity.setDescrFormatted(existingLprTransactionEntity.getDescrFormatted());
            newLprTransactionEntity.setDescrPlain(existingLprTransactionEntity.getDescrPlain());
            List<LprTransactionItemEntity> newItems = new ArrayList(existingLprTransactionEntity.getLprTransactionItems().size());
            for (LprTransactionItemEntity existingItem : existingLprTransactionEntity.getLprTransactionItems()) {
                LprTransactionItemEntity newItem = new LprTransactionItemEntity();
                newItem.setId(UUIDHelper.genStringUUID());
                newItem.setExistingLuiId(lprTransactionId);
                newItem.setLprTransactionItemState(LprServiceConstants.LPRTRANS_ITEM_NEW_STATE_KEY);
                newItem.setLprTransactionItemType(existingItem.getLprTransactionItemType());
                newItem.setNewLuiId(existingItem.getNewLuiId());
                newItem.setPersonId(existingItem.getPersonId());
                newItem.setDescrFormatted(existingItem.getDescrFormatted());
                newItem.setDescrPlain(existingItem.getDescrPlain());

            }
            newLprTransactionEntity.setLprTransactionItems(newItems);
            newLprTransactionEntity.setLprTransState(LprServiceConstants.LPRTRANS_NEW_STATE_KEY);
            newLprTransactionEntity.setLprTransType(existingLprTransactionEntity.getLprTransType());
            newLprTransactionEntity.setRequestingPersonId(existingLprTransactionEntity.getRequestingPersonId());
            lprTransDao.persist(newLprTransactionEntity);

        } else {
            throw new DoesNotExistException("Could not find any LPR Transaction for id : " + lprTransactionId);
        }
        LprTransactionEntity retrived = lprTransDao.find(newLprTransactionEntity.getId());
        LprTransactionInfo info = null;
        if (retrived != null) {
            info = retrived.toDto();
        } else {
            throw new OperationFailedException("");
        }
        return info;
    }

    @Override
    @Transactional
    public LprTransactionInfo processLprTransaction(String lprTransactionId, ContextInfo context) throws AlreadyExistsException,
            DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException,
            PermissionDeniedException {
        LprTransactionInfo lprTransaction = getLprTransaction(lprTransactionId, context);

        for (LprTransactionItemInfo lprTransactionItemInfo : lprTransaction.getLprTransactionItems()) {
            LprTransactionItemResultInfo lprTransResultInfo = new LprTransactionItemResultInfo();
            if (lprTransactionItemInfo.getTypeKey().equals(LprServiceConstants.LPRTRANS_ITEM_ADD_TYPE_KEY) ||
                    lprTransactionItemInfo.getTypeKey().equals(LprServiceConstants.LPRTRANS_ITEM_ADD_TO_WAITLIST_TYPE_KEY)) {
                /*
                // TODO Mezba - the method createLprFromLprTransactionItem is no longer there, decide what to do
                String lprCreated = createLprFromLprTransactionItem(lprTransactionItemInfo, context);
                
                lprTransResultInfo.setResultingLprId(lprCreated);
                 */
            } else if (lprTransactionItemInfo.getTypeKey().equals(LprServiceConstants.LPRTRANS_ITEM_DROP_TYPE_KEY)) {
                /**
                 * TODO this needs to be implemented for drop to work, read below Be careful with the implementation here,
                 * because we DO NOT want to delete all lprs that are matched by lui, person, and state, but instead only the
                 * ones that are related to the registration group we are dropping. It is VERY possible that a student could
                 * have the same course offering on their schedule twice with different reg groups, therefore deleting every
                 * LPR that matches that course offering id would be WRONG. In addition, it is possible that 2 reg groups
                 * that point to the same activity offering could be on the same schedule (this is more unlikely however and
                 * may be prevented by the system), and deleting both of those LPRs would be incorrect. So what we actually
                 * want to do is delete only lprs that have a direct relation to the reg group being dropped. However, there
                 * is no easy way currently to link these things together, one possible route is to get the original
                 * transactions and use their group id somehow, but this route may be flawed if the there is more than one
                 * succeeded transaction for the same reg group (VERY possible). There is no way currently (that I know of)
                 * to link the lprs for courseOffering, reg group, activities, and roster in a way that would be simple to
                 * determine by retrieving them from the db. This may be a possible hole in the service/db design.
                 */
                List<LprInfo> toBeDroppedLPRs;
                try {
                    toBeDroppedLPRs = getLprsByLuiPersonAndState(lprTransactionItemInfo.getPersonId(),
                            lprTransactionItemInfo.getExistingLuiId(), LprServiceConstants.REGISTERED_STATE_KEY, context);
                } catch (DisabledIdentifierException ex) {
                    throw new OperationFailedException("unexpected", ex);
                }

                if (toBeDroppedLPRs.size() > 1) {
                    throw new OperationFailedException("Multiple LuiPersonRelations between person:" +
                            lprTransactionItemInfo.getPersonId() + " and lui:" + lprTransactionItemInfo.getExistingLuiId() +
                            "; unimplemented functionality required to deal with this scenario is currentluy unimplemented");
                }
                for (LprInfo lprInfo : toBeDroppedLPRs) {
                    // TODO - change state to LprServiceConstants.DROPPED_STATE_KEY, rather than deleting
                    /*
                     * do this instead of delete lprInfo.setStateKey(LprServiceConstants.DROPPED_STATE_KEY);
                     * try { updateLpr(lprInfo.getId(), lprInfo, context); } catch (ReadOnlyException e) { throw new
                     * OperationFailedException("updateLpr() failure in processLprTransaction()", e); }
                     */
                    deleteLpr(lprInfo.getId(), context);
                    lprTransResultInfo.setResultingLprId(lprInfo.getId());
                }

            } else if (lprTransactionItemInfo.getTypeKey().equals(LprServiceConstants.LPRTRANS_ITEM_SWAP_TYPE_KEY)) {

                List<LprInfo> toBeDroppedLPRs;
                try {
                    toBeDroppedLPRs = getLprsByLuiPersonAndState(lprTransactionItemInfo.getPersonId(),
                            lprTransactionItemInfo.getExistingLuiId(), LprServiceConstants.REGISTERED_STATE_KEY, context);
                } catch (DisabledIdentifierException ex) {
                    throw new OperationFailedException("unexpected", ex);
                }
                if (toBeDroppedLPRs.size() > 1) {
                    throw new OperationFailedException("Multiple LuiPersonRelations between person:" +
                            lprTransactionItemInfo.getPersonId() + " and lui:" + lprTransactionItemInfo.getExistingLuiId() +
                            "; unimplemented functionality required to deal with this scenario is currentluy unimplemented");
                }
                for (LprInfo lprInfo : toBeDroppedLPRs) {
                    // TODO - change state to LprServiceConstants.DROPPED_STATE_KEY, rather than deleting
                    /*
                     * do this instead of delete lprInfo.setStateKey(LprServiceConstants.DROPPED_STATE_KEY);
                     * try { updateLpr(lprInfo.getId(), lprInfo, context); } catch (ReadOnlyException e) { throw new
                     * OperationFailedException("updateLpr() failure in processLprTransaction()", e); }
                     */
                    deleteLpr(lprInfo.getId(), context);
                    /*
                    // TODO Mezba - the method createLprFromLprTransactionItem is no longer there, decide what to do
                    
                    String lprCreated = createLprFromLprTransactionItem(lprTransactionItemInfo, context);
                    
                    lprTransResultInfo.setResultingLprId(lprCreated);
                     */
                }
            } else {

                throw new OperationFailedException("The LPR Transaction Item did not have one of the supported type ");
            }
            lprTransResultInfo.setStatus(true);
            lprTransactionItemInfo.setStateKey(LprServiceConstants.LPRTRANS_ITEM_SUCCEEDED_STATE_KEY);
            lprTransactionItemInfo.setLprTransactionItemResult(lprTransResultInfo);

        }
        try {
            lprTransaction.setStateKey(LprServiceConstants.LPRTRANS_SUCCEEDED_STATE_KEY);
            updateLprTransaction(lprTransactionId, lprTransaction, context);
        } catch (DataValidationErrorException ex) {
            throw new OperationFailedException(ex.getMessage());
        }
        return lprTransaction;
    }

    @Override
    public List<LprTransactionInfo> getLprTransactionsByIds(List<String> lprIds, ContextInfo context) throws DoesNotExistException,
            InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        // TODO sambit - THIS METHOD NEEDS JAVADOCS
        return null;
    }

    @Override
    public List<LprTransactionItemInfo> getLprTransactionItemsByResultingLpr(String lprId, ContextInfo context) throws
            InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {

        List<LprTransactionItemEntity> lprTransItems = lprTransItemDao.getLprTransactionItemsByLpr(lprId);
        List<LprTransactionEntity> lprTrans = new ArrayList<LprTransactionEntity>();
        for (LprTransactionItemEntity lprTransItem : lprTransItems) {

            lprTrans.add(lprTransDao.getByLprTransactionItemId(lprTransItem.getId()));
        }
        List<LprTransactionInfo> lprTransInfos = new ArrayList<LprTransactionInfo>();

        for (LprTransactionEntity lprTransEntity : lprTrans) {
            lprTransInfos.add(lprTransEntity.toDto());
        }
        // return lprTransInfos;
        return null;
        // TODO Mezba - implement method
    }

    @Transactional
    @Override
    public LprTransactionInfo updateLprTransaction(String lprTransactionId, LprTransactionInfo lprTransactionInfo, ContextInfo context) throws
            DataValidationErrorException, DoesNotExistException, InvalidParameterException, MissingParameterException,
            OperationFailedException, PermissionDeniedException {
        LprTransactionEntity lprTrans = lprTransDao.find(lprTransactionId);

        if (null != lprTrans) {
            LprTransactionEntity modifiedLprTrans = new LprTransactionEntity(lprTransactionInfo);
            if (lprTransactionInfo.getStateKey() != null) {
                modifiedLprTrans.setLprTransState(lprTransactionInfo.getStateKey());
            }
            if (lprTransactionInfo.getTypeKey() != null) {
                modifiedLprTrans.setLprTransType(lprTransactionInfo.getTypeKey());
            }

            List<LprTransactionItemEntity> lprTransItemEntityList = processLprTransactionItemsModification(lprTransactionInfo,
                    lprTrans, context);

            modifiedLprTrans.setLprTransactionItems(lprTransItemEntityList);
            lprTransDao.merge(modifiedLprTrans);
            return lprTransDao.find(modifiedLprTrans.getId()).toDto();

        } else {
            throw new DoesNotExistException(lprTransactionId);
        }
    }

    @Transactional
    private List<LprTransactionItemEntity> processLprTransactionItemsModification(LprTransactionInfo modifiedTransactionInfo, LprTransactionEntity originalLprTransEntity, ContextInfo context) {
        List<LprTransactionItemEntity> modifiedLprTransItemEntities = new ArrayList<LprTransactionItemEntity>();
        LprTransactionInfo originalLprTransInfo = originalLprTransEntity.toDto();
        List<String> deletedItems = new ArrayList<String>();
        // Assume all original items are deleted until matched
        for (LprTransactionItemInfo originalLprTransItemInfo : originalLprTransInfo.getLprTransactionItems()) {
            deletedItems.add(originalLprTransItemInfo.getId());
        }

        for (LprTransactionItemInfo modifiedTransactionItemInfo : modifiedTransactionInfo.getLprTransactionItems()) {
            boolean existingItem = false;
            if (modifiedTransactionItemInfo.getId() != null) {
                for (LprTransactionItemInfo originalLprTransItemInfo : originalLprTransInfo.getLprTransactionItems()) {
                    if (originalLprTransItemInfo.getId().equals(modifiedTransactionItemInfo.getId())) {
                        existingItem = true;
                        deletedItems.remove(originalLprTransItemInfo.getId());
                    }
                }
                if (existingItem) {

                    modifiedLprTransItemEntities.add(updateLprTransactionItem(modifiedTransactionItemInfo, context));
                }
            }

            if (!existingItem || modifiedTransactionItemInfo.getId() == null) {
                modifiedLprTransItemEntities.add(createLprTransactionItem(modifiedTransactionItemInfo, context));
            }
        }

        for (String id : deletedItems) {
            lprTransItemDao.remove(lprTransItemDao.find(id));
        }

        return modifiedLprTransItemEntities;

    }

    private LprTransactionItemEntity updateLprTransactionItem(LprTransactionItemInfo modifiedTransactionItemInfo, ContextInfo context) {
        LprTransactionItemEntity modifiedLprItemEntity = new LprTransactionItemEntity(modifiedTransactionItemInfo);
        if (null != modifiedTransactionItemInfo.getStateKey()) {
            modifiedLprItemEntity.setLprTransactionItemState(modifiedTransactionItemInfo.getStateKey());
        }
        if (null != modifiedTransactionItemInfo.getTypeKey()) {
            modifiedLprItemEntity.setLprTransactionItemType(modifiedTransactionItemInfo.getTypeKey());
        }
        if (null != modifiedTransactionItemInfo.getDescr()) {
        	RichTextInfo descr = modifiedTransactionItemInfo.getDescr();
            modifiedLprItemEntity.setDescrFormatted(descr.getFormatted());
            modifiedLprItemEntity.setDescrPlain(descr.getPlain());
        }
        lprTransItemDao.merge(modifiedLprItemEntity);
        return modifiedLprItemEntity;
    }

    @Transactional
    private LprTransactionItemEntity createLprTransactionItem(LprTransactionItemInfo lprTransactionItemInfo, ContextInfo context) {
        LprTransactionItemEntity lprTransItemEntity = new LprTransactionItemEntity(lprTransactionItemInfo);
        if (lprTransItemEntity.getId() == null) {
            lprTransItemEntity.setId(UUIDHelper.genStringUUID());
        }
        if (null != lprTransactionItemInfo.getStateKey()) {
            lprTransItemEntity.setLprTransactionItemState(lprTransactionItemInfo.getStateKey());
        }

        if (null != lprTransactionItemInfo.getTypeKey()) {
            lprTransItemEntity.setLprTransactionItemType(lprTransactionItemInfo.getTypeKey());
        }
        if (null != lprTransactionItemInfo.getDescr()) {
        	RichTextInfo descr = lprTransactionItemInfo.getDescr();
        	lprTransItemEntity.setDescrFormatted(descr.getFormatted());
        	lprTransItemEntity.setDescrPlain(descr.getPlain());
        }
        
        lprTransItemEntity.setEntityCreated(context);


        lprTransItemDao.persist(lprTransItemEntity);

        return lprTransItemDao.find(lprTransItemEntity.getId());
    }

    @Override
    public List<LprTransactionInfo> searchForLprTransactions(QueryByCriteria criteria, ContextInfo context) throws
            InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        throw new UnsupportedOperationException("Operation not implemented");
    }

    @Override
    public List<String> searchForLprTransactionIds(QueryByCriteria criteria, ContextInfo context) throws InvalidParameterException,
            MissingParameterException, OperationFailedException, PermissionDeniedException {
        throw new UnsupportedOperationException("Operation not implemented");
    }

    @Override
    public List<LprInfo> getLprsByPersonAndTypeForAtp(String personId, String atpId, String typeKey, ContextInfo context) throws
            DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException,
            PermissionDeniedException {

        List<LprEntity> entityList = lprDao.getLprsByPersonAndType(personId, typeKey);
        List<LprInfo> infoList = new ArrayList<LprInfo>();
        for (LprEntity entity : entityList) {
            // TODO: inject this impl with a lui service impl to get the atp to check
//            LuiEntity lui = luiDao.find(entity.getLuiId());
//            if (StringUtils.equals(lui.getAtpId(), atpId)) {
            infoList.add(entity.toDto());
//            }
        }

        return infoList;
    }

    @Override
    public List<LprInfo> getLprsByPersonForAtpAndLuiType(String personId, String atpId, String luiTypeKey, ContextInfo context) throws
            DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException,
            PermissionDeniedException {
        List<LprEntity> entityList = lprDao.getLprsByPerson(personId);

        List<LprInfo> infoList = new ArrayList<LprInfo>();
        for (LprEntity entity : entityList) {
//            LuiEntity lui = luiDao.find(entity.getLuiId());
//            if ((lui.getAtpId().equals(atpId)) && (lui.getLuiType().equals(luiTypeKey))) {
            infoList.add(entity.toDto());
//            }
        }

        return infoList;
    }

    @Override
    public List<LprInfo> getLprsByLuiAndType(String luiId, String typeKey, ContextInfo context) throws DoesNotExistException,
            InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        // TODO sambit - THIS METHOD NEEDS JAVADOCS
        return null;
    }

    @Override
    public List<LprInfo> getLprsByPersonAndLuiType(String personId, String luiTypeKey, ContextInfo context) throws
            DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException,
            PermissionDeniedException {
        List<LprEntity> entityList = lprDao.getLprsByPerson(personId);

        List<LprInfo> infoList = new ArrayList<LprInfo>();
        for (LprEntity entity : entityList) {
//            LuiEntity lui = luiDao.find(entity.getLuiId());
//            if ((lui.getLuiType().equals(luiTypeKey))) {
            infoList.add(entity.toDto());
//            }
        }

        return infoList;
    }

    @Override
    public List<ValidationResultInfo> verifyLprTransaction(String lprTransactionId,
            ContextInfo contextInfo)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException,
            OperationFailedException, PermissionDeniedException {
        return null;
        // TODO Mezba - implement method
    }

    @Override
    public List<ValidationResultInfo> validateLpr(String validationType,
            String luiId,
            String personId,
            String lprTypeKey,
            LprInfo lprInfo,
            ContextInfo contextInfo)
            throws DoesNotExistException, InvalidParameterException,
            MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;
        // TODO Mezba - implement method
    }

    @Override
    public List<LprTransactionItemInfo> getLprTransactionItemsByLui(String luiId, ContextInfo contextInfo)
            throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;
        // TODO Mezba - implement method
    }

    @Override
    public List<LprTransactionInfo> getUnsubmittedLprTransactionsByRequestingPersonAndAtp(
            String requestingPersonId,
            String atpId,
            ContextInfo contextInfo) throws InvalidParameterException,
            MissingParameterException, OperationFailedException, PermissionDeniedException {
        return null;
        // TODO Mezba - implement method
    }

    @Override
    public List<LprTransactionItemInfo> getLprTransactionItemsByPersonAndLui(
            String personId, String luiId, ContextInfo contextInfo)
            throws DoesNotExistException, InvalidParameterException, MissingParameterException,
            OperationFailedException, PermissionDeniedException {
        return null;
        // TODO Mezba - implement method

    }

    @Override
    public List<BulkStatusInfo> createLprsForLui(String luiId,
            String lprTypeKey,
            List<LprInfo> lprInfos,
            ContextInfo contextInfo)
            throws DataValidationErrorException,
            DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException,
            PermissionDeniedException, ReadOnlyException {
        return null;
        // TODO Mezba - implement method
    }
}
