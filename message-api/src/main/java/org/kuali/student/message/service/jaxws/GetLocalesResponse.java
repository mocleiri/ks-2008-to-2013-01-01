
package org.kuali.student.message.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.1.3
 * Fri Jan 09 10:52:53 PST 2009
 * Generated source version: 2.1.3
 */

@XmlRootElement(name = "getLocalesResponse", namespace = "http://student.kuali.org/wsdl/MessageService")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getLocalesResponse", namespace = "http://student.kuali.org/wsdl/MessageService")

public class GetLocalesResponse {

    @XmlElement(name = "return")
    private org.kuali.student.message.dto.LocaleKeyList _return;

    public org.kuali.student.message.dto.LocaleKeyList getReturn() {
        return this._return;
    }

    public void setReturn(org.kuali.student.message.dto.LocaleKeyList new_return)  {
        this._return = new_return;
    }

}

