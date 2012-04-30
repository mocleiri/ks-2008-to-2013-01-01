--------------------------------------------------------
--  Constraints for Table KSEN_ATP
--------------------------------------------------------

  ALTER TABLE "KSEN_ATP" ADD CONSTRAINT "KSEN_ATP_P" PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_ATPATP_RELTN
--------------------------------------------------------

  ALTER TABLE "KSEN_ATPATP_RELTN"
      ADD CONSTRAINT  "KSEN_ATPATP_RELTN_P" PRIMARY KEY ("ID")
/

--------------------------------------------------------
--  Constraints for Table KSEN_ATPMSTONE_RELTN
--------------------------------------------------------

  ALTER TABLE "KSEN_ATPMSTONE_RELTN" ADD CONSTRAINT  "KSEN_ATPMSTONE_RELTN_P" PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_ATP_TYPE
--------------------------------------------------------

  ALTER TABLE "KSEN_ATP_TYPE" MODIFY ("TYPE_KEY" NOT NULL ENABLE)
/
 
  ALTER TABLE "KSEN_ATP_TYPE" ADD PRIMARY KEY ("TYPE_KEY") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_ATP_STATE
--------------------------------------------------------

  ALTER TABLE "KSEN_ATP_STATE" MODIFY ("ID" NOT NULL ENABLE)
/
 
  ALTER TABLE "KSEN_ATP_STATE" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_COMM_STATE
--------------------------------------------------------

  ALTER TABLE "KSEN_COMM_STATE" MODIFY ("ID" NOT NULL ENABLE)
/

  ALTER TABLE "KSEN_COMM_STATE" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_MSTONE
--------------------------------------------------------

  ALTER TABLE "KSEN_MSTONE" ADD CONSTRAINT "KSEN_MSTONE_P" PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_MSTONE_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_MSTONE_ATTR" ADD CONSTRAINT "KSEN_MSTONE_ATTR_P" PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_RICH_TEXT_T
--------------------------------------------------------

  ALTER TABLE "KSEN_RICH_TEXT_T" MODIFY ("ID" NOT NULL ENABLE)
/
 
  ALTER TABLE "KSEN_RICH_TEXT_T" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_STATEPROCESS_RELTN
--------------------------------------------------------

  ALTER TABLE "KSEN_STATEPROCESS_RELTN" MODIFY ("ID" NOT NULL ENABLE)
/
 
  ALTER TABLE "KSEN_STATEPROCESS_RELTN" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_STATE_PROCESS
--------------------------------------------------------

  ALTER TABLE "KSEN_STATE_PROCESS" MODIFY ("ID" NOT NULL ENABLE)
/
 
  ALTER TABLE "KSEN_STATE_PROCESS" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_ATPMSTONE_RELTN_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_ATPMSTONE_RELTN_ATTR" MODIFY ("ID" NOT NULL ENABLE)
/
 
  ALTER TABLE "KSEN_ATPMSTONE_RELTN_ATTR" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_ISSUE_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_ISSUE_ATTR" MODIFY ("ID" NOT NULL ENABLE)
/
 
  ALTER TABLE "KSEN_ISSUE_ATTR" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_RESTRICTION_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_RESTRICTION_ATTR" MODIFY ("ID" NOT NULL ENABLE)
/
 
  ALTER TABLE "KSEN_RESTRICTION_ATTR" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_ATPATP_RELTN_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_ATPATP_RELTN_ATTR" ADD CONSTRAINT "KSEN_ATPATP_RELTN_ATTR_P" PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_ATP_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_ATP_ATTR" ADD CONSTRAINT "KSEN_ATP_ATTR_P" PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_ATPATP_RELTN
--------------------------------------------------------

  ALTER TABLE "KSEN_ATPATP_RELTN" ADD CONSTRAINT "KSEN_ATPATP_RELTN_FK1" FOREIGN KEY ("ATP_ID")
	  REFERENCES "KSEN_ATP" ("ID") ENABLE
/
 
  ALTER TABLE "KSEN_ATPATP_RELTN" ADD CONSTRAINT "KSEN_ATPATP_RELTN_FK2" FOREIGN KEY ("RELATED_ATP_ID")
	  REFERENCES "KSEN_ATP" ("ID") ENABLE
/
 
--------------------------------------------------------
--  Ref Constraints for Table KSEN_ATPMSTONE_RELTN
--------------------------------------------------------

  ALTER TABLE "KSEN_ATPMSTONE_RELTN" ADD CONSTRAINT "KSEN_ATPMSTONE_RELTN_FK1" FOREIGN KEY ("ATP_ID")
	  REFERENCES "KSEN_ATP" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_ATP
--------------------------------------------------------

-- None anymore

--------------------------------------------------------
--  Ref Constraints for Table KSEN_HOLD
--------------------------------------------------------

  ALTER TABLE "KSEN_HOLD" ADD CONSTRAINT "FKEA92B02D654F946E" FOREIGN KEY ("TYPE_ID")
	  REFERENCES "KSEN_HOLD_TYPE" ("TYPE_KEY") ENABLE
/
 
  ALTER TABLE "KSEN_HOLD" ADD CONSTRAINT "FKEA92B02D98517E2C" FOREIGN KEY ("STATE_ID")
	  REFERENCES "KSEN_COMM_STATE" ("ID") ENABLE
/
 
  ALTER TABLE "KSEN_HOLD" ADD CONSTRAINT "FKEA92B02DB16974B" FOREIGN KEY ("ISSUE_ID")
	  REFERENCES "KSEN_ISSUE" ("ID") ENABLE
/
 
  ALTER TABLE "KSEN_HOLD" ADD CONSTRAINT "FKEA92B02DCDDEC8D3" FOREIGN KEY ("RT_DESCR_ID")
	  REFERENCES "KSEN_HOLD_RICH_TEXT" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_ISSUE
--------------------------------------------------------

  ALTER TABLE "KSEN_ISSUE" ADD CONSTRAINT "FK67D35B0B654F946E" FOREIGN KEY ("TYPE_ID")
	  REFERENCES "KSEN_HOLD_TYPE" ("TYPE_KEY") ENABLE
/
 
  ALTER TABLE "KSEN_ISSUE" ADD CONSTRAINT "FK67D35B0B98517E2C" FOREIGN KEY ("STATE_ID")
	  REFERENCES "KSEN_COMM_STATE" ("ID") ENABLE
/
 
  ALTER TABLE "KSEN_ISSUE" ADD CONSTRAINT "FK67D35B0BCDDEC8D3" FOREIGN KEY ("RT_DESCR_ID")
	  REFERENCES "KSEN_HOLD_RICH_TEXT" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_MSTONE
--------------------------------------------------------

-- None anymore

--------------------------------------------------------
--  Ref Constraints for Table KSEN_RESTRICTION
--------------------------------------------------------

  ALTER TABLE "KSEN_RESTRICTION" ADD CONSTRAINT "FKC2C6CC7E654F946E" FOREIGN KEY ("TYPE_ID")
	  REFERENCES "KSEN_HOLD_TYPE" ("TYPE_KEY") ENABLE
/
 
  ALTER TABLE "KSEN_RESTRICTION" ADD CONSTRAINT "FKC2C6CC7E98517E2C" FOREIGN KEY ("STATE_ID")
	  REFERENCES "KSEN_COMM_STATE" ("ID") ENABLE
/
 
  ALTER TABLE "KSEN_RESTRICTION" ADD CONSTRAINT "FKC2C6CC7ECDDEC8D3" FOREIGN KEY ("RT_DESCR_ID")
	  REFERENCES "KSEN_HOLD_RICH_TEXT" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_STATEPROCESS_RELTN
--------------------------------------------------------

  ALTER TABLE "KSEN_STATEPROCESS_RELTN" ADD CONSTRAINT "FKCDFDC0E024D2ACA6" FOREIGN KEY ("PRIOR_STATEKEY")
	  REFERENCES "KSEN_COMM_STATE" ("ID") ENABLE
/
 
  ALTER TABLE "KSEN_STATEPROCESS_RELTN" ADD CONSTRAINT "FKCDFDC0E09F94F29D" FOREIGN KEY ("NEXT_STATEKEY")
	  REFERENCES "KSEN_COMM_STATE" ("ID") ENABLE
/
 
  ALTER TABLE "KSEN_STATEPROCESS_RELTN" ADD CONSTRAINT "FKCDFDC0E0D6162101" FOREIGN KEY ("PROCESS_KEY")
	  REFERENCES "KSEN_STATE_PROCESS" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_ATPATP_RELTN_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_ATPATP_RELTN_ATTR" ADD CONSTRAINT "KSEN_ATPATP_RELTN_ATTR_FK1" FOREIGN KEY ("OWNER")
	  REFERENCES "KSEN_ATPATP_RELTN" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_ATPMSTONE_RELTN_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_ATPMSTONE_RELTN_ATTR" ADD CONSTRAINT "FK586D7715FC300ED8" FOREIGN KEY ("OWNER")
	  REFERENCES "KSEN_ATPMSTONE_RELTN" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_ATP_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_ATP_ATTR" ADD CONSTRAINT "KSEN_ATP_ATTR_FK1" FOREIGN KEY ("OWNER")
	  REFERENCES "KSEN_ATP" ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table KSEN_HOLD_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_HOLD_ATTR" MODIFY ("ID" NOT NULL ENABLE)
/
 
  ALTER TABLE "KSEN_HOLD_ATTR" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_HOLD_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_HOLD_ATTR" ADD CONSTRAINT "FK87BEEEC3DE9EDF87" FOREIGN KEY ("OWNER")
	  REFERENCES "KSEN_HOLD" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_HOLD_TYPE_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_HOLD_TYPE_ATTR" ADD CONSTRAINT "FK31328E449E7CA6E1" FOREIGN KEY ("OWNER")
	  REFERENCES "KSEN_HOLD_TYPE" ("TYPE_KEY") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_ISSUE_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_ISSUE_ATTR" ADD CONSTRAINT "FKE52C5EA5E7C51BBD" FOREIGN KEY ("OWNER")
	  REFERENCES "KSEN_ISSUE" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_RESTRICTION_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_RESTRICTION_ATTR" ADD CONSTRAINT "FK1F4DD12D828D9F0" FOREIGN KEY ("OWNER")
	  REFERENCES "KSEN_RESTRICTION" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_MSTONE_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_MSTONE_ATTR" ADD CONSTRAINT "FK3DFA6EE1BA0FC113" FOREIGN KEY ("OWNER")
	  REFERENCES "KSEN_MSTONE" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table KSEN_ATPTYPE_ATTR
--------------------------------------------------------

  ALTER TABLE "KSEN_ATPTYPE_ATTR" ADD CONSTRAINT "FK3DFA6EE13309051A" FOREIGN KEY ("OWNER")
	  REFERENCES "KSEN_ATP_TYPE" ("TYPE_KEY") ENABLE
/
