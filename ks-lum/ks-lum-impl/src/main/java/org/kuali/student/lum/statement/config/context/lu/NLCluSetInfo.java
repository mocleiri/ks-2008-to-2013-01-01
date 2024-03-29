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

package org.kuali.student.lum.statement.config.context.lu;

import java.util.List;

import org.kuali.student.lum.lu.dto.CluInfo;

/**
 * <p><b><u>Warning</u></b><br/>
 * DO NOT change the public method signatures of this class.<br/>
 * The natural language templates are coded against this class's public methods.
 * If the method signatures are changed then all the templates referencing 
 * this class will need to be changed as well.</p>
 * 
 * This class is inserted into the template engine to get Clu and CluSet 
 * information. <code>$cluSet</code> is this class.
 * <p>
 * Example:
 * <code>"Student must have completed $expectedValue of $cluSet.getCluSetAsShortName()"</code>
 * </p>
 * 
 * {@link CluSetInfo} wrapper class.
 */
public class NLCluSetInfo {

	private String cluSetId;
	private List<CluInfo> cluList;
	
	public NLCluSetInfo(String cluSetId, List<CluInfo> cluList) {
		this.cluSetId = cluSetId;
		this.cluList = cluList;
	}

	/**
	 * Gets the CLU set id.
	 * 
	 * @return Clu set id
	 */
	public String getCluSetId() {
		return cluSetId;
	}

	/**
	 * Gets a list of CLUs.
	 *  
	 * @return List of CLUs
	 */
	public List<CluInfo> getCluList() {
		return this.cluList;
	}

	/**
	 * Gets a particular CLU's official identifier short name.
	 * 
	 * @param index Index in CLU set
	 * @return CLU short name
	 */
	public String getCluAsShortName(int index) {
		return this.cluList.get(index).getOfficialIdentifier().getShortName();
	}

	/**
	 * Gets a particular CLU's official identifier code at <code>index</code>
	 * @param index
	 * @return
	 */
	public String getCluAsCode(int index) {
		return this.cluList.get(index).getOfficialIdentifier().getCode();
	}

	/**
	 * Gets all the CLUs' official identifier short name in the CLU set 
	 * as a comma separated list.
	 *   
	 * @return Comma separated list of CLU official identifier short name 
	 */
	public String getCluSetAsShortName() {
		StringBuilder sb = new StringBuilder();
		for(CluInfo clu : this.cluList) {
			sb.append(clu.getOfficialIdentifier().getShortName());
			sb.append(", ");
		}
		return getString(sb);
	}

	/**
	 * Gets all the CLUs' official identifier long name in the CLU set 
	 * as a comma separated list.
	 * 
	 * @return Comma separated list of CLU official identifier long name 
	 */
	public String getCluSetAsLongName() {
		StringBuilder sb = new StringBuilder();
		for(CluInfo clu : this.cluList) {
			sb.append(clu.getOfficialIdentifier().getShortName());
			sb.append(", ");
		}
		return getString(sb);
	}

	/**
	 * Gets all the CLUs' official identifier code in the CLU set 
	 * as a comma separated list.
	 * 
	 * @return Comma separated list of CLU official identifier code 
	 */
	public String getCluSetAsCode() {
		StringBuilder sb = new StringBuilder();
		for(CluInfo clu : this.cluList) {
			sb.append(clu.getOfficialIdentifier().getCode());
			sb.append(", ");
		}
		return getString(sb);
	}

	private String getString(StringBuilder sb) {
		return (sb.length() == 0 ? "No Clus available in CluSet" : sb.toString().substring(0, sb.toString().length() - 2));
	}

	public String toString() {
		if(this.cluList == null) {
			return "Null CluSet";
		}
		return "id=" + this.cluSetId;
	}
}
