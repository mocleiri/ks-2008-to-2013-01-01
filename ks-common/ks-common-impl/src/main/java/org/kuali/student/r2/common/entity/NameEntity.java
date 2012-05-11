package org.kuali.student.r2.common.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.kuali.student.r2.common.dto.NameInfo;
import org.kuali.student.r2.common.infc.Name;

@MappedSuperclass
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"LOCALE", "OWNER"})})
public abstract class NameEntity<T extends NameOwner<?>> extends BaseEntity implements Name {

    @Column(name = "LOCALE")
    private String locale;

    @Column(name = "NAME")
    private String name;
    
    public NameEntity(){
    	
    }
    public NameEntity(Name name) {
        this.locale = name.getLocale();
        this.name = name.getName();
    }

    public NameEntity(String locale, String name) {
        this.locale = locale;
        this.name = name;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public abstract void setOwner(T owner);

    public abstract T getOwner();

    public NameInfo toDto() {
        NameInfo nameInfo = new NameInfo();
        nameInfo.setLocale(this.getLocale());
        nameInfo.setName(this.getName());
        return nameInfo;
    }
}
