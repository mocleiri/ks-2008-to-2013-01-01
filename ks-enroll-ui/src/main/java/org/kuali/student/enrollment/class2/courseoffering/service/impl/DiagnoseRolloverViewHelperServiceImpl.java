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
 * Created by Charles on 9/21/12
 */
package org.kuali.student.enrollment.class2.courseoffering.service.impl;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.criteria.PredicateFactory;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.kuali.student.enrollment.acal.dto.TermInfo;
import org.kuali.student.enrollment.acal.service.AcademicCalendarService;
import org.kuali.student.enrollment.class2.courseoffering.service.DiagnoseRolloverViewHelperService;
import org.kuali.student.enrollment.class2.courseoffering.util.CourseOfferingConstants;
import org.kuali.student.enrollment.courseoffering.dto.CourseOfferingInfo;
import org.kuali.student.enrollment.courseoffering.service.CourseOfferingService;
import org.kuali.student.enrollment.courseofferingset.dto.SocInfo;
import org.kuali.student.enrollment.courseofferingset.service.CourseOfferingSetService;
import org.kuali.student.r2.common.constants.CommonServiceConstants;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.util.constants.AcademicCalendarServiceConstants;
import org.kuali.student.r2.common.util.constants.CourseOfferingServiceConstants;
import org.kuali.student.r2.common.util.constants.CourseOfferingSetServiceConstants;
import org.kuali.student.r2.lum.course.service.CourseService;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * This class //TODO ...
 *
 * @author Kuali Student Team
 */
public class DiagnoseRolloverViewHelperServiceImpl extends ViewHelperServiceImpl implements DiagnoseRolloverViewHelperService {
    private AcademicCalendarService acalService = null;
    private CourseOfferingService coService = null;
    private CourseOfferingSetService socService = null;
    private CourseService courseService = null;

    private static final Logger LOG = Logger.getLogger(DiagnoseRolloverViewHelperServiceImpl.class);

    @Override
    public TermInfo searchTermByTermCode(String termCode) throws Exception {
        _initServices();

        // TODO: Find sensible way to rewrap exception that acal service may throw
        // Find the term (alas, I think it does approximate search)
        QueryByCriteria.Builder qbcBuilder = QueryByCriteria.Builder.create();
        qbcBuilder.setPredicates(PredicateFactory.equal(CourseOfferingConstants.ATP_CODE, termCode));
        QueryByCriteria criteria = qbcBuilder.build();

        // Do search.  In ideal case, terms returns one element, which is the desired term.
        List<TermInfo> terms = acalService.searchForTerms(criteria, new ContextInfo());
        if (terms == null || terms.isEmpty()) {
            return null;
        } else {
            return terms.get(0);
        }
    }

    @Override
    public boolean termHasCourseOfferings(TermInfo termInfo) throws Exception {
        _initServices();
        List<String> coIds = coService.getCourseOfferingIdsByTerm(termInfo.getId(), true, new ContextInfo());
        return coIds != null && coIds.size() > 0;
    }

    @Override
    public boolean termHasSoc(TermInfo termInfo) throws Exception {
        _initServices();
        ContextInfo contextInfo = new ContextInfo();
        List<String> socIds = socService.getSocIdsByTerm(termInfo.getId(), contextInfo);
        List<SocInfo> socInfos = socService.getSocsByIds(socIds, contextInfo);
        for (SocInfo socInfo: socInfos) {
            if (socInfo.getTypeKey().equals(CourseOfferingSetServiceConstants.MAIN_SOC_TYPE_KEY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CourseOfferingInfo getCourseOfferingInfo(String termId, String courseOfferingCode) throws Exception {
        List<CourseOfferingInfo> coList = searchCourseOfferingByCOCodeAndTerm(courseOfferingCode, termId);
        if (coList == null ||  coList.isEmpty()) {
            return null;
        } else {
            CourseOfferingInfo courseOfferingInfo = coList.get(0);
            return courseOfferingInfo; // Pick first one
        }
    }

    private List<CourseOfferingInfo> searchCourseOfferingByCOCodeAndTerm(String courseOfferingCode, String termId) throws Exception {
        _initServices();

        QueryByCriteria.Builder qbcBuilder = QueryByCriteria.Builder.create();
        qbcBuilder.setPredicates(
                PredicateFactory.and(
                    PredicateFactory.equal(CourseOfferingConstants.COURSEOFFERING_COURSE_OFFERING_CODE, courseOfferingCode),
                    PredicateFactory.equal(CourseOfferingConstants.ATP_ID, termId)
                ));
        QueryByCriteria criteria = qbcBuilder.build();
        List<CourseOfferingInfo> coList = coService.searchForCourseOfferings(criteria, new ContextInfo());
        return coList;
    }

    private void _initServices() {
        if (coService == null) {
            coService = (CourseOfferingService) GlobalResourceLoader.getService(new QName(CourseOfferingServiceConstants.NAMESPACE,
                    CourseOfferingServiceConstants.SERVICE_NAME_LOCAL_PART));
        }

        if (socService == null) {
            socService = (CourseOfferingSetService) GlobalResourceLoader.getService(new QName(CourseOfferingSetServiceConstants.NAMESPACE,
                    CourseOfferingSetServiceConstants.SERVICE_NAME_LOCAL_PART));
        }

        if (courseService == null) {
            Object o = GlobalResourceLoader.getService(new QName(CommonServiceConstants.REF_OBJECT_URI_GLOBAL_PREFIX + "course",
                    "CourseService"));
            courseService = (CourseService) o;
        }

        if (acalService == null) {
            acalService = (AcademicCalendarService) GlobalResourceLoader.getService(new QName(AcademicCalendarServiceConstants.NAMESPACE,
                    AcademicCalendarServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
    }
}
