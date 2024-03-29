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

package org.kuali.student.brms.statement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.core.entity.Attribute;

@Entity
@Table(name = "KSST_STMT_TYPE_HEADER_TMPL")
public class StatementTypeHeaderTemplate extends Attribute<StatementType> {
	@ManyToOne
	@JoinColumn(name = "OWNER")
	private StatementType owner;
	
	@Column(name="NL_USUAGE_TYPE_KEY")
	private String nlUsageTypeKey;

	@Column(name="TEMPLATE", length=2000)
	private String template;

	@Column(name="LANGUAGE", length=2)
	private String language;

	@Override
	public StatementType getOwner() {
		return owner;
	}

	@Override
	public void setOwner(StatementType owner) {
		this.owner = owner;
	}

	public String getNlUsageTypeKey() {
		return nlUsageTypeKey;
	}

	public void setNlUsageTypeKey(String nlUsageTypeKey) {
		this.nlUsageTypeKey = nlUsageTypeKey;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String locale) {
		this.language = locale;
	}

	@Override
	public String toString() {
		return "statementTypeHeaderTemplate[language=" + language
				+ ", nlUsageTypeKey=" + nlUsageTypeKey 
				+ ", statementTypeId" + owner.getId() +"]";
	}
}
