/*
 * Copyright 2009 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may	obtain a copy of the License at
 *
 * 	http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.contract.model;

/**
 *
 * @author nwright
 */
public class MessageStructure {

    private String id;
    private String xmlObject;
    private String shortName;
    private String name;
    private String type;
    private String url;
    private String description;
    private String required;
    private String readOnly;
    private String cardinality;
    private String status;
    private String xmlAttribute;
    private String implNotes;
    private boolean overriden;
    private boolean deprecated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXmlObject() {
        return xmlObject;
    }

    public void setXmlObject(String xmlObject) {
        this.xmlObject = xmlObject;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    public String getCardinality() {
        return cardinality;
    }

    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }

    public String getXmlAttribute() {
        return xmlAttribute;
    }

    public void setXmlAttribute(String xmlAttribute) {
        this.xmlAttribute = xmlAttribute;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImplNotes() {
        return implNotes;
    }

    public void setImplNotes(String implNotes) {
        this.implNotes = implNotes;
    }

    public boolean isOverriden() {
        return overriden;
    }

    public void setOverriden(boolean overriden) {
        this.overriden = overriden;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }
   
}
