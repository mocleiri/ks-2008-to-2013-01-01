package org.kuali.student.enrollment.class2.courseoffering.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateFactory;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.MaintenanceForm;
import org.kuali.student.common.util.UUIDHelper;
import org.kuali.student.enrollment.acal.dto.TermInfo;
import org.kuali.student.enrollment.acal.service.AcademicCalendarService;
import org.kuali.student.enrollment.class2.courseoffering.dto.*;
import org.kuali.student.enrollment.class2.courseoffering.form.ActivityOfferingForm;
import org.kuali.student.enrollment.class2.courseoffering.service.ActivityOfferingMaintainable;
import org.kuali.student.enrollment.class2.courseoffering.service.SeatPoolUtilityService;
import org.kuali.student.enrollment.class2.courseoffering.util.ActivityOfferingConstants;
import org.kuali.student.enrollment.class2.courseoffering.util.CourseOfferingResourceLoader;
import org.kuali.student.enrollment.class2.courseoffering.util.ViewHelperUtil;
import org.kuali.student.enrollment.common.util.ContextBuilder;
import org.kuali.student.enrollment.courseoffering.dto.*;
import org.kuali.student.enrollment.courseoffering.service.CourseOfferingService;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.StatusInfo;
import org.kuali.student.r2.common.dto.TimeOfDayInfo;
import org.kuali.student.r2.common.util.constants.CourseOfferingServiceConstants;
import org.kuali.student.r2.common.util.constants.LprServiceConstants;
import org.kuali.student.r2.core.class1.state.dto.StateInfo;
import org.kuali.student.r2.core.class1.state.service.StateService;
import org.kuali.student.r2.core.class1.type.dto.TypeInfo;
import org.kuali.student.r2.core.class1.type.service.TypeService;
import org.kuali.student.r2.core.constants.PopulationServiceConstants;
import org.kuali.student.r2.core.population.dto.PopulationInfo;
import org.kuali.student.r2.core.population.service.PopulationService;
import org.kuali.student.r2.core.room.dto.BuildingInfo;
import org.kuali.student.r2.core.room.dto.RoomInfo;
import org.kuali.student.r2.core.room.service.RoomService;
import org.kuali.student.r2.core.scheduling.constants.SchedulingServiceConstants;
import org.kuali.student.r2.core.scheduling.dto.*;
import org.kuali.student.r2.core.scheduling.service.SchedulingService;
import org.kuali.student.r2.lum.course.service.CourseService;

import javax.xml.namespace.QName;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ActivityOfferingMaintainableImpl extends MaintainableImpl implements ActivityOfferingMaintainable {

    private transient CourseOfferingService courseOfferingService;
    private transient ContextInfo contextInfo;
    private transient TypeService typeService;
    private transient StateService stateService;
    private transient CourseService courseService;
    private transient AcademicCalendarService academicCalendarService;
    private transient SchedulingService schedulingService;
    private transient RoomService roomService;
    private transient PopulationService populationService;
    private transient SeatPoolUtilityService seatPoolUtilityService = new SeatPoolUtilityServiceImpl();

    @Override
    public void saveDataObject() {
        if(getMaintenanceAction().equals(KRADConstants.MAINTENANCE_EDIT_ACTION)) {
            ActivityOfferingWrapper activityOfferingWrapper = (ActivityOfferingWrapper) getDataObject();
            disassembleInstructorsWrapper(activityOfferingWrapper.getInstructors(), activityOfferingWrapper.getAoInfo());

            List<SeatPoolDefinitionInfo> seatPools = this.getSeatPoolDefinitions(activityOfferingWrapper.getSeatpools());

            seatPoolUtilityService.updateSeatPoolDefinitionList(seatPools, activityOfferingWrapper.getAoInfo().getId(), getContextInfo());

            createOrUpdateScheduleRequests(activityOfferingWrapper);

            try {
                ActivityOfferingInfo activityOfferingInfo = getCourseOfferingService().updateActivityOffering(activityOfferingWrapper.getAoInfo().getId(), activityOfferingWrapper.getAoInfo(), getContextInfo());
                activityOfferingWrapper.setAoInfo(activityOfferingInfo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void createOrUpdateScheduleRequests(ActivityOfferingWrapper wrapper){

        if (wrapper.getScheduleRequestInfo() == null){
            ScheduleRequestInfo scheduleRequest = new ScheduleRequestInfo();
            scheduleRequest.setRefObjectId(wrapper.getAoInfo().getId());
            scheduleRequest.setRefObjectTypeKey(CourseOfferingServiceConstants.REF_OBJECT_URI_ACTIVITY_OFFERING);
            scheduleRequest.setName("Schedule request for " + wrapper.getAoInfo().getCourseOfferingCode() + " - " + wrapper.getAoInfo().getActivityCode());
            scheduleRequest.setTypeKey(SchedulingServiceConstants.SCHEDULE_REQUEST_TYPE_SCHEDULE_REQUEST);
            scheduleRequest.setStateKey(SchedulingServiceConstants.SCHEDULE_REQUEST_STATE_CREATED);
            wrapper.setScheduleRequestInfo(scheduleRequest);
        }

        for (ScheduleWrapper scheduleWrapper : wrapper.getRequestedScheduleComponents()) {
            if (!scheduleWrapper.isAlreadySaved()){
                ScheduleRequestComponentInfo componentInfo = buildScheduleComponentRequest(scheduleWrapper);
                wrapper.getScheduleRequestInfo().getScheduleRequestComponents().add(componentInfo);
            }
        }

        if (StringUtils.isBlank(wrapper.getScheduleRequestInfo().getId())){

            try{
                ScheduleRequestInfo createdScheduleRequestInfo = getSchedulingService().createScheduleRequest(SchedulingServiceConstants.SCHEDULE_REQUEST_TYPE_SCHEDULE_REQUEST,wrapper.getScheduleRequestInfo(),getContextInfo());
                wrapper.setScheduleRequestInfo(createdScheduleRequestInfo);
            } catch (Exception e){
                throw new RuntimeException(e);
            }

        } else {

            try{
               ScheduleRequestInfo updatedScheduleRequestInfo = getSchedulingService().updateScheduleRequest(wrapper.getScheduleRequestInfo().getId(),wrapper.getScheduleRequestInfo(),getContextInfo());
               wrapper.setScheduleRequestInfo(updatedScheduleRequestInfo);
            } catch (Exception e){
                throw new RuntimeException(e);
            }

        }

    }

    private ScheduleRequestComponentInfo buildScheduleComponentRequest(ScheduleWrapper scheduleWrapper){

        ScheduleRequestComponentInfo componentInfo = new ScheduleRequestComponentInfo();
        componentInfo.setId(UUIDHelper.genStringUUID());
        componentInfo.setIsTBA(scheduleWrapper.isTba());

        List<String> room = new ArrayList();
        room.add(scheduleWrapper.getRoom().getId());
        componentInfo.setRoomIds(room);

        componentInfo.setResourceTypeKeys(scheduleWrapper.getFeatures());

        TimeSlotInfo timeSlot = new TimeSlotInfo();
        timeSlot.setTypeKey(SchedulingServiceConstants.TIME_SLOT_TYPE_ACTIVITY_OFFERING);
        timeSlot.setStateKey(SchedulingServiceConstants.TIME_SLOT_STATE_STANDARD_KEY);
        List<Integer> days = buildDaysForDTO(scheduleWrapper.getDays());
        timeSlot.setWeekdays(days);

        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        if (!scheduleWrapper.getStartTime().isEmpty()) {
            try {
                long time = dateFormat.parse(scheduleWrapper.getStartTimeUI()).getTime();
                TimeOfDayInfo timeOfDayInfo = new TimeOfDayInfo();
                timeOfDayInfo.setMilliSeconds(time);
                timeSlot.setStartTime(timeOfDayInfo);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        if (!scheduleWrapper.getEndTime().isEmpty()) {
            try {
                long time = dateFormat.parse(scheduleWrapper.getEndTime() + " " + scheduleWrapper.getEndTimeAMPM()).getTime();
                TimeOfDayInfo timeOfDayInfo = new TimeOfDayInfo();
                timeOfDayInfo.setMilliSeconds(time);
                timeSlot.setEndTime(timeOfDayInfo);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            TimeSlotInfo createdTimeSlot = getSchedulingService().createTimeSlot(SchedulingServiceConstants.TIME_SLOT_TYPE_ACTIVITY_OFFERING,timeSlot,getContextInfo());
            componentInfo.getTimeSlotIds().add(createdTimeSlot.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return componentInfo;
    }


    private List<Integer> buildDaysForDTO(String days){

        List<Integer> weekdays  = new ArrayList();

        if (StringUtils.containsIgnoreCase(days,"M")){
            weekdays.add(Calendar.MONDAY);
        }

        if (StringUtils.containsIgnoreCase(days,"T")){
            weekdays.add(Calendar.TUESDAY);
        }

        if (StringUtils.containsIgnoreCase(days,"W")){
            weekdays.add(Calendar.WEDNESDAY);
        }

        if (StringUtils.containsIgnoreCase(days,"H")){
            weekdays.add(Calendar.THURSDAY);
        }

        if (StringUtils.containsIgnoreCase(days,"F")){
            weekdays.add(Calendar.FRIDAY);
        }

        if (StringUtils.containsIgnoreCase(days,"S")){
            weekdays.add(Calendar.SATURDAY);
        }

        if (StringUtils.containsIgnoreCase(days,"U")){
            weekdays.add(Calendar.SUNDAY);
        }

        return weekdays;
    }

    private String buildDaysForUI(List<Integer> days){

        String returnValue = StringUtils.EMPTY;

        for (Integer day : days) {
            switch (day.intValue()){
                case Calendar.MONDAY:
                   returnValue = returnValue + "M ";
                   break;
                case Calendar.TUESDAY:
                    returnValue = returnValue + "T ";
                   break;
                case Calendar.WEDNESDAY:
                    returnValue = returnValue + "W ";
                   break;
                case Calendar.THURSDAY:
                    returnValue = returnValue + "H ";
                   break;
                case Calendar.FRIDAY:
                    returnValue = returnValue + "F ";
                   break;
                case Calendar.SATURDAY:
                    returnValue = returnValue + "S ";
                   break;
                case Calendar.SUNDAY:
                    returnValue = returnValue + "U ";
                   break;
            }
        }

        return StringUtils.removeEnd(returnValue," ");
    }

    public void addOrUpdateScheduleRequestComponent(ActivityOfferingWrapper wrapper){
        ScheduleWrapper scheduleWrapper = wrapper.getNewScheduleRequest();

        //Add a space between selected days ("MTWHFSU") for informational purpose
        char[] days = scheduleWrapper.getDays().toUpperCase().toCharArray();
        StringBuffer buffer = new StringBuffer();
        for (char day : days) {
            buffer.append(day + " ");
        }

        scheduleWrapper.setDaysUI(StringUtils.stripEnd(buffer.toString()," "));
        scheduleWrapper.setStartTimeUI(scheduleWrapper.getStartTime() + " " + scheduleWrapper.getStartTimeAMPM());
        scheduleWrapper.setEndTimeUI(scheduleWrapper.getEndTime() + " " + scheduleWrapper.getEndTimeAMPM());

        try {
            List<BuildingInfo> buildings = getRoomService().getBuildingsByBuildingCode(scheduleWrapper.getBuildingCode(),getContextInfo());
            scheduleWrapper.setBuilding(buildings.get(0));
            List<RoomInfo> rooms = getRoomService().getRoomsByBuildingAndRoomCode(scheduleWrapper.getBuildingCode(),scheduleWrapper.getRoomCode(),getContextInfo());
            RoomInfo room = rooms.get(0);
            if(room.getRoomUsages() != null && !room.getRoomUsages().isEmpty()){
                scheduleWrapper.setRoomCapacity(room.getRoomUsages().get(0).getHardCapacity());
            }
            scheduleWrapper.setRoom(room);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        wrapper.getRequestedScheduleComponents().add(scheduleWrapper);
        wrapper.setNewScheduleRequest(new ScheduleWrapper());

    }

    public ScheduleWrapper getMatchingActualForRequestedSchedule(ActivityOfferingWrapper activityOfferingWrapper,ScheduleWrapper request){
        for (ScheduleWrapper actual : activityOfferingWrapper.getActualScheduleComponents()){
            if (request.isTba() && actual.isTba() &&
                StringUtils.equals(request.getDaysUI(),actual.getDaysUI()) &&
                StringUtils.equals(request.getStartTimeUI(),actual.getStartTimeUI()) &&
                StringUtils.equals(request.getEndTimeUI(),actual.getEndTimeUI()) &&
                StringUtils.equals(request.getBuildingCode(),actual.getBuildingCode()) &&
                StringUtils.equals(request.getRoomCode(),actual.getRoomCode())){
                return actual;
            }
        }
        return null;
    }

    public void saveAndProcessScheduleRequest(ActivityOfferingWrapper activityOfferingWrapper,ActivityOfferingForm form){

        ScheduleInfo scheduleInfo = activityOfferingWrapper.getScheduleInfo();

        for (ScheduleWrapper scheduleWrapper : activityOfferingWrapper.getBackUpActualComponents()){
             scheduleInfo.getScheduleComponents().remove(scheduleWrapper.getScheduleComponentInfo());
        }

        try {
            if (!activityOfferingWrapper.getBackUpActualComponents().isEmpty()){
                scheduleInfo = getSchedulingService().updateSchedule(scheduleInfo.getId(),scheduleInfo,getContextInfo());
                activityOfferingWrapper.setScheduleInfo(scheduleInfo);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ScheduleRequestInfo requestInfo = activityOfferingWrapper.getScheduleRequestInfo();

        boolean isUpdateRequest = false;
        for (ScheduleWrapper scheduleWrapper : activityOfferingWrapper.getBackUpRequestedComponents()){
            requestInfo.getScheduleRequestComponents().remove(scheduleWrapper.getScheduleComponentInfo());
            isUpdateRequest = true;
        }

        for (ScheduleWrapper scheduleWrapper : activityOfferingWrapper.getRequestedScheduleComponents()){
            ScheduleRequestComponentInfo componentInfo = buildScheduleComponentRequest(scheduleWrapper);
            requestInfo.getScheduleRequestComponents().add(componentInfo);
            isUpdateRequest = true;
        }

        if (isUpdateRequest){
            try{
                requestInfo = getSchedulingService().updateScheduleRequest(requestInfo.getId(),requestInfo,getContextInfo());
                activityOfferingWrapper.setScheduleRequestInfo(requestInfo);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }

        try{
            StatusInfo statusInfo = getCourseOfferingService().scheduleActivityOffering(activityOfferingWrapper.getId(),getContextInfo());
            if (statusInfo.getIsSuccess()){
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, RiceKeyConstants.ERROR_CUSTOM, "Activity Offering has been successfully scheduled");
            } else {
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_ERRORS, RiceKeyConstants.ERROR_CUSTOM,statusInfo.getMessage());
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    private  List<SeatPoolDefinitionInfo> getSeatPoolDeleteList(List<SeatPoolDefinitionInfo> newList, List<SeatPoolDefinitionInfo> oldList){
        List<SeatPoolDefinitionInfo> deleteList = new ArrayList<SeatPoolDefinitionInfo>();
        // loop through old list, add items that don't exist in new list to ret list

        if(oldList == null) return deleteList;
        else{
            for(SeatPoolDefinitionInfo oldPool : oldList){
                if(newList == null){
                   deleteList.add(oldPool);
                } else {
                    if(!seatPoolListContains(newList, oldPool.getId())){
                        deleteList.add(oldPool);
                    }
                }
            }
        }
        return deleteList;
    }

    private boolean seatPoolListContains(List<SeatPoolDefinitionInfo> poolList, String poolId){
        for(SeatPoolDefinitionInfo pool : poolList){
            if(poolId != null && poolId.equalsIgnoreCase(pool.getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public Object retrieveObjectForEditOrCopy(MaintenanceDocument document, Map<String, String> dataObjectKeys) {
        try {
            ActivityOfferingInfo info = getCourseOfferingService().getActivityOffering(dataObjectKeys.get(ActivityOfferingConstants.ACTIVITY_OFFERING_WRAPPER_ID),getContextInfo());
            ActivityOfferingWrapper wrapper = new ActivityOfferingWrapper(info);

            //get the course offering
            CourseOfferingInfo courseOfferingInfo = getCourseOfferingService().getCourseOffering(info.getCourseOfferingId(), getContextInfo());

            // get the format offering
            FormatOfferingInfo formatOfferingInfo = getCourseOfferingService().getFormatOffering(info.getFormatOfferingId(), getContextInfo());
            wrapper.setFormatOffering(formatOfferingInfo);

            // Added for WaitList Tanveer 06/27/2012
            wrapper.setWaitListLevelTypeKey(courseOfferingInfo.getWaitlistLevelTypeKey());
            wrapper.setWaitListTypeKey(courseOfferingInfo.getWaitlistTypeKey());
            wrapper.setHasWaitList(courseOfferingInfo.getHasWaitlist());
            if (!wrapper.getHasWaitList())
                wrapper.setWaitListText("There is no wait list for this offering.");
            if (wrapper.getWaitListLevelTypeKey().equals("Course Offering")){
                wrapper.setWaitListText("This waitlist is managed at the Course Offering level.");
                wrapper.setToolTipText("There is one waitlist for all Activity Offerings");
            }
            if (wrapper.getWaitListLevelTypeKey().equals("Activity Offering")){
                wrapper.setWaitListText("This waitlist is managed at the Activity Offering level.");
                wrapper.setToolTipText("Each Activity Offering has its own wait list.");
            }

            // Set the display string (e.g. 'FALL 2020 (9/26/2020 to 12/26/2020)')
            TermInfo term = getAcademicCalendarService().getTerm(info.getTermId(), getContextInfo());
            if (term != null) {
                wrapper.setTermName(term.getName());
            }
            wrapper.setTermDisplayString(getTermDisplayString(info.getTermId(), term));

            wrapper.setCourseOfferingCode(info.getCourseOfferingCode());
            wrapper.setCourseOfferingTitle(info.getCourseOfferingTitle());

            String sCredits = courseOfferingInfo.getCreditCnt();
            if (sCredits == null) {
                sCredits = "0";
            }
            wrapper.setCredits(sCredits);
            //wrapper.setAbbreviatedActivityCode(info.getActivityCode().toUpperCase().substring(0,3));
            wrapper.setActivityCode(info.getActivityCode());
            wrapper.setAbbreviatedCourseType(getTypeService().getType(info.getTypeKey(), getContextInfo()).getName().toUpperCase().substring(0,3));

            //process instructor effort
            assembleInstructorWrapper(info.getInstructors(), wrapper);


            boolean readOnlyView = Boolean.parseBoolean(dataObjectKeys.get("readOnlyView"));
            wrapper.setReadOnlyView(readOnlyView);

            document.getNewMaintainableObject().setDataObject(wrapper);
            document.getOldMaintainableObject().setDataObject(wrapper);
            document.getDocumentHeader().setDocumentDescription("Edit AO - " + info.getActivityCode());
            StateInfo state = getStateService().getState(wrapper.getAoInfo().getStateKey(), getContextInfo());
            wrapper.setStateName(state.getName());
            TypeInfo typeInfo = getTypeService().getType(wrapper.getAoInfo().getTypeKey(), getContextInfo());
            wrapper.setTypeName(typeInfo.getName());

            // Get/Set SeatPools
            List<SeatPoolDefinitionInfo> seatPoolDefinitionInfoList = getCourseOfferingService().getSeatPoolDefinitionsForActivityOffering(info.getId(), getContextInfo());

            //Sort the seatpools by priority order
            Collections.sort(seatPoolDefinitionInfoList, new Comparator<SeatPoolDefinitionInfo>() {
                @Override
                public int compare(SeatPoolDefinitionInfo sp1, SeatPoolDefinitionInfo sp2) {
                    return sp1.getProcessingPriority().compareTo(sp2.getProcessingPriority());
                }
            });

            List<SeatPoolWrapper> seatPoolWrapperList = new ArrayList<SeatPoolWrapper>();

            for(SeatPoolDefinitionInfo seatPoolDefinitionInfo :  seatPoolDefinitionInfoList){
                SeatPoolWrapper spWrapper = new SeatPoolWrapper();

                PopulationInfo pInfo = getPopulationService().getPopulation(seatPoolDefinitionInfo.getPopulationId(), getContextInfo());
                spWrapper.setSeatPoolPopulation(pInfo);
                spWrapper.setSeatPool(seatPoolDefinitionInfo);
                spWrapper.setId(seatPoolDefinitionInfo.getId());
                seatPoolWrapperList.add(spWrapper);
            }
            wrapper.setSeatpools(seatPoolWrapperList);

            loadScheduleRequests(wrapper);
            loadScheduleActuals(wrapper);

            return wrapper;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method loads the schedule requests/components.
     * For M5, we're going to have only one schedule request.
     *
     * @param wrapper  ActivityOfferingWrapper
     */
    protected void loadScheduleRequests(ActivityOfferingWrapper wrapper){

        try {
            List<ScheduleRequestInfo> requestInfos = getSchedulingService().getScheduleRequestsByRefObject(CourseOfferingServiceConstants.REF_OBJECT_URI_ACTIVITY_OFFERING,wrapper.getId(),getContextInfo());

            if (requestInfos.size() > 1){  // For M5, we should have only one Schedule Request
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, RiceKeyConstants.ERROR_CUSTOM,"Multiple schedule requests not supported in M5 implementation");
                return;
            }

            if (!requestInfos.isEmpty()){

                ScheduleRequestInfo scheduleRequestInfo = requestInfos.get(0);
                wrapper.setScheduleRequestInfo(scheduleRequestInfo);

                for (ScheduleRequestComponentInfo componentInfo : scheduleRequestInfo.getScheduleRequestComponents()) {
                    ScheduleWrapper scheduleWrapper = new ScheduleWrapper(componentInfo);
                    scheduleWrapper.setTba(componentInfo.getIsTBA());

                    List<TimeSlotInfo> timeSlotInfos = getSchedulingService().getTimeSlotsByIds(componentInfo.getTimeSlotIds(),getContextInfo());

                    if (!timeSlotInfos.isEmpty()){
                        scheduleWrapper.setTimeSlot(timeSlotInfos.get(0));

                        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                        Date timeForDisplay = new Date(scheduleWrapper.getTimeSlot().getStartTime().getMilliSeconds());
                        scheduleWrapper.setStartTimeUI(dateFormat.format(timeForDisplay));

                        timeForDisplay = new Date(scheduleWrapper.getTimeSlot().getEndTime().getMilliSeconds());
                        scheduleWrapper.setEndTimeUI(dateFormat.format(timeForDisplay));

                        scheduleWrapper.setDaysUI(buildDaysForUI(scheduleWrapper.getTimeSlot().getWeekdays()));
                    }

                    if (!componentInfo.getRoomIds().isEmpty()){

                        RoomInfo room = getRoomService().getRoom(componentInfo.getRoomIds().get(0),getContextInfo());

                        scheduleWrapper.setRoom(room);
                        scheduleWrapper.setRoomCode(room.getRoomCode());

                        if (!room.getRoomUsages().isEmpty()){
                            scheduleWrapper.setRoomCapacity(room.getRoomUsages().get(0).getHardCapacity());
                        }

                        BuildingInfo buildingInfo = getRoomService().getBuilding(room.getBuildingId(),getContextInfo());
                        scheduleWrapper.setBuilding(buildingInfo);
                        scheduleWrapper.setBuildingCode(buildingInfo.getBuildingCode());
                    }

                    wrapper.getRequestedScheduleComponents().add(scheduleWrapper);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void loadScheduleActuals(ActivityOfferingWrapper wrapper){

        if(wrapper.getAoInfo().getScheduleId() == null) return;
        try {
            ScheduleInfo scheduleInfo = getSchedulingService().getSchedule(wrapper.getAoInfo().getScheduleId(),getContextInfo());
            wrapper.setScheduleInfo(scheduleInfo);

            for (ScheduleComponentInfo componentInfo : scheduleInfo.getScheduleComponents()) {
                ScheduleWrapper scheduleWrapper = new ScheduleWrapper(componentInfo);
                scheduleWrapper.setTba(componentInfo.getIsTBA());

                List<TimeSlotInfo> timeSlotInfos = getSchedulingService().getTimeSlotsByIds(componentInfo.getTimeSlotIds(),getContextInfo());

                if (!timeSlotInfos.isEmpty()){
                    scheduleWrapper.setTimeSlot(timeSlotInfos.get(0));

                    DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    Date timeForDisplay = new Date(scheduleWrapper.getTimeSlot().getStartTime().getMilliSeconds());
                    scheduleWrapper.setStartTimeUI(dateFormat.format(timeForDisplay));

                    timeForDisplay = new Date(scheduleWrapper.getTimeSlot().getEndTime().getMilliSeconds());
                    scheduleWrapper.setEndTimeUI(dateFormat.format(timeForDisplay));

                    scheduleWrapper.setDaysUI(buildDaysForUI(scheduleWrapper.getTimeSlot().getWeekdays()));
                }


                if (StringUtils.isNotBlank(componentInfo.getRoomId())){

                    RoomInfo room = getRoomService().getRoom(componentInfo.getRoomId(),getContextInfo());

                    scheduleWrapper.setRoom(room);
                    scheduleWrapper.setRoomCode(room.getRoomCode());

                    if (!room.getRoomUsages().isEmpty()){
                        scheduleWrapper.setRoomCapacity(room.getRoomUsages().get(0).getHardCapacity());
                    }

                    BuildingInfo buildingInfo = getRoomService().getBuilding(room.getBuildingId(),getContextInfo());
                    scheduleWrapper.setBuilding(buildingInfo);
                    scheduleWrapper.setBuildingCode(buildingInfo.getBuildingCode());
                }

                wrapper.getActualScheduleComponents().add(scheduleWrapper);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * unwrap seatPoolWrapper. If the seatPoolWrapper is null or contains no seatPools, return null
     *
     * @param seatPoolWrappers
     * @return
     */
    private List<SeatPoolDefinitionInfo> getSeatPoolDefinitions(List<SeatPoolWrapper> seatPoolWrappers)   {

        List<SeatPoolDefinitionInfo> spRet = new ArrayList<SeatPoolDefinitionInfo>();

        if(seatPoolWrappers != null){
            for(SeatPoolWrapper seatPoolWrapper : seatPoolWrappers){
                SeatPoolDefinitionInfo seatPool = seatPoolWrapper.getSeatPool();
                seatPool.setPopulationId(seatPoolWrapper.getSeatPoolPopulation().getId());
                spRet.add(seatPool);
            }
        }

        return spRet;
    }

    private QueryByCriteria buildQueryByCriteria(Map<String, String> fieldValues){
        String aoId = fieldValues.get(ActivityOfferingConstants.ACTIVITYOFFERING_ID);

        List<Predicate> predicates = new ArrayList<Predicate>();
        if (StringUtils.isNotBlank(aoId)) {
            predicates.add(PredicateFactory.equalIgnoreCase("id", aoId));
        }

        QueryByCriteria.Builder qbcBuilder = QueryByCriteria.Builder.create();
        qbcBuilder.setPredicates(predicates.toArray(new Predicate[predicates.size()]));
        QueryByCriteria qbc = qbcBuilder.build();

        return qbc;
    }

    private String getTermDisplayString(String termId, TermInfo term) {
        // Return Term as String display like 'FALL 2020 (9/26/2020-12/26/2020)'
        StringBuilder    stringBuilder = new StringBuilder();
        Formatter        formatter     = new Formatter(stringBuilder, Locale.US);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
        String           displayString = termId; // use termId as a default.
        if (term != null) {
            String           startDate = dateFormatter.format(term.getStartDate());
            String           endDate   = dateFormatter.format(term.getEndDate());
            String           termType  = term.getName();
            formatter.format("%s (%s to %s)", termType, startDate, endDate);
            displayString = stringBuilder.toString();
        }
        return displayString;
    }

    private void assembleInstructorWrapper(List<OfferingInstructorInfo> instructors, ActivityOfferingWrapper wrapper){
        if(instructors!= null && !instructors.isEmpty()){
            for(OfferingInstructorInfo instructor : instructors){
                OfferingInstructorWrapper instructorWrapper = new OfferingInstructorWrapper(instructor);
                if(instructor.getPercentageEffort() != null){
                    instructorWrapper.setsEffort(Integer.toString(instructor.getPercentageEffort().intValue()));
                }
                wrapper.getInstructors().add(instructorWrapper);
            }
        }
    }

    private void disassembleInstructorsWrapper(List<OfferingInstructorWrapper> instructors, ActivityOfferingInfo aoInfo){
        aoInfo.setInstructors(new ArrayList<OfferingInstructorInfo>());
        if(instructors!= null && !instructors.isEmpty()){
            for(OfferingInstructorWrapper instructor : instructors){
                aoInfo.getInstructors().add(disassembleInstructorWrapper(instructor));
            }
        }
    }

    private OfferingInstructorInfo disassembleInstructorWrapper(OfferingInstructorWrapper instructor){
        OfferingInstructorInfo instructorInfo = new OfferingInstructorInfo(instructor.getOfferingInstructorInfo());
        if(!StringUtils.isBlank(instructor.getsEffort())){
            instructorInfo.setPercentageEffort(new Float(instructor.getsEffort()));
        }


        if(StringUtils.isBlank(instructorInfo.getStateKey())) {
            try {
                StateInfo state = getStateService().getState(LprServiceConstants.TENTATIVE_STATE_KEY, getContextInfo());
                instructorInfo.setStateKey(state.getKey());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return instructorInfo;
    }

    @Override
    public void processAfterNew(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        ActivityOfferingWrapper wrapper = (ActivityOfferingWrapper)document.getNewMaintainableObject().getDataObject();
        document.getDocumentHeader().setDocumentDescription("Activity Offering");
        try {
            StateInfo state = getStateService().getState(wrapper.getAoInfo().getStateKey(), getContextInfo());
            wrapper.setStateName(state.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void processAfterAddLine(View view, CollectionGroup collectionGroup, Object model, Object addLine) {
        super.processAfterAddLine(view, collectionGroup, model, addLine);

        if (addLine instanceof ScheduleComponentWrapper) {
            ScheduleComponentWrapper scheduleComponentWrapper = (ScheduleComponentWrapper)addLine;
            if ("1".equals(scheduleComponentWrapper.getAddDaysSpecifiedBoolean())) {
                if (null != scheduleComponentWrapper.getAddWeekDayOptions()) {
                    List<String> weekDayLabels = Arrays.asList("Su ","M ","T ","W ","Th ","F ","Sa ");
                    StringBuilder weekDays = new StringBuilder();
                    for (Integer day : scheduleComponentWrapper.getAddWeekDayOptions()) {
                        weekDays.append(weekDayLabels.get(day));
                    }
                    scheduleComponentWrapper.setWeekDays(weekDays.toString());
                }
            }
            else {
                scheduleComponentWrapper.setWeekDays("To Be Announced");
            }
            if (null != scheduleComponentWrapper.getAddRoomResources()) {
                StringBuilder resources = new StringBuilder();
                for (String resource : scheduleComponentWrapper.getAddRoomResources()) {
                    if (resources.length() > 0) {
                        resources.append(", ");
                    }
                    resources.append(resource);
                }
                scheduleComponentWrapper.setRoomFeatures(resources.toString());
            }
        }
        else if(addLine instanceof OfferingInstructorWrapper) {
            // set the person name if it's null, in the case of user-input personell id
            OfferingInstructorWrapper instructor = (OfferingInstructorWrapper) addLine;
            if (instructor.getOfferingInstructorInfo().getPersonName() == null && instructor.getOfferingInstructorInfo().getPersonId() != null) {
                List<Person> personList = ViewHelperUtil.getInstructorByPersonId(instructor.getOfferingInstructorInfo().getPersonId());
                if (personList.size() == 1) {
                    instructor.getOfferingInstructorInfo().setPersonName(personList.get(0).getName());
                }
            }
        }
    }

    @Override
    protected boolean performAddLineValidation(View view, CollectionGroup collectionGroup, Object model, Object addLine) {
        if (addLine instanceof OfferingInstructorWrapper){   //Personnel
            OfferingInstructorWrapper instructor = (OfferingInstructorWrapper) addLine;

            //check duplication
            MaintenanceForm form = (MaintenanceForm)model;
            ActivityOfferingWrapper activityOfferingWrapper = (ActivityOfferingWrapper)form.getDocument().getNewMaintainableObject().getDataObject();
            List<OfferingInstructorWrapper> instructors = activityOfferingWrapper.getInstructors();
            if(instructors != null && !instructors.isEmpty()){
                for(OfferingInstructorWrapper thisInst : instructors){
                    if(instructor.getOfferingInstructorInfo().getPersonId().equals(thisInst.getOfferingInstructorInfo().getPersonId())){
                        GlobalVariables.getMessageMap().putErrorForSectionId("ao-personnelgroup", ActivityOfferingConstants.MSG_ERROR_INSTRUCTOR_DUPLICATE, instructor.getOfferingInstructorInfo().getPersonId());
                        return false;
                    }
                }
            }

            //validate ID
            List<Person> lstPerson = ViewHelperUtil.getInstructorByPersonId(instructor.getOfferingInstructorInfo().getPersonId());
            if(lstPerson == null || lstPerson.isEmpty()){
                GlobalVariables.getMessageMap().putErrorForSectionId("ao-personnelgroup", ActivityOfferingConstants.MSG_ERROR_INSTRUCTOR_NOTFOUND, instructor.getOfferingInstructorInfo().getPersonId());
                return false;
            }
        }
        else if (addLine instanceof SeatPoolWrapper){   //Seat Pool
            SeatPoolWrapper seatPool = (SeatPoolWrapper) addLine;
            //check duplication
            MaintenanceForm form = (MaintenanceForm)model;
            ActivityOfferingWrapper activityOfferingWrapper = (ActivityOfferingWrapper)form.getDocument().getNewMaintainableObject().getDataObject();
            List<SeatPoolWrapper> pools = activityOfferingWrapper.getSeatpools();
            if(pools != null && !pools.isEmpty()){
                for (SeatPoolWrapper pool : pools ) {
                    if (seatPool.getSeatPoolPopulation().getId().equals( pool.getSeatPoolPopulation().getId())) {
                        GlobalVariables.getMessageMap().putErrorForSectionId("ao-seatpoolgroup", ActivityOfferingConstants.MSG_ERROR_SEATPOOL_DUPLICATE, pool.getSeatPoolPopulation().getName());
                        return false;
                    }
                }
            }
        }
        return super.performAddLineValidation(view, collectionGroup, model, addLine);
    }

    @Override
    protected void processBeforeAddLine(View view, CollectionGroup collectionGroup, Object model, Object addLine) {
        if (addLine instanceof OfferingInstructorWrapper){
            OfferingInstructorWrapper instructor = (OfferingInstructorWrapper) addLine;
            instructor.setOfferingInstructorInfo(disassembleInstructorWrapper(instructor));
        }
    }

    public ContextInfo getContextInfo() {
        if (null == contextInfo) {
            contextInfo = ContextBuilder.loadContextInfo();
        }

        return contextInfo;
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

    protected CourseOfferingService getCourseOfferingService() {
        if (courseOfferingService == null) {
            courseOfferingService = CourseOfferingResourceLoader.loadCourseOfferingService();
        }
        return courseOfferingService;
    }

    private CourseService getCourseService() {
        if(courseService == null) {
            courseService = CourseOfferingResourceLoader.loadCourseService();
        }

        return courseService;
    }

    private AcademicCalendarService getAcademicCalendarService() {
        if(academicCalendarService == null) {
            academicCalendarService = CourseOfferingResourceLoader.loadAcademicCalendarService();
        }

        return academicCalendarService;
    }

    private PopulationService getPopulationService() {
        if(populationService == null) {
            populationService = (PopulationService) GlobalResourceLoader.getService(new QName(PopulationServiceConstants.NAMESPACE, "PopulationService"));
        }
        return populationService;
    }

    public SchedulingService getSchedulingService() {
        if(schedulingService == null){
            schedulingService =  CourseOfferingResourceLoader.loadSchedulingService();
        }
        return schedulingService;
    }

    public RoomService getRoomService(){
        if (roomService == null){
            roomService = CourseOfferingResourceLoader.loadRoomService();
        }
        return roomService;
    }

    /**
     * Mock data that was being used in the Delivery Logistics section of Edit Activity Offering
     *
     * @param userEnteredCode
     * @return
     */
    public List<String> getBuildingsCodesForSuggest(String userEnteredCode) {
        //TODO - make this an actual search based on user-entered text
        List<String> buildingCodes = new ArrayList<String>();
        buildingCodes.add(userEnteredCode+"Dog");
        buildingCodes.add(userEnteredCode+"Emu");
        buildingCodes.add(userEnteredCode+"Fox");
        return buildingCodes;
    }

    /**
     * Mock data that was being used in the Delivery Logistics section of Edit Activity Offering
     *
     * @param buildingCode
     * @return
     */
    public List<String> getRoomNumbersForSuggest(String buildingCode) {
        //TODO - make this an actual search based on the building & user-entered text
        List<String> roomNumbers = new ArrayList<String>();
        roomNumbers.add("101");
        roomNumbers.add("202");
        roomNumbers.add("303");
        return roomNumbers;
    }

}