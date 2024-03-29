/**
 * Copyright 2010 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.student.lum.lrc.service.impl;

import java.util.List;

import javax.jws.WebService;

import org.kuali.student.core.dto.StatusInfo;
import org.kuali.student.core.exceptions.AlreadyExistsException;
import org.kuali.student.core.exceptions.DataValidationErrorException;
import org.kuali.student.core.exceptions.DoesNotExistException;
import org.kuali.student.core.exceptions.InvalidParameterException;
import org.kuali.student.core.exceptions.MissingParameterException;
import org.kuali.student.core.exceptions.OperationFailedException;
import org.kuali.student.core.exceptions.PermissionDeniedException;
import org.kuali.student.core.exceptions.VersionMismatchException;
import org.kuali.student.core.search.dto.SearchCriteriaTypeInfo;
import org.kuali.student.core.search.dto.SearchRequest;
import org.kuali.student.core.search.dto.SearchResult;
import org.kuali.student.core.search.dto.SearchResultTypeInfo;
import org.kuali.student.core.search.dto.SearchTypeInfo;
import org.kuali.student.core.search.service.impl.SearchManager;
import org.kuali.student.lum.lrc.dao.LrcDao;
import org.kuali.student.lum.lrc.dto.CredentialInfo;
import org.kuali.student.lum.lrc.dto.CredentialTypeInfo;
import org.kuali.student.lum.lrc.dto.CreditInfo;
import org.kuali.student.lum.lrc.dto.CreditTypeInfo;
import org.kuali.student.lum.lrc.dto.GradeInfo;
import org.kuali.student.lum.lrc.dto.GradeTypeInfo;
import org.kuali.student.lum.lrc.dto.ResultComponentInfo;
import org.kuali.student.lum.lrc.dto.ResultComponentTypeInfo;
import org.kuali.student.lum.lrc.dto.ScaleInfo;
import org.kuali.student.lum.lrc.entity.Credential;
import org.kuali.student.lum.lrc.entity.CredentialType;
import org.kuali.student.lum.lrc.entity.Credit;
import org.kuali.student.lum.lrc.entity.CreditType;
import org.kuali.student.lum.lrc.entity.Grade;
import org.kuali.student.lum.lrc.entity.GradeType;
import org.kuali.student.lum.lrc.entity.ResultComponent;
import org.kuali.student.lum.lrc.entity.ResultComponentType;
import org.kuali.student.lum.lrc.entity.Scale;
import org.kuali.student.lum.lrc.service.LrcService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lindholm
 *
 */
@WebService(endpointInterface = "org.kuali.student.lum.lrc.service.LrcService", serviceName = "LrcService", portName = "LrcService", targetNamespace = "http://student.kuali.org/wsdl/lrc")
@Transactional(rollbackFor={Throwable.class})
public class LrcServiceImpl implements LrcService {
	private LrcDao lrcDao;
    private SearchManager searchManager;

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#compareGrades(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String compareGrades(String gradeKey, String scaleKey,
			String compareGradeKey, String compareScaleKey)
			throws InvalidParameterException, MissingParameterException,
			OperationFailedException {
		// TODO Auto-generated method stub
//		return null;
		throw new UnsupportedOperationException("Method not yet implemented!");
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#createResultComponent(java.lang.String, org.kuali.student.lum.lrc.dto.ResultComponentInfo)
	 */
	@Override
	public ResultComponentInfo createResultComponent(
			String resultComponentTypeKey,
			ResultComponentInfo resultComponentInfo)
			throws AlreadyExistsException, DataValidationErrorException,
			DoesNotExistException, InvalidParameterException,
			MissingParameterException, OperationFailedException,
			PermissionDeniedException {
	    checkForMissingParameter(resultComponentTypeKey, "resultComponentTypeKey");
	    checkForMissingParameter(resultComponentInfo, "resultComponentInfo");

	    ResultComponent rc = LrcServiceAssembler.toResultComponent(resultComponentTypeKey, resultComponentInfo, lrcDao);
	    lrcDao.create(rc);
	    return LrcServiceAssembler.toResultComponentInfo(rc);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#deleteResultComponent(java.lang.String)
	 */
	@Override
	public StatusInfo deleteResultComponent(String resultComponentId)
			throws DoesNotExistException, InvalidParameterException,
			MissingParameterException, OperationFailedException,
			PermissionDeniedException {
	    checkForMissingParameter(resultComponentId, "resultComponentId");
		lrcDao.delete(ResultComponent.class, resultComponentId);
		StatusInfo statusInfo = new StatusInfo();
		return statusInfo;
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getCredential(java.lang.String)
	 */
	@Override
	public CredentialInfo getCredential(String credentialKey)
			throws DoesNotExistException, InvalidParameterException,
			MissingParameterException, OperationFailedException {
	      checkForMissingParameter(credentialKey, "credentialKey");
	      Credential credential = lrcDao.fetch(Credential.class, credentialKey);

	      return LrcServiceAssembler.toCredentialInfo(credential);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getCredentialKeysByCredentialType(java.lang.String)
	 */
	@Override
	public List<String> getCredentialKeysByCredentialType(
			String credentialTypeKey) throws DoesNotExistException,
			InvalidParameterException, MissingParameterException,
			OperationFailedException {
        checkForMissingParameter(credentialTypeKey, "credentialTypeKey");
        List<String> credentialIds = lrcDao.getCredentialIdsByCredentialType(credentialTypeKey);
        if (credentialIds.isEmpty()) {
            throw new DoesNotExistException(credentialTypeKey);
        }
        return credentialIds;
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getCredentialType(java.lang.String)
	 */
	@Override
	public CredentialTypeInfo getCredentialType(String credentialTypeKey)
			throws DoesNotExistException, InvalidParameterException,
			MissingParameterException, OperationFailedException {
        checkForMissingParameter(credentialTypeKey, "credentialTypeKey");
        CredentialType credentialType = lrcDao.fetch(CredentialType.class, credentialTypeKey);

        return LrcServiceAssembler.toCredentialTypeInfo(credentialType);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getCredentialTypes()
	 */
	@Override
	public List<CredentialTypeInfo> getCredentialTypes()
			throws OperationFailedException {
        List<CredentialType> entities = lrcDao.find(CredentialType.class);
        return LrcServiceAssembler.toCredentialTypeInfos(entities);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getCredentialsByKeyList(java.util.List)
	 */
	@Override
	public List<CredentialInfo> getCredentialsByKeyList(
			List<String> credentialKeyList) throws DoesNotExistException,
			InvalidParameterException, MissingParameterException,
			OperationFailedException {
        checkForMissingParameter(credentialKeyList, "credentialKeyList");
        checkForEmptyList(credentialKeyList, "credentialKeyList");
        List<Credential> credentials = lrcDao.getCredentialsByIdList(credentialKeyList);
        if (credentials.isEmpty()) {
            throw new DoesNotExistException();
        }
        return LrcServiceAssembler.toCredentialInfos(credentials);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getCredit(java.lang.String)
	 */
	@Override
	public CreditInfo getCredit(String creditKey) throws DoesNotExistException,
			InvalidParameterException, MissingParameterException,
			OperationFailedException {
		checkForMissingParameter(creditKey, "creditKey");
		Credit credit = lrcDao.fetch(Credit.class, creditKey);

		return LrcServiceAssembler.toCreditInfo(credit);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getCreditKeysByCreditType(java.lang.String)
	 */
	@Override
	public List<String> getCreditKeysByCreditType(String creditTypeKey)
			throws DoesNotExistException, InvalidParameterException,
			MissingParameterException, OperationFailedException {
	    checkForMissingParameter(creditTypeKey, "creditTypeKey");
		List<String> creditIds = lrcDao.getCreditIdsByCreditType(creditTypeKey);
		if (creditIds.isEmpty()) {
		    throw new DoesNotExistException(creditTypeKey);
		}
		return creditIds;
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getCreditType(java.lang.String)
	 */
	@Override
	public CreditTypeInfo getCreditType(String creditTypeKey)
			throws DoesNotExistException, InvalidParameterException,
			MissingParameterException, OperationFailedException {
		checkForMissingParameter(creditTypeKey, "creditTypeKey");
        CreditType creditType = lrcDao.fetch(CreditType.class, creditTypeKey);

        return LrcServiceAssembler.toCreditTypeInfo(creditType);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getCreditTypes()
	 */
	@Override
	public List<CreditTypeInfo> getCreditTypes()
			throws OperationFailedException {
		List<CreditType> entities = lrcDao.find(CreditType.class);
		return LrcServiceAssembler.toCreditTypeInfos(entities);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getCreditsByKeyList(java.util.List)
	 */
	@Override
	public List<CreditInfo> getCreditsByKeyList(List<String> creditKeyList)
			throws DoesNotExistException, InvalidParameterException,
			MissingParameterException, OperationFailedException {
	    checkForMissingParameter(creditKeyList, "creditKeyList");
	    checkForEmptyList(creditKeyList, "creditKeyList");
		List<Credit> credits = lrcDao.getCreditsByIdList(creditKeyList);
		if (credits.isEmpty()) {
		    throw new DoesNotExistException();
		}
		return LrcServiceAssembler.toCreditInfos(credits);
	}

    /* (non-Javadoc)
     * @see org.kuali.student.lum.lrc.service.LrcService#getGrade(java.lang.String)
     */
    @Override
    public GradeInfo getGrade(String gradeKey) throws DoesNotExistException,
            InvalidParameterException, MissingParameterException,
            OperationFailedException {
        checkForMissingParameter(gradeKey, "gradeKey");
        Grade grade = lrcDao.fetch(Grade.class, gradeKey);

        return LrcServiceAssembler.toGradeInfo(grade);
    }

    /* (non-Javadoc)
     * @see org.kuali.student.lum.lrc.service.LrcService#getGradeKeysByGradeType(java.lang.String)
     */
    @Override
    public List<String> getGradeKeysByGradeType(String gradeTypeKey)
            throws DoesNotExistException, InvalidParameterException,
            MissingParameterException, OperationFailedException {
        checkForMissingParameter(gradeTypeKey, "gradeTypeKey");
        List<String> gradeIds = lrcDao.getGradeIdsByGradeType(gradeTypeKey);
        if (gradeIds.isEmpty()) {
            throw new DoesNotExistException(gradeTypeKey);
        }
        return gradeIds;
    }

    /* (non-Javadoc)
     * @see org.kuali.student.lum.lrc.service.LrcService#getGradeType(java.lang.String)
     */
    @Override
    public GradeTypeInfo getGradeType(String gradeTypeKey)
            throws DoesNotExistException, InvalidParameterException,
            MissingParameterException, OperationFailedException {
        checkForMissingParameter(gradeTypeKey, "gradeTypeKey");
        GradeType gradeType = lrcDao.fetch(GradeType.class, gradeTypeKey);

        return LrcServiceAssembler.toGradeTypeInfo(gradeType);
    }

    /* (non-Javadoc)
     * @see org.kuali.student.lum.lrc.service.LrcService#getGradeTypes()
     */
    @Override
    public List<GradeTypeInfo> getGradeTypes() throws OperationFailedException {
        List<GradeType> entities = lrcDao.find(GradeType.class);
        return LrcServiceAssembler.toGradeTypeInfos(entities);
    }

    /* (non-Javadoc)
     * @see org.kuali.student.lum.lrc.service.LrcService#getGradesByKeyList(java.util.List)
     */
    @Override
    public List<GradeInfo> getGradesByKeyList(List<String> gradeKeyList)
            throws DoesNotExistException, InvalidParameterException,
            MissingParameterException, OperationFailedException {
        checkForMissingParameter(gradeKeyList, "gradeKeyList");
        checkForEmptyList(gradeKeyList, "gradeKeyList");
        List<Grade> grades = lrcDao.getGradesByIdList(gradeKeyList);
        if (grades.isEmpty()) {
            throw new DoesNotExistException();
        }
        return LrcServiceAssembler.toGradeInfos(grades);
    }
	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getGradesByScale(java.lang.String)
	 */
	@Override
	public List<GradeInfo> getGradesByScale(String scale)
			throws DoesNotExistException, InvalidParameterException,
			MissingParameterException, OperationFailedException {
        checkForMissingParameter(scale, "scale");
        List<Grade> grades = lrcDao.getGradesByScale(scale);
        if (grades.isEmpty()) {
            throw new DoesNotExistException(scale);
        }

        return LrcServiceAssembler.toGradeInfos(grades);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getResultComponent(java.lang.String)
	 */
	@Override
	public ResultComponentInfo getResultComponent(String resultComponentId)
			throws DoesNotExistException, InvalidParameterException,
			MissingParameterException, OperationFailedException {
	    checkForMissingParameter(resultComponentId, "resultComponentId");
	    ResultComponent resultComponent = lrcDao.fetch(ResultComponent.class, resultComponentId);

	    return LrcServiceAssembler.toResultComponentInfo(resultComponent);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getResultComponentIdsByResult(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getResultComponentIdsByResult(String resultValueId,
			String resultComponentTypeKey) throws DoesNotExistException,
			InvalidParameterException, MissingParameterException,
			OperationFailedException {
	    checkForMissingParameter(resultValueId, "resultValueId");
	    checkForMissingParameter(resultComponentTypeKey, "resultComponentTypeKey");
	    List<String> ids = lrcDao.getResultComponentIdsByResult(resultValueId, resultComponentTypeKey);
	    if (ids.isEmpty()) {
	        throw new DoesNotExistException();
	    }
	    return ids;
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getResultComponentIdsByResultComponentType(java.lang.String)
	 */
	@Override
	public List<String> getResultComponentIdsByResultComponentType(
			String resultComponentTypeKey) throws DoesNotExistException,
			InvalidParameterException, MissingParameterException,
			OperationFailedException {
	    checkForMissingParameter(resultComponentTypeKey, "resultComponentTypeKey");
        List<String> ids = lrcDao.getResultComponentIdsByResultComponentType(resultComponentTypeKey);
        if (ids.isEmpty()) {
            throw new DoesNotExistException();
        }
        return ids;
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getResultComponentType(java.lang.String)
	 */
	@Override
	public ResultComponentTypeInfo getResultComponentType(
			String resultComponentTypeKey) throws DoesNotExistException,
			InvalidParameterException, MissingParameterException,
			OperationFailedException {
		checkForMissingParameter(resultComponentTypeKey, "resultComponentTypeKey");
		ResultComponentType entity = lrcDao.getResultComponentType(resultComponentTypeKey);
		return LrcServiceAssembler.toResultComponentTypeInfo(entity);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getResultComponentTypes()
	 */
	@Override
	public List<ResultComponentTypeInfo> getResultComponentTypes()
			throws OperationFailedException {
		List<ResultComponentType> rct = lrcDao.find(ResultComponentType.class);
		return LrcServiceAssembler.toResultComponentTypeInfos(rct);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#getScale(java.lang.String)
	 */
	@Override
	public ScaleInfo getScale(String scaleKey) throws DoesNotExistException,
			InvalidParameterException, MissingParameterException,
			OperationFailedException {
		checkForMissingParameter(scaleKey, "scaleKey");
		Scale scale = lrcDao.fetch(Scale.class, scaleKey);
		return LrcServiceAssembler.toScaleInfo(scale);
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#translateGrade(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<GradeInfo> translateGrade(String gradeKey, String scaleKey,
			String translateScaleKey) throws InvalidParameterException,
			MissingParameterException, OperationFailedException {
		// TODO Auto-generated method stub
//		return null;
		throw new UnsupportedOperationException("Method not yet implemented!");
	}

	/* (non-Javadoc)
	 * @see org.kuali.student.lum.lrc.service.LrcService#updateResultComponent(java.lang.String, org.kuali.student.lum.lrc.dto.ResultComponentInfo)
	 */
	@Override
	public ResultComponentInfo updateResultComponent(String resultComponentId,
			ResultComponentInfo resultComponentInfo)
			throws DataValidationErrorException, DoesNotExistException,
			InvalidParameterException, MissingParameterException,
			OperationFailedException, PermissionDeniedException,
			VersionMismatchException {
	    checkForMissingParameter(resultComponentId, "resultComponentId");
        checkForMissingParameter(resultComponentInfo, "resultComponentInfo");

        ResultComponent entity = lrcDao.fetch(ResultComponent.class, resultComponentId);
        
		if (!String.valueOf(entity.getVersionInd()).equals(resultComponentInfo.getMetaInfo().getVersionInd())){
			throw new VersionMismatchException("ResultComponent to be updated is not the current version");
		}
        
        LrcServiceAssembler.toResultComponent(entity, resultComponentInfo, lrcDao);
        lrcDao.update(entity);
        return LrcServiceAssembler.toResultComponentInfo(entity);
    }

	/**
	 * @return the lrcDao
	 */
	public LrcDao getLrcDao() {
		return lrcDao;
	}

	/**
	 * @param lrcDao the lrcDao to set
	 */
	public void setLrcDao(LrcDao lrcDao) {
		this.lrcDao = lrcDao;
	}

    /**
     * Check for missing parameter and throw localized exception if missing
     *
     * @param param
     * @param parameter name
     * @throws MissingParameterException
     */
    private void checkForMissingParameter(Object param, String paramName)
            throws MissingParameterException {
        if (param == null) {
            throw new MissingParameterException(paramName + " can not be null");
        }
    }

    /**
     * @param param
     * @param paramName
     * @throws MissingParameterException
     */
    private void checkForEmptyList(Object param, String paramName)
            throws MissingParameterException {
        if (param != null && param instanceof List<?> && ((List<?>)param).size() == 0) {
            throw new MissingParameterException(paramName + " can not be an empty list");
        }
    }
    

	@Override
	public SearchCriteriaTypeInfo getSearchCriteriaType(
			String searchCriteriaTypeKey) throws DoesNotExistException,
			InvalidParameterException, MissingParameterException,
			OperationFailedException {

		return searchManager.getSearchCriteriaType(searchCriteriaTypeKey);
	}

	@Override
	public List<SearchCriteriaTypeInfo> getSearchCriteriaTypes()
			throws OperationFailedException {
		return searchManager.getSearchCriteriaTypes();
	}

	@Override
	public SearchResultTypeInfo getSearchResultType(String searchResultTypeKey)
			throws DoesNotExistException, InvalidParameterException,
			MissingParameterException, OperationFailedException {
		checkForMissingParameter(searchResultTypeKey, "searchResultTypeKey");
		return searchManager.getSearchResultType(searchResultTypeKey);
	}

	@Override
	public List<SearchResultTypeInfo> getSearchResultTypes()
			throws OperationFailedException {
		return searchManager.getSearchResultTypes();
	}

	@Override
	public SearchTypeInfo getSearchType(String searchTypeKey)
			throws DoesNotExistException, InvalidParameterException,
			MissingParameterException, OperationFailedException {
		checkForMissingParameter(searchTypeKey, "searchTypeKey");
		return searchManager.getSearchType(searchTypeKey);
	}

	@Override
	public List<SearchTypeInfo> getSearchTypes()
			throws OperationFailedException {
		return searchManager.getSearchTypes();
	}

	@Override
	public List<SearchTypeInfo> getSearchTypesByCriteria(
			String searchCriteriaTypeKey) throws DoesNotExistException,
			InvalidParameterException, MissingParameterException,
			OperationFailedException {
		checkForMissingParameter(searchCriteriaTypeKey, "searchCriteriaTypeKey");
		return searchManager.getSearchTypesByCriteria(searchCriteriaTypeKey);
	}

	@Override
	public List<SearchTypeInfo> getSearchTypesByResult(
			String searchResultTypeKey) throws DoesNotExistException,
			InvalidParameterException, MissingParameterException,
			OperationFailedException {
		checkForMissingParameter(searchResultTypeKey, "searchResultTypeKey");
		return searchManager.getSearchTypesByResult(searchResultTypeKey);
	}

	public SearchManager getSearchManager() {
		return searchManager;
	}

	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}

	@Override
	public SearchResult search(SearchRequest searchRequest) throws MissingParameterException {
        checkForMissingParameter(searchRequest, "searchRequest");
        return searchManager.search(searchRequest, lrcDao);
	}

}
