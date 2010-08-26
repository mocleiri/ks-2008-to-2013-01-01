
package org.kuali.student.wsdl.organization;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.3
 * Thu Aug 26 16:27:08 EDT 2010
 * Generated source version: 2.2.3
 * 
 */

@WebFault(name = "PermissionDeniedException", targetNamespace = "http://student.kuali.org/wsdl/organization")
public class PermissionDeniedException extends Exception {
    public static final long serialVersionUID = 20100826162708L;
    
    private org.kuali.student.wsdl.exceptions.PermissionDeniedException permissionDeniedException;

    public PermissionDeniedException() {
        super();
    }
    
    public PermissionDeniedException(String message) {
        super(message);
    }
    
    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionDeniedException(String message, org.kuali.student.wsdl.exceptions.PermissionDeniedException permissionDeniedException) {
        super(message);
        this.permissionDeniedException = permissionDeniedException;
    }

    public PermissionDeniedException(String message, org.kuali.student.wsdl.exceptions.PermissionDeniedException permissionDeniedException, Throwable cause) {
        super(message, cause);
        this.permissionDeniedException = permissionDeniedException;
    }

    public org.kuali.student.wsdl.exceptions.PermissionDeniedException getFaultInfo() {
        return this.permissionDeniedException;
    }
}
