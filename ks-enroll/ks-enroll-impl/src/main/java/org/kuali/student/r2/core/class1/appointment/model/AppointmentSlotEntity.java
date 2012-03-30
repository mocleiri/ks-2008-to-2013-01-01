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
 * Created by Charles on 3/8/12
 */
package org.kuali.student.r2.core.class1.appointment.model;

import org.kuali.student.r2.common.dto.AttributeInfo;
import org.kuali.student.r2.common.entity.MetaEntity;
import org.kuali.student.r2.common.infc.Attribute;
import org.kuali.student.r2.core.appointment.dto.AppointmentSlotInfo;
import org.kuali.student.r2.core.appointment.infc.AppointmentSlot;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class //TODO ...
 *
 * @author Kuali Student Team
 */
@Entity
@Table(name = "KSEN_APPT_SLOT")
public class AppointmentSlotEntity extends MetaEntity {
    // These refer to columns unique to AppointmentSlotEntity (i.e., not inherited)
    // We use AppointmentWindowEntity because ORMs recognize foreign keys not by strings, but by objects that
    // represent a row in that table.  The ORM handles looking up the foreign key.
    @ManyToOne
    @JoinColumn(name = "APPT_WINDOW_ID")
    private AppointmentWindowEntity apptWinEntity;

    @Column(name = "START_DT")
    private Date startDate;  // When registration starts (for individual) month/day/year

    @Column(name = "END_DT")
    private Date endDate;    // When registration ends (for individual) month/day/year

    // --------------------------------------------------
    // These instance variables are not inherited, so they need to be explicitly put here
    @Column(name = "APPT_SLOT_TYPE")
    private String apptSlotType;

    @Column(name = "APPT_SLOT_STATE")
    private String apptSlotState;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<AppointmentSlotAttributeEntity> attributes = new ArrayList<AppointmentSlotAttributeEntity>();
    // -- Note: version number, create time, create id, update time, update id should be
    // inherited via MetaEntity.  id and object id are inherited as well

    // ==================== METHODS =======================
    public AppointmentSlotEntity() {

    }

    public AppointmentSlotEntity(AppointmentSlot apptSlot) {
        super(apptSlot);
        this.setId(apptSlot.getId()); // obj_id set separately via inheritance, id needs to explicitly set...(why?)
        this.fromDto(apptSlot);
    }

    public AppointmentWindowEntity getApptWinEntity() {
        return apptWinEntity;
    }

    public void setApptWinEntity(AppointmentWindowEntity apptWinEntity) {
        this.apptWinEntity = apptWinEntity;
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

    public String getApptSlotType() {
        return apptSlotType;
    }

    public void setApptSlotType(String apptSlotType) {
        this.apptSlotType = apptSlotType;
    }

    public String getApptSlotState() {
        return apptSlotState;
    }

    public void setApptSlotState(String apptSlotState) {
        this.apptSlotState = apptSlotState;
    }

    public void setAttributes(List<AppointmentSlotAttributeEntity> attributes) {
        this.attributes = attributes;
    }

    public List<AppointmentSlotAttributeEntity> getAttributes() {
        return attributes;
    }

    public void fromDto(AppointmentSlot apptSlot) {
        // AppointmentSlot specific fields set below
        this.setStartDate(apptSlot.getStartDate());
        this.setEndDate(apptSlot.getEndDate());
        // Type/state are in every entity though, in this case, named to AppointmentSlot
        this.setApptSlotType(apptSlot.getTypeKey());
        this.setApptSlotState(apptSlot.getStateKey());
        // Add attributes individually
        this.setAttributes(new ArrayList<AppointmentSlotAttributeEntity>());
        if (null != apptSlot.getAttributes()) {
            for (Attribute att : apptSlot.getAttributes()) {
                this.getAttributes().add(new AppointmentSlotAttributeEntity(att, this));
            }
        }
        // Note: apptWinEntity can't be set from apptSlot which only contains the
        // id (which is a string) for AppointmentWindow.  When constructing an AppointmentSlotEntity
        // in AssignmentServiceImpl, one needs to use the appointmentWindowDao to "find" (call the find
        // method) to get the AppointmentWindowEntity and call setApptWinEntity to set the value
        // separately
    }

    // Converts AppoinmentSlotEntity to AppointmentSlotInfo
    public AppointmentSlotInfo toDto() {
        AppointmentSlotInfo slotInfo = new AppointmentSlotInfo();
        // Data specific to AppointmentSlotInfo--all are not null in the DB
        slotInfo.setStartDate(getStartDate());
        slotInfo.setEndDate(getEndDate());
        slotInfo.setAppointmentWindowId(getApptWinEntity().getId());
        // -------------------------------------------------
        // Stuff that is updated for nearly all entities
        slotInfo.setId(getId()); // id is assumed not null
        slotInfo.setTypeKey(getApptSlotType()); // type is assumed not null
        slotInfo.setStateKey(getApptSlotState()); // state is assumed not null
        slotInfo.setMeta(super.toDTO());
        List<AttributeInfo> attrs = new ArrayList<AttributeInfo>();
        for (AppointmentSlotAttributeEntity att : getAttributes()) {
            AttributeInfo attInfo = att.toDto();
            attrs.add(attInfo);
        }
        slotInfo.setAttributes(attrs);
        return slotInfo;
    }
}
