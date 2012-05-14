package org.kuali.student.datadictionary.mojo;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.impl.ServiceContractModelCache;
import org.kuali.student.contract.model.impl.ServiceContractModelQDoxLoader;
import org.kuali.student.contract.model.validation.ServiceContractModelValidator;
import org.kuali.student.contract.writer.service.EachMethodServiceWriter;

/**
 * The plugin entrypoint which is used to generate a java interface file for each
 * service method in the source files based on the contract definition
 * @phase generate-sources
 * @goal kseachmethodservicegenerator
 */
public class EachMethodServiceGeneratorrMojo extends AbstractMojo {

    /**
     * @parameter
     **/
    private List<String> sourceDirs;
    /**
     * @parameter expression="${outputDirectory}" default-value="${project.build.directory}/generated-sources"
     */
    private File outputDirectory;

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public List<String> getSourceDirs() {
        return sourceDirs;
    }

    public void setOutputDirectory(File htmlDirectory) {
        this.outputDirectory = htmlDirectory;
    }

    public void setSourceDirs(List<String> sourceDirs) {
        this.sourceDirs = sourceDirs;
    }
    /**
     * @parameter expression="${rootPackage}" default-value="org.kuali.student.enrollment"
     */
    private String rootPackage;

    public String getRootPackage() {
        return rootPackage;
    }

    public void setRootPackage(String rootPackage) {
        this.rootPackage = rootPackage;
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
        getLog().info("generating separate java interface files for each method in the service contracts");
        ServiceContractModel model = this.getModel();
        this.validate(model);
        if (!outputDirectory.exists()) {
            if (!outputDirectory.mkdirs()) {
//                throw new MojoExecutionException("Could not create directory "
                throw new IllegalArgumentException("Could not create directory "
                        + this.outputDirectory.getPath());
            }
        }
        String targetDirectory = this.outputDirectory.toString();
        EachMethodServiceWriter instance =
                new EachMethodServiceWriter(model,
                targetDirectory,
                rootPackage,
                null);
        instance.write();

    }
}
