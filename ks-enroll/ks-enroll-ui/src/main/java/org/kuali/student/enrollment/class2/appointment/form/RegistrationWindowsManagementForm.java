package org.kuali.student.enrollment.class2.appointment.form;

import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.student.enrollment.acal.dto.KeyDateInfo;
import org.kuali.student.enrollment.acal.dto.TermInfo;
import org.kuali.student.r2.core.appointment.dto.AppointmentWindowInfo;
import org.kuali.student.enrollment.class2.appointment.dto.AppointmentWindowWrapper;

import java.util.ArrayList;
import java.util.List;

public class RegistrationWindowsManagementForm extends UifFormBase {
    private TermInfo termInfo;
    private String termType;
    private String termYear;
    private String periodName;
    private List<KeyDateInfo> periodMilestones;
    private List<AppointmentWindowWrapper> appointmentWindows;
//    private Map<String, List<AppointmentWindowInfo>> periodAndWindowsMap;

    public RegistrationWindowsManagementForm(){
        termInfo = new TermInfo();
        periodMilestones = new ArrayList<KeyDateInfo>();
        appointmentWindows = new ArrayList<AppointmentWindowWrapper>();
//        periodAndWindowsMap = new HashMap<String, List<AppointmentWindowInfo>>();
    }

    public TermInfo getTermInfo(){
        return termInfo;
    }

    public void setTermInfo(TermInfo termInfo) {
        this.termInfo = termInfo;
    }

    public String getTermType(){
        return termType;
    }

    public void setTermType(String termType) {
        this.termType = termType;
    }

    public String getTermYear(){
        return termYear;
    }

    public void setTermYear(String termYear) {
        this.termYear = termYear;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public List<KeyDateInfo> getPeriodMilestones() {
        return periodMilestones;
    }

    public void setPeriodMilestones(List<KeyDateInfo> periodMilestones){
        this.periodMilestones = periodMilestones;
    }

    public List<AppointmentWindowWrapper> getAppointmentWindows(){
        return appointmentWindows;
    }

    public void setAppointmentWindows(List<AppointmentWindowWrapper> appointmentWindows){
        this.appointmentWindows = appointmentWindows;
    }
}
