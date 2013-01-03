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

package org.kuali.student.r1.common.messages.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.student.r1.common.messages.dto.Message;

/**
 * This class was generated by Apache CXF 2.1.3
 * Fri Jan 09 10:52:53 PST 2009
 * Generated source version: 2.1.3
 */

@Deprecated
@XmlRootElement(name = "updateMessage", namespace = "http://student.kuali.org/wsdl/messages")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateMessage", namespace = "http://student.kuali.org/wsdl/messages", propOrder = {"localeKey","messageGroupKey","messageKey","messageInfo"})

public class UpdateMessage {

    @XmlElement(name = "localeKey")
    private java.lang.String localeKey;
    @XmlElement(name = "messageGroupKey")
    private java.lang.String messageGroupKey;
    @XmlElement(name = "messageKey")
    private java.lang.String messageKey;
    @XmlElement(name = "messageInfo")
    private Message messageInfo;

    public String getLocaleKey() {
        return this.localeKey;
    }

    public void setLocaleKey(String newLocaleKey)  {
        this.localeKey = newLocaleKey;
    }

    public String getMessageGroupKey() {
        return this.messageGroupKey;
    }

    public void setMessageGroupKey(String newMessageGroupKey)  {
        this.messageGroupKey = newMessageGroupKey;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public void setMessageKey(String newMessageKey)  {
        this.messageKey = newMessageKey;
    }

    public Message getMessageInfo() {
        return this.messageInfo;
    }

    public void setMessageInfo(Message newMessageInfo)  {
        this.messageInfo = newMessageInfo;
    }

}

