/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kuali.student.enrollment.class2.courseofferingset.service.impl;

import org.junit.runner.RunWith;
import org.kuali.student.enrollment.courseofferingset.dto.SocInfo;
import org.kuali.student.enrollment.test.util.AttributeTester;
import org.kuali.student.r2.common.dto.StatusInfo;
import org.kuali.student.r2.common.util.RichTextHelper;
import org.kuali.student.r2.common.util.constants.CourseOfferingSetServiceConstants;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tests the jpa persistence impl
 *
 * @author Kuali Student Team
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:soc-jpa-persistence-test-context.xml"})
@TransactionConfiguration(transactionManager = "JtaTxManager", defaultRollback = true)
@Transactional
public class TestCourseOfferingSetServiceJpaPersistenceImpl extends TestCourseOfferingSetServiceMockImpl {

    @Test
    public void testSocSchedulingState() throws Exception {
        SocInfo orig = new SocInfo();
        orig.setName("SOC");
        orig.setDescr(new RichTextHelper().toRichTextInfo("description plain 1", "description formatted 1"));
        orig.setTypeKey(CourseOfferingSetServiceConstants.MAIN_SOC_TYPE_KEY);
        orig.setStateKey(CourseOfferingSetServiceConstants.DRAFT_SOC_STATE_KEY);
        orig.setTermId("myTermId");
        orig.setSubjectArea("ENG");
        orig.setUnitsContentOwnerId("myUnitId");

        //  Verify that scheduling state changes are successfully "logged" in dynamic attributes and that the conversion between date string and date works.
        SimpleDateFormat formatter = new SimpleDateFormat(CourseOfferingSetServiceConstants.STATE_CHANGE_DATE_FORMAT);
        Date startDate = new Date();
        String startDateString = formatter.format(startDate);
        orig.getAttributes().add(new AttributeTester().toAttribute(CourseOfferingSetServiceConstants.SOC_SCHEDULING_STATE_IN_PROGRESS, startDateString));
        SocInfo info = socService.createSoc(orig.getTermId(), orig.getTypeKey(), orig, callContext);

        assertEquals(startDateString, formatter.format(info.getLastSchedulingRunStarted()));
        assertNull(info.getLastSchedulingRunCompleted());

        //  Update SOC with scheduling state "completed"
        callContext.setCurrentDate(new Date());
        StatusInfo status = socService.updateSocState(info.getId(), CourseOfferingSetServiceConstants.SOC_SCHEDULING_STATE_COMPLETED, callContext);
        assertTrue(status.getIsSuccess());

        SocInfo updated = socService.getSoc(info.getId(), callContext);

        assertEquals(startDateString, formatter.format(updated.getLastSchedulingRunStarted()));
        assertNotNull(updated.getLastSchedulingRunCompleted());
        assertFalse(updated.getLastSchedulingRunStarted().before(updated.getLastSchedulingRunCompleted()));
    }

    @Test
    public void testUpdateSocState() throws Exception {
        SocInfo orig = new SocInfo();
        orig.setName("SOC");
        orig.setDescr(new RichTextHelper().toRichTextInfo("description plain 1", "description formatted 1"));
        orig.setTypeKey(CourseOfferingSetServiceConstants.MAIN_SOC_TYPE_KEY);
        orig.setStateKey(CourseOfferingSetServiceConstants.DRAFT_SOC_STATE_KEY);
        orig.setTermId("myTermId2");
        orig.setSubjectArea("ENGL");
        orig.setUnitsContentOwnerId("myUnitId2");
        SocInfo info = socService.createSoc(orig.getTermId(), orig.getTypeKey(), orig, callContext);

        // assert publish dates are both null on create
        assertNull(info.getPublishingStarted());
        assertNull(info.getPublishingCompleted());

        //  Update SOC with a state of publishing
        StatusInfo status = socService.updateSocState(info.getId(), CourseOfferingSetServiceConstants.PUBLISHING_SOC_STATE_KEY, callContext);
        assertTrue(status.getIsSuccess());

        SocInfo updated = socService.getSoc(info.getId(), callContext);

        // assert that publishing has started but not completed
        assertEquals(CourseOfferingSetServiceConstants.PUBLISHING_SOC_STATE_KEY, updated.getStateKey());
        assertNotNull(updated.getPublishingStarted());
        assertNull(updated.getPublishingCompleted());

        //  Now update the SOC state to published
        status = socService.updateSocState(info.getId(), CourseOfferingSetServiceConstants.PUBLISHED_SOC_STATE_KEY, callContext);
        assertTrue(status.getIsSuccess());

        updated = socService.getSoc(info.getId(), callContext);

        // assert that publishing has completed
        assertEquals(CourseOfferingSetServiceConstants.PUBLISHED_SOC_STATE_KEY, updated.getStateKey());
        assertNotNull(updated.getPublishingStarted());
        assertNotNull(updated.getPublishingCompleted());
    }
}
