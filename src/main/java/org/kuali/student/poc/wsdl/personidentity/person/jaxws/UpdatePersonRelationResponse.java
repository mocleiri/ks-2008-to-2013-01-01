
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

@XmlRootElement(name = "updatePersonRelationResponse", namespace = "http://student.kuali.org/poc/wsdl/personidentity/person")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updatePersonRelationResponse", namespace = "http://student.kuali.org/poc/wsdl/personidentity/person")

public class UpdatePersonRelationResponse {

    @XmlElement(name = "return")
    private boolean _return;

    public boolean get_return() {
        return this._return;
    }
    
    public void set_return( boolean new_return ) {
        this._return = new_return;
    }
    
}

