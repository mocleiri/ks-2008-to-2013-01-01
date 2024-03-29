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

package org.kuali.student.brms.repository.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class RuleSetVerificationResultInfo implements java.io.Serializable {

	/** Class serial version uid */
    private static final long serialVersionUID = 1L;

    @XmlElement
    private Boolean ruleSetValid = false;

    @XmlElement
    private List<String> messageList = new ArrayList<String>();

    public RuleSetVerificationResultInfo() {}

	public Boolean isRuleSetValid() {
		return this.ruleSetValid;
	}

	public void setRuleSetValid(Boolean isRuleValid) {
		this.ruleSetValid = isRuleValid;
	}

	public List<String> getMessages() {
		return this.messageList;
	}

	public void setMessages(List<String> messages) {
		this.messageList = messages;
	}

    public String getMessage() {
    	StringBuilder errorMessage = new StringBuilder();
    	for(String msg : this.messageList) {
    		errorMessage.append(msg);
    	}
    	return errorMessage.toString();
    }
}
