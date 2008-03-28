
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

@XmlRootElement(name = "createPerson", namespace = "http://student.kuali.org/poc/wsdl/personidentity/person")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createPerson", namespace = "http://student.kuali.org/poc/wsdl/personidentity/person")

public class CreatePerson {

    @XmlElement(name = "personCreateInfo")
    private org.kuali.student.poc.xsd.personidentity.person.dto.PersonCreateInfo personCreateInfo;
    @XmlElement(name = "personTypeKeys")
    private java.util.List<Long> personTypeKeys;

    public org.kuali.student.poc.xsd.personidentity.person.dto.PersonCreateInfo getPersonCreateInfo() {
        return this.personCreateInfo;
    }
    
    public void setPersonCreateInfo( org.kuali.student.poc.xsd.personidentity.person.dto.PersonCreateInfo newPersonCreateInfo ) {
        this.personCreateInfo = newPersonCreateInfo;
    }
    
    public java.util.List<Long> getPersonTypeKeys() {
        return this.personTypeKeys;
    }
    
    public void setPersonTypeKeys( java.util.List<Long> newPersonTypeKeys ) {
        this.personTypeKeys = newPersonTypeKeys;
    }
    
}

