
package org.kuali.student.wsdl.atp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getMilestoneResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getMilestoneResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://student.kuali.org/wsdl/atp}milestoneInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getMilestoneResponse", propOrder = {
    "_return"
})
public class GetMilestoneResponse {

    @XmlElement(name = "return")
    protected MilestoneInfo _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link MilestoneInfo }
     *     
     */
    public MilestoneInfo getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link MilestoneInfo }
     *     
     */
    public void setReturn(MilestoneInfo value) {
        this._return = value;
    }

}
