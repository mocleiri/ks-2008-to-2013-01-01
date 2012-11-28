package org.kuali.student.enrollment.main.view;

import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.krad.util.KRADConstants;

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewAuthorizerBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.student.enrollment.class2.courseoffering.form.CourseOfferingManagementForm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom Authorizer that resolves qualifiers from the model. This will need to be refactored and made more generic in
 * the future (configuration of mapping from model object graph to role qualifiers, maybe using beanutils
 * (object.field1[3].field2->qualifierId)
 */
public class KsViewAuthorizerBase extends ViewAuthorizerBase {

    /**
     * This method resolves the unitsDeploymentOrgId from the CourseOffering Model and passes it in as
     * a role qualifier
     */
    @Override
    protected boolean isAuthorizedByTemplate(View view, Component component, ViewModel model, String permissionTemplateName, Person user, Map<String, String> additionalPermissionDetails, Map<String, String> additionalRoleQualifications, boolean checkPermissionExistence) {
        if (model !=null && model instanceof CourseOfferingManagementForm) {
            CourseOfferingManagementForm theForm = (CourseOfferingManagementForm) model;
            if(theForm.getTheCourseOffering() != null){
                //Pull out the org ids and pass in the first one as a role qualifier
                List<String> orgIds = theForm.getTheCourseOffering().getUnitsDeploymentOrgIds();
                if(orgIds !=null && !orgIds.isEmpty()){
                    if(additionalRoleQualifications == null){
                        //Instantiate if null was passed in
                        additionalRoleQualifications = new HashMap<String, String>();
                    }
                    additionalRoleQualifications.put("org", orgIds.get(0));
                }
            }
            else if(theForm.getAdminOrg() != null){
                additionalRoleQualifications.put("org", theForm.getAdminOrg());
            }
        }
        // Make the actual call to is authorized by template
        return super.isAuthorizedByTemplate(view, component, model, permissionTemplateName, user, additionalPermissionDetails, additionalRoleQualifications, checkPermissionExistence);
    }

}