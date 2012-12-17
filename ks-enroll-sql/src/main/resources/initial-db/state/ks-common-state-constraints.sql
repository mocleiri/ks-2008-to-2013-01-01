CREATE UNIQUE INDEX KSEN_STATE_P ON KSEN_STATE
(ID   ASC)
/


ALTER TABLE KSEN_STATE
	ADD CONSTRAINT  KSEN_STATE_P PRIMARY KEY (ID)
/


CREATE  INDEX KSEN_STATE_IF1 ON KSEN_STATE
(LIFECYCLE_KEY   ASC)
/




CREATE UNIQUE INDEX KSEN_STATE_ATTR_P ON KSEN_STATE_ATTR
(ID   ASC)
/


ALTER TABLE KSEN_STATE_ATTR
	ADD CONSTRAINT  KSEN_STATE_ATTR_P PRIMARY KEY (ID)
/


CREATE  INDEX KSEN_STATE_ATTR_IF1 ON KSEN_STATE_ATTR
(OWNER_ID   ASC)
/





CREATE UNIQUE INDEX KSEN_STATE_LIFECYCLE_P ON KSEN_STATE_LIFECYCLE
(ID   ASC)
/


ALTER TABLE KSEN_STATE_LIFECYCLE
	ADD CONSTRAINT  KSEN_STATE_LIFECYCLE_P PRIMARY KEY (ID)
/


CREATE  INDEX KSEN_STATE_LIFECYCLE_I1 ON KSEN_STATE_LIFECYCLE
(REF_OBJECT_URI   ASC)
/


CREATE UNIQUE INDEX KSEN_STATE_LIFECYCLE_ATTR_P ON KSEN_STATE_LIFECYCLE_ATTR
(ID   ASC)
/


ALTER TABLE KSEN_STATE_LIFECYCLE_ATTR
	ADD CONSTRAINT  KSEN_STATE_LIFECYCLE_ATTR_P PRIMARY KEY (ID)
/


CREATE  INDEX KSEN_STATE_LIFECYCLE_ATTR_IF1 ON KSEN_STATE_LIFECYCLE_ATTR
(OWNER_ID   ASC)
/


ALTER TABLE KSEN_STATE
	ADD (CONSTRAINT KSEN_STATE_FK1 FOREIGN KEY (LIFECYCLE_KEY) REFERENCES KSEN_STATE_LIFECYCLE (ID))
/


ALTER TABLE KSEN_STATE_ATTR
	ADD (CONSTRAINT KSEN_STATE_ATTR_FK1 FOREIGN KEY (OWNER_ID) REFERENCES KSEN_STATE (ID))
/


ALTER TABLE KSEN_STATE_LIFECYCLE_ATTR
	ADD (CONSTRAINT KSEN_STATE_LIFECYCLE_ATTR_FK1 FOREIGN KEY (OWNER_ID) REFERENCES KSEN_STATE_LIFECYCLE (ID))
/


-----------------------------------------------------------------------------
-- State Change constraints
-----------------------------------------------------------------------------
ALTER TABLE KSEN_STATE_CHG
	ADD CONSTRAINT  KSEN_STATE_CHG_P PRIMARY KEY (ID)
/


ALTER TABLE KSEN_STATE_CHG_ATTR
	ADD CONSTRAINT  KSEN_STATE_CHG_ATTR_P PRIMARY KEY (ID)
/

 CREATE  INDEX KSEN_STATE_CHG_I1 ON KSEN_STATE_CHG
 (STATE_CHG_TYPE   ASC)
 /


 CREATE  INDEX KSEN_STATE_CHG_I2 ON KSEN_STATE_CHG
 (FROM_STATE_ID   ASC,TO_STATE_ID   ASC)
 /


 CREATE  INDEX KSEN_STATE_CHG_IF1 ON KSEN_STATE_CHG
 (FROM_STATE_ID   ASC)
 /


 CREATE  INDEX KSEN_STATE_CHG_IF2 ON KSEN_STATE_CHG
 (TO_STATE_ID   ASC)
 /


 CREATE  INDEX KSEN_STATE_CHG_ATTR_IF1 ON KSEN_STATE_CHG_ATTR
 (OWNER_ID   ASC)
 /

 ALTER TABLE KSEN_STATE_CHG
 	ADD (CONSTRAINT KSEN_STATE_CHG_FK1 FOREIGN KEY (FROM_STATE_ID) REFERENCES KSEN_STATE (ID))
 /


 ALTER TABLE KSEN_STATE_CHG
 	ADD (CONSTRAINT KSEN_STATE_CHG_IF2 FOREIGN KEY (TO_STATE_ID) REFERENCES KSEN_STATE (ID))
 /


 ALTER TABLE KSEN_STATE_CHG_ATTR
 	ADD (CONSTRAINT KSEN_STATE_CHG_ATTR_FK1 FOREIGN KEY (OWNER_ID) REFERENCES KSEN_STATE_CHG (ID))
 /


ALTER TABLE KSEN_STATE_PROPAGT
	ADD CONSTRAINT  KSEN_STATE_PROPAGT_P PRIMARY KEY (ID)
/


CREATE  INDEX KSEN_STATE_PROPAGT_I1 ON KSEN_STATE_PROPAGT
(STATE_PROPAGT_TYPE   ASC)
/


CREATE  INDEX KSEN_STATE_PROPAGT_I2 ON KSEN_STATE_PROPAGT
(TARGET_STATE_CHG_ID   ASC)
/

ALTER TABLE KSEN_STATE_PROPAGT
	ADD (CONSTRAINT KSEN_STATE_PROPAGT_FK1 FOREIGN KEY (TARGET_STATE_CHG_ID) REFERENCES KSEN_STATE_CHG (ID))
/

ALTER TABLE KSEN_STATE_PROPAGT_ATTR
	ADD CONSTRAINT  KSEN_STATE_PROPAGT_ATTR_P PRIMARY KEY (ID)
/


ALTER TABLE KSEN_STATE_PROPAGT_ATTR
	ADD (CONSTRAINT KSEN_STATE_PROPAGT_ATTR_FK1 FOREIGN KEY (OWNER_ID) REFERENCES KSEN_STATE_PROPAGT (ID))
/


CREATE  INDEX KSEN_STATE_PROPAGT_C_IF1 ON KSEN_STATE_PROPAGT_CNSTRNT
(STATE_CNSTRNT_ID   ASC)
/


CREATE  INDEX KSEN_STATE_PROPAGT_C_IF2 ON KSEN_STATE_PROPAGT_CNSTRNT
(STATE_PROPAGT_ID   ASC)
/


ALTER TABLE KSEN_STATE_PROPAGT_CNSTRNT
	ADD CONSTRAINT  KSEN_STATE_PROPAGT_C_P PRIMARY KEY (STATE_PROPAGT_ID,STATE_CNSTRNT_ID)
/


ALTER TABLE KSEN_STATE_PROPAGT_CNSTRNT
	ADD (CONSTRAINT KSEN_STATE_PROPAGT_CNS_FK1 FOREIGN KEY (STATE_PROPAGT_ID) REFERENCES KSEN_STATE_PROPAGT (ID))
/

ALTER TABLE KSEN_STATE_PROPAGT_CNSTRNT
	ADD (CONSTRAINT KSEN_STATE_PROPAGT_CNS_FK2 FOREIGN KEY (STATE_CNSTRNT_ID) REFERENCES KSEN_STATE_CNSTRNT (ID))
/

CREATE  INDEX KSEN_STATE_CNSTRNT_I1 ON KSEN_STATE_CNSTRNT
(STATE_CNSTRNT_TYPE   ASC)
/

ALTER TABLE KSEN_STATE_CNSTRNT_ROS
	ADD (CONSTRAINT KSEN_STATE_CNSTRNT_ROS_FK1 FOREIGN KEY (STATE_CNSTRNT_ID) REFERENCES KSEN_STATE_CNSTRNT (ID))
/

ALTER TABLE KSEN_STATE_CNSTRNT_ROS
    ADD (CONSTRAINT KSEN_STATE_CNSTRNT_ROS_FK2 FOREIGN KEY (REL_OBJ_STATE_ID) REFERENCES KSEN_STATE (ID))
/

ALTER TABLE KSEN_STATE_CHG_CNSTRNT
	ADD CONSTRAINT  KSEN_STATE_CHG_C_P PRIMARY KEY (STATE_CHG_ID,STATE_CNSTRNT_ID)
/

CREATE  INDEX KSEN_STATE_CHG_C_IF1 ON KSEN_STATE_CHG_CNSTRNT
(STATE_CHG_ID   ASC)
/


CREATE  INDEX KSEN_STATE_CHG_C_IF2 ON KSEN_STATE_CHG_CNSTRNT
(STATE_CNSTRNT_ID   ASC)
/

ALTER TABLE KSEN_STATE_CHG_PROPAGT
	ADD CONSTRAINT  KSEN_STATE_CHG_P_P PRIMARY KEY (STATE_CHG_ID,STATE_PROPAGT_ID)
/

CREATE  INDEX KSEN_STATE_CHG_P_IF1 ON KSEN_STATE_CHG_PROPAGT
(STATE_CHG_ID   ASC)
/


CREATE  INDEX KSEN_STATE_CHG_P_IF2 ON KSEN_STATE_CHG_PROPAGT
(STATE_PROPAGT_ID   ASC)
/
