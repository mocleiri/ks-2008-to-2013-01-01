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
package org.kuali.student.contract.command.run;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.impl.ServiceContractModelCache;
import org.kuali.student.contract.model.impl.ServiceContractModelQDoxLoader;
import org.kuali.student.contract.model.validation.ServiceContractModelValidator;
import org.kuali.student.contract.writer.service.MockImplWriter;

/**
 *
 * @author nwright
 */
public class MockImplWriterRun {

    public MockImplWriterRun() {
    }
    private static final String CORE_DIRECTORY =
            "C:/svn/student/ks-core/ks-core-api/src/main/java";
    private static final String ENROLL13_DIRECTORY =
            "C:/svn/ks-1.3/ks-enroll/ks-enroll-api/src/main/java";
    private static final String ENROLL13_SERVICES_DIRECTORY =
            "C:/svn/ks-1.3-services/ks-enroll/ks-enroll-api/src/main/java";
//                           "C:/svn/maven-dictionary-generator/trunk/src/main/java/org/kuali/student/core";
    private static final String COMMON_DIRECTORY =
            "C:/svn/student/ks-common/ks-common-api/src/main/java";
    private static final String LUM_DIRECTORY =
            "C:/svn/student/ks-lum/ks-lum-api/src/main/java";

    private static ServiceContractModel getModel() {
        List<String> srcDirs = new ArrayList();
        srcDirs.add(ENROLL13_SERVICES_DIRECTORY);
//        srcDirs.add(ENROLL13_DIRECTORY);
//        srcDirs.add(LUM_DIRECTORY);
//        srcDirs.add(CORE_DIRECTORY);
//        srcDirs.add(COMMON_DIRECTORY);
        ServiceContractModel instance = new ServiceContractModelQDoxLoader(srcDirs);
        return new ServiceContractModelCache(instance);
    }

    private static void validate(ServiceContractModel model) {
        Collection<String> errors =
                new ServiceContractModelValidator(model).validate();
        if (!errors.isEmpty()) {
            StringBuilder buf = new StringBuilder();
            buf.append(errors.size()).append(" errors found while validating the data.");
            int cnt = 0;
            for (String msg : errors) {
                cnt++;
                buf.append("\n");
                buf.append("*error*").append(cnt).append(":").append(msg);
            }

            throw new IllegalArgumentException(buf.toString());
        }
    }

    public static void main(String[] args) {
        ServiceContractModel model = getModel();
        validate(model);
//   List<String> servicesToProcess = new ArrayList ();
//   servicesToProcess.add ("atp");
//   servicesToProcess.add ("lu");
//   servicesToProcess.add ("lo");
//   servicesToProcess.add ("organization");
//   servicesToProcess.add ("proposal");
////   servicesToProcess.add ("comment");
//   servicesToProcess.add ("dictionary");
//   servicesToProcess.add ("document");
//   servicesToProcess.add ("enumerable");
//   servicesToProcess.add ("search");
//   ServicesFilter filter = new ServicesFilterByKeys (servicesToProcess);
        String targetDir = "target/gen-src";
//  targetDir = "src/main/java";
        boolean isR1 = false;
        MockImplWriter instance =
                new MockImplWriter(model,
                targetDir,
                MockImplWriter.ROOT_PACKAGE,
//                MockImplWriter.ENROLLMENT_ROOT_PACKAGE,
                null,
                isR1);
        instance.write();

    }
}
