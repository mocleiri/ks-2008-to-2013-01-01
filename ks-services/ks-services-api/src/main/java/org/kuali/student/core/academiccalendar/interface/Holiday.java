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

package org.kuali.student.core.academiccalendar.infc;

import java.util.Date;
import org.kuali.student.common.interface.KeyEntity;


/**
 * Information about a holiday.
 *
 * @Author tom
 * @Since Tue Apr 05 14:22:34 EDT 2011
 */ 

public interface Holiday extends KeyDate {

	/**
	 * Name: IsDateRange
	 * Tests if this holiday is an instructional day. 
	 *
	 * @return true if this holiday is an instructional day, false if
	 *         it does not count as an instructional day
	 */
	public Boolean getIsInstructionalDay();

	/**
	 * Name: IsDateRange
	 * Tests if this holiday is an exam day. 
	 *
	 * @return true if this holiday is an exam day, false if
	 *         it does not count as an exam day
	 */
	public Boolean getIsExamDay();
}
