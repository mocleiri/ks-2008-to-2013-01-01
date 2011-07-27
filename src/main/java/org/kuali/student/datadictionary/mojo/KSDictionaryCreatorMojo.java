package org.kuali.student.datadictionary.mojo;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.XmlType;
import org.kuali.student.contract.model.impl.ServiceContractModelCache;
import org.kuali.student.contract.model.impl.ServiceContractModelQDoxLoader;
import org.kuali.student.datadictionary.util.KradDictionaryCreator;
import org.kuali.student.contract.model.validation.ServiceContractModelValidator;

/**
 * The plugin entrypoint which is used to generate dictionary files based on the contract
 * @phase generate-sources
 * @goal ksdictionarycreator
 */
public class KSDictionaryCreatorMojo extends AbstractMojo {

    /**
     * @parameter
     **/
    private List<String> sourceDirs;
    /**
     * @parameter expression="${outputDirectory}" default-value="${project.build.directory}/classes"
     */
    private File outputDirectory;
    /**
     * @parameter expression=false
     */
    private boolean writeManual;
    /**
     * @parameter expression=true
     */
    private boolean writeGenerated;
    /**
     * @parameter
     */
    private List<String> classNames;

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public List<String> getSourceDirs() {
        return sourceDirs;
    }

    public boolean isWriteManual() {
        return writeManual;
    }

    public boolean isWriteGenerated() {
        return writeGenerated;
    }

    public List<String> getClassNames() {
        return classNames;
    }

    public void setClassNames(List<String> classNames) {
        this.classNames = classNames;
    }

    public void setWriteManual(boolean writeManual) {
        this.writeManual = writeManual;
    }

    public void setWriteGenerated(boolean writeGenerated) {
        this.writeGenerated = writeGenerated;
    }

    public void setOutputDirectory(File htmlDirectory) {
        this.outputDirectory = htmlDirectory;
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
        getLog().info("generating ks-XXX-dictionary.xml files=" + this.writeManual);
        getLog().info("generating ks-XXX-dictionary-generated.xml files=" + this.writeGenerated);
        ServiceContractModel model = this.getModel();
        this.validate(model);

        Set<String> lowerClasses = new HashSet();
        for (String className : classNames) {
            lowerClasses.add(className.toLowerCase());
        }

        String dictionaryDirectory = this.outputDirectory.toString();
        for (XmlType xmlType : model.getXmlTypes()) {
            if (lowerClasses.contains(xmlType.getName().toLowerCase())) {
                lowerClasses.remove(xmlType.getName().toLowerCase());
                String xmlObject = xmlType.getName();
                KradDictionaryCreator writer =
                        new KradDictionaryCreator(dictionaryDirectory,
                        model,
                        xmlObject,
                        writeManual,
                        writeGenerated);
                writer.write();
            }
        }
        if (!lowerClasses.isEmpty()) {
            StringBuilder buf = new StringBuilder();
            buf.append(lowerClasses.size());
            buf.append(" classes were not processed: ");
            String comma = "";
            for (String className : lowerClasses) {
                buf.append(comma);
                buf.append(className);
                comma = ", ";
            }
            throw new MojoExecutionException(buf.toString());
        }
    }
}
