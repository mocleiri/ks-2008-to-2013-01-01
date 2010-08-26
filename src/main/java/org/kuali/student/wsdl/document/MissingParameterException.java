
package org.kuali.student.wsdl.document;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.3
 * Thu Aug 26 16:26:49 EDT 2010
 * Generated source version: 2.2.3
 * 
 */

@WebFault(name = "MissingParameterException", targetNamespace = "http://student.kuali.org/wsdl/document")
public class MissingParameterException extends Exception {
    public static final long serialVersionUID = 20100826162649L;
    
    private org.kuali.student.wsdl.exceptions.MissingParameterException missingParameterException;

    public MissingParameterException() {
        super();
    }
    
    public MissingParameterException(String message) {
        super(message);
    }
    
    public MissingParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingParameterException(String message, org.kuali.student.wsdl.exceptions.MissingParameterException missingParameterException) {
        super(message);
        this.missingParameterException = missingParameterException;
    }

    public MissingParameterException(String message, org.kuali.student.wsdl.exceptions.MissingParameterException missingParameterException, Throwable cause) {
        super(message, cause);
        this.missingParameterException = missingParameterException;
    }

    public org.kuali.student.wsdl.exceptions.MissingParameterException getFaultInfo() {
        return this.missingParameterException;
    }
}
