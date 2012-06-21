---------------------------------------------------------------------------------------------------------
-- *These scripts was used TO load DB -  do normal load via impex
--  and then apply the following 2 *
---------------------------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------------------
-- Alter table KSLU_CLU_IDENT rename Column Div to DIVISION
---------------------------------------------------------------------------------------------------------
ALTER TABLE KSLU_CLU_IDENT RENAME COLUMN DIV TO DIVISION
/


ALTER TABLE KSEM_CTX_T ADD (CREATEID VARCHAR2(255) NULL,UPDATEID VARCHAR2(255) NULL, CREATETIME TIMESTAMP(6) NULL,  UPDATETIME TIMESTAMP(6) NULL)
/
ALTER TABLE KSEM_ENUM_T ADD (CREATEID VARCHAR2(255) NULL, UPDATEID VARCHAR2(255) NULL, CREATETIME TIMESTAMP(6) NULL, UPDATETIME TIMESTAMP(6) NULL)
/
ALTER TABLE KSEM_ENUM_VAL_T ADD (CREATEID VARCHAR2(255) NULL, UPDATEID VARCHAR2(255) NULL, CREATETIME TIMESTAMP(6) NULL, UPDATETIME TIMESTAMP(6) NULL)
/
ALTER TABLE KSEM_ENUM_T RENAME COLUMN DESCR TO DESCR_PLAIN
/
ALTER TABLE KSEM_ENUM_T MODIFY (DESCR_PLAIN VARCHAR2(4000))
/
ALTER TABLE KSEM_ENUM_T ADD (ENUM_TYPE VARCHAR2(255) NULL, ENUM_STATE VARCHAR2(255) NULL, DESCR_FORMATTED VARCHAR2(4000) NULL)
/

alter table KSEM_ENUM_VAL_T add x VARCHAR2(255)
/
update KSEM_ENUM_VAL_T set x = SORT_KEY; 
alter table KSEM_ENUM_VAL_T drop column SORT_KEY; 
alter table KSEM_ENUM_VAL_T rename column x to SORT_KEY;

/*ALTER TABLE KSEM_ENUM_VAL_T MODIFY (SORT_KEY VARCHAR2(255))
/
*/
CREATE TABLE KSEM_ENUM_ATTR
(
        ID VARCHAR2(255),
         OBJ_ID VARCHAR2(36),
         ATTR_KEY VARCHAR2(255),
         ATTR_VALUE VARCHAR2(4000),
         OWNER VARCHAR2(255),
        PRIMARY KEY (ID),
       CONSTRAINT KSEM_ENUM_ATTR_FK1
    FOREIGN KEY (OWNER )
    REFERENCES KSEM_ENUM_T (ENUM_KEY ) 
)
/
CREATE INDEX KSEM_ENUM_ATTR_IF1 on KSEM_ENUM_ATTR (OWNER ASC)
/

CREATE TABLE KSEN_ATP(
      ID	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      VER_NBR	NUMBER(19),
      CREATEID	VARCHAR2(255),
      CREATETIME	TIMESTAMP(6),
      UPDATEID	VARCHAR2(255),
      UPDATETIME	TIMESTAMP(6),
      END_DT	TIMESTAMP(6)  NOT NULL,
      NAME	VARCHAR2(255),
      START_DT	TIMESTAMP(6)  NOT NULL,
      ADMIN_ORG_ID	VARCHAR2(50),
      ATP_STATE	VARCHAR2(255) NOT NULL,
      ATP_TYPE	VARCHAR2(255) NOT NULL,
      DESCR_PLAIN	VARCHAR2(4000) NOT NULL,
      DESCR_FORMATTED	VARCHAR2(4000),
      ATP_CD	VARCHAR2(255),
      PRIMARY KEY (ID) 
)
/
CREATE INDEX KSEN_ATP_I1 on KSEN_ATP (ATP_TYPE)
/
CREATE INDEX KSEN_ATP_I2 on KSEN_ATP (ATP_CD)
/
CREATE INDEX KSEN_ATP_I3 on KSEN_ATP (START_DT)
/

CREATE TABLE KSEN_ATPATP_RELTN(
      ID	VARCHAR2(255),	
      OBJ_ID	VARCHAR2(36),
      VER_NBR	NUMBER(19),
      CREATEID	VARCHAR2(255)	,
      CREATETIME	TIMESTAMP(6),
      UPDATEID	VARCHAR2(255)	,
      UPDATETIME	TIMESTAMP(6),
      EFF_DT	TIMESTAMP(6),
      EXPIR_DT	TIMESTAMP(6),
      ATP_ID	VARCHAR2(255) NOT NULL,	
      ATP_STATE	VARCHAR2(255) NOT NULL,	
      ATP_TYPE	VARCHAR2(255) NOT NULL,	
      RELATED_ATP_ID	VARCHAR2(255) NOT NULL,	
      PRIMARY KEY (ID),
       CONSTRAINT KSEN_ATPATP_RELTN_FK2
    FOREIGN KEY (RELATED_ATP_ID )
    REFERENCES KSEN_ATP (ID ) ,
    CONSTRAINT KSEN_ATPATP_RELTN_FK1
    FOREIGN KEY (ATP_ID )
    REFERENCES KSEN_ATP (ID )
)
/

CREATE INDEX KSEN_ATPATP_RELTN_I1 on KSEN_ATPATP_RELTN (ATP_TYPE)
/
CREATE INDEX KSEN_ATPATP_RELTN_IF1 on KSEN_ATPATP_RELTN (ATP_ID)
/
CREATE INDEX KSEN_ATPATP_RELTN_IF2 on KSEN_ATPATP_RELTN (RELATED_ATP_ID)
/

CREATE TABLE KSEN_ATP_ATTR(
      ID	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      ATTR_KEY	VARCHAR2(255),
      ATTR_VALUE	VARCHAR2(4000),
      OWNER	VARCHAR2(255),
      PRIMARY KEY (ID),
      CONSTRAINT KSEN_ATP_ATTR_FK1
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_ATP (ID )
)
/    

CREATE INDEX KSEN_ATP_ATTR_IF1 on KSEN_ATP_ATTR (OWNER)
/   
CREATE TABLE KSEN_ATP_STATE(  
      ID	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      VER_NBR	NUMBER(19),
      CREATEID	VARCHAR2(255),
      CREATETIME	TIMESTAMP(6),
      UPDATEID	VARCHAR2(255),
      UPDATETIME	TIMESTAMP(6),
      DESCR	VARCHAR2(255),
      NAME	VARCHAR2(255),
      PRIMARY KEY (ID)  
)
/

CREATE TABLE KSEN_ATP_TYPE(
      TYPE_KEY	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      VER_NBR	NUMBER(19),
      TYPE_DESC	VARCHAR2(2000),
      EFF_DT	TIMESTAMP(6),
      EXPIR_DT	TIMESTAMP(6),
      NAME	VARCHAR2(255),
      REF_OBJECT_URI	VARCHAR2(255),
      PRIMARY KEY (TYPE_KEY)  
)
/ 
CREATE TABLE KSEN_ATPTYPE_ATTR(
      ID	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      ATTR_KEY	VARCHAR2(255),
      ATTR_VALUE	VARCHAR2(2000),
      OWNER	VARCHAR2(255),
      CONSTRAINT FK3DFA6EE13309051A
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_ATP_TYPE (TYPE_KEY )
)
/
CREATE TABLE KSEN_CHECK(  
       ID	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      VER_NBR	NUMBER(19),
      CREATEID	VARCHAR2(255),
      CREATETIME	TIMESTAMP(6),
      UPDATEID	VARCHAR2(255),
      UPDATETIME	TIMESTAMP(6),
      NAME	VARCHAR2(255),
      RT_DESCR_ID	VARCHAR2(255),
      STATE_ID	VARCHAR2(255) NOT NULL,
      TYPE_ID	VARCHAR2(255) NOT NULL,
      ISSUE_ID	VARCHAR2(255),
      MILESTONE_TYPE_ID	VARCHAR2(255),
      AGENDA_ID	VARCHAR2(255),
      PROCESS_ID	VARCHAR2(255),
      PRIMARY KEY (ID) 
)
/  
CREATE TABLE KSEN_CHECK_ATTR(  
      ID	VARCHAR2(255),
      ATTR_NAME	VARCHAR2(255),
      ATTR_VALUE	VARCHAR2(2000),
      OWNER	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      VER_NBR	NUMBER(19),
      PRIMARY KEY (ID)
)    
/

CREATE TABLE KSEN_CHECK_RICH_TEXT(  
      ID	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      VER_NBR	NUMBER(19),
      FORMATTED	VARCHAR2(2000),
      PLAIN	VARCHAR2(2000),
      PRIMARY KEY (ID)
)
/

CREATE TABLE KSEN_CHECK_TYPE(  
      TYPE_KEY	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      VER_NBR	NUMBER(19),
      TYPE_DESC	VARCHAR2(2000),
      EFF_DT	TIMESTAMP(6),
      EXPIR_DT	TIMESTAMP(6),
      NAME	VARCHAR2(255),
      REF_OBJECT_URI	VARCHAR2(255),
      PRIMARY KEY (TYPE_KEY)
)
/
  
CREATE TABLE KSEN_CHECK_TYPE_ATTR(  
      ID	VARCHAR2(255),
      ATTR_NAME	VARCHAR2(255),
      ATTR_VALUE	VARCHAR2(2000),
      OWNER	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      VER_NBR	NUMBER(19),
      PRIMARY KEY (ID)
)  
/
CREATE TABLE KSEN_COMM_STATE( 
      ID	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      VER_NBR	NUMBER(19),
      CREATEID	VARCHAR2(255),
      CREATETIME	TIMESTAMP(6),
      UPDATEID	VARCHAR2(255),
      UPDATETIME	TIMESTAMP(6),
      DESCR	VARCHAR2(255),
      EFF_DT	TIMESTAMP(6),
      EXPIR_DT	TIMESTAMP(6),
      NAME	VARCHAR2(255),
      PROCESS_KEY	VARCHAR2(255),
      PRIMARY KEY (ID)
)
/

CREATE TABLE KSEN_HOLD_RICH_TEXT(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    FORMATTED	VARCHAR2(2000),
    PLAIN	VARCHAR2(2000),
    PRIMARY KEY (ID)
)
/
CREATE TABLE KSEN_HOLD_TYPE(   
      TYPE_KEY	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      VER_NBR	NUMBER(19),
      TYPE_DESC	VARCHAR2(2000),
      EFF_DT	TIMESTAMP(6),
      EXPIR_DT	TIMESTAMP(6),
      NAME	VARCHAR2(255),
      REF_OBJECT_URI	VARCHAR2(255),
      PRIMARY KEY (TYPE_KEY)
)
/
CREATE TABLE KSEN_ISSUE(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    NAME	VARCHAR2(255),
    ORG_ID	VARCHAR2(255),
    RT_DESCR_ID	VARCHAR2(255),
    STATE_ID	VARCHAR2(255),
    TYPE_ID	VARCHAR2(255) NOT NULL,
    PRIMARY KEY (ID),
     CONSTRAINT FK67D35B0B98517E2C
        FOREIGN KEY (STATE_ID )
        REFERENCES KSEN_COMM_STATE (ID),
         CONSTRAINT FK67D35B0BCDDEC8D3
        FOREIGN KEY (RT_DESCR_ID )
        REFERENCES KSEN_HOLD_RICH_TEXT (ID),
          CONSTRAINT FK67D35B0B654F946E
        FOREIGN KEY (TYPE_ID )
        REFERENCES KSEN_HOLD_TYPE (TYPE_KEY)
)
/  
CREATE TABLE KSEN_HOLD( 
      ID	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      VER_NBR	NUMBER(19),
      CREATEID	VARCHAR2(255),
      CREATETIME	TIMESTAMP(6),
      UPDATEID	VARCHAR2(255),
      UPDATETIME	TIMESTAMP(6),
      EFF_DT	TIMESTAMP(6),
      IS_OVERRIDABLE	NUMBER(1),
      IS_WARNING	NUMBER(1),
      NAME	VARCHAR2(255),
      PERS_ID	VARCHAR2(255),
      RELEASED_DT	TIMESTAMP(6),
      RT_DESCR_ID	VARCHAR2(255),
      STATE_ID	VARCHAR2(255) NOT NULL,
      TYPE_ID	VARCHAR2(255) NOT NULL,
      ISSUE_ID	VARCHAR2(255) NOT NULL,
       PRIMARY KEY (ID),
        CONSTRAINT FKEA92B02D654F946E
    FOREIGN KEY (TYPE_ID )
    REFERENCES KSEN_HOLD_TYPE (TYPE_KEY ),
    
            CONSTRAINT FKEA92B02DCDDEC8D3
    FOREIGN KEY (RT_DESCR_ID )
    REFERENCES KSEN_HOLD_RICH_TEXT (ID ),
    
     CONSTRAINT FKEA92B02D98517E2C
    FOREIGN KEY (STATE_ID )
    REFERENCES KSEN_COMM_STATE (ID ),
    
     CONSTRAINT FKEA92B02DB16974B
    FOREIGN KEY (ISSUE_ID )
    REFERENCES KSEN_ISSUE (ID )
)
/
CREATE TABLE KSEN_HOLD_ATTR(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    ATTR_KEY	VARCHAR2(255),
    ATTR_VALUE	VARCHAR2(2000),
    OWNER	VARCHAR2(255),
    PRIMARY KEY (ID),
     CONSTRAINT FK87BEEEC3DE9EDF87
        FOREIGN KEY (OWNER )
        REFERENCES KSEN_HOLD (ID )
)
/
CREATE TABLE KSEN_HOLD_TYPE_ATTR(   
      ID	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      ATTR_KEY	VARCHAR2(255),
      ATTR_VALUE	VARCHAR2(2000),
      OWNER	VARCHAR2(255),
      PRIMARY KEY (ID),
       CONSTRAINT FK31328E449E7CA6E1
        FOREIGN KEY (OWNER )
        REFERENCES KSEN_HOLD_TYPE (TYPE_KEY )
)
/
CREATE TABLE KSEN_INSTR(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    STATE_ID	VARCHAR2(255) NOT NULL,
    TYPE_ID	VARCHAR2(255) NOT NULL,
    EFF_DT	TIMESTAMP(6),
    EXPIR_DT	TIMESTAMP(6),
    PROCESS_ID	VARCHAR2(255),
    CHECK_ID	VARCHAR2(255),
    MESSAGE	VARCHAR2(255),
    POSITION	NUMBER(19),
    IS_WARNING	NUMBER(1),
    CONTINUE_ON_FAIL	NUMBER(1),
    IS_EXEMPTABLE	NUMBER(1),
      PRIMARY KEY (ID)
)
/

CREATE TABLE KSEN_INSTR_ATPTYPE_RELTN(   
INSTR_ID	VARCHAR2(255)NOT NULL,
ATP_TYPE_ID	VARCHAR2(255) NOT NULL
)
/

CREATE TABLE KSEN_INSTR_ATTR(   
    ID	VARCHAR2(255),
    ATTR_NAME	VARCHAR2(255),
    ATTR_VALUE	VARCHAR2(2000),
    OWNER	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    PRIMARY KEY (ID)
)
/

CREATE TABLE KSEN_INSTR_MESSAGE(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    FORMATTED	VARCHAR2(2000),
    PLAIN	VARCHAR2(2000),
    PRIMARY KEY (ID)
)
/
CREATE TABLE KSEN_INSTR_POPLTN_RELTN(   
    INSTR_ID	VARCHAR2(255),
    POPLTN_ID	VARCHAR2(255)
)
/

CREATE TABLE KSEN_INSTR_TYPE(   
    TYPE_KEY	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    TYPE_DESC	VARCHAR2(2000),
    EFF_DT	TIMESTAMP(6),
    EXPIR_DT	TIMESTAMP(6),
    NAME	VARCHAR2(255),
    REF_OBJECT_URI	VARCHAR2(255),
    PRIMARY KEY (TYPE_KEY)
)
/  

CREATE TABLE KSEN_INSTR_TYPE_ATTR(   
    ID	VARCHAR2(255),
    ATTR_NAME	VARCHAR2(255),
    ATTR_VALUE	VARCHAR2(2000),
    OWNER	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    PRIMARY KEY (ID)
)
/  
  
CREATE TABLE KSEN_ISSRESTRCTN_RELTN(   
    ID	VARCHAR2(255),
    ISSUE_ID	VARCHAR2(255),
    RESTRICTION_ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    PRIMARY KEY (ID)
)
/  

 CREATE TABLE KSEN_ISSUE_ATTR(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    ATTR_KEY	VARCHAR2(255),
    ATTR_VALUE	VARCHAR2(2000),
    OWNER	VARCHAR2(255),
    PRIMARY KEY (ID),
     CONSTRAINT FKE52C5EA5E7C51BBD
        FOREIGN KEY (OWNER )
        REFERENCES KSEN_ISSUE (ID)    
)
/   

CREATE TABLE KSEN_LPR_TYPE(   
    TYPE_KEY	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    TYPE_DESC	VARCHAR2(2000),
    EFF_DT	TIMESTAMP(6),
    EXPIR_DT	TIMESTAMP(6),
    NAME	VARCHAR2(255),
    REF_OBJECT_URI	VARCHAR2(255),
     PRIMARY KEY (TYPE_KEY)
)
/
 CREATE TABLE KSEN_LPR(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    EFFECTIVEDATE	TIMESTAMP(6),
    EXPIRATIONDATE	TIMESTAMP(6),
    LUIID	VARCHAR2(255),
    PERSONID	VARCHAR2(255),
    COMMITMENTPERCENT	FLOAT,
    RELATION_STATE_ID	VARCHAR2(255),
    RELATION_TYPE_ID	VARCHAR2(255),
    PRIMARY KEY (ID),
     CONSTRAINT FK1BE1597B61E975F6
        FOREIGN KEY (RELATION_STATE_ID )
        REFERENCES KSEN_COMM_STATE (ID),
        CONSTRAINT FK1BE1597B1D2EFA44
        FOREIGN KEY (RELATION_TYPE_ID )
        REFERENCES KSEN_LPR_TYPE (TYPE_KEY)     
)
/ 
CREATE TABLE KSEN_LUI_TYPE(   
   TYPE_KEY	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  TYPE_DESC	VARCHAR2(2000),
  EFF_DT	TIMESTAMP(6),
  EXPIR_DT	TIMESTAMP(6),
  NAME	VARCHAR2(255),
  REF_OBJECT_URI	VARCHAR2(255),
    PRIMARY KEY (TYPE_KEY)
)
/
CREATE TABLE KSEN_LUI_RICH_TEXT(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  FORMATTED	VARCHAR2(2000),
  PLAIN	VARCHAR2(2000),
    PRIMARY KEY (ID)
)
/
CREATE TABLE KSEN_LUI_IDENT(   
     ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    CD	VARCHAR2(255),
    DIVISION	VARCHAR2(255),
    LNG_NAME	VARCHAR2(255),
    SHRT_NAME	VARCHAR2(255),
    ST	VARCHAR2(255),
    SUFX_CD	VARCHAR2(255),
    TYPE	VARCHAR2(255),
    VARTN	VARCHAR2(255),
  PRIMARY KEY (ID)
)
/
CREATE TABLE KSEN_LUI(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  CREATEID	VARCHAR2(255),
  CREATETIME	TIMESTAMP(6),
  UPDATEID	VARCHAR2(255),
  UPDATETIME	TIMESTAMP(6),
  ATP_ID	VARCHAR2(255),
  CLU_ID	VARCHAR2(255),
  EFF_DT	TIMESTAMP(6),
  EXP_DT	TIMESTAMP(6),
  MAX_SEATS	NUMBER(10),
  MIN_SEATS	NUMBER(10),
  NAME	VARCHAR2(255),
  REF_URL	VARCHAR2(255),
  RT_DESCR_ID	VARCHAR2(255),
  STATE_ID	VARCHAR2(255)NOT NULL,
  TYPE_ID	VARCHAR2(255) NOT NULL,
  OFFIC_LUI_ID	VARCHAR2(255),
  PRIMARY KEY (ID),
  CONSTRAINT FKFD33EB2B7C4E988
    FOREIGN KEY (TYPE_ID )
    REFERENCES KSEN_LUI_TYPE (TYPE_KEY),
    
    CONSTRAINT FKFD33EB298517E2C
    FOREIGN KEY (STATE_ID )
    REFERENCES KSEN_COMM_STATE (ID),
    
    CONSTRAINT FKFD33EB28D1000ED
    FOREIGN KEY (RT_DESCR_ID )
    REFERENCES KSEN_LUI_RICH_TEXT (ID),
    
     CONSTRAINT FKFD33EB2656AF27
    FOREIGN KEY (OFFIC_LUI_ID )
    REFERENCES KSEN_LUI_IDENT (ID)
)
/

CREATE TABLE KSEN_RICH_TEXT_T(   
   ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  FORMATTED	VARCHAR2(2000),
  PLAIN	VARCHAR2(2000),
  PRIMARY KEY (ID)
)
/
CREATE TABLE KSEN_LPR_ROSTER(   
    ID	VARCHAR2(255),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(22),
    NAME	VARCHAR2(255),
    RT_DESCR_ID	VARCHAR2(255),
    MAX_CAPACITY	NUMBER(22),
    CHECK_IN_REQ	NUMBER(22),
    STATE_ID	VARCHAR2(255),
    TYPE_ID	VARCHAR2(255),
    ATP_DUR_TYP_KEY	VARCHAR2(255),
    TM_QUANTITY	NUMBER(22),
   PRIMARY KEY (ID),
       
 CONSTRAINT KSEN_LPR_ROSTER_FK1
    FOREIGN KEY (RT_DESCR_ID )
    REFERENCES KSEN_RICH_TEXT_T (ID)
)
/
CREATE TABLE KSEN_LPRROSTER_LUI_RELTN(   
        LPRROSTER_ID	VARCHAR2(255),
        LUI_ID	VARCHAR2(255),
     CONSTRAINT KSEN_LPRROSTER_LUI_RELTN_FK2
        FOREIGN KEY (LUI_ID )
        REFERENCES KSEN_LUI (ID),
        
        CONSTRAINT KSEN_LPRROSTER_LUI_RELTN_FK1
        FOREIGN KEY (LPRROSTER_ID )
        REFERENCES KSEN_LPR_ROSTER (ID)     
)
/ 

CREATE TABLE KSEN_LPR_ATTR(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    ATTR_KEY	VARCHAR2(255),
    ATTR_VALUE	VARCHAR2(2000),
    OWNER	VARCHAR2(255),
       PRIMARY KEY (ID),
       
     CONSTRAINT FKDF4BE635A8427FA
        FOREIGN KEY (OWNER )
        REFERENCES KSEN_LPR_TYPE (TYPE_KEY),
        
        CONSTRAINT FKDF4BE635DC3CD520
        FOREIGN KEY (OWNER )
        REFERENCES KSEN_LPR (ID),
        CONSTRAINT FKDF4BE635EA869E3D
        FOREIGN KEY (OWNER )
        REFERENCES KSEN_COMM_STATE (ID)  
)
/      

CREATE TABLE KSEN_LPR_ROSTER_ENTRY(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    EFFECTIVEDATE	TIMESTAMP(6),
    EXPIRATIONDATE	TIMESTAMP(6),
    LPRROSTER_ID	VARCHAR2(255),
    LPR_ID	VARCHAR2(255),
    POSITION	VARCHAR2(10),
    RELATION_STATE_ID	VARCHAR2(255),
    RELATION_TYPE_ID	VARCHAR2(255),
   PRIMARY KEY (ID),
       
 CONSTRAINT KSEN_LPR_ROSTER_ENTRY_FK2
    FOREIGN KEY (LPR_ID )
    REFERENCES KSEN_LPR (ID),
    
    CONSTRAINT KSEN_LPR_ROSTER_ENTRY_FK1
    FOREIGN KEY (LPRROSTER_ID )
    REFERENCES KSEN_LPR_ROSTER (ID)
)
/

CREATE TABLE KSEN_LPR_RV_GRP_RELTN(   
    LPR_ID	VARCHAR2(255),
    RV_GRP_ID	VARCHAR2(255)
)
/

CREATE TABLE KSEN_LPR_TRANS(   
    ID	VARCHAR2(255),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    NAME	VARCHAR2(255),
    RT_DESCR_ID	VARCHAR2(255),
    REQ_PERSON_ID	VARCHAR2(255),
    STATE_ID	VARCHAR2(255),
    LPR_TYPE_ID	VARCHAR2(255)
)
/

CREATE TABLE KSEN_LPR_TRANS_ATTR(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  ATTR_KEY	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(2000),
  OWNER	VARCHAR2(255)
)
/

CREATE TABLE KSEN_LPR_TRANS_ITEMS(   
    ID	VARCHAR2(255),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    NAME	VARCHAR2(255),
    RT_DESCR_ID	VARCHAR2(255),
    PERSON_ID	VARCHAR2(255),
    NEW_LUI_ID	VARCHAR2(255),
    EXIST_LUI_ID	VARCHAR2(255),
    RESULTING_LPR_ID	VARCHAR2(255),
    GROUP_ID	VARCHAR2(255),
    STATUS	VARCHAR2(255),
    LPR_TRANS_ID	VARCHAR2(255),
    STATE_ID	VARCHAR2(255),
    TYPE_ID	VARCHAR2(255)
)
/
CREATE TABLE KSEN_LPR_TRANS_ITEM_ATTR(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    ATTR_KEY	VARCHAR2(255),
    ATTR_VALUE	VARCHAR2(2000),
    OWNER	VARCHAR2(255)
)
/

CREATE TABLE KSEN_LRC_RES_SCALE(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    EFFECTIVEDATE	TIMESTAMP(6),
    EXPIRATIONDATE	TIMESTAMP(6),
    NAME	VARCHAR2(255),
    RT_DESCR_ID	VARCHAR2(255),
    STATE_ID	VARCHAR2(255),
    TYPE_ID	VARCHAR2(255),
    MIN_VALUE	VARCHAR2(255),
    MAX_VALUE	VARCHAR2(255),
    INCR	VARCHAR2(255),
     PRIMARY KEY (ID)
)
/
CREATE TABLE KSEN_LRC_RES_SCALE_ATTR(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    ATTR_KEY	VARCHAR2(255),
    ATTR_VALUE	VARCHAR2(2000),
    OWNER	VARCHAR2(255),
     PRIMARY KEY (ID)
)
/

CREATE TABLE KSEN_LRC_RES_VALUE(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    EFFECTIVEDATE	TIMESTAMP(6),
    EXPIRATIONDATE	TIMESTAMP(6),
    STATE_ID	VARCHAR2(255),
    TYPE_ID	VARCHAR2(255),
    NAME	VARCHAR2(255),
    RT_DESCR_ID	VARCHAR2(255),
    RES_SCALE_ID	VARCHAR2(255),
    NUMERIC_VALUE	VARCHAR2(255),
    VALUE	VARCHAR2(255),
     PRIMARY KEY (ID)
)
/
CREATE TABLE KSEN_LRC_RES_VALUE_ATTR(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  ATTR_KEY	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(2000),
  OWNER	VARCHAR2(255),
  PRIMARY KEY (ID)
)
/

CREATE TABLE KSEN_LRC_RES_VAL_GRP(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  CREATEID	VARCHAR2(255),
  CREATETIME	TIMESTAMP(6),
  UPDATEID	VARCHAR2(255),
  UPDATETIME	TIMESTAMP(6),
  EFFECTIVEDATE	TIMESTAMP(6),
  EXPIRATIONDATE	TIMESTAMP(6),
  NAME	VARCHAR2(255),
  RT_DESCR_ID	VARCHAR2(255),
  STATE_ID	VARCHAR2(255),
  TYPE_ID	VARCHAR2(255),
  MIN_VALUE	VARCHAR2(255),
  MAX_VALUE	VARCHAR2(255),
  INCR	VARCHAR2(255),
  RES_SCALE_ID	VARCHAR2(255),
  PRIMARY KEY (ID)
)
/

CREATE TABLE KSEN_LRC_RES_VAL_GRP_ATTR(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  ATTR_KEY	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(2000),
  OWNER	VARCHAR2(255),
  PRIMARY KEY (ID)
)
/
CREATE TABLE KSEN_LRC_RVGP_RV_RELTN(   
    RES_VAL_GRP_ID	VARCHAR2(255),
    RES_VAL_ID	VARCHAR2(255)

)
/

CREATE TABLE KSEN_LRC_TYPE(   
   TYPE_KEY	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  TYPE_DESC	VARCHAR2(2000),
  EFF_DT	TIMESTAMP(6),
  EXPIR_DT	TIMESTAMP(6),
  NAME	VARCHAR2(255),
  REF_OBJECT_URI	VARCHAR2(255),
  PRIMARY KEY (TYPE_KEY)
)
/
CREATE TABLE KSEN_LRR_TYPE(   
  TYPE_KEY	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  TYPE_DESC	VARCHAR2(2000),
  EFF_DT	TIMESTAMP(6),
  EXPIR_DT	TIMESTAMP(6),
  NAME	VARCHAR2(255),
  REF_OBJECT_URI	VARCHAR2(255),
  PRIMARY KEY (TYPE_KEY)
)
/
CREATE TABLE KSEN_LRR(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  CREATEID	VARCHAR2(255),
  CREATETIME	TIMESTAMP(6),
  UPDATEID	VARCHAR2(255),
  UPDATETIME	TIMESTAMP(6),
  NAME	VARCHAR2(255),
  RT_DESCR_ID	VARCHAR2(255),
  STATE_ID	VARCHAR2(255),
  TYPE_ID	VARCHAR2(255),
  LPR_ID	VARCHAR2(255),
  RESULT_VALUE_ID	VARCHAR2(255),
  PRIMARY KEY (ID),
  CONSTRAINT FK1BE15Q7B1D2EA121
    FOREIGN KEY (RT_DESCR_ID )
    REFERENCES KSEN_RICH_TEXT_T (ID),
    
    CONSTRAINT FK1BE15Q7B1D2EF131
    FOREIGN KEY (TYPE_ID )
    REFERENCES KSEN_LRR_TYPE (TYPE_KEY),
    
    CONSTRAINT FK1BE2597A61E975Q1
    FOREIGN KEY (STATE_ID )
    REFERENCES KSEN_COMM_STATE (ID)
)
/ 
 
CREATE TABLE KSEN_LRR_ATTR(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  ATTR_KEY	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(2000),
  OWNER	VARCHAR2(255),
  PRIMARY KEY (ID),
  CONSTRAINT FKSF4BE635DC3CD510
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_LRR (ID)
)
/

CREATE TABLE KSEN_LRR_RES_SOURCE(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  CREATEID	VARCHAR2(255),
  CREATETIME	TIMESTAMP(6),
  UPDATEID	VARCHAR2(255),
  UPDATETIME	TIMESTAMP(6),
  NAME	VARCHAR2(255),
  RT_DESCR_ID	VARCHAR2(255),
  TYPE_ID	VARCHAR2(255),
  ARTICULATE_ID	VARCHAR2(255),
  RES_TRANS_ID	VARCHAR2(255),
    PRIMARY KEY (ID)
)
/
CREATE TABLE KSEN_LRR_RES_SOURCE_ATTR(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  ATTR_KEY	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(2000),
  OWNER	VARCHAR2(255)

)
/

CREATE TABLE KSEN_LRR_RES_SRC_RELTN(   
 LRR_ID	VARCHAR2(255),
 RES_SRC_ID	VARCHAR2(255)
)
/

CREATE TABLE KSEN_LUILUI_RELTN(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  CREATEID	VARCHAR2(255),
  CREATETIME	TIMESTAMP(6),
  UPDATEID	VARCHAR2(255),
  UPDATETIME	TIMESTAMP(6),
  EFF_DT	TIMESTAMP(6),
  EXP_DT	TIMESTAMP(6),
  NAME	VARCHAR2(255),
  RT_DESCR_ID	VARCHAR2(255),
  LUI_ID	VARCHAR2(255),
  TYPE_ID	VARCHAR2(255)NOT NULL,
  STATE_ID	VARCHAR2(255) NOT NULL,
  RELATED_LUI_ID	VARCHAR2(255),
  PRIMARY KEY (ID),
  CONSTRAINT FKF07F592255882CA8
    FOREIGN KEY (LUI_ID )
    REFERENCES KSEN_LUI (ID),
    
    CONSTRAINT FKF07F59228D1000ED
    FOREIGN KEY (RT_DESCR_ID )
    REFERENCES KSEN_LUI_RICH_TEXT (ID),
    
    CONSTRAINT FKF07F5922B7C4E988
    FOREIGN KEY (TYPE_ID )
    REFERENCES KSEN_LUI_TYPE (TYPE_KEY),
    
     CONSTRAINT FKF07F59221343973C
    FOREIGN KEY (RELATED_LUI_ID )
    REFERENCES KSEN_LUI (ID),
       CONSTRAINT FKF07F592298517E2C
    FOREIGN KEY (STATE_ID )
    REFERENCES KSEN_COMM_STATE (ID)
)
/
CREATE TABLE KSEN_LUI_INSTR(   
    ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    ORG_ID	VARCHAR2(255),
    PERCT_EFFT	FLOAT,
    PERS_ID	VARCHAR2(255),
    PERS_OVRID	VARCHAR2(255),
    PRIMARY KEY (ID)
)
/

CREATE TABLE KSEN_LUILUI_RELTN_ATTR(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  ATTR_KEY	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(2000),
  OWNER	VARCHAR2(255),
  PRIMARY KEY (ID),
  CONSTRAINT FKA58E96EE6831FFE7
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_LUILUI_RELTN (ID)
)
/
CREATE TABLE KSEN_LUI_ATTR(   
   ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  ATTR_KEY	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(2000),
  OWNER	VARCHAR2(255),
  PRIMARY KEY (ID),
  CONSTRAINT FKDD9BAB5EA1B912DE
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_LUI_INSTR (ID),
    
     CONSTRAINT FKDD9BAB5EF0F1FBFB
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_LUI_TYPE (TYPE_KEY),
    
    CONSTRAINT FKDD9BAB5E9CEED1A1
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_LUI(ID),
      CONSTRAINT FKDD9BAB5E6831FFE7
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_LUILUI_RELTN(ID)
)
/

CREATE TABLE KSEN_LUI_IDENT_ATTR(   
     ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    ATTR_KEY	VARCHAR2(255),
    ATTR_VALUE	VARCHAR2(2000),
    OWNER	VARCHAR2(255),
  PRIMARY KEY (ID),
   CONSTRAINT FK3C63D6ADB1BE75EA
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_LUI_IDENT (ID)
)
/
CREATE TABLE KSEN_LUI_JN_LUI_IDENT(   
    LUI_ID	VARCHAR2(255) NOT NULL,
    ALT_LUI_ID	VARCHAR2(255) NOT NULL,
     CONSTRAINT FK40FD27E33A82D447
    FOREIGN KEY (ALT_LUI_ID )
    REFERENCES KSEN_LUI_IDENT (ID),
    
    CONSTRAINT FK40FD27E355882CA8
    FOREIGN KEY (LUI_ID )
    REFERENCES KSEN_LUI (ID)
)
/

CREATE TABLE KSEN_LUI_JN_LUI_INSTR(   
  LUI_ID	VARCHAR2(255) NOT NULL,
  LUI_INSTR_ID	VARCHAR2(255) NOT NULL

)
/

CREATE TABLE KSEN_LUI_LUCD(   
   ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    TYPE	VARCHAR2(255),
    VALUE	VARCHAR2(255),
    RT_DESCR_ID	VARCHAR2(255),
    LUI_ID	VARCHAR2(255),
    PRIMARY KEY (ID),
     CONSTRAINT FKDDA0AD178D1000ED
    FOREIGN KEY (RT_DESCR_ID )
    REFERENCES KSEN_LUI_RICH_TEXT (ID),
    
    CONSTRAINT FKDDA0AD1755882CA8
    FOREIGN KEY (LUI_ID )
    REFERENCES KSEN_LUI (ID)
)
/

CREATE TABLE KSEN_LUI_LUCD_ATTR(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  ATTR_KEY	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(2000),
  OWNER	VARCHAR2(255),
    PRIMARY KEY (ID),
     CONSTRAINT FK95C7419C1170681
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_LUI_LUCD (ID)
)
/

CREATE TABLE KSEN_LUI_MTG_SCHE(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  CREATEID	VARCHAR2(255),
  CREATETIME	TIMESTAMP(6),
  UPDATEID	VARCHAR2(255),
  UPDATETIME	TIMESTAMP(6),
  SPACE_ID	VARCHAR2(255),
  TM_PRD	VARCHAR2(255),
  LUI_ID	VARCHAR2(255),
    PRIMARY KEY (ID),
     CONSTRAINT KSEN_LUI_MTG_SCHE_FK_LUI
    FOREIGN KEY (LUI_ID )
    REFERENCES KSEN_LUI (ID)
)
/
  

CREATE TABLE KSEN_LUI_RV_GRP_RELTN(   
  ID	VARCHAR2(255),
  LUI_ID	VARCHAR2(255),
  RV_GRP_ID	VARCHAR2(255)
)
/

CREATE TABLE KSEN_LUI_TYPE_ATTR(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  ATTR_KEY	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(2000),
  OWNER	VARCHAR2(255),
    PRIMARY KEY (ID),
      CONSTRAINT FKFC6A7A89F0F1FBFB
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_LUI_TYPE (TYPE_KEY)
)
/
CREATE TABLE KSEN_POPULATION(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  CREATEID	VARCHAR2(255),
  CREATETIME	TIMESTAMP(6),
  UPDATEID	VARCHAR2(255),
  UPDATETIME	TIMESTAMP(6),
  STATE_ID	VARCHAR2(255),
  TYPE_ID	VARCHAR2(255)
)
/

CREATE TABLE KSEN_PROCESS(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  CREATEID	VARCHAR2(255),
  CREATETIME	TIMESTAMP(6),
  UPDATEID	VARCHAR2(255),
  UPDATETIME	TIMESTAMP(6),
  NAME	VARCHAR2(255),
  RT_DESCR_ID	VARCHAR2(255),
  STATE_ID	VARCHAR2(255) NOT NULL,
  TYPE_ID	VARCHAR2(255) NOT NULL,
  OWNER_ORG_ID	VARCHAR2(255),
  PRIMARY KEY (ID)
)
/

CREATE TABLE KSEN_PROCESS_ATTR(   
  ID	VARCHAR2(255),
  ATTR_NAME	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(2000),
  OWNER	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  PRIMARY KEY (ID)
)
/
CREATE TABLE KSEN_PROCESS_RICH_TEXT(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  FORMATTED	VARCHAR2(2000),
  PLAIN	VARCHAR2(2000),
  PRIMARY KEY (ID)
)
/

CREATE TABLE KSEN_PROCESS_TYPE(   
  TYPE_KEY	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  TYPE_DESC	VARCHAR2(2000),
  EFF_DT	TIMESTAMP(6),
  EXPIR_DT	TIMESTAMP(6),
  NAME	VARCHAR2(255),
  REF_OBJECT_URI	VARCHAR2(255),
  PRIMARY KEY (TYPE_KEY)
)
/

CREATE TABLE KSEN_PROCESS_TYPE_ATTR(   
  ID	VARCHAR2(255),
  ATTR_NAME	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(2000),
  OWNER	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  PRIMARY KEY (ID)
)
/
CREATE TABLE KSEN_STATE_PROCESS(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  CREATEID	VARCHAR2(255),
  CREATETIME	TIMESTAMP(6),
  UPDATEID	VARCHAR2(255),
  UPDATETIME	TIMESTAMP(6),
  DESCR	VARCHAR2(255),
  EFF_DT	TIMESTAMP(6),
  EXPIR_DT	TIMESTAMP(6),
  NAME	VARCHAR2(255),
  PRIMARY KEY (ID)
)
/
CREATE TABLE KSEN_MSTONE(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  CREATEID	VARCHAR2(255),
  CREATETIME	TIMESTAMP(6),
  UPDATEID	VARCHAR2(255),
  UPDATETIME	TIMESTAMP(6),
  END_DT	TIMESTAMP(6),
  IS_ALL_DAY	NUMBER(1) NOT NULL,
  IS_DATE_RANGE	NUMBER(1) NOT NULL,
  IS_RELATIVE	NUMBER(1) NOT NULL,
  RELATIVE_ANCHOR_MSTONE_ID	VARCHAR2(255),
  NAME	VARCHAR2(255),
  START_DT	TIMESTAMP(6),
  MSTONE_STATE	VARCHAR2(255) NOT NULL,
  MSTONE_TYPE	VARCHAR2(255) NOT NULL,
  DESCR_PLAIN	VARCHAR2(4000) NOT NULL,
  DESCR_FORMATTED	VARCHAR2(4000),
  IS_INSTRCT_DAY	NUMBER(1) DEFAULT 0 NOT NULL,
    PRIMARY KEY (ID)
)
/
CREATE INDEX KSEN_MSTONE_I1 on KSEN_MSTONE (MSTONE_TYPE)
/
CREATE INDEX KSEN_MSTONE_I2 on KSEN_MSTONE (START_DT)
/
  
CREATE TABLE KSEN_ATPMSTONE_RELTN(
     ID	VARCHAR2(255),
    OBJ_ID	VARCHAR2(36),
    VER_NBR	NUMBER(19),
    CREATEID	VARCHAR2(255),
    CREATETIME	TIMESTAMP(6),
    UPDATEID	VARCHAR2(255),
    UPDATETIME	TIMESTAMP(6),
    ATP_ID	VARCHAR2(255),
    MSTONE_ID	VARCHAR2(255),
    PRIMARY KEY (ID),
     CONSTRAINT KSEN_ATPMSTONE_RELTN_FK2
    FOREIGN KEY (MSTONE_ID )
    REFERENCES KSEN_MSTONE (ID ) ,
    
    CONSTRAINT KSEN_ATPMSTONE_RELTN_FK1
    FOREIGN KEY (ATP_ID )
    REFERENCES KSEN_ATP (ID )
 ) 
/

CREATE INDEX KSEN_ATPMSTONE_RELTN_IF1 on KSEN_ATPMSTONE_RELTN (ATP_ID)
/ 
CREATE INDEX KSEN_ATPMSTONE_RELTN_IF2 on KSEN_ATPMSTONE_RELTN (MSTONE_ID)
/ 
CREATE TABLE KSEN_ATPMSTONE_RELTN_ATTR(
      ID	VARCHAR2(255),
      OBJ_ID	VARCHAR2(36),
      ATTR_KEY	VARCHAR2(255),
      ATTR_VALUE	VARCHAR2(2000),
      OWNER	VARCHAR2(255),
      PRIMARY KEY (ID),
      CONSTRAINT FK586D7715FC300ED8
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_ATPMSTONE_RELTN (ID )
)
/

CREATE TABLE KSEN_MSTONE_ATTR(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  ATTR_KEY	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(4000),
  OWNER	VARCHAR2(255),
    PRIMARY KEY (ID),
      CONSTRAINT KSEN_MSTONE_ATTR_FK1
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_MSTONE (ID)
)
/
CREATE INDEX KSEN_MSTONE_ATTR_IF1 on KSEN_MSTONE_ATTR (OWNER)
/

CREATE TABLE KSEN_RESTRICTION(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  CREATEID	VARCHAR2(255),
  CREATETIME	TIMESTAMP(6),
  UPDATEID	VARCHAR2(255),
  UPDATETIME	TIMESTAMP(6),
  NAME	VARCHAR2(255),
  RT_DESCR_ID	VARCHAR2(255),
  STATE_ID	VARCHAR2(255) NOT NULL,
  TYPE_ID	VARCHAR2(255) NOT NULL,
  PRIMARY KEY (ID),
   CONSTRAINT FKC2C6CC7E654F946E
    FOREIGN KEY (TYPE_ID )
    REFERENCES KSEN_HOLD_TYPE (TYPE_KEY),
    
     CONSTRAINT FKC2C6CC7E98517E2C
    FOREIGN KEY (STATE_ID )
    REFERENCES KSEN_COMM_STATE (ID),
    
     CONSTRAINT FKC2C6CC7ECDDEC8D3
    FOREIGN KEY (RT_DESCR_ID )
    REFERENCES KSEN_HOLD_RICH_TEXT (ID)
)
/
CREATE TABLE KSEN_RESTRICTION_ATTR(   
   ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  ATTR_KEY	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(2000),
  OWNER	VARCHAR2(255),
  PRIMARY KEY (ID),
   CONSTRAINT FK1F4DD12D828D9F0
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_RESTRICTION (ID)
)
/

CREATE TABLE KSEN_TYPETYPE_RELTN(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  CREATEID	VARCHAR2(255),
  CREATETIME	TIMESTAMP(6),
  UPDATEID	VARCHAR2(255),
  UPDATETIME	TIMESTAMP(6),
  EFF_DT	TIMESTAMP(6),
  EXPIR_DT	TIMESTAMP(6),
  NAME	VARCHAR2(255),
  OWNER_TYPE_ID	VARCHAR2(255),
  RANK	NUMBER(10),
  RELATED_TYPE_ID	VARCHAR2(255),
  TYPETYPE_RELATION_TYPE	VARCHAR2(255),
  RT_DESCR_ID	VARCHAR2(255),
  PRIMARY KEY (ID),
   CONSTRAINT FK71693C166C2F628C
    FOREIGN KEY (RT_DESCR_ID )
    REFERENCES KSEN_RICH_TEXT_T (ID)
)
/
CREATE TABLE KSEN_ATPATP_RELTN_ATTR(
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  ATTR_KEY	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(4000),
  OWNER	VARCHAR2(255),
  PRIMARY KEY (ID),
   CONSTRAINT KSEN_ATPATP_RELTN_ATTR_FK2
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_TYPETYPE_RELTN (ID ) ,
    
    CONSTRAINT KSEN_ATPATP_RELTN_ATTR_FK1
    FOREIGN KEY (OWNER)
    REFERENCES KSEN_ATPATP_RELTN (ID )
  )
/
CREATE INDEX KSEN_ATPATP_RELTN_ATTR_IF1 on KSEN_ATPATP_RELTN_ATTR (OWNER)
/

CREATE TABLE KSEN_STATEPROCESS_RELTN(   
   ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  VER_NBR	NUMBER(19),
  CREATEID	VARCHAR2(255),
  CREATETIME	TIMESTAMP(6),
  UPDATEID	VARCHAR2(255),
  UPDATETIME	TIMESTAMP(6),
  NEXT_STATEKEY	VARCHAR2(255),
  PRIOR_STATEKEY	VARCHAR2(255),
  PROCESS_KEY	VARCHAR2(255),
  PRIMARY KEY (ID),
   CONSTRAINT FKCDFDC0E09F94F29D
    FOREIGN KEY (NEXT_STATEKEY )
    REFERENCES KSEN_COMM_STATE (ID),
    
    CONSTRAINT FKCDFDC0E0D6162101
    FOREIGN KEY (PROCESS_KEY )
    REFERENCES KSEN_STATE_PROCESS (ID),
    CONSTRAINT FKCDFDC0E024D2ACA6
    FOREIGN KEY (PRIOR_STATEKEY )
    REFERENCES KSEN_COMM_STATE (ID)
    
)
/
CREATE TABLE KSEN_STATE_ATTR(   
  ID	VARCHAR2(255),
  OBJ_ID	VARCHAR2(36),
  ATTR_KEY	VARCHAR2(255),
  ATTR_VALUE	VARCHAR2(2000),
  OWNER	VARCHAR2(255),
  PRIMARY KEY (ID),
   CONSTRAINT FK8193D5ED50135956
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_COMM_STATE (ID),
    
    CONSTRAINT FK8193D5EDD052F725
    FOREIGN KEY (OWNER )
    REFERENCES KSEN_STATE_PROCESS (ID)
)
/