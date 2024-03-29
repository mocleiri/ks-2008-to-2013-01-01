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

package org.kuali.student.common.ws.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.kuali.student.security.cxf.interceptors.SamlTokenCxfInInterceptor;

public class CxfJaxWsProxyClientFactory extends JaxWsProxyFactoryBean implements JaxWsClientFactory {

    private SamlTokenCxfInInterceptor samlTokenInInterceptor;
    
    @Override
    public Class<?> getServiceEndpointInterface() {
        return super.getServiceClass();
    }

    @Override
    public void setServiceEndpointInterface(Class<?> serviceEndpointInterface) {
        super.setServiceClass(serviceEndpointInterface);  
    }

    @Override
    public Object getObject() throws Exception {
        
        if(samlTokenInInterceptor != null){
            this.getInInterceptors().add(samlTokenInInterceptor);
        }
        
        return super.create();
    }

    @Override
    public Class<?> getObjectType() {
        return super.getServiceClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
    
    public void setServiceQNameString(String serviceName) {
        super.setServiceName(QName.valueOf(serviceName));
    }

    public SamlTokenCxfInInterceptor getSamlTokenInInterceptor() {
        return samlTokenInInterceptor;
    }

    public void setSamlTokenInInterceptor(SamlTokenCxfInInterceptor samlTokenInInterceptor) {
        this.samlTokenInInterceptor = samlTokenInInterceptor;
    }

}
