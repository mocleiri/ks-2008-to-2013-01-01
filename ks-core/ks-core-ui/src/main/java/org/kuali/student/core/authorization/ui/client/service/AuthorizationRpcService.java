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

package org.kuali.student.core.authorization.ui.client.service;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("rpcservices/AuthorizationRpcService")
public interface AuthorizationRpcService extends RemoteService {
	public Boolean isAuthorizedForPermission(String namespace, String permissionTemplateName);
	public Boolean isAuthorizedForPermissionWithQualifications(String namespace, String permissionTemplateName, Map<String,String> roleQualifications);
	public Boolean isAuthorizedForPermissionWithDetailsAndQualifications(String namespace, String permissionTemplateName, Map<String,String> roleQualifications, Map<String,String> permissionDetails);
}
