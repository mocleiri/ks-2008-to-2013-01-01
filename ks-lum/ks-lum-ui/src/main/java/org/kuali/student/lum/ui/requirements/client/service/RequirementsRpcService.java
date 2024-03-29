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

package org.kuali.student.lum.ui.requirements.client.service;

import java.util.List;

import org.kuali.student.common.ui.client.service.BaseRpcService;
import org.kuali.student.brms.statement.dto.ReqCompFieldInfo;
import org.kuali.student.brms.statement.dto.ReqComponentInfo;
import org.kuali.student.brms.statement.dto.ReqComponentTypeInfo;
import org.kuali.student.lum.ui.requirements.client.model.StatementVO;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpcservices/RequirementsRpcService")
public interface RequirementsRpcService extends BaseRpcService {
    public String getNaturalLanguageForReqComponentInfo(ReqComponentInfo compInfo, String nlUsageTypeKey, String language) throws Exception;
    public String getNaturalLanguageForStatementVO(String cluId, StatementVO statementVO, String nlUsageTypeKey, String language) throws Exception;  
    public List<ReqComponentTypeInfo> getReqComponentTypesForLuStatementType(String luStatementTypeKey) throws Exception;   
	public List<ReqCompFieldInfo> verifyFieldsAndSetIds(List<ReqCompFieldInfo> editedFields) throws Exception;
	public String retrieveCluCode(String cluId) throws Exception;	
}
