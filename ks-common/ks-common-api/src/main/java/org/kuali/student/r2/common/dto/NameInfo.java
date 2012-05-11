package org.kuali.student.r2.common.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.student.r2.common.infc.Name;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NameInfo", propOrder = {"locale", "name"})
public final class NameInfo implements Name, Serializable {

    private static final long serialVersionUID = 1L;
    
    @XmlElement
    private String locale;
    
    @XmlElement
    private String name;
    
    public NameInfo() {
        this.locale = null;
        this.name = null;
    }
    
    public NameInfo(String locale, String name) {
        this.locale = locale;
        this.name = name;
    }

    public NameInfo(Name name) {
        this.locale = name.getLocale();
        this.name = name.getName();
    }

    @Override
    public String getLocale() {
        return locale;
    }

    
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }
}