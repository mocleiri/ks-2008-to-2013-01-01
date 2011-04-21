/*
 * Copyright 2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.datadictionary;

/**
 *
 * @author nwright
 */
public interface HoldsValidatorInfc {

   
    /**
     * Get the validtor used to implement the dictionary service and validation
     * @return validator that has been configured
     */
    public DataDictionaryValidator getValidator ();


    /**
     * Set the validator used to implement the dictionary service and validation
     *
     * @param validator to use
     */
    public void setValidator (DataDictionaryValidator validator);

}
