/**
 * Copyright 2005-2012 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.enrollment.class1.krms.dto;

import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameterType;
import org.kuali.rice.krms.api.repository.proposition.PropositionType;
import org.kuali.rice.krms.impl.repository.PropositionParameterBo;
import org.kuali.rice.krms.impl.repository.TermBo;

import java.util.List;

/**
 * abstract data class for the rule tree {@link org.kuali.rice.core.api.util.tree.Node}s
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SimpleStudentPropositionEditNode extends RuleEditorTreeNode {

    private static final long serialVersionUID = -5650654824214437325L;

    public static final String NODE_TYPE = "ruleTreeNode simplePropositionEditNode";
    protected String parameterDisplayString;

    public SimpleStudentPropositionEditNode(PropositionEditor proposition){
        super(proposition);
        setupParameterDisplayString();
    }
    
    private void setupParameterDisplayString(){
        if (proposition != null && proposition.getProposition().getPropositionTypeCode().equalsIgnoreCase(PropositionType.SIMPLE.getCode())){
            // Simple Propositions should have 3 parameters ordered in reverse polish notation.
            // TODO: enhance to get term names for term type parameters.
            List<PropositionParameterBo> parameters = proposition.getProposition().getParameters();
            if (parameters != null && parameters.size() == 3){
                setParameterDisplayString(getParamValue(parameters.get(0)) 
                        + " " + getParamValue(parameters.get(2))
                        + " " + getParamValue(parameters.get(1)));
            } else {
                // should not happen
            }
        }
    }
    
    private String getParamValue(PropositionParameterBo prop){
        if (PropositionParameterType.TERM.getCode().equalsIgnoreCase(prop.getParameterType())){
            //TODO: use termBoService
            String termName = "";
            String termId = prop.getValue();
            if (termId != null && termId.length() > 0){
                TermBo term = getBoService().findBySinglePrimaryKey(TermBo.class,termId);
                if (term != null){
                    termName = term.getSpecification().getName();
                }
            }
            return termName;
        } else {
            return prop.getValue();
        }
    }
    /**
     * @return the parameterDisplayString
     */
    public String getParameterDisplayString() {
        return this.parameterDisplayString;
    }

    /**
     * @param parameterDisplayString the parameterDisplayString to set
     */
    public void setParameterDisplayString(String parameterDisplayString) {
        this.parameterDisplayString = parameterDisplayString;
    }

    public BusinessObjectService getBoService() {
        return KRADServiceLocator.getBusinessObjectService();
    }
    
}
