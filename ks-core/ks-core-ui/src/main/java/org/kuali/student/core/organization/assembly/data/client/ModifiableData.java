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

package org.kuali.student.core.organization.assembly.data.client;

import org.kuali.student.core.assembly.data.Data;
import org.kuali.student.core.assembly.helper.PropertyEnum;

public class ModifiableData extends Data {
	public enum Properties implements PropertyEnum {
		MODIFICATIONS("modifications");

		private final String key;

		private Properties(final String key) {
			this.key = key;
		}

		@Override
		public String getKey() {
			return this.key;
		}
	}
	private static final long serialVersionUID = 1L;
	
	public ModifiableData() {
		super();
	}
	public ModifiableData(String className) {
		super(className);
	}
	
	public ModificationData getModifications() {
		ModificationData result = super.get(Properties.MODIFICATIONS.getKey());
		if (result == null) {
			result = new ModificationData();
			super.set(Properties.MODIFICATIONS.getKey(), result);
		}
		return result;
	}
	public void setModifications(ModificationData modifications) {
		super.set(Properties.MODIFICATIONS.getKey(), modifications);
	}
}
