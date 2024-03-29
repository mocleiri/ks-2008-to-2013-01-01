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

package org.kuali.student.common.assembly;

import java.util.List;

import org.kuali.student.core.assembly.Assembler;
import org.kuali.student.core.assembly.data.AssemblyException;
import org.kuali.student.core.assembly.data.Metadata;
import org.kuali.student.core.assembly.data.SaveResult;
import org.kuali.student.core.validation.dto.ValidationResultInfo;

public class AddOneAssembler implements Assembler<Integer,Integer>{

	@Override
	public Integer assemble(Integer input) throws AssemblyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer disassemble(Integer input) throws AssemblyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer get(String id) throws AssemblyException {
		return new Integer(1);
	}

	@Override
	public Metadata getMetadata(String idType, String id, String type, String state)
			throws AssemblyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SaveResult<Integer> save(Integer input) throws AssemblyException {
		System.out.println("Saving Result, adding 1 to input and returning. Input:"+input);
		SaveResult<Integer> result = new SaveResult<Integer>();
		result.setValue(input + 1);
		return result;
	}

	@Override
	public List<ValidationResultInfo> validate(Integer input)
			throws AssemblyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Metadata getDefaultMetadata() throws AssemblyException {
		// TODO Auto-generated method stub
		return null;
	}

}
