package org.kuali.student.core.web.organization.server.gwt;

import java.util.List;

import org.kuali.student.core.organization.dto.OrgHierarchyInfo;
import org.kuali.student.core.organization.dto.OrgInfo;
import org.kuali.student.core.organization.dto.OrgOrgRelationInfo;
import org.kuali.student.core.web.organization.client.service.OrgRpcService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class OrgRpcServiceGwt extends RemoteServiceServlet implements OrgRpcService{

	private static final long serialVersionUID = 1L;

	private OrgRpcService serviceImpl;

	public OrgRpcService getServiceImpl() {
		return serviceImpl;
	}

	public void setServiceImpl(OrgRpcService serviceImpl) {
		this.serviceImpl = serviceImpl;
	}

	public List<OrgInfo> getOrganizationsByIdList(List<String> orgIdList) {
		return serviceImpl.getOrganizationsByIdList(orgIdList);
	}

	public List<OrgHierarchyInfo> getOrgHierarchies() {
		return serviceImpl.getOrgHierarchies();
	}

	public List<OrgOrgRelationInfo> getOrgOrgRelationsByOrg(String orgId) {
		return serviceImpl.getOrgOrgRelationsByOrg(orgId);
	}
	
}
