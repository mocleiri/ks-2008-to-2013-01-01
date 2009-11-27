/*
 * Copyright 2009 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may	obtain a copy of the License at
 *
 * 	http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.orchestration.orch;


import org.kuali.student.common.assembly.client.Data;
import org.kuali.student.lum.lu.assembly.data.client.PropertyEnum;


public class CreditCourseProposalInfoHelper
{
	private static final long serialVersionUID = 1;
	
	public enum Properties implements PropertyEnum
	{
		ID ("id"),
		TITLE ("title"),
		RATIONALE ("rationale"),
		REFERENCE_TYPE ("referenceType"),
		REFERENCES ("references"),
		_RUNTIME_DATA ("_runtimeData");
		
		private final String key;
		
		private Properties (final String key)
		{
			this.key = key;
		}
		
		@Override
		public String getKey ()
		{
			return this.key;
		}
	}
	private Data data;
	
	public CreditCourseProposalInfoHelper (Data data)
	{
		this.data = data;
	}
	
	public Data getData ()
	{
		return data;
	}
	
	
	public void setId (String value)
	{
		data.set (Properties.ID.getKey (), value);
	}
	
	
	public String getId ()
	{
		return (String) data.get (Properties.ID.getKey ());
	}
	
	
	public void setTitle (String value)
	{
		data.set (Properties.TITLE.getKey (), value);
	}
	
	
	public String getTitle ()
	{
		return (String) data.get (Properties.TITLE.getKey ());
	}
	
	
	public void setRationale (String value)
	{
		data.set (Properties.RATIONALE.getKey (), value);
	}
	
	
	public String getRationale ()
	{
		return (String) data.get (Properties.RATIONALE.getKey ());
	}
	
	
	public void setReferenceType (String value)
	{
		data.set (Properties.REFERENCE_TYPE.getKey (), value);
	}
	
	
	public String getReferenceType ()
	{
		return (String) data.get (Properties.REFERENCE_TYPE.getKey ());
	}
	
	
	public void setReferences (Data value)
	{
		data.set (Properties.REFERENCES.getKey (), value);
	}
	
	
	public Data getReferences ()
	{
		return (Data) data.get (Properties.REFERENCES.getKey ());
	}
	
	
	public void set_runtimeData (RuntimeDataHelper value)
	{
		data.set (Properties._RUNTIME_DATA.getKey (), value.getData ());
	}
	
	
	public RuntimeDataHelper get_runtimeData ()
	{
		return new RuntimeDataHelper ((Data) data.get (Properties._RUNTIME_DATA.getKey ()));
	}
	
}

