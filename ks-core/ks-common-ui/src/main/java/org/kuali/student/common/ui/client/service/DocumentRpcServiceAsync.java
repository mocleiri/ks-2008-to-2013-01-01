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

package org.kuali.student.common.ui.client.service;

import java.util.List;

import org.kuali.student.core.document.dto.DocumentInfo;
import org.kuali.student.core.dto.StatusInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DocumentRpcServiceAsync extends BaseRpcServiceAsync{
	public void getDocument(String documentId, AsyncCallback<DocumentInfo> callback) throws Exception;
	
	public void getDocumentsByIdList(List<String> documentIdList, AsyncCallback<List<DocumentInfo>> callback) throws Exception;
	
	public void deleteDocument(String documentId, AsyncCallback<StatusInfo> callback) throws Exception;
	
    public void updateDocument(String documentId, DocumentInfo documentInfo, AsyncCallback<DocumentInfo> callback) throws Exception;
	
	public void addDocumentCategoryToDocument(String documentId, String documentCategoryKey, AsyncCallback<StatusInfo> callback) throws Exception;
	
    public void removeDocumentCategoryFromDocument(String documentId, String documentCategoryKey, AsyncCallback<StatusInfo> callback) throws Exception;

    /**
     * Check for authorization to upload documents
     * @param referenceId identifier of reference
     * @param referenceTypeKey reference type
     */
    public void isAuthorizedUploadDocuments(String referenceId, String referenceTypeKey, AsyncCallback<Boolean> callback);
}
