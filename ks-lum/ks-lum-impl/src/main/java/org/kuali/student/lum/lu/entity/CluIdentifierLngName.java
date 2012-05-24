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

package org.kuali.student.lum.lu.entity;
 
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.r2.common.entity.NameEntity;
import org.kuali.student.r2.common.infc.Name;
import org.kuali.student.lum.lu.entity.CluIdentifier;

@Entity
@Table(name = "KSLU_CLU_IDENT_LNG_NAME")
public class CluIdentifierLngName extends NameEntity<CluIdentifier>{

	@ManyToOne
	@JoinColumn(name = "OWNER")
	private CluIdentifier owner;
	
    public CluIdentifierLngName(){
    	
    }
    public CluIdentifierLngName(Name name) {
        super(name);
    }
    
    public CluIdentifierLngName(String locale, String name) {
        super(locale, name);
    }
    
    public CluIdentifierLngName(Name name, CluIdentifier owner) {
        super(name);
        setOwner(owner);
    }

	@Override
	public CluIdentifier getOwner() {
		return owner;
	}

	@Override
	public void setOwner(CluIdentifier owner) {
		this.owner = owner;
	}
}
