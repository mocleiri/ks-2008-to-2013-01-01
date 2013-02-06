package org.kuali.student.enrollment.class2.courseoffering.service;

import org.kuali.student.r2.core.acal.dto.TermInfo;
import org.kuali.student.enrollment.class2.courseoffering.dto.ActivityOfferingWrapper;
import org.kuali.student.enrollment.class2.courseoffering.dto.CourseOfferingListSectionWrapper;
import org.kuali.student.enrollment.class2.courseoffering.form.CourseOfferingManagementForm;
import org.kuali.student.enrollment.courseoffering.dto.CourseOfferingInfo;

import java.util.List;

public interface CourseOfferingManagementViewHelperService extends CO_AO_RG_ViewHelperService{
    /**
     *
     * @param termCode Each institution uses a code to represent a term.  At UW, the code appears to be three letters
     *                 followed by a 4-digit year, e.g., FAL2011, WIN2011, etc.
     * @return List of terms which match the term code (should be a list of one TermInfo)
     * @throws Exception
     */
    public List<TermInfo> findTermByTermCode(String termCode) throws Exception;

    public void loadCourseOfferingsByTermAndCourseCode(String termId, String courseCode, CourseOfferingManagementForm form) throws Exception;

    public void loadCourseOfferingsByTermAndSubjectCode(String termId, String subjectCode, CourseOfferingManagementForm form) throws Exception;

    public void loadActivityOfferingsByCourseOffering (CourseOfferingInfo theCourseOfferingInfo, CourseOfferingManagementForm form) throws Exception;

    public void createActivityOfferings(String formatOfferingId,String activityId,int noOfActivityOfferings, CourseOfferingManagementForm form);

    public void changeActivityOfferingsState(List<ActivityOfferingWrapper> aoList, CourseOfferingInfo courseOfferingInfo, String selectedAction) throws Exception;

    public void markCourseOfferingsForScheduling(List<CourseOfferingListSectionWrapper> coWrappers) throws Exception;

    public void loadPreviousAndNextCourseOffering(CourseOfferingManagementForm form, CourseOfferingInfo courseOfferingInfo);

    public List<ActivityOfferingWrapper> getActivityOfferingsByCourseOfferingId (String courseOfferingId, CourseOfferingManagementForm form) throws Exception;

    public void approveCourseOfferings(CourseOfferingManagementForm form) throws Exception;
    public void deleteCourseOfferings(CourseOfferingManagementForm form) throws Exception;
    public void approveActivityOfferings(CourseOfferingManagementForm form) throws Exception;
    public void draftActivityOfferings(CourseOfferingManagementForm form) throws Exception;

}
