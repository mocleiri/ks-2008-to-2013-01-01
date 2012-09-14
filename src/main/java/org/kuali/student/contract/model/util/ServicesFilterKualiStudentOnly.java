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
package org.kuali.student.contract.model.util;

import java.util.ArrayList;
import java.util.List;

import org.kuali.student.contract.model.Service;

/**
 *
 * @author nwright
 */
public class ServicesFilterKualiStudentOnly implements ServicesFilter {

    @Override
    public List<Service> filter(List<Service> services) {
        List<Service> list = new ArrayList();
        for (Service target : services) {
           if (target.getImplProject().startsWith("org.kuali.student.")) {
               list.add (target);
           }
        }
        return list;
    }

}
