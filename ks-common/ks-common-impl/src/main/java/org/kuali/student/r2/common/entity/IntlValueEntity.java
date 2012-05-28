package org.kuali.student.r2.common.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.kuali.student.r2.common.dto.IntlValueInfo;
import org.kuali.student.r2.common.infc.IntlValue;

@MappedSuperclass
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"LOCALE", "OWNER"})})
public abstract class IntlValueEntity<T extends LngNameOwner<?>> extends BaseEntity implements IntlValue {

    @Column(name = "LOCALE")
    private String locale;

    @Column(name = "VALUE")
    private String value;
    
    public IntlValueEntity(){
    	
    }
    public IntlValueEntity(IntlValue value) {
        this.locale =  value.getLocale();
        this.value = value.getValue();
    }

    public IntlValueEntity(String locale, String value) {
        this.locale = locale;
        this.setValue(value);
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
    
    public abstract void setOwner(T owner);

    public abstract T getOwner();

    public IntlValueInfo toDto() {
    	IntlValueInfo intlValueInfo = new IntlValueInfo();
    	intlValueInfo.setLocale(this.getLocale());
    	intlValueInfo.setValue(this.getValue());
        return intlValueInfo;
    }
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}
