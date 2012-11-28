
DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_POPULATION';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_POPULATION CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_POPULATION
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	POPULATION_TYPE      VARCHAR2(255) NOT NULL ,
	POPULATION_STATE     VARCHAR2(255) NOT NULL ,
	NAME                 VARCHAR2(255) NULL ,
	POPULATION_RULE_ID   VARCHAR2(255) NULL ,
	DESCR_PLAIN          VARCHAR2(4000) NULL ,
	DESCR_FORMATTED      VARCHAR2(255) NULL ,
	VER_NBR              NUMBER(19) NOT NULL ,
	CREATETIME           TIMESTAMP(6) NOT NULL ,
	CREATEID             VARCHAR2(255) NOT NULL ,
	UPDATETIME           TIMESTAMP(6) NULL ,
	UPDATEID             VARCHAR2(255) NULL
)
/


ALTER TABLE KSEN_POPULATION
	ADD CONSTRAINT  KSEN_POP_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_POPULATION_ATTR';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_POPULATION_ATTR CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_POPULATION_ATTR
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	ATTR_KEY             VARCHAR2(255) NULL ,
	ATTR_VALUE           VARCHAR2(4000) NULL ,
	OWNER_ID             VARCHAR2(255) NULL
)
/


ALTER TABLE KSEN_POPULATION_ATTR
	ADD CONSTRAINT  KSEN_POP_ATTR PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_POPULATION_CAT';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_POPULATION_CAT CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_POPULATION_CAT
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	POPULATION_CAT_TYPE  VARCHAR2(255) NOT NULL ,
	POPULATION_CAT_STATE VARCHAR2(255) NOT NULL ,
	NAME                 VARCHAR2(255) NULL ,
	DESCR_PLAIN          VARCHAR2(4000) NULL ,
	DESCR_FORMATTED      VARCHAR2(4000) NULL ,
	VER_NBR              NUMBER(19) NOT NULL ,
	CREATETIME           TIMESTAMP(6) NOT NULL ,
	CREATEID             VARCHAR2(255) NOT NULL ,
	UPDATETIME           TIMESTAMP(6) NULL ,
	UPDATEID             VARCHAR2(255) NULL
)
/


ALTER TABLE KSEN_POPULATION_CAT
	ADD CONSTRAINT  KSEN_POP_CAT_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_POPULATION_CAT_ATTR';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_POPULATION_CAT_ATTR CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_POPULATION_CAT_ATTR
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	ATTR_KEY             VARCHAR2(255) NULL ,
	ATTR_VALUE           VARCHAR2(4000) NULL ,
	OWNER_ID             VARCHAR2(255) NULL
)
/


ALTER TABLE KSEN_POPULATION_CAT_ATTR
	ADD CONSTRAINT  KSEN_POP_CAT_ATTR_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_POPULATION_CAT_RELTN';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_POPULATION_CAT_RELTN CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_POPULATION_CAT_RELTN
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	POPULATION_ID        VARCHAR2(255) NOT NULL ,
	POPULATION_CAT_ID    VARCHAR2(255) NOT NULL
)
/


ALTER TABLE KSEN_POPULATION_CAT_RELTN
	ADD CONSTRAINT  KSEN_POP_CAT_RELTN_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_POPULATION_RULE';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_POPULATION_RULE CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_POPULATION_RULE
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	POPULATION_RULE_TYPE VARCHAR2(255) NOT NULL ,
	POPULATION_RULE_STATE VARCHAR2(255) NOT NULL ,
	NAME                 VARCHAR2(255) NULL ,
	REF_POPULATION_ID    VARCHAR2(255) NULL ,
	VARIES_BY_TIME_IND   NUMBER(1) NOT NULL ,
	SUPPORTS_GET_MBR_IND NUMBER(1) NOT NULL ,
	DESCR_PLAIN          VARCHAR2(4000) NULL ,
	DESCR_FORMATTED      VARCHAR2(255) NULL ,
	VER_NBR              NUMBER(19) NOT NULL ,
	CREATETIME           TIMESTAMP(6) NOT NULL ,
	CREATEID             VARCHAR2(255) NOT NULL ,
	UPDATETIME           TIMESTAMP(6) NULL ,
	UPDATEID             VARCHAR2(255) NULL
)
/


ALTER TABLE KSEN_POPULATION_RULE
	ADD CONSTRAINT  KSEN_POP_RULE_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_POPULATION_RULE_AGENDA';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_POPULATION_RULE_AGENDA CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_POPULATION_RULE_AGENDA
(
	AGENDA_ID            VARCHAR2(255) NOT NULL ,
	POPULATION_RULE_ID   VARCHAR2(255) NOT NULL
)
/


ALTER TABLE KSEN_POPULATION_RULE_AGENDA
	ADD CONSTRAINT  KSEN_POP_RULE_AGENDA_P PRIMARY KEY (POPULATION_RULE_ID,AGENDA_ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_POPULATION_RULE_ATTR';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_POPULATION_RULE_ATTR CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_POPULATION_RULE_ATTR
(
	ID                   VARCHAR2(255) NOT NULL ,
	OBJ_ID               VARCHAR2(36) NULL ,
	ATTR_KEY             VARCHAR2(255) NULL ,
	ATTR_VALUE           VARCHAR2(4000) NULL ,
	OWNER_ID             VARCHAR2(255) NULL
)
/


ALTER TABLE KSEN_POPULATION_RULE_ATTR
	ADD CONSTRAINT  KSEN_POP_RULE_ATTR_P PRIMARY KEY (ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_POPULATION_RULE_CHILD_POP';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_POPULATION_RULE_CHILD_POP CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_POPULATION_RULE_CHILD_POP
(
	POPULATION_RULE_ID   VARCHAR2(255) NOT NULL ,
	CHILD_POPULATION_ID  VARCHAR2(255) NOT NULL
)
/


ALTER TABLE KSEN_POPULATION_RULE_CHILD_POP
	ADD CONSTRAINT  KSEN_POP_RULE_CHILD_POP_P PRIMARY KEY (POPULATION_RULE_ID,CHILD_POPULATION_ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_POPULATION_RULE_GRP';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_POPULATION_RULE_GRP CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_POPULATION_RULE_GRP
(
	POPULATION_RULE_ID   VARCHAR2(255) NOT NULL ,
	GROUP_ID             VARCHAR2(255) NOT NULL
)
/


ALTER TABLE KSEN_POPULATION_RULE_GRP
	ADD CONSTRAINT  KSEN_POP_RULE_GRP_P PRIMARY KEY (POPULATION_RULE_ID,GROUP_ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_POPULATION_RULE_PERS';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_POPULATION_RULE_PERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_POPULATION_RULE_PERS
(
	POPULATION_RULE_ID   VARCHAR2(255) NOT NULL ,
	PERSON_ID            VARCHAR2(255) NOT NULL
)
/


ALTER TABLE KSEN_POPULATION_RULE_PERS
	ADD CONSTRAINT  KSEN_POP_RULE_PERS_P PRIMARY KEY (POPULATION_RULE_ID,PERSON_ID)
/


DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_POPULATION_RULE_SOT';
	IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_POPULATION_RULE_SOT CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_POPULATION_RULE_SOT
(
	POPULATION_RULE_ID   VARCHAR2(255) NOT NULL ,
	SORT_ORDER_TYPE_ID   VARCHAR2(255) NOT NULL
)
/


ALTER TABLE KSEN_POPULATION_RULE_SOT
	ADD CONSTRAINT  KSEN_POP_RULE_SOT_P PRIMARY KEY (POPULATION_RULE_ID,SORT_ORDER_TYPE_ID)
/