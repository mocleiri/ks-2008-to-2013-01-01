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

package org.kuali.student.rice.config.spring;

/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;
import java.util.Properties;

import org.kuali.rice.core.config.Config;
import org.kuali.rice.core.config.ConfigContext;
import org.kuali.rice.core.config.ConfigurationException;
import org.springframework.beans.factory.FactoryBean;

public class NestedConfigFactoryBean implements FactoryBean {

	private List<String> configLocations;

	public static String CONFIG_OVERRIDE_LOCATION;

	public Object getObject() throws Exception {
		if (getConfigLocations() == null) {
			throw new ConfigurationException(
					"No config locations declared, at least one is required");
		}
		Properties baseProperties = new Properties();
		if (ConfigContext.getCurrentContextConfig() != null) {
			baseProperties = ConfigContext.getCurrentContextConfig()
					.getProperties();
		}
		NestedSimpleConfig config = null;
		if (CONFIG_OVERRIDE_LOCATION != null) {
			config = new NestedSimpleConfig(CONFIG_OVERRIDE_LOCATION, baseProperties);
		} else {
			config = new NestedSimpleConfig(getConfigLocations(), baseProperties);
		}

		config.parseConfig();
		return config;
	}

	public Class<?> getObjectType() {
		return Config.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public List<String> getConfigLocations() {
		return this.configLocations;
	}

	public void setConfigLocations(List<String> configLocations) {
		this.configLocations = configLocations;
	}

}
