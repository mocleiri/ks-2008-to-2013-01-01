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


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.kuali.student.common.mojo.AbstractKSMojo;
import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.util.ServicesFilterKualiStudentOnly;

/**
 *
 * @author nwright
 * 
 *  @goal kscreateadminui
 *  @phase none
 *  @requiresProject true
 */
public class KSCreateAdminUiMojo extends AbstractKSMojo {

    public KSCreateAdminUiMojo() {
    }
   

   
    @Override
	public void execute() throws MojoExecutionException, MojoFailureException {
        ServiceContractModel model = getModel();
        validate(model);
        String targetDir = "target/generated-sources/admin-ui";
        AdminUiWriter instance =
                new AdminUiWriter(model,
                targetDir,
                AdminUiWriter.DEFAULT_ROOT_PACKAGE,
                // TODO: inject filters as part of the Mojo parameters
                new ServicesFilterKualiStudentOnly ());
        instance.write();

    }
}
