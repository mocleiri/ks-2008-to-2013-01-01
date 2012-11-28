/**
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.student.krms.termresolver;

import org.kuali.rice.krms.api.engine.TermResolutionException;
import org.kuali.student.enrollment.academicrecord.dto.StudentCourseRecordInfo;
import org.kuali.student.krms.util.KSKRMSExecutionConstants;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GradeTermResolver extends CompletedCoursesTermResolver {

    @Override
    public String getOutput() {
        return KSKRMSExecutionConstants.GRADE_TERM_NAME;
    }

    @Override
    public Set<String> getParameterNames() {
        return Collections.singleton(KSKRMSExecutionConstants.PERSON_ID_TERM_PROPERTY);
    }

    @Override
    public int getCost() {
        // TODO Analyze, though probably not much to check here
        return 0;
    }

    /*@Override
    public Boolean resolve(Map<String, Object> resolvedPrereqs, Map<String, String> parameters) throws TermResolutionException {
        // Get the list of course records from the superclass and then just return the one we need. (in this case we know there will only be one)
        List<StudentCourseRecordInfo> gradeRecords = super.resolve(resolvedPrereqs, parameters);

        return gradeRecords;
    }*/
}