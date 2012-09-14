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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateFactory;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.web.form.LookupForm;
import org.kuali.student.contract.model.Service;

import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.ServiceMethod;
import org.kuali.student.contract.model.XmlType;
import org.kuali.student.contract.model.util.ModelFinder;
import org.kuali.student.contract.writer.JavaClassWriter;
import org.kuali.student.contract.writer.service.GetterSetterNameCalculator;

/**
 *
 * @author nwright
 */
public class AdminUiInquirableWriter extends JavaClassWriter {

    private ServiceContractModel model;
    private ModelFinder finder;
    private String directory;
    private String rootPackage;
    private String servKey;
    private Service service;
    private XmlType xmlType;
    private List<ServiceMethod> methods;

    public AdminUiInquirableWriter(ServiceContractModel model,
            String directory,
            String rootPackage,
            String servKey,
            XmlType xmlType,
            List<ServiceMethod> methods) {
        super(directory, calcPackage(servKey, rootPackage, xmlType), calcClassName(servKey, xmlType));
        this.model = model;
        this.finder = new ModelFinder(model);
        this.directory = directory;
        this.rootPackage = rootPackage;
        this.servKey = servKey;
        service = finder.findService(servKey);
        this.xmlType = xmlType;
        this.methods = methods;
    }

    public static String calcPackage(String servKey, String rootPackage, XmlType xmlType) {
        String pack = rootPackage + "." + servKey.toLowerCase();
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
        return pack;
//        return rootPackage;
    }

    public static String calcClassName(String servKey, String xmlTypeName) {
        return GetterSetterNameCalculator.calcInitUpper(xmlTypeName) + "AdminInquirableImpl";
    }

    public static String calcClassName(String servKey, XmlType xmlType) {
        return calcClassName(servKey, xmlType.getName());
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
        indentPrint("public class " + calcClassName(servKey, xmlType));
        println(" extends InquirableImpl");
        importsAdd("org.kuali.rice.krad.inquiry.InquirableImpl");
        openBrace();
        writeLogic();
        closeBrace();

        this.writeJavaClassAndImportsOutToFile();
        this.getOut().close();
    }

    private void writeLogic() {

        String initUpper = GetterSetterNameCalculator.calcInitUpper(servKey);
        String initLower = GetterSetterNameCalculator.calcInitLower(servKey);
        String infoClass = GetterSetterNameCalculator.calcInitUpper(xmlType.getName());
        String serviceClass = GetterSetterNameCalculator.calcInitUpper(service.getName());
        String serviceVar = GetterSetterNameCalculator.calcInitLower(service.getName());
        importsAdd(service.getImplProject() + "." + service.getName());
        importsAdd("org.apache.log4j.Logger");
        indentPrintln("private static final Logger LOG = Logger.getLogger(" + calcClassName(servKey, xmlType) + ".class);");
        indentPrintln("private transient " + serviceClass + " " + serviceVar + ";");
        indentPrintln("private final static String KEY = \"key\";");

        println("");
        indentPrintln("@Override");
        importsAdd(xmlType.getJavaPackage() + "." + infoClass);
        importsAdd(List.class.getName());
        importsAdd(Map.class.getName());
//        importsAdd(LookupForm.class.getName());
//        importsAdd(QueryByCriteria.class.getName());
//        importsAdd(Predicate.class.getName());
//        importsAdd(PredicateFactory.class.getName());
        importsAdd(GlobalResourceLoader.class.getName());
        XmlType contextInfo = finder.findXmlType("contextInfo");
        importsAdd(contextInfo.getJavaPackage() + "." + contextInfo.getName());
        importsAdd("org.kuali.student.enrollment.common.util.ContextBuilder");
//        importsAdd(PredicateFactory.class.getName());
        importsAdd(ArrayList.class.getName());
        importsAdd(QName.class.getName());
//        importsAdd (PredicateFactory.class.getName());
        indentPrintln("public " + infoClass + " retrieveDataObject(Map<String, String> parameters)");
        openBrace();
        indentPrintln("String key = parameters.get(KEY);");
        indentPrintln("try");
        openBrace();
        String getMethod = calcGetMethod();
        if (getMethod == null) {
            indentPrintln("// WARNING: Missing get method please add it to the service contract: " + servKey + "." + xmlType.getName());
            getMethod = "get" + GetterSetterNameCalculator.calcInitUpper(xmlType.getName());
        }
        indentPrintln("" + infoClass + " info = this.get" + serviceClass + "()." + getMethod + "(key, getContextInfo());");
        indentPrintln("return info;");
        closeBrace();
        indentPrintln("catch (Exception ex) {");
        indentPrintln("    throw new RuntimeException(ex);");
        indentPrintln("}");
        closeBrace();

        indentPrintln("public void set" + serviceClass + "(" + serviceClass + " " + serviceVar + ")");
        openBrace();
        indentPrintln("    this." + serviceVar + " = " + serviceVar + ";");
        closeBrace();
        println("");
        indentPrintln("public " + serviceClass + " get" + serviceClass + "()");
        openBrace();
        indentPrintln("if (" + serviceVar + " == null)");
        openBrace();
        indentPrintln("QName qname = new QName(" + serviceClass + "Constants.NAMESPACE," + serviceClass + "Constants.SERVICE_NAME_LOCAL_PART);");
        indentPrintln(serviceVar + " = (" + serviceClass + ") GlobalResourceLoader.getService(qname);");
        closeBrace();
        indentPrintln("return this." + serviceVar + ";");
        closeBrace();
        println("");
        indentPrintln("private ContextInfo getContextInfo() {");
        indentPrintln("    return ContextBuilder.loadContextInfo();");
        indentPrintln("}");
    }

    private String calcGetMethod() {
        ServiceMethod method = this.findGetMethod();
        if (method != null) {
            return method.getName();
        }
        return null;
    }

    private ServiceMethod findGetMethod() {
        String infoClass = GetterSetterNameCalculator.calcInitUpper(xmlType.getName());
        for (ServiceMethod method : methods) {
            if (method.getName().startsWith("get")) {
                if (method.getReturnValue().getType().equalsIgnoreCase(infoClass)) {
                    if (method.getParameters().size() == 2) {
                        if (method.getParameters().get(0).getType().equalsIgnoreCase("String")) {
                            return method;
                        }
                    }
                }
            }
        }
        return null;
    }
}
