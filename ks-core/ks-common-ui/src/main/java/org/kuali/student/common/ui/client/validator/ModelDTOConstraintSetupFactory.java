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

package org.kuali.student.common.ui.client.validator;

import org.kuali.student.common.ui.client.application.Application;
import org.kuali.student.common.validator.ConstraintDataProvider;
import org.kuali.student.common.validator.ConstraintSetupFactory;
import org.kuali.student.core.dictionary.dto.ObjectStructure;

public class ModelDTOConstraintSetupFactory implements ConstraintSetupFactory {
  
	@Override
	public ConstraintDataProvider getDataProvider(Object obj) {

		ModelDTOConstraintDataProvider mdp = new ModelDTOConstraintDataProvider();
		mdp.initialize(obj);

		return mdp;
	}

	@Override
	public ObjectStructure getObjectStructure(String key) {
		return Application.getApplicationContext().getDictionaryData(key);
	}

}
