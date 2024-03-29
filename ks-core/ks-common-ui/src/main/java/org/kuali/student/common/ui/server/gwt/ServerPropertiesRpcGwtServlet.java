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

package org.kuali.student.common.ui.server.gwt;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kuali.rice.core.config.ConfigContext;
import org.kuali.student.common.ui.client.service.ServerPropertiesRpcService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ServerPropertiesRpcGwtServlet extends RemoteServiceServlet
		implements ServerPropertiesRpcService {

	final Logger logger = Logger.getLogger(ServerPropertiesRpcGwtServlet.class);

	Map<String, String> properties = new HashMap<String, String>();

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@Override
	public String get(String property) {
		String value = properties.get(property);
		logger.info("Getting property: " + property + " with value: " + value);
		if(null==value){
			value = ConfigContext.getCurrentContextConfig().getProperty(property);
			logger.info("Property not found, looking in Context: " + property + " with value: " + value);
		}
		return value;
	}

	@Override
	public Map<String, String> get(List<String> properties) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (String property : properties) {
			map.put(property, get(property));
		}
		return map;
	}

}
