//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.09.18 at 09:08:39 PM GMT-05:00 
//


package com.sigmasys.kuali.ksa.transform;

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
 *         &lt;element name="response-identifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="response-to-batch-identifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="batch-status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="accepted">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element ref="{}ksa-transaction"/>
 *                   &lt;element name="transaction-details">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="transaction-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="accepted-date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="failed">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element ref="{}ksa-transaction"/>
 *                   &lt;element name="reason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="batch-summary">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="transactions-in-batch" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="value-of-batch" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                   &lt;element name="transactions-accepted" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="value-accepted" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                   &lt;element name="transactions-failed" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="value-failed" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "responseIdentifier",
    "responseToBatchIdentifier",
    "batchStatus",
    "accepted",
    "failed",
    "batchSummary"
})
@XmlRootElement(name = "ksa-batch-transaction-response")
public class KsaBatchTransactionResponse {

    @XmlElement(name = "response-identifier", required = true)
    protected String responseIdentifier;
    @XmlElement(name = "response-to-batch-identifier", required = true)
    protected String responseToBatchIdentifier;
    @XmlElement(name = "batch-status", required = true)
    protected String batchStatus;
    @XmlElement(required = true)
    protected KsaBatchTransactionResponse.Accepted accepted;
    @XmlElement(required = true)
    protected KsaBatchTransactionResponse.Failed failed;
    @XmlElement(name = "batch-summary", required = true)
    protected KsaBatchTransactionResponse.BatchSummary batchSummary;

    /**
     * Gets the value of the responseIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    /**
     * Sets the value of the responseIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseIdentifier(String value) {
        this.responseIdentifier = value;
    }

    /**
     * Gets the value of the responseToBatchIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseToBatchIdentifier() {
        return responseToBatchIdentifier;
    }

    /**
     * Sets the value of the responseToBatchIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseToBatchIdentifier(String value) {
        this.responseToBatchIdentifier = value;
    }

    /**
     * Gets the value of the batchStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatchStatus() {
        return batchStatus;
    }

    /**
     * Sets the value of the batchStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatchStatus(String value) {
        this.batchStatus = value;
    }

    /**
     * Gets the value of the accepted property.
     * 
     * @return
     *     possible object is
     *     {@link KsaBatchTransactionResponse.Accepted }
     *     
     */
    public KsaBatchTransactionResponse.Accepted getAccepted() {
        return accepted;
    }

    /**
     * Sets the value of the accepted property.
     * 
     * @param value
     *     allowed object is
     *     {@link KsaBatchTransactionResponse.Accepted }
     *     
     */
    public void setAccepted(KsaBatchTransactionResponse.Accepted value) {
        this.accepted = value;
    }

    /**
     * Gets the value of the failed property.
     * 
     * @return
     *     possible object is
     *     {@link KsaBatchTransactionResponse.Failed }
     *     
     */
    public KsaBatchTransactionResponse.Failed getFailed() {
        return failed;
    }

    /**
     * Sets the value of the failed property.
     * 
     * @param value
     *     allowed object is
     *     {@link KsaBatchTransactionResponse.Failed }
     *     
     */
    public void setFailed(KsaBatchTransactionResponse.Failed value) {
        this.failed = value;
    }

    /**
     * Gets the value of the batchSummary property.
     * 
     * @return
     *     possible object is
     *     {@link KsaBatchTransactionResponse.BatchSummary }
     *     
     */
    public KsaBatchTransactionResponse.BatchSummary getBatchSummary() {
        return batchSummary;
    }

    /**
     * Sets the value of the batchSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link KsaBatchTransactionResponse.BatchSummary }
     *     
     */
    public void setBatchSummary(KsaBatchTransactionResponse.BatchSummary value) {
        this.batchSummary = value;
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
     *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *         &lt;element ref="{}ksa-transaction"/>
     *         &lt;element name="transaction-details">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="transaction-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="accepted-date" type="{http://www.w3.org/2001/XMLSchema}date"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "ksaTransactionAndTransactionDetails"
    })
    public static class Accepted {

        @XmlElements({
            @XmlElement(name = "ksa-transaction", type = KsaTransaction.class),
            @XmlElement(name = "transaction-details", type = KsaBatchTransactionResponse.Accepted.TransactionDetails.class)
        })
        protected List<Object> ksaTransactionAndTransactionDetails;

        /**
         * Gets the value of the ksaTransactionAndTransactionDetails property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the ksaTransactionAndTransactionDetails property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getKsaTransactionAndTransactionDetails().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link KsaTransaction }
         * {@link KsaBatchTransactionResponse.Accepted.TransactionDetails }
         * 
         * 
         */
        public List<Object> getKsaTransactionAndTransactionDetails() {
            if (ksaTransactionAndTransactionDetails == null) {
                ksaTransactionAndTransactionDetails = new ArrayList<Object>();
            }
            return this.ksaTransactionAndTransactionDetails;
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
         *         &lt;element name="transaction-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="accepted-date" type="{http://www.w3.org/2001/XMLSchema}date"/>
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
            "transactionId",
            "acceptedDate"
        })
        public static class TransactionDetails {

            @XmlElement(name = "transaction-id", required = true)
            protected String transactionId;
            @XmlElement(name = "accepted-date", required = true)
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar acceptedDate;

            /**
             * Gets the value of the transactionId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTransactionId() {
                return transactionId;
            }

            /**
             * Sets the value of the transactionId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTransactionId(String value) {
                this.transactionId = value;
            }

            /**
             * Gets the value of the acceptedDate property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getAcceptedDate() {
                return acceptedDate;
            }

            /**
             * Sets the value of the acceptedDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setAcceptedDate(XMLGregorianCalendar value) {
                this.acceptedDate = value;
            }

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
     *         &lt;element name="transactions-in-batch" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="value-of-batch" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *         &lt;element name="transactions-accepted" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="value-accepted" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
     *         &lt;element name="transactions-failed" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="value-failed" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
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
        "transactionsInBatch",
        "valueOfBatch",
        "transactionsAccepted",
        "valueAccepted",
        "transactionsFailed",
        "valueFailed"
    })
    public static class BatchSummary {

        @XmlElement(name = "transactions-in-batch")
        protected int transactionsInBatch;
        @XmlElement(name = "value-of-batch", required = true)
        protected BigDecimal valueOfBatch;
        @XmlElement(name = "transactions-accepted")
        protected int transactionsAccepted;
        @XmlElement(name = "value-accepted", required = true)
        protected BigDecimal valueAccepted;
        @XmlElement(name = "transactions-failed")
        protected int transactionsFailed;
        @XmlElement(name = "value-failed", required = true)
        protected BigDecimal valueFailed;

        /**
         * Gets the value of the transactionsInBatch property.
         * 
         */
        public int getTransactionsInBatch() {
            return transactionsInBatch;
        }

        /**
         * Sets the value of the transactionsInBatch property.
         * 
         */
        public void setTransactionsInBatch(int value) {
            this.transactionsInBatch = value;
        }

        /**
         * Gets the value of the valueOfBatch property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getValueOfBatch() {
            return valueOfBatch;
        }

        /**
         * Sets the value of the valueOfBatch property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setValueOfBatch(BigDecimal value) {
            this.valueOfBatch = value;
        }

        /**
         * Gets the value of the transactionsAccepted property.
         * 
         */
        public int getTransactionsAccepted() {
            return transactionsAccepted;
        }

        /**
         * Sets the value of the transactionsAccepted property.
         * 
         */
        public void setTransactionsAccepted(int value) {
            this.transactionsAccepted = value;
        }

        /**
         * Gets the value of the valueAccepted property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getValueAccepted() {
            return valueAccepted;
        }

        /**
         * Sets the value of the valueAccepted property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setValueAccepted(BigDecimal value) {
            this.valueAccepted = value;
        }

        /**
         * Gets the value of the transactionsFailed property.
         * 
         */
        public int getTransactionsFailed() {
            return transactionsFailed;
        }

        /**
         * Sets the value of the transactionsFailed property.
         * 
         */
        public void setTransactionsFailed(int value) {
            this.transactionsFailed = value;
        }

        /**
         * Gets the value of the valueFailed property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getValueFailed() {
            return valueFailed;
        }

        /**
         * Sets the value of the valueFailed property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setValueFailed(BigDecimal value) {
            this.valueFailed = value;
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
     *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *         &lt;element ref="{}ksa-transaction"/>
     *         &lt;element name="reason" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "ksaTransactionAndReason"
    })
    public static class Failed {

        @XmlElements({
            @XmlElement(name = "ksa-transaction", type = KsaTransaction.class),
            @XmlElement(name = "reason", type = String.class)
        })
        protected List<Object> ksaTransactionAndReason;

        /**
         * Gets the value of the ksaTransactionAndReason property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the ksaTransactionAndReason property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getKsaTransactionAndReason().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link KsaTransaction }
         * {@link String }
         * 
         * 
         */
        public List<Object> getKsaTransactionAndReason() {
            if (ksaTransactionAndReason == null) {
                ksaTransactionAndReason = new ArrayList<Object>();
            }
            return this.ksaTransactionAndReason;
        }

    }

}
