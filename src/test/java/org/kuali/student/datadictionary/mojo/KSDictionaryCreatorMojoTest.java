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
public class KSDictionaryCreatorMojoTest {

    private static final String CORE_DIRECTORY =
            "C:/svn/ks-1.3/ks-core/ks-core-api/src/main/java";
    // "C:/svn/maven-dictionary-generator/trunk/src/main/java/org/kuali/student/core";
    private static final String COMMON_DIRECTORY =
            "C:/svn/ks-1.3/ks-common/ks-common-api/src/main/java";
    private static final String ENROLL_PROJECT_SRC_MAIN = "C:/svn/ks-1.3/ks-enroll/ks-enroll-api/src/main";
    private static final String ENROLL_PROJECT_JAVA_DIRECTORY = ENROLL_PROJECT_SRC_MAIN + "/java";
    private static final String ENROLL_PROJECT_RESOURCES_DIRECTORY = ENROLL_PROJECT_SRC_MAIN + "/resources";
      
    private static final String LUM_DIRECTORY =
            "C:/svn/ks-1.3/ks-lum/ks-lum-api/src/main/java";
    private static final String RICE_DIRECTORY =
            "C:/svn/rice/rice-release-1-0-2-1-br/api/src/main/java";
    private static final String TEST_SOURCE_DIRECTORY =
            "src/test/java/org/kuali/student/contract/model/test/source";
    private static final String TEST_ATP_SOURCE_DIRECTORY =
            "src/test/java/org/kuali/student/r2/core/atp.dto";    
    private static final String TARGET_GENERATED_SOURCES = "target/generated-sources";
    private static final String RESOURCES_DIRECTORY =
            // "C:/svn/student/ks-core/ks-core-api/src/main/java";
            "src/main/resources";
    private static final String PESC_CORE_MAIN = RESOURCES_DIRECTORY
            + "/CoreMain_v1.8.0.xsd";

    public KSDictionaryCreatorMojoTest() {
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
     * Test of execute method, of class KSDictionaryCreatorMojo.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        List<String> srcDirs = new ArrayList<String>();
//        srcDirs.add(TEST_SOURCE_DIRECTORY);
        srcDirs.add (TEST_ATP_SOURCE_DIRECTORY);
//        srcDirs.add(ENROLL_PROJECT_JAVA_DIRECTORY);
//		srcDirs.add(CORE_DIRECTORY);
//		srcDirs.add(COMMON_DIRECTORY);
//		srcDirs.add(LUM_DIRECTORY);
        KSDictionaryCreatorMojo instance = new KSDictionaryCreatorMojo();
        instance.setSourceDirs(srcDirs);
        instance.setOutputDirectory(new File (TARGET_GENERATED_SOURCES)); 
        // Be careful when you uncomment this one it will overwrite stuff in another project
//        instance.setOutputDirectory(new File(ENROLL_PROJECT_RESOURCES_DIRECTORY));
        instance.setWriteManual(true);
        instance.setWriteGenerated(false);
        instance.setThrowExceptionIfNotAllFilesProcessed(false);
        List<String> classNames = new ArrayList();
        // Atp
        classNames.add("AtpInfo");
        classNames.add("MilestoneInfo");
        classNames.add("AtpMilestoneRelationInfo");
        classNames.add("AtpAtpRelationInfo");
        // Acal
        classNames.add("AcademicCalendarInfo");
        classNames.add("CampusCalendarInfo");
        classNames.add("TermInfo");
        classNames.add("RegistrationDateGroupInfo");
        classNames.add("HolidayInfo");
        classNames.add("KeyDateInfo");
        // LPR 
        classNames.add("LprInfo");
        // Hold
        classNames.add("HoldInfo");
        classNames.add("IssueInfo");
        classNames.add("RestrictionInfo");
        // LUI 
        classNames.add("LuiInfo");
        classNames.add("LuiLuiRelationInfo");
        classNames.add("LuiCapacityInfo");
        // Course Offering
        classNames.add("CourseOfferingInfo");
        classNames.add("ActivityOfferingInfo");
        classNames.add("RegistrationGroupInfo");
        classNames.add("SeatPoolDefinitionInfo");
        instance.setClassNames(classNames);
        instance.execute();
//        assertTrue(new File(instance.getOutputDirectory() + "/" + "ks-LprInfo-dictionary.xml").exists());
    }
}
