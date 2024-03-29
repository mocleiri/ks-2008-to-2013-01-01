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


package org.kuali.student.lum.lo.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.2
 * Fri Feb 19 15:17:58 PST 2010
 * Generated source version: 2.2
 */

@XmlRootElement(name = "getLosByRepository", namespace = "http://student.kuali.org/wsdl/lo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getLosByRepository", namespace = "http://student.kuali.org/wsdl/lo", propOrder = {"loRepositoryKey","loTypeKey","loStateKey"})

public class GetLosByRepository {

    @XmlElement(name = "loRepositoryKey")
    private java.lang.String loRepositoryKey;
    @XmlElement(name = "loTypeKey")
    private java.lang.String loTypeKey;
    @XmlElement(name = "loStateKey")
    private java.lang.String loStateKey;

    public java.lang.String getLoRepositoryKey() {
        return this.loRepositoryKey;
    }

    public void setLoRepositoryKey(java.lang.String newLoRepositoryKey)  {
        this.loRepositoryKey = newLoRepositoryKey;
    }

    public java.lang.String getLoTypeKey() {
        return this.loTypeKey;
    }

    public void setLoTypeKey(java.lang.String newLoTypeKey)  {
        this.loTypeKey = newLoTypeKey;
    }

    public java.lang.String getLoStateKey() {
        return this.loStateKey;
    }

    public void setLoStateKey(java.lang.String newLoStateKey)  {
        this.loStateKey = newLoStateKey;
    }

}

