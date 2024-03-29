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

package org.kuali.student.core.document.dao;

import java.util.List;

import org.kuali.student.core.dao.CrudDao;
import org.kuali.student.core.dao.SearchableDao;
import org.kuali.student.core.document.entity.Document;
import org.kuali.student.core.document.entity.DocumentCategory;
import org.kuali.student.core.exceptions.DoesNotExistException;

/**
 * This is a description of what this class does - lindholm don't forget to fill this in.
 *
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public interface DocumentDao extends CrudDao, SearchableDao {

    public Boolean addDocumentCategoryToDocument(String documentId, String documentCategoryKey) throws DoesNotExistException;

    public List<DocumentCategory> getCategoriesByDocument(String documentId);

    public List<Document> getDocumentsByIdList(List<String> documentIdList) throws DoesNotExistException;

    public Boolean removeDocumentCategoryFromDocument(String documentId, String documentCategoryKey) throws DoesNotExistException;

}
