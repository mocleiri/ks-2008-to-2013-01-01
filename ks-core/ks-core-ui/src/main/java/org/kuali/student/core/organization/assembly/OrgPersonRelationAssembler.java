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

package org.kuali.student.core.organization.assembly;

import static org.kuali.student.core.assembly.util.AssemblerUtils.addVersionIndicator;
import static org.kuali.student.core.assembly.util.AssemblerUtils.getVersionIndicator;
import static org.kuali.student.core.assembly.util.AssemblerUtils.isCreated;
import static org.kuali.student.core.assembly.util.AssemblerUtils.isDeleted;
import static org.kuali.student.core.assembly.util.AssemblerUtils.isModified;
import static org.kuali.student.core.assembly.util.AssemblerUtils.isUpdated;

import java.util.ArrayList;
import java.util.List;

import org.kuali.student.common.ui.client.mvc.DataModel;
import org.kuali.student.common.ui.client.mvc.DataModelDefinition;
import org.kuali.student.core.assembly.Assembler;
import org.kuali.student.core.assembly.data.AssemblyException;
import org.kuali.student.core.assembly.data.Data;
import org.kuali.student.core.assembly.data.Metadata;
import org.kuali.student.core.assembly.data.QueryPath;
import org.kuali.student.core.assembly.data.SaveResult;
import org.kuali.student.core.assembly.data.Data.Property;
import org.kuali.student.core.dto.MetaInfo;
import org.kuali.student.core.dto.StatusInfo;
import org.kuali.student.core.exceptions.DoesNotExistException;
import org.kuali.student.core.organization.assembly.data.client.org.OrgHelper;
import org.kuali.student.core.organization.assembly.data.client.org.OrgPersonHelper;
import org.kuali.student.core.organization.dto.OrgPersonRelationInfo;
import org.kuali.student.core.organization.service.OrganizationService;
import org.kuali.student.core.validation.dto.ValidationResultInfo;

public class OrgPersonRelationAssembler implements Assembler<Data, OrgPersonHelper>{
    private OrganizationService orgService;
    private Metadata metadata;
    private DataModel orgPersonModel = new DataModel();
    public static final String PERSON_PATH                  = "orgPersonRelationInfo";
    
    public void setMetaData(Metadata metadata){
        this.metadata=metadata;
        
    }

    public void setOrgService(OrganizationService service){
        orgService = service;
    }
    @Override
    public Data assemble(OrgPersonHelper input) throws AssemblyException {
        // TODO Neerav Agrawal - THIS METHOD NEEDS JAVADOCS
        return null;
    }

    @Override
    public OrgPersonHelper disassemble(Data input) throws AssemblyException {
        // TODO Neerav Agrawal - THIS METHOD NEEDS JAVADOCS
        return null;
    }

    @Override
    public Data get(String id) throws AssemblyException {
        List<OrgPersonRelationInfo> relations = new ArrayList<OrgPersonRelationInfo>();
        Data orgRelationMap = null;
        try{
            relations = orgService.getOrgPersonRelationsByOrg(id);
            orgRelationMap = buildOrgPersonRelationMap(relations);
        }
        catch(DoesNotExistException dnee){
            return null;
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return orgRelationMap;
    }



    @Override
    public SaveResult<Data> save(Data input) throws AssemblyException {
        
        addPersonRelation(input);
        SaveResult<Data> result = new SaveResult<Data>();
        List<ValidationResultInfo> validationResults = validate(input);
        result.setValue(input);
        return result;
    }



    @Override
    public List<ValidationResultInfo> validate(Data input) throws AssemblyException {
        // TODO Neerav Agrawal - THIS METHOD NEEDS JAVADOCS
        return null;
    }
    
    private void addPersonRelation(Data input) throws AssemblyException{
        if (input == null) {
            return;
        }
        DataModelDefinition def = new DataModelDefinition(metadata);
        orgPersonModel.setDefinition(def);
        //Set this for readonly permission
        QueryPath metaPath = QueryPath.concat(null, PERSON_PATH);
        Metadata orgPersonMeta =orgPersonModel.getMetadata(metaPath);
        for (Property p : (Data)input.get("orgPersonRelationInfo")) {
            OrgPersonHelper orgPersonHelper=  OrgPersonHelper.wrap((Data)p.getValue());
            if (isUpdated(orgPersonHelper.getData())) {
                if (orgPersonMeta.isCanEdit()) {
                    OrgPersonRelationInfo orgPersonRelationInfo = buildOrgPersonRelationInfo(orgPersonHelper);
                    orgPersonRelationInfo.setId(orgPersonHelper.getId());
                    try {
                        OrgPersonRelationInfo result = orgService.updateOrgPersonRelation(orgPersonHelper.getId(), orgPersonRelationInfo);
                        addVersionIndicator(orgPersonHelper.getData(), OrgPersonRelationInfo.class.getName(), result.getId(), result.getMetaInfo().getVersionInd());
                    } catch (Exception e) {
                        throw new AssemblyException();
                    }
                }
            }
            else if(isDeleted(orgPersonHelper.getData())){
                try{
                    if(orgPersonHelper.getId()!=null){
                        StatusInfo  result = orgService.removeOrgPersonRelation(orgPersonHelper.getId());
                    }
                }
                catch(Exception e ){
                    e.printStackTrace();
                    throw(new AssemblyException());
                }
            }
            else if(isCreated(orgPersonHelper.getData())){
                orgPersonHelper.setOrgId((OrgHelper.wrap((Data)input.get("orgInfo")).getId()));
                OrgPersonRelationInfo orgPersonRelationInfo = buildOrgPersonRelationInfo(orgPersonHelper);
                try{
                    OrgPersonRelationInfo  result = orgService.createOrgPersonRelation(orgPersonHelper.getOrgId(), orgPersonHelper.getPersonId(), orgPersonHelper.getTypeKey(), orgPersonRelationInfo);
                    orgPersonHelper.setId(result.getId());
                    addVersionIndicator(orgPersonHelper.getData(),OrgPersonRelationInfo.class.getName(),result.getId(),result.getMetaInfo().getVersionInd());
                }
                catch(Exception e ){
                    e.printStackTrace();
                    throw new AssemblyException();
                }
            }
           
          
        }
    }
    
    private OrgPersonRelationInfo buildOrgPersonRelationInfo(OrgPersonHelper orgPersonHelper){
        OrgPersonRelationInfo orgPersonRelationInfo = new OrgPersonRelationInfo();
        orgPersonRelationInfo.setOrgId(orgPersonHelper.getOrgId());
        orgPersonRelationInfo.setPersonId(orgPersonHelper.getPersonId());
        orgPersonRelationInfo.setType(orgPersonHelper.getTypeKey());
        orgPersonRelationInfo.setEffectiveDate(orgPersonHelper.getEffectiveDate());
        orgPersonRelationInfo.setExpirationDate(orgPersonHelper.getExpirationDate());
        
        if (isModified(orgPersonHelper.getData())) {
            if (isUpdated(orgPersonHelper.getData())) {
                MetaInfo metaInfo = new MetaInfo();
                orgPersonRelationInfo.setMetaInfo(metaInfo);
                orgPersonRelationInfo.setId(orgPersonHelper.getId());
            }
            else if (isDeleted(orgPersonHelper.getData())) {
            }
            else if (isCreated(orgPersonHelper.getData())) {
            } 
        }
        if(orgPersonRelationInfo.getMetaInfo()!=null){
            orgPersonRelationInfo.getMetaInfo().setVersionInd(getVersionIndicator(orgPersonHelper.getData()));
        }
        return orgPersonRelationInfo;
    }
    
    private Data buildOrgPersonRelationMap( List<OrgPersonRelationInfo> relations){
        Data orgRelations = new Data();
        try {
        int count =0;

        DataModelDefinition def = new DataModelDefinition(metadata);
        orgPersonModel.setDefinition(def);
        //Set this for readonly permission
        QueryPath metaPath = QueryPath.concat(null, PERSON_PATH);
        Metadata orgPersonMeta =orgPersonModel.getMetadata(metaPath);
        for(OrgPersonRelationInfo relation:relations){
            Data relationMap = new Data();
            OrgPersonHelper orgPersonHelper = OrgPersonHelper.wrap(relationMap);
            orgPersonHelper.setId(relation.getId());
            orgPersonHelper.setOrgId(relation.getOrgId());
            if(!orgPersonMeta.isCanEdit()){
                
                    orgPersonHelper.setTypeKey(orgService.getOrgPersonRelationType(relation.getType()).getName());
               
            }
            else{
                orgPersonHelper.setTypeKey(relation.getType());
            }
            
            orgPersonHelper.setPersonId(relation.getPersonId());
            orgPersonHelper.setEffectiveDate(relation.getEffectiveDate());
            orgPersonHelper.setExpirationDate(relation.getExpirationDate());
            addVersionIndicator(orgPersonHelper.getData(),OrgPersonRelationInfo.class.getName(),relation.getId(),relation.getMetaInfo().getVersionInd());
            orgRelations.set(count,orgPersonHelper.getData());
            count = count +1;
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return orgRelations;
    }
    @Override
    public Metadata getMetadata(String idType, String id, String type, String state) throws AssemblyException {
        // TODO Neerav Agrawal - THIS METHOD NEEDS JAVADOCS
        return null;
    }

	@Override
	public Metadata getDefaultMetadata() throws AssemblyException {
		// TODO Auto-generated method stub
		return null;
	}

}
