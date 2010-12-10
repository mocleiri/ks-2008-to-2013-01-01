
/*
 * 
 */
package org.kuali.student.loader.program;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;
import org.kuali.student.lum.program.service.ProgramService;

/**
 * This class was generated by Apache CXF 2.2.10
 * Wed Sep 08 11:26:43 EDT 2010
 * Generated source version: 2.2.10
 * 
 */
@WebServiceClient (name = "ProgramService",
                   wsdlLocation =
                   "file:/D:/svn/maven-dictionary-generator/trunk/src/main/resources/wsdl/ProgramService.wsdl",
                   targetNamespace = "http://student.kuali.org/wsdl/program")
public class ProgramService_Service extends Service
{

 public final static URL WSDL_LOCATION;
 public final static QName SERVICE = new QName (
   "http://student.kuali.org/wsdl/program", "ProgramService");
 public final static QName ProgramServicePort = new QName (
   "http://student.kuali.org/wsdl/program", "ProgramServicePort");

 static
 {
  URL url = null;
  try
  {
   url =
   new URL (
     "file:/D:/svn/maven-dictionary-generator/trunk/src/main/resources/wsdl/ProgramService.wsdl");
  }
  catch (MalformedURLException e)
  {
   System.err.println (
     "Can not initialize the default wsdl from file:/D:/svn/maven-dictionary-generator/trunk/src/main/resources/wsdl/ProgramService.wsdl");
   // e.printStackTrace();
  }
  WSDL_LOCATION = url;
 }

 public ProgramService_Service (URL wsdlLocation)
 {
  super (wsdlLocation, SERVICE);
 }

 public ProgramService_Service (URL wsdlLocation, QName serviceName)
 {
  super (wsdlLocation, serviceName);
 }

 public ProgramService_Service ()
 {
  super (WSDL_LOCATION, SERVICE);
 }

 /**
  *
  * @return
  *     returns ProgramService
  */
 @WebEndpoint (name = "ProgramServicePort")
 public ProgramService getProgramServicePort ()
 {
  return super.getPort (ProgramServicePort, ProgramService.class);
 }

 /**
  *
  * @param features
  *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
  * @return
  *     returns ProgramService
  */
 @WebEndpoint (name = "ProgramServicePort")
 public ProgramService getProgramServicePort (WebServiceFeature... features)
 {
  return super.getPort (ProgramServicePort, ProgramService.class, features);
 }
}
