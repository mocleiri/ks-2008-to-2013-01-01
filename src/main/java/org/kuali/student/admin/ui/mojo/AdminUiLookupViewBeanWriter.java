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
import java.util.List;
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
public class AdminUiLookupViewBeanWriter {

    private ServiceContractModel model;
    private ModelFinder finder;
    private String directory;
    private String rootPackage;
    private String servKey;
    private Service service;
    private XmlType xmlType;
    private List<ServiceMethod> methods;
    private XmlWriter out;

    public AdminUiLookupViewBeanWriter(ServiceContractModel model,
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
        String lookupable = AdminUiLookupableWriter.calcPackage(servKey, rootPackage, xmlType) + "."
                + AdminUiLookupableWriter.calcClassName(servKey, xmlType);
        out.println("");
        out.incrementIndent ();
        out.indentPrintln("<import resource=\"classpath:ks-" + infoClass + "-dictionary.xml\"/>");
        out.indentPrintln("<import resource=\"classpath:UifKSDefinitions.xml\"/>");
        out.indentPrintln("<!-- LookupView -->");
        out.indentPrintln("<bean id=\"KS-" + infoClass + "-AdminLookupView\" parent=\"KSLookupView\"");
        out.incrementIndent ();
        out.indentPrintln("p:title=\"" + xmlType.getName() + " Lookup\"");
        out.indentPrintln("p:dataObjectClassName=\"" + xmlType.getJavaPackage() + "." + infoClass + "\"");
        out.indentPrintln("p:viewHelperServiceClass=\"" + lookupable + "\">");
        out.indentPrintln("");
        out.indentPrintln("<property name=\"criteriaFields\">");
        out.incrementIndent ();
        out.indentPrintln("<list>");
        out.indentPrintln("    <bean parent=\"Uif-LookupCriteriaInputField\" p:propertyName=\"searchValue\"/>");
        out.indentPrintln("</list>");
        out.decrementIndent ();
        out.indentPrintln("</property>");
        out.indentPrintln("<property name=\"resultFields\">");
        out.indentPrintln("    <list>");
        for (MessageStructure ms : finder.findMessageStructures(xmlType.getName())) {
            XmlType msType = finder.findXmlType(ms.getType());
            if (msType != null) {
                if (!msType.getPrimitive().equalsIgnoreCase(XmlType.COMPLEX)) {
                    String fieldName = GetterSetterNameCalculator.calcInitLower (ms.getShortName());
                    out.indentPrintln("        <bean parent=\"Uif-DataField\" p:propertyName=\"" + fieldName + "\" />");
                }
            }
        }
        out.indentPrintln("    </list>");
        out.indentPrintln("</property>");
        out.decrementIndent();
        out.indentPrintln("</bean>");
        out.indentPrintln("");
        out.decrementIndent();
        out.indentPrintln("</beans>");

    }

    private void initXmlWriter() {
        String infoClass = GetterSetterNameCalculator.calcInitUpper(xmlType.getName());
        String serviceClass = GetterSetterNameCalculator.calcInitUpper(service.getName());
        String serviceVar = GetterSetterNameCalculator.calcInitLower(service.getName());
        String serviceContract = service.getImplProject() + "." + service.getName();
        
        String path = AdminUiLookupableWriter.calcPackage(servKey, rootPackage, xmlType).replace('.', '/');
        String fileName = infoClass + "AdminLookupView.xml";

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
