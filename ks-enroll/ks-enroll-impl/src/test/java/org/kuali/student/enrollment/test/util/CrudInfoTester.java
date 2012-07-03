/**
 * Copyright 2012 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * Created by mahtabme on 6/29/12
 */
package org.kuali.student.enrollment.test.util;

import org.kuali.student.r2.common.dto.AttributeInfo;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.EntityInfo;
import org.kuali.student.r2.common.dto.IdEntityInfo;
import org.kuali.student.r2.common.dto.KeyEntityInfo;
import org.kuali.student.r2.common.dto.StatusInfo;

import static org.junit.Assert.*;

/**
 * This class //TODO ...
 *
 * @author Kuali Student Team
 */
public class CrudInfoTester {

    ///////////////////////
    // DATA VARIABLES
    ///////////////////////

    /**
     * The context associated with the tester.
     */
    private ContextInfo contextInfo;

    /**
     * The principal id associated with the tester.
     */
    private String principalId;

    /**
     * The principal id 2 associated with the tester.
     */
    private String principalId2;

    /**
     * The Attribute tester.
     */
    private AttributeTester attributeTester;

    /**
     * The Meta tester.
     */
    private MetaTester metaTester;

    /**
     * The List of String tester.
     */
    private ListOfStringTester listOfStringTester;

    /**
     * The IdEntityTester tester.
     */
    private EntityInfoTester entityInfoTester;

    ///////////////////////
    // CONSTRUCTORS
    ///////////////////////

    public CrudInfoTester (String principalId, String principalId2, ContextInfo contextInfo) {
        this.principalId = principalId;
        this.principalId2 = principalId2;
        this.contextInfo = contextInfo;
        contextInfo.setPrincipalId(principalId);
        attributeTester = new AttributeTester();
        metaTester = new MetaTester();
        listOfStringTester = new ListOfStringTester();
        entityInfoTester = new EntityInfoTester();
    }

    public CrudInfoTester () {
        this ("123", "321", new ContextInfo());
    }

    ///////////////////////
    // GETTERS AND SETTERS
    ///////////////////////

    public ContextInfo getContextInfo() {
        return contextInfo;
    }

    public void setContextInfo(ContextInfo contextInfo) {
        this.contextInfo = contextInfo;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getPrincipalId2() {
        return principalId2;
    }

    public void setPrincipalId2(String principalId2) {
        this.principalId2 = principalId2;
    }

    public AttributeTester getAttributeTester() {
        return attributeTester;
    }

    public void setAttributeTester(AttributeTester attributeTester) {
        this.attributeTester = attributeTester;
    }

    public MetaTester getMetaTester() {
        return metaTester;
    }

    public void setMetaTester(MetaTester metaTester) {
        this.metaTester = metaTester;
    }

    public ListOfStringTester getListOfStringTester() {
        return listOfStringTester;
    }

    public void setListOfStringTester(ListOfStringTester listOfStringTester) {
        this.listOfStringTester = listOfStringTester;
    }

    public EntityInfoTester getEntityInfoTester() {
        return entityInfoTester;
    }

    public void setEntityInfoTester(EntityInfoTester entityInfoTester) {
        this.entityInfoTester = entityInfoTester;
    }

    ///////////////////////
    // FUNCTIONALS
    ///////////////////////

    public void initializeInfoForTestCreate (EntityInfo expected, String typeKey, String stateKey) throws Exception {
        expected.setTypeKey(typeKey);
        expected.setStateKey(stateKey);
        expected.setName("Name 1");
        getAttributeTester().add2ForCreate(expected.getAttributes());
    }

    public void testCreate (EntityInfo expected, EntityInfo actual) throws Exception {
        doCommonTests(expected, actual);
        getMetaTester().checkAfterCreate(actual.getMeta());
    }

    public void initializeInfoForTestRead (EntityInfo expected) throws Exception {
        clearAttributeIds(expected);
    }

    public void testRead (EntityInfo expected, EntityInfo actual) throws Exception {
        doCommonTests(expected, actual);
        getMetaTester().checkAfterGet(expected.getMeta(), actual.getMeta());
    }

    public void initializeInfoForTestUpdate (EntityInfo expected) throws Exception {
        clearAttributeIds(expected);
        getAttributeTester().delete1Update1Add1ForUpdate(expected.getAttributes());
    }

    public void testUpdate (EntityInfo expected, EntityInfo actual) throws Exception {
        doCommonTests(expected, actual);
        getMetaTester().checkAfterUpdate(expected.getMeta(), actual.getMeta());
    }

    public void testDelete (EntityInfo expected, EntityInfo actual, StatusInfo deleteStatus) {
        assertNotNull(deleteStatus);
        assertTrue(deleteStatus.getIsSuccess());
    }

    ///////////////////////
    // HELPERS
    ///////////////////////

    private void doCommonTests (EntityInfo expected, EntityInfo actual) throws Exception {
        if (actual instanceof IdEntityInfo) {
            assertNotNull(((IdEntityInfo) actual).getId());
            assertEquals(((IdEntityInfo) expected).getId(), ((IdEntityInfo) actual).getId());
        }
        else if (actual instanceof KeyEntityInfo) {
            assertNotNull(((KeyEntityInfo) actual).getKey());
            assertEquals(((KeyEntityInfo) expected).getKey(), ((KeyEntityInfo) actual).getKey());
        }
        getEntityInfoTester().check(expected, actual);
        getAttributeTester().check(expected.getAttributes(), actual.getAttributes());
    }

    private void clearAttributeIds(EntityInfo expected) throws Exception {
        for (AttributeInfo itemInfo : expected.getAttributes()) {
            // clear out any id's set during the persistence
            // to let the checks work properly
            itemInfo.setId(null);
        }
    }
}
