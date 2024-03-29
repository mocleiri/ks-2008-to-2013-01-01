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


package org.kuali.student.brms.repository.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.2
 * Mon Jul 13 20:53:46 PDT 2009
 * Generated source version: 2.2
 */

@XmlRootElement(name = "createRuleSet", namespace = "http://student.kuali.org/wsdl/brms/RuleRepository")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createRuleSet", namespace = "http://student.kuali.org/wsdl/brms/RuleRepository")

public class CreateRuleSet {

    @XmlElement(name = "ruleSet")
    private org.kuali.student.brms.repository.dto.RuleSetInfo ruleSet;

    public org.kuali.student.brms.repository.dto.RuleSetInfo getRuleSet() {
        return this.ruleSet;
    }

    public void setRuleSet(org.kuali.student.brms.repository.dto.RuleSetInfo newRuleSet)  {
        this.ruleSet = newRuleSet;
    }

}

