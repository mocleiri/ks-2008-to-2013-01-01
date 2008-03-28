
package org.kuali.student.poc.wsdl.personidentity.person.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.student.poc.xsd.personidentity.person.dto.PersonAttributeSetTypeDisplay;

/**
 * This class was generated by the CXF 2.0.4-incubator
 * Thu Mar 27 11:42:24 EDT 2008
 * Generated source version: 2.0.4-incubator
 * 
 */

@XmlRootElement(name = "findPersonAttributeSetTypesResponse", namespace = "http://student.kuali.org/poc/wsdl/personidentity/person")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findPersonAttributeSetTypesResponse", namespace = "http://student.kuali.org/poc/wsdl/personidentity/person")

public class FindPersonAttributeSetTypesResponse {

    @XmlElement(name = "return")
    private java.util.List<PersonAttributeSetTypeDisplay> _return;

    public java.util.List<PersonAttributeSetTypeDisplay> get_return() {
        return this._return;
    }
    
    public void set_return( java.util.List<PersonAttributeSetTypeDisplay> new_return ) {
        this._return = new_return;
    }
    
}

