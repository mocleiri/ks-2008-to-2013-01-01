/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kuali.student.dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kuali.student.dictionary.OrchestrationObjectField.FieldTypeCategory;

/**
 *
 * @author nwright
 */
public class OrchestrationObjectsWriter
{

 private DictionaryModel model;
 private String directory;
 public static final String ROOT_PACKAGE = "org.kuali.student.orchestration";

 public OrchestrationObjectsWriter (DictionaryModel model, String directory)
 {
  this.model = model;
  this.directory = directory;
 }

 /**
  * Write out the entire file
  * @param out
  */
 public void write ()
 {
  this.validate ();

  // first do from message structures
  Map<String, OrchestrationObject> orchObjs =
   getOrchestrationObjectsFromMessageStructures ();
  orchObjs.putAll (this.getOrchestrationObjectsFromOrchObjs ());

  // do the helpers first
  for (OrchestrationObject oo : orchObjs.values ())
  {
   System.out.println ("Writing out " + oo.getFullyQualifiedJavaClassHelperName ());
   OrchestrationObjectHelperWriter writer =
    new OrchestrationObjectHelperWriter (model, directory, orchObjs, oo);
   writer.write ();
  }

  // do the metadata next
  for (OrchestrationObject oo : orchObjs.values ())
  {
   System.out.println ("Writing out " + oo.getFullyQualifiedJavaClassMetadataName ());
   OrchestrationObjectMetadataWriter writer =
    new OrchestrationObjectMetadataWriter (model, directory, orchObjs, oo);
   writer.write ();
  }
 }

 private void validate ()
 {
  Collection<String> errors =
   new OrchestrationModelValidator (model).validate ();
  if (errors.size () > 0)
  {
   StringBuffer buf = new StringBuffer ();
   buf.append (errors.size () +
    " errors found while validating the spreadsheet.");
   int cnt = 0;
   for (String msg : errors)
   {
    cnt ++;
    buf.append ("\n");
    buf.append ("*error*" + cnt + ":" + msg);
   }

   throw new DictionaryValidationException (buf.toString ());
  }

 }

 private Map<String, OrchestrationObject> getOrchestrationObjectsFromMessageStructures ()
 {
  Map<String, OrchestrationObject> map = new HashMap ();
  for (XmlType xmlType : model.getXmlTypes ())
  {
   // TODO: remove this hack once all java packages exist so we are not trying to copy them.
   //       ALSO remove the one below
   //if (xmlType.getJavaPackage ().equals (""))
   //{
   // continue;
   // }
   if (xmlType.getPrimitive ().equals ("Complex"))
   {
    OrchestrationObject obj = new OrchestrationObject ();
    map.put (xmlType.getName ().toLowerCase (), obj);
    obj.setName (xmlType.getName ());
    obj.setInfoPackagePath (xmlType.getJavaPackage ());
    obj.setOrchestrationPackagePath (ROOT_PACKAGE + ".base");

    // these orchestratration data objects get assembled from versions of themself
    // i.e CluInfoData from CluInfo
    obj.setAssembleFromClass (xmlType.getName ());
    obj.setHasOwnCreateUpdate (xmlType.getHasOwnCreateUpdate ().equals ("true"));
    List<OrchestrationObjectField> fields = new ArrayList ();
    obj.setFields (fields);
    for (MessageStructure ms : model.getMessageStructures ())
    {
     if (ms.getXmlObject ().equalsIgnoreCase (xmlType.getName ()))
     {
      // TODO: remove this hack once all java packages exist so we are not trying to copy them.
      //       ALSO remove the one above
//      Field dictField = new ModelFinder (model).findField (ms.getId ());
//      if (dictField == null)
//      {
//       throw new DictionaryValidationException ("could not find corresponding field entry for message structure entry " +
//        ms.getId ());
//      }
//      XmlType fieldXmlType = new ModelFinder (model).findXmlType (dictField.
//       getXmlType ());
//      if (fieldXmlType.getPrimitive ().equals ("Complex"))
//      {
//       if (fieldXmlType.getJavaPackage ().equals (""))
//       {
//        continue;
//       }
//      }
      OrchestrationObjectField field = new OrchestrationObjectField ();
      fields.add (field);
      field.setParent (obj);
      field.setName (ms.getShortName ());
      field.setType (calcType (ms.getType ()));
      field.setFieldTypeCategory (
       calcFieldTypeCategory (field, calcIsList (ms.getType ()), true));
     }

    }
   }
  }
  return map;
 }

 public boolean calcIsList (String type)
 {
  if (type.endsWith ("List"))
  {
   return true;
  }

  return false;
 }

 public String calcType (String type)
 {
  if (type.endsWith ("List"))
  {
   type = type.substring (0, type.length () - "List".length ());
  }

  return type;
 }

 private FieldTypeCategory calcFieldTypeCategory (OrchestrationObjectField field,
                                                  boolean isAList,
                                                  boolean mustBeInXmlTypes)
 {
  if (field.getType ().equalsIgnoreCase ("attributeInfo"))
  {
   return FieldTypeCategory.DYNAMIC_ATTRIBUTE;
  }
  if (isAList)
  {
   return FieldTypeCategory.LIST;
  }
  if (field.getType ().equalsIgnoreCase ("Complex-Inline"))
  {
   return FieldTypeCategory.COMPLEX_INLINE;
  }
  XmlType xmlType = new ModelFinder (model).findXmlType (field.getType ());
  if (xmlType == null)
  {
   if (mustBeInXmlTypes)
   {
    throw new DictionaryValidationException ("No XmlType found for field type " +
     field.getType () + " " + field.getName ());
   }
   // if not found it must be a complex orchestration object
   return FieldTypeCategory.COMPLEX;
  }

  if (xmlType.getPrimitive ().equalsIgnoreCase ("Primitive"))
  {
   return FieldTypeCategory.PRIMITIVE;
  }

  if (xmlType.getPrimitive ().equalsIgnoreCase ("Mapped String"))
  {
   return FieldTypeCategory.MAPPED_STRING;
  }

  if (xmlType.getPrimitive ().equalsIgnoreCase ("Complex"))
  {
   return FieldTypeCategory.COMPLEX;
  }

  throw new DictionaryValidationException ("Unknown/unhandled xmlType.primtive value, " +
   xmlType.getPrimitive () + ", for field type " +
   field.getType () + " for field " + field.getName ());
 }

 private Map<String, OrchestrationObject> getOrchestrationObjectsFromOrchObjs ()
 {
  Map<String, OrchestrationObject> map = new HashMap ();
  OrchestrationObject parentObj = null;
  OrchestrationObjectField childField = null;
  for (OrchObj orch : model.getOrchObjs ())
  {
   // reset and add the object
   if ( ! orch.getParent ().equals (""))
   {
    parentObj = new OrchestrationObject ();
    // TODO: worry about qualifying the name somehow so we can support different orchestrations
    map.put (orch.getParent ().toLowerCase (), parentObj);
    parentObj.setName (orch.getParent ());
    // TODO: add this to spreadsheet
    parentObj.setAssembleFromClass ("TODO: add this to spreadsheet");
    parentObj.setOrchestrationPackagePath (ROOT_PACKAGE + ".orch");
    parentObj.setHasOwnCreateUpdate (true);
    parentObj.setFields (new ArrayList ());
    continue;

   }

   if ( ! orch.getChild ().equals (""))
   {
    childField = new OrchestrationObjectField ();
    parentObj.getFields ().add (childField);
    childField.setParent (parentObj);
    childField.setName (orch.getChild ());
    childField.setType (calcType (orch.getXmlType ()));
    childField.setFieldTypeCategory (
     calcFieldTypeCategory (childField, calcIsCardList (orch.getCard1 ()), false));
    continue;
   }
   if ( ! orch.getGrandChild ().equals (""))
   {
    OrchestrationObjectField grandChildField = new OrchestrationObjectField ();
    OrchestrationObject inlineObj = childField.getInlineObject ();
    if (inlineObj == null)
    {
     inlineObj = new OrchestrationObject ();
     childField.setInlineObject (inlineObj);
     inlineObj.setInlineField (childField);
     inlineObj.setName (childField.getName ());
     inlineObj.setAssembleFromClass ("TODO: add this to spreadsheet");
     inlineObj.setOrchestrationPackagePath (ROOT_PACKAGE + ".orch");
     inlineObj.setHasOwnCreateUpdate (false);
     inlineObj.setFields (new ArrayList ());
    }
    inlineObj.getFields ().add (grandChildField);
    grandChildField.setParent (inlineObj);
    grandChildField.setName (orch.getGrandChild ());
    grandChildField.setType (calcType (orch.getXmlType ()));
    grandChildField.setFieldTypeCategory (
     calcFieldTypeCategory (grandChildField, calcIsCardList (orch.getCard2 ()), false));
    continue;
   }

  }
  return map;
 }

 public boolean calcIsCardList (String type)
 {
  // TODO: make this logic more robust to handle cases like 1-5 etc
  if (type.endsWith ("-N"))
  {
   return true;
  }

  return false;
 }

}
