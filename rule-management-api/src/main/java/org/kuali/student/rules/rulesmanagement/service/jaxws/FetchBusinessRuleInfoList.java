
package org.kuali.student.rules.rulesmanagement.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.1
 * Fri Sep 05 14:53:07 EDT 2008
 * Generated source version: 2.1
 */

@XmlRootElement(name = "fetchBusinessRuleInfoList", namespace = "http://student.kuali.org/poc/wsdl/brms/rulesmanagement")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fetchBusinessRuleInfoList", namespace = "http://student.kuali.org/poc/wsdl/brms/rulesmanagement")

public class FetchBusinessRuleInfoList {

    @XmlElement(name = "businessRuleAnchorInfoList")
    private org.kuali.student.rules.rulesmanagement.dto.BusinessRuleAnchorDTO businessRuleAnchorInfoList;

    public org.kuali.student.rules.rulesmanagement.dto.BusinessRuleAnchorDTO getBusinessRuleAnchorInfoList() {
        return this.businessRuleAnchorInfoList;
    }

    public void setBusinessRuleAnchorInfoList(org.kuali.student.rules.rulesmanagement.dto.BusinessRuleAnchorDTO newBusinessRuleAnchorInfoList)  {
        this.businessRuleAnchorInfoList = newBusinessRuleAnchorInfoList;
    }

}

