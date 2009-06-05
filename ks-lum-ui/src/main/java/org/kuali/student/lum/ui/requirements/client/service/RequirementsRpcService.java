package org.kuali.student.lum.ui.requirements.client.service;

import java.util.List;
import java.util.Map;

import org.kuali.student.lum.lu.dto.LuStatementInfo;
import org.kuali.student.lum.lu.dto.ReqComponentInfo;
import org.kuali.student.lum.lu.dto.ReqComponentTypeInfo;
import org.kuali.student.lum.ui.requirements.client.model.CourseRuleInfo;
import org.kuali.student.lum.ui.requirements.client.model.StatementVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

@RemoteServiceRelativePath("RequirementsService")
public interface RequirementsRpcService extends RemoteService{
    
    public String[] getReqComponentTemplates(ReqComponentInfo compInfo) throws Exception;
    public String getNaturalLanguageForReqComponentInfo(ReqComponentInfo compInfo, String nlUsageTypeKey) throws Exception;
    public String getNaturalLanguageForStatementVO(String cluId, StatementVO statementVO, String nlUsageTypeKey) throws Exception;  
    public CourseRuleInfo getCourseAndRulesInfo(String cluId) throws Exception;
    public List<ReqComponentTypeInfo> getReqComponentTypesForLuStatementType(String luStatementTypeKey) throws Exception;
    public Map<String, String> getAllClus() throws Exception;
    public Map<String, String> getAllClusets() throws Exception;  
    public LuStatementInfo getLuStatementForCluAndStatementType(String cluId, String luStatementTypeKey) throws Exception;
    public String getRuleRationale(String cluId, String luStatementTypeKey) throws Exception;
    public StatementVO getStatementVO(String cluId, String luStatementTypeKey) throws Exception;    
}
