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

package org.kuali.student.brms.repository.exceptions;

/**
 * This is a rule engine repository login exception class.
 * 
 * @author Kuali Student Team (len.kuali@googlegroups.com)
 *
 */
public class RepositoryLoginException extends Exception {

    /** Class serial version uid */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new repository login exception.
     * 
     * @param cause Cause of this exception
     */
    public RepositoryLoginException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new repository login exception.
     * 
     * @param msg Error message description
     * @param cause Cause of this exception
     */
    public RepositoryLoginException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs a new repository login exception.
     * 
     * @param msg Error message description
     */
    public RepositoryLoginException(String msg) {
        super(msg);
    }
}
