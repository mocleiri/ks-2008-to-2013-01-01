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
    private static final String DICTIONARY_DOC_DIRECTORY = "target/site/dictionary";
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

    /**
     * Test of execute method, of class KSDictionaryDocMojo.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        KSDictionaryDocMojo instance = new KSDictionaryDocMojo();
        List<String> supportFiles = new ArrayList ();
//        supportFiles.add("ks-AtpInfo-dictionary-generated.xml");        
        supportFiles.add("ks-base-dictionary.xml");          
        supportFiles.add("ks-base-dictionary-validchars.xml");
        supportFiles.add("org/kuali/rice/krad/bo/datadictionary/DataDictionaryBaseTypes.xml");
        supportFiles.add("org/kuali/rice/krad/uif/UifControlDefinitions.xml");
        supportFiles.add("org/kuali/rice/krad/uif/UifMiscDefinitions.xml");
        instance.setSupportFiles(supportFiles);        
        List<String> inputFiles = new ArrayList ();
        inputFiles.add("ks-AtpInfo-dictionary.xml");        
//        inputFiles.add("ks-LprInfo-dictionary.xml");            
        instance.setInputFiles(inputFiles);
        instance.setHtmlDirectory(new File (DICTIONARY_DOC_DIRECTORY));
        instance.execute();
        assertTrue(new File(instance.getHtmlDirectory() + "/" + "index.html").exists());        
        assertTrue(new File(instance.getHtmlDirectory() + "/" + "AtpInfo.html").exists());
    }
}
