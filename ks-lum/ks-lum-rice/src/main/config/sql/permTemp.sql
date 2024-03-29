--
-- Copyright 2010 The Kuali Foundation Licensed under the
-- Educational Community License, Version 2.0 (the "License"); you may
-- not use this file except in compliance with the License. You may
-- obtain a copy of the License at
--
-- http://www.osedu.org/licenses/ECL-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an "AS IS"
-- BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
-- or implied. See the License for the specific language governing
-- permissions and limitations under the License.
--

-- insert permission type for field permissions
INSERT INTO KRIM_TYP_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,VER_NBR) 
  VALUES ('Y','120','Field Permission Type','KS-SYS','FIELDPERMISSION0000000000000TYP',1);
-- type attribute definition for DTO Name
INSERT INTO KRIM_ATTR_DEFN_T (ACTV_IND,APPL_URL,CMPNT_NM,KIM_ATTR_DEFN_ID,NM,NMSPC_CD,OBJ_ID,VER_NBR) 
  VALUES ('Y','${application.url}','org.kuali.rice.student.bo.KualiStudentKimAttributes','110','dtoName','KS-SYS','COLLEGESYSTYPE00000000000ATTRDEF',1);
INSERT INTO KRIM_TYP_ATTR_T (ACTV_IND,KIM_ATTR_DEFN_ID,KIM_TYP_ATTR_ID,KIM_TYP_ID,OBJ_ID,SORT_CD,VER_NBR) 
  VALUES ('Y','110','205','120','1OLLEGETYPE00000000000000TYPATTR','a',1);
-- type attribute definition for Field Name
INSERT INTO KRIM_ATTR_DEFN_T (ACTV_IND,APPL_URL,CMPNT_NM,KIM_ATTR_DEFN_ID,NM,NMSPC_CD,OBJ_ID,VER_NBR) 
  VALUES ('Y','${application.url}','org.kuali.rice.student.bo.KualiStudentKimAttributes','111','dtoFieldKey','KS-SYS','COLLEGESY1TYPE00000000000ATTRDEF',1);
INSERT INTO KRIM_TYP_ATTR_T (ACTV_IND,KIM_ATTR_DEFN_ID,KIM_TYP_ATTR_ID,KIM_TYP_ID,OBJ_ID,SORT_CD,VER_NBR) 
  VALUES ('Y','111','206','120','2OLLEGETYPE00000000000000TYPATTR','b',1);
-- type attribute definition for Action
INSERT INTO KRIM_ATTR_DEFN_T (ACTV_IND,APPL_URL,CMPNT_NM,KIM_ATTR_DEFN_ID,NM,NMSPC_CD,OBJ_ID,VER_NBR) 
  VALUES ('Y','${application.url}','org.kuali.rice.student.bo.KualiStudentKimAttributes','112','fieldAccessLevel','KS-SYS','COLLEGESY2TYPE00000000000ATTRDEF',1);
INSERT INTO KRIM_TYP_ATTR_T (ACTV_IND,KIM_ATTR_DEFN_ID,KIM_TYP_ATTR_ID,KIM_TYP_ID,OBJ_ID,SORT_CD,VER_NBR) 
  VALUES ('Y','112','207','120','3OLLEGETYPE00000000000000TYPATTR','c',1);
-- insert template for KIM Permission for KS Fields
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','120','Field Access','KS-SYS','AC27A267ET5CAA7E0404F81EEOO30AA','120',1);

-- insert field perm for 'courseTitle'
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-LUM','5B4F0974494BEF33E0R04EX189D8AT24','3105','120',1, 'Field Permission Type');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7BB976406BAA02E0404F81A9D86F1B','120','110','2400','3105','kuali.lu.type.CreditCourse');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976416BAA02E0404F8189D86F1C','120','111','2401','3105','courseTitle');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976426BAA02E0404F8189D86F1D','120','112','2402','3105','edit');
-- insert field perm for 'department'
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-LUM','5B4F0974494BEF33E0R04EX189D8AT26','3106','120',1, 'Field Permission Type');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976436BAA02E0404F8189D86F1E','120','110','2403','3106','kuali.lu.type.CreditCourse');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976446BAA02E0404F8189D86F1F','120','111','2404','3106','department');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976456BAA02E0404F8189D86F1G','120','112','2405','3106','edit');
-- insert field perm for 'description'
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-LUM','5B4F0974494BEF33E0R04EX189D8AT27','3107','120',1, 'Field Permission Type');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9AA6406BAA02E0404F8189D86F1H','120','110','2406','3107','kuali.lu.type.CreditCourse');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAA02E0404F8189D86F1I','120','111','2407','3107','description');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAA02B04AAF8189D86F1J','120','112','2408','3107','edit');

-- insert field perm for 'duration'
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-LUM','5B4F0974494BEF3AE0R04EX189D8AT27','3108','120',1, 'Field Permission Type');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAA02E0404F8189D86F1K','120','110','2409','3108','kuali.lu.type.CreditCourse');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAA02E0404F8189D86F1L','120','111','2410','3108','duration');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAA02E0404F8189D86F1M','120','112','2411','3108','edit');

INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','1','KS-SYS','DEPARTMENTWDMINREVIEWER20000ROLE','920','Global Field Editor',1);
INSERT INTO KRIM_ROLE_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,ROLE_ID,ROLE_MBR_ID,VER_NBR) 
  VALUES ('fred','P','5BAA421E43857CC7E04AAF8189D821F7','920','1300',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','1','KS-SYS','DEPARTMANTWDMINREVIEWER20000ROLE','921','Title/Description Field Editor',1);
INSERT INTO KRIM_ROLE_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,ROLE_ID,ROLE_MBR_ID,VER_NBR) 
  VALUES ('fran','P','5BAA421A43857717E04AAFAA89D821F7','921','1301',1);

INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EA5C7O17E0404F81EEOO30AB','3105','920','780',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EV5C7O17E0404F81EEOO30AC','3106','920','781',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267ER5C7O17E0404F81EEOO30AD','3107','920','782',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EQ5C7O17E0404F81EEOO30AE','3108','920','783',1);

INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267ET5C7O17E0404F81EEOO30AF','3105','921','784',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267ET5C7O17E0404F81EEOO30AG','3107','921','785',1);
