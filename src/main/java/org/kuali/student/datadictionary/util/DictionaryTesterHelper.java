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
package org.kuali.student.datadictionary.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.kuali.rice.krad.datadictionary.DataObjectEntry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DictionaryTesterHelper {

    private String outputDir;
    private Set<String> configFiles;
    private ApplicationContext ac = null;

    public DictionaryTesterHelper(String outputDir,
            Set<String> configFiles) {
        this.outputDir = outputDir;
        this.configFiles = configFiles;
    }

    public void loadApplicationContext() {
        System.out.println ("DictionaryTesterHelper: begin loading application context");
        List<String> configLocations = new ArrayList(configFiles);
//        System.out.println ("DictionaryTesterHelper: adding " + supportFiles.size() + " support files");
//        configLocations.add("classpath:" + dictFileName);
        String[] configLocs = configLocations.toArray(new String[0]);
        ac = new ClassPathXmlApplicationContext(configLocs);
        System.out.println ("DictionaryTesterHelper: end loading application context");        
    }

    public Map<String, DataObjectEntry> getDataObjectEntryBeans () {
      return (Map<String, DataObjectEntry>) ac.getBeansOfType(DataObjectEntry.class);  
    }
    
    public List<String> doTest() {
        List<String> outputFileNames = new ArrayList();
        if (ac == null) {
            loadApplicationContext ();
        }
        Map<String, DataObjectEntry> beansOfType = this.getDataObjectEntryBeans();
                
        for (String beanId : beansOfType.keySet()) {
            DataObjectEntry doe = beansOfType.get(beanId);
            if ("org.kuali.rice.krad.bo.AttributeReferenceDummy".equals(doe.getFullClassName())) {
                continue;
            }
            System.out.println("Processing data object entry: " + doe.getDataObjectClass().getName());
            DictionaryValidator validator = new DictionaryValidator(doe, new HashSet());
            List<String> errors = validator.validate();
            if (errors != null) {
                if (!errors.isEmpty()) {
                    throw new IllegalArgumentException("Errors validating bean "
                            + beanId + "\n" + this.formatAsString(errors));
                }
            }
            String outputFileName = beanId + ".html";
            outputFileNames.add(outputFileName);
            String fullOutputFileName = this.outputDir + "/" + outputFileName;
            DictionaryFormatter formatter = new DictionaryFormatter(doe, beansOfType, beanId, fullOutputFileName);
            formatter.formatForHtml();
        }
        return outputFileNames;
    }

    private String formatAsString(List<String> errors) {
        int i = 0;
        StringBuilder builder = new StringBuilder();
        for (String error : errors) {
            i++;
            builder.append(i).append(". ").append(error).append("\n");
        }
        return builder.toString();
    }
}
