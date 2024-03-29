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

package org.kuali.student.lum.lu.ui.main.client.view;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.kuali.student.common.ui.client.mvc.Controller;
import org.kuali.student.common.ui.client.mvc.ViewComposite;
import org.kuali.student.common.ui.client.service.ServerPropertiesRpcService;
import org.kuali.student.common.ui.client.service.ServerPropertiesRpcServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;

public class ActionListView extends ViewComposite {
    private final String ACTION_LIST_URL	= "ks.rice.actionList.serviceAddress";
	
    private ServerPropertiesRpcServiceAsync serverPropertiesRpcService = GWT.create(ServerPropertiesRpcService.class);
    
	private Frame actionList = new Frame();
	private boolean loaded = false;
	
	private String actionListUrl = null;
	
	public ActionListView(Controller controller, String name) {
		super(controller, name);
		actionList.addStyleName("KS-Action-List");
		initWidget(actionList);
	}

	@Override
	protected void onLoad() {
	    if (!loaded && actionListUrl == null){
			List<String> serverPropertyList = Arrays.asList(ACTION_LIST_URL);
			
	        serverPropertiesRpcService.get(serverPropertyList, new AsyncCallback<Map<String,String>>() {
	            public void onFailure(Throwable caught) {
	            	loaded = false;
	            }
	            
	            public void onSuccess(Map<String,String> result) {
	                if(result != null){
	                	String actionListUrl 	= result.get(ACTION_LIST_URL);
	            	    actionList.setUrl(actionListUrl);
	            	    loaded = true;
	                }
	            }	            
	        });	    	
	    } else {
	    	actionList.setUrl(actionListUrl);
	    }
	}

}
