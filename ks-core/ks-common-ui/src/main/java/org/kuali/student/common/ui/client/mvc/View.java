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

package org.kuali.student.common.ui.client.mvc;

import org.kuali.student.common.ui.client.mvc.history.HistorySupport;

/**
 * Interface defining the operations necessary to implement a view.
 * 
 * @author Kuali Student Team
 */
public interface View extends HistorySupport {
    /**
     * Called by controller before the view is displayed to allow lazy initialization or any other preparatory work to be
     * done.
     */
    public void beforeShow(Callback<Boolean> onReadyCallback);

    /**
     * Called by the controller before the view is hidden to allow the view to perform cleanup or request confirmation from
     * the user, etc. Can cancel the action by returning false.
     * 
     * @return true if the view can be hidden, or false to cancel the action.
     */
    public boolean beforeHide();

    /**
     * Returns the controller associated with the view
     * 
     * @return
     */
    public Controller getController();

    /**
     * Returns the view's name
     * 
     * @return
     */
    public String getName();

    /** 
     * Can be called to reset a view to a cleared state.
     *
     */
    public void clear();
    
    public void updateModel();
    
}
