--Use Course Modify Screens
--- Permission
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM, DESC_TXT)
  VALUES ('Y','KS-SYS',sys_guid(),KRIM_PERM_ID_S.NEXTVAL,(SELECT PERM_TMPL_ID FROM KRIM_PERM_TMPL_T WHERE NMSPC_CD='KS-SYS' AND NM='Use Screen'),1,'Use Course Modify Screens','Allows users to access the Course modification screen')
/
--- Permission Detail
INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,PERM_ID,VER_NBR)
  VALUES (KRIM_ATTR_DATA_ID_S.NEXTVAL,'cluModifyItem',(SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM='screenComponent' AND NMSPC_CD='KS-SYS'),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM='Default'),sys_guid(),KRIM_PERM_ID_S.CURRVAL,1)
/
--- Assign to Roles
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y',sys_guid(),(SELECT PERM_ID FROM KRIM_PERM_T WHERE NMSPC_CD='KS-SYS' AND NM='Use Course Modify Screens'),(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE NMSPC_CD='UMD-KS-CM' AND ROLE_NM='Kuali Student CM Admin'),KRIM_ROLE_PERM_ID_S.NEXTVAL,1)
/
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y',sys_guid(),(SELECT PERM_ID FROM KRIM_PERM_T WHERE NMSPC_CD='KS-SYS' AND NM='Use Course Modify Screens'),(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE NMSPC_CD='UMD-KS-CM' AND ROLE_NM='Kuali Student CM User'),KRIM_ROLE_PERM_ID_S.NEXTVAL,1)
/