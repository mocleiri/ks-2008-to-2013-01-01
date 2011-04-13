/*
 * Copyright 2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may	obtain a copy of the License at
 *
 * 	http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.datadictionary.mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nwright
 */
public class KSDictionaryDocMojoTest {

    public KSDictionaryDocMojoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    private static final String POC_DIRECTORY = "C:/svn/ks-r2-poc/trunk/ks-services/ks-services-api/src/main/resources/";
    private static final String HTML_DIRECTORY = "target/html/dictionary";
    private static final String PROJECT_URL = "https://test.kuali.org/svn/student/sandbox/ks-r2-poc/trunk/ks-services/ks-services-api/src/main/resources";
    private static final String DICTIONARY_RESOURCES_DIRECTORY = "src/test/resources";

    /**
     * Test of execute method, of class KSDictionaryDocMojo.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        KSDictionaryDocMojo instance = new KSDictionaryDocMojo();
        instance.setHtmlDirectory(new File(HTML_DIRECTORY));
        List<String> inputFiles = new ArrayList();
        inputFiles.add(DICTIONARY_RESOURCES_DIRECTORY + "/ks-lui-person-relation-dictionary.xml");
        instance.setInputFiles(inputFiles);
        instance.setProjectUrl(PROJECT_URL);
        instance.execute();
    }
}
