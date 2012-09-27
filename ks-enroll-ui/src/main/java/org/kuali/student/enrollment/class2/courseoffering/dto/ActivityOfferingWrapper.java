package org.kuali.student.enrollment.class2.courseoffering.dto;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.student.enrollment.acal.dto.TermInfo;
import org.kuali.student.enrollment.courseoffering.dto.ActivityOfferingInfo;
import org.kuali.student.enrollment.courseoffering.dto.FormatOfferingInfo;
import org.kuali.student.r2.common.util.constants.LuiServiceConstants;
import org.kuali.student.r2.core.scheduling.dto.ScheduleInfo;
import org.kuali.student.r2.core.scheduling.dto.ScheduleRequestInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActivityOfferingWrapper implements Serializable{

    private ActivityOfferingInfo aoInfo;
    private FormatOfferingInfo formatOffering;
    private TermInfo term;
    private List<OfferingInstructorWrapper> instructors;
    private List<ScheduleComponentWrapper> scheduleComponentWrappers;
    private List<SeatPoolWrapper> seatpools;
    private boolean readOnlyView;
    private boolean isChecked;
    private String courseOfferingId;
    // Tanveer 06/13/2012
    private String stateName;
    private String typeName;

    private String termName;

    private String formatOfferingName;

    // Tanveer 06/27/2012
    private String waitListLevelTypeKey;
    private String waitListTypeKey;
    private boolean hasWaitList;
    private String waitListText = "";
    private String toolTipText = "";


    private String instructorNameHighestPercentEffort = "";

    private String firstInstructorDisplayName;

    private String courseOfferingCode = "";

    private List<ScheduleWrapper> actualScheduleComponents;
    private List<ScheduleWrapper> requestedScheduleComponents;
    private List<ScheduleWrapper> backUpRequestedComponents;
    private ScheduleWrapper newScheduleRequest;

    private ScheduleRequestInfo scheduleRequestInfo;
    private ScheduleInfo scheduleInfo;

    private String startTimeDisplay;
    private String endTimeDisplay;
    private String daysDisplayName;
    private String buildingName;
    private String roomName;
    private String tbaDisplayName;

    public ActivityOfferingWrapper(){
        aoInfo = new ActivityOfferingInfo();
        instructors = new ArrayList<OfferingInstructorWrapper>();
        seatpools = new ArrayList<SeatPoolWrapper>();
        aoInfo.setStateKey(LuiServiceConstants.LUI_AO_STATE_DRAFT_KEY);
        aoInfo.setTypeKey(LuiServiceConstants.LECTURE_ACTIVITY_OFFERING_TYPE_KEY);
        formatOffering = new FormatOfferingInfo();
        term = new TermInfo();
        scheduleComponentWrappers = new ArrayList<ScheduleComponentWrapper>();
        this.setReadOnlyView(false);
        this.setIsChecked(false);
        actualScheduleComponents = new ArrayList<ScheduleWrapper>();
        requestedScheduleComponents = new ArrayList<ScheduleWrapper>();
        backUpRequestedComponents = new ArrayList<ScheduleWrapper>();
        newScheduleRequest = new ScheduleWrapper();
    }

    public ActivityOfferingWrapper(ActivityOfferingInfo info){
        super();
        aoInfo = info;
        instructors = new ArrayList<OfferingInstructorWrapper>();
        seatpools = new ArrayList<SeatPoolWrapper>();
    }

    public String getCourseOfferingCode() {
        return courseOfferingCode;
    }

    public void setCourseOfferingCode(String courseOfferingCode) {
        this.courseOfferingCode = courseOfferingCode;
    }

    public String courseOfferingTitle;
    public void setCourseOfferingTitle(String courseOfferingTitle) {
        this.courseOfferingTitle = courseOfferingTitle;
    }

    public String getCourseOfferingTitle() {
        return courseOfferingTitle;
    }

    private String credits = "";
    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    private String activityCode = "";

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    private String abbreviatedCourseType = "";

    public String getAbbreviatedCourseType() {
        return abbreviatedCourseType;
    }

    public void setAbbreviatedCourseType(String abbreviatedCourseType) {
        this.abbreviatedCourseType= abbreviatedCourseType;
    }

    private String termDisplayString = "";

    public String getTermDisplayString() {
        return termDisplayString;
    }

    public void setTermDisplayString(String termDisplayString) {
        this.termDisplayString = termDisplayString;
    }

    public String getToolTipText() {
        return toolTipText;
    }

    public void setToolTipText(String toolTipText) {
        this.toolTipText = toolTipText;
    }

    public String getWaitListText() {
        return waitListText;
    }

    public void setWaitListText(String waitListText) {
        this.waitListText = waitListText;
    }



    public boolean getHasWaitList() {
        return hasWaitList;
    }

    public String getTbaDisplayName() {
        return tbaDisplayName;
    }

    public void setTbaDisplayName(boolean tba) {
        tbaDisplayName = StringUtils.EMPTY;
        if (tba){
            tbaDisplayName =  "TBA";
        }
    }

    public void setHasWaitList(boolean hasWaitList) {
        this.hasWaitList = hasWaitList;
    }

    public String getWaitListLevelTypeKey() {
        return waitListLevelTypeKey;
    }
    public void setWaitListLevelTypeKey(String waitListLevelTypeKey) {
        this.waitListLevelTypeKey = waitListLevelTypeKey;
    }
    public String getWaitListTypeKey() {
        return waitListTypeKey;
    }
    public void setWaitListTypeKey(String waitListTypeKey) {
        this.waitListTypeKey = waitListTypeKey;
    }

    public FormatOfferingInfo getFormatOffering() {
        return formatOffering;
    }

    public void setFormatOffering(FormatOfferingInfo formatOffering) {
        this.formatOffering = formatOffering;
    }

    public TermInfo getTerm() {
        return term;
    }

    public void setTerm(TermInfo term) {
        this.term = term;
    }

    public ActivityOfferingInfo getAoInfo() {
        return aoInfo;
    }

    public void setAoInfo(ActivityOfferingInfo aoInfo) {
        this.aoInfo = aoInfo;
    }

    public List<ScheduleComponentWrapper> getScheduleComponentWrappers() {
        return scheduleComponentWrappers;
    }

    public void setScheduleComponentWrappers(List<ScheduleComponentWrapper> scheduleComponentWrappers) {
        this.scheduleComponentWrappers = scheduleComponentWrappers;
    }

    public boolean getReadOnlyView() {
        return readOnlyView;
    }

    public void setReadOnlyView(boolean readOnlyView) {
        this.readOnlyView = readOnlyView;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean checked) {
        this.isChecked = checked;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCourseOfferingId() {
        return courseOfferingId;
    }

    public void setCourseOfferingId(String courseOfferingId) {
        this.courseOfferingId = courseOfferingId;
    }

    public List<OfferingInstructorWrapper> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<OfferingInstructorWrapper> instructors) {
        this.instructors = instructors;
    }

    public List<SeatPoolWrapper> getSeatpools() {
        return seatpools;
    }

    public void setSeatpools(List<SeatPoolWrapper> seatpools) {
        this.seatpools = seatpools;
    }

    public String getFirstInstructorDisplayName() {
        return firstInstructorDisplayName;
    }

    public void setFirstInstructorDisplayName(String firstInstructorDisplayName) {
        this.firstInstructorDisplayName = firstInstructorDisplayName;
    }

    public String getId() {
        return aoInfo.getId();
    }

    public void setId(String id) {
        aoInfo.setId(id);
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getFormatOfferingName() {
        return formatOfferingName;
    }

    public void setFormatOfferingName(String formatOfferingName) {
        this.formatOfferingName = formatOfferingName;
    }

    public String getIsMaxEnrollmentEstimateUI(){
        return StringUtils.capitalize(BooleanUtils.toStringYesNo(aoInfo.getIsMaxEnrollmentEstimate()));
    }

    public String getIsEvaluatedUI(){
        return StringUtils.capitalize(BooleanUtils.toStringYesNo(aoInfo.getIsEvaluated()));
    }

    public String getIsHonorsOfferingUI(){
        return StringUtils.capitalize(BooleanUtils.toStringYesNo(aoInfo.getIsHonorsOffering()));
    }

    public String getInstructorNameHighestPercentEffort() {
        return instructorNameHighestPercentEffort;
    }

    public void setInstructorNameHighestPercentEffort(String instructorNameHighestPercentEffort) {
        this.instructorNameHighestPercentEffort = instructorNameHighestPercentEffort;
    }

    public boolean isLegalToDelete() {
        if (StringUtils.equals(aoInfo.getStateKey(), LuiServiceConstants.LUI_AO_STATE_DRAFT_KEY) ||
                StringUtils.equals(aoInfo.getStateKey(), LuiServiceConstants.LUI_AO_STATE_APPROVED_KEY) ||
                StringUtils.equals(aoInfo.getStateKey(), LuiServiceConstants.LUI_AO_STATE_SUBMITTED_KEY)) {
            return true;
        }
        return false;
    }

    public List<ScheduleWrapper> getActualScheduleComponents() {
        if (actualScheduleComponents == null){
            actualScheduleComponents = new ArrayList<ScheduleWrapper>();
        }
        return actualScheduleComponents;
    }

    public void setActualScheduleComponents(List<ScheduleWrapper> actualScheduleComponents) {
        this.actualScheduleComponents = actualScheduleComponents;
    }

    public List<ScheduleWrapper> getRequestedScheduleComponents() {
        if (requestedScheduleComponents == null){
            requestedScheduleComponents = new ArrayList<ScheduleWrapper>();
        }
        return requestedScheduleComponents;
    }

    public void setRequestedScheduleComponents(List<ScheduleWrapper> requestedScheduleComponents) {
        this.requestedScheduleComponents = requestedScheduleComponents;
    }

    public ScheduleWrapper getNewScheduleRequest() {
        return newScheduleRequest;
    }

    public void setNewScheduleRequest(ScheduleWrapper newScheduleRequest) {
        this.newScheduleRequest = newScheduleRequest;
    }

    public String getStartTimeDisplay() {
        return startTimeDisplay;
    }

    public void setStartTimeDisplay(String startTimeDisplay) {
        this.startTimeDisplay = startTimeDisplay;
    }

    public String getEndTimeDisplay() {
        return endTimeDisplay;
    }

    public void setEndTimeDisplay(String endTimeDisplay) {
        this.endTimeDisplay = endTimeDisplay;
    }

    public String getDaysDisplayName() {
        return daysDisplayName;
    }

    public void setDaysDisplayName(String daysDisplayName) {
        this.daysDisplayName = daysDisplayName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ScheduleRequestInfo getScheduleRequestInfo() {
        return scheduleRequestInfo;
    }

    public void setScheduleRequestInfo(ScheduleRequestInfo scheduleRequestInfo) {
        this.scheduleRequestInfo = scheduleRequestInfo;
    }

    public ScheduleInfo getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(ScheduleInfo scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }

    public List<ScheduleWrapper> getBackUpRequestedComponents() {
        if (backUpRequestedComponents == null){
            backUpRequestedComponents = new ArrayList<ScheduleWrapper>();
        }
        return backUpRequestedComponents;
    }

    public void setBackUpRequestedComponents(List<ScheduleWrapper> backUpRequestedComponents) {
        this.backUpRequestedComponents = backUpRequestedComponents;
    }
}
