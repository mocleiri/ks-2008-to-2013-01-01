/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kuali.student.dictionary;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author nwright
 */
public abstract class JavaClassWriter extends XmlWriter
{

 private String rootDirectory;
 private String packageName;
 private String className;
 private String fileName;
 private String directory;
 private ByteArrayOutputStream body;
 protected Set<String> imports;

 public JavaClassWriter (String rootDirectory, String packageName,
                         String className)
 {
  super ();
  this.body = new ByteArrayOutputStream (1000);
  this.setOut (new PrintStream (body));
  this.setIndent (0);
  this.rootDirectory = rootDirectory;

  this.packageName = packageName;
  this.className = className;
  this.fileName =
   new JavaClassFileNameBuilder (rootDirectory, packageName, className).build ();
  this.directory =
   new JavaClassFileNameBuilder (rootDirectory, packageName, className).
   buildDirectory ();
  this.imports = new TreeSet ();
 }

 public void writeHeader ()
 {
  indentPrintln ("/*");
  indentPrintln (" * Copyright 2009 The Kuali Foundation");
  indentPrintln (" *");
  indentPrintln (" * Licensed under the Educational Community License, Version 2.0 (the \"License\");");
  indentPrintln (" * you may not use this file except in compliance with the License.");
  indentPrintln (" * You may	obtain a copy of the License at");
  indentPrintln (" *");
  indentPrintln (" * 	http://www.osedu.org/licenses/ECL-2.0");
  indentPrintln (" *");
  indentPrintln (" * Unless required by applicable law or agreed to in writing, software");
  indentPrintln (" * distributed under the License is distributed on an \"AS IS\" BASIS,");
  indentPrintln (" * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
  indentPrintln (" * See the License for the specific language governing permissions and");
  indentPrintln (" * limitations under the License.");
  indentPrintln (" */");
  indentPrintln ("package " + packageName + ";");
  indentPrintln ("");
 }

 public void writeImports ()
 {
  if (imports.size () == 0)
  {
   return;
  }


  for (String imprt : imports)
  {
   // exclude imports from same package
   if (imprt.startsWith (packageName))
   {
    // don't exclude imports for same package that are including nested classes
    if ( ! imprt.substring (packageName.length ()).contains ("."))
    {
     continue;
    }
   }
   indentPrintln ("import " + imprt + ";");
  }
  indentPrintln ("");
 }

 public void writeJavaClassAndImportsOutToFile ()
 {

  File dir = new File (this.directory);
  System.out.println ("Writing " + fileName + " to " + dir.getAbsolutePath ());

  if ( ! dir.exists ())
  {
   if ( ! dir.mkdirs ())
   {
    throw new DictionaryExecutionException ("Could not create directory " +
     this.directory);
   }
  }
  try
  {
   PrintStream out = new PrintStream (new FileOutputStream (fileName, false));
   this.setOut (out);
  }
  catch (FileNotFoundException ex)
  {
   throw new DictionaryExecutionException (ex);
  }
  writeHeader ();
  indentPrintln ("");
  writeImports ();
  indentPrintln ("");
  indentPrintln (body.toString ());
 }

 public void openBrace ()
 {
  indentPrintln ("{");
  incrementIndent ();
 }

 public void closeBrace ()
 {
  decrementIndent ();
  indentPrintln ("}");
 }

}
