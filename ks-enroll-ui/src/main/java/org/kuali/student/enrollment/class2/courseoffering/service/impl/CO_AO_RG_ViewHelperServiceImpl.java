package org.kuali.student.enrollment.class2.courseoffering.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.kuali.student.enrollment.class2.courseoffering.dto.ActivityOfferingWrapper;
import org.kuali.student.enrollment.class2.courseoffering.service.CO_AO_RG_ViewHelperService;
import org.kuali.student.enrollment.class2.courseoffering.util.CourseOfferingResourceLoader;
import org.kuali.student.enrollment.class2.courseoffering.util.ViewHelperUtil;
import org.kuali.student.enrollment.courseoffering.dto.ActivityOfferingInfo;
import org.kuali.student.enrollment.courseoffering.dto.FormatOfferingInfo;
import org.kuali.student.enrollment.courseoffering.dto.OfferingInstructorInfo;
import org.kuali.student.enrollment.courseoffering.service.CourseOfferingService;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.TimeOfDayInfo;
import org.kuali.student.r2.common.util.ContextUtils;
import org.kuali.student.r2.common.util.constants.CourseOfferingServiceConstants;
import org.kuali.student.r2.core.class1.state.dto.StateInfo;
import org.kuali.student.r2.core.class1.state.service.StateService;
import org.kuali.student.r2.core.class1.type.dto.TypeInfo;
import org.kuali.student.r2.core.class1.type.service.TypeService;
import org.kuali.student.r2.core.room.dto.BuildingInfo;
import org.kuali.student.r2.core.room.dto.RoomInfo;
import org.kuali.student.r2.core.room.service.RoomService;
import org.kuali.student.r2.core.scheduling.constants.SchedulingServiceConstants;
import org.kuali.student.r2.core.scheduling.dto.*;
import org.kuali.student.r2.core.scheduling.service.SchedulingService;
import org.kuali.student.r2.lum.course.service.CourseService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public abstract class CO_AO_RG_ViewHelperServiceImpl extends ViewHelperServiceImpl implements CO_AO_RG_ViewHelperService{

    protected CourseService courseService;
    protected TypeService typeService;
    protected StateService stateService;
    protected SchedulingService schedulingService;
    protected RoomService roomService;

    public ActivityOfferingWrapper convertAOInfoToWrapper(ActivityOfferingInfo aoInfo) throws Exception{

        ActivityOfferingWrapper aoWrapper = new ActivityOfferingWrapper(aoInfo);

        ContextInfo contextInfo = ContextUtils.createDefaultContextInfo();

        StateInfo state = getStateService().getState(aoInfo.getStateKey(), contextInfo);
        aoWrapper.setStateName(state.getName());

        TypeInfo typeInfo = getTypeService().getType(aoInfo.getTypeKey(), contextInfo);
        aoWrapper.setTypeName(typeInfo.getName());

        FormatOfferingInfo fo = getCourseOfferingService().getFormatOffering(aoInfo.getFormatOfferingId(), contextInfo);
        aoWrapper.setFormatOffering(fo);

        OfferingInstructorInfo displayInstructor = ViewHelperUtil.findDisplayInstructor(aoInfo.getInstructors());

        if(displayInstructor != null) {
            aoWrapper.setFirstInstructorDisplayName(displayInstructor.getPersonName());
        }

        //for multiple instructor display
        List<OfferingInstructorInfo> instructorInfos = aoInfo.getInstructors();
        if (instructorInfos != null) {
            for (OfferingInstructorInfo offeringInstructorInfo : instructorInfos) {
                aoWrapper.setInstructorDisplayNames(offeringInstructorInfo.getPersonName(), true);
            }
        }

        //This section is to display either schedule request or actuals. If actuals available, display that instead of request
        if (StringUtils.isNotBlank(aoInfo.getScheduleId())){
            //FIXME: Use display object once we get the TBA with ScheduleComponentDisplay
            /*ScheduleDisplayInfo displayInfo = getSchedulingService().getScheduleDisplay(aoInfo.getScheduleId(),contextInfo);
            if (!displayInfo.getScheduleComponentDisplays().isEmpty()){
                ScheduleComponentDisplay componentDisplay = displayInfo.getScheduleComponentDisplays().get(0);
                updateScheduleToAOWrapperForDisplay(aoWrapper,Boolean.FALSE,componentDisplay.getRoom(),componentDisplay.getTimeSlots().get(0));

            }*/

            ScheduleInfo scheduleInfo = getSchedulingService().getSchedule(aoInfo.getScheduleId(),contextInfo);

            if (!scheduleInfo.getScheduleComponents().isEmpty()){

                boolean appendScheduleRowDisplay = false;

                for (ScheduleComponentInfo scheduleComponentInfo : scheduleInfo.getScheduleComponents()) {

                    String roomId = scheduleComponentInfo.getRoomId();
                    TimeSlotInfo timeSlotInfo =  getSchedulingService().getTimeSlot(scheduleComponentInfo.getTimeSlotIds().get(0),contextInfo);

                    updateScheduleToAOWrapperForDisplay(aoWrapper,scheduleComponentInfo.getIsTBA(),roomId,timeSlotInfo,appendScheduleRowDisplay);

                    if (!appendScheduleRowDisplay){
                        appendScheduleRowDisplay = true;
                    }
                }

            }

        }else{

            List<ScheduleRequestInfo> scheduleRequestInfoList = getSchedulingService().getScheduleRequestsByRefObject(CourseOfferingServiceConstants.REF_OBJECT_URI_ACTIVITY_OFFERING, aoInfo.getId(), contextInfo);

            if (!scheduleRequestInfoList.isEmpty()){

                boolean appendScheduleRowDisplay = false;

                for (ScheduleRequestComponentInfo componentInfo : scheduleRequestInfoList.get(0).getScheduleRequestComponents()) {
                    String roomId = componentInfo.getRoomIds().isEmpty() ? StringUtils.EMPTY : componentInfo.getRoomIds().get(0);
                    TimeSlotInfo timeSlotInfo =  getSchedulingService().getTimeSlot(componentInfo.getTimeSlotIds().get(0),contextInfo);

                    updateScheduleToAOWrapperForDisplay(aoWrapper,componentInfo.getIsTBA(),roomId,timeSlotInfo,appendScheduleRowDisplay);

                    if (!appendScheduleRowDisplay){
                        appendScheduleRowDisplay = true;
                    }
                }
            }

        }

        return aoWrapper;
    }

    private void updateScheduleToAOWrapperForDisplay(ActivityOfferingWrapper aoWrapper, Boolean isTBA,String roomId,TimeSlotInfo timeSlot,boolean append) throws Exception{
        RoomInfo roomInfo = null;
        if (StringUtils.isNotBlank(roomId)){
            roomInfo = getRoomService().getRoom(roomId, ContextUtils.createDefaultContextInfo());
        }
        updateScheduleToAOWrapperForDisplay(aoWrapper,isTBA,roomInfo,timeSlot,append);
    }

    private void updateScheduleToAOWrapperForDisplay(ActivityOfferingWrapper aoWrapper, Boolean isTBA, RoomInfo roomInfo,TimeSlotInfo timeSlot,boolean append) throws Exception{

        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");

        aoWrapper.setTbaDisplayName(isTBA,append);

        if (timeSlot != null) {

            TimeOfDayInfo startTime = timeSlot.getStartTime();
            TimeOfDayInfo endTime = timeSlot.getEndTime();
            List<Integer> days = timeSlot.getWeekdays();

            if (startTime != null && startTime.getMilliSeconds() != null) {
                calendar.setTimeInMillis(startTime.getMilliSeconds());
                aoWrapper.setStartTimeDisplay(format.format(calendar.getTime()),append);
            }

            if (endTime != null && endTime.getMilliSeconds() != null) {
                calendar.setTimeInMillis(endTime.getMilliSeconds());
                aoWrapper.setEndTimeDisplay(format.format(calendar.getTime()),append);
            }

            if (days != null && days.size() > 0) {
                aoWrapper.setDaysDisplayName(getDays(days),append);
            }
        }

        if (roomInfo != null && StringUtils.isNotBlank(roomInfo.getBuildingId())) {
            BuildingInfo buildingInfo = getRoomService().getBuilding(roomInfo.getBuildingId(), ContextUtils.createDefaultContextInfo());
            aoWrapper.setBuildingName(buildingInfo.getName(),append);
            aoWrapper.setRoomName(roomInfo.getRoomCode(),append);
        }
    }

    private String getDays(List<Integer> intList) {

        StringBuilder sb = new StringBuilder();
        if(intList == null) return sb.toString();

        for(Integer d : intList) {
            sb.append(convertIntoDays(d));
        }
        return sb.toString();
    }

    private String convertIntoDays(int day) {
        String dayOfWeek;
        switch (day) {
            case 1:
                dayOfWeek = SchedulingServiceConstants.SUNDAY_TIMESLOT_DAY_CODE;
                break;
            case 2:
                dayOfWeek = SchedulingServiceConstants.MONDAY_TIMESLOT_DAY_CODE;
                break;
            case 3:
                dayOfWeek = SchedulingServiceConstants.TUESDAY_TIMESLOT_DAY_CODE;
                break;
            case 4:
                dayOfWeek = SchedulingServiceConstants.WEDNESDAY_TIMESLOT_DAY_CODE;
                break;
            case 5:
                dayOfWeek = SchedulingServiceConstants.THURSDAY_TIMESLOT_DAY_CODE;
                break;
            case 6:
                dayOfWeek = SchedulingServiceConstants.FRIDAY_TIMESLOT_DAY_CODE;
                break;
            case 7:
                dayOfWeek = SchedulingServiceConstants.SATURDAY_TIMESLOT_DAY_CODE;
                break;
            default:
                dayOfWeek = StringUtils.EMPTY;
        }
        // TODO implement TBA when service stores it.
        return dayOfWeek;
    }

    public TypeService getTypeService() {
        if(typeService == null) {
            typeService = CourseOfferingResourceLoader.loadTypeService();
        }
        return this.typeService;
    }

    public StateService getStateService() {
        if(stateService == null) {
            stateService = CourseOfferingResourceLoader.loadStateService();
        }
        return stateService;
    }

    public CourseOfferingService getCourseOfferingService() {
        return CourseOfferingResourceLoader.loadCourseOfferingService();
    }

    public SchedulingService getSchedulingService() {
        if(schedulingService == null)  {
            schedulingService = CourseOfferingResourceLoader.loadSchedulingService();
        }
        return schedulingService;
    }

    public RoomService getRoomService(){
        if (roomService == null){
            roomService = CourseOfferingResourceLoader.loadRoomService();
        }
        return roomService;
    }

}