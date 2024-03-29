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

package org.kuali.student.core.organization.ui.client.mvc.controller;

import org.kuali.student.common.ui.client.mvc.Callback;
import org.kuali.student.common.ui.client.mvc.Controller;
import org.kuali.student.common.ui.client.mvc.DelegatingViewComposite;
import org.kuali.student.common.ui.client.mvc.View;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;




public class OrgApplicationManager extends Controller{
    private final SimplePanel viewPanel = new SimplePanel();
    private Controller orgProposalController = null;
    private DelegatingViewComposite createOrgView = null;

    
    public OrgApplicationManager(){
        super(OrgApplicationManager.class.getName());
        super.initWidget(viewPanel);
    }

    public enum ORGViews {
        CREATE_ORG
    }
    @Override
    protected <V extends Enum<?>> View getView(V viewType) {
        switch ((ORGViews) viewType) {
            case CREATE_ORG:
//                initBlankCluProposalView(LUConstants.PROPOSAL_TYPE_COURSE_CREATE, LUConstants.CLU_TYPE_CREDIT_COURSE);
                initOrgView();
                //FIXME: This is a quick fix, need better way to reset view
                orgProposalController.showDefaultView(NO_OP_CALLBACK);  

                return createOrgView;
            default:
                return null;
        }
        
    }

    @Override
    public Class<? extends Enum<?>> getViewsEnum() {
        
        return ORGViews.class;
    }
    
    @Override
    public Enum<?> getViewEnumValue(String enumValue) {
        return ORGViews.valueOf(enumValue);
    }

    @Override
    protected void hideView(View view) {
        
        viewPanel.clear();
    }

    @Override
    protected void renderView(View view) {
        viewPanel.setWidget((Composite)view);
        
    }

    @Override
    public void showDefaultView(final Callback<Boolean> onReadyCallback) {
        initOrgView();
        showView(ORGViews.CREATE_ORG, onReadyCallback);
        
    }
    
    private View initOrgView(){
        orgProposalController = new OrgProposalController();
        createOrgView = new DelegatingViewComposite(OrgApplicationManager.this,orgProposalController);
        return createOrgView;
        
    }

}
