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

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 *
 * @author nwright
 */
public class XsdContentHandler implements ContentHandler
{

 private StringBuilder documentation;

 public XsdContentHandler (StringBuilder documentation)
 {
  this.documentation = documentation;
 }
 
 private boolean parsingDocumentation = false;

 @Override
 public void characters (char[] ch, int start, int length)
   throws SAXException
 {
  if (parsingDocumentation)
  {
   documentation.append (ch, start, length);
  }
 }

 @Override
 public void endElement (String uri, String localName, String name)
   throws SAXException
 {
  if (localName.equals ("documentation"))
  {
   parsingDocumentation = false;
  }
 }

 @Override
 public void startElement (String uri, String localName, String name,
                           Attributes atts) throws SAXException
 {
  if (localName.equals ("documentation"))
  {
   parsingDocumentation = true;
  }
 }

 @Override
 public void endDocument () throws SAXException
 {
  // do nothing
 }

 @Override
 public void endPrefixMapping (String prefix) throws SAXException
 {
  // do nothing
 }

 @Override
 public void ignorableWhitespace (char[] ch, int start, int length) throws SAXException
 {
   // do nothing
 }

 @Override
 public void processingInstruction (String target, String data) throws SAXException
 {
    // do nothing
 }

 @Override
 public void setDocumentLocator (Locator locator)
 {
    // do nothing
 }

 @Override
 public void skippedEntity (String name) throws SAXException
 {
    // do nothing
 }

 @Override
 public void startDocument () throws SAXException
 {
    // do nothing
 }

 @Override
 public void startPrefixMapping (String prefix, String uri) throws SAXException
 {
    // do nothing
 }


}

