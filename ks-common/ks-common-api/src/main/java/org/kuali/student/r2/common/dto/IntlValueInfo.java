package org.kuali.student.r2.common.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.kuali.student.r2.common.infc.IntlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IntlValueInfo", propOrder = {"locale", "value"})
public final class IntlValueInfo implements IntlValue, Serializable {

    private static final long serialVersionUID = 1L;
    
    @XmlElement
    private String locale;
    
    @XmlElement
    private String value;
    
    public IntlValueInfo() {
        this.locale = null;
        this.value = null;
    }
    
    public IntlValueInfo(String locale, String value) {
        this.locale = locale;
        this.value = value;
    }

    public IntlValueInfo(IntlValue value) {
        this.locale = value.getLocale();
        this.value = value.getValue();
    }

    @Override
    public String getLocale() {
        return locale;
    }

    
    public void setLocale(String locale) {
        this.locale = locale;
    }

	@Override
	public String getValue() {
		return this.value;
	}
	
	public void setValue(String value){
		this.value = value;
	}
}