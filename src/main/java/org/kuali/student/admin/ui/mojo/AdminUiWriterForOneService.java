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
package org.kuali.student.admin.ui.mojo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.ServiceMethod;
import org.kuali.student.contract.model.XmlType;
import org.kuali.student.contract.model.util.ModelFinder;

/**
 *
 * @author nwright
 */
public class AdminUiWriterForOneService {

    private ServiceContractModel model;
    private ModelFinder finder;
    private String directory;
    private String rootPackage;
    private String servKey;

    public AdminUiWriterForOneService(ServiceContractModel model,
            String directory,
            String rootPackage,
            String servKey) {
        this.model = model;
        this.finder = new ModelFinder(model);
        this.directory = directory;
        this.rootPackage = rootPackage;
        this.servKey = servKey;
    }

    /**
     * Write out the entire file
     *
     * @param out
     */
    public void write() {
        List<ServiceMethod> methods = finder.getServiceMethodsInService(servKey);
        if (methods.size() == 0) {
            System.out.println("No methods defined for servKey: " + servKey);
            return;
        }
        Set<XmlType> types = this.getMainXmlTypesUsedByService(methods);
        // the main servKey
        System.out.println("Generating admin UI for " + servKey);
        for (XmlType type : types) {
            new AdminUiInquirableWriter(model, directory, rootPackage, servKey, type, methods).write();
            new AdminUiLookupableWriter(model, directory, rootPackage, servKey, type, methods).write();
            new AdminUiLookupViewBeanWriter(model, directory, rootPackage, servKey, type, methods).write();
            new AdminUiInquiryViewBeanWriter(model, directory, rootPackage, servKey, type, methods).write();
        }
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
            if (!returnType.getName().equalsIgnoreCase("TypeInfo")) {
                if (!servKey.equalsIgnoreCase("type")) {
                    continue;
                }
            }
            // State only should show up on type service
            if (!returnType.getName().equalsIgnoreCase("StateInfo")) {
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
}
