/* Copyright 2011 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kuali.student.myplan.course.form;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.student.ap.framework.course.CourseSearchForm;

public class CourseSearchFormImpl extends UifFormBase implements
		CourseSearchForm {

	private static final long serialVersionUID = 4898118410378641665L;

	private String searchQuery;
	private String searchTerm = SEARCH_TERM_ANY_ITEM;
	private List<String> campusSelect;

	@Override
	public List<String> getCampusSelect() {
		return campusSelect;
	}

	public void setCampusSelect(List<String> campusSelect) {
		this.campusSelect = campusSelect;
	}

	@Override
	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	@Override
	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	@Override
	public String toString() {
		return "CourseSearchFormImpl [searchQuery=" + searchQuery
				+ ", searchTerm=" + searchTerm + ", campusSelect="
				+ campusSelect + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((campusSelect == null) ? 0 : campusSelect.hashCode());
		result = prime * result
				+ ((searchQuery == null) ? 0 : searchQuery.hashCode());
		result = prime * result
				+ ((searchTerm == null) ? 0 : searchTerm.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseSearchFormImpl other = (CourseSearchFormImpl) obj;
		if (campusSelect == null) {
			if (other.campusSelect != null)
				return false;
		} else if (!campusSelect.equals(other.campusSelect))
			return false;
		if (searchQuery == null) {
			if (other.searchQuery != null)
				return false;
		} else if (!searchQuery.equals(other.searchQuery))
			return false;
		if (searchTerm == null) {
			if (other.searchTerm != null)
				return false;
		} else if (!searchTerm.equals(other.searchTerm))
			return false;
		return true;
	}

}
