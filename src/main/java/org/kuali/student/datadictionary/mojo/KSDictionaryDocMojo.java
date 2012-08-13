/*
 * Copyright 2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.datadictionary.mojo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.kuali.student.contract.model.MessageStructure;
import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.impl.ServiceContractModelCache;
import org.kuali.student.contract.model.impl.ServiceContractModelQDoxLoader;
import org.kuali.student.contract.model.validation.ServiceContractModelValidator;
import org.kuali.student.datadictionary.util.DictionaryFormatter;
import org.kuali.student.datadictionary.util.DictionaryTesterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mojo for generating a formatted view of the data dictionary.
 * 
 * <pre>
 * {@code
 * <plugin>
 * 		<groupId>org.kuali.maven.plugins</groupId>
 *      <artifactId>maven-kscontractdoc-plugin</artifactId>
 *      <execution>
 *      	<id>generate-dictionary-documentation</id>
 *          <phase>site</phase>
 *          <goals>
 *          	<goal>ksdictionarydoc</goal>                            
 *          </goals>
 *          <configuration>
 *          	<inputFiles>
 *              	<inputFile>ks-AtpInfo-dictionary.xml</inputFile>
 *              	<inputFile>ks-MilestoneInfo-dictionary.xml</inputFile>
 *              	<inputFile>ks-AtpAtpRelationInfo-dictionary.xml</inputFile>
 *           	</inputFiles>
 *           <supportFiles>
 *           	<supportFile>commonApplicationContext.xml</supportFile>
 *           </supportFiles>
 *          </configuration>
 *     </execution>
 * </plugin>
 *  }
 * </pre>
 * 
 * An application context is constructed for each <b>inputFile</b> and all of the <b>supportFiles</b>
 * 
 *  Errors with an application context are detected and logged but will not break the plugin's ability to generate the other files.
 * 
 * @goal ksdictionarydoc
 * @phase site
 * @requiresDependencyResolution test
 */
public class KSDictionaryDocMojo extends AbstractMojo {

	private static final Logger log = LoggerFactory.getLogger(KSDictionaryDocMojo.class);
	
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;
    
    /**
     * The base applicationContext files.  
     * @parameter
     **/
    private List<String> supportFiles = new ArrayList<String>();
    /**
     * @parameter expression="${htmlDirectory}" default-value="${project.build.directory}/site/services/dictionarydocs"
     */
    private File htmlDirectory;

	private String testDictionaryFile;

    public void setHtmlDirectory(File htmlDirectory) {
        this.htmlDirectory = htmlDirectory;
    }

    public File getHtmlDirectory() {
        return htmlDirectory;
    }

    public MavenProject getProject() {
        return project;
    }

    public List<String> getSupportFiles() {
        return supportFiles;
    }

    public void setSupportFiles(List<String> supportFiles) {
    	this.supportFiles.clear();
    	
    	if (supportFiles != null)
    		this.supportFiles.addAll(supportFiles);
    }

    private ServiceContractModel getModel() {
        ServiceContractModel instance = new ServiceContractModelQDoxLoader(
                project.getCompileSourceRoots());
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
    public void execute()
            throws MojoExecutionException {
    	this.getLog().info("generating dictionary documentation");
        
        if (getPluginContext() != null) {
        	project = (MavenProject) getPluginContext().get("project");
        }
        
        // add the current projects classpath to the plugin so the springbean
        // loader can find the xml files and lasses that it needs to can be run
        // against the current project's files
        if (project != null) {
            this.getLog().info("adding current project's classpath to plugin class loader");
            List<String> runtimeClasspathElements;
            try {
                runtimeClasspathElements = project.getRuntimeClasspathElements();
            } catch (DependencyResolutionRequiredException ex) {
                throw new MojoExecutionException("Failed to get runtime classpath elements.", ex);
            }
            URL[] runtimeUrls = new URL[runtimeClasspathElements.size()];
            for (int i = 0; i < runtimeClasspathElements.size(); i++) {
                String element = (String) runtimeClasspathElements.get(i);
                try {
                    runtimeUrls[i] = new File(element).toURI().toURL();
                } catch (MalformedURLException ex) {
                    throw new MojoExecutionException(element, ex);
                }
            }
            URLClassLoader newLoader = new URLClassLoader(runtimeUrls,
                    Thread.currentThread().getContextClassLoader());
            Thread.currentThread().setContextClassLoader(newLoader);
        }


        if (!htmlDirectory.exists()) {
            if (!htmlDirectory.mkdirs()) {
                throw new IllegalArgumentException("Could not create directory "
                        + this.htmlDirectory.getPath());
            }
        }
        
        Set<String> inpFiles = new LinkedHashSet<String>();
        if (project != null) {
        ServiceContractModel model = this.getModel();
        this.validate(model);
        inpFiles.addAll(extractDictionaryFiles(model));

    }
        else {
        		inpFiles.add(this.testDictionaryFile);
        }
    
       
        
        

        String outputDir = this.htmlDirectory.getAbsolutePath();
        DictionaryTesterHelper tester = new DictionaryTesterHelper(outputDir, inpFiles, this.supportFiles);
        tester.doTest();

        // write out the index file
        String indexFileName = this.htmlDirectory.getPath() + "/" + "index.html";
        File indexFile = new File(indexFileName);
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(indexFile, false);
        } catch (FileNotFoundException ex) {
//            throw new MojoExecutionException(indexFileName, ex);
            throw new IllegalArgumentException(indexFileName, ex);
        }
        PrintStream out = new PrintStream(outputStream);
        DictionaryFormatter.writeHeader(out, "Data Dictionary Index");
        out.println("<h1>Data Dictionary Index</h1>");

        out.println("<ul>");
        
        Map<String, List<String>> fileToBeanNameMap = tester.getInputFileToBeanNameMap();
        
		for (String inputFile : fileToBeanNameMap.keySet()) {

			List<String> beanIds = fileToBeanNameMap.get(inputFile);

			for (String beanId : beanIds) {

				String outputFileName = beanId + ".html";
				out.println("<li><a href=\"" + outputFileName + "\">" + beanId
						+ "</a>");
			}
		}
        out.println("</ul>");
        
		if (tester.getInvalidDictionaryFiles().size() > 0) {

			out.println("<h1>Invalid Dictionary Files</h1>");
			out.println("<blockquote>The Dictionary File exists but a problem is present.</blockquote>");
			out.println("<ul>");

			for (String invalidFile : tester.getInvalidDictionaryFiles()) {
				out.println("<li><b>" + invalidFile + "</b></li>");
			}

			out.println("</ul>");

		}
        
		if (tester.getMissingDictionaryFiles().size() > 0) {
			out.println("<h1>Missing Dictionary Files</h1>");
			out.println("<blockquote>The Message structure exists but there is not dictionary file present.</blockquote>");
			out.println("<ul>");
			for (String missingFile : tester.getMissingDictionaryFiles()) {
				out.println("<li><b>" + missingFile + "</b></li>");
			}
			out.println("</ul>");

		}
        
        DictionaryFormatter.writeFooter(out);
        out.close();
        
        log.info("finished generating dictionary documentation");
    }

	private Collection<String> extractDictionaryFiles(
			ServiceContractModel model) {
		
		Set<String> dictionaryFiles = new LinkedHashSet<String>();
		
		List<MessageStructure> ms = model.getMessageStructures();
		
		for (MessageStructure messageStructure : ms) {
			
			String inputFileName = "ks-" + messageStructure.getXmlObject() + "-dictionary.xml";

			dictionaryFiles.add(inputFileName);
		}
		
		
		return dictionaryFiles;
	}

	/**
	 * Used for testing to hard code a single dictionary file to use.
	 * @param string
	 */
	public void setTestDictionaryFile(String dictionaryFile) {
		this.testDictionaryFile = dictionaryFile;
		
	}
}
