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
 * Created by venkat on 11/27/12
 */
package org.kuali.student.enrollment.class1.state.impl;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.student.enrollment.courseoffering.dto.ActivityOfferingInfo;
import org.kuali.student.enrollment.courseoffering.dto.FormatOfferingInfo;
import org.kuali.student.enrollment.courseoffering.service.CourseOfferingService;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.util.constants.CourseOfferingServiceConstants;
import org.kuali.student.r2.core.class1.state.service.RelatedObjectHelper;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class //TODO ...
 *
 * @author Kuali Student Team
 */
public class RelatedObjectHelperFOtoAOImpl implements RelatedObjectHelper {

    private CourseOfferingService courseOfferingService;

    @Override
    public Set<String> getRelatedObjectStateKeys(String entityId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<ActivityOfferingInfo> activityOfferingInfos = getCourseOfferingService().getActivityOfferingsByFormatOffering(entityId, contextInfo);
        Set<String> stateKeys = new HashSet<String>();
        for (ActivityOfferingInfo activityOfferingInfo : activityOfferingInfos) {
             stateKeys.add(activityOfferingInfo.getStateKey());
        }
        return stateKeys;
    }

    @Override
    public Set<String> getRelatedObjectIds(String entityId, ContextInfo contextInfo) throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<ActivityOfferingInfo> activityOfferingInfos = getCourseOfferingService().getActivityOfferingsByFormatOffering(entityId, contextInfo);
        Set<String> aoIds = new HashSet<String>();
        for (ActivityOfferingInfo activityOfferingInfo : activityOfferingInfos) {
            aoIds.add(activityOfferingInfo.getId());
        }
        return aoIds;
    }

    protected CourseOfferingService getCourseOfferingService(){
        if (courseOfferingService == null){
            courseOfferingService = (CourseOfferingService) GlobalResourceLoader.getService(new QName(CourseOfferingServiceConstants.NAMESPACE, CourseOfferingServiceConstants.SERVICE_NAME_LOCAL_PART));
        }
        return  courseOfferingService;
    }
}
