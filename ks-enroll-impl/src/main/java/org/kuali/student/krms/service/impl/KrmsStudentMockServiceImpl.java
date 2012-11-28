package org.kuali.student.krms.service.impl;

import org.kuali.rice.krms.framework.engine.expression.ComparisonOperator;

/**
 * Created with IntelliJ IDEA.
 * User: SW
 * Date: 2012/08/02
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class KrmsStudentMockServiceImpl {

    public String getTermForType(String type){
        if( "20000".equals(type) || "20007".equals(type) || "20009".equals(type)){
            return "20000";
        } else if ( "20001".equals(type)){
            return "20001";
        } else if ( "20002".equals(type) || "20003".equals(type) || "20008".equals(type)){
            return "20002";
        }
        return "";
    }

    public String getOperationForType(String type){
        if( "20000".equals(type)){
            return ComparisonOperator.GREATER_THAN_EQUAL.getCode();
        }
        return ComparisonOperator.EQUALS.getCode();
    }

    public String getValueForType(String type){
        if( "20000".equals(type)){
            return "?";
        } else if ( "20001".equals(type)){
            return "true";
        } else if ( "20002".equals(type)){
            return "true";
        } else if ( "20003".equals(type)){
            return "false";
        }
        return "";
    }
}