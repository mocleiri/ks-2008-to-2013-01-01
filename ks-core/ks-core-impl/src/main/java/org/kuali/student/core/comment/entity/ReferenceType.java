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

package org.kuali.student.core.comment.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.kuali.student.core.entity.Type;

@Entity
@Table(name = "KSCO_REFERENCE_TYPE")
public class ReferenceType extends Type<ReferenceTypeAttribute>{
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<ReferenceTypeAttribute> attributes;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="referenceType")
    private List<Reference> references;

    @Override
    public List<ReferenceTypeAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public void setAttributes(List<ReferenceTypeAttribute> attributes) {
        this.attributes=attributes;
    }

	/**
	 * @return the references
	 */
	public List<Reference> getReferences() {
		return references;
	}

	/**
	 * @param references the references to set
	 */
	public void setReferences(List<Reference> references) {
		this.references = references;
	}
}
