/**
 * Copyright 2010 The Kuali Foundation Licensed under the
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
 */

package org.kuali.student.lum.ui.requirements.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.kuali.student.common.ui.client.widgets.table.Node;
import org.kuali.student.common.ui.client.widgets.table.Token;
import org.kuali.student.core.dto.MetaInfo;
import org.kuali.student.core.dto.RichTextInfo;
import org.kuali.student.core.ws.binding.JaxbAttributeMapListAdapter;
import org.kuali.student.brms.statement.dto.ReqComponentInfo;
import org.kuali.student.brms.statement.dto.StatementInfo;
import org.kuali.student.brms.statement.dto.StatementOperatorTypeKey;
import org.kuali.student.brms.statement.dto.StatementTreeViewInfo;

public class StatementVO extends Token implements Serializable {

    private static final long serialVersionUID = 1L;
    private StatementInfo statementInfo;
    private List<ReqComponentVO> reqComponentVOs = new ArrayList<ReqComponentVO>();
    private List<StatementVO> statementVOs = new ArrayList<StatementVO>();
    private boolean checkBoxOn;
    
    public StatementVO() {        
    }
    
    public StatementVO(StatementInfo statementInfo) {        
        setStatementInfo(statementInfo);
    }        
    
    public boolean isCheckBoxOn() {
        return checkBoxOn;
    }

    public void setCheckBoxOn(boolean checkBoxOn) {
        this.checkBoxOn = checkBoxOn;
    }

    public void printTree(Node node) {        
        int level = 0;
        ReqComponentVO content;
//        level++;
        
        if (node == null) {
            System.out.println("null node found");
            return;
        }
        
        level = node.getDistance(node);
        if (node.getUserObject() != null) {
            Token token = (Token) node.getUserObject();
            //content = (ReqComponentVO) token.value;
            System.out.println("Node level " + level + ", content: " + token.value);
        }
        else System.out.println("Node user object null, level: " + level);
        for (int i = 0; i < node.getChildCount(); i++) {
            Node child = node.getChildAt(i);
            if (child.isLeaf()) {
                Token token = (Token) child.getUserObject();
                content = (ReqComponentVO) child.getUserObject();
                System.out.println("Node level " + child.getDistance(child) + ", content: " + content);
            } else {
                printTree(child);
            }
        }
    }
       
    /**
     * Gets the immediate parent statement of reqComponentVO
     * Example: (a and b) or (c and d) or (e)
     *     Where the statements are enclosed in brackets.  So in this example
     *     there are 3 statements.  Namely (a and b), (c and d), and (e).  
     *     There are 5 requirement components a, b, c, d, and e. If
     *     requirement component b is passed in as a parameter, then (a and b)
     *     is returned.  If e is passed in, then (e) is returned.  If d is
     *     is passed in, then (c and d) is returned.
     * @param reqComponentVO
     * @return
     */
    public StatementVO getEnclosingStatementVO(StatementVO root, ReqComponentVO reqComponentVO) {
        StatementVO result = null;
        result = doGetEnclosingStatementVO(root, reqComponentVO);
        return result;
    }
    
    private StatementVO doGetEnclosingStatementVO(StatementVO statementVO, 
            ReqComponentVO reqComponentVO) {
        StatementVO result = null;
        List<StatementVO> statementVOs =
            (statementVO == null)? null : statementVO.getStatementVOs();
        List<ReqComponentVO> reqComponentVOs =
            (statementVO == null)? null : statementVO.getReqComponentVOs();
        
        if (statementVOs != null && !statementVOs.isEmpty()) {
            for (StatementVO subStatementVO : statementVOs) {
                List<ReqComponentVO>subStatementReqComponentVOs = subStatementVO.getReqComponentVOs();
                if (subStatementReqComponentVOs.size() == 1) {
                    if (subStatementReqComponentVOs.get(0) == reqComponentVO) {
                        result = statementVO;
                        break;
                    }
                }
                else {
                    result = doGetEnclosingStatementVO(subStatementVO, reqComponentVO);
                    // found the enclosing statement exit
                    if (result != null) {
                        break;
                    }
                }
            }
        } else if (reqComponentVOs != null && !reqComponentVOs.isEmpty()) {
            for (ReqComponentVO leafReqComponentVO : reqComponentVOs) {
                if (leafReqComponentVO == reqComponentVO) {
                    result = statementVO;
                    break;
                }
            }
        }
        return result;
    }
    
    public StatementVO getParentStatementVO(StatementVO root, StatementVO nodeStatement) {
        StatementVO parentStatementVO = null;
        if (nodeStatement == root) {
            parentStatementVO = null;
        } else {
            parentStatementVO = doGetParentStatementVO(root, nodeStatement);
        }
        return parentStatementVO;
    }
    
    private StatementVO doGetParentStatementVO(StatementVO statementVO, StatementVO nodeStatement) {
        StatementVO parentStatementVO = null;
        if (statementVO.getStatementVOCount() > 0) {
            for (StatementVO childStatementVO : statementVO.getStatementVOs()) {
                if (childStatementVO == nodeStatement) {
                    return statementVO;
                } else {
                    parentStatementVO = doGetParentStatementVO(childStatementVO, nodeStatement);
                }
            }
        }
        return parentStatementVO;
    }
    
    /**
     * returns the number of leaf requirement componentVO of the this statement
     * excluding those compound sub statements.
     * e.g. if this method is called on S1, the return value will be 2. 
     * <pre>
     *                    S1:OR
     *           S2:OR    S3:OR   S4:AND
     *             a        b     c d e
     * </pre>
     * @return
     */
    public int getImmediateChildReqComponentCount() {
        int result = 0;
        if (reqComponentVOs != null && !reqComponentVOs.isEmpty()) {
            result = reqComponentVOs.size();
        } else if (statementVOs != null && !statementVOs.isEmpty()) {
            for (StatementVO subStatementVO : statementVOs) {
                List<ReqComponentVO> subStatementReqComponentVOs =
                    subStatementVO.getReqComponentVOs();
                if (subStatementReqComponentVOs != null &&
                        subStatementReqComponentVOs.size() == 1) {
                    result++;
                }
            }
        }
        return result;
    }
    
    private void validate() {
        if (statementVOs != null && !statementVOs.isEmpty() &&
                reqComponentVOs != null && !reqComponentVOs.isEmpty()) {
            throw new java.lang.IllegalStateException(
                    "Requirement components and statements can not exist together in a statement");
        }
        checkDuplicateRC(this, new ArrayList<ReqComponentVO>());
    }
    
    private void checkDuplicateRC(StatementVO statementVO, List<ReqComponentVO> seenRCs) {
        if (statementVO.getStatementVOs() != null &&
                !statementVO.getStatementVOs().isEmpty()) {
            for (StatementVO childStatementVO : statementVO.getStatementVOs()) {
                checkDuplicateRC(childStatementVO, seenRCs);
            }
        } else {
            if (statementVO.getReqComponentVOs() != null &&
                    !statementVO.getReqComponentVOs().isEmpty()) {
                for (ReqComponentVO childReqComponent : statementVO.getReqComponentVOs()) {
                    if (seenRCs.contains(childReqComponent)) {
                        throw new java.lang.IllegalStateException(
                                "statement and sub statements cannot have duplicated components");
                    } else {
                        seenRCs.add(childReqComponent);
                    }
                }
            }
        }
    }
    
    public void addStatementVO(StatementVO statementVO) {
        doAddStatementVO(statementVO);
        validate();
    }
    
    private void doAddStatementVO(StatementVO statementVO) {
        // if there are currently requirement components in this StatementVO
        // move all the existing requirement components into separate wrapping 
        // StatementVOs
        if (reqComponentVOs != null && !reqComponentVOs.isEmpty()) {
            List<ReqComponentVO> tempReqComponentVOs = 
                new ArrayList<ReqComponentVO>(reqComponentVOs);
            for (ReqComponentVO currReqComponentVO : tempReqComponentVOs) {
                StatementVO wrapStatementVO = new StatementVO(statementInfo);
                wrapStatementVO.addReqComponentVO(currReqComponentVO);
                reqComponentVOs.remove(currReqComponentVO);
                statementVOs.add(wrapStatementVO);
            }
        }
        statementVOs.add(statementVO);
    }
    
    public void addReqComponentVO(ReqComponentVO reqComponentVO) {
        doAddReqComponentVO(reqComponentVO);
        validate();
    }
    
    private void doAddReqComponentVO(ReqComponentVO reqComponentVO) {
        // if there are sub statements in this statement already
        // add a new sub statement with the same operator as this statement
        // and a the requirement component
        if (statementVOs != null && !statementVOs.isEmpty()) {
            StatementInfo newStatementInfo = new StatementInfo();
            StatementVO newStatementVO = new StatementVO();
            newStatementInfo.setOperator(statementInfo.getOperator());
            newStatementVO.setStatementInfo(newStatementInfo);
            newStatementVO.getReqComponentVOs().add(reqComponentVO);
            statementVOs.add(newStatementVO);
        } else {
            reqComponentVOs.add(reqComponentVO);
        }
    }
    
    public void removeStatementVO(StatementVO statementVO) {
        statementVOs.remove(statementVO);
        validate();
    }   

    public void removeReqComponentVO(ReqComponentVO reqComponentVO) {
        doRemoveReqComponentVO(reqComponentVO);
        validate();
    }
    
    private void doRemoveReqComponentVO(ReqComponentVO reqComponentVO) {
        if (statementVOs != null && !statementVOs.isEmpty()) {
            List<StatementVO> tempStatementVOs = new ArrayList<StatementVO>(statementVOs);
            for (StatementVO subStatementVO : tempStatementVOs) {
                List<ReqComponentVO> subStatementReqComponentVOs = 
                    (subStatementVO == null)? null : subStatementVO.getReqComponentVOs();
                if (subStatementReqComponentVOs != null &&
                        subStatementReqComponentVOs.size() == 1 &&
                        subStatementReqComponentVOs.get(0) == reqComponentVO) {
                    subStatementVO.removeReqComponentVO(reqComponentVO);
                    // cleans up empty statements with neither statements nor requirement components.
                    statementVOs.remove(subStatementVO);
                }
            }
        } else { 
            reqComponentVOs.remove(reqComponentVO);
        }
    }
    
    public StatementInfo getStatementInfo() {
        return statementInfo;
    }

    public void setStatementInfo(StatementInfo statementInfo) {
        this.statementInfo = statementInfo;
    }    
    
    public List<ReqComponentVO> getReqComponentVOs() {
        return reqComponentVOs;
    }

    public List<StatementVO> getStatementVOs() {
        return statementVOs;
    }
    
    public void clearStatementAndReqComponents() {
        if (statementVOs != null) {
            statementVOs.clear();
        }
        if (reqComponentVOs != null) {
            reqComponentVOs.clear();
        }
    }
    
    public void shiftReqComponent(String shiftType, 
            final ReqComponentVO reqComponentVO) {
        if (statementVOs != null && !statementVOs.isEmpty()) {
            // the statementVO that wraps the reqComponentVO
            StatementVO reqComponentVOWrap = null;
            for (StatementVO currStatementVO : statementVOs) {
                List<ReqComponentVO> currReqComponentVOs =
                    (currStatementVO == null)? null : 
                        currStatementVO.getReqComponentVOs();
                if (currReqComponentVOs != null &&
                        currReqComponentVOs.size() == 1 &&
                        currReqComponentVOs.get(0) == reqComponentVO) {
                    reqComponentVOWrap = currStatementVO;
                }
            }
            if (reqComponentVOWrap != null) {
                swapElement(statementVOs, reqComponentVOWrap, shiftType);
            }
        } else if (reqComponentVOs != null && reqComponentVOs.size() > 1) {
            swapElement(reqComponentVOs, reqComponentVO, shiftType);
        }
    }
    
    private <T> void swapElement(List<T> elements, T element, String direction) {
        int elementIndex = 0;
        if (elements != null && elements.size() > 1) {
            for (T currElement :
                elements) {
                if (direction != null && direction.equals("RIGHT")) {
                    if (currElement == element &&
                            elementIndex + 1 < elements.size()) {
                        Collections.swap(elements, elementIndex,
                                elementIndex + 1);
                        break;
                    }
                } else if (direction != null && direction.equals("LEFT")) {
                    if (currElement == element &&
                            elementIndex > 0) {
                        Collections.swap(elements, elementIndex,
                                elementIndex - 1);
                        break;
                    }
                }
                elementIndex++;
            }
        }
    }
    
    /**
     * returns A, B, C, ... etc depending on the number of
     * Requirement components in the list.
     * @param rcs
     * @return
     */
    private String getNextGuiRCId(List<ReqComponentVO> rcs) {
        int charCode = 65; // ASCII code for capitalized A
        int newCharCode = -1;
        String guiRCId = null;
        
        while (newCharCode == -1) {
            boolean charUsed = false;
            if (rcs != null) {
                for (ReqComponentVO rc : rcs) {
                    String currGuiRCId = rc.getGuiReferenceLabelId();
                    currGuiRCId = (currGuiRCId == null)? "" : currGuiRCId;
                    if (currGuiRCId.equals(Character.toString((char)charCode))) {
                        charUsed = true;
                        charCode++;
                    }
                }
            }
            if (!charUsed) {
                newCharCode = charCode;
            }
        }
        
        // the next GUI id will be A - Z, and A1, A2, A3 afterwards.
        if (newCharCode < 65 + 26) {
            guiRCId = new String(Character.toString((char)newCharCode));
        } else {
            guiRCId = new String(Character.toString((char)(65 + 26)));
            guiRCId = guiRCId + Integer.toString(
                    newCharCode - 65 + 26 - 1);
        }
        return guiRCId;
    }
    
    private void assignGuiRCId() {
        doAssignGuiRCId(this, new ArrayList<ReqComponentVO>());
    }
    
    private void doAssignGuiRCId(StatementVO statementVO, List<ReqComponentVO> rcs) {
        List<StatementVO> statementVOs = statementVO.getStatementVOs();
        List<ReqComponentVO> reqComponentVOs = statementVO.getReqComponentVOs();
        
        if (statementVOs != null) {
//            node.setUserObject(statementVO);
            for (int i = 0; i < statementVOs.size(); i++) {
                StatementVO childStatementVO = statementVOs.get(i);
                doAssignGuiRCId(childStatementVO, rcs);
            }
        }

        if (reqComponentVOs != null) {
            for (int rcIndex = 0, rcCount = reqComponentVOs.size(); rcIndex < rcCount; rcIndex++) {
                ReqComponentVO childReqComponentVO = reqComponentVOs.get(rcIndex);
                if (childReqComponentVO.getGuiReferenceLabelId() == null ||
                        childReqComponentVO.getGuiReferenceLabelId().trim().length() == 0) {
                    String guiRCId = null;
                    guiRCId = getNextGuiRCId(rcs);
                    childReqComponentVO.setGuiReferenceLabelId(guiRCId);
                }
                rcs.add(childReqComponentVO);
            }
        }        
    }
    
    public Node getTree() {        
        Node node = new Node();
        assignGuiRCId();
        addChildrenNodes(node, this);
        //printTree(node);
        return node;
    }
    
    private void addChildrenNodes(Node node, StatementVO statementVO) {
        List<StatementVO> statementVOs = statementVO.getStatementVOs();
        List<ReqComponentVO> reqComponentVOs = statementVO.getReqComponentVOs();
        
        if (statementVOs != null) {
//            node.setUserObject(statementVO);
            setOperatorNode(node, statementVO);
            for (int i = 0; i < statementVOs.size(); i++) {
                StatementVO childStatementVO = statementVOs.get(i);
                Node childNode = new Node();
                node.addNode(childNode);
                addChildrenNodes(childNode, childStatementVO);
            }
        }

        if (reqComponentVOs != null) {
            //System.out.println("VO size: " + reqComponentVOs.size());
            for (int rcIndex = 0, rcCount = reqComponentVOs.size(); rcIndex < rcCount; rcIndex++) {
                ReqComponentVO childReqComponentVO = reqComponentVOs.get(rcIndex);
                if (rcCount > 1) {
                    //System.out.println("TESTING 00---> " + childReqComponentVO.getReqComponentInfo().getDesc() + " ### " + childReqComponentVO.getReqComponentInfo().getReqCompField().size());
                    node.addNode(new Node(childReqComponentVO));
                } else {
                    //System.out.println("TESTING 0---> " + childReqComponentVO.getReqComponentInfo().getReqCompField().size());
                    node.setUserObject(childReqComponentVO);
                }
            }
        }        
    }
    
    private void setOperatorNode(Node node, StatementVO statementVO) {
        if (statementVO.getStatementInfo() != null &&
                statementVO.getStatementInfo().getOperator() ==
                    StatementOperatorTypeKey.AND) {
            statementVO.type = Token.And;
            statementVO.value = "and";
            node.setUserObject(statementVO);
        } else if (statementVO.getStatementInfo() != null &&
                statementVO.getStatementInfo().getOperator() ==
                    StatementOperatorTypeKey.OR) {
            statementVO.type = Token.Or;
            statementVO.value = "or";
            node.setUserObject(statementVO);
        }
    }
    
    public List<StatementVO> getSelectedStatementVOs() {
        List<StatementVO> selectedStatementVOs = new ArrayList<StatementVO>();
        return doGetSelectedStatmentVOs(this, selectedStatementVOs);
    }
    
    private List<StatementVO> doGetSelectedStatmentVOs(StatementVO statementVO, List<StatementVO> selectedStatementVOs) {
        List<StatementVO> childrenStatementVOs = statementVO.getStatementVOs();
        if (statementVO.isCheckBoxOn()) {
            selectedStatementVOs.add(statementVO);
        }
        // check children
        if (childrenStatementVOs != null && !childrenStatementVOs.isEmpty()) {
            for (StatementVO childStatementVO : statementVO.getStatementVOs()) {
                doGetSelectedStatmentVOs(childStatementVO, selectedStatementVOs);
            }
        }
        return selectedStatementVOs;
    }
    
    /**
     * goes through the entire tree recursively and returns the list of all RCs
     * @return
     */
    public List<ReqComponentVO> getAllReqComponentVOs() {
        return doGetAllReqComponentVOs(this, new ArrayList<ReqComponentVO>());
    }
    
    public ReqComponentVO getReqComponentVOByGuiKey(String guiKey) {
        ReqComponentVO result = null;
        List<ReqComponentVO> allRCs = getAllReqComponentVOs();
        for (ReqComponentVO rc : allRCs) {
            if (rc.getGuiReferenceLabelId() != null && rc.getGuiReferenceLabelId().equals(guiKey)) {
                result = rc;
                break;
            }
        }
        return result;
    }
    
    private List<ReqComponentVO> doGetAllReqComponentVOs(StatementVO statementVO,
            List<ReqComponentVO> allRCs) {
        List<ReqComponentVO> childrenReqComponentVOs = statementVO.getReqComponentVOs();
        List<StatementVO> childrenStatementVOs = statementVO.getStatementVOs();
        if (childrenReqComponentVOs != null && !childrenReqComponentVOs.isEmpty()) {
            for (ReqComponentVO childReqComponentVO : childrenReqComponentVOs) {
                allRCs.add(childReqComponentVO);
            }
        }
        if (childrenStatementVOs != null && !childrenStatementVOs.isEmpty()) {
            for (StatementVO childStatementVO : statementVO.getStatementVOs()) {
                doGetAllReqComponentVOs(childStatementVO, allRCs);
            }
        }
        return allRCs;
    }
    
    public List<ReqComponentVO> getSelectedReqComponentVOs() {
        List<ReqComponentVO> selectedReqComponentVOs = new ArrayList<ReqComponentVO>();
        return doGetSelectedReqComponentVOs(this, selectedReqComponentVOs);
    }
    
    private List<ReqComponentVO> doGetSelectedReqComponentVOs(StatementVO statementVO,
            List<ReqComponentVO> selectedReqComponentVOs) {
        List<ReqComponentVO> childrenReqComponentVOs = statementVO.getReqComponentVOs();
        List<StatementVO> childrenStatementVOs = statementVO.getStatementVOs();
        if (childrenReqComponentVOs != null && !childrenReqComponentVOs.isEmpty()) {
            for (ReqComponentVO childReqComponentVO : childrenReqComponentVOs) {
                if (childReqComponentVO.isCheckBoxOn()) {
                    selectedReqComponentVOs.add(childReqComponentVO);
                }
            }
        }
        if (childrenStatementVOs != null && !childrenStatementVOs.isEmpty()) {
            for (StatementVO childStatementVO : statementVO.getStatementVOs()) {
                doGetSelectedReqComponentVOs(childStatementVO, selectedReqComponentVOs);
            }
        }
        return selectedReqComponentVOs;
    }
    
    /**
     * 
     * @param statementVO
     * @return
     */
    public int getNestingDepth() {
        return doGetNestingDepth(this);
    }
    
    private int doGetNestingDepth(StatementVO statementVO) {
        int depth = 0;
        List<StatementVO> statementVOs = getStatementVOs();
        if (this == statementVO) {
            return depth;
        }
        if (statementVOs != null && !statementVOs.isEmpty()) {
            for (StatementVO childStatementVO : statementVOs) {
                depth = depth + doGetNestingDepth(childStatementVO);
            }
        }
        return depth;
    }
    
    @Override
    public String toString() {
        StringBuilder sbResult = new StringBuilder();
        sbResult.append(value);
        return sbResult.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
    
    public int getReqComponentVOCount() {
        return (reqComponentVOs == null)? 0 : reqComponentVOs.size();
    }
    
    public int getStatementVOCount() {
        return (statementVOs == null)? 0 : statementVOs.size();
    }
    
    public int getChildCount() {
        return getReqComponentVOCount() + getStatementVOCount();
    }
    
    public boolean isWrapperStatementVO() {
        boolean result = false;
        if (getReqComponentVOCount() == 1 && getStatementVOCount() == 0) {
            result = true;
        }
        return result;
    }
    
    public void addStatementVOs(List<StatementVO> statementVOs) {
        if (statementVOs != null && !statementVOs.isEmpty()) {
            for (StatementVO s : statementVOs) {
                this.addStatementVO(s);
            }
        }
    }
    
    public void addReqComponentVOs(List<ReqComponentVO> reqComponentVOs) {
        if (reqComponentVOs != null && !reqComponentVOs.isEmpty()) {
            for (ReqComponentVO rc : reqComponentVOs) {
                this.addReqComponentVO(rc);
            }
        }
    }
    
    /**
     * simplifies statement
     * @return true if statement has been changed as a result of the call
     */
    public boolean simplify() {
        boolean structureChanged = false;
        structureChanged = structureChanged || doSimplify(this, null);
        structureChanged = structureChanged || cleanupStatementVO();
        structureChanged = structureChanged || unwrapRCs();
        return structureChanged;
    } 
    
    private boolean doSimplify(StatementVO statementVO, StatementVO parent) {
        boolean structureChanged = false;
        StatementOperatorTypeKey op =
            (statementVO == null || statementVO.getStatementInfo() == null)? null :
                statementVO.getStatementInfo().getOperator();
        StatementOperatorTypeKey
        parentOp = (parent == null || parent.getStatementInfo() == null)? null :
            parent.getStatementInfo().getOperator();
        if (parentOp == op && !statementVO.isWrapperStatementVO()) {
            structureChanged = true;
            if (statementVO.getReqComponentVOCount() > 0) {
                parent.removeStatementVO(statementVO);
                for (ReqComponentVO rc : statementVO.getReqComponentVOs()) {
                    parent.addReqComponentVO(rc);
                }
            } else if (statementVO.getStatementVOCount() > 0) {
                parent.removeStatementVO(statementVO);
                List<StatementVO> subSs = new ArrayList<StatementVO>(
                        statementVO.getStatementVOs());
                for (StatementVO subS : subSs) {
                    doSimplify(subS, statementVO);
                }
                parent.addStatementVOs(statementVO.getStatementVOs());
            }
        } else if (statementVO.getStatementVOCount() > 0) {
            List<StatementVO> subSs = new ArrayList<StatementVO>(statementVO.getStatementVOs());
            for (StatementVO subS : subSs) {
                structureChanged = structureChanged || doSimplify(subS, statementVO);
            }
        }
        return structureChanged;
    }
    
    /**
     * looks for statements where all sub statements are wrapped statements and
     * unwrap them
     */
    private boolean unwrapRCs() {
        boolean structureChanged = false;
        structureChanged = doUnwrapRCs(this, 0);
        return structureChanged;
    }
    
    private boolean doUnwrapRCs(StatementVO statementVO, int level) {
        boolean structureChanged = false;
        List<ReqComponentVO> wrappedRCs = new ArrayList<ReqComponentVO>();
        if (statementVO.getStatementVOCount() > 0) {
            List<StatementVO> subSs = new ArrayList<StatementVO>(statementVO.getStatementVOs());
            for (StatementVO subS : subSs) {
                if (!subS.isWrapperStatementVO()) {
                    structureChanged = structureChanged || doUnwrapRCs(subS, level + 1);
                }
            }
            
            for (StatementVO subS : subSs) {
                if (subS.isWrapperStatementVO()) {
                    wrappedRCs.add(subS.getReqComponentVOs().get(0));
                }
            }
            if (wrappedRCs != null && 
                    wrappedRCs.size() == statementVO.getChildCount()) {
                structureChanged = true;
                for (StatementVO subS : subSs) {
                    statementVO.removeStatementVO(subS);
                }
                for (ReqComponentVO wrappedRC : wrappedRCs) {
                    statementVO.addReqComponentVO(wrappedRC);
                }
            }
        }
        return structureChanged;
    }
    
    /**
     * goes through the tree recursively to delete statements with no child.
     */
    private boolean cleanupStatementVO() {
        boolean structureChanged = false;
        structureChanged = doCleanupStatementVO(this, null);
        return structureChanged;
    }
    
    private boolean doCleanupStatementVO(StatementVO statementVO, StatementVO parent) {
        boolean structureChanged = false;
        if (statementVO.getStatementVOCount() == 0 &&
                statementVO.getReqComponentVOCount() == 0) {
            if (parent != null) {
                parent.removeStatementVO(statementVO);
                structureChanged = true;
            }
        } else if (statementVO.getStatementVOCount() > 0) {
            for (StatementVO subS : statementVO.getStatementVOs()) {
                structureChanged = structureChanged || doCleanupStatementVO(subS, statementVO);
            }
        }
        return structureChanged;
    }

    public String getPrintableStatement() {
        StringBuilder sbResult = null;
        sbResult = doConvertToExpression(new StringBuilder(),
                this, true);
        return sbResult.toString();
    }
    
    public String convertToExpression() {
        StringBuilder sbResult = null;
        assignGuiRCId();
        sbResult = doConvertToExpression(new StringBuilder(),
                this, false);
        return sbResult.toString();
    }
    
    private StringBuilder doConvertToExpression(StringBuilder inSbResult, 
            StatementVO statementVO,
            boolean extraBrackets) {
        List<StatementVO> currStatementVOs = (statementVO == null)? null :
            statementVO.getStatementVOs();
        List<ReqComponentVO> currReqComponentVOs = (statementVO == null)? null :
            statementVO.getReqComponentVOs();
        if (currStatementVOs != null && !currStatementVOs.isEmpty()) {
            int statementCounter = 0;
            for (StatementVO childStatementVO : statementVO.getStatementVOs()) {
                if (statementCounter > 0) {
                    StatementOperatorTypeKey operator =
                        (statementVO == null ||
                                statementVO.getStatementInfo() == null)? null :
                                    statementVO.getStatementInfo().getOperator();
                    inSbResult.append(" " + operator + " ");
                }
                if (extraBrackets || !childStatementVO.isWrapperStatementVO()) {
                    inSbResult.append("(");
                }
                inSbResult.append(doConvertToExpression(new StringBuilder(), childStatementVO, 
                        extraBrackets).toString());
                if (extraBrackets || !childStatementVO.isWrapperStatementVO()) {
                    inSbResult.append(")");
                }
                statementCounter++;
            }
        } else if (currReqComponentVOs != null && !currReqComponentVOs.isEmpty()) {
            int rcCounter = 0;
            for (ReqComponentVO childReqComponentInfo : currReqComponentVOs) {
                if (rcCounter > 0) {
                    StatementOperatorTypeKey operator =
                        (statementVO == null ||
                                statementVO.getStatementInfo() == null)? null :
                                    statementVO.getStatementInfo().getOperator();
                    inSbResult.append(" " + operator.toString().toLowerCase() + " ");
                }
                inSbResult.append(childReqComponentInfo.getGuiReferenceLabelId());
                rcCounter++;
            }
        }
        return inSbResult;
    }
    
    public void clearSelections() {
        doClearSelections(this);
    }
    
    private void doClearSelections(StatementVO statementVO) {
        statementVO.setCheckBoxOn(false);
        if (statementVO.getStatementVOCount() > 0) {
            for (StatementVO childS : statementVOs) {
                doClearSelections(childS);
            }
        } else if (statementVO.getReqComponentVOCount() > 0) {
            for (ReqComponentVO rc : statementVO.getReqComponentVOs()) {
                rc.setCheckBoxOn(false);
            }
        }
    }
    
    public boolean isSimple() {
        boolean simple = false;
        if (getStatementVOCount() == 0 && getReqComponentVOCount() <= 1) {
            simple = true;
        }
        return simple;
    }

    public boolean isEmpty() {
        boolean simple = false;
        if (getStatementVOCount() == 0 && getReqComponentVOCount() == 0) {
            simple = true;
        }
        return simple;
    }
    
    private void setFieldsTo(final StatementTreeViewInfo stvInfo) {
        stvInfo.setAttributes(getStatementInfo().getAttributes());
        stvInfo.setDesc(getStatementInfo().getDesc());
        stvInfo.setId(getStatementInfo().getId());
        stvInfo.setMetaInfo(getStatementInfo().getMetaInfo());
        stvInfo.setName(getStatementInfo().getName());
        stvInfo.setOperator(getStatementInfo().getOperator());
        stvInfo.setState(getStatementInfo().getState());
        stvInfo.setType(getStatementInfo().getType());
    }
    
    public String composeStatementTreeViewInfo(StatementVO statementVO, StatementTreeViewInfo statementTreeViewInfo) throws Exception {
        List<StatementVO> statementVOs = statementVO.getStatementVOs();
        List<ReqComponentVO> reqComponentVOs = statementVO.getReqComponentVOs();
        
        statementVO.setFieldsTo(statementTreeViewInfo);
        if ((statementVOs != null) && (reqComponentVOs != null) && (statementVOs.size() > 0) && (reqComponentVOs.size() > 0))
        {
            return "Internal error: found both Statements and Requirement Components on the same level of boolean expression";
        }
        
        if ((statementVOs != null) && (statementVOs.size() > 0)) {
            // retrieve all statements
            List<StatementTreeViewInfo> subStatementTVInfos = new ArrayList<StatementTreeViewInfo>();
            for (StatementVO statement : statementVOs) {
                StatementTreeViewInfo subStatementTVInfo = new StatementTreeViewInfo();
                subStatementTVInfo.setParentId(statementVO.getStatementInfo().getId());
                statement.setFieldsTo(subStatementTVInfo);
                composeStatementTreeViewInfo(statement, subStatementTVInfo); // inside set the children of this statementTreeViewInfo
                subStatementTVInfos.add(subStatementTVInfo);
            }
            statementTreeViewInfo.setStatements(subStatementTVInfos);
        } else {
            // retrieve all req. component LEAFS
            List<ReqComponentInfo> reqComponentList = new ArrayList<ReqComponentInfo>();
            for (ReqComponentVO reqComponent : reqComponentVOs) {
                ReqComponentInfo newReqComp = ObjectClonerUtil.clone(reqComponent.getReqComponentInfo());
                reqComponentList.add(newReqComp);
            }
            statementTreeViewInfo.setReqComponents(reqComponentList);
        }
        
        return "";
    }
}
