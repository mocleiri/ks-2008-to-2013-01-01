
package org.kuali.student.lum.lu.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.student.lum.lu.dto.LuDocRelationInfo;

/**
 * This class was generated by Apache CXF 2.1.4
 * Tue Feb 24 12:25:30 EST 2009
 * Generated source version: 2.1.4
 */

@XmlRootElement(name = "getLuDocRelationsByDocumentResponse", namespace = "http://student.kuali.org/lum/lu")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getLuDocRelationsByDocumentResponse", namespace = "http://student.kuali.org/lum/lu")

public class GetLuDocRelationsByDocumentResponse {

    @XmlElement(name = "return")
    private java.util.List<LuDocRelationInfo> _return;

    public java.util.List<LuDocRelationInfo> getReturn() {
		if(_return==null){
			_return = new java.util.ArrayList<LuDocRelationInfo>();
		}
        return this._return;
    }

    public void setReturn(java.util.List<LuDocRelationInfo> new_return)  {
        this._return = new_return;
    }

}

