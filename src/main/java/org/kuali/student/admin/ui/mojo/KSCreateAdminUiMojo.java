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
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.kuali.student.common.mojo.AbstractKSMojo;
import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.util.ServicesFilterByKeys;

/**
 *
 * @author nwright
 *
 * @goal kscreateadminui
 * @phase none
 * @requiresProject true
 */
public class KSCreateAdminUiMojo extends AbstractKSMojo {

    public KSCreateAdminUiMojo() {
    }
    public String targetDir;

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ServiceContractModel model = getModel();
        validate(model);
        if (targetDir == null) {
            targetDir = "target/generated-sources/admin-ui";
        }
//        ServicesFilterKualiStudentOnly filter = new ServicesFilterKualiStudentOnly ();
        List<String> servKeys = new ArrayList<String>();
        servKeys.add("Atp");
        servKeys.add("Type");
        servKeys.add("State");
        servKeys.add("LRC");
        ServicesFilterByKeys filter = new ServicesFilterByKeys(servKeys);

        AdminUiWriter instance =
                new AdminUiWriter(model,
                targetDir,
                AdminUiWriter.DEFAULT_ROOT_PACKAGE,
                // TODO: inject filters as part of the Mojo parameters
                filter);
        instance.write();

    }
}
