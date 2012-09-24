package org.kuali.student.enrollment.class2.courseofferingset.service.impl;

import org.apache.log4j.Logger;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.student.enrollment.courseoffering.dto.ActivityOfferingInfo;
import org.kuali.student.enrollment.courseoffering.service.CourseOfferingService;
import org.kuali.student.enrollment.courseofferingset.dto.SocInfo;
import org.kuali.student.enrollment.courseofferingset.service.CourseOfferingSetService;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.StatusInfo;
import org.kuali.student.r2.common.exceptions.*;
import org.kuali.student.r2.common.util.constants.CourseOfferingServiceConstants;
import org.kuali.student.r2.common.util.constants.CourseOfferingSetServiceConstants;
import org.kuali.student.r2.common.util.constants.LuiServiceConstants;

import javax.xml.namespace.QName;
import java.util.List;

/**
 *  SOC mass publishing event helper.
 *
 *  This code needs to move to services layer ... probably to CourseOfferingSetServiceBuisnessLogic.
 *  Concurrency not addressed since it is just a stop-gap.
 */
public class CourseOfferingSetPublishingHelper {
    final static Logger LOG = Logger.getLogger(CourseOfferingSetPublishingHelper.class);

    private CourseOfferingService coService;
    private CourseOfferingSetService socService;

    /**
     * Kicks off SOC lifecycle mass publishing event.
     *
     * Runs asynchronously by default. If any items are are provided in the options List runs runs in the existing thread.
     *
     * @param socId The ID of the SOC to publish.
     * @param optionKeys List of options. Runs synchronously unless the list is empty.
     * @param context
     * @return A StatusInfo on success. Otherwise, throws and exception.
     */
    public StatusInfo startMassPublishingEvent(String socId, List<String> optionKeys, ContextInfo context)
            throws InvalidParameterException, MissingParameterException, DoesNotExistException, PermissionDeniedException, OperationFailedException {

        //  Validate the SOC. Should exist and state should be "publishing".
        SocInfo soc = getSocService().getSoc(socId, context);
        if ( ! StringUtils.equals(soc.getStateKey(), CourseOfferingSetServiceConstants.PUBLISHING_SOC_STATE_KEY)) {
            throw new OperationFailedException(String.format("SOC state [%s] was invalid for mass publishing.", soc.getStateKey()));
        }

        //  Initialize the runner
        final SocMassPublishingRunner runner = new SocMassPublishingRunner();
        runner.setCoService(getCourseOfferingService());
        runner.setSocService(getSocService());
        runner.setSocId(socId);
        runner.setContext(context);

        if (optionKeys.size() > 0) {
            //  Run in the existing thread.
            runner.run();
        } else {
            //  Run asynchronously
            KSThreadRunnerAfterTransactionSynchronization.runAfterTransactionCompletes(runner);
        }

        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setSuccess(true);
        statusInfo.setMessage("Success");

        return statusInfo;
    }

    //  For unit testing
    public void setCoService(CourseOfferingService coService) {
        this.coService = coService;
    }

    public void setSocService(CourseOfferingSetService socService) {
        this.socService = socService;
    }

    private CourseOfferingSetService getSocService() {
        if (socService == null) {
            socService = (CourseOfferingSetService) GlobalResourceLoader.getService(new QName(CourseOfferingSetServiceConstants.NAMESPACE,
                CourseOfferingSetServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return socService;
    }

    private CourseOfferingService getCourseOfferingService() {
        if (coService == null) {
            coService = (CourseOfferingService) GlobalResourceLoader.getService(new QName(CourseOfferingServiceConstants.NAMESPACE,
                    CourseOfferingServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return coService;
    }

    public class SocMassPublishingRunner implements Runnable {
        private ContextInfo context;
        private CourseOfferingService coService;
        private CourseOfferingSetService socService;

        private String socId;

        private final String[] aoSchedStatesForOfferedKeys = {
                LuiServiceConstants.LUI_AO_SCHEDULING_STATE_EXEMPT_KEY,
                LuiServiceConstants.LUI_AO_SCHEDULING_STATE_SCHEDULED_KEY
        };
        private final String aoOfferedKey = LuiServiceConstants.LUI_AO_STATE_OFFERED_KEY;
        private final String aoApprovedKey = LuiServiceConstants.LUI_AO_STATE_APPROVED_KEY;
        private final String foOfferedKey = LuiServiceConstants.LUI_FO_STATE_OFFERED_KEY;

        @Override
        public void run() {
            LOG.warn(String.format("Beginning Mass Publishing Event for SOC [%s].", socId));
            try {
                /*
                 * Get all of the COs within the SOC. Query the AOs for each CO and do state changes.
                 */
                List<String> coIds = socService.getCourseOfferingIdsBySoc(socId, context);
                for (String coId : coIds) {
                    boolean hasAOStateChange = false;
                    List<ActivityOfferingInfo> activityOfferings = coService.getActivityOfferingsByCourseOffering(coId, context);
                    for (ActivityOfferingInfo ao : activityOfferings) {
                        /*
                         * All AOs with BOTH a state of Approved and a Scheduling state of Scheduled or Exempt will change to AO
                         * state of Offered. The FO and CO for these AOs also changes state from Planned to Offered.
                         */
                        String aoState = ao.getStateKey();
                        String aoSchedState = ao.getSchedulingStateKey();
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(String.format("Inspecting CO [%s] AO [%s] in state %s and scheduling state [%s].", coId, ao.getId(), aoState, aoSchedState));
                        }
                        if (StringUtils.equals(aoState, aoApprovedKey) && ArrayUtils.contains(aoSchedStatesForOfferedKeys, aoSchedState)) {
                            if (! hasAOStateChange) {
                                hasAOStateChange = true;
                            }
                            StatusInfo statusInfo = coService.updateActivityOfferingState(ao.getId(), aoOfferedKey, context);
                            if ( ! statusInfo.getIsSuccess()) {
                                LOG.error(String.format("State change failed for AO [%s]: %s", ao.getId(), statusInfo.getMessage()));
                            } else {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(String.format("Updating AO [%s] state to [%s].", ao.getId(), aoState));
                                }
                            }
                            //  Change the FO state to offered.
                            statusInfo = coService.updateFormatOfferingState(ao.getFormatOfferingId(), foOfferedKey, context);
                            if ( ! statusInfo.getIsSuccess()) {
                                LOG.error(String.format("State change failed for FO [%s]: %s", ao.getFormatOfferingId(), statusInfo.getMessage()));
                            }  else {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(String.format("Updating FO [%s] state to [%s].", ao.getFormatOfferingId(), foOfferedKey));
                                }
                            }
                        } else {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(String.format("CO [%s] AO [%s] doesn't need a state change.", coId, ao.getId()));
                            }
                        }
                    }

                    // If an AO changed state then state change the CO.
                    if (hasAOStateChange) {
                        coService.updateCourseOfferingState(coId, LuiServiceConstants.LUI_CO_STATE_OFFERED_KEY, context);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(String.format("Updating CO [%s] state to [%s].", coId, LuiServiceConstants.LUI_CO_STATE_OFFERED_KEY));
                        }
                    }
                }

                //  Set SOC scheduling state to "published"
                LOG.warn(String.format("Updating SOC [%s] state to [%s].", socId, CourseOfferingSetServiceConstants.PUBLISHED_SOC_STATE_KEY));
                StatusInfo statusInfo = socService.updateSocState(socId, CourseOfferingSetServiceConstants.PUBLISHED_SOC_STATE_KEY, context);
                if ( ! statusInfo.getIsSuccess()) {
                    LOG.error(String.format("State changed failed for SOC [%s]: %s", socId, statusInfo.getMessage()));
                }
            } catch (Exception e) {
                LOG.error("Mass Publishing Event did not complete successfully.", e);
                return;
            }
            LOG.warn(String.format("Mass Publishing Event for SOC [%s] completed.", socId));
        }

        public String getSocId() {
            return socId;
        }

        public void setSocId(String socId) {
            this.socId = socId;
        }

        public void setContext(ContextInfo context) {
            this.context = context;
        }

        public void setCoService(CourseOfferingService coService) {
            this.coService = coService;
        }

        public void setSocService(CourseOfferingSetService socService) {
            this.socService = socService;
        }
    }
}
