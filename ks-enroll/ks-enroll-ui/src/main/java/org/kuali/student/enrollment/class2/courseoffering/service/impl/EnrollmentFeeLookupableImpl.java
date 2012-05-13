/**
 * Copyright 2012 The Kuali Foundation Licensed under the
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
 *
 */
package org.kuali.student.enrollment.class2.courseoffering.service.impl;

import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.web.form.LookupForm;
import org.kuali.student.enrollment.class2.courseoffering.util.ActivityOfferingConstants;
import org.kuali.student.enrollment.class2.courseoffering.util.CourseOfferingResourceLoader;
import org.kuali.student.enrollment.common.util.ContextBuilder;
import org.kuali.student.enrollment.courseoffering.dto.ActivityOfferingInfo;
import org.kuali.student.enrollment.courseoffering.service.CourseOfferingService;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.RichTextInfo;
import org.kuali.student.r2.common.exceptions.*;
import org.kuali.student.r2.core.fee.dto.EnrollmentFeeAmountInfo;
import org.kuali.student.r2.core.fee.dto.EnrollmentFeeInfo;
import org.kuali.student.r2.core.fee.infc.EnrollmentFeeAmount;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class //TODO ...
 *
 * @author Kuali Student Team
 */
public class EnrollmentFeeLookupableImpl extends LookupableImpl {
    @Override
    protected List<?> getSearchResults(LookupForm lookupForm, Map<String, String> fieldValues, boolean unbounded) {
        List<EnrollmentFeeInfo> enrollmentFeeInfos = new ArrayList<EnrollmentFeeInfo>();

        try {
            EnrollmentFeeInfo tempObj = new EnrollmentFeeInfo();

            EnrollmentFeeAmountInfo efa = new EnrollmentFeeAmountInfo();
            efa.setCurrencyQuantity(5);
            efa.setCurrencyTypeKey("some.type.key.for.currency");

            tempObj.setAmount(efa);
            tempObj.setDescr(new RichTextInfo("This is a test description of the EnrollmentFeeInfo", "Just a stub"));
            tempObj.setOrgId("123 Org");
            tempObj.setRefObjectId("42");
            tempObj.setId("112211");
            tempObj.setStateKey("active");
            tempObj.setTypeKey("some.type.key");


            enrollmentFeeInfos.add(tempObj);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return enrollmentFeeInfos;
    }

    public CourseOfferingService getCourseOfferingService() {
        return CourseOfferingResourceLoader.loadCourseOfferingService();
    }

    public ContextInfo getContextInfo() {
        return ContextBuilder.loadContextInfo();
    }
}
