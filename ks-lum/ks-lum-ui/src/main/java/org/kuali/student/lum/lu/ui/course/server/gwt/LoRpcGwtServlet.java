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

package org.kuali.student.lum.lu.ui.course.server.gwt;

import java.util.List;

import org.kuali.student.common.ui.server.gwt.BaseRpcGwtServletAbstract;
import org.kuali.student.core.dto.StatusInfo;
import org.kuali.student.core.exceptions.DataValidationErrorException;
import org.kuali.student.core.exceptions.DependentObjectsExistException;
import org.kuali.student.core.exceptions.DoesNotExistException;
import org.kuali.student.core.exceptions.InvalidParameterException;
import org.kuali.student.core.exceptions.MissingParameterException;
import org.kuali.student.core.exceptions.OperationFailedException;
import org.kuali.student.core.exceptions.PermissionDeniedException;
import org.kuali.student.core.exceptions.VersionMismatchException;
import org.kuali.student.lum.lo.dto.LoCategoryInfo;
import org.kuali.student.lum.lo.dto.LoCategoryTypeInfo;
import org.kuali.student.lum.lo.dto.LoInfo;
import org.kuali.student.lum.lo.service.LearningObjectiveService;
import org.kuali.student.lum.lu.ui.course.client.service.LoRpcService;

/**
 * 
 * @author Kuali Student Team
 *
 */
public class LoRpcGwtServlet extends BaseRpcGwtServletAbstract<LearningObjectiveService> implements LoRpcService{

    private static final long serialVersionUID = 1L;

    /**
     * This overridden method ...
     * 
     * @see org.kuali.student.lum.lu.ui.course.client.service.LoRpcService#getLoByIdList(java.util.List)
     */
    @Override
    public List<LoInfo> getLoByIdList(List<String> loIds) {
        try {
            return service.getLoByIdList(loIds);

        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (MissingParameterException e) {
            e.printStackTrace();
        } catch (OperationFailedException e) {
            e.printStackTrace();

        }
        return null;

    }

    /**
     * This overridden method ...
     * 
     * @see org.kuali.student.lum.lu.ui.course.client.service.LoRpcService#getLoCategoryTypes()
     */
    @Override
    public List<LoCategoryTypeInfo> getLoCategoryTypes() {
        try {
            return service.getLoCategoryTypes();

        } catch (OperationFailedException e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * This overridden method ...
     * 
     * @see org.kuali.student.lum.lu.ui.course.client.service.LoRpcService#createLoCategory(java.lang.String, java.lang.String, org.kuali.student.lum.lo.dto.LoCategoryInfo)
     */
    @Override
    public LoCategoryInfo createLoCategory(String loRepositoryKey, String loCategoryTypeKey, LoCategoryInfo loCategoryInfo) {
        try {
            return service.createLoCategory(loRepositoryKey, loCategoryTypeKey, loCategoryInfo);

        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (MissingParameterException e) {
            e.printStackTrace();
        } catch (OperationFailedException e) {
            e.printStackTrace();
        } catch (DataValidationErrorException e) {
            e.printStackTrace();
        } catch (DoesNotExistException e) {
            e.printStackTrace();
        } catch (PermissionDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * This overridden method ...
     * 
     * @see org.kuali.student.lum.lu.ui.course.client.service.LoRpcService#getLoCategory(java.lang.String)
     */
    @Override
    public LoCategoryInfo getLoCategory(String loCategoryId) {
        try {
            
            return service.getLoCategory(loCategoryId);

        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (MissingParameterException e) {
            e.printStackTrace();
        } catch (OperationFailedException e) {
            e.printStackTrace();
        } catch (DoesNotExistException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This overridden method ...
     * 
     * @see org.kuali.student.lum.lu.ui.course.client.service.LoRpcService#getLoCategoryType(java.lang.String)
     */
    @Override
    public LoCategoryTypeInfo getLoCategoryType(String loCategoryTypeKey) {
        try {
            return service.getLoCategoryType(loCategoryTypeKey);

        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (MissingParameterException e) {
            e.printStackTrace();
        } catch (OperationFailedException e) {
            e.printStackTrace();
        } catch (DoesNotExistException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<LoCategoryInfo> getLoCategories(String loRepositoryKey) {
        try {
            return service.getLoCategories(loRepositoryKey);

        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (MissingParameterException e) {
            e.printStackTrace();
        } catch (OperationFailedException e) {
            e.printStackTrace();
        } catch (DoesNotExistException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public StatusInfo deleteLoCategory(String loCategoryId) {
        try {
             return  service.deleteLoCategory(loCategoryId);
            } catch (InvalidParameterException e) {
                e.printStackTrace();
            } catch (MissingParameterException e) {
                e.printStackTrace();
            } catch (OperationFailedException e) {
                e.printStackTrace();
            } catch (DoesNotExistException e) {
                e.printStackTrace();
            } catch (DependentObjectsExistException e) {
                e.printStackTrace();
            } catch (PermissionDeniedException e) {
                e.printStackTrace();
            }
            return null;
    }

    @Override
    public LoCategoryInfo updateLoCategory(String loCategoryId, LoCategoryInfo loCategoryInfo) {
        try {
            
            return service.updateLoCategory(loCategoryId, loCategoryInfo);

            } catch (InvalidParameterException e) {
                e.printStackTrace();
            } catch (MissingParameterException e) {
                e.printStackTrace();
            } catch (OperationFailedException e) {
                e.printStackTrace();
            } catch (DoesNotExistException e) {
                e.printStackTrace();
            } catch (DataValidationErrorException e) {
                e.printStackTrace();
            } catch (PermissionDeniedException e) {
                e.printStackTrace();
            } catch (VersionMismatchException e) {
                e.printStackTrace();
            }
            return null;
    }
    @Override
    public List<LoInfo> getLosByLoCategory(String loCategoryId){
        try {
            return service.getLosByLoCategory(loCategoryId);
        } catch (DoesNotExistException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (MissingParameterException e) {
            e.printStackTrace();
        } catch (OperationFailedException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
}


