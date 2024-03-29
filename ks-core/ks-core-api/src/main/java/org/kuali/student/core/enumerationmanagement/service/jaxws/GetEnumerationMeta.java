/**
 * Copyright 2010 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.student.core.enumerationmanagement.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.2
 * Fri May 15 12:08:08 EDT 2009
 * Generated source version: 2.2
 */

@XmlRootElement(name = "getEnumerationMeta", namespace = "http://student.kuali.org/wsdl/EnumerationManagementService")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getEnumerationMeta", namespace = "http://student.kuali.org/wsdl/EnumerationManagementService")

public class GetEnumerationMeta {

    @XmlElement(name = "enumerationKey")
    private java.lang.String enumerationKey;

    public java.lang.String getEnumerationKey() {
        return this.enumerationKey;
    }

    public void setEnumerationKey(java.lang.String newEnumerationKey)  {
        this.enumerationKey = newEnumerationKey;
    }

}

