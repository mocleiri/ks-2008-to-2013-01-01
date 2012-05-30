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

import org.kuali.student.r1.core.atp.dto.AtpDurationTypeInfo;
import org.kuali.student.r1.core.atp.service.AtpService;
import org.kuali.student.r1.core.statement.dto.ReqComponentInfo;
import org.kuali.student.r1.lum.statement.typekey.ReqComponentFieldTypes;
import org.kuali.student.r2.common.exceptions.OperationFailedException;


/**
 * This class creates the template context for an academic time period.
 */
public class AtpContextImpl extends BasicContextImpl {
 
	private AtpService atpService;
	
	public final static String DURATION_TYPE_TOKEN = "durationType";
	public final static String DURATION_TOKEN = "duration";

	public void setAtpService(AtpService atpService) {
		this.atpService = atpService;
	}

	private AtpDurationTypeInfo getAtpDurationType(String atpDurationTypeKey) throws OperationFailedException {
		if (atpDurationTypeKey == null) {
			return null;
		}
		try {
			AtpDurationTypeInfo atpDurationType = this.atpService.getAtpDurationType(atpDurationTypeKey);
			return atpDurationType;
		} catch (Exception e) {
			throw new OperationFailedException(e.getMessage(), e);
		}
	}
	
    /**
     * Creates the context map (template data) for the requirement component.
     * 
     * @param reqComponent Requirement component
     * @throws OperationFailedException Creating context map fails
     */
    public Map<String, Object> createContextMap(ReqComponentInfo reqComponent) throws OperationFailedException {
        String durationTypeKey = getReqComponentFieldValue(reqComponent, ReqComponentFieldTypes.DURATION_TYPE_KEY.getId());
        String duration = getReqComponentFieldValue(reqComponent, ReqComponentFieldTypes.DURATION_KEY.getId());
        AtpDurationTypeInfo atpDurationType = getAtpDurationType(durationTypeKey);

        Map<String, Object> contextMap = super.createContextMap(reqComponent);
        contextMap.put(DURATION_TYPE_TOKEN, atpDurationType);
        contextMap.put(DURATION_TOKEN, duration);
        return contextMap;
    }
}
