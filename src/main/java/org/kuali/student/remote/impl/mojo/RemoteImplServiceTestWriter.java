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
package org.kuali.student.remote.impl.mojo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateFactory;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.student.admin.ui.mojo.AdminUiLookupableWriter;
import org.kuali.student.contract.model.MessageStructure;
import org.kuali.student.contract.model.Service;

import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.ServiceMethod;
import org.kuali.student.contract.model.ServiceMethodError;
import org.kuali.student.contract.model.XmlType;
import org.kuali.student.contract.model.util.ModelFinder;
import org.kuali.student.contract.writer.JavaClassWriter;
import org.kuali.student.contract.writer.service.GetterSetterNameCalculator;
import org.kuali.student.contract.writer.service.MessageStructureTypeCalculator;
import org.kuali.student.contract.writer.service.ServiceExceptionWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nwright
 */
public class RemoteImplServiceTestWriter extends JavaClassWriter {

    private static final Logger log = LoggerFactory.getLogger(RemoteImplServiceTestWriter.class);
    private ServiceContractModel model;
    private ModelFinder finder;
    private String directory;
    private String rootPackage;
    private String servKey;
    private List<ServiceMethod> methods;
    private Service service;

    public RemoteImplServiceTestWriter(ServiceContractModel model,
            String directory,
            String rootPackage,
            String servKey,
            List<ServiceMethod> methods) {
        super(directory + "/test/java", calcPackage(servKey, rootPackage), calcClassName(servKey));
        this.model = model;
        this.finder = new ModelFinder(model);
        this.directory = directory;
        this.rootPackage = rootPackage;
        this.servKey = servKey;
        this.service = finder.findService(servKey);
        this.methods = methods;
    }

    public static String calcPackage(String servKey, String rootPackage) {
//        String pack = rootPackage + "." + servKey.toLowerCase() + ".";
//  StringBuffer buf = new StringBuffer (service.getVersion ().length ());
//  for (int i = 0; i < service.getVersion ().length (); i ++)
//  {
//   char c = service.getVersion ().charAt (i);
//   c = Character.toLowerCase (c);
//   if (Character.isLetter (c))
//   {
//    buf.append (c);
//    continue;
//   }
//   if (Character.isDigit (c))
//   {
//    buf.append (c);
//   }
//  }
//  pack = pack + buf.toString ();
//        pack = pack + "service.decorators";
//        return pack;
        return rootPackage;
    }

    public static String calcClassName(String servKey) {
        return GetterSetterNameCalculator.calcInitUpper(servKey + "ServiceRemoteImplTest");
    }

    public static String calcDecoratorClassName(String servKey) {
        return GetterSetterNameCalculator.calcInitUpper(servKey + "ServiceDecorator");
    }

    private static enum MethodType {

        VALIDATE, CREATE, UPDATE
    };

    private MethodType calcMethodType(ServiceMethod method) {
        if (method.getName().startsWith("validate")) {
            return MethodType.VALIDATE;
        }
        if (method.getName().startsWith("create")) {
            return MethodType.CREATE;
        }
        if (method.getName().startsWith("update")) {
            return MethodType.UPDATE;
        }
        return null;
    }

    /**
     * Write out the entire file
     *
     * @param out
     */
    public void write() {
        String serviceName = service.getName();
        indentPrintln("//@Ignore");
        indentPrintln("public class " + calcClassName(servKey));
        // TODO: figure out how to add import for the decorator
        openBrace();
        importsAdd("org.junit.*");
        XmlType contextInfo = finder.findXmlType("contextInfo");
        importsAdd(contextInfo.getJavaPackage() + "." + contextInfo.getName());
        indentPrintln("private static ContextInfo contextInfo;");

        indentPrintln("private static " + serviceName + "RemoteImpl service;");
        indentPrintln("");
        indentPrintln("");
        indentPrintln("@BeforeClass");
        indentPrintln("public static void setUpClass() throws Exception");
        openBrace();
        indentPrintln("service = new " + serviceName + "RemoteImpl();");
        indentPrintln("service.setHostUrl(RemoteServiceConstants.ENV2_URL);");
        indentPrintln("//service.setHostUrl(RemoteServiceConstants.LOCAL_HOST_EMBEDDED_URL);");
        indentPrintln("contextInfo = new ContextInfo();");
        indentPrintln("contextInfo.setPrincipalId(\"TESTUSER\");");
        closeBrace();
        indentPrintln("");
        indentPrintln("@AfterClass");
        indentPrintln("public static void tearDownClass() throws Exception");
        openBrace();
        closeBrace();
        indentPrintln("");
        indentPrintln("@Before");
        indentPrintln("public void setUp()");
        openBrace();
        closeBrace();
        indentPrintln("");
        indentPrintln("@After");
        indentPrintln("public void tearDown()");
        openBrace();
        closeBrace();
        indentPrintln("");


        Set<XmlType> types = this.getMainXmlTypesUsedByService(methods);
        if (types.isEmpty()) {
            log.warn("No types defined for servKey: " + servKey);
            return;
        }
        // the main servKey
        log.info("Generating search by criteria tests for " + types.size() + " in " + servKey);
        for (XmlType type : types) {
            this.writeTestMethodsForXmlType(type);
        }
        closeBrace();

        this.writeJavaClassAndImportsOutToFile();
        this.getOut().close();
    }

    private void writeTestMethodsForXmlType(XmlType xmlType) {
        ServiceMethod searchMethod = AdminUiLookupableWriter.findSearchMethod(xmlType, methods);
        String infoClass = GetterSetterNameCalculator.calcInitUpper(xmlType.getName());
        importsAdd(xmlType.getJavaPackage() + "." + xmlType.getName());
        indentPrintln("");
        indentPrintln("@Test");
        indentPrintln("public void testSearch" + infoClass + "All () throws Exception");
        openBrace();
        importsAdd(QueryByCriteria.class.getName());
        importsAdd(Predicate.class.getName());
        importsAdd(PredicateFactory.class.getName());
        indentPrintln("QueryByCriteria.Builder qBuilder = QueryByCriteria.Builder.create();");
        indentPrintln("qBuilder.setMaxResults (30);");
        importsAdd(List.class.getName());
        indentPrintln("List<" + infoClass + "> infos = service." + searchMethod.getName() + "(qBuilder.build(), contextInfo);");
        closeBrace();


        indentPrintln("");
        indentPrintln("@Test");
        indentPrintln("public void testSearch" + infoClass + "KeywordSearch () throws Exception");
        openBrace();
        importsAdd(QueryByCriteria.class.getName());
        importsAdd(Predicate.class.getName());
        importsAdd(PredicateFactory.class.getName());
        indentPrintln("QueryByCriteria.Builder qBuilder = QueryByCriteria.Builder.create();");
        importsAdd (ArrayList.class.getName());
        indentPrintln("List<Predicate> pList = new ArrayList<Predicate>();");
        indentPrintln("pList.add(PredicateFactory.equal(\"keywordSearch\", \"xyzzysomethingnothingmatches\"));");
        indentPrintln("qBuilder.setPredicates(PredicateFactory.and(pList.toArray(new Predicate[pList.size()])));");
        indentPrintln("qBuilder.setMaxResults (30);");
        importsAdd(List.class.getName());
        indentPrintln("List<" + infoClass + "> infos = service." + searchMethod.getName() + "(qBuilder.build(), contextInfo);");
        closeBrace();

        this.writeFieldTests(infoClass, searchMethod, xmlType, new Stack<XmlType>(), "");
    }

    private void writeFieldTests(String infoClass, ServiceMethod searchMethod, XmlType type, Stack<XmlType> parents, String prefix) {
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
            String fieldNameCamel = GetterSetterNameCalculator.dot2Camel(fieldName);
            if (ms.getShortName().equalsIgnoreCase("versionInd")) {
                indentPrintln("// TODO: deal with seaching on the version indicator which is a string in the contract but a number in the database");
                continue;
            }
            if (ms.getType().equalsIgnoreCase("AttributeInfoList")) {
                indentPrintln("// TODO: deal with dynamic attributes");
                continue;
            }
            if (ms.getType().endsWith("List")) {
                indentPrintln("// TODO: deal with  " + fieldName + " which is a List");
                continue;
            }
            XmlType fieldType = finder.findXmlType(ms.getType());
            if (fieldType.getPrimitive().equalsIgnoreCase(XmlType.COMPLEX)) {
                // complex sub-types such as rich text 
                this.writeFieldTests(infoClass, searchMethod, fieldType, parents, fieldName);
                continue;
            }
            if (!ms.getType().equalsIgnoreCase("String")) {
                indentPrintln("// TODO: deal with  " + fieldName + " which is a " + ms.getType());
                continue;
            }

            indentPrintln("");
            indentPrintln("@Test");
            indentPrintln("public void testSearch" + infoClass + "By" + fieldNameCamel + " () throws Exception");
            openBrace();
            indentPrintln("QueryByCriteria.Builder qBuilder = QueryByCriteria.Builder.create();");
            indentPrintln("List<Predicate> pList = new ArrayList<Predicate>();");
            indentPrintln("pList.add(PredicateFactory.equal(\"" + fieldName + "\", \"xyzzysomethingnothingmatches\"));");
            indentPrintln("qBuilder.setPredicates(PredicateFactory.and(pList.toArray(new Predicate[pList.size()])));");
            indentPrintln("qBuilder.setMaxResults (30);");
            indentPrintln("List<" + infoClass + "> infos = service." + searchMethod.getName() + "(qBuilder.build(), contextInfo);");
            closeBrace();

        }
        parents.pop();
    }

    private String calcType(String type, String realType) {
        XmlType t = finder.findXmlType(this.stripList(type));
        return MessageStructureTypeCalculator.calculate(this, model, type, realType,
                t.getJavaPackage());
    }

    private String stripList(String str) {
        return GetterSetterNameCalculator.stripList(str);
    }

    private String calcExceptionClassName(ServiceMethodError error) {
        if (error.getClassName() == null) {
            return ServiceExceptionWriter.calcClassName(error.getType());
        }
        return error.getClassName();
    }

    private String calcExceptionPackageName(ServiceMethodError error) {
        if (error.getClassName() == null) {
            return ServiceExceptionWriter.calcPackage(rootPackage);
        }
        return error.getPackageName();
    }

    private Set<XmlType> getMainXmlTypesUsedByService(List<ServiceMethod> methods) {
        Set<XmlType> set = new HashSet();
        for (ServiceMethod method : methods) {
            if (method.getReturnValue().getType().endsWith("List")) {
                continue;
            }
            XmlType returnType = finder.findXmlType(method.getReturnValue().getType());
            if (returnType == null) {
                continue;
            }
            if (!returnType.getPrimitive().equalsIgnoreCase(XmlType.COMPLEX)) {
                continue;
            }
            // TYPE only should show up on type service
            if (returnType.getName().equalsIgnoreCase("TypeInfo")) {
                if (!servKey.equalsIgnoreCase("type")) {
                    continue;
                }
            }
            // State only should show up on type service
            if (returnType.getName().equalsIgnoreCase("StateInfo")) {
                if (!servKey.equalsIgnoreCase("state")) {
                    continue;
                }
            }
//            if (method.getName().startsWith("create")) {
//                set.add(returnType);
//                continue;
//            }
//            if (method.getName().startsWith("update")) {
//                set.add(returnType);
//                continue;
//            }
            if (method.getName().startsWith("get")) {
                if (method.getParameters().size() == 2) {
                    if (method.getParameters().get(0).getType().equalsIgnoreCase("String")) {
                        set.add(returnType);
                        continue;
                    }
                }
            }
        }
        return set;
    }

    private List<MessageStructure> getFieldsToSearchOn(XmlType xmlType) {
        List<MessageStructure> list = new ArrayList<MessageStructure>();
        for (MessageStructure ms : finder.findMessageStructures(xmlType.getName())) {
            XmlType msType = finder.findXmlType(ms.getType());
            // TODO: dive down into complex sub-types 
            if (msType.getPrimitive().equalsIgnoreCase(XmlType.COMPLEX)) {
                continue;
            }
            list.add(ms);
        }
        return list;
    }
}
