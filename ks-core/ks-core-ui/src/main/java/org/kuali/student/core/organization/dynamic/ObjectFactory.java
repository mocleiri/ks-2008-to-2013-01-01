/**
 * Copyright 2010 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.01.08 at 02:11:47 PM EST 
//


package org.kuali.student.core.organization.dynamic;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sectionConfig package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SectionConfig_QNAME = new QName("", "sectionConfig");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sectionConfig
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MultipleField }
     * 
     */
    public MultipleField createMultipleField() {
        return new MultipleField();
    }

    /**
     * Create an instance of {@link Fields }
     * 
     */
    public Fields createFields() {
        return new Fields();
    }

    /**
     * Create an instance of {@link Field }
     * 
     */
    public Field createField() {
        return new Field();
    }

    /**
     * Create an instance of {@link Section }
     * 
     */
    public Section createSection() {
        return new Section();
    }

    /**
     * Create an instance of {@link SectionView }
     * 
     */
    public SectionView createSectionView() {
        return new SectionView();
    }

    /**
     * Create an instance of {@link SectionConfig }
     * 
     */
    public SectionConfig createSectionConfig() {
        return new SectionConfig();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SectionConfig }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "sectionConfig")
    public JAXBElement<SectionConfig> createSectionConfig(SectionConfig value) {
        return new JAXBElement<SectionConfig>(_SectionConfig_QNAME, SectionConfig.class, null, value);
    }

}
