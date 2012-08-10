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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.kuali.rice.krad.datadictionary.DataObjectEntry;
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
     * The dictionary applicationContext files.
     * @parameter
     **/
    private List<String> inputFiles = new ArrayList<String>();
    /**
     * The base applicationContext files.  
     * @parameter
     **/
    private List<String> supportFiles = new ArrayList<String>();
    /**
     * @parameter expression="${htmlDirectory}" default-value="${project.build.directory}/site/services/dictionarydocs"
     */
    private File htmlDirectory;

    public void setHtmlDirectory(File htmlDirectory) {
        this.htmlDirectory = htmlDirectory;
    }

    public File getHtmlDirectory() {
        return htmlDirectory;
    }

    public List<String> getInputFiles() {
        return inputFiles;
    }

    public MavenProject getProject() {
        return project;
    }

    public void setInputFiles(List<String> inputFiles) {
    	
    	this.inputFiles.clear();
    	
    	if (inputFiles != null)
    		this.inputFiles.addAll(inputFiles);
    }

    public List<String> getSupportFiles() {
        return supportFiles;
    }

    public void setSupportFiles(List<String> supportFiles) {
    	this.supportFiles.clear();
    	
    	if (supportFiles != null)
    		this.supportFiles.addAll(supportFiles);
    }

    @Override
    public void execute()
            throws MojoExecutionException {
        this.getLog().info("generating dictionary documentation");
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

        Set<String> inpFiles = new LinkedHashSet<String>(this.inputFiles.size());
        for (String dictFileName : this.inputFiles) {
            if (dictFileName.endsWith(".xml")) {
                inpFiles.add(dictFileName);
            }
            else {
            	log.warn("INVALID Input File: " + dictFileName);
            }
        }


        String outputDir = this.htmlDirectory.getAbsolutePath();
        DictionaryTesterHelper tester = new DictionaryTesterHelper(outputDir, this.inputFiles, this.supportFiles);
        List<String> outputFileNames = tester.doTest();

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
        DictionaryFormatter.writeFooter(out);
        out.close();
        
        log.info("finished generating dictionary documentation");
    }
}
