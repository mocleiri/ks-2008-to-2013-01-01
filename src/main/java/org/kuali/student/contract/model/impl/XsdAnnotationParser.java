/*
 * Copyright 2011 The Kuali Foundation
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
package org.kuali.student.contract.model.impl;

import com.sun.xml.xsom.parser.AnnotationContext;
import com.sun.xml.xsom.parser.AnnotationParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

/**
 *
 * @author nwright
 */
public class XsdAnnotationParser extends AnnotationParser
{

 private StringBuilder documentation = new StringBuilder ();

 @Override
 public ContentHandler getContentHandler (AnnotationContext context,
                                          String parentElementName,
                                          ErrorHandler handler,
                                          EntityResolver resolver)
 {
  return new XsdContentHandler (documentation);
 }

 @Override
 public Object getResult (Object existing)
 {
  return documentation.toString ().trim ();
 }
}
