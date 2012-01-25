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
package org.kuali.student.contract.model.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.kuali.student.contract.model.MessageStructure;
import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.ServiceMethod;
import org.kuali.student.contract.model.ServiceMethodParameter;
import org.kuali.student.contract.model.XmlType;
import org.kuali.student.contract.model.util.HtmlContractMessageStructureWriter;
import org.kuali.student.contract.model.util.ModelFinder;
import org.kuali.student.contract.model.validation.ServiceContractModelValidator;

/**
 *
 * @author nwright
 */
//@Ignore
public class R1R2ServiceContractComparisonTest {

    public R1R2ServiceContractComparisonTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        getModel1();
        getModel2();
        getFinder1();
        getFinder2();
        loadKnownObjectRenames();
        loadKnownUnconvertedObjects();
        loadKnownFieldRenames();
    }

    @After
    public void tearDown() {
    }
    private static final String RESOURCES_DIRECTORY = "src/test/resources";
    private static final String TEST_SOURCE_DIRECTORY =
            "src/test/java/org/kuali/student/contract/model/test/source";
    private static final String ENROLL_PROJECT_SRC_MAIN = "C:/svn/ks-1.3/ks-enroll/ks-enroll-api/src/main";
    private static final String ENROLL_PROJECT_JAVA_DIRECTORY = ENROLL_PROJECT_SRC_MAIN + "/java";
    private static final String RICE_CORE_API_DIRECTORY = "C:/svn/rice/trunk/core/api/src/main/java";
    private static final String RICE_KIM_API_DIRECTORY = "C:/svn/rice/trunk/kim/kim-api/src/main/java";
    private static final String RICE_LOCATION_API_DIRECTORY = "C:/svn/rice/trunk/location/api/src/main/java";
    private static final String RICE_KEW_API_DIRECTORY = "C:/svn/rice/trunk/kew/api/src/main/java";
    private static final String RICE_KEN_API_DIRECTORY = "C:/svn/rice/trunk/ken/api/src/main/java";
    private static final String RICE_KSB_API_DIRECTORY = "C:/svn/rice/trunk/ksb/api/src/main/java";
    private static final String RICE_KRMS_API_DIRECTORY = "C:/svn/rice/trunk/krms/api/src/main/java";
    private static final String CORE_API_DIRECTORY = "C:/svn/ks-1.3/ks-core/ks-core-api/src/main/java";
    private static final String COMMON_API_DIRECTORY = "C:/svn/ks-1.3/ks-common/ks-common-api/src/main/java";
    private static final String LUM_API_DIRECTORY = "C:/svn/ks-1.3/ks-lum/ks-lum-api/src/main/java";
    private static ServiceContractModel model1 = null;
    private static ServiceContractModel model2 = null;

    /**
     * Test of getServiceMethods method, of class ServiceContractModelQDoxLoader.
     */
    @Test
    public void testCompareModels() {
        System.out.println("compareModels");
        compareTypes();
        System.out.println("===== service method comparison ====");
        compareMethods();
    }

    private ServiceContractModel getModel1() {
        if (model1 != null) {
            return model1;
        }
        List<String> srcDirs = new ArrayList();
        System.out.println("User directory=" + System.getProperty("user.dir"));
        System.out.println("Current directory=" + new File(".").getAbsolutePath());
        srcDirs.add(COMMON_API_DIRECTORY);
        srcDirs.add(CORE_API_DIRECTORY);
        srcDirs.add(LUM_API_DIRECTORY);
        boolean validateKualiStudent = false;
        ServiceContractModel instance = new ServiceContractModelQDoxLoader(srcDirs, validateKualiStudent);

        instance = new ServiceContractModelCache(instance);
        validate(instance);
        model1 = instance;
        return instance;
    }

    private ServiceContractModel getModel2() {
        if (model2 != null) {
            return model2;
        }
        List<String> srcDirs = new ArrayList();
        System.out.println("User directory=" + System.getProperty("user.dir"));
        System.out.println("Current directory=" + new File(".").getAbsolutePath());
        srcDirs.add(ENROLL_PROJECT_JAVA_DIRECTORY);
        boolean validateKualiStudent = true;
        ServiceContractModel instance = new ServiceContractModelQDoxLoader(srcDirs, validateKualiStudent);

        instance = new ServiceContractModelCache(instance);
        validate(instance);
        model2 = instance;
        return instance;
    }

    private String dump(ServiceMethod method) {
        StringBuilder bldr = new StringBuilder();
        bldr.append(method.getName());
        String comma = "";
        bldr.append("(");
        for (ServiceMethodParameter param : method.getParameters()) {
            bldr.append(comma);
            comma = ", ";
            bldr.append(param.getType());
            bldr.append(" ");
            bldr.append(param.getName());
        }
        bldr.append(")");
        return bldr.toString();
    }

    private void validate(ServiceContractModel model) {
        Collection<String> errors =
                new ServiceContractModelValidator(model).validate();
        if (errors.size() > 0) {
            StringBuilder buf = new StringBuilder();
            buf.append(errors.size()).append(" errors found while validating the data.");
            int cnt = 0;
            for (String msg : errors) {
                cnt++;
                buf.append("\n");
                buf.append("*error*").append(cnt).append(":").append(msg);
            }

            fail(buf.toString());
        }
    }
    private ModelFinder finder1 = null;

    private ModelFinder getFinder1() {
        if (finder1 == null) {
            finder1 = new ModelFinder(getModel1());
        }
        return finder1;
    }
    private ModelFinder finder2 = null;

    private ModelFinder getFinder2() {
        if (finder2 == null) {
            finder2 = new ModelFinder(getModel2());
        }
        return finder2;
    }

    private void compareMethods() {
        for (ServiceMethod method : model1.getServiceMethods()) {
            findCompareMethod(method);
        }
    }
            
    private ServiceMethod findMethod(ServiceMethod method1) {
        ServiceMethod method2 = finder2.findServiceMethod(method1.getService(), method1.getName());
        if (method2 == null) {
            if (method1.getService().equals("LuService")) {
                method2 = finder2.findServiceMethod("CluService", method1.getName());
                if (method2 == null) {
                    method2 = finder2.findServiceMethod("LuiService", method1.getName());
                }
            }
        }
        return method2;
    }

    private String calcMethods(ServiceMethod method1) {
        StringBuilder bldr = new StringBuilder();
        String comma = "";
        for (ServiceMethod method2 : finder2.findServiceMethods(method1.getService())) {
            bldr.append(comma);
            comma = ", ";
            bldr.append(method2.getName());
        }
        return bldr.toString();
    }

    private String calcPossibleMethods(ServiceMethod method1) {
        StringBuilder bldr = new StringBuilder();
        String comma = "";
        for (ServiceMethod method2 : findPossibleMethods(method1)) {
            bldr.append(comma);
            comma = ", ";
            bldr.append(method2.getName());
        }
        return bldr.toString();
    }

    private List<ServiceMethod> findPossibleMethods(ServiceMethod method1) {
        List<ServiceMethod> methods = new ArrayList<ServiceMethod>();
        for (ServiceMethod method2 : finder2.findServiceMethods(method1.getService())) {
            if (isPossibleMatch(method1, method2)) {
                methods.add(method2);
            }
        }
        return methods;
    }

    private boolean isPossibleMatch(ServiceMethod method1, ServiceMethod method2) {
        if (!method1.getService().equalsIgnoreCase(method2.getService())) {
            return false;
        }
        if (method1.getName().contains(method2.getName())) {
            return true;
        }
        if (method2.getName().contains(method1.getName())) {
            return true;
        }
        if (method1.getName().substring(0, 5).equals(method2.getName().substring(0, 5))) {
            return true;
        }
        return false;
    }

    private void findCompareMethod(ServiceMethod method1) {
        ServiceMethod method2 = findMethod(method1);
        if (method2 == null) {
//            String possibleMethods = calcPossibleMethods(method1);
            if (isTypeMethod(method1)) {
                System.out.println("# (*g) " + method1.getService() + "." + method1.getName() + " has no corresponding method in r2 -- is a type method use TypeService instead");
                return;
            }
            System.out.println("# " + method1.getService() + "." + method1.getName() + " has no corresponding method in r2: " + this.calcPossibleMethods(method1));
            return;
        }
    }

    private boolean isTypeMethod(ServiceMethod method1) {
        if (method1.getReturnValue().getType().endsWith("TypeInfo")) {
            return true;
        }
        if (method1.getReturnValue().getType().endsWith("TypeInfoList")) {
            return true;
        }
        return false;
    }

    private void compareTypes() {
        for (XmlType type : model1.getXmlTypes()) {
            findCompareType(type);
        }
    }

    private String calcService(XmlType xmlType) {
        StringBuilder bldr = new StringBuilder();
        String comma = "";
        for (String serviceKey : HtmlContractMessageStructureWriter.calcUsageByService(model1, xmlType)) {
            bldr.append(comma);
            comma = ", ";
            bldr.append(serviceKey);
        }
        return bldr.toString();
    }

    private String calcFieldNames(XmlType xmlType) {
        StringBuilder bldr = new StringBuilder();
        String comma = "";
        for (MessageStructure ms : finder2.findMessageStructures(xmlType.getName())) {
            bldr.append(comma);
            comma = ", ";
            bldr.append(ms.getShortName());
        }
        return bldr.toString();
    }
    private Set<String> knownUnconvertedObjects = null;

    private void loadKnownUnconvertedObjects() {
        Set<String> missings = new HashSet<String>();
        missings.add("ObjectStructureDefinition");
        missings.add("FieldDefinition");
        missings.add("ValidCharsConstraint");
        missings.add("RequiredConstraint");
        missings.add("CaseConstraint");
        missings.add("WhenConstraint");
        missings.add("Constraint");
        missings.add("MustOccurConstraint");
        missings.add("LookupConstraint");
        missings.add("CommonLookupParam");
        missings.add("CommonLookup");
        missings.add("DateRangeInfo");
        missings.add("CredentialInfo");
        missings.add("CreditInfo");
        missings.add("ScaleInfo");
        missings.add("GradeInfo");
        missings.add("ResultComponentInfo");
        knownUnconvertedObjects = missings;
        return;
    }
    private Map<String, String> knownObjectRenames = null;

    private void loadKnownObjectRenames() {
        Map<String, String> renames = new HashMap<String, String>();
        renames.put("Message", "MessageInfo");
        knownObjectRenames = renames;
        return;
    }
    private Map<String, String> knownFieldRenames = null;

    private void loadKnownFieldRenames() {
        Map<String, String> renames = new HashMap<String, String>();
        renames.put("desc", "descr");
        renames.put("state", "stateKey");
        renames.put("type", "typeKey");
        renames.put("metaInfo", "meta");
        renames.put("desc", "descr");
        renames.put("startTerm", "startTermId");
        renames.put("endTerm", "endTermId");
        renames.put("longDesc", "longDescr");
        renames.put("shortDesc", "shortDescr");
        knownFieldRenames = renames;
        return;
    }
    
    private XmlType findType(XmlType r1) {
        XmlType r2 = finder2.findXmlType(r1.getName());
        if (r2 == null) {
            String renamedName = this.knownObjectRenames.get(r1.getName());
            if (renamedName != null) {
                r2 = finder2.findXmlType(renamedName);
            }
        }
        if (r2 == null) {
            if (r1.getName().endsWith("TypeInfo")) {
                r2 = finder2.findXmlType("TypeInfo");
            }
        }
        return r2;
    }

    private void findCompareType(XmlType r1) {
        if (r1.getName().endsWith("List")) {
            return;
        }
        if (this.knownUnconvertedObjects.contains(r1.getName())) {
            return;
        }
        XmlType r2 = findType(r1);
        if (r2 == null) {
            System.out.println("# " + calcService(r1) + " Service:" + r1.getName() + " has no corresponding object in r2");
            return;
        }
        for (MessageStructure ms : finder1.findMessageStructures(r1.getName())) {
            findCompareMessageStructure(ms, r2);
        }
    }

    private void findCompareMessageStructure(MessageStructure r1, XmlType xmlType2) {
        MessageStructure r2 = findMessageStructure(r1, xmlType2);
        if (r2 == null) {
            System.out.println("# " + r1.getXmlObject() + "." + r1.getShortName() + " not found in r2: renamed to one of these? " + calcFieldNames(xmlType2));
            return;
        }
        compareType(r1, r2);
    }

    private void compareType(MessageStructure r1, MessageStructure r2) {
        if (r1.getType().equalsIgnoreCase(r2.getType())) {
            return;
        }
        if (r1.getShortName().equals("attributes")) {
            if (r1.getType().equals("Map<String, String>")) {
                if (r2.getType().equals("AttributeInfoList")) {
                    return;
                }
            }
        }
        if (r1.getShortName().equals("desc") || r1.getShortName().equals("descr")) {
            if (r1.getType().equals("String")) {
                if (r2.getType().equals("RichTextInfo")) {
                    return;
                }
            }
        }
        System.out.println("# " + r1.getXmlObject() + "." + r1.getShortName() + ": the type was changed from " + r1.getType() + " to " + r2.getType());
    }

    private MessageStructure findMessageStructure(MessageStructure r1, XmlType xmlType2) {
        MessageStructure r2 = finder2.findMessageStructure(xmlType2.getName(), r1.getShortName());
        if (r2 == null) {
            String renamed = this.knownFieldRenames.get(r1.getShortName());
            if (renamed != null) {
                r2 = finder2.findMessageStructure(xmlType2.getName(), renamed);
                if (r2 == null) {
                    System.out.println("# " + r1.getXmlObject() + "." + r1.getShortName() + " was renamed to " + xmlType2.getName() + "." + renamed + " BUT IT STILL DIDN'T EXIST IN R2");
                    return null;
                }
            }
        }
        return r2;
    }
}
