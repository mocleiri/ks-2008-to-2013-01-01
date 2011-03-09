package org.kuali.student.contract.mojo;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.impl.ServiceContractModelCache;
import org.kuali.student.contract.model.impl.ServiceContractModelQDoxLoader;
import org.kuali.student.contract.model.util.HtmlContractWriter;
import org.kuali.student.contract.model.validation.ServiceContractModelValidator;

/**
 * Says "Hi" to the user.
 * @goal kscontractdoc
 */
public class KSContractDocMojo extends AbstractMojo
{

	private static final String coreDirectory = "https://test.kuali.org/svn/student/sandbox/ks-r2-poc/trunk/ks-services/ks-services-api/src/main/java";
	// "C:/svn/maven-dictionary-generator/trunk/src/main/java/org/kuali/student/core";
	private static final String commonDirectory = "C:/Users/sambit/kuali/code/trunk/ks-common/ks-common-api/src/main/java";
	private static final String lumDirectory = "C:/Users/sambit/kuali/code/trunk/ks-lum/ks-lum-api/src/main/java";
	private static final String htmlDirectory = "target/html";

	private ServiceContractModel getModel() {
		List<String> srcDirs = new ArrayList<String>();
		srcDirs.add(coreDirectory);
		srcDirs.add(commonDirectory);
		srcDirs.add(lumDirectory);
		ServiceContractModel instance = new ServiceContractModelQDoxLoader(
				srcDirs);
		return new ServiceContractModelCache(instance);

	}


	private boolean validate(ServiceContractModel model) {
		Collection<String> errors = new ServiceContractModelValidator(model)
		.validate();
		if (errors.size() > 0) {
			StringBuffer buf = new StringBuffer();
			buf.append(errors.size()
					+ " errors found while validating the data.");
			return false;


		}
		return true;
	}

	public void execute() throws MojoExecutionException
	{
		ServiceContractModel model = null;
		HtmlContractWriter writer = null;
		getLog().info("publishing wiki contracts");
		model = this.getModel();
		this.validate(model);
		writer = new HtmlContractWriter(htmlDirectory + "/student", model);
		writer.write();


	}
}
