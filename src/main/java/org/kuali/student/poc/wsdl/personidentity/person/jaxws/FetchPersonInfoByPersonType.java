
package org.kuali.student.poc.wsdl.personidentity.person.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by the CXF 2.0.4-incubator
 * Thu Mar 27 11:42:25 EDT 2008
 * Generated source version: 2.0.4-incubator
 * 
 */

@XmlRootElement(name = "fetchPersonInfoByPersonType", namespace = "http://student.kuali.org/poc/wsdl/personidentity/person")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fetchPersonInfoByPersonType", namespace = "http://student.kuali.org/poc/wsdl/personidentity/person")

public class FetchPersonInfoByPersonType {

    @XmlElement(name = "personId")
    private java.lang.Long personId;
    @XmlElement(name = "personTypeKey")
    private java.lang.Long personTypeKey;

    public java.lang.Long getPersonId() {
        return this.personId;
    }
    
    public void setPersonId( java.lang.Long newPersonId ) {
        this.personId = newPersonId;
    }
    
    public java.lang.Long getPersonTypeKey() {
        return this.personTypeKey;
    }
    
    public void setPersonTypeKey( java.lang.Long newPersonTypeKey ) {
        this.personTypeKey = newPersonTypeKey;
    }
    
}

