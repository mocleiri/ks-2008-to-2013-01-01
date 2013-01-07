//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.09.18 at 09:08:39 PM GMT-05:00 
//


package com.sigmasys.kuali.ksa.transform;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}external-statement" maxOccurs="unbounded"/>
 *         &lt;element ref="{}external-statement-remove" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "externalStatement",
    "externalStatementRemove"
})
@XmlRootElement(name = "external-statement-batch")
public class ExternalStatementBatch {

    @XmlElement(name = "external-statement", required = true)
    protected List<ExternalStatement> externalStatement;
    @XmlElement(name = "external-statement-remove", required = true)
    protected List<ExternalStatementRemove> externalStatementRemove;

    /**
     * Gets the value of the externalStatement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the externalStatement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExternalStatement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExternalStatement }
     * 
     * 
     */
    public List<ExternalStatement> getExternalStatement() {
        if (externalStatement == null) {
            externalStatement = new ArrayList<ExternalStatement>();
        }
        return this.externalStatement;
    }

    /**
     * Gets the value of the externalStatementRemove property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the externalStatementRemove property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExternalStatementRemove().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExternalStatementRemove }
     * 
     * 
     */
    public List<ExternalStatementRemove> getExternalStatementRemove() {
        if (externalStatementRemove == null) {
            externalStatementRemove = new ArrayList<ExternalStatementRemove>();
        }
        return this.externalStatementRemove;
    }

}