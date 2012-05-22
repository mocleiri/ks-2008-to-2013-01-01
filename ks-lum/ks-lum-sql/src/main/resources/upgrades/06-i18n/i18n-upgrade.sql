---------------------------------------------------------------------------------------------------------
-- Create table KSLU_CLU_IDENT_LNG_NAME with FOREIGN (OWNER) KEY which map to KSLU_CLU_IDENT (ID)
---------------------------------------------------------------------------------------------------------
CREATE TABLE KSLU_CLU_IDENT_LNG_NAME
(
      ID VARCHAR2(255)
        , NAME VARCHAR2(255)
        , LOCALE VARCHAR2(255)
        , OWNER VARCHAR2(255)
)
/

ALTER TABLE KSLU_CLU_IDENT_LNG_NAME
    ADD CONSTRAINT KSLU_CLU_IDENT_LNG_NAME_FK1 FOREIGN KEY (OWNER)
    REFERENCES KSLU_CLU_IDENT (ID)
/

---------------------------------------------------------------------------------------------------------
-- Populate KSLU_CLU_IDENT_LNG_NAME with the values from KSLU_CLU_IDENT (LNG_NAME)
---------------------------------------------------------------------------------------------------------
INSERT INTO KSLU_CLU_IDENT_LNG_NAME (ID, NAME, LOCALE, OWNER)
SELECT (ID || '.lngName'), LNG_NAME, 'EN', ID FROM KSLU_CLU_IDENT
/

---------------------------------------------------------------------------------------------------------
-- Alter table KSLU_CLU_IDENT and drop the column LNG_NAME
---------------------------------------------------------------------------------------------------------
ALTER TABLE KSLU_CLU_IDENT DROP COLUMN LNG_NAME
/
