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

package org.kuali.student.core.organization.ui.client.mvc.model;

import java.io.Serializable;
import java.util.List;

public class MultipleFieldInfoImpl implements FieldInfo,Serializable{

    private String itemLabel;
    private String addItemLabel;
    private String key;
    private String widget;
    private List<FieldInfo> fields;

    public String getLabel(){
        return itemLabel;
    }
    
    public void setLabel(String itemLabel){
        this.itemLabel=itemLabel;
    }
    
    public String getAddItemLabel(){
        return addItemLabel;
    }
    
    public void setAddItemLabel(String addItemLabel){
        this.addItemLabel=addItemLabel;
    }
    
    public String getKey(){
        return key;
    }
    
    public void setKey(String key){
        this.key = key;
    }
    
    public String getWidget(){
        return widget;
    }
    public void setWidget(String widget){
        this.widget=widget;
    }
    
    public List<FieldInfo> getFields(){
        return fields;
    }
    
    public void setFields(List<FieldInfo> fields){
        this.fields=fields;
    }
    
}
