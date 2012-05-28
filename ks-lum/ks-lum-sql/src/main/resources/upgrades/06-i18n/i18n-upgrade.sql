---------------------------------------------------------------------------------------------------------
-- Create table KSLU_CLU_IDENT_INTL_VALUES with FOREIGN (OWNER) KEY which map to KSLU_CLU_IDENT (ID)
---------------------------------------------------------------------------------------------------------
CREATE TABLE KSLU_CLU_IDENT_INTL_VALUES
(
      ID VARCHAR2(255)
        , VALUE VARCHAR2(255)
        , LOCALE VARCHAR2(255)
        , OWNER VARCHAR2(255)
        , OBJ_ID VARCHAR2(36)
)
/

ALTER TABLE KSLU_CLU_IDENT_INTL_VALUES
    ADD CONSTRAINT KSLU_CLU_IDENT_INTL_VALUES_FK1 FOREIGN KEY (OWNER)
    REFERENCES KSLU_CLU_IDENT (ID)
/

---------------------------------------------------------------------------------------------------------
-- Populate KSLU_CLU_IDENT_INTL_VALUES with the values from KSLU_CLU_IDENT (LNG_NAME)
---------------------------------------------------------------------------------------------------------
INSERT INTO KSLU_CLU_IDENT_INTL_VALUES (ID, VALUE, LOCALE, OWNER)
SELECT (ID || '.lngName'), LNG_NAME, 'EN', ID FROM KSLU_CLU_IDENT
/

---------------------------------------------------------------------------------------------------------
-- Alter table KSLU_CLU_IDENT and drop the column LNG_NAME
---------------------------------------------------------------------------------------------------------
ALTER TABLE KSLU_CLU_IDENT DROP COLUMN LNG_NAME
/
