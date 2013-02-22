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

package org.kuali.student.krms.naturallanguage;

import org.kuali.rice.krms.impl.repository.PropositionBo;
import org.kuali.student.r1.core.statement.entity.ReqComponent;
import org.kuali.student.r1.core.statement.entity.Statement;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;

/**
 * This class translates requirement components and statements into 
 * natural language.
 */
public interface NaturalLanguageTranslator {
	/**
	 * Translates a requirement component for a specific natural language 
	 * usuage type (context) into natural language.
	 * 
	 * @param proposition Requirement component to be translated
	 * @param nlUsageTypeKey Natural language usage type key (context)
	 * @return Natural language requirement translation
	 * @throws org.kuali.student.r2.common.exceptions.DoesNotExistException Requirement component id does not exists
	 * @throws org.kuali.student.r2.common.exceptions.OperationFailedException
	 */
	public String translateProposition(PropositionBo proposition, String nlUsageTypeKey) throws DoesNotExistException, OperationFailedException;

	/**
	 * Translates a requirement component for a specific natural language
	 * usuage type (context) and language locale (e.g. 'en' for English,
	 * 'de' for German) into natural language.
	 *
	 * @param proposition Requirement component to be translated
	 * @param nlUsageTypeKey Natural language usage type key (context)
	 * @param language Translation language
	 * @return
	 * @throws org.kuali.student.r2.common.exceptions.DoesNotExistException
	 * @throws org.kuali.student.r2.common.exceptions.OperationFailedException
	 */
	public String translateProposition(PropositionBo proposition, String nlUsageTypeKey, String language) throws DoesNotExistException, OperationFailedException;


}
