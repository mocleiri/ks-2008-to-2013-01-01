package org.kuali.student.contract.mojo;



import java.io.File;
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
  @goal kscontractdoc
 */
public class KSContractDocMojo extends AbstractMojo
{	
	/**
	 * @parameter
	 */
	private List <String> sourceDirs; 
	
		/**
	* @parameter
	*/
	private   File htmlDirectory;


		
	public void setsourceDirs(List<String> sourceDirs) {
		this.sourceDirs = sourceDirs;
	}

	
	private ServiceContractModel getModel() {
		ServiceContractModel instance = new ServiceContractModelQDoxLoader(
				sourceDirs);
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
		getLog().info("publishing to = " + this.htmlDirectory.toString ());
		writer = new HtmlContractWriter(htmlDirectory.toString (), model);
		writer.write();


	}
}
