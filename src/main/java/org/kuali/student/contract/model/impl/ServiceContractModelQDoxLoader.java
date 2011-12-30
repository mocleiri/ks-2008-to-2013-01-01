/*
 * Copyright 2010 The Kuali Foundation
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
package org.kuali.student.contract.model.impl;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.DefaultDocletTagFactory;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.Type;
import com.thoughtworks.qdox.model.annotation.AnnotationValue;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kuali.student.contract.model.MessageStructure;
import org.kuali.student.contract.model.Service;
import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.ServiceMethod;
import org.kuali.student.contract.model.ServiceMethodError;
import org.kuali.student.contract.model.ServiceMethodParameter;
import org.kuali.student.contract.model.ServiceMethodReturnValue;
import org.kuali.student.contract.model.XmlType;

/**
 *
 * @author nwright
 */
public class ServiceContractModelQDoxLoader implements
        ServiceContractModel {

    private static final String LOCALE_KEY_LIST = "LocaleKeyList";
    private static final String MESSAGE_GROUP_KEY_LIST = "MessageGroupKeyList";
    private static final JavaClass STRING_JAVA_CLASS = new JavaClass(
            "java.lang.String");
    private List<String> sourceDirectories = null;
    private List<Service> services = null;
    private List<ServiceMethod> serviceMethods = null;
    private Map<String, XmlType> xmlTypeMap = null;
    private List<MessageStructure> messageStructures;
    private boolean validateKualiStudent = true;

    public ServiceContractModelQDoxLoader(List<String> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
    }

    public ServiceContractModelQDoxLoader(List<String> sourceDirectories, boolean validateKualiStudent) {
        this.sourceDirectories = sourceDirectories;
        this.setValidateKualiStudent(validateKualiStudent);
    }

    public boolean isValidateKualiStudent() {
        return validateKualiStudent;
    }

    public void setValidateKualiStudent(boolean validateKualiStudent) {
        this.validateKualiStudent = validateKualiStudent;
    }

    @Override
    public List<ServiceMethod> getServiceMethods() {
        if (this.serviceMethods == null) {
            this.parse();
        }
        return this.serviceMethods;
    }

    @Override
    public List<String> getSourceNames() {
        List<String> list = new ArrayList(this.sourceDirectories.size());
        for (String javaFile : this.sourceDirectories) {
            list.add(javaFile);
        }
        return list;
    }

    @Override
    public List<Service> getServices() {
        if (services == null) {
            this.parse();
        }
        return services;
    }

    @Override
    public List<XmlType> getXmlTypes() {
        if (xmlTypeMap == null) {
            this.parse();
        }
        return new ArrayList(xmlTypeMap.values());
    }

    @Override
    public List<MessageStructure> getMessageStructures() {
        if (messageStructures == null) {
            this.parse();
        }
        return this.messageStructures;
    }

    private String dump(DocletTag tag) {
        if (tag == null) {
            return null;
        }
        StringBuilder bldr = new StringBuilder();
        bldr.append(tag.getName());
        bldr.append("=");
        if (tag.getNamedParameterMap() == null
                || tag.getNamedParameterMap().isEmpty()) {
            bldr.append(tag.getValue());
        } else {
            for (Object key : tag.getNamedParameterMap().keySet()) {
                Object value = tag.getNamedParameterMap().get(key);
                bldr.append("(");
                bldr.append(key);
                bldr.append("=");
                bldr.append(value);
                bldr.append(")");
            }
        }
        return bldr.toString();
    }

    private void checkIfExists(String sourceDirectory) {
        File file = new File(sourceDirectory);
        if (!file.isDirectory()) {
            throw new IllegalArgumentException(sourceDirectory + " is not a directory on disk");
        }
    }

    private void parse() {
//  System.out.println ("ServiceContractModelQDoxLoader: Starting parse");
        services = new ArrayList();
        serviceMethods = new ArrayList();
        xmlTypeMap = new LinkedHashMap();
        messageStructures = new ArrayList();
        DefaultDocletTagFactory dtf = new DefaultDocletTagFactory();
        JavaDocBuilder builder = new JavaDocBuilder(dtf);
        for (String sourceDirectory : sourceDirectories) {
            checkIfExists(sourceDirectory);
            builder.addSourceTree(new File(sourceDirectory));
        }
        List<JavaClass> sortedClasses = Arrays.asList(builder.getClasses());
        Collections.sort(sortedClasses);
        for (JavaClass javaClass : sortedClasses) {
            if (!this.isServiceToProcess(javaClass)) {
                continue;
            }
//   System.out.println ("processing service=" + javaClass.getName ());
            Service service = new Service();
            services.add(service);
            service.setKey(javaClass.getName().substring(0, javaClass.getName().length()
                    - "Service".length()));
            service.setName(javaClass.getName());
            service.setComments(this.calcComment(javaClass));
            service.setUrl(this.calcServiceUrl(javaClass));
            service.setVersion(this.calcVersion(javaClass));
            service.setStatus("???");
            service.setIncludedServices(calcIncludedServices(javaClass));
            service.setImplProject(javaClass.getPackageName());

//   for (DocletTag tag : javaClass.getTags ())
//   {
//    System.out.println ("ServiceContractModelQDoxLoader: Class: "
//                        + javaClass.getName () + " has tag=" + dump (
//      tag));
//   }
            for (JavaMethod javaMethod : javaClass.getMethods()) {

                ServiceMethod serviceMethod = new ServiceMethod();
                serviceMethods.add(serviceMethod);
                serviceMethod.setService(service.getKey());
                serviceMethod.setName(javaMethod.getName());
                serviceMethod.setDescription(calcMissing(javaMethod.getComment()));
                serviceMethod.setParameters(new ArrayList());
//    for (DocletTag tag : javaMethod.getTags ())
//    {
//     System.out.println ("ServiceContractModelQDoxLoader: Method: "
//                         + service.getName () + "."
//                         + javaMethod.getName ()
//                         + " has tag=" + dump (tag));
//    }
                // parameters
                for (JavaParameter parameter : javaMethod.getParameters()) {
                    ServiceMethodParameter param = new ServiceMethodParameter();
                    serviceMethod.getParameters().add(param);
                    param.setName(parameter.getName());
                    param.setType(calcType(parameter.getType()));
                    param.setDescription(calcMissing(
                            calcParameterDescription(javaMethod,
                            param.getName())));
                    addXmlTypeAndMessageStructure(calcRealJavaClass(parameter.getType()),
                            serviceMethod.getService());
                }
                // errors
                serviceMethod.setErrors(new ArrayList());
                for (Type exception : javaMethod.getExceptions()) {
                    ServiceMethodError error = new ServiceMethodError();
                    error.setType(this.calcType(exception.getJavaClass()));
                    error.setDescription(calcMissing(
                            calcExceptionDescription(javaMethod,
                            error.getType())));
                    error.setPackageName(exception.getJavaClass().getPackageName());
                    error.setClassName(exception.getJavaClass().getName());
                    serviceMethod.getErrors().add(error);
                }
                // return values
                ServiceMethodReturnValue rv = new ServiceMethodReturnValue();
                serviceMethod.setReturnValue(rv);
                Type returnType = null;
                try {
                    returnType = javaMethod.getReturnType();
                } catch (NullPointerException ex) {
                    System.out.println("Nullpinter getting return type: " + javaMethod.getCallSignature());
                    returnType = null;
                }

                rv.setType(calcType(returnType));
                rv.setDescription(calcMissing(this.calcReturnDescription(javaMethod)));
                if (returnType != null) {
                    addXmlTypeAndMessageStructure(calcRealJavaClass(returnType),
                            serviceMethod.getService());
                }
            }
        }
    }

    private boolean isServiceToProcess(JavaClass javaClass) {
//  System.out.println ("looking if javaClass is a service to process=" + javaClass.getName () + "=" + javaClass.getPackageName ());
        if (!javaClass.getName().endsWith("Service")) {
            return false;
        }
        if (javaClass.getPackageName().contains(".old.")) {

            return false;
        }
        if (javaClass.getPackageName().endsWith(".old")) {
            return false;
        }
        for (Annotation annotation : javaClass.getAnnotations()) {
//   System.out.println ("looking for webservice tag=" + annotation.getType ().getJavaClass ().getName ());
            if (annotation.getType().getJavaClass().getName().equals("WebService")) {
//    System.out.println ("Processing web service=" + javaClass.getPackageName ()
//                        + "." + javaClass.getName ());
                return true;
            }
        }
        // This includes RICE's business object services even though they are not web services
        // because often they are the only real service they have exposed and it helps to document
        // them to see what the data really is like
        if (javaClass.getName().endsWith("BoService")) {
            return true;
        }
//  System.out.println ("skipping service because it is not a web service="
//                      + javaClass.getPackageName () + "." + javaClass.getName ());
        return false;
    }

    private List<String> calcIncludedServices(JavaClass javaClass) {
        List<String> includedServices = new ArrayList<String>();
        for (JavaClass interfaceClass : javaClass.getImplementedInterfaces()) {
//            System.out.println("ServiceContractModelQDoxLoader:" + javaClass.getName()
//                    + " implements " + interfaceClass.getName());
            includedServices.add(interfaceClass.getName());
        }
        return includedServices;
    }

    private String calcParameterDescription(JavaMethod method,
            String parameterName) {
        for (DocletTag tag : method.getTags()) {
            if (tag.getName().equals("param")) {
                if (tag.getValue().startsWith(parameterName + " ")) {
                    return tag.getValue().substring(parameterName.length() + 1);
                }
            }
        }
        return null;
    }

    private String calcExceptionDescription(JavaMethod serviceMethod,
            String exceptionType) {
        for (DocletTag tag : serviceMethod.getTags()) {
            if (tag.getName().equals("throws")) {
                if (tag.getValue().startsWith(exceptionType + " ")) {
                    return tag.getValue().substring(exceptionType.length() + 1);
                }
            }
        }
        return null;
    }

    private String calcReturnDescription(JavaMethod serviceMethod) {
        for (DocletTag tag : serviceMethod.getTags()) {
            if (tag.getName().equals("return")) {
                return tag.getValue();
            }
        }
        return null;
    }

    private String calcServiceUrl(JavaClass serviceClass) {
        for (DocletTag tag : serviceClass.getTags()) {
            if (tag.getName().equals("See")) {
                return tag.getValue();
            }
        }
        return null;
    }

    private void addXmlTypeAndMessageStructure(JavaClass messageStructureJavaClass,
            String serviceKey) {
        String name = calcType(messageStructureJavaClass);
        XmlType xmlType = xmlTypeMap.get(name);
        if (xmlType == null) {
            xmlType = new XmlType();
            xmlTypeMap.put(name, xmlType);
            xmlType.setName(name);
            xmlType.setDesc(this.calcMessageStructureDesc(messageStructureJavaClass));
            xmlType.setService(serviceKey);
            xmlType.setVersion("IGNORE -- SAME AS SERVICE");
            xmlType.setPrimitive(calcPrimitive(messageStructureJavaClass));
            xmlType.setJavaPackage(calcJavaPackage(messageStructureJavaClass));
            if (xmlType.getPrimitive().equals(XmlType.COMPLEX)) {
                addMessageStructure(messageStructureJavaClass, serviceKey);
            }
        } else {
            addServiceToList(xmlType, serviceKey);
        }
    }

    private String calcJavaPackage(JavaClass javaClass) {
        String packageName = javaClass.getPackageName();
        return packageName;
    }

    private String calcMessageStructureDesc(JavaClass javaClass) {
        {
            String desc = javaClass.getComment();
            if (desc != null) {
                if (!desc.isEmpty()) {
                    return desc;
                }
            }
            JavaClass infcClass = this.getMatchingInfc(javaClass);
            if (infcClass == null) {
                return null;
            }
            return infcClass.getComment();
        }
    }

    private JavaClass getMatchingInfc(JavaClass javaClass) {
        // ks uses this pattern
        String nameInfc = javaClass.getName();
        if (nameInfc.endsWith("Info")) {
            nameInfc = nameInfc.substring(0, nameInfc.length() - "Info".length())
                    + "Infc";
        }
        String nameWithOutInfo = javaClass.getName();
        // rice uses this pattern
        if (nameWithOutInfo.endsWith("Info")) {
            nameWithOutInfo = nameWithOutInfo.substring(0, nameWithOutInfo.length()
                    - "Info".length());
        }
        for (JavaClass infc : javaClass.getImplementedInterfaces()) {
            if (infc.getName().equals(nameInfc)) {
//                System.out.println("found matching interface " + infc.getName());
                return infc;
            }
            if (infc.getName().equals(nameWithOutInfo)) {
                return infc;
            }
        }
        return null;
    }

    private String calcPrimitive(JavaClass javaClass) {
        if (this.isComplex(javaClass)) {
            return XmlType.COMPLEX;
        }
        return "Primitive";
    }

    private String initLower(String str) {
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

    private String initUpper(String str) {
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

    private Set<String> getShortNames(JavaClass messageStructureJavaClass) {
        Set<String> fields = getFieldsUsingPropOrder(messageStructureJavaClass);
        if (fields != null) {
            return fields;
        }
        fields = new LinkedHashSet();
        for (JavaMethod method : messageStructureJavaClass.getMethods(true)) {
            if (isSetterMethodToProcess(method, messageStructureJavaClass.getName())) {
                String shortName = this.calcShortNameFromSetter(method);
                fields.add(shortName);
                continue;
            }
            if (isGetterMethodToProcess(method, messageStructureJavaClass.getName())) {
                String shortName = this.calcShortNameFromGetter(method);
                fields.add(shortName);
                continue;
            }
        }
        return fields;
    }

    private Set<String> getFieldsUsingPropOrder(
            JavaClass messageStructureJavaClass) {
        for (Annotation annotation : messageStructureJavaClass.getAnnotations()) {
            if (annotation.getType().getJavaClass().getName().equals("XmlType")) {
                AnnotationValue propOrderParam = annotation.getProperty("propOrder");
                if (propOrderParam == null) {
                    continue;
                }
                Object propOrderValue = propOrderParam.getParameterValue();
                if (!(propOrderValue instanceof List)) {
                    continue;
                }
                Set<String> fields = new LinkedHashSet();
                for (Object value : (List) propOrderValue) {
                    if (value instanceof String) {
                        String shortName = (String) value;
                        shortName = this.stripQuotes(shortName);
                        if (shortName.contains(".Elements.")) {
                            String newShortName = getShortNameFromElements(shortName, messageStructureJavaClass);
                            if (newShortName == null) {
                                continue;
                            }
                            shortName = newShortName;
                        } else if (shortName.startsWith("CoreConstants.CommonElements.")) {
                            String newShortName = getCoreConstants(shortName);
                            if (newShortName == null) {
                                continue;
                            }
                            shortName = newShortName;
                        }
                        if (shortName.equals("_futureElements")) {
                            continue;
                        }
                        shortName = this.initUpper(shortName);
                        fields.add(shortName);
                    }
                }
                return fields;
            }
        }
        return null;
    }

    private String getShortNameFromElements(String shortName, JavaClass messageStructureJavaClass) {
        JavaClass elementsJavaClass = messageStructureJavaClass.getNestedClassByName("Elements");
        if (elementsJavaClass == null) {
            return null;
        }
        String fieldName = shortName.substring(shortName.indexOf(".Elements.") + ".Elements.".length());
        JavaField field = elementsJavaClass.getFieldByName(fieldName);
        String initExpr = field.getInitializationExpression();
        return stripQuotes(initExpr);
    }

    private String getCoreConstants(String shortName) {
        if (shortName.endsWith("VERSION_NUMBER")) {
            return "versionNumber";
        }
        if (shortName.endsWith("OBJECT_ID")) {
            return "objectId";
        }
        if (shortName.endsWith("ACTIVE")) {
            return "active";
        }
        if (shortName.endsWith("ACTIVE_FROM_DATE")) {
            return "activeFromDate";
        }
        if (shortName.endsWith("ACTIVE_TO_DATE")) {
            return "activeToDate";
        }
        if (shortName.endsWith("ATTRIBUTES")) {
            return "attributes";
        }
        if (shortName.endsWith("FUTURE_ELEMENTS")) {
            return "_futureElements";
        }
        throw new RuntimeException("Unknown shortName " + shortName);
    }

    private void addMessageStructure(JavaClass messageStructureJavaClass,
            String serviceKey) {
        Set<JavaClass> subObjectsToAdd = new LinkedHashSet();
        for (String shortName : this.getShortNames(messageStructureJavaClass)) {
            JavaMethod setterMethod = findSetterMethod(messageStructureJavaClass,
                    shortName);
            JavaMethod getterMethod = findGetterMethod(messageStructureJavaClass,
                    shortName);
            if (getterMethod == null) {
                if (this.validateKualiStudent) {
                    throw new IllegalArgumentException("shortName has no corresponding getter method: "
                            + messageStructureJavaClass.getFullyQualifiedName()
                            + "." + shortName);
                }
            }
            JavaField beanField = this.findField(messageStructureJavaClass,
                    shortName, setterMethod);
            if (beanField == null) {
                String accessorType = getAccessorType(getterMethod);
                if ("XmlAccessType.FIELD".equals(accessorType)) {
                    throw new IllegalArgumentException("Setter method has no corresponding bean field: "
                            + messageStructureJavaClass.getName()
                            + "." + getterMethod.getName());
                }
            }
            // overide the shortName if the bean field has an XmlAttribute name=xxx
            // this catches the key=id switch
            if (beanField != null) {
                for (Annotation annotation : beanField.getAnnotations()) {
                    if (annotation.getType().getJavaClass().getName().equals("XmlAttribute")) {
                        Object nameValue = annotation.getNamedParameter("name");
                        if (nameValue != null) {
                            shortName = stripQuotes(nameValue.toString());
                        }
                    }
                }
            }
            shortName = initLower(shortName);
            MessageStructure ms = new MessageStructure();
            messageStructures.add(ms);
            ms.setXmlObject(messageStructureJavaClass.getName());
            ms.setShortName(shortName);
            ms.setId(ms.getXmlObject() + "." + ms.getShortName());
            ms.setName(calcMissing(calcName(messageStructureJavaClass, getterMethod, setterMethod,
                    beanField, shortName)));
            ms.setType(calcType(messageStructureJavaClass, getterMethod, setterMethod, beanField, shortName));
            if (ms.getType().equals("Object")) {
                System.out.println("WARNING " + ms.getId()
                        + " has Object as it's type ==> Changing to String");
                ms.setType("String");
            } else if (ms.getType().equals("ObjectList")) {
                System.out.println(
                        "WARNING " + ms.getId()
                        + " has a list of Objects as it's type ==> Changing to List of String");
                ms.setType("StringList");
            }
            ms.setXmlAttribute(this.calcXmlAttribute(beanField));
            ms.setRequired(calcRequired(getterMethod, setterMethod, beanField));
            ms.setReadOnly(calcReadOnly(getterMethod, setterMethod, beanField));
            ms.setCardinality(this.calcCardinality(messageStructureJavaClass, getterMethod, setterMethod, beanField, shortName));
            ms.setDescription(calcMissing(calcDescription(messageStructureJavaClass, getterMethod, setterMethod,
                    beanField)));
            ms.setImplNotes(calcImplementationNotes(getterMethod, setterMethod, beanField));
            ms.setStatus("???");
//            if (ms.getId().equals("AcademicCalendarInfo.typeKey")) {
//                System.out.println("debug from here");
//            }
            ms.setOverriden(this.calcOverridden(messageStructureJavaClass, getterMethod));
            JavaClass subObjToAdd = this.calcRealJavaClassOfGetterReturn(getterMethod);
            if (subObjToAdd != null) {
//                if (!subObjToAdd.isEnum()) {
                    if (!subObjToAdd.getName().equals("Object")) {
                        if (!subObjToAdd.getName().equals("LocaleKeyList")) {
                            if (!subObjToAdd.getName().equals("MessageGroupKeyList")) {
                                subObjectsToAdd.add(subObjToAdd);
                            }
                        }
                    }
//                }
            }
        }
        // now add all it's complex sub-objects if they haven't already been added
        for (JavaClass subObjectToAdd : subObjectsToAdd) {
            XmlType xmlType = xmlTypeMap.get(calcType(subObjectToAdd));
            if (xmlType == null) {
                addXmlTypeAndMessageStructure(subObjectToAdd, serviceKey);
            } else {
                addServiceToList(xmlType, serviceKey);
            }
        }
        return;
    }

    private boolean calcOverridden(JavaClass mainClass, JavaMethod getterMethod) {
        if (getterMethod == null) {
            return false;
        }
        JavaMethod infcGetter = null;
        if (getterMethod.getParentClass().isInterface()) {
            infcGetter = getterMethod;
        }
        if (infcGetter == null) {
            infcGetter = findInterfaceMethod(mainClass, getterMethod);
        }
        if (infcGetter == null) {
            return false;
        }
        Annotation annotation = this.getAnnotation(infcGetter, null, null, "Override");
        if (annotation != null) {
            return true;
        }
        return false;
    }

    private String calcComment(JavaClass javaClass) {
        return this.calcComment(javaClass.getComment());
    }

    private String calcComment(String comment) {
        return this.parseCommentVersion(comment)[0];
    }

    private String calcVersion(JavaClass javaClass) {
        DocletTag tag = javaClass.getTagByName("version", true);
        if (tag != null) {
            return tag.getValue();
        }
        return this.calcVersion(javaClass.getComment());
    }

    private String calcVersion(String comment) {
        return this.parseCommentVersion(comment)[1];
    }

    private String[] parseCommentVersion(String commentVersion) {
        String[] parsed = new String[2];
        if (commentVersion == null) {
            return parsed;
        }
        commentVersion = commentVersion.trim();
        int i = commentVersion.toLowerCase().indexOf("\nversion:");
        if (i == -1) {
            parsed[0] = commentVersion;
            return parsed;
        }
        parsed[1] = commentVersion.substring(i + "\nversion:".length()).trim();
        parsed[0] = commentVersion.substring(0, i).trim();

        return parsed;
    }

    private Annotation getAnnotation(JavaMethod getterMethod,
            JavaMethod setterMethod, JavaField beanField, String type) {
        if (beanField != null) {

            for (Annotation annotation : beanField.getAnnotations()) {
                if (annotation.getType().getJavaClass().getName().equals(type)) {
                    return annotation;
                }
            }
        }
        if (getterMethod != null) {

            for (Annotation annotation : getterMethod.getAnnotations()) {
                if (annotation.getType().getJavaClass().getName().equals(type)) {
                    return annotation;
                }
            }
        }
        if (setterMethod != null) {

            for (Annotation annotation : setterMethod.getAnnotations()) {
                if (annotation.getType().getJavaClass().getName().equals(type)) {
                    return annotation;
                }
            }
        }
        return null;
    }

    private String calcRequired(JavaMethod getterMethod,
            JavaMethod setterMethod, JavaField beanField) {
        Annotation annotation = this.getAnnotation(getterMethod, setterMethod, beanField, "XmlElement");
        if (annotation == null) {
            annotation = this.getAnnotation(getterMethod, setterMethod, beanField, "XmlAttribute");
        }
        if (annotation != null) {
            Object required = annotation.getNamedParameter("required");
            if (required != null) {
                if (required.toString().equalsIgnoreCase("true")) {
                    return "Required";
                }
            }
        }
        if (getterMethod != null) {
            DocletTag tag = getterMethod.getTagByName("required", true);
            if (tag != null) {
                if (tag.getValue() == null) {
                    return "Required";
                }
                String required = "Required " + tag.getValue();
                return required.trim();
            }
        }
        return null;
    }

    private String calcReadOnly(JavaMethod getterMethod,
            JavaMethod setterMethod, JavaField beanField) {
        if (getterMethod != null) {
            DocletTag tag = getterMethod.getTagByName("readOnly", true);
            if (tag != null) {
                if (tag.getValue() == null) {
                    return "Read only";
                }
                String readOnly = "Read only " + tag.getValue();
                return readOnly.trim();
            }
        }
        return null;
    }

    private String calcImplementationNotes(JavaMethod getterMethod,
            JavaMethod setterMethod, JavaField beanField) {
        if (getterMethod != null) {
            DocletTag tag = getterMethod.getTagByName("impl", true);
            if (tag != null) {
                return tag.getValue();
            }
        }
        return null;
    }

    private String calcNameFromShortName(String shortName) {
        StringBuilder bldr = new StringBuilder(shortName.length() + 3);
        char c = shortName.charAt(0);
        bldr.append(Character.toUpperCase(c));
        boolean lastWasUpper = true;
        for (int i = 1; i < shortName.length(); i++) {
            c = shortName.charAt(i);
            if (Character.isUpperCase(c)) {
                if (!lastWasUpper) {
                    bldr.append(" ");
                }
            } else {
                lastWasUpper = false;
            }
            bldr.append(c);
        }
        return bldr.toString();
    }

    private String calcName(JavaClass mainClass, JavaMethod getterMethod,
            JavaMethod setterMethod, JavaField beanField, String shortName) {
        String name = this.calcNameFromTag(getterMethod, setterMethod, beanField);
        if (name != null) {
            return name;
        }
        name = this.calcNameFromNameEmbeddedInDescription(mainClass, getterMethod, setterMethod, beanField);
        if (name != null) {
            return name;
        }
        return this.calcNameFromShortName(shortName);
    }

    private String calcNameFromTag(JavaMethod getterMethod,
            JavaMethod setterMethod, JavaField beanField) {
        if (getterMethod != null) {
            DocletTag tag = getterMethod.getTagByName("name", true);
            if (tag != null) {
                return tag.getValue();
            }
        }
        return null;
    }

    private String calcNameFromNameEmbeddedInDescription(JavaClass mainClass, JavaMethod getterMethod,
            JavaMethod setterMethod, JavaField beanField) {
        String nameDesc = this.calcMethodComment(mainClass, getterMethod, setterMethod,
                beanField);
        String[] parsed = parseNameDesc(nameDesc);
        return parsed[0];
    }

    private String[] parseNameDesc(String nameDesc) {
        String[] parsed = new String[2];
        if (nameDesc == null) {
            return parsed;
        }
        nameDesc = nameDesc.trim();
        if (!nameDesc.startsWith("Name:")) {
            parsed[1] = nameDesc;
            return parsed;
        }
        nameDesc = nameDesc.substring("Name:".length()).trim();
        int i = nameDesc.indexOf("\n");
        if (i == -1) {
            parsed[0] = nameDesc.trim();
            return parsed;
        }
        parsed[0] = nameDesc.substring(0, i).trim();
        parsed[1] = nameDesc.substring(i).trim();
        return parsed;
    }

    private String calcDescription(JavaClass mainClass, JavaMethod getterMethod,
            JavaMethod setterMethod, JavaField beanField) {
        String nameDesc = this.calcMethodComment(mainClass, getterMethod, setterMethod,
                beanField);
        String[] parsed = parseNameDesc(nameDesc);
        return parsed[1];
    }

    private String calcMethodComment(JavaClass mainClass, JavaMethod getterMethod,
            JavaMethod setterMethod,
            JavaField beanField) {
        String desc = null;
        if (getterMethod != null) {
            desc = getterMethod.getComment();
            if (isCommentNotEmpty(desc)) {
                return desc;
            }
        }
        if (setterMethod != null) {
            desc = setterMethod.getComment();
            if (isCommentNotEmpty(desc)) {
                return desc;
            }
        }
        if (beanField != null) {
            desc = beanField.getComment();
            if (isCommentNotEmpty(desc)) {
                return desc;
            }
        }
        desc = calcMethodCommentRecursively(mainClass, getterMethod);
        if (isCommentNotEmpty(desc)) {
            return desc;
        }
        desc = calcMethodCommentRecursively(mainClass, setterMethod);
        if (isCommentNotEmpty(desc)) {
            return desc;
        }
        return null;
    }

    private String calcMethodCommentRecursively(JavaClass mainClass, JavaMethod method) {
        if (method == null) {
            return null;
        }
        String desc = method.getComment();
        if (isCommentNotEmpty(desc)) {
            return desc;
        }
        JavaMethod infcMethod = findInterfaceMethod(mainClass, method);
        if (infcMethod != null) {
            desc = infcMethod.getComment();
            if (isCommentNotEmpty(desc)) {
                return desc;
            }
        }
        JavaMethod superMethod = findSuperMethod(method);
        if (superMethod != null) {
            desc = superMethod.getComment();
            if (isCommentNotEmpty(desc)) {
                return desc;
            }
        }
        return null;
    }

    private JavaMethod findSuperMethod(JavaMethod method) {
//        System.out.println("Searching for super method for "
//                + method.getParentClass().getName() + "."
//                + method.getCallSignature());
        for (JavaMethod superMethod : method.getParentClass().getMethods(true)) {
            if (method.equals(superMethod)) {
                continue;
            }
            if (method.getCallSignature().equals(superMethod.getCallSignature())) {
                return superMethod;
            }
        }
        return null;
    }

    private JavaMethod findInterfaceMethod(JavaClass mainClass, JavaMethod method) {
        String callSig = method.getCallSignature();
        JavaClass classToSearch = mainClass;
//        log ("Searching mainClass " + classToSearch.getName() + " for " + callSig, callSig);
        while (true) {
            for (JavaClass infcClass : classToSearch.getImplementedInterfaces()) {
                JavaMethod meth = this.findMethodOnInterfaceRecursively(infcClass, callSig);
                if (meth != null) {
//                    recursionCntr = 0;
                    return meth;
                }
            }
            JavaClass superClass = classToSearch.getSuperJavaClass();
            if (superClass == null) {
//                recursionCntr = 0;                
//                log ("Did not find " + callSig + " on " + mainClass, callSig); 
                return null;
            }
            classToSearch = superClass;
//            log ("Searching superClass " + classToSearch.getName() + " for " + callSig, callSig);                
        }
    }

//    private void log (String message, String callSig) {
//        if (callSig.equalsIgnoreCase("getTypeKey()")) {
//            for (int i = 0; i < this.recursionCntr; i++) {
//                System.out.print (" ");
//            }
//            System.out.println (message);
//        }
//    }
//    private int recursionCntr = 0;
    private JavaMethod findMethodOnInterfaceRecursively(JavaClass infcClass, String callSig) {
//        recursionCntr++;
//        log ("Searching interface " + infcClass.getName() + " for " + callSig, callSig);
        for (JavaMethod infcMethod : infcClass.getMethods()) {
            if (callSig.equals(infcMethod.getCallSignature())) {
//                log (callSig + " found on " + infcClass.getName() + "!!!!!!!!!!!!!!!!", callSig); 
//                recursionCntr--;
                return infcMethod;
            }
        }
        for (JavaClass subInfc : infcClass.getImplementedInterfaces()) {
//            log ("Searching  sub-interface " + subInfc.getName() + " for " + callSig, callSig);
            JavaMethod infcMethod = findMethodOnInterfaceRecursively(subInfc, callSig);
            if (infcMethod != null) {
//                recursionCntr--;
                return infcMethod;
            }
        }
//        log (callSig + " not found on " + infcClass.getName(), callSig);
//        this.recursionCntr--;
        return null;
    }

    private boolean isCommentNotEmpty(String desc) {
        if (desc == null) {
            return false;
        }
        if (desc.trim().isEmpty()) {
            return false;
        }
        if (desc.contains("@inheritDoc")) {
            return false;
        }
        return true;
    }

    private String getAccessorType(JavaMethod method) {
        String accessorType = getAccessorType(method.getAnnotations());
        if (accessorType != null) {
            return accessorType;
        }
        accessorType = getAccessorType(method.getParentClass().getAnnotations());
        return accessorType;
    }

    private String getAccessorType(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.getType().getJavaClass().getName().equals(
                    "XmlAccessorType")) {
//    System.out.println ("Looking for XmlAccessorType annotation = "
//                        + annotation.getParameterValue ());
                return annotation.getParameterValue().toString();
            }
        }
        return null;
    }

    private String stripQuotes(String str) {
        if (str.startsWith("\"")) {
            str = str.substring(1);
        }
        if (str.endsWith("\"")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private String calcMissing(String str) {
        if (str == null) {
            return "???";
        }
        if (str.trim().isEmpty()) {
            return "???";
        }
        return str;
    }

    private void addServiceToList(XmlType xmlType, String serviceKey) {
        if (!xmlType.getService().contains(serviceKey)) {
            xmlType.setService(xmlType.getService() + ", " + serviceKey);
        }
    }

    private String calcXmlAttribute(JavaField beanField) {
        if (beanField == null) {
            // TODO: worry about checking for this annotation on the method for non-field based AccessorTypes
            return "No";
        }
        for (Annotation annotation : beanField.getAnnotations()) {
            if (annotation.getType().getJavaClass().getName().equals("XmlAttribute")) {
                return "Yes";
            }
        }
        return "No";
    }

    private JavaField findField(JavaClass javaClass, String shortName,
            JavaMethod setterMethod) {
        JavaField field = findField(javaClass, shortName);
        if (field != null) {
            return field;
        }
        if (setterMethod != null) {
            String paramName = setterMethod.getParameters()[0].getName();
            if (paramName.equalsIgnoreCase(shortName)) {
                return null;
            }
            return findField(javaClass, paramName);
        }
        return null;
    }

    private JavaField findField(JavaClass javaClass, String name) {
        if (name == null) {
            return null;
        }
        for (JavaField field : javaClass.getFields()) {
            if (field.getName().equalsIgnoreCase(name)) {
                return field;
            }
            // TODO: check for shortNames that already start with is so we don't check for isIsEnrollable
            if (field.getName().equals("is" + name)) {
                return field;
            }
        }
        JavaClass superClass = javaClass.getSuperJavaClass();
        if (superClass == null) {
            return null;
        }
        return findField(superClass, name);
    }

    private JavaMethod findGetterMethod(JavaClass msClass, String shortName) {
        for (JavaMethod method : msClass.getMethods(true)) {
            if (method.getName().equalsIgnoreCase("get" + shortName)) {
                return method;
            }
            if (method.getName().toLowerCase().startsWith("is")) {
                if (method.getName().equalsIgnoreCase("is" + shortName)) {
                    return method;
                }
                // shortName already has "is" in it
                if (method.getName().equalsIgnoreCase(shortName)) {
                    return method;
                }
            }
            // TODO: followup on KimEntityResidencyInfo.getInState
            if (method.getName().equalsIgnoreCase("getInState") && shortName.equalsIgnoreCase(
                    "InStateFlag")) {
                return method;
            }
        }
        return null;
    }

    private JavaMethod findSetterMethod(JavaClass msClass, String shortName) {
        for (JavaMethod method : msClass.getMethods(true)) {
            if (method.getName().equals("set" + shortName)) {
                return method;
            }
            // TODO: check for shortNames that already start with is so we don't check for isIsEnrollable
            if (method.getName().equals("setIs" + shortName)) {
                return method;
            }
            // TODO: followup on KimEntityResidencyInfo.getInState
            if (method.getName().equals("setInStateFlag") && shortName.equals(
                    "InState")) {
                return method;
            }
        }
        return null;
    }
    private static final String[] SETTER_METHODS_TO_SKIP = {
        // Somebody put "convenience" methods on the validation result info
        "ValidationResultInfo.setWarning",
        "ValidationResultInfo.setError",
        // not on original wiki but still defined as a method but not backed by a field so not in wsdl
        "CredentialProgramInfo.setDiplomaTitle",
        // synonym for the official of setCredentialType
        "CredentialProgramInfo.setType",
        // not on original wiki but still defined as a method but not backed by a field so not in wsdl
        "CredentialProgramInfo.setHegisCode",
        "CredentialProgramInfo.setCip2000Code",
        "CredentialProgramInfo.setCip2010Code",
        "CredentialProgramInfo.setSelectiveEnrollmentCode",
        "CoreProgramInfo.setDiplomaTitle",
        // synonym for the official of setCredentialType
        //  "CoreProgramInfo.setType",
        // not on original wiki but still defined as a method but not backed by a field so not in wsdl
        "CoreProgramInfo.setHegisCode",
        "CoreProgramInfo.setCip2000Code",
        "CoreProgramInfo.setCip2010Code",
        "CoreProgramInfo.setSelectiveEnrollmentCode",
        "WhenConstraint.setValue"
    };
    private static final String[] GETTER_METHODS_TO_SKIP = {
        // Somebody put "convenience" methods on the validation result info
        "ValidationResultInfo.getWarning",
        "ValidationResultInfo.getError",
        // not on original wiki but still defined as a method but not backed by a field so not in wsdl
        "CredentialProgramInfo.getDiplomaTitle",
        // synonym for the official of setCredentialType
        "CredentialProgramInfo.getType",
        // not on original wiki but still defined as a method but not backed by a field so not in wsdl
        "CredentialProgramInfo.getHegisCode",
        "CredentialProgramInfo.getCip2000Code",
        "CredentialProgramInfo.getCip2010Code",
        "CredentialProgramInfo.getSelectiveEnrollmentCode",
        "CoreProgramInfo.getDiplomaTitle",
        // synonym for the official of setCredentialType
        //  "CoreProgramInfo.setType",
        // not on original wiki but still defined as a method but not backed by a field so not in wsdl
        "CoreProgramInfo.getHegisCode",
        "CoreProgramInfo.getCip2000Code",
        "CoreProgramInfo.getCip2010Code",
        "CoreProgramInfo.getSelectiveEnrollmentCode",
        "WhenConstraint.getValue"
    };

    private boolean isSetterMethodToProcess(JavaMethod method, String className) {
        if (!method.getName().startsWith("set")) {
            return false;
        }
        if (method.getParameters().length != 1) {
            return false;
        }
        if (method.isPrivate()) {
            return false;
        }
        if (method.isProtected()) {
            return false;
        }
        if (method.isStatic()) {
            return false;
        }
        if (method.getParentClass().getPackageName().startsWith("java")) {
            return false;
        }
        String fullName = className + "." + method.getName();
        for (String skip : SETTER_METHODS_TO_SKIP) {
            if (skip.equals(fullName)) {
                return false;
            }
        }
//  if (method.getParentClass ().isInterface ())
//  {
//   return false;
//  }
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation.getType().getJavaClass().getName().equals("XmlTransient")) {
                return false;
            }
        }
        return true;
    }

    private boolean isGetterMethodToProcess(JavaMethod method, String className) {
        if (!method.getName().startsWith("get")) {
            if (!method.getName().startsWith("is")) {
                return false;
            }
        }
        if (method.getParameters().length != 0) {
            return false;
        }
        if (method.isPrivate()) {
            return false;
        }
        if (method.isProtected()) {
            return false;
        }
        if (method.isStatic()) {
            return false;
        }
        if (method.getParentClass().getPackageName().startsWith("java")) {
            return false;
        }
        String fullName = className + "." + method.getName();
        for (String skip : GETTER_METHODS_TO_SKIP) {
            if (skip.equals(fullName)) {
                return false;
            }
        }
//  if (method.getParentClass ().isInterface ())
//  {
//   return false;
//  }
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation.getType().getJavaClass().getName().equals("XmlTransient")) {
                return false;
            }
        }
        return true;
    }

    private String calcShortNameFromSetter(JavaMethod method) {
        return method.getName().substring(3);
    }

    private String calcShortNameFromGetter(JavaMethod method) {
        if (method.getName().startsWith("get")) {
            return method.getName().substring(3);
        }
        if (method.getName().startsWith("is")) {
            return method.getName().substring(2);
        }
        throw new IllegalArgumentException(method.getName()
                + " does not start with is or get");
    }

    private String calcCardinality(JavaClass mainClass, JavaMethod getterMethod,
            JavaMethod setterMethod, JavaField beanField, String shortName) {
        if (isReturnACollection(mainClass, getterMethod, setterMethod, beanField, shortName)) {
            return "Many";
        }
        return "One";
    }

    private boolean isReturnACollection(JavaClass mainClass, JavaMethod getterMethod,
            JavaMethod setterMethod, JavaField beanField, String shortName) {
        if (getterMethod != null) {
            return isCollection(getterMethod.getReturnType());
        }
        if (beanField != null) {
            return isCollection(beanField.getType());
        }
        // TODO: check setterMethod
        return false;
    }

    private boolean isCollection(Type type) {
        JavaClass javaClass = type.getJavaClass();
        return this.isCollection(javaClass);
    }

    private boolean isCollection(JavaClass javaClass) {
        if (javaClass.getName().equals("LocalKeyList")) {
            return true;
        }
        if (javaClass.getName().equals("MessageGroupKeyList")) {
            return true;
        }
        if (javaClass.getName().equals(List.class.getSimpleName())) {
            return true;
        }
        if (javaClass.getName().equals(ArrayList.class.getSimpleName())) {
            return true;
        }
        if (javaClass.getName().equals(Collection.class.getSimpleName())) {
            return true;
        }
        if (javaClass.getName().equals(Set.class.getSimpleName())) {
            return true;
        }
        return false;
    }

    private String calcType(JavaClass mainClass, JavaMethod getterMethod,
            JavaMethod setterMethod, JavaField beanField, String shortName) {
        if (getterMethod != null) {
            return calcTypeOfGetterMethodReturn(getterMethod);
        }
        if (beanField != null) {
            Type type = beanField.getType();
            return calcType(type);
        }
        // TODO: calc type based on the setterMethod
        return null;
    }

    private String calcTypeOfGetterMethodReturn(JavaMethod getterMethod) {
        Type type = getterMethod.getReturnType();
        return calcType(type);
    }

    private String calcTypeOfSetterMethodFirstParam(JavaMethod setterMethod) {
        JavaParameter param = setterMethod.getParameters()[0];
        return calcType(param);
    }

    private String calcType(JavaParameter parameter) {
        return calcType(parameter.getType());
    }

    private String calcType(Type type) {
        if (type == null) {
            return "void";
        }
        if (isCollection(type.getJavaClass())) {
            return calcType(calcRealJavaClass(type)) + "List";
        }
        return calcType(calcRealJavaClass(type));
    }

    private String calcType(JavaClass javaClass) {
        if (javaClass.isEnum()) {
            if (javaClass.getName().equals("ErrorLevel")) {
                return "Integer";
            }
            if (javaClass.getName().equals("StatementOperatorTypeKey")) {
                return "String";
            }
            if (javaClass.getName().equals("WriteAccess")) {
                return "String";
            }
            if (javaClass.getName().equals("Widget")) {
                return "String";
            }
            if (javaClass.getName().equals("DataType")) {
                return "String";
            }
            if (javaClass.getName().equals("SortDirection")) {
                return "String";
            }
            if (javaClass.getName().equals("Usage")) {
                return "String";
            }
        }
        // this is messed up instead of list of strings it is an object with a list of strings
        if (javaClass.getName().equals(LOCALE_KEY_LIST)) {
            return "StringList";
        }
        if (javaClass.getName().equals(MESSAGE_GROUP_KEY_LIST)) {
            return "StringList";
        }
        // TODO: figure out why rice stuff translates like this junk?
        if (javaClass.getName().equals("java$util$Map")) {
            return "Map<String, String>";
        }
        if (javaClass.getName().equals("Map")) {
            // TODO: make sure it is in fact a String,String map
            return "Map<String, String>";
        }
        return javaClass.getName();
    }

    private JavaClass calcRealJavaClassOfGetterReturn(JavaMethod getterMethod) {
        if (getterMethod == null) {
            return null;
        }
        Type type = getterMethod.getReturnType();
        return this.calcRealJavaClass(type);
    }

    private JavaClass calcRealJavaClassOfSetterFirstParam(JavaMethod setterMethod) {
        JavaParameter param = setterMethod.getParameters()[0];
        return this.calcRealJavaClass(param);
    }

    private JavaClass calcRealJavaClass(JavaParameter param) {
        Type type = param.getType();
        return calcRealJavaClass(type);
    }

    private JavaClass calcRealJavaClass(Type type) {
        if (type == null) {
            return null;
        }
        JavaClass javaClass = type.getJavaClass();
        if (javaClass.getName().equals(LOCALE_KEY_LIST)) {
            return STRING_JAVA_CLASS;
        }
        if (javaClass.getName().equals(MESSAGE_GROUP_KEY_LIST)) {
            return STRING_JAVA_CLASS;
        }
        if (!this.isCollection(javaClass)) {
            return javaClass;
        }

//  for (Type t : type.getActualTypeArguments ())
//  {
//   System.out.println ("ServiceContractModelQDoxLoader: type arguments = "
//                       + t.toString ());
//  }

        Type t = type.getActualTypeArguments()[0];
        return t.getJavaClass();
    }

    private boolean isComplex(JavaClass javaClass) {
        if (javaClass.isEnum()) {
            return false;
        }
        if (javaClass.getName().equals(String.class.getSimpleName())) {
            return false;
        }
        if (javaClass.getName().equals(Integer.class.getSimpleName())) {
            return false;
        }
        if (javaClass.getName().equals(Date.class.getSimpleName())) {
            return false;
        }
        if (javaClass.getName().equals(Long.class.getSimpleName())) {
            return false;
        }
        if (javaClass.getName().equals(Boolean.class.getSimpleName())) {
            return false;
        }
        if (javaClass.getName().equals(Double.class.getSimpleName())) {
            return false;
        }
        if (javaClass.getName().equals(Float.class.getSimpleName())) {
            return false;
        }
        if (javaClass.getName().equals(int.class.getSimpleName())) {
            return false;
        }
        if (javaClass.getName().equals(long.class.getSimpleName())) {
            return false;
        }
        if (javaClass.getName().equals(boolean.class.getSimpleName())) {
            return false;
        }
        if (javaClass.getName().equals(double.class.getSimpleName())) {
            return false;
        }
        if (javaClass.getName().equals(float.class.getSimpleName())) {
            return false;
        }
        if (javaClass.getName().equals(Map.class.getSimpleName())) {
            return false;
        }

        if (javaClass.getName().equals(String.class.getName())) {
            return false;
        }
        if (javaClass.getName().equals(Integer.class.getName())) {
            return false;
        }
        if (javaClass.getName().equals(Date.class.getName())) {
            return false;
        }
        if (javaClass.getName().equals(Long.class.getName())) {
            return false;
        }
        if (javaClass.getName().equals(Boolean.class.getName())) {
            return false;
        }
        if (javaClass.getName().equals(Double.class.getName())) {
            return false;
        }
        if (javaClass.getName().equals(Float.class.getName())) {
            return false;
        }
        if (javaClass.getName().equals(int.class.getName())) {
            return false;
        }
        if (javaClass.getName().equals(long.class.getName())) {
            return false;
        }
        if (javaClass.getName().equals(boolean.class.getName())) {
            return false;
        }
        if (javaClass.getName().equals(double.class.getName())) {
            return false;
        }
        if (javaClass.getName().equals(float.class.getName())) {
            return false;
        }
        if (javaClass.getName().equals(Map.class.getName())) {
            return false;
        }
        if (javaClass.getName().equals(LOCALE_KEY_LIST)) {
            return false;
        }
        if (javaClass.getName().equals(MESSAGE_GROUP_KEY_LIST)) {
            return false;
        }
        if (javaClass.getName().equals("java$util$Map")) {
            return false;
        }
        return true;
    }
}
