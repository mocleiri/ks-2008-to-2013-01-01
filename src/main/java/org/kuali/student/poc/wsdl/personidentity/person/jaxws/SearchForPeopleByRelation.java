
package org.kuali.student.poc.wsdl.personidentity.person.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by the CXF 2.0.4-incubator
 * Thu Mar 27 11:42:24 EDT 2008
 * Generated source version: 2.0.4-incubator
 * 
 */

@XmlRootElement(name = "searchForPeopleByRelation", namespace = "http://student.kuali.org/poc/wsdl/personidentity/person")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchForPeopleByRelation", namespace = "http://student.kuali.org/poc/wsdl/personidentity/person")

public class SearchForPeopleByRelation {

    @XmlElement(name = "personRelationCriteria")
    private org.kuali.student.poc.xsd.personidentity.person.dto.PersonRelationCriteria personRelationCriteria;

    public org.kuali.student.poc.xsd.personidentity.person.dto.PersonRelationCriteria getPersonRelationCriteria() {
        return this.personRelationCriteria;
    }
    
    public void setPersonRelationCriteria( org.kuali.student.poc.xsd.personidentity.person.dto.PersonRelationCriteria newPersonRelationCriteria ) {
        this.personRelationCriteria = newPersonRelationCriteria;
    }
    
}

