
package org.kuali.student.core.statement.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.2
 * Wed May 12 12:54:47 PDT 2010
 * Generated source version: 2.2
 */

@XmlRootElement(name = "getReqComponentTypesForStatementType", namespace = "http://student.kuali.org/wsdl/statement")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getReqComponentTypesForStatementType", namespace = "http://student.kuali.org/wsdl/statement")

public class GetReqComponentTypesForStatementType {

    @XmlElement(name = "statementTypeKey")
    private java.lang.String statementTypeKey;

    public java.lang.String getStatementTypeKey() {
        return this.statementTypeKey;
    }

    public void setStatementTypeKey(java.lang.String newStatementTypeKey)  {
        this.statementTypeKey = newStatementTypeKey;
    }

}

