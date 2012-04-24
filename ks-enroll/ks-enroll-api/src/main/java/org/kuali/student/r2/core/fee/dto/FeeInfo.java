/*
 * Copyright 2012 The Kuali Foundation 
 *
 * Licensed under the Educational Community License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.kuali.student.r2.core.fee.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.student.r2.core.fee.infc.Fee;
import org.kuali.student.r2.common.dto.IdNamelessEntityInfo;
import org.kuali.student.r2.common.dto.CurrencyAmountInfo;
import org.w3c.dom.Element;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FeeInfo", propOrder = { 
                "id", "typeKey", "stateKey", "descr", 
                "amount", "org", "refObjectURI",
                "refObjectId", 
		"meta", "attributes", "_futureElements" })

public class FeeInfo 
    extends IdNamelessEntityInfo 
    implements Fee, Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private CurrencyAmountInfo amount;

    @XmlElement
    private String orgId;

    @XmlElement
    private String refObjectURI;

    @XmlElement
    private String refObjectId;
	
    @XmlAnyElement
    private List<Element> _futureElements;


    /**
     * Constructs a new FeeInfo.
     */
    public FeeInfo() {
    }

    /**
     * Constructs a new FeeInfo from another Fee.
     * 
     * @param fee the Fee to copy
     */
    public FeeInfo(Fee fee) {
        super(fee);

        if (fee != null) {
            this.amount = new CurrencyAmountInfo(fee.getAmount());
            this.orgId = fee.getOrgId();
            this.refObjectURI = fee.getRefObjectURI();
            this.refObjectId = fee.getRefObjectId();
        }
    }

    @Override
    public CurrencyAmountInfo getAmount() {
        return amount;
    }
    
    public void setAmount(CurrencyAmountInfo amount) {
        this.amount = amount;;
    }

    @Override
    public String getOrgId() {
        return orgId;
    }
    
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Override
    public String getRefObjectURI() {
        return refObjectURI;
    }
    
    public void setRefObjectURI(String refObjectURI) {
        this.refObjectURI = refObjectURI;
    }

    @Override
    public String getRefObjectId() {
        return refObjectId;
    }
    
    public void setRefObjectId(String refObjectId) {
        this.refObjectId = refObjectId;
    }
}    
