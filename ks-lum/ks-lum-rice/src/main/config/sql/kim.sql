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


-- clean up ksb registry table
DELETE FROM KRSB_SVC_DEF_T;

-- create namespaces for lookups
INSERT INTO KRNS_NMSPC_T (NMSPC_CD, OBJ_ID, VER_NBR, NM, ACTV_IND, APPL_NMSPC_CD)
  VALUES ('KS-LUM', 'F102F3FA08CF45CFAA404FBB89D831AA', 1, 'Kuali Student Learning Unit Management', 'Y', 'STUDENT');
INSERT INTO KRNS_NMSPC_T (NMSPC_CD, OBJ_ID, VER_NBR, NM, ACTV_IND, APPL_NMSPC_CD)
  VALUES ('KS-SYS', 'G102F3FA08CF45CFAA404FBB89D831AA', 1, 'Kuali Student System', 'Y', 'STUDENT');

-- create principal for KS system user
INSERT INTO KRIM_ENTITY_T (ACTV_IND,ENTITY_ID,OBJ_ID,VER_NBR)
  VALUES ('Y','5','VV1B6B919CC96496E0404F8189D822F2',1);
INSERT INTO KRIM_PRNCPL_T (ACTV_IND,ENTITY_ID,OBJ_ID,PRNCPL_ID,PRNCPL_NM,VER_NBR)
  VALUES ('Y','5','5B1ZZB919CCA6496E0404F8189D822F2','5','ks',1);
INSERT INTO KRIM_ENTITY_ENT_TYP_T (ACTV_IND,ENTITY_ID,ENT_TYP_CD,OBJ_ID,VER_NBR)
  VALUES ('Y','5','SYSTEM','5B1B6B919CCB6VV6E040ZZ8189D822F2',1);

-- create role for system users
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','1','KS-SYS','DEPARUUUNTADMINREVIEWER00000ROLE','899','Student System User Role',1);
INSERT INTO KRIM_ROLE_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,ROLE_ID,ROLE_MBR_ID,VER_NBR) 
  VALUES ('5','P','5BAA421E4385M717E04AAF8189D821F7','899','2292',1);

-- insert kim responsibility for 'Resolve Exception' responsibility template
INSERT INTO KRIM_RSP_T (RSP_ID,NMSPC_CD,NM,ACTV_IND,RSP_TMPL_ID,VER_NBR,DESC_TXT,OBJ_ID) 
  VALUES ('1000','KS-SYS','Resolve Exception','Y','2',0,'Responsibility for Kuali Student Exception Routing','5ADFE1V2441D6320E04AAAA189D85169');
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('1000','KualiStudentDocument','13','54','5G4F09744G28EF33E0404F8189AAAF24','1000',1);
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','BC27A267EF607417E0404F8189DAA0A9','1000','63','1',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('1000','A102F3FA08CF45CFAA404FBB89D831AA',1,'A','F','*','1','Y');

-- workflow module kim permissions
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-SYS','5A4F0974494BEAA3E0404F8189D84F24','2000','3',1, 'Administer Routing for Document');
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-SYS','5B4F0974494CEF33E04AAF8189D84F24','2100','4',1, 'Blanket Approve Document');
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-SYS','5B4F0974494DEF33E0404F8189D8AA24','2200','15',1, 'Save Document');
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-SYS','5BRF0974494DEF33E0404F8189D8AA24','2300','9',1, 'Ad Hoc Review Document');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAA02E0404F8189D86F1E','3','13','7400','2000','KualiStudentDocument');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAA02E0404F8189D86F1F','3','13','7500','2100','KualiStudentDocument');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAA02E0404F8189D86F1G','3','13','7600','2200','KualiStudentDocument');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAR02E0404F8189D86F1G','5','13','7700','2300','KualiStudentDocument');
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EF5C7417E0404F8189D830AB','2000','63','750',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','638DEE6CC3F9BCD5E0404F8189D86240','2000','899','751',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EF5C7417E0404F8189D830AC','2100','63','752',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EF5C7417E0404F8189D830AD','2200','1','753',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EF5C7417E0404F8R89D830AD','2300','1','754',1);

-- rice system module kim permissions
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-SYS','5B4F0974494BEF33E0404XX189D8AA24','2400','10',1, 'Initiate Document');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAA02E0404F8189D86F1H','3','13','7800','2400','KualiStudentDocument');
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EF5C7O17E0404F8189OO30AA','2400','1','755',1);

-- kim module kim permissions
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-SYS','5B4F097X494BEF33E0404XX189D8AA24','2500','35',1, 'Assign Role');
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-SYS','5B4F09XX494BEF33E0404XX189D8AA24','2600','36',1, 'Grant Permission');
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-SYS','5B4F09744XXBEF33E0404XX189D8AA24','2700','37',1, 'Grant Responsibility');
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-SYS','5B4F0B74494BEF33XX404XX189D8AA24','2800','38',1, 'Populate Group');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAA02E0404F8189D86F1D','20','4','7000','2500','KS*');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAA02E0404F8189D86F1C','20','4','7100','2600','KS*');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAA02E0404F8189D86F1B','20','4','7200','2700','KS*');
INSERT INTO KRIM_PERM_ATTR_DATA_T (VER_NBR,OBJ_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_DATA_ID,PERM_ID,ATTR_VAL)
   VALUES (1,'5C7D9976406BAA02E0404F8189D86F1A','20','4','7300','2800','KS*');
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EF5C7417E0404F8189D830AE','2500','63','756',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EF5C7417E0404F8189D830AF','2600','63','757',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EF5C7417E0404F8189D830AG','2700','63','758',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EF5C7417E0404F8189D830AH','2800','63','759',1);

--COURSE PROPOSAL WORKFLOW--------------------------------  
  
--KIM TYPES--------------------------

--College
INSERT INTO KRIM_ATTR_DEFN_T (ACTV_IND,APPL_URL,CMPNT_NM,KIM_ATTR_DEFN_ID,NM,LBL,NMSPC_CD,OBJ_ID,VER_NBR) 
  VALUES ('Y','${application.url}','org.kuali.rice.student.bo.KualiStudentKimAttributes','101','college','College','KS-LUM','COLLEGETYPE00000000000000ATTRDEF',1);
INSERT INTO KRIM_TYP_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,SRVC_NM,VER_NBR) 
  VALUES ('Y','101','College Type','KS-LUM','COLLEGETYPE000000000000000000TYP','kimRoleTypeService',1);
INSERT INTO KRIM_TYP_ATTR_T (ACTV_IND,KIM_ATTR_DEFN_ID,KIM_TYP_ATTR_ID,KIM_TYP_ID,OBJ_ID,SORT_CD,VER_NBR) 
  VALUES ('Y','101','201','101','COLLEGETYPE00000000000000TYPATTR','a',1);
--Department
INSERT INTO KRIM_ATTR_DEFN_T (ACTV_IND,APPL_URL,CMPNT_NM,KIM_ATTR_DEFN_ID,NM,LBL,NMSPC_CD,OBJ_ID,VER_NBR) 
  VALUES ('Y','${application.url}','org.kuali.rice.student.bo.KualiStudentKimAttributes','102','department','Department','KS-LUM','DEPARTMTYPE00000000000000ATTRDEF',1);
INSERT INTO KRIM_TYP_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,SRVC_NM,VER_NBR) 
  VALUES ('Y','102','Department Type','KS-LUM','DEPARTMTYPE000000000000000000TYP','kimRoleTypeService',1);
INSERT INTO KRIM_TYP_ATTR_T (ACTV_IND,KIM_ATTR_DEFN_ID,KIM_TYP_ATTR_ID,KIM_TYP_ID,OBJ_ID,SORT_CD,VER_NBR) 
  VALUES ('Y','102','202','102','DEPARTMTYPE00000000000000TYPATTR','a',1);
--Division
INSERT INTO KRIM_ATTR_DEFN_T (ACTV_IND,APPL_URL,CMPNT_NM,KIM_ATTR_DEFN_ID,NM,LBL,NMSPC_CD,OBJ_ID,VER_NBR) 
  VALUES ('Y','${application.url}','org.kuali.rice.student.bo.KualiStudentKimAttributes','103','division','Division','KS-LUM','DIVISIONTYPE0000000000000ATTRDEF',1);
INSERT INTO KRIM_TYP_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,SRVC_NM,VER_NBR) 
  VALUES ('Y','103','Division Type','KS-LUM','DIVISIONTYPE00000000000000000TYP','kimRoleTypeService',1);
INSERT INTO KRIM_TYP_ATTR_T (ACTV_IND,KIM_ATTR_DEFN_ID,KIM_TYP_ATTR_ID,KIM_TYP_ID,OBJ_ID,SORT_CD,VER_NBR) 
  VALUES ('Y','103','203','103','DIVISIONTYPE0000000000000TYPATTR','a',1);
--KIM Types for undergrad and senate and publication....  
--INSERT INTO KRIM_TYP_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,SRVC_NM,VER_NBR) 
--  VALUES ('Y','104','Undergrad Type','KS-SYS','UNDERGRAD00000000000000000000TYP','kimRoleTypeService',1);
--INSERT INTO KRIM_TYP_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,SRVC_NM,VER_NBR) 
--  VALUES ('Y','105','Senate Type','KS-SYS', 'SENATE00000000000000000000000TYP','kimRoleTypeService',1);
--INSERT INTO KRIM_TYP_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,SRVC_NM,VER_NBR) 
--  VALUES ('Y','106','Publication Type','KS-SYS','PUBLICATION000000000000000000TYP','kimRoleTypeService',1);
  
--OrgAdmin dervied role
INSERT INTO KRIM_TYP_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,SRVC_NM,VER_NBR) 
  VALUES ('Y','107','Derived Role: Org Admin Type','KS-SYS','ORGADMINDERIVED00000000000000TYP','ksOrgAdminRoleTypeService',1);
--OrgCommittee derived role  
INSERT INTO KRIM_TYP_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,SRVC_NM,VER_NBR) 
  VALUES ('Y','108','Derived Role: Org Committee Type','KS-SYS','ORGCOMMITTEEDERIVED0000000000TYP','ksOrgCommitteeRoleTypeService',1); 

--ROLES------------------------------
--Department admin reviewer
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','102','KS-LUM','DEPARTMENTADMINREVIEWER00000ROLE','900','Department Admin Reviewer',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','103','KS-LUM','DIVISIONADMINREVIEWER0000000ROLE','901','Division Admin Reviewer',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','103','KS-LUM','DIVISIONCOMMITTEEREVIEWER000ROLE','902','Division Committee Reviewer',1);
--INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
--  VALUES ('Y','1','KS-LUM','UNDERGRADADMINREVIEWER000000ROLE','903','Undergrad Admin Reviewer',1);
--INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
--  VALUES ('Y','1','KS-LUM','UNDERGRADCOMMITTEEREVIEWER00ROLE','904','Undergrad Committee Reviewer',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','101','KS-LUM','COLLEGEADMINREVIEWER00000000ROLE','905','College Admin Reviewer',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','101','KS-LUM','COLLEGECOMMITTEEREVIEWER0000ROLE','906','College Committee Reviewer',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','1','KS-LUM','SENATEADMINREVIEWER000000000ROLE','907','Senate Admin Reviewer',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','1','KS-LUM','SENATECOMMITTEEREVIEWER00000ROLE','908','Senate Committee Reviewer',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','1','KS-LUM','PUBLICATIONREVIEWER000000000ROLE','909','Publication Reviewer',1); 
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','107','KS-LUM','OrgAdminReviewer000000000000ROLE','910','Org Admin Reviewer',1); 
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','108','KS-LUM','OrgCommitteeReviewer00000000ROLE','911','Org Committee Reviewer',1); 

INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','102','KS-LUM','DEPARTMENTCOMITREVIEWER00000ROLE','917','Department Committee Reviewer',1);

  
--RESPONSIBILITIES-------------------
--DepartmentReview-----
INSERT INTO KRIM_RSP_T (RSP_ID,NMSPC_CD,NM,ACTV_IND,RSP_TMPL_ID,VER_NBR,DESC_TXT,OBJ_ID) 
  VALUES ('300','KS-LUM','Review','Y','1',0,'Responsibility for Department Review','DepartmentReview0000000000000RSP');
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40001','CluCreditCourseParentDocument','13','7','DepartmentReview0000000RSPATTR01','300',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40002','Department Review','16','7','DepartmentReview0000000RSPATTR02','300',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40003','false','41','7','DepartmentReview0000000RSPATTR03','300',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40004','false','40','7','DepartmentReview0000000RSPATTR04','300',1);
--DepartmentReview admin kim
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','DepartmentReview0000000ROLERSP01','300','900','50001',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60001','DepartmentReview000ROLERSPACTN01',1,'A','F','*','50001','Y');
--DepartmentReview org admin kim derived
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','DepartmentReview0000000ROLERSP02','300','910','50002',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60002','DepartmentReview000ROLERSPACTN02',1,'A','F','*','50002','Y');
--DepartmentReview committee kim
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','DepartmentReview0000000ROLERSP03','300','917','50003',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60003','DepartmentReview000ROLERSPACTN03',1,'F','F','*','50003','Y');
--DepartmentReview org committee kim derived
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','DepartmentReview0000000ROLERSP04','300','911','50004',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60004','DepartmentReview000ROLERSPACTN04',1,'F','F','*','50004','Y'); 
  
  
--CollegeReview-----
INSERT INTO KRIM_RSP_T (RSP_ID,NMSPC_CD,NM,ACTV_IND,RSP_TMPL_ID,VER_NBR,DESC_TXT,OBJ_ID) 
  VALUES ('301','KS-LUM','Review','Y','1',0,'Responsibility for College Review','CollegeReview0000000000000000RSP');
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40101','CluCreditCourseParentDocument','13','7','CollegeReview0000000000RSPATTR01','301',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40102','College Review','16','7','CollegeReview0000000000RSPATTR02','301',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40103','false','41','7','CollegeReview0000000000RSPATTR03','301',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40104','false','40','7','CollegeReview0000000000RSPATTR04','301',1);
--CollegeReview admin kim
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','CollegeReview0000000000ROLERSP01','301','905','50101',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60101','CollegeReview000000ROLERSPACTN01',1,'A','F','*','50101','Y');
--CollegeReview org admin kim derived
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','CollegeReview0000000000ROLERSP02','301','910','50102',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60102','CollegeReview000000ROLERSPACTN02',1,'A','F','*','50102','Y'); 
--CollegeReview committee kim
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','CollegeReview0000000000ROLERSP03','301','906','50103',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60103','CollegeReview000000ROLERSPACTN03',1,'F','F','*','50103','Y');
--CollegeReview org committee kim derived
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','CollegeReview0000000000ROLERSP04','301','911','50104',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60104','CollegeReview000000ROLERSPACTN04',1,'F','F','*','50104','Y'); 

--UndergradReview-----
--INSERT INTO KRIM_RSP_T (RSP_ID,NMSPC_CD,NM,ACTV_IND,RSP_TMPL_ID,VER_NBR,DESC_TXT,OBJ_ID) 
--  VALUES ('302','KS-LUM','Review','Y','1',0,'Responsibility for Undergrad Affairs Review','UndergradReview00000000000000RSP');
--INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
--  VALUES ('40201','CluCreditCourseParentDocument','13','7','UndergradReview00000000RSPATTR01','302',1);
--INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
--  VALUES ('40202','Undergrad Affairs Review','16','7','UndergradReview00000000RSPATTR02','302',1);
--INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
--  VALUES ('40203','false','41','7','UndergradReview00000000RSPATTR03','302',1);
--INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
--  VALUES ('40204','false','40','7','UndergradReview00000000RSPATTR04','302',1);
--UndergradReview admin kim
--INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
--  VALUES ('Y','UndergradReview00000000ROLERSP01','302','903','50201',1);
--INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
--  VALUES ('60201','UndergradReview0000ROLERSPACTN01',1,'A','F','*','50201','Y');
--UndergradReview org admin kim derived
--INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
--  VALUES ('Y','UndergradReview00000000ROLERSP02','302','910','50202',1);
--INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
--  VALUES ('60202','UndergradReview0000ROLERSPACTN02',1,'A','F','*','50202','Y'); 
--UndergradReview committee kim
--INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
--  VALUES ('Y','UndergradReview00000000ROLERSP03','302','904','50203',1);
--INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
--  VALUES ('60203','UndergradReview0000ROLERSPACTN03',1,'F','F','*','50203','Y');
--UndergradReview org committee kim derived
--INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
--  VALUES ('Y','UndergradReview00000000ROLERSP04','302','911','50204',1);
--INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
--  VALUES ('60204','UndergradReview0000ROLERSPACTN04',1,'F','F','*','50204','Y');   

--DivisionReview-----
INSERT INTO KRIM_RSP_T (RSP_ID,NMSPC_CD,NM,ACTV_IND,RSP_TMPL_ID,VER_NBR,DESC_TXT,OBJ_ID) 
  VALUES ('303','KS-LUM','Review','Y','1',0,'Responsibility for Division Review','DivisionReview000000000000000RSP');
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40301','CluCreditCourseParentDocument','13','7','DivisionReview000000000RSPATTR01','303',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40302','Division Review','16','7','DivisionReview000000000RSPATTR02','303',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40303','false','41','7','DivisionReview000000000RSPATTR03','303',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40304','false','40','7','DivisionReview000000000RSPATTR04','303',1);
--DivisionReview admin kim
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','DivisionReview000000000ROLERSP01','303','901','50301',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60301','DivisionReview00000ROLERSPACTN01',1,'A','F','*','50301','Y');
--DivisionReview org admin kim derived
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','DivisionReview000000000ROLERSP02','303','910','50302',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60302','DivisionReview00000ROLERSPACTN02',1,'A','F','*','50302','Y'); 
--DivisionReview committee kim
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','DivisionReview000000000ROLERSP03','303','902','50303',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60303','DivisionReview00000ROLERSPACTN03',1,'F','F','*','50303','Y');
--DivisionReview org committee kim derived
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','DivisionReview000000000ROLERSP04','303','911','50304',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60304','DivisionReview00000ROLERSPACTN04',1,'F','F','*','50304','Y');

--SenateReview-----
INSERT INTO KRIM_RSP_T (RSP_ID,NMSPC_CD,NM,ACTV_IND,RSP_TMPL_ID,VER_NBR,DESC_TXT,OBJ_ID) 
  VALUES ('304','KS-LUM','Review','Y','1',0,'Responsibility for Senate Review','SenateReview00000000000000000RSP');
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40401','CluCreditCourseParentDocument','13','7','SenateReview00000000000RSPATTR01','304',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40402','Senate Review','16','7','SenateReview00000000000RSPATTR02','304',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40403','false','41','7','SenateReview00000000000RSPATTR03','304',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40404','false','40','7','SenateReview00000000000RSPATTR04','304',1);
--SenateReview admin kim
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','SenateReview00000000000ROLERSP01','304','907','50401',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60401','SenateReview0000000ROLERSPACTN01',1,'A','F','*','504301','Y');
--SenateReview org admin kim derived
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','SenateReview00000000000ROLERSP02','304','910','50402',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60402','SenateReview0000000ROLERSPACTN02',1,'A','F','*','50402','Y'); 
--SenateReview committee kim
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','SenateReview00000000000ROLERSP03','304','908','50403',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60403','SenateReview0000000ROLERSPACTN03',1,'F','F','*','50403','Y');
--SenateReview org committee kim derived
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','SenateReview00000000000ROLERSP04','304','911','50404',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60404','SenateReview0000000ROLERSPACTN04',1,'F','F','*','50404','Y');    

--PublicationReview-----
INSERT INTO KRIM_RSP_T (RSP_ID,NMSPC_CD,NM,ACTV_IND,RSP_TMPL_ID,VER_NBR,DESC_TXT,OBJ_ID) 
  VALUES ('305','KS-LUM','Review','Y','1',0,'Responsibility for Publication Review','PublicationReview000000000000RSP');
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40501','CluCreditCourseParentDocument','13','7','PublicationReview000000RSPATTR01','305',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40502','Publication Review','16','7','PublicationReview000000RSPATTR02','305',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40503','false','41','7','PublicationReview000000RSPATTR03','305',1);
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID,ATTR_VAL,KIM_ATTR_DEFN_ID,KIM_TYP_ID,OBJ_ID,RSP_ID,VER_NBR) 
  VALUES ('40504','false','40','7','PublicationReview000000RSPATTR04','305',1);
--PublicationReview admin kim
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','PublicationReview000000ROLERSP01','305','909','50501',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60501','PublicationReview00ROLERSPACTN01',1,'A','F','*','50501','Y');
--PublicationReview org admin kim derived
INSERT INTO KRIM_ROLE_RSP_T (ACTV_IND,OBJ_ID,RSP_ID,ROLE_ID,ROLE_RSP_ID,VER_NBR) 
  VALUES ('Y','PublicationReview000000ROLERSP02','305','910','50502',1);
INSERT INTO KRIM_ROLE_RSP_ACTN_T (ROLE_RSP_ACTN_ID,OBJ_ID,VER_NBR,ACTN_TYP_CD,ACTN_PLCY_CD,ROLE_MBR_ID,ROLE_RSP_ID,FRC_ACTN) 
  VALUES ('60502','PublicationReview00ROLERSPACTN02',1,'A','F','*','50502','Y');

--sample members
--INSERT INTO KRIM_ROLE_MBR_T (MBR_ID,MBR_TYP_CD,OBJ_ID,ROLE_ID,ROLE_MBR_ID,VER_NBR) 
--  VALUES ('fred','P','5BAA421E43857717E04AAF8189D821F7','901','1292',1);
--INSERT INTO KRIM_ROLE_MBR_ATTR_DATA_T (VER_NBR,OBJ_ID,ATTR_DATA_ID,KIM_TYP_ID,ROLE_MBR_ID,KIM_ATTR_DEFN_ID,ATTR_VAL) 
--  VALUES (1,'5B4AA21CV385AA17E0404AA189DAA1F7','7','101','1292','101','Arts & Hum COC');

-- create perm templates for KS
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','1','Open Document','KS-SYS','5ADF1SW6D4857954E0404F8189D85002','60',1);
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','1','Comment on Document','KS-SYS','5ADF18B6D4857954W0404F818SD85002','61',1);
INSERT INTO KRIM_PERM_TMPL_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,PERM_TMPL_ID,VER_NBR)
  VALUES ('Y','1','Edit Document','KS-SYS','5ADF18B6D4857S54EW404F8189D85002','62',1);

-- CREATE PERMISSIONS FOR KS
-- create new kim types for new KS derived roles
INSERT INTO KRIM_TYP_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,SRVC_NM,VER_NBR) 
  VALUES ('Y','109','Derived Role: KS Non-Adhoc Action Request Role Type','KS-SYS','ORGADMINDERIVED000000T0000000TYP','ksNonAdhocActionRequestDerivedRoleTypeServiceImpl',1);
-- create new kim types for new KS derived roles
INSERT INTO KRIM_TYP_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,SRVC_NM,VER_NBR) 
  VALUES ('Y','110','Derived Role: KS Route Log Role Type','KS-SYS','ORGCOMMITTEEDERIVED0000000T00TYP','ksRouteLogDerivedRoleTypeServiceImpl',1); 
-- create new kim type for KS derived role for KS Document Editor
INSERT INTO KRIM_TYP_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,SRVC_NM,VER_NBR)
  VALUES ('Y','111','Derived Role: KS Document Editor','KS-SYS','1RGCOMMITTEEDERIVED0000100T00TYP','ksEditDocumentRoleTypeService',1);
-- create new kim type for KS derived role for KS Document Commenter
INSERT INTO KRIM_TYP_T (ACTV_IND,KIM_TYP_ID,NM,NMSPC_CD,OBJ_ID,SRVC_NM,VER_NBR)
  VALUES ('Y','112','Derived Role: KS Document Commenter','KS-SYS','2RGCOMMITTEEDERIVED0000200T00TYP','ksCommentOnDocumentRoleTypeService',1);

-- add roles
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','109','KS-SYS','DEPARTMENTFDMINREVIEWER00000ROLE','912','Approve Request Recipient',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','109','KS-SYS','DEPARTMENTGDMINREVIEWER00000ROLE','913','Acknowledge Request Recipient',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','109','KS-SYS','DEPARTMENTHDMINREVIEWER00000ROLE','914','FYI Request Recipient',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','110','KS-SYS','DEPARTMENTRDMINREVIEWER00000ROLE','915','Initiator or Reviewer',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','110','KS-SYS','DEPARTMENTWDMINREVIEWER00000ROLE','916','Router',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','111','KS-SYS','1EPARTMENT5DMINREVIEWER00000ROLE','918','Permission: KS-SYS Edit Document',1);
INSERT INTO KRIM_ROLE_T (ACTV_IND,KIM_TYP_ID,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR) 
  VALUES ('Y','112','KS-SYS','2EPARTMENT6DMINREVIEWER00000ROLE','919','Permission: KS-SYS Comment on Document',1);

-- create perms for KS LUM documents
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-LUM','5B4F0974494BEF33E0R04TX189D8AA24','2900','60',1, 'Open Document');
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-LUM','5B4F0974494BEF33E0R04RX189D8AA24','3000','61',1, 'Comment on Document');
INSERT INTO KRIM_PERM_T (ACTV_IND,NMSPC_CD,OBJ_ID,PERM_ID,PERM_TMPL_ID,VER_NBR,NM)
  VALUES ('Y','KS-LUM','5B4F0974494BEF33E0R04EX189D8AA24','3100','62',1, 'Edit Document');

-- add roles to 'open document' perm (approval, fyi, ack requested, or route log reviewer, router, or initiator)
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267ET5C7O17E0404F81EEOO30AA','2900','918','760',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EF5CTO17E0404FEE89OO30AA','2900','915','761',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EF5C7OT7E040EE8189OO30AA','2900','916','762',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EF5C7O17T0EE4F8189OO30AA','2900','919','763',1);
--INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
--  VALUES ('Y','5C27A267EF5C7O1EE0T04F8189OO30AA','2900','914','764',1);

-- roles for 'comment on document' perm (approval, fyi, ack requested)
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267ET5C7OEEE0404F8189OO30AA','3000','918','765',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A267EFEE7O17T0404F8189OO30AA','3000','913','766',1);
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C27A2EEEF5C7O17E0T04F8189OO30AA','3000','914','767',1);

-- roles for 'edit document' perm (approval requested)
INSERT INTO KRIM_ROLE_PERM_T (ACTV_IND,OBJ_ID,PERM_ID,ROLE_ID,ROLE_PERM_ID,VER_NBR)
  VALUES ('Y','5C2EE267ET5C7O17E04QQF8189OO30AA','3100','912','768',1);
