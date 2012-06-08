package org.kuali.student.lum.lu.bo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.kuali.student.core.bo.KsBusinessObjectBase;

public class CluIdentifierIntlValues extends KsBusinessObjectBase {

	private static final long serialVersionUID = -3231108861178451995L;

	@Column(name = "LOCALE")
    private String locale;

    @Column(name = "VALUE")
    private String value;

    @ManyToOne
	@JoinColumn(name = "OWNER")
	private String owner;

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getLocale() {
		return locale;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}


}
