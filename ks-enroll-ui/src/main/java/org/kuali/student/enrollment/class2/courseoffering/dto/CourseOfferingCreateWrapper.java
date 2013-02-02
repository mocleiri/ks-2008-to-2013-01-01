/**
 * Copyright 2013 The Kuali Foundation Licensed under the
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
package org.kuali.student.enrollment.class2.courseoffering.dto;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class around {@link org.kuali.student.enrollment.courseoffering.dto.CourseOfferingInfo} to hold all
 * the details related to a course offering used at the presentation layer. This is used to handle the ui
 * needs at the Create Co screen
 *
 * @see JointCourseWrapper
 * @see FormatOfferingWrapper
 */
public class CourseOfferingCreateWrapper extends CourseOfferingWrapper {

    private String targetTermCode;
    private String catalogCourseCode;
    private boolean createFromCatalog;

    private String creditCount;

    private boolean showTermOfferingLink;
    private boolean showCatalogLink;
    private boolean showAllSections;
    private boolean enableCreateButton;

    private String courseOfferingSuffix;

    private List<FormatOfferingWrapper> formatOfferingWrappers;
    private List<ExistingCourseOffering> existingOfferingsInCurrentTerm;
    private List<ExistingCourseOffering> existingTermOfferings;

    private List<String> coListedCOs;
    private Boolean selectCrossListingAllowed;
    private boolean crossListedCo;
    private String displayStringCoListedCOs;


    private boolean excludeCancelledActivityOfferings;
    private boolean excludeSchedulingInformation;
    private boolean excludeInstructorInformation;

    private List<JointCourseWrapper> jointCourses;
    private boolean showJointOption;
    private String jointCourseCodes;

    private FormatOfferingWrapper addLineFormatWrapper;
    private boolean showCreateFormatSection;
    private boolean showCopyFormatSection;
    private List<FormatOfferingWrapper> copyFromFormats;

    public CourseOfferingCreateWrapper(){
        super();
        showTermOfferingLink = true;
        formatOfferingWrappers = new ArrayList<FormatOfferingWrapper>();
        existingOfferingsInCurrentTerm = new ArrayList<ExistingCourseOffering>();
        existingTermOfferings = new ArrayList<ExistingCourseOffering>();
        coListedCOs = new ArrayList<String>();
        jointCourses = new ArrayList<JointCourseWrapper>();
        showJointOption = false;
        crossListedCo = false;
        addLineFormatWrapper = new FormatOfferingWrapper();
        copyFromFormats = new ArrayList<FormatOfferingWrapper>();
        showCreateFormatSection = true;
    }

    public String getTargetTermCode() {
        return targetTermCode;
    }

    public void setTargetTermCode(String targetTermCode) {
        this.targetTermCode = targetTermCode;
    }

    public String getCatalogCourseCode() {
        return catalogCourseCode;
    }

    public void setCatalogCourseCode(String catalogCourseCode) {
        this.catalogCourseCode = catalogCourseCode;
    }

    public boolean isCreateFromCatalog() {
        return createFromCatalog;
    }

    public void setCreateFromCatalog(boolean createFromCatalog) {
        this.createFromCatalog = createFromCatalog;
    }

    public String getCreditCount() {
        return creditCount;
    }

    public void setCreditCount(String creditCount) {
        this.creditCount = creditCount;
    }

    public boolean isShowTermOfferingLink() {
        return showTermOfferingLink;
    }

    public void setShowTermOfferingLink(boolean showTermOfferingLink) {
        this.showTermOfferingLink = showTermOfferingLink;
    }

    public boolean isShowCatalogLink() {
        return showCatalogLink;
    }

    public void setShowCatalogLink(boolean showCatalogLink) {
        this.showCatalogLink = showCatalogLink;
    }

    public String getCourseOfferingSuffix() {
        return courseOfferingSuffix;
    }

    public void setCourseOfferingSuffix(String courseOfferingSuffix) {
        this.courseOfferingSuffix = courseOfferingSuffix;
    }

    /**
     * Reference at the view xml
     * @return
     */
    @SuppressWarnings("unused")
    public boolean isShowAllSections() {
        return showAllSections;
    }

    public void setShowAllSections(boolean showAllSections) {
        this.showAllSections = showAllSections;
    }

    public List<ExistingCourseOffering> getExistingOfferingsInCurrentTerm() {
        return existingOfferingsInCurrentTerm;
    }

    public void setExistingOfferingsInCurrentTerm(List<ExistingCourseOffering> existingOfferingsInCurrentTerm) {
        this.existingOfferingsInCurrentTerm = existingOfferingsInCurrentTerm;
    }

    public List<ExistingCourseOffering> getExistingTermOfferings() {
        return existingTermOfferings;
    }

    public void setExistingTermOfferings(List<ExistingCourseOffering> existingTermOfferings) {
        this.existingTermOfferings = existingTermOfferings;
    }

    /**
     * Reference at the view xml
     * @return
     */
    @SuppressWarnings("unused")
    public int getNoOfTermOfferings() {
        return getExistingTermOfferings().size();
    }

    /**
     * Reference at the view xml
     * @return
     */
    @SuppressWarnings("unused")
    public boolean isEnableCreateButton() {
        return enableCreateButton;
    }

    public void setEnableCreateButton(boolean enableCreateButton) {
        this.enableCreateButton = enableCreateButton;
    }

    public boolean isExcludeCancelledActivityOfferings() {
        return excludeCancelledActivityOfferings;
    }

    public void setExcludeCancelledActivityOfferings(boolean excludeCancelledActivityOfferings) {
        this.excludeCancelledActivityOfferings = excludeCancelledActivityOfferings;
    }

    public boolean isExcludeSchedulingInformation() {
        return excludeSchedulingInformation;
    }

    public void setExcludeSchedulingInformation(boolean excludeSchedulingInformation) {
        this.excludeSchedulingInformation = excludeSchedulingInformation;
    }

    public boolean isExcludeInstructorInformation() {
        return excludeInstructorInformation;
    }

    public void setExcludeInstructorInformation(boolean excludeInstructorInformation) {
        this.excludeInstructorInformation = excludeInstructorInformation;
    }

    public List<String> getCoListedCOs() {
        return coListedCOs;
    }

    public void setCoListedCOs(List<String> coListedCOs) {
        this.coListedCOs = coListedCOs;
    }

    public boolean isSelectCrossListingAllowed() {
         if (null == selectCrossListingAllowed) {
             String selectiveColocationAllowed = ConfigContext.getCurrentContextConfig().getProperty("kuali.ks.enrollment.options.selective-crossListing-allowed");
             if("false".equalsIgnoreCase(selectiveColocationAllowed)) {
                 selectCrossListingAllowed = false;
             } else {
                 selectCrossListingAllowed = true;
             };
         }

        return selectCrossListingAllowed;
    }

    public boolean isCrossListedCo() {
        return crossListedCo;
    }

    public void setCrossListedCo(boolean crossListedCo) {
        this.crossListedCo = crossListedCo;
    }

    public void setSelectCrossListingAllowed(boolean selectCrossListingAllowed) {
        this.selectCrossListingAllowed = selectCrossListingAllowed;
    }

    public String getDisplayStringCoListedCOs() {
        return displayStringCoListedCOs;
    }

    public void setDisplayStringCoListedCOs(String displayStringCoListedCOs) {
        this.displayStringCoListedCOs = displayStringCoListedCOs;
    }


    public List<JointCourseWrapper> getJointCourses() {
        return jointCourses;
    }

    /**
     * List of wrappers for the joint courses exists for a course
     * @param jointCourses
     */
    @SuppressWarnings("unused")
    public void setJointCourses(List<JointCourseWrapper> jointCourses) {
        this.jointCourses = jointCourses;
    }

    /**
     * This is used in the view xml to whether display the joint courses table or not
     * @return
     */
    @SuppressWarnings("unused")
    public boolean isShowJointOption() {
        return showJointOption;
    }

    /**
     * To decide whether to display joint courses if exists sothat the joint course table
     * will be displayed.
     *
     * @param showJointOption
     */
    public void setShowJointOption(boolean showJointOption) {
        this.showJointOption = showJointOption;
    }

    /**
     * @see #setJointCourses(java.util.List)
     * @return
     */
    public String getJointCourseCodes() {
        return jointCourseCodes;
    }

    /**
     * This is a ui property to display all the course offering codes for
     * which the user can create format offerings
     *
     * @param jointCourseCodes
     */
    public void setJointCourseCodes(String jointCourseCodes) {
        this.jointCourseCodes = jointCourseCodes;
    }

    /**
     * @see #setFormatOfferingWrappers(java.util.List)
     * @return
     */
    public List<FormatOfferingWrapper> getFormatOfferingWrappers() {
        return formatOfferingWrappers;
    }

    /**
     * This collection is used to have a list of format offering wrappers
     * which includes all the format offerings from joint courses.
     *
     * @param formatOfferingWrappers list of format offering wrappers
     */
    @SuppressWarnings("unused")
    public void setFormatOfferingWrappers(List<FormatOfferingWrapper> formatOfferingWrappers) {
        this.formatOfferingWrappers = formatOfferingWrappers;
    }

    /**
     * @see #setAddLineFormatWrapper(FormatOfferingWrapper)
     * @return
     */
    public FormatOfferingWrapper getAddLineFormatWrapper() {
        return addLineFormatWrapper;
    }

    /**
     * This is a format offering wrapper used to handle the new format offering add logic
     * at the ui.
     *
     * @param addLineFormatWrapper
     */
    public void setAddLineFormatWrapper(FormatOfferingWrapper addLineFormatWrapper) {
        this.addLineFormatWrapper = addLineFormatWrapper;
    }

    /**
     *
     * @see #setShowCreateFormatSection(boolean)
     * @return
     */
    public boolean isShowCreateFormatSection() {
        return showCreateFormatSection;
    }

    /**
     * Whether to display the <i>'Add Formats'</i> link at the ui.
     *
     * <p>
     *     Please note, if the course doesnt have any joint courses associated with
     *     this course, the copy link wont show up at the ui.
     * </p>
     *
     * @see #setShowCopyFormatSection(boolean)
     * @param showCreateFormatSection
     */
    public void setShowCreateFormatSection(boolean showCreateFormatSection) {
        this.showCreateFormatSection = showCreateFormatSection;
    }

    /**
     * @see #setShowCreateFormatSection(boolean)
     * @return
     */
    @SuppressWarnings("unused")
    public boolean isShowCopyFormatSection() {
        return showCopyFormatSection;
    }

    /**
     * This is a flag whether to display the <i>'Copy From Joints'</i> link at the UI.
     * If allowed, user can copy existing format offerings to create new ones.
     *
     * <p>
     *     Please note, if the course doesnt have any joint courses associated with
     *     this course, the copy link wont show up at the ui.
     * </p>
     *
     * @see #setShowCreateFormatSection(boolean)
     * @param showCopyFormatSection
     */
    public void setShowCopyFormatSection(boolean showCopyFormatSection) {
        this.showCopyFormatSection = showCopyFormatSection;
    }

    /**
     *
     * @see #setCopyFromFormats(java.util.List)
     * @return
     */
    public List<FormatOfferingWrapper> getCopyFromFormats() {
        return copyFromFormats;
    }

    /**
     * List of existing format offerings from which users can create new formatofferings
     * for other joint courses or regular course.
     *
     * @param copyFromFormats list of format offering wrappers
     */
    public void setCopyFromFormats(List<FormatOfferingWrapper> copyFromFormats) {
        this.copyFromFormats = copyFromFormats;
    }

    /**
     * Clears all the properties. This is needed to clear out all the previous ui data first
     * before displaying the new data when the user clicks on the Show button
     */
    public void clear(){
        setCourse(null);
        setShowAllSections(false);
        setCreditCount("");
        getExistingTermOfferings().clear();
        getExistingOfferingsInCurrentTerm().clear();
        setEnableCreateButton(false);
        setExcludeCancelledActivityOfferings(false);
        setExcludeSchedulingInformation(false);
        setExcludeInstructorInformation(false);
        getFormatOfferingWrappers().clear();
        getJointCourses().clear();
        setJointCourseCodes(StringUtils.EMPTY);
        setShowJointOption(false);
        setCrossListedCo(false);
        setShowCopyFormatSection(false);
        setShowCreateFormatSection(true);
        getCopyFromFormats().clear();
    }
}
