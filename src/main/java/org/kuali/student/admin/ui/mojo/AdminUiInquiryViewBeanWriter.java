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
package org.kuali.student.admin.ui.mojo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import org.kuali.student.contract.model.MessageStructure;
import org.kuali.student.contract.model.Service;

import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.ServiceMethod;
import org.kuali.student.contract.model.XmlType;
import org.kuali.student.contract.model.util.ModelFinder;
import org.kuali.student.contract.writer.XmlWriter;
import org.kuali.student.contract.writer.service.GetterSetterNameCalculator;

/**
 *
 * @author nwright
 */
public class AdminUiInquiryViewBeanWriter {

    private ServiceContractModel model;
    private ModelFinder finder;
    private String directory;
    private String rootPackage;
    private String servKey;
    private Service service;
    private XmlType xmlType;
    private List<ServiceMethod> methods;
    private XmlWriter out;

    public AdminUiInquiryViewBeanWriter(ServiceContractModel model,
            String directory,
            String rootPackage,
            String servKey,
            XmlType xmlType,
            List<ServiceMethod> methods) {
        this.model = model;
        this.finder = new ModelFinder(model);
        this.directory = directory;
        this.rootPackage = rootPackage;
        this.servKey = servKey;
        service = finder.findService(servKey);
        this.xmlType = xmlType;
        this.methods = methods;
    }

    /**
     * Write out the entire file
     *
     * @param out
     */
    public void write() {
        initXmlWriter();
        writeBoilerPlate();
        writeBean();
    }

    private void writeBean() {
        String infoClass = GetterSetterNameCalculator.calcInitUpper(xmlType.getName());
        String serviceClass = GetterSetterNameCalculator.calcInitUpper(service.getName());
        String serviceVar = GetterSetterNameCalculator.calcInitLower(service.getName());
        String serviceContract = service.getImplProject() + "." + service.getName();
        String inquirable = AdminUiInquirableWriter.calcPackage(servKey, rootPackage, xmlType) + "."
                + AdminUiInquirableWriter.calcClassName(servKey, xmlType);
        out.println("");
        out.incrementIndent();
        out.indentPrintln("<import resource=\"classpath:ks-" + infoClass + "-dictionary.xml\"/>");
        out.indentPrintln("<import resource=\"classpath:UifKSDefinitions.xml\"/>");
        out.indentPrintln("<!-- InquiryView -->");
        out.indentPrintln("<bean id=\"KS-" + infoClass + "-AdminInquiryView\" parent=\"KSInquryView\"");
        out.incrementIndent();
        out.indentPrintln("p:title=\"" + xmlType.getName() + " Inquiry\"");
        out.indentPrintln("p:dataObjectClassName=\"" + xmlType.getJavaPackage() + "." + infoClass + "\"");
        out.indentPrintln("p:viewHelperServiceClass=\"" + inquirable + "\">");
        out.indentPrintln("");
        out.indentPrintln("<property name=\"Items\">");
        out.indentPrintln("    <list>");
        out.indentPrintln("        <bean parent=\"Uif-Disclosure-GridSection\">");
        out.indentPrintln("            <property name=\"layoutManager.numberOfColumns\" value=\"2\"/>");
        out.indentPrintln("            <property name=\"headerText\" value=\"" + xmlType.getName() + " Inquiry\"/>");
        out.indentPrintln("            <property name=\"items\">");
        out.indentPrintln("                <list>");
        this.writeFields(xmlType, new Stack<XmlType>(), "");
        out.indentPrintln("                </list>");
        out.indentPrintln("            </property>");
        out.indentPrintln("        </bean>");
        out.indentPrintln("    </list>");
        out.indentPrintln("</property>");
        out.decrementIndent();
        out.indentPrintln("</bean>");
        out.indentPrintln("");
        out.decrementIndent();
        out.indentPrintln("</beans>");

    }

    private void writeFields(XmlType type, Stack<XmlType> parents, String prefix) {
        // avoid recursion
        if (parents.contains(type)) {
            return;
        }
        parents.push(type);
        for (MessageStructure ms : finder.findMessageStructures(type.getName())) {
            String fieldName = GetterSetterNameCalculator.calcInitLower(ms.getShortName());
            if (!prefix.isEmpty()) {
                fieldName = prefix + "." + fieldName;
            }
            if (ms.getType().equalsIgnoreCase("AttributeInfoList")) {
                out.indentPrintln("                    <!-- TODO: deal with dynamic attributes -->");
                continue;
            }
            if (ms.getType().endsWith("List")) {
                out.indentPrintln("                    <!-- TODO: deal with " + fieldName + " which is a list -->");
                continue;
            }
            XmlType fieldType = finder.findXmlType(ms.getType());
            if (!fieldType.getPrimitive().equalsIgnoreCase(XmlType.COMPLEX)) {
                out.indentPrintln("                    <bean parent=\"Uif-DataField\" p:propertyName=\"" + fieldName + "\" />");
                continue;
            }
            // complex sub-types such as rich text 
            this.writeFields(fieldType, parents, fieldName);
        }
        parents.pop();
    }

    private void initXmlWriter() {
        String infoClass = GetterSetterNameCalculator.calcInitUpper(xmlType.getName());
        String serviceClass = GetterSetterNameCalculator.calcInitUpper(service.getName());
        String serviceVar = GetterSetterNameCalculator.calcInitLower(service.getName());
        String serviceContract = service.getImplProject() + "." + service.getName();

        String path = AdminUiInquirableWriter.calcPackage(servKey, rootPackage, xmlType).replace('.', '/');
        String fileName = infoClass + "AdminInquiryView.xml";

        File dir = new File(this.directory);

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IllegalStateException("Could not create directory "
                        + this.directory);
            }
        }

        String dirStr = this.directory + "/" + "resources" + "/" + path;
        File dirFile = new File(dirStr);
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                throw new IllegalStateException(
                        "Could not create directory " + dirStr);
            }
        }
        try {
            PrintStream out = new PrintStream(new FileOutputStream(
                    dirStr + File.separator
                    + fileName, false));
            this.out = new XmlWriter(out, 0);
        } catch (FileNotFoundException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private void writeBoilerPlate() {
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        out.println("       xmlns:p=\"http://www.springframework.org/schema/p\"");
        out.println("       xsi:schemaLocation=\"http://www.springframework.org/schema/beans");
        out.println("                    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\">");
        out.println("    <!--");
        out.println("     Copyright 2007-2012 The Kuali Foundation");
        out.println("");
        out.println("     Licensed under the Educational Community License, Version 2.0 (the \"License\");");
        out.println("     you may not use this file except in compliance with the License.");
        out.println("     You may obtain a copy of the License at");
        out.println("");
        out.println("     http://www.opensource.org/licenses/ecl2.php");
        out.println("");
        out.println("     Unless required by applicable law or agreed to in writing, software");
        out.println("     distributed under the License is distributed on an \"AS IS\" BASIS,");
        out.println("     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
        out.println("     See the License for the specific language governing permissions and");
        out.println("     limitations under the License.");
        out.println("    -->");

    }
}
