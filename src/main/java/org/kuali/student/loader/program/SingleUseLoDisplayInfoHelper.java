/*
 * Copyright 2010 The Kuali Foundation
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
package org.kuali.student.loader.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.kuali.student.loader.util.AttributeInfoHelper;
import org.kuali.student.loader.util.RichTextInfoHelper;
import org.kuali.student.lum.course.dto.LoDisplayInfo;
import org.kuali.student.lum.lo.dto.LoInfo;

/**
 *
 * @author nwright
 */
public class SingleUseLoDisplayInfoHelper
{

 public  List <LoDisplayInfo> parse (String losStr, Date effectiveDate)
 {
  if (losStr == null)
  {
   return null;
  }
  if (losStr.trim ().isEmpty ())
  {
   return null;
  }
  String[] lo =losStr.split ("\n");
  List <LoDisplayInfo> ldis = new ArrayList (lo.length);
  for (int i = 0; i < lo.length; i++)
  {
   LoDisplayInfo ldi = new LoDisplayInfo ();
   LoInfo li = new LoInfo ();
   li.setLoRepositoryKey ("kuali.loRepository.key.singleUse");
   li.setDesc (new RichTextInfoHelper ().getFromPlain (lo[i]));
   li.setType ("kuali.lo.type.singleUse");
   li.setState ("Active");
   li.setEffectiveDate (effectiveDate);
   li.setName ("SINGLE USE LO");
   ldi.setLoInfo (li);
   li.setAttributes (new AttributeInfoHelper ().setValue ("sequence",  "" + i, li.getAttributes ()));
   ldis.add (ldi);
  }
  return ldis;
 
 }


 
}
