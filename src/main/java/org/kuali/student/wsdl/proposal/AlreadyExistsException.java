
package org.kuali.student.wsdl.proposal;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.3
 * Thu Aug 26 16:25:56 EDT 2010
 * Generated source version: 2.2.3
 * 
 */

@WebFault(name = "AlreadyExistsException", targetNamespace = "http://student.kuali.org/wsdl/proposal")
public class AlreadyExistsException extends Exception {
    public static final long serialVersionUID = 20100826162556L;
    
    private org.kuali.student.wsdl.exceptions.AlreadyExistsException alreadyExistsException;

    public AlreadyExistsException() {
        super();
    }
    
    public AlreadyExistsException(String message) {
        super(message);
    }
    
    public AlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistsException(String message, org.kuali.student.wsdl.exceptions.AlreadyExistsException alreadyExistsException) {
        super(message);
        this.alreadyExistsException = alreadyExistsException;
    }

    public AlreadyExistsException(String message, org.kuali.student.wsdl.exceptions.AlreadyExistsException alreadyExistsException, Throwable cause) {
        super(message, cause);
        this.alreadyExistsException = alreadyExistsException;
    }

    public org.kuali.student.wsdl.exceptions.AlreadyExistsException getFaultInfo() {
        return this.alreadyExistsException;
    }
}
