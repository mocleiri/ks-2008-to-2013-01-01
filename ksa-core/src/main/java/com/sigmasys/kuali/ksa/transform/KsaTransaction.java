//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.07 at 05:50:44 PM MDT 
//


package com.sigmasys.kuali.ksa.transform;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="account-identifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="incoming-identifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="effective-date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="origination-date" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="native-amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *           &lt;element name="currency" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;/sequence>
 *         &lt;element name="document" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="person-to-contact" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="telephone-number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="email-address" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="open-document" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="override">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="override-rollup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                     &lt;element name="override-statement-text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                     &lt;sequence minOccurs="0">
 *                       &lt;element name="general-ledger-override">
 *                         &lt;complexType>
 *                           &lt;complexContent>
 *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                               &lt;sequence maxOccurs="unbounded">
 *                                 &lt;element name="general-ledger-account" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                 &lt;element name="percentage-allocation" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                               &lt;/sequence>
 *                             &lt;/restriction>
 *                           &lt;/complexContent>
 *                         &lt;/complexType>
 *                       &lt;/element>
 *                     &lt;/sequence>
 *                     &lt;element name="override-refund-rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                     &lt;element name="override-clear-date" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *         &lt;element name="transaction-type" type="{http://www.w3.org/2001/XMLSchema}string" form="qualified"/>
 *         &lt;element name="is-refundable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="refund-rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "accountIdentifier",
    "incomingIdentifier",
    "effectiveDate",
    "originationDate",
    "amount",
    "nativeAmount",
    "currency",
    "document",
    "override",
    "transactionType",
    "isRefundable",
    "refundRule"
})
@XmlRootElement(name = "ksa-transaction")
public class KsaTransaction {

    @XmlElement(name = "account-identifier", required = true)
    protected String accountIdentifier;
    @XmlElement(name = "incoming-identifier")
    protected String incomingIdentifier;
    @XmlElement(name = "effective-date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar effectiveDate;
    @XmlElement(name = "origination-date")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar originationDate;
    @XmlElement(required = true)
    protected BigDecimal amount;
    @XmlElement(name = "native-amount")
    protected BigDecimal nativeAmount;
    protected String currency;
    protected KsaTransaction.Document document;
    protected KsaTransaction.Override override;
    @XmlElement(name = "transaction-type", required = true)
    protected String transactionType;
    @XmlElement(name = "is-refundable")
    protected Boolean isRefundable;
    @XmlElement(name = "refund-rule")
    protected String refundRule;

    /**
     * Gets the value of the accountIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    /**
     * Sets the value of the accountIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountIdentifier(String value) {
        this.accountIdentifier = value;
    }

    /**
     * Gets the value of the incomingIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncomingIdentifier() {
        return incomingIdentifier;
    }

    /**
     * Sets the value of the incomingIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncomingIdentifier(String value) {
        this.incomingIdentifier = value;
    }

    /**
     * Gets the value of the effectiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Sets the value of the effectiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEffectiveDate(XMLGregorianCalendar value) {
        this.effectiveDate = value;
    }

    /**
     * Gets the value of the originationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOriginationDate() {
        return originationDate;
    }

    /**
     * Sets the value of the originationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOriginationDate(XMLGregorianCalendar value) {
        this.originationDate = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Gets the value of the nativeAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNativeAmount() {
        return nativeAmount;
    }

    /**
     * Sets the value of the nativeAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNativeAmount(BigDecimal value) {
        this.nativeAmount = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the document property.
     * 
     * @return
     *     possible object is
     *     {@link KsaTransaction.Document }
     *     
     */
    public KsaTransaction.Document getDocument() {
        return document;
    }

    /**
     * Sets the value of the document property.
     * 
     * @param value
     *     allowed object is
     *     {@link KsaTransaction.Document }
     *     
     */
    public void setDocument(KsaTransaction.Document value) {
        this.document = value;
    }

    /**
     * Gets the value of the override property.
     * 
     * @return
     *     possible object is
     *     {@link KsaTransaction.Override }
     *     
     */
    public KsaTransaction.Override getOverride() {
        return override;
    }

    /**
     * Sets the value of the override property.
     * 
     * @param value
     *     allowed object is
     *     {@link KsaTransaction.Override }
     *     
     */
    public void setOverride(KsaTransaction.Override value) {
        this.override = value;
    }

    /**
     * Gets the value of the transactionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the value of the transactionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionType(String value) {
        this.transactionType = value;
    }

    /**
     * Gets the value of the isRefundable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRefundable() {
        return isRefundable;
    }

    /**
     * Sets the value of the isRefundable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRefundable(Boolean value) {
        this.isRefundable = value;
    }

    /**
     * Gets the value of the refundRule property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefundRule() {
        return refundRule;
    }

    /**
     * Sets the value of the refundRule property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefundRule(String value) {
        this.refundRule = value;
    }


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
     *         &lt;element name="person-to-contact" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="telephone-number" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="email-address" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="open-document" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "personToContact",
        "telephoneNumber",
        "emailAddress",
        "openDocument"
    })
    public static class Document {

        @XmlElement(name = "person-to-contact", required = true)
        protected String personToContact;
        @XmlElement(name = "telephone-number", required = true)
        protected String telephoneNumber;
        @XmlElement(name = "email-address", required = true)
        protected String emailAddress;
        @XmlElement(name = "open-document")
        protected String openDocument;

        /**
         * Gets the value of the personToContact property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPersonToContact() {
            return personToContact;
        }

        /**
         * Sets the value of the personToContact property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPersonToContact(String value) {
            this.personToContact = value;
        }

        /**
         * Gets the value of the telephoneNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTelephoneNumber() {
            return telephoneNumber;
        }

        /**
         * Sets the value of the telephoneNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTelephoneNumber(String value) {
            this.telephoneNumber = value;
        }

        /**
         * Gets the value of the emailAddress property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEmailAddress() {
            return emailAddress;
        }

        /**
         * Sets the value of the emailAddress property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEmailAddress(String value) {
            this.emailAddress = value;
        }

        /**
         * Gets the value of the openDocument property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOpenDocument() {
            return openDocument;
        }

        /**
         * Sets the value of the openDocument property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOpenDocument(String value) {
            this.openDocument = value;
        }

    }


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
     *         &lt;element name="override-rollup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="override-statement-text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;sequence minOccurs="0">
     *           &lt;element name="general-ledger-override">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence maxOccurs="unbounded">
     *                     &lt;element name="general-ledger-account" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                     &lt;element name="percentage-allocation" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *                   &lt;/sequence>
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *         &lt;/sequence>
     *         &lt;element name="override-refund-rule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="override-clear-date" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
        "overrideRollup",
        "overrideStatementText",
        "generalLedgerOverride",
        "overrideRefundRule",
        "overrideClearDate"
    })
    public static class Override {

        @XmlElement(name = "override-rollup")
        protected String overrideRollup;
        @XmlElement(name = "override-statement-text")
        protected String overrideStatementText;
        @XmlElement(name = "general-ledger-override")
        protected KsaTransaction.Override.GeneralLedgerOverride generalLedgerOverride;
        @XmlElement(name = "override-refund-rule")
        protected String overrideRefundRule;
        @XmlElement(name = "override-clear-date")
        protected Integer overrideClearDate;

        /**
         * Gets the value of the overrideRollup property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOverrideRollup() {
            return overrideRollup;
        }

        /**
         * Sets the value of the overrideRollup property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOverrideRollup(String value) {
            this.overrideRollup = value;
        }

        /**
         * Gets the value of the overrideStatementText property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOverrideStatementText() {
            return overrideStatementText;
        }

        /**
         * Sets the value of the overrideStatementText property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOverrideStatementText(String value) {
            this.overrideStatementText = value;
        }

        /**
         * Gets the value of the generalLedgerOverride property.
         * 
         * @return
         *     possible object is
         *     {@link KsaTransaction.Override.GeneralLedgerOverride }
         *     
         */
        public KsaTransaction.Override.GeneralLedgerOverride getGeneralLedgerOverride() {
            return generalLedgerOverride;
        }

        /**
         * Sets the value of the generalLedgerOverride property.
         * 
         * @param value
         *     allowed object is
         *     {@link KsaTransaction.Override.GeneralLedgerOverride }
         *     
         */
        public void setGeneralLedgerOverride(KsaTransaction.Override.GeneralLedgerOverride value) {
            this.generalLedgerOverride = value;
        }

        /**
         * Gets the value of the overrideRefundRule property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOverrideRefundRule() {
            return overrideRefundRule;
        }

        /**
         * Sets the value of the overrideRefundRule property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOverrideRefundRule(String value) {
            this.overrideRefundRule = value;
        }

        /**
         * Gets the value of the overrideClearDate property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getOverrideClearDate() {
            return overrideClearDate;
        }

        /**
         * Sets the value of the overrideClearDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setOverrideClearDate(Integer value) {
            this.overrideClearDate = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence maxOccurs="unbounded">
         *         &lt;element name="general-ledger-account" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="percentage-allocation" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
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
            "generalLedgerAccountAndPercentageAllocation"
        })
        public static class GeneralLedgerOverride {

            @XmlElements({
                @XmlElement(name = "general-ledger-account", required = true, type = String.class),
                @XmlElement(name = "percentage-allocation", required = true, type = BigDecimal.class)
            })
            protected List<Serializable> generalLedgerAccountAndPercentageAllocation;

            /**
             * Gets the value of the generalLedgerAccountAndPercentageAllocation property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the generalLedgerAccountAndPercentageAllocation property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getGeneralLedgerAccountAndPercentageAllocation().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link String }
             * {@link BigDecimal }
             * 
             * 
             */
            public List<Serializable> getGeneralLedgerAccountAndPercentageAllocation() {
                if (generalLedgerAccountAndPercentageAllocation == null) {
                    generalLedgerAccountAndPercentageAllocation = new ArrayList<Serializable>();
                }
                return this.generalLedgerAccountAndPercentageAllocation;
            }

        }

    }

}
