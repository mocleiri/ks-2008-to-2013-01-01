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
package org.kuali.student.search;

import java.util.ArrayList;
import java.util.List;
import org.kuali.student.dictionary.SpreadsheetReader;
import org.kuali.student.dictionary.WorksheetReader;

/**
 * Loads a spreadsheet using either a google or excel reader
 * @author nwright
 */
public class SpreadsheetLoader implements Spreadsheet
{

 private SpreadsheetReader spreadsheetReader;

 public SpreadsheetLoader (SpreadsheetReader spreadsheetReader)
 {
  this.spreadsheetReader = spreadsheetReader;
 }

 public List<SearchType> getSearchTypes ()
 {
  WorksheetReader worksheetReader =
   spreadsheetReader.getWorksheetReader ("Searches");
  List<SearchType> list = new ArrayList (worksheetReader.getEstimatedRows ());
  SearchType searchType = null;
  while (worksheetReader.next ())
  {
   String type = getFixup (worksheetReader, "Type");
   if (type.equals ("Search"))
   {
    searchType = new SearchType ();
    loadRow (worksheetReader, searchType);
    list.add (searchType);
   }
   else if (type.equals ("SQL"))
   {
    SearchSql sql = new SearchSql ();
    loadRow (worksheetReader, sql);
    searchType.setSql (sql);
   }
   else if (type.equals ("Criteria"))
   {
    SearchCriteria criteria = new SearchCriteria ();
    searchType.setCriteria (criteria);
    loadRow (worksheetReader, criteria);
   }
   else if (type.equals ("Parm"))
   {
    SearchParameter parm = new SearchParameter ();
    searchType.getCriteria ().getParameters ().add (parm);
    loadRow (worksheetReader, parm);
   }
   else if (type.equals ("Result"))
   {
    SearchResults results = new SearchResults ();
    searchType.setResults (results);
    loadRow (worksheetReader, results);
   }
   else if (type.equals ("Column"))
   {
    SearchResultColumn col = new SearchResultColumn ();
    searchType.getResults ().getResultColumns ().add (col);
    loadRow (worksheetReader, col);
   }
  }
  return list;
 }

 private void loadRow (WorksheetReader worksheetReader, SearchRow row)
 {
  row.setKey (getFixup (worksheetReader, "Key"));
  row.setType (getFixup (worksheetReader, "Type"));
  row.setName (getFixup (worksheetReader, "Name"));
  row.setDescription (getFixup (worksheetReader, "Description"));
  row.setDataType (getFixup (worksheetReader, "DataType"));
  row.setStatus (getFixup (worksheetReader, "Status"));
  row.setComments (getFixup (worksheetReader, "Comments"));
 }

 private String get (WorksheetReader worksheetReader, String colName)
 {
  String value = worksheetReader.getValue (colName);
  if (value == null)
  {
   return "";
  }
  value = value.trim ();
  return value;
 }

 private String getFixup (WorksheetReader worksheetReader, String colName)
 {
  return fixup (get (worksheetReader, colName));
 }

 private String fixup (String str)
 {
  if (str.equals ("FALSE"))
  {
   return "false";
  }
  if (str.equals ("TRUE"))
  {
   return "true";
  }
  return str;
 }

}
