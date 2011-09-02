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

/**
 * Mojo for generating a formatted view of the data dictionary
 * @goal ksdictionarydoc
 * @phase site
 * @requiresDependencyResolution test
 */
public class KSDictionaryDocMojo extends AbstractMojo {

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;
    /**
     * @parameter
     **/
    private List<String> inputFiles;
    /**
     * @parameter
     **/
    private List<String> supportFiles = new ArrayList();
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
        this.inputFiles = inputFiles;
    }

    public List<String> getSupportFiles() {
        return supportFiles;
    }

    public void setSupportFiles(List<String> supportFiles) {
        this.supportFiles = supportFiles;
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
            List runtimeClasspathElements;
            try {
                runtimeClasspathElements = project.getRuntimeClasspathElements();
            } catch (DependencyResolutionRequiredException ex) {
                throw new MojoExecutionException("got error", ex);
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


        //System.out.println ("Writing java class: " + fileName + " to " + dir.getAbsolutePath ());
        if (!htmlDirectory.exists()) {
            if (!htmlDirectory.mkdirs()) {
//                throw new MojoExecutionException("Could not create directory "
                throw new IllegalArgumentException("Could not create directory "
                        + this.htmlDirectory.getPath());
            }
        }

        Set<String> inpFiles = new LinkedHashSet(this.inputFiles.size());
        for (String dictFileName : this.inputFiles) {
            if (dictFileName.endsWith(".xml")) {
                inpFiles.add(dictFileName);
            }
        }

        Set<String> configFiles = new LinkedHashSet(this.inputFiles.size() + supportFiles.size());
        configFiles.addAll(inpFiles);
        configFiles.addAll(this.supportFiles);

        String outputDir = this.htmlDirectory.getAbsolutePath();
        DictionaryTesterHelper tester = new DictionaryTesterHelper(outputDir, configFiles);
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
        String endUL = "";
        Map<String, DataObjectEntry> beansOfType = tester.getDataObjectEntryBeans();
        for (String beanId : beansOfType.keySet()) {
            String outputFileName = beanId + ".html";
            out.println("<li><a href=\"" + outputFileName + "\">" + beanId + "</a>");
        }
        out.println("</ul>");
        DictionaryFormatter.writeFooter(out);
        this.getLog().info("finished generating dictionary documentation");
    }
}
