TRUNCATE TABLE KSLU_CLU_SET_JN_CLU_SET DROP STORAGE
/
INSERT INTO KSLU_CLU_SET_JN_CLU_SET (CLU_SET_CHILD_ID,CLU_SET_PARENT_ID)
  VALUES ('CLUSET-3','CLUSET-2')
/
INSERT INTO KSLU_CLU_SET_JN_CLU_SET (CLU_SET_CHILD_ID,CLU_SET_PARENT_ID)
  VALUES ('CLUSET-2','CLUSET-1')
/
INSERT INTO KSLU_CLU_SET_JN_CLU_SET (CLU_SET_CHILD_ID,CLU_SET_PARENT_ID)
  VALUES ('CLUSET-4','CLUSET-2')
/