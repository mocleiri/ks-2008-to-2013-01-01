package org.kuali.student.contract.mojo;

import java.io.File;
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
 * The plugin entrypoint which is used to generate the html wiki doc of the service interface.
 * @goal kscontractdoc
 * @phase site
 * @requiresProject true
 */
public class KSContractDocMojo extends AbstractMojo {

    /**
     * @parameter
     **/
    private List<String> sourceDirs;
    /**
     * @parameter expression="${htmlDirectory}" default-value="${project.build.directory}/site/services/contractdocs"
     */
    private File htmlDirectory;

    public File getHtmlDirectory() {
        return htmlDirectory;
    }

    public List<String> getSourceDirs() {
        return sourceDirs;
    }

    public void setHtmlDirectory(File htmlDirectory) {
        this.htmlDirectory = htmlDirectory;
    }

    public void setSourceDirs(List<String> sourceDirs) {
        this.sourceDirs = sourceDirs;
    }

    private ServiceContractModel getModel() {
        ServiceContractModel instance = new ServiceContractModelQDoxLoader(
                sourceDirs);
        return new ServiceContractModelCache(instance);

    }

    private boolean validate(ServiceContractModel model) {
        Collection<String> errors = new ServiceContractModelValidator(model).validate();
        if (errors.size() > 0) {
            StringBuilder buf = new StringBuilder();
            buf.append(errors.size()).append(" errors found while validating the data.");
            return false;


        }
        return true;
    }

    @Override
    public void execute() throws MojoExecutionException {
        ServiceContractModel model = null;
        HtmlContractWriter writer = null;
        getLog().info("publishing wiki contracts");
        model = this.getModel();
        this.validate(model);
        getLog().info("publishing to = " + this.htmlDirectory.toString());
        writer = new HtmlContractWriter(htmlDirectory.toString(), model);
        writer.write();


    }
}
