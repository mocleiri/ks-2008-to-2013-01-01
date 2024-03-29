/**
 * Copyright 2010 The Kuali Foundation Licensed under the
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
 */

package org.kuali.student.core.person.ui.server.gwt;

import javax.xml.namespace.QName;

import org.kuali.rice.core.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.bo.entity.dto.KimPrincipalInfo;
import org.kuali.rice.kim.service.IdentityService;
import org.kuali.student.common.ui.server.gwt.BaseRpcGwtServletAbstract;
import org.kuali.student.core.person.dto.PersonInfo;
import org.kuali.student.core.person.dto.PersonNameInfo;
import org.kuali.student.core.person.ui.client.service.PersonRpcService;
import org.kuali.student.core.search.service.SearchService;

/**
 *
 * @deprecated Use SearchRpcService instead in conjunction with PersonSearchServiceImpl
 *
 */
public class PersonRpcGwtServlet extends BaseRpcGwtServletAbstract<SearchService> implements
		PersonRpcService {
	protected static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
	.getLogger(PersonRpcGwtServlet.class);
	private static final long serialVersionUID = 3797505861921543183L;

	private IdentityService identityService;
	private String identityServiceAddress;
/*
	@Override
	public List<Result> searchForResults(String searchTypeKey,
			List<QueryParamValue> queryParamValues) {
		// TODO Auto-generated method stub
		// try {
		List<Result> results = new ArrayList<Result>();

		if (null == identityService) {
			identityService = (IdentityService) GlobalResourceLoader.getService(new QName("KIM","kimIdentityServiceSOAPUnsecure"));
		}

		if (identityService != null) {
			try{
				List<KimEntityDefaultInfo> entities = (List<KimEntityDefaultInfo>) identityService
						.lookupEntityDefaultInfo(new HashMap<String, String>(),
								true);
				for (KimEntityDefaultInfo entity : entities) {
					if (entity.getPrincipals() != null) {
						for (KimPrincipal principal : entity.getPrincipals()) {
							Result result = new Result();
							ResultCell cell = new ResultCell();
							cell.setKey("Person Id");
							cell.setValue(principal.getPrincipalId());
							result.getResultCells().add(cell);
							cell = new ResultCell();
							cell.setKey("Person Name");
							cell.setValue(principal.getPrincipalName());
							result.getResultCells().add(cell);
							results.add(result);
						}
					}
				}
				return results;
			}catch(Exception e){
				LOG.error("Error getting identityService", e);
			}
		}

		String[] kimPrincipalIds= new String[]{
				"1",
				"admin",
				"admin1",
				"admin2",
				"dev1",
				"dev2",
				"director",
				"doug",
				"earl",
				"edna",
				"employee",
				"eric",
				"erin",
				"fran",
				"frank",
				"fred",
				"idm1",
				"idm2",
				"idm3",
				"kuluser",
				"newaccountuser",
				"notsys",
				"notsysadm",
				"quickstart",
				"supervisor",
				"test1",
				"test2",
				"testadmin1",
				"testadmin2",
				"testuser1",
				"testuser2",
				"testuser3",
				"testuser4",
				"testuser5",
				"testuser6",
				"user1",
				"user2",
				"user3",
				"user4"};
		for(int i = 0;i<kimPrincipalIds.length;i++){

			Result result = new Result();
			ResultCell cell = new ResultCell();
			cell.setKey("Person Id");
			cell.setValue(kimPrincipalIds[i]);
			result.getResultCells().add(cell);
			cell = new ResultCell();
			cell.setKey("Person Name");
			cell.setValue(kimPrincipalIds[i]);
			result.getResultCells().add(cell);
			results.add(result);
		}

		return results;
	}
*/
	@Override
	public PersonInfo fetchPerson(String personId) {
		if (null == identityService) {
			identityService = (IdentityService) GlobalResourceLoader.getService(new QName("KIM","kimIdentityServiceSOAPUnsecure"));
		}
		//Try to use the identity service, otherwise send back name=id;
		if (identityService != null) {
			try{
				KimPrincipalInfo kimPrincipalInfo = identityService.getPrincipal(personId);
				PersonInfo person = new PersonInfo();
				person.setId(kimPrincipalInfo.getPrincipalId());
				PersonNameInfo nameInfo = new PersonNameInfo();
				nameInfo.setGivenName(kimPrincipalInfo.getPrincipalName());
				person.getPersonNameInfoList().add(nameInfo);
				return person;
			}catch(Exception e){
				LOG.error("Error getting identityService", e);
			}
		}
		PersonInfo person = new PersonInfo();
		person.setId(personId);
		PersonNameInfo nameInfo = new PersonNameInfo();
		nameInfo.setGivenName(personId);
		person.getPersonNameInfoList().add(nameInfo);
		return person;
	}
/*
	public IdentityService getIdentityService() {
		return identityService;
	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	public String getIdentityServiceAddress() {
		return identityServiceAddress;
	}

	public void setIdentityServiceAddress(String identityServiceAddress) {
		this.identityServiceAddress = identityServiceAddress;
	}
*/
}
