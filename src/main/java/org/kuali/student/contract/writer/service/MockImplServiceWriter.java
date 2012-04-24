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
package org.kuali.student.contract.writer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;
import org.kuali.student.contract.model.Service;
import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.ServiceMethod;
import org.kuali.student.contract.model.ServiceMethodError;
import org.kuali.student.contract.model.ServiceMethodParameter;
import org.kuali.student.contract.model.XmlType;
import org.kuali.student.contract.model.util.ModelFinder;
import org.kuali.student.contract.writer.JavaClassWriter;

/**
 *
 * @author nwright
 */
public class MockImplServiceWriter extends JavaClassWriter {

    private ServiceContractModel model;
    private ModelFinder finder;
    private String directory;
    private String rootPackage;
    private String servKey;
    private List<ServiceMethod> methods;

    public MockImplServiceWriter(ServiceContractModel model,
            String directory,
            String rootPackage,
            String servKey,
            List<ServiceMethod> methods) {
        super(directory, calcPackage(servKey, rootPackage), calcClassName(servKey));
        this.model = model;
        this.finder = new ModelFinder(model);
        this.directory = directory;
        this.rootPackage = rootPackage;
        this.servKey = servKey;
        this.methods = methods;
    }

    public static String calcPackage(String servKey, String rootPackage) {
        String pack = rootPackage + "." + servKey.toLowerCase() + ".";
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
        pack = pack + "service.impl";
        return pack;
    }

    public static String calcClassName(String servKey) {
        return GetterSetterNameCalculator.calcInitUpper(servKey + "ServiceMockImpl");
    }

    public static String calcServiceInterfaceClassName(String servKey) {
        return GetterSetterNameCalculator.calcInitUpper(servKey + "Service");
    }

    private static enum MethodType {

        VALIDATE,
        CREATE,
        CREATE_BULK,
        UPDATE,
        UPDATE_OTHER,
        DELETE,
        DELETE_OTHER,
        GET_BY_ID,
        GET_BY_IDS,
        GET_IDS_BY_TYPE,
        GET_IDS_BY_OTHER,
        GET_INFOS_BY_OTHER,
        GET_TYPE,
        GET_TYPES,
        UNKNOWN
    };

    private MethodType calcMethodType(ServiceMethod method) {
        if (method.getName().startsWith("validate")) {
            return MethodType.VALIDATE;
        }
        if (method.getName().startsWith("create")) {
            if (this.findInfoParameter(method) != null) {
                return MethodType.CREATE;
            }
            return MethodType.CREATE_BULK;
        }
        if (method.getName().startsWith("update")) {
            if (this.findInfoParameter(method) != null) {
                return MethodType.UPDATE;
            }
            return MethodType.UPDATE_OTHER;
        }
        if (method.getName().startsWith("delete")) {
            if (method.getName().contains("By")) {
                if (!method.getName().startsWith("deleteBy")) {
                    return MethodType.DELETE_OTHER;
                }
            }
            if (method.getName().contains("For")) {
                if (!method.getName().startsWith("deleteFor")) {
                    return MethodType.DELETE_OTHER;
                }
            }
            return MethodType.DELETE;
        }
        if (method.getName().startsWith("get")) {
            if (method.getName().endsWith("ByIds")) {
                return MethodType.GET_BY_IDS;
            }
            if (method.getName().endsWith("ByType")) {
                return MethodType.GET_IDS_BY_TYPE;
            }
            if (method.getReturnValue().getType().equals("TypeInfo")) {
                return MethodType.GET_TYPE;
            }
            if (method.getReturnValue().getType().equals("TypeInfoList")) {
                return MethodType.GET_TYPES;
            }
            if (method.getName().endsWith("Type")) {
                return MethodType.GET_IDS_BY_TYPE;
            }
            if (method.getParameters().size() == 2) {
                if (!method.getReturnValue().getType().endsWith("List")) {
                    if (method.getParameters().get(0).getName().endsWith("Id")) {
                        return MethodType.GET_BY_ID;
                    }
                }
            }
            if (method.getName().contains("By")) {
                if (method.getReturnValue().getType().equals("StringList")) {
                    return MethodType.GET_IDS_BY_OTHER;
                }
                if (method.getReturnValue().getType().endsWith("InfoList")) {
                    return MethodType.GET_INFOS_BY_OTHER;
                }
            }
        }

        return MethodType.UNKNOWN;
    }

    /**
     * Write out the entire file
     * @param out
     */
    public void write() {
        indentPrint("public class " + calcClassName(servKey));
        println(" implements " + calcServiceInterfaceClassName(servKey));
        Service serv = finder.findService(servKey);
        importsAdd(serv.getImplProject() + "." + serv.getName());
        openBrace();
        for (ServiceMethod method : methods) {
            MethodType methodType = calcMethodType(method);
            if (methodType == MethodType.CREATE) {
                writeCacheVariable(method);
            }
            indentPrintln("");
//            indentPrintln("/**");
//            indentPrintWrappedComment(method.getDescription());
//            indentPrintln("* ");
//            for (ServiceMethodParameter param : method.getParameters()) {
//                indentPrintWrappedComment("@param " + param.getName() + " - "
//                        + param.getType() + " - "
//                        + param.getDescription());
//            }
//            indentPrintWrappedComment("@return " + method.getReturnValue().
//                    getDescription());
//            indentPrintln("*/");
            indentPrintln("@Override");
            String type = method.getReturnValue().getType();
            String realType = stripList(type);
            indentPrint("public " + calcType(type, realType) + " " + method.getName()
                    + "(");
            // now do parameters
            String comma = "";
            for (ServiceMethodParameter param : method.getParameters()) {
                type = param.getType();
                realType = stripList(type);
                print(comma);
                print(calcType(type, realType));
                print(" ");
                print(param.getName());
                comma = ", ";
            }
            println(")");
            // now do exceptions
            comma = "throws ";
            incrementIndent();
            for (ServiceMethodError error : method.getErrors()) {
                indentPrint(comma);
                String exceptionClassName = calcExceptionClassName(error);
                String exceptionPackageName = this.calcExceptionPackageName(error);
                println(exceptionClassName);
                this.importsAdd(exceptionPackageName + "." + exceptionClassName);
                comma = "      ,";
            }
            decrementIndent();
            openBrace();
            switch (methodType) {
                case VALIDATE:
                    writeValidate(method);
                    break;
                case CREATE:
                    writeCreate(method);
                    break;
                case UPDATE:
                    writeUpdate(method);
                    break;
                case DELETE:
                    writeDelete(method);
                    break;
                case GET_BY_ID:
                    writeGetById(method);
                    break;
                case GET_BY_IDS:
                    writeGetByIds(method);
                    break;
                case GET_IDS_BY_OTHER:
                    writeGetIdsByOther(method);
                    break;
                case GET_INFOS_BY_OTHER:
                    writeGetInfosByOther(method);
                    break;
                default:
                    writeThrowsNotImplemented(method);
            }
            closeBrace();
        }

        writeBoilerPlate();
        closeBrace();

        this.writeJavaClassAndImportsOutToFile();
        this.getOut().close();
    }

    private void writeThrowsNotImplemented(ServiceMethod method) {
        indentPrintln("throw new OperationFailedException (\"" + method.getName() + " has not been implemented\");");
    }

    private String initLower(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    private void writeBoilerPlate() {
        importsAdd("org.kuali.student.r2.common.dto.MetaInfo");
        indentPrintln("");
        indentPrintln("private MetaInfo newMeta(ContextInfo context) {");
        indentPrintln("     MetaInfo meta = new MetaInfo();");
        indentPrintln("     meta.setCreateId(context.getPrincipalId());");
        importsAdd(Date.class.getName());
        indentPrintln("     meta.setCreateTime(new Date());");
        indentPrintln("     meta.setUpdateId(context.getPrincipalId());");
        indentPrintln("     meta.setUpdateTime(meta.getCreateTime());");
        indentPrintln("     meta.setVersionInd(\"0\");");
        indentPrintln("     return meta;");
        indentPrintln("}");
        indentPrintln("");
        indentPrintln("private StatusInfo newStatus() {");
        indentPrintln("     StatusInfo status = new StatusInfo();");
        indentPrintln("     status.setSuccess(Boolean.TRUE);");
        indentPrintln("     return status;");
        indentPrintln("}");
        indentPrintln("");
        indentPrintln("private MetaInfo updateMeta(MetaInfo old, ContextInfo context) {");
        indentPrintln("     MetaInfo meta = new MetaInfo(old);");
        indentPrintln("     meta.setUpdateId(context.getPrincipalId());");
        indentPrintln("     meta.setUpdateTime(new Date());");
        indentPrintln("     meta.setVersionInd((Integer.parseInt(meta.getVersionInd()) + 1) + \"\");");
        indentPrintln("     return meta;");
        indentPrintln("}");
        indentPrintln("");
    }

    private void writeValidate(ServiceMethod method) {
        indentPrintln("// validate");
        indentPrintln("return new ArrayList<ValidationResultInfo> ();");


        this.importsAdd(ArrayList.class.getName());
    }

    private void writeCacheVariable(ServiceMethod method) {
        indentPrintln("// cache variable ");
        String objectName = calcObjectName(method);
        String mapName = calcMapName(method);
        indentPrintln("// The LinkedHashMap is just so the values come back in a predictable order");
        indentPrintln("private Map<String, " + objectName + "Info> " + mapName + " = new LinkedHashMap<String, " + objectName + "Info>();");
        importsAdd(Map.class.getName());
        importsAdd(LinkedHashMap.class.getName());
    }

    private void writeCreate(ServiceMethod method) {
        indentPrintln("// create ");
        ServiceMethodParameter typeParam = this.findTypeParameter(method);
        ServiceMethodParameter infoParam = this.findInfoParameter(method);
        ServiceMethodParameter contextParam = this.findContextParameter(method);
        String objectName = calcObjectName(method);
        String infoName = objectName + "Info";
        String mapName = calcMapName(method);

        if (typeParam != null) {
            indentPrintln("if (!" + typeParam.getName() + ".equals (" + infoParam.getName() + ".getTypeKey())) {");
            indentPrintln("    throw new InvalidParameterException (\"The type parameter does not match the type on the info object\");");
            indentPrintln("}");
        }
        if (method.getParameters().size() > 3) {
            indentPrintln("// TODO: check the rest of the readonly fields that are specified on the create to make sure they match the info object");
        }
        indentPrintln(infoName + " copy = new " + infoName + "(" + infoParam.getName() + ");");
        indentPrintln("if (copy.getId() == null) {");
        indentPrintln("   copy.setId(" + mapName + ".size() + \"\");");
        indentPrintln("}");
        indentPrintln("copy.setMeta(newMeta(" + contextParam.getName() + "));");
        indentPrintln(mapName + ".put(copy.getId(), copy);");
        indentPrintln("return new " + infoName + "(copy);");
    }

    private ServiceMethodParameter findIdParameter(ServiceMethod method) {
        String idFieldName = calcObjectName(method) + "Id";
        for (ServiceMethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals("String")) {
                if (parameter.getName().equals(idFieldName)) {
                    return parameter;
                }
            }
        }
        // can't find name exactly 
        for (ServiceMethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals("String")) {
                if (parameter.getName().endsWith("Id")) {
                    return parameter;
                }
            }
        }
        // can't find name exactly try key 
        for (ServiceMethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals("String")) {
                if (!parameter.getName().endsWith("TypeKey")) {
                    if (parameter.getName().endsWith("Key")) {
                        return parameter;
                    }
                }
            }
        }
        return null;
    }

    private ServiceMethodParameter findContextParameter(ServiceMethod method) {
        for (ServiceMethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals("ContextInfo")) {
                return parameter;
            }
        }
        return null;
    }

    private ServiceMethodParameter findInfoParameter(ServiceMethod method) {
        String objectName = calcObjectName(method) + "Info";
        for (ServiceMethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals(objectName)) {
                return parameter;
            }
        }
        return null;
    }

    private ServiceMethodParameter findTypeParameter(ServiceMethod method) {
        for (ServiceMethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals("String")) {
                if (parameter.getName().endsWith("TypeKey")) {
                    return parameter;
                }
                if (parameter.getName().endsWith("Type")) {
                    return parameter;
                }
            }
        }
        return null;
    }

    private String calcMapName(ServiceMethod method) {
        String mapName = this.calcObjectName(method);
        mapName = this.initLower(mapName) + "Map";
        return mapName;
    }

    private String calcObjectName(ServiceMethod method) {
        if (method.getName().startsWith("create")) {
            return method.getName().substring("create".length());
        }
        if (method.getName().startsWith("update")) {
            return method.getName().substring("update".length());
        }
        if (method.getName().startsWith("validate")) {
            return method.getName().substring("validate".length());
        }
        if (method.getName().startsWith("delete")) {
            return method.getName().substring("delete".length());
        }
        if (method.getName().startsWith("get")) {
            if (method.getReturnValue().getType().equals("StringList")) {
                if (method.getName().contains("IdsBy")) {
                    return method.getName().substring("get".length(),
                            method.getName().indexOf("IdsBy"));
                }
                if (method.getName().contains("IdsFor")) {
                    return method.getName().substring("get".length(),
                            method.getName().indexOf("IdsFor"));
                }
                return method.getName().substring("get".length());
            }
            String name = method.getReturnValue().getType();
            if (name.endsWith("List")) {
                name = name.substring(0, name.length() - "List".length());
            }
            if (name.endsWith("Info")) {
                name = name.substring(0, name.length() - "Info".length());
            }
            return name;
        }
        throw new IllegalArgumentException(method.getName());
    }

    private void writeUpdate(ServiceMethod method) {
        indentPrintln("// update");
        ServiceMethodParameter idParam = this.findIdParameter(method);
        ServiceMethodParameter infoParam = this.findInfoParameter(method);
        ServiceMethodParameter contextParam = this.findContextParameter(method);
        if (infoParam == null) {
            throw new NullPointerException(method.getName());
        }
        String objectName = calcObjectName(method);
        String infoName = objectName + "Info";
        String mapName = calcMapName(method);
        if (idParam != null) {
            indentPrintln("if (!" + idParam.getName() + ".equals (" + infoParam.getName() + ".getId())) {");
            indentPrintln("    throw new InvalidParameterException (\"The id parameter does not match the id on the info object\");");
            indentPrintln("}");
        }
        indentPrintln(infoName + " copy = new " + infoName + "(" + infoParam.getName() + ");");
        indentPrintln(infoName + " old = this.get" + objectName + "(" + infoParam.getName() + ".getId(), " + contextParam.getName() + ");");
        indentPrintln("if (!old.getMeta().getVersionInd().equals(copy.getMeta().getVersionInd())) {");
        indentPrintln("    throw new VersionMismatchException(old.getMeta().getVersionInd());");
        indentPrintln("}");
        indentPrintln("copy.setMeta(updateMeta(copy.getMeta(), " + contextParam.getName() + "));");
        indentPrintln("this." + mapName + " .put(" + infoParam.getName() + ".getId(), copy);");
        indentPrintln("return new " + infoName + "(copy);");

    }

    private void writeDelete(ServiceMethod method) {
        ServiceMethodParameter idParam = this.findIdParameter(method);
        String mapName = calcMapName(method);
        indentPrintln("if (this." + mapName + ".remove(" + idParam.getName() + ") == null) {");
        indentPrintln("   throw new DoesNotExistException(" + idParam.getName() + ");");
        indentPrintln("}");
        indentPrintln("return newStatus();");
    }

    private void writeGetById(ServiceMethod method) {
        ServiceMethodParameter idParam = this.findIdParameter(method);
        String mapName = calcMapName(method);
        indentPrintln("if (!this." + mapName + ".containsKey(" + idParam.getName() + ")) {");
        indentPrintln("   throw new DoesNotExistException(" + idParam.getName() + ");");
        indentPrintln("}");
        indentPrintln("return this." + mapName + ".get (" + idParam.getName() + ");");
    }

    private void writeGetByIds(ServiceMethod method) {
        String objectName = this.calcObjectName(method);
        String infoName = objectName + "Info";
        ServiceMethodParameter idListParam = this.findIdListParameter(method);
        ServiceMethodParameter contextParam = this.findContextParameter(method);
        indentPrintln("List<" + infoName + "> list = new ArrayList<" + infoName + "> ();");
        indentPrintln("for (String id: " + idListParam.getName() + ") {");
        indentPrintln("    list.add (this.get" + objectName + "(id, " + contextParam.getName() + "));");
        indentPrintln("}");
        indentPrintln("return list;");
    }

    private void writeGetIdsByOther(ServiceMethod method) {
        String objectName = this.calcObjectName(method);
        String infoName = objectName + "Info";
        String mapName = calcMapName(method);
        indentPrintln("List<String> list = new ArrayList<String> ();");
        indentPrintln("for (" + infoName + " info: " + mapName + ".values ()) {");
        for (ServiceMethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals("ContextInfo")) {
                continue;
            }
            incrementIndent();
            indentPrintln("if (" + parameter.getName() + ".equals(info.get" + initUpper(parameter.getName()) + "())) {");
        }
        indentPrintln("    list.add (info.getId ());");
        for (ServiceMethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals("ContextInfo")) {
                continue;
            }
            indentPrintln("}");
            decrementIndent();
        }
        indentPrintln("}");
        indentPrintln("return list;");
    }

    private void writeGetInfosByOther(ServiceMethod method) {
        String objectName = this.calcObjectName(method);
        String infoName = objectName + "Info";
        String mapName = calcMapName(method);
        indentPrintln("List<" + infoName + "> list = new ArrayList<" + infoName + "> ();");
        indentPrintln("for (" + infoName + " info: " + mapName + ".values ()) {");
        for (ServiceMethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals("ContextInfo")) {
                continue;
            }
            incrementIndent();
            indentPrintln("if (" + parameter.getName() + ".equals(info.get" + initUpper(parameter.getName()) + "())) {");
        }
        indentPrintln("    list.add (info);");
        for (ServiceMethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals("ContextInfo")) {
                continue;
            }
            indentPrintln("}");
            decrementIndent();
        }
        indentPrintln("}");
        indentPrintln("return list;");
    }

    private String initUpper(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private ServiceMethodParameter findIdListParameter(ServiceMethod method) {
        String idFieldName = calcObjectName(method) + "Ids";
        for (ServiceMethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals("StringList")) {
                if (parameter.getName().equals(idFieldName)) {
                    return parameter;
                }
            }
        }
        // can't find name exactly 
        for (ServiceMethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals("StringList")) {
                if (parameter.getName().endsWith("Ids")) {
                    return parameter;
                }
            }
        }
        // can't find name exactly try key 
        for (ServiceMethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals("StringList")) {
                if (parameter.getName().endsWith("Keys")) {
                    return parameter;
                }
            }
        }
        return null;
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

    private String calcType(String type, String realType) {
        XmlType t = finder.findXmlType(this.stripList(type));
        return MessageStructureTypeCalculator.calculate(this, model, type, realType,
                t.getJavaPackage());
    }
}
