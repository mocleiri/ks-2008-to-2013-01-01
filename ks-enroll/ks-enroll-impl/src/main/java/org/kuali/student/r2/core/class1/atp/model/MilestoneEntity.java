package org.kuali.student.r2.core.class1.atp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.kuali.student.common.entity.KSEntityConstants;
import org.kuali.student.r2.common.dto.AttributeInfo;
import org.kuali.student.r2.common.entity.MetaEntity;
import org.kuali.student.r2.common.infc.Attribute;
import org.kuali.student.r2.common.util.RichTextHelper;
import org.kuali.student.r2.core.atp.dto.MilestoneInfo;
import org.kuali.student.r2.core.atp.infc.Milestone;
import org.kuali.student.r2.core.class1.atp.service.impl.DateUtil;

@Entity
@Table(name = "KSEN_MSTONE")
public class MilestoneEntity extends MetaEntity {

    @Column(name = "NAME")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DT", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DT", nullable = true)
    private Date endDate;

    @Column(name = "MSTONE_TYPE", nullable = false)
    private String atpType;

    @Column(name = "MSTONE_STATE", nullable = false)
    private String atpState;

    @Column(name = "IS_ALL_DAY", nullable = false)
    private String isAllDay;

    @Column(name = "IS_INSTRCT_DAY", nullable = false)
    private String isInstructionalDay;

    @Column(name = "IS_DATE_RANGE", nullable = false)
    private String isDateRange;

    @Column(name = "DESCR_FORMATTED", length = KSEntityConstants.EXTRA_LONG_TEXT_LENGTH)
    private String descrFormatted;

    @Column(name = "DESCR_PLAIN", length = KSEntityConstants.EXTRA_LONG_TEXT_LENGTH, nullable = false)
    private String descrPlain;

    @Column(name = "IS_RELATIVE", nullable = false)
    private String isRelative;

    @Column(name = "RELATIVE_ANCHOR_MSTONE_ID")
    private String relativeAnchorMilestoneId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<MilestoneAttributeEntity> attributes;

    public MilestoneEntity() {
    }

    public MilestoneEntity(Milestone milestone) {
        super(milestone);
        this.setId(milestone.getId());
        this.atpType = milestone.getTypeKey();
        this.fromDto(milestone);
    }

    private boolean defaultFalse(Boolean b) {
        if (b == null) {
            return false;
        }
        return b.booleanValue();
    }

    private String toYN(Boolean flag) {
        if (flag == null) {
            return null;
        }
        if (flag) {
            return "Y";
        }
        return "N";
    }

    private Boolean toBoolean(String flag) {
        if (flag == null) {
            return false;
        }
        if (flag.equals("Y")) {
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAtpType() {
        return atpType;
    }

    public void setAtpType(String atpType) {
        this.atpType = atpType;
    }

    public String getAtpState() {
        return atpState;
    }

    public void setAtpState(String atpState) {
        this.atpState = atpState;
    }

    public boolean isAllDay() {
        return toBoolean(isAllDay);
    }

    public void setAllDay(boolean isAllDay) {
        this.isAllDay = toYN(isAllDay);
    }

    public boolean isDateRange() {
        return toBoolean(isDateRange);
    }

    public void setDateRange(boolean isDateRange) {
        this.isDateRange = toYN(isDateRange);
    }

    public boolean isRelative() {
        return toBoolean(isRelative);
    }

    public void setRelative(boolean relative) {
        isRelative = toYN(relative);
    }

    public String getRelativeAnchorMilestoneId() {
        return relativeAnchorMilestoneId;
    }

    public void setRelativeAnchorMilestoneId(String relativeAnchorMilestoneId) {
        this.relativeAnchorMilestoneId = relativeAnchorMilestoneId;
    }

    public void setAttributes(List<MilestoneAttributeEntity> attributes) {
        this.attributes = attributes;
    }

    public List<MilestoneAttributeEntity> getAttributes() {
        return attributes;
    }

    public String getDescrFormatted() {
        return descrFormatted;
    }

    public void setDescrFormatted(String formatted) {
        this.descrFormatted = formatted;
    }

    public String getDescrPlain() {
        return descrPlain;
    }

    public void setDescrPlain(String plain) {
        this.descrPlain = plain;
    }

    public boolean getIsInstructionalDay() {
        return toBoolean(isInstructionalDay);
    }

    public void setIsInstructionalDay(boolean isInstructionalDay) {
        this.isInstructionalDay = toYN(isInstructionalDay);
    }
    

    public void fromDto(Milestone milestone) {
        boolean allDay = defaultFalse(milestone.getIsAllDay());
        this.setAllDay(allDay);
        this.setIsInstructionalDay(defaultFalse(milestone.getIsInstructionalDay()));
        boolean dateRange = defaultFalse(milestone.getIsDateRange());
        this.setDateRange(dateRange);
        this.setRelative(defaultFalse(milestone.getIsRelative()));
        this.relativeAnchorMilestoneId = milestone.getRelativeAnchorMilestoneId();
        this.atpState = milestone.getStateKey();
        this.name = milestone.getName();
        if (milestone.getDescr() != null) {
            this.descrFormatted = milestone.getDescr().getFormatted();
            this.descrPlain = milestone.getDescr().getPlain();
        } else {
            this.descrFormatted = null;
            this.descrPlain = null;
        }
//      For explanation See https://wiki.kuali.org/display/STUDENT/Storing+and+Querying+Milestone+Dates
        this.startDate = DateUtil.startOfDayfIsAllDay (allDay, milestone.getStartDate());
        this.endDate = DateUtil.endOfDayIfIsAllDay (allDay, DateUtil.nullIfNotDateRange(dateRange, milestone.getEndDate()));
        this.attributes = new ArrayList<MilestoneAttributeEntity>();
        if (null != milestone.getAttributes()) {
            for (Attribute att : milestone.getAttributes()) {
                this.attributes.add(new MilestoneAttributeEntity(att, this));
            }
        }
    }

    public MilestoneInfo toDto() {
        MilestoneInfo info = new MilestoneInfo();
        info.setId(getId());
        info.setName(getName());
        info.setTypeKey(atpType);
        info.setStateKey(atpState);
        info.setStartDate(getStartDate());
        info.setEndDate(getEndDate());
        info.setIsAllDay(isAllDay());
        info.setIsDateRange(isDateRange());
        info.setIsInstructionalDay(getIsInstructionalDay());
        info.setIsRelative(isRelative());
        info.setRelativeAnchorMilestoneId(relativeAnchorMilestoneId);
        info.setMeta(super.toDTO());
        info.setDescr(new RichTextHelper().toRichTextInfo(descrPlain, descrFormatted));
        if (attributes != null) {
            for (MilestoneAttributeEntity att : getAttributes()) {
                AttributeInfo attInfo = att.toDto();
                info.getAttributes().add(attInfo);
            }
        }

        return info;
    }
}
