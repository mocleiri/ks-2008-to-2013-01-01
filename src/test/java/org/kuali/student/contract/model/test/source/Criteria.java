/*
 * Copyright 2009 The Kuali Foundation
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
package org.kuali.student.contract.model.test.source;

import java.util.List;

/**
 * Criteria for a generic query
 */
public interface Criteria {

    /**
     * The list of comparisons to be applied
     * TODO: Decide if null or empty list is ok?  I.e. can you get ALL?
     * @name List of Comparisons
     * @impl These are implementation notes you should see
     * that they go on for a while and a while and a while
     * and never really stop
     * @return list of comparisons
     */
    public List<? extends Comparison> getComparisons();

    /**
     * Name: Maximum Results
     *
     * Get the maximum number of results to be returned
     *
     * Specify Null if do not want to limit the results
     */
    public Integer getMaxResults();
}
