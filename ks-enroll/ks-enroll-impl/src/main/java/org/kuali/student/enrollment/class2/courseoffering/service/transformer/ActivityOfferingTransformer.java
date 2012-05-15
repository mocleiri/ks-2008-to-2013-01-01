package org.kuali.student.enrollment.class2.courseoffering.service.transformer;

import org.kuali.student.enrollment.courseoffering.dto.ActivityOfferingInfo;
import org.kuali.student.enrollment.courseoffering.dto.OfferingInstructorInfo;
import org.kuali.student.enrollment.lpr.dto.LuiPersonRelationInfo;
import org.kuali.student.enrollment.lui.dto.LuiInfo;
import org.kuali.student.r2.common.dto.AttributeInfo;
import org.kuali.student.r2.common.dto.HasAttributesInfo;
import org.kuali.student.r2.common.infc.Attribute;
import org.kuali.student.r2.common.util.constants.LuiServiceConstants;

import java.util.ArrayList;
import java.util.List;

public class ActivityOfferingTransformer {

    public static void lui2Activity(ActivityOfferingInfo ao, LuiInfo lui) {
        ao.setId(lui.getId());
        ao.setMeta(lui.getMeta());
        ao.setStateKey(lui.getStateKey());
        ao.setTypeKey(lui.getTypeKey());
        ao.setDescr(lui.getDescr());
        ao.setActivityId(lui.getCluId());
        ao.setTermId(lui.getAtpId());
        ao.setMinimumEnrollment(lui.getMinimumEnrollment());
        ao.setMaximumEnrollment(lui.getMaximumEnrollment());

        //Dynamic attributes - Some lui dynamic attributes are defined fields on Activity Offering
        List<AttributeInfo> attributes = ao.getAttributes();
        for (Attribute attr : lui.getAttributes()) {
            if (LuiServiceConstants.LUI_COURSE_EVALUATION_INDICATOR_ATTR.equals(attr.getKey())){
                ao.setIsEvaluated(Boolean.valueOf(attr.getValue()));
            } else if (LuiServiceConstants.LUI_IS_MAX_ENROLLMENT_ESTIMATE_ATTR.equals(attr.getKey())){
                ao.setIsMaxEnrollmentEstimate(Boolean.valueOf(attr.getValue()));
            } else if (LuiServiceConstants.LUI_HONORS_OFFERING_INDICATOR_ATTR.equals(attr.getKey())){
                ao.setIsHonorsOffering(Boolean.valueOf(attr.getValue()));
            } else {
                attributes.add(new AttributeInfo(attr));
            }
        }
        ao.setAttributes(attributes);
    }

    public static void activity2Lui (ActivityOfferingInfo ao, LuiInfo lui) {
        lui.setId(ao.getId());
        lui.setTypeKey(ao.getTypeKey());
        lui.setStateKey(ao.getStateKey());
        lui.setDescr(ao.getDescr());
        lui.setMeta(ao.getMeta());
        lui.setCluId(ao.getActivityId());
        lui.setAtpId(ao.getTermId());
        lui.setMinimumEnrollment(ao.getMinimumEnrollment());
        lui.setMaximumEnrollment(ao.getMaximumEnrollment());

        //Dynamic attributes - Some lui dynamic attributes are defined fields on Activity Offering
        List<AttributeInfo> attributes = lui.getAttributes();
        for (Attribute attr : ao.getAttributes()) {
            attributes.add(new AttributeInfo(attr));
        }

        AttributeInfo isEvaluated = new AttributeInfo();
        isEvaluated.setKey(LuiServiceConstants.LUI_COURSE_EVALUATION_INDICATOR_ATTR);
        isEvaluated.setValue(String.valueOf(ao.getIsEvaluated()));
        attributes.add(isEvaluated);

        AttributeInfo isMaxEnrollmentEstimate = new AttributeInfo();
        isMaxEnrollmentEstimate.setKey(LuiServiceConstants.LUI_IS_MAX_ENROLLMENT_ESTIMATE_ATTR);
        isMaxEnrollmentEstimate.setValue(String.valueOf(ao.getIsMaxEnrollmentEstimate()));
        attributes.add(isMaxEnrollmentEstimate);

        AttributeInfo honorsOffering = new AttributeInfo();
        honorsOffering.setKey(LuiServiceConstants.LUI_HONORS_OFFERING_INDICATOR_ATTR);
        honorsOffering.setValue(String.valueOf(ao.getIsHonorsOffering()));
        attributes.add(honorsOffering);

        lui.setAttributes(attributes);
    }

    public static OfferingInstructorInfo transformInstructorForActivityOffering(LuiPersonRelationInfo lpr) {
        OfferingInstructorInfo instructor = new OfferingInstructorInfo();
        instructor.setPersonId(lpr.getPersonId());
        instructor.setPercentageEffort(lpr.getCommitmentPercent());
        instructor.setId(lpr.getId());
        instructor.setTypeKey(lpr.getTypeKey());
        instructor.setStateKey(lpr.getStateKey());
        return instructor;

    }
}
