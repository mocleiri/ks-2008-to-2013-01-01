
package org.kuali.student.lum.lu.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.1.4
 * Tue Feb 24 12:25:30 EST 2009
 * Generated source version: 2.1.4
 */

@XmlRootElement(name = "getCluIdsByLuType", namespace = "http://student.kuali.org/wsdl/lu")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCluIdsByLuType", namespace = "http://student.kuali.org/wsdl/lu", propOrder = {"luTypeKey","luState"})

public class GetCluIdsByLuType {

    @XmlElement(name = "luTypeKey")
    private java.lang.String luTypeKey;
    @XmlElement(name = "luState")
    private java.lang.String luState;

    public java.lang.String getLuTypeKey() {
        return this.luTypeKey;
    }

    public void setLuTypeKey(java.lang.String newLuTypeKey)  {
        this.luTypeKey = newLuTypeKey;
    }

    public java.lang.String getLuState() {
        return this.luState;
    }

    public void setLuState(java.lang.String newLuState)  {
        this.luState = newLuState;
    }

}

