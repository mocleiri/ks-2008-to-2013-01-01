
package org.kuali.student.common.dictionary.service.old.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.2
 * Fri May 14 11:26:06 PDT 2010
 * Generated source version: 2.2
 */

@Deprecated
@XmlRootElement(name = "getObjectStructure", namespace = "http://student.kuali.org/wsdl/dictionary")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getObjectStructure", namespace = "http://student.kuali.org/wsdl/dictionary")

public class GetObjectStructure {

    @XmlElement(name = "objectTypeKey")
    private java.lang.String objectTypeKey;

    public java.lang.String getObjectTypeKey() {
        return this.objectTypeKey;
    }

    public void setObjectTypeKey(java.lang.String newObjectTypeKey)  {
        this.objectTypeKey = newObjectTypeKey;
    }

}

