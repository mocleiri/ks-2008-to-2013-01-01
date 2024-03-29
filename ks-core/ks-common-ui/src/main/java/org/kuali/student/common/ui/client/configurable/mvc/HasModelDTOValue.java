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

package org.kuali.student.common.ui.client.configurable.mvc;

import org.kuali.student.common.ui.client.mvc.dto.ModelDTOValue;

import com.google.gwt.user.client.ui.HasValue;

/**
 * This extends the HasValue to add an updateModelDTOValue method, which can be called
 * on a widget implementing this interface to update the ModelDTOValue object with values
 * contained within the implementing widget.
 * 
 * @author Kuali Student Team
 *
 */
@Deprecated
public interface HasModelDTOValue extends HasValue<ModelDTOValue>{
       
    public void updateModelDTOValue();
    
}
