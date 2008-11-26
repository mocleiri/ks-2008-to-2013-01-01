/**
 * 
 */
package org.kuali.student.rules.devgui.client.service;

import java.util.List;

import org.kuali.student.rules.devgui.client.model.RuleTypesHierarchyInfo;
import org.kuali.student.rules.devgui.client.model.RulesHierarchyInfo;
import org.kuali.student.rules.factfinder.dto.FactTypeInfoDTO;
import org.kuali.student.rules.rulemanagement.dto.BusinessRuleInfoDTO;
import org.kuali.student.rules.rulemanagement.dto.BusinessRuleTypeDTO;
import org.kuali.student.rules.rulemanagement.dto.StatusDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @author zzraly
 */
public interface DevelopersGuiService extends RemoteService {
    /**
     * URI for the log service servlet.
     */
    public static final String SERVICE_URI = "/DevelopersGuiService";

    /**
     * Utility class used to create instance of the asynchronous service interface.
     */
    public static class Util {
        /**
         * Creates an instance of the DevGuiServiceAsync interface using module base URL and the SERVICE_URI constant
         * 
         * @return instance of LogServiceAsync
         */
        public static DevelopersGuiServiceAsync getInstance() {
            DevelopersGuiServiceAsync instance = (DevelopersGuiServiceAsync) GWT.create(DevelopersGuiService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }
    
    public FactTypeInfoDTO fetchFactType(String factTypeKey);

    public String createBusinessRule(BusinessRuleInfoDTO businessRuleInfo);

    public StatusDTO updateBusinessRule(String businessRuleId, BusinessRuleInfoDTO businessRuleInfo) throws Exception;

    public BusinessRuleInfoDTO fetchDetailedBusinessRuleInfo(String ruleId);

    public BusinessRuleTypeDTO fetchBusinessRuleType(String ruleTypeKey, String anchorTypeKey);

    public String testBusinessRule(String businessRuleId);
    
    public List<String> findAgendaTypes();
    
    public List<String> findBusinessRuleTypesByAgendaType(String agendaTypeKey);
    
    public List<RulesHierarchyInfo> fetchRulesHierarchyInfo();

    public List<RuleTypesHierarchyInfo> fetchRuleTypesHierarchyInfo();    
}
