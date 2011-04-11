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
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.kuali.student.datadictionary.util.DictionaryTesterHelper;

/**
 * Mojo for generating a formatted view of the data dictionary
 * @goal ksdictionarydoc
 */
public class KSDictionaryDocMojo extends AbstractMojo {

    /**
     * @parameter
     **/
    private List<String> inputFiles;
    /**
     * @parameter expression="${htmlDirectory}" default-value="${project.build.directory}/site/services/dictionarydocs"
     */
    private File htmlDirectory;
    /**
     * @parameter
     **/
    private String projectUrl;

    public void setHtmlDirectory(File htmlDirectory) {
        this.htmlDirectory = htmlDirectory;
    }

    public void setInputFiles(List<String> inputFiles) {
        this.inputFiles = inputFiles;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    @Override
    public void execute() throws MojoExecutionException {
        for (String dictFileName : this.inputFiles) {
            String outputFileName = this.htmlDirectory.getPath() + "/" + replaceXmlWithHtml(dictFileName);
            DictionaryTesterHelper tester = new DictionaryTesterHelper(outputFileName, this.projectUrl, dictFileName);
            List errors = tester.doTest();
            if (errors == null) {
                continue;
            }
            if (errors.isEmpty()) {
                continue;
            }
            throw new MojoExecutionException("Errors validating dictionary file "
                    + dictFileName + "\n" + this.formatAsString(errors));
        }
    }

    private String replaceXmlWithHtml(String name) {
        if (name.endsWith(".xml")) {
            name = name.substring(0, ".xml".length());
        }
        return name + ".html";
    }

    private String formatAsString(List<String> errors) {
        int i = 0;
        StringBuilder builder = new StringBuilder();
        for (String error : errors) {
            i++;
            builder.append(i + ". " + error + "\n");
        }
        return builder.toString();
    }
}
