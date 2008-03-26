package org.kuali.student.poc.xsd.personidentity.person.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonType extends PersonTypeInfoDTO {
    @XmlAttribute
    private Long id;
    @XmlAttribute
    private String name;
	
	@XmlElement(name = "set")
	@XmlElementWrapper(name = "setList")
	private List<AttributeSetDefinition> attributeSets;

	/**
	 * @return the attributeSets
	 */
	public List<AttributeSetDefinition> getAttributeSets() {
		if (attributeSets == null) {
			attributeSets = new ArrayList<AttributeSetDefinition>();
		}
		return attributeSets;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
