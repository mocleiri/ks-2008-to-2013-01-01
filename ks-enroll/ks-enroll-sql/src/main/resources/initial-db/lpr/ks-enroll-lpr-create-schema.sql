DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	LPR_TYPE             VARCHAR2(255) NOT NULL ,
	LPR_STATE            VARCHAR2(255) NOT NULL ,
	LUI_ID               VARCHAR(255) NOT NULL ,
	PERS_ID              VARCHAR2(255) NOT NULL ,
	COMMIT_PERCT         NUMBER NULL ,
	EFF_DT               TIMESTAMP(6) NULL ,
	EXPIR_DT             TIMESTAMP(6) NULL ,
	VER_NBR              NUMBER(19) NOT NULL ,
	CREATETIME           TIMESTAMP(6) NOT NULL ,
	CREATEID             VARCHAR2(255) NOT NULL ,
	UPDATETIME           TIMESTAMP(6) NULL ,
	UPDATEID             VARCHAR(255) NULL 
)
/


ALTER TABLE KSEN_LPR
	ADD CONSTRAINT  KSEN_LPR_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR_ATTR';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR_ATTR CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR_ATTR
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	ATTR_KEY             VARCHAR2(255) NULL ,
	ATTR_VALUE           VARCHAR2(4000) NULL ,
	OWNER_ID             VARCHAR2(255) NULL 
)
/


ALTER TABLE KSEN_LPR_ATTR
	ADD CONSTRAINT  KSEN_LPR_ATTR_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR_RESULT_VAL_GRP';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR_RESULT_VAL_GRP CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR_RESULT_VAL_GRP
(
	ID                   VARCHAR2(255) NOT NULL ,
	LPR_ID               VARCHAR2(255) NOT NULL ,
	RESULT_VAL_GRP_ID    VARCHAR(255) NOT NULL 
)
/


ALTER TABLE KSEN_LPR_RESULT_VAL_GRP
	ADD CONSTRAINT  KSEN_LPR_RESULT_VAL_GRP_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR_ROSTER';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR_ROSTER CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR_ROSTER
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	LPR_ROSTER_TYPE      VARCHAR2(255) NOT NULL ,
	LPR_ROSTER_STATE     VARCHAR2(255) NOT NULL ,
	NAME                 VARCHAR2(255) NULL ,
	DESCR_PLAIN          VARCHAR2(4000) NULL ,
	DESCR_FORMATTED      VARCHAR2(4000) NULL ,
	MAX_CAPACITY         NUMBER NULL ,
	CHECK_IN_REQ_IND     VARCHAR2(1) NULL  CONSTRAINT  CHECK_BOOLEAN_1792658835 CHECK (CHECK_IN_REQ_IND IN ('Y', 'N')),
	CHECK_IN_FREQ_DUR_TYPE VARCHAR2(255) NULL ,
	CHECK_IN_FREQ_TIME_QTY NUMBER NULL ,
	VER_NBR              NUMBER(19) NOT NULL ,
	CREATETIME           TIMESTAMP(6) NOT NULL ,
	CREATEID             VARCHAR2(255) NOT NULL ,
	UPDATETIME           TIMESTAMP(6) NULL ,
	UPDATEID             VARCHAR2(255) NULL 
)
/


ALTER TABLE KSEN_LPR_ROSTER
	ADD CONSTRAINT  KSEN_LPR_ROSTER_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR_ROSTER_ASSO_LUI_ID';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR_ROSTER_ASSO_LUI_ID CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR_ROSTER_ASSO_LUI_ID
(
	ID                   VARCHAR2(255) NOT NULL ,
	LPR_ROSTER_ID        VARCHAR2(255) NOT NULL ,
	LUI_ID               VARCHAR2(255) NOT NULL 
)
/


ALTER TABLE KSEN_LPR_ROSTER_ASSO_LUI_ID
	ADD CONSTRAINT  KSEN_LPR_ROSTER_ASSO_LUI_ID_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR_ROSTER_ATTR';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR_ROSTER_ATTR CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR_ROSTER_ATTR
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	ATTR_KEY             VARCHAR2(255) NULL ,
	ATTR_VALUE           VARCHAR2(4000) NULL ,
	OWNER_ID             VARCHAR2(255) NULL 
)
/


ALTER TABLE KSEN_LPR_ROSTER_ATTR
	ADD CONSTRAINT  KSEN_LPR_ROSTER_ATTR_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR_ROSTER_ENTRY';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR_ROSTER_ENTRY CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR_ROSTER_ENTRY
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	LPR_ROSTER_ENTRY_TYPE VARCHAR2(255) NOT NULL ,
	LPR_ROSTER_ENTRY_STATE VARCHAR2(255) NOT NULL ,
	EFF_DT               TIMESTAMP(6) NULL ,
	EXPIR_DT             TIMESTAMP(6) NULL ,
	POSITION             NUMBER NULL ,
	VER_NBR              NUMBER(19) NOT NULL ,
	CREATETIME           TIMESTAMP(6) NOT NULL ,
	CREATEID             VARCHAR2(255) NOT NULL ,
	UPDATETIME           TIMESTAMP(6) NULL ,
	UPDATEID             VARCHAR2(255) NULL ,
	LPR_ID               VARCHAR2(255) NULL ,
	LPR_ROSTER_ID        VARCHAR2(255) NULL 
)
/


ALTER TABLE KSEN_LPR_ROSTER_ENTRY
	ADD CONSTRAINT  KSEN_LPR_ROSTER_ENTRY_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR_ROSTER_ENTRY_ATTR';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR_ROSTER_ENTRY_ATTR CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR_ROSTER_ENTRY_ATTR
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	ATTR_KEY             VARCHAR2(255) NULL ,
	ATTR_VALUE           VARCHAR2(4000) NULL ,
	OWNER_ID             VARCHAR2(255) NULL 
)
/


ALTER TABLE KSEN_LPR_ROSTER_ENTRY_ATTR
	ADD CONSTRAINT  KSEN_LPR_ROSTER_ENTRY_ATTR_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR_TRANS';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR_TRANS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR_TRANS
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	LPR_TRANS_TYPE       VARCHAR2(255) NOT NULL ,
	LPR_TRANS_STATE      VARCHAR2(255) NOT NULL ,
	NAME                 VARCHAR2(255) NULL ,
	REQUESTING_PERS_ID   VARCHAR2(255) NULL ,
	ATP_ID               VARCHAR2(255) NULL ,
	DESCR_PLAIN          VARCHAR2(4000) NULL ,
	DESCR_FORMATTED      VARCHAR2(4000) NULL ,
	VER_NBR              NUMBER(9) NOT NULL ,
	CREATETIME           TIMESTAMP(6) NOT NULL ,
	CREATEID             VARCHAR2(255) NOT NULL ,
	UPDATETIME           TIMESTAMP(6) NULL ,
	UPDATEID             VARCHAR2(255) NULL 
)
/


ALTER TABLE KSEN_LPR_TRANS
	ADD CONSTRAINT  KSEN_LPR_TRANS_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR_TRANS_ATTR';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR_TRANS_ATTR CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR_TRANS_ATTR
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	ATTR_KEY             VARCHAR2(255) NULL ,
	ATTR_VALUE           VARCHAR2(4000) NULL ,
	OWNER_ID             VARCHAR2(255) NULL 
)
/


ALTER TABLE KSEN_LPR_TRANS_ATTR
	ADD CONSTRAINT  KSEN_LPR_TRANS_ATTR_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR_TRANS_ITEM';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR_TRANS_ITEM CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR_TRANS_ITEM
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	LPR_TRANS_ITEM_TYPE  VARCHAR2(255) NOT NULL ,
	LPR_TRANS_ITEM_STATE VARCHAR2(255) NOT NULL ,
	PERS_ID              VARCHAR2(255) NULL ,
	NEW_LUI_ID           VARCHAR2(255) NULL ,
	EXISTING_LUI_ID      VARCHAR2(255) NULL ,
	NAME                 VARCHAR2(255) NULL ,
	DESCR_PLAIN          VARCHAR2(4000) NULL ,
	DESCR_FORMATTED      VARCHAR2(4000) NULL ,
	VER_NBR              NUMBER(9) NOT NULL ,
	CREATETIME           TIMESTAMP(6) NOT NULL ,
	CREATEID             VARCHAR2(255) NOT NULL ,
	UPDATETIME           TIMESTAMP(6) NULL ,
	UPDATEID             VARCHAR2(255) NULL ,
	GROUP_ID             VARCHAR2(255) NULL ,
	LTI_RESULTING_LPR_ID VARCHAR2(255) NULL ,
	LTI_RESULT_MESSAGE  VARCHAR2(255) NULL ,
	LTI_RESULT_STATUS    VARCHAR2(255) NULL ,
	LPR_TRANS_ID         VARCHAR2(255) NULL 
)
/


ALTER TABLE KSEN_LPR_TRANS_ITEM
	ADD CONSTRAINT  KSEN_LPR_TRANS_ITEM_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR_TRANS_ITEM_ATTR';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR_TRANS_ITEM_ATTR CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR_TRANS_ITEM_ATTR
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	ATTR_KEY             VARCHAR2(255) NULL ,
	ATTR_VALUE           VARCHAR2(4000) NULL ,
	OWNER_ID             VARCHAR2(255) NULL 
)
/


ALTER TABLE KSEN_LPR_TRANS_ITEM_ATTR
	ADD CONSTRAINT  KSEN_LPR_TRANS_ITEM_ATTR_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR_TRANS_ITEM_RQST_OPT';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR_TRANS_ITEM_RQST_OPT CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR_TRANS_ITEM_RQST_OPT
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	OPTION_KEY           VARCHAR2(255) NULL ,
	OPTION_VALUE         VARCHAR2(255) NULL ,
	LPR_TRANS_ITEM_ID    VARCHAR2(255) NULL 
)
/


ALTER TABLE KSEN_LPR_TRANS_ITEM_RQST_OPT
	ADD CONSTRAINT  KSEN_LPR_LTI_RQST_OPT_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_LPR_TRANS_ITEM_RVG';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_LPR_TRANS_ITEM_RVG CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_LPR_TRANS_ITEM_RVG
(
	ID                   VARCHAR2(255) NOT NULL ,
	LPR_TRANS_ITEM_ID    VARCHAR2(255) NOT NULL ,
	RESULT_VAL_GRP_ID    VARCHAR(255) NOT NULL 
)
/


ALTER TABLE KSEN_LPR_TRANS_ITEM_RVG
	ADD CONSTRAINT  KSEN_LPR_LTI_RSLT_VALUE_GRP_P PRIMARY KEY (ID)
/


