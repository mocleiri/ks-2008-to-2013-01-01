TRUNCATE TABLE KREN_CHNL_T DROP STORAGE
/
INSERT ALL
  INTO KREN_CHNL_T (CHNL_ID,DESC_TXT,NM,SUBSCRB_IND,VER_NBR)
  VALUES (500,'Builtin channel for KEW','KEW','N',1)
SELECT * FROM DUAL
/
