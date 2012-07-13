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

package org.kuali.student.lum.statement.config.context;

import java.util.Map;

//import org.kuali.student.r2.core.atp.dto.AtpDurationTypeInfo;
import org.kuali.student.r2.common.dto.TypeInfo;
import org.kuali.student.r2.common.exceptions.*;
import org.kuali.student.r2.common.type.service.TypeService;
import org.kuali.student.r2.common.util.ContextUtils;
import org.kuali.student.r2.core.atp.service.AtpService;
import org.kuali.student.r1.core.statement.dto.ReqComponentInfo;
import org.kuali.student.r1.lum.statement.typekey.ReqComponentFieldTypes;
import org.kuali.student.r2.common.dto.ContextInfo;


/**
 * This class creates the template context for an academic time period.
 */
public class AtpContextImpl extends BasicContextImpl {
 
    private TypeService typeService;
	
	public final static String DURATION_TYPE_TOKEN = "durationType";
	public final static String DURATION_TOKEN = "duration";


    public TypeService getTypeService() {
        return typeService;
    }

    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    /**
     * Creates the context map (template data) for the requirement component.
     * 
     *
     *
     * @param reqComponent Requirement component
     * @param contextInfo
     * @throws OperationFailedException Creating context map fails
     */
    public Map<String, Object> createContextMap(ReqComponentInfo reqComponent, ContextInfo contextInfo) throws OperationFailedException {
        String durationTypeKey = getReqComponentFieldValue(reqComponent, ReqComponentFieldTypes.DURATION_TYPE_KEY.getId());
        String duration = getReqComponentFieldValue(reqComponent, ReqComponentFieldTypes.DURATION_KEY.getId());
        TypeInfo atpType;

        try {
            atpType = getTypeService().getType(durationTypeKey, ContextUtils.getContextInfo());
        } catch (DoesNotExistException e) {
            throw new OperationFailedException("Error getting ATP Type from Type Service", e);
        } catch (InvalidParameterException e) {
            throw new OperationFailedException("Error getting ATP Type from Type Service", e);
        } catch (MissingParameterException e) {
            throw new OperationFailedException("Error getting ATP Type from Type Service", e);
        } catch (PermissionDeniedException e) {
            throw new OperationFailedException("Error getting ATP Type from Type Service", e);
        }

        Map<String, Object> contextMap = super.createContextMap(reqComponent, contextInfo);
        contextMap.put(DURATION_TYPE_TOKEN, atpType);
        contextMap.put(DURATION_TOKEN, duration);
        return contextMap;
    }
}
