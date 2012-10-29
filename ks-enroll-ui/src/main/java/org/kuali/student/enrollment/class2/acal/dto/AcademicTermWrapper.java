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
package org.kuali.student.enrollment.class2.acal.dto;

import org.apache.commons.lang.StringUtils;
import org.kuali.student.r2.common.class1.type.dto.TypeInfo;
import org.kuali.student.r2.common.util.constants.AcademicCalendarServiceConstants;
import org.kuali.student.enrollment.acal.dto.TermInfo;
import org.kuali.student.r2.common.dto.RichTextInfo;
import org.kuali.student.r2.core.constants.AtpServiceConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Wrapper class for <code>TermInfo</code> dto.
 *
 * @author Kuali Student Team
 */
public class AcademicTermWrapper {

    private TermInfo termInfo;

    private String name;
    private int instructionalDays;
    private String termType;
    private Date startDate;
    private Date endDate;

    private String termNameForUI;

    private TypeInfo typeInfo;

    private List<KeyDatesGroupWrapper> keyDatesGroupWrappers;
    private List<KeyDateWrapper> keyDatesToDeleteOnSave;

    /**
     * This constructor sets all the default values.
     */
    public AcademicTermWrapper(){
        keyDatesGroupWrappers = new ArrayList();
        keyDatesToDeleteOnSave = new ArrayList<KeyDateWrapper>();
        termInfo = new TermInfo();
        termInfo.setStateKey(AtpServiceConstants.ATP_DRAFT_STATE_KEY);
        RichTextInfo desc = new RichTextInfo();
        desc.setPlain("Test");
        termInfo.setDescr(desc);
    }

    public AcademicTermWrapper(TermInfo termInfo,boolean isCopy){

        this.startDate = termInfo.getStartDate();
        this.endDate = termInfo.getEndDate();
        this.termType = termInfo.getTypeKey();
        this.keyDatesGroupWrappers = new ArrayList();
        this.keyDatesToDeleteOnSave = new ArrayList<KeyDateWrapper>();

        if (isCopy){
            setTermInfo(new TermInfo());
            RichTextInfo desc = new RichTextInfo();
            desc.setPlain(termInfo.getTypeKey());
            getTermInfo().setDescr(desc);
            getTermInfo().setStateKey(AtpServiceConstants.ATP_DRAFT_STATE_KEY);
        } else{
           setTermInfo(termInfo);
           this.name = termInfo.getName();
        }

    }

    /**
     * See setName()
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for display purpose. This would display a term as <p>"Spring 2013"</p>.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * See setTermType()
     *
     * @return
     */
    public String getTermType() {
        return termType;
    }

    /**
     * Sets the term type from the drop down list. This is being
     * used only at the add line which allows user to pick an available term type
     *
     * @param termType
     */
    public void setTermType(String termType) {
        this.termType = termType;
    }

    /**
     * Returns the term start date
     *
     * @return
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * sets the term start date.
     *
     * @param startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * See setEndDate()
     *
     * @return
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the term end date.
     *
     * @param endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * See setTermInfo()
     *
     * @return
     */
    public TermInfo getTermInfo() {
        return termInfo;
    }

    /**
     * <code>TermInfo</code> DTO associated with a term.
     * @param termInfo
     */
    public void setTermInfo(TermInfo termInfo) {
        this.termInfo = termInfo;
    }

    /**
     * See setInstructionalDays()
     *
     * @return
     */
    public int getInstructionalDays() {
        return instructionalDays;
    }

    /**
     * Sets the number of instructional days for ui display. It's just for display purpose and this is being
     * calculated at the service.
     *
     * @param instructionalDays
     */
    public void setInstructionalDays(int instructionalDays) {
        this.instructionalDays = instructionalDays;
    }

    public String getTermNameForUI() {
        return termNameForUI;
    }

    /**
     * Sets the term name for the ui display purpose
     *
     * @param termNameForUI
     */
    public void setTermNameForUI(String termNameForUI) {
        this.termNameForUI = termNameForUI;
    }

    /**
     * See setKeyDatesGroupWrappers()
     *
     * @return
     */
    public List<KeyDatesGroupWrapper> getKeyDatesGroupWrappers() {
        return keyDatesGroupWrappers;
    }

    /**
     * Collection of KeyDate groups associated with a term.
     *
     * @param keyDatesGroupWrappers
     */
    public void setKeyDatesGroupWrappers(List<KeyDatesGroupWrapper> keyDatesGroupWrappers) {
        this.keyDatesGroupWrappers = keyDatesGroupWrappers;
    }

    /**
     * See setKeyDatesToDeleteOnSave()
     *
     * @return the terms marked for deletion which is already exists in the DB
     */
    public List<KeyDateWrapper> getKeyDatesToDeleteOnSave() {
        return keyDatesToDeleteOnSave;
    }

    /**
     * This collection is used to hold the terms which are marked for deletion on save. The terms exists
     * in this collection will be already present in the DB.
     *
     * @param keyDatesToDeleteOnSave
     */
    public void setKeyDatesToDeleteOnSave(List<KeyDateWrapper> keyDatesToDeleteOnSave) {
        this.keyDatesToDeleteOnSave = keyDatesToDeleteOnSave;
    }

    /**
     * Resets the form fields.
     */
    public void clear(){
        setEndDate(null);
        setStartDate(null);
        setTermType(null);
        setName(null);
        setTypeInfo(null);
        keyDatesToDeleteOnSave.clear();
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    /**
     * Type DTO for the term type.
     *
     * @param typeInfo
     */
    public void setTypeInfo(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    /**
     * Returns whether the term is official or not. This is being used at the UI to render fields
     *
     * @return
     */
    public boolean isOfficial() {
        return StringUtils.equals(termInfo.getStateKey(), AcademicCalendarServiceConstants.TERM_OFFICIAL_STATE_KEY);
    }

    /**
     * Returns true if the Acal DTO is new. If it's new, it doesnt have an id.
     *
     * @return
     */
    public boolean isNew() {
        return StringUtils.isBlank(termInfo.getId());
    }

    /**
     * This method validates whether the key date group already exists or not. This is being use to validate
     * users not to add duplicates
     *
     * @param keydateGroupTypeKey
     * @return
     */
    public boolean isKeyDateGroupExists(String keydateGroupTypeKey){
        for(KeyDatesGroupWrapper wrapper : keyDatesGroupWrappers){
            if (StringUtils.equalsIgnoreCase(wrapper.getKeyDateGroupType(),keydateGroupTypeKey)){
                return true;
            }
        }
        return false;
    }

}
