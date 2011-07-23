/*
 * Copyright 2011 The Kuali Foundation
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
package org.kuali.student.contract.model.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;

import org.kuali.student.contract.model.MessageStructure;
import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.XmlType;
import org.kuali.student.contract.writer.XmlWriter;

/**
 *
 * @author nwright
 */
public class XmlKradBaseDictionaryCreator {

    private ServiceContractModel model;
    private ModelFinder finder;
    private String directory;
    private String className;
    private XmlType xmlType;
    private XmlWriter gwriter;
    private XmlWriter bwriter;
    private List<MessageStructure> messageStructures;

    public XmlKradBaseDictionaryCreator(String directory,
            ServiceContractModel model, String className) {
        this.directory = directory;
        this.model = model;
        this.finder = new ModelFinder(this.model);
        this.className = className;
        this.xmlType = this.finder.findXmlType(className);
        if (xmlType == null) {
            throw new IllegalArgumentException(className);
        }
        this.messageStructures = this.finder.findMessageStructures(className);
        if (this.messageStructures.isEmpty()) {
            throw new IllegalStateException(className);
        }
    }

    public void write() {
        this.initXmlWriters();
        this.writeSpringHeaderOpen(gwriter);
        this.writeWarning(gwriter);
        this.writeGeneratedImports(gwriter);
        this.writeGeneratedObjectStructure(gwriter);
        this.writeSpringHeaderClose(gwriter);

        this.writeSpringHeaderOpen(bwriter);
        this.writeNote (bwriter);
        this.writeBaseImports(bwriter);
        this.writeBaseObjectStructure(bwriter);
        this.writeSpringHeaderClose(bwriter);
    }

    private void initXmlWriters() {
        String generatedFileName = "/ks-" + initUpper(className) + "-dictionary-generated.xml";
        String baseFileName = "/ks-" + initUpper(className) + "-dictionary.xml";

        File dir = new File(this.directory);
        //System.out.indentPrintln ("Writing java class: " + fileName + " to " + dir.getAbsolutePath ());

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IllegalStateException("Could not create directory "
                        + this.directory);
            }
        }

        try {
            PrintStream out = new PrintStream(new FileOutputStream(this.directory + "/" + generatedFileName, false));
            this.gwriter = new XmlWriter(out, 0);
        } catch (FileNotFoundException ex) {
            throw new IllegalStateException(ex);
        }
        try {
            PrintStream out = new PrintStream(new FileOutputStream(this.directory + "/" + baseFileName, false));
            this.bwriter = new XmlWriter(out, 0);
        } catch (FileNotFoundException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static String initLower(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return str;
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    private static String initUpper(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void writeSpringHeaderClose(XmlWriter out) {
        out.decrementIndent();
        out.indentPrintln("</beans>");
    }

    private void writeSpringHeaderOpen(XmlWriter out) {
        out.indentPrintln("<!--");
        out.indentPrintln(" Copyright 2011 The Kuali Foundation");
        out.println("");
        out.indentPrintln(" Licensed under the Educational Community License, Version 2.0 (the \"License\");");
        out.indentPrintln(" you may not use this file except in compliance with the License.");
        out.indentPrintln(" You may obtain a copy of the License at");
        out.indentPrintln("");
        out.indentPrintln(" http://www.opensource.org/licenses/ecl2.php");
        out.println("");
        out.indentPrintln(" Unless required by applicable law or agreed to in writing, software");
        out.indentPrintln(" distributed under the License is distributed on an \"AS IS\" BASIS,");
        out.indentPrintln(" WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
        out.indentPrintln(" See the License for the specific language governing permissions and");
        out.indentPrintln(" limitations under the License.");
        out.indentPrintln("-->");
        out.indentPrintln("<beans xmlns=\"http://www.springframework.org/schema/beans\"");
        out.indentPrintln("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        out.indentPrintln("xsi:schemaLocation=\""
                + "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd" + "\">");
        out.println("");
        out.incrementIndent();
    }

    private void writeWarning(XmlWriter out) {
        out.println("");
        out.indentPrintln("<!-- ********************************************************");
        out.indentPrintln("                           WARNING ");
        out.indentPrintln("                 DO NOT UPDATE THIS FILE MANUALLY");
        out.indentPrintln("    This dictionary file was automatically generated on " + new Date());
        out.indentPrintln("    The DictionaryGeneratorMojo reads the service contract ");
        out.indentPrintln("    and creates these ks-XXXX-dictionary-generated.xml files.");
        out.indentPrintln("    ");
        out.indentPrintln("    If this file is out of sync with the contract re-run the mojo.");
        out.indentPrintln("    ");
        out.indentPrintln("    To add in additional constraints or change these default values (perhaps");
        out.indentPrintln("    because the generator is not perfect) please update the corresponding ");
        out.indentPrintln("    ks-XXXX-dictionary.xml instead of this one.");
        out.indentPrintln("************************************************************* -->");
    }

    private void writeNote(XmlWriter out) {
        out.println("");
        out.indentPrintln("<!-- ********************************************************");
        out.indentPrintln("                           NOTE");
        out.indentPrintln("              THIS FILE WAS INTENDED TO BE MODIFIED");
        out.println ("");        
        out.indentPrintln("    While this file was originally generated on " + new Date());
        out.indentPrintln("    it was intended to be subsequently modified by hand.");
        out.indentPrintln("    It imports a corresponding ks-XXXX-dictionary-generated.xml file");
        out.indentPrintln("    that was also automatically generated by the DictionaryCreatorMojo.");
        out.indentPrintln("    This file gives you the ability to layer in addiditional definitions");
        out.indentPrintln("    that are not/cannot be generated simply by reading the service contract");
        out.println ("");
        out.indentPrintln("    The goal is to be able to easily re-generate the ks-XXXX-dictionary-generated.xml file");        
        out.indentPrintln("    without affecting the manually entered additions that are coded here.");              
        out.indentPrintln("************************************************************* -->");
    }

    private void writeGeneratedImports(XmlWriter out) {
        out.indentPrintln("<import resource=\"classpath:ks-base-dictionary.xml\"/>");
        // TODO: only write out the ones that are used in this structure
//        out.indentPrintln("<import resource=\"classpath:ks-RichTextInfo-dictionary.xml\"/>");
//        out.indentPrintln("<import resource=\"classpath:ks-MetaInfo-dictionary.xml\"/>");
    }

    private void writeBaseImports(XmlWriter out) {
        out.indentPrintln("<import resource=\"classpath:ks-" + initUpper(className) + "-dictionary-generated.xml\"/>");
    }

    private String stripListOffEnd(String name) {
        if (name.endsWith("List")) {
            return name.substring(0, name.length() - "List".length());
        }
        return name;
    }

    private void writeSubStructures(XmlType type, Stack<String> stack) {
        boolean first = true;
        for (MessageStructure ms : finder.findMessageStructures(type.getName())) {
            XmlType st = finder.findXmlType(this.stripListOffEnd(ms.getType()));
            if (st == null) {
                throw new NullPointerException(ms.getType() + " does not exist in list of types with parents " + calcParents(stack));
            }
            if (!st.getPrimitive().equalsIgnoreCase(XmlType.COMPLEX)) {
                continue;
            }
            if (first) {
                first = false;
                gwriter.indentPrintln("<ul>");
            }
            if (!stack.contains(st.getName())) {
                stack.push(st.getName());
                this.writeSubStructures(st, stack);
                stack.pop();
            }
        }
        if (!first) {
            gwriter.indentPrintln("</ul>");
        }
    }

    private String calcParents(Stack<String> stack) {
        StringBuilder sb = new StringBuilder();
        String dot = "";
        Enumeration<String> en = stack.elements();
        while (en.hasMoreElements()) {
            sb.append(dot);
            dot = ".";
            sb.append(en.nextElement());
        }
        return sb.toString();
    }

    private void writeGeneratedObjectStructure(XmlWriter out) {
        //Step 1, create the abstract structure
        out.println("");
        out.indentPrintln("<!-- " + className + "-->");
        out.indentPrintln("<bean id=\"" + initUpper(className) + "-generated\" abstract=\"true\" parent=\"DataObjectEntry\">");
        out.incrementIndent();
        writeProperty("name", initLower(className));
        writeProperty("objectClass", xmlType.getJavaPackage() + "." + initUpper(className));
        writeProperty("objectLabel", calcObjectLabel());
        writeProperty("objectDescription", xmlType.getDesc());
        String titleAttribute = calcTitleAttribute();
        if (titleAttribute != null) {
            writeProperty("titleAttribute", titleAttribute);
        }
        out.indentPrintln("<property name=\"primaryKeys\">");
        List<String> pks = calcPrimaryKeys();
        if (pks != null && !pks.isEmpty()) {
            out.incrementIndent();
            out.indentPrintln("<list>");
            out.incrementIndent();
            for (String pk : pks) {
                addValue(pk);
            }
            out.decrementIndent();
            out.indentPrintln("</list>");
            out.decrementIndent();
        }
        out.indentPrintln("</property>");
        out.indentPrintln("<property name=\"attributes\">");
        out.incrementIndent();
        out.indentPrintln("<list>");
        out.incrementIndent();
        this.writeGeneratedAttributeRefBeans(className, null, new Stack<String>(), this.messageStructures, out);
        out.decrementIndent();
        out.indentPrintln("</list>");
        out.decrementIndent();
        out.indentPrintln("</property>");
        out.decrementIndent();
        out.indentPrintln("</bean>");

        //Step 2, loop through attributes
        this.writeGeneratedAttributeDefinitions(className, null, new Stack<String>(), this.messageStructures, out);

    }

    private void addValue(String value) {
        gwriter.indentPrintln("<value>" + value + "</value>");
    }

    private String calcObjectLabel() {
        String label = this.className;
        if (label.endsWith("Info")) {
            label = label.substring(0, label.length() - "Info".length());
        }
        label = initUpper(label);
        return splitCamelCase(label);
    }

    // got this from http://stackoverflow.com/questions/2559759/how-do-i-convert-camelcase-into-human-readable-names-in-java
    private static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                "(?<=[A-Z])(?=[A-Z][a-z])",
                "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"),
                " ");
    }

    private void writeGeneratedAttributeRefBeans(String className, String parentFieldName,
            Stack<String> parents, List<MessageStructure> fields, XmlWriter out) {
        if (parents.contains(className)) {
            return;
        }
        for (MessageStructure ms : fields) {
            String fieldName = calcFieldName(className, parentFieldName, ms);
            out.indentPrintln("<ref bean=\"" + fieldName + "\"/>");
            // Add complex sub-types fields
            String childTypeName = this.stripListOffEnd(ms.getType());
            XmlType childType = this.finder.findXmlType(childTypeName);
            if (childType == null) {
                throw new IllegalStateException(childTypeName);
            }
            if (childType.getPrimitive().equalsIgnoreCase(XmlType.COMPLEX)) {
                parents.push(className);
                List<MessageStructure> childFields = this.finder.findMessageStructures(childTypeName);
                if (childFields.isEmpty()) {
                    throw new IllegalStateException(childTypeName);
                }
                writeGeneratedAttributeRefBeans(childTypeName, fieldName, parents, childFields, out);
                parents.pop();
            }
        }
    }

    private void writeGeneratedAttributeDefinitions(String className, String parentFieldName,
            Stack<String> parents, List<MessageStructure> fields, XmlWriter out) {
        if (parents.contains(className)) {
            return;
        }
        for (MessageStructure ms : fields) {
            String fieldName = calcFieldName(className, parentFieldName, ms);
            String childTypeName = this.stripListOffEnd(ms.getType());
            XmlType childType = this.finder.findXmlType(childTypeName);
            if (childType == null) {
                throw new IllegalStateException(childTypeName);
            }
            writeGeneratedAttributeDefinition(className, parentFieldName, ms, out);

            // Add complex sub-types fields
            if (childType.getPrimitive().equalsIgnoreCase(XmlType.COMPLEX)) {
                parents.push(className);
                List<MessageStructure> childFields = this.finder.findMessageStructures(childTypeName);
                if (childFields.isEmpty()) {
                    throw new IllegalStateException(childTypeName);
                }
                writeGeneratedAttributeDefinitions(childTypeName, fieldName, parents, childFields, out);
                parents.pop();
            }
        }
    }

    private void writeGeneratedAttributeDefinition(String className, String parentFieldName, MessageStructure ms, XmlWriter out) {

        //Create the abstract field
        String fieldName = this.calcFieldName(className, parentFieldName, ms);
        String baseKualiType = this.calcBaseKualiType(ms);
        out.println("");
        out.indentPrintln("<bean id=\"" + fieldName + "-generated\" abstract=\"true\" parent=\"" + baseKualiType + "\">");
        out.incrementIndent();
        writeProperty("name", initLower(ms.getShortName()));
        if (ms.isOverriden()) {
            writeProperty("shortLabel", ms.getName());
        }
        out.decrementIndent();
        // TODO: implement maxoccurs
//        if (isList(pd)) {
//            addProperty("maxOccurs", "" + DictionaryConstants.UNBOUNDED, s);
//        }
        out.indentPrintln("</bean>");
    }

    private void writeBaseObjectStructure(XmlWriter out) {
        //Step 1, create the parent bean
        out.println("");
        out.indentPrintln("<!-- " + className + "-->");
        out.indentPrintln("<bean id=\"" + initUpper(className) + "-parent\" abstract=\"true\" parent=\"" + initUpper(className) + "-generated\">");
        out.writeComment("insert any overrides to the generated object definitions here");
        out.indentPrintln("</bean>");

        //Create the actual instance of the bean
        out.indentPrintln("<bean id=\"" + initUpper(className) + "\" parent=\"" + initUpper(className) + "-parent\"/>");
        out.println("");

        //Step 2, loop through attributes
        this.writeBaseAttributeDefinitions(className, null, new Stack<String>(), this.messageStructures, out);

    }

    private void writeBaseAttributeDefinitions(String className, String parentFieldName,
            Stack<String> parents, List<MessageStructure> fields, XmlWriter out) {
        if (parents.contains(className)) {
            return;
        }
        for (MessageStructure ms : fields) {
            String fieldName = calcFieldName(className, parentFieldName, ms);
            String childTypeName = this.stripListOffEnd(ms.getType());
            XmlType childType = this.finder.findXmlType(childTypeName);
            if (childType == null) {
                throw new IllegalStateException(childTypeName);
            }
            writeBaseAttributeDefinition(className, parentFieldName, ms, out);

            // Add complex sub-types fields
            if (childType.getPrimitive().equalsIgnoreCase(XmlType.COMPLEX)) {
                parents.push(className);
                List<MessageStructure> childFields = this.finder.findMessageStructures(childTypeName);
                if (childFields.isEmpty()) {
                    throw new IllegalStateException(childTypeName);
                }
                writeBaseAttributeDefinitions(childTypeName, fieldName, parents, childFields, out);
                parents.pop();
            }
        }
    }

    private void writeBaseAttributeDefinition(String className, String parentFieldName, MessageStructure ms, XmlWriter out) {

        //Create the abstract field
        String fieldName = this.calcFieldName(className, parentFieldName, ms);
//        String baseKualiType = this.calcBaseKualiType(ms);
        out.println("");
        out.indentPrintln("<bean id=\"" + fieldName + "-parent\" abstract=\"true\" parent=\"" + fieldName + "-generated\">");
        out.writeComment("insert any overrides to the generated attribute definitions here");
        out.indentPrintln("</bean>");

        //Create the actual bean instance
        out.indentPrintln("<bean id=\"" + fieldName + "\" parent=\"" + fieldName + "-parent\"/>");
    }

    private String calcBaseKualiType(MessageStructure ms) {

        String name = ms.getShortName();
        if (name.equals("id")) {
            return "BaseKuali.id";
        }
        if (name.equals("key")) {
            return "BaseKuali.key";
        }
        if (name.equals("descr")) {
            return "BaseKuali.descr";
        }
        if (name.equals("name")) {
            return "BaseKuali.name";
        }
        if (name.equals("typeKey")) {
            return "BaseKuali.typeKey";
        }
        if (name.equals("stateKey")) {
            return "BaseKuali.stateKey";
        }
        // to handle r1 services
        if (name.equals("type")) {
            return "BaseKuali.typeKey";
        }
        if (name.equals("state")) {
            return "BaseKuali.stateKey";
        }
        if (name.equals("effectiveDate")) {
            return "BaseKuali.effectiveDate";
        }
        if (name.equals("expirationDate")) {
            return "BaseKuali.expirationDate";
        }
        if (name.endsWith("orgId")) {
            return "BaseKuali.orgId";
        }
        if (name.endsWith("personId")) {
            return "BaseKuali.personId";
        }
        if (name.endsWith("principalId")) {
            return "BaseKuali.principalId";
        }
        if (name.endsWith("cluId")) {
            return "BaseKuali.cluId";
        }
        if (name.endsWith("luiId")) {
            return "BaseKuali.luiId";
        }
        if (name.endsWith("Code")) {
            return "BaseKuali.code";
        }

        // now key off the type
        String type = this.stripListOffEnd(ms.getType());
        if (type.equalsIgnoreCase("String")) {
            return "BaseKuali.string";
        }
        if (type.equalsIgnoreCase("DateTime")) {
            return "BaseKuali.dateTime";
        }
        if (type.equalsIgnoreCase("Date")) {
            return "BaseKuali.date";
        }
        if (type.equalsIgnoreCase("Boolean")) {
            return "BaseKuali.boolean";
        }
        if (type.equalsIgnoreCase("Integer")) {
            return "BaseKuali.integer";
        }
//        if (type.equalsIgnoreCase("Float")) {
//            return "BaseKuali.Currency";
//        }
        if (type.equalsIgnoreCase(XmlType.COMPLEX)) {
            return "BaseKuali.complex";
        }
        // all else fails call it a string
        return "BaseKuali.string";

    }

    private String calcFieldName(String className, String parentFieldName, MessageStructure ms) {
        if (parentFieldName == null) {
            return initUpper(className) + "." + initLower(ms.getShortName());
        }
        return parentFieldName + "." + initLower(ms.getShortName());
    }

    private String calcTitleAttribute() {
        MessageStructure ms = null;
        ms = this.findMessageStructure("name");
        if (ms != null) {
            return initLower(ms.getShortName());
        }
        ms = this.findMessageStructure("title");
        if (ms != null) {
            return initLower(ms.getShortName());
        }
        ms = this.findMessageStructureEndsWith("name");
        if (ms != null) {
            return initLower(ms.getShortName());
        }
        ms = this.findMessageStructureEndsWith("title");
        if (ms != null) {
            return initLower(ms.getShortName());
        }
        ms = this.findMessageStructure("key");
        if (ms != null) {
            return initLower(ms.getShortName());
        }
        // TODO: consider checking for ID and just returning null
        System.out.println("XmlKradBaseDictionaryCreator: could not find a title attribute for " + this.className);
//        ms = this.findMessageStructure("id");
//        if (ms != null) {
//            return initLower(ms.getShortName());
//        }
        return null;
    }

    private MessageStructure findMessageStructureEndsWith(String shortNameEndsWith) {
        shortNameEndsWith = shortNameEndsWith.toLowerCase();
        for (MessageStructure ms : this.messageStructures) {
            if (ms.getShortName().toLowerCase().endsWith(shortNameEndsWith)) {
                return ms;
            }
        }
        return null;
    }

    private MessageStructure findMessageStructure(String shortName) {
        for (MessageStructure ms : this.messageStructures) {
            if (ms.getShortName().equalsIgnoreCase(shortName)) {
                return ms;
            }
        }
        return null;
    }

    private MessageStructure getMessageStructure(String shortName) {
        MessageStructure ms = this.findMessageStructure(shortName);
        if (ms == null) {
            throw new IllegalArgumentException(shortName);
        }
        return ms;
    }

    private List<String> calcPrimaryKeys() {
        MessageStructure ms = null;
        ms = this.findMessageStructure("id");
        if (ms != null) {
            return Collections.singletonList(initLower(ms.getShortName()));
        }
        ms = this.findMessageStructure("key");
        if (ms != null) {
            return Collections.singletonList(initLower(ms.getShortName()));
        }
        return Collections.EMPTY_LIST;
    }

    private void writeProperty(String propertyName, String propertyValue) {
        gwriter.indentPrintln("<property name=\"" + propertyName + "\" value=\"" + propertyValue + "\"/>");
    }
}
