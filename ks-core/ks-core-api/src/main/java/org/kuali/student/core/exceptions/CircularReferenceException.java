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

package org.kuali.student.core.exceptions;

import javax.xml.ws.WebFault;

@WebFault(faultBean="org.kuali.student.core.exceptions.jaxws.CircularReferenceExceptionBean")
public class CircularReferenceException extends Exception {

	private static final long serialVersionUID = -6652661226236017610L;

	/**
	 *
	 */
	public CircularReferenceException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CircularReferenceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public CircularReferenceException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CircularReferenceException(Throwable cause) {
		super(cause);
	}

}
