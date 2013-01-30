TRUNCATE TABLE KSLU_LUTYPE DROP STORAGE
/
INSERT ALL
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Core','F7E6BBECCF2C408E813497BBA8553E5A','Program containing core requirements','kuali.lu.type.CoreProgram',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Credit Course','45F061A5111A417B8F401C0529809B5C','An course offered for academic credit','kuali.lu.type.CreditCourse',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Credit Course Format Shell','31561FFA4B2043B98BE2ACC956C9FEE8','A shell for course formats','kuali.lu.type.CreditCourseFormatShell',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Major','E82DFABF0D99439FBF970CB09A4778B6','A Major Discipline','kuali.lu.type.MajorDiscipline',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Non-Credit Course','74F7443203534261A2E9654AE6867A79','A course that is not offered for regular academic credit','kuali.lu.type.NonCreditCourse',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Req','96EAAB2D6B884F388C3DA7858A6F3EB7','Program requirements','kuali.lu.type.Requirement',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Specialization','1CEBE9BAC710481DA398C766B8EE113F','Specialization of a Major Discipline','kuali.lu.type.Variation',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Directed','279D87863C84442D927F317C29E436DE','The exchange of opinions or questions on course material, directed by the instructor.','kuali.lu.type.activity.Directed',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Discussion','2C116DCB4BE147E58E22946268DAF7F3','The exchange of opinions or questions on course material.','kuali.lu.type.activity.Discussion',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Experiential Learning/Other','8074fb30-0604-49c9-b526-3f9d1045dfbe','Individual activity in authentic non-academic setting arranged by instructor.','kuali.lu.type.activity.ExperientialLearningOROther',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Homework','B01F8C874CA3C64EE040C60AF05B7C9A','Student''s doing homework, problem sets and reading assignments and writing','kuali.lu.type.activity.Homework',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Lab','528734054E4A44E4AAE2789490A0D463','Student working on projects in a defined laboratory space.  Instructors are on-hand for students to ask questions and guidance.','kuali.lu.type.activity.Lab',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Lecture','3CB868F97F804AC0B4AE0F84174AE701','Instructor presentation of course materials.','kuali.lu.type.activity.Lecture',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Lecture or Seminar','39f83cee-7473-46ab-80cd-d5876621c5dd','Instructor presentation of course materials.','kuali.lu.type.activity.LectureORSeminar',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Tutorial','CF0872C0047A4809A3A77E813EEABDBF','Instructor or assistant walking through a learning practice.','kuali.lu.type.activity.Tutorial',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Web Discuss','B01F8C874CA2C64EE040C60AF05B7C9A','Web-based or technologically-mediated activities replacing standard discussion sections','kuali.lu.type.activity.WebDiscussion',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Web Lecture','55AE1CB7FDEE43DDAE8E4DA47767CE21','Instructor presentation of course materials via the web','kuali.lu.type.activity.WebLecture',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Baccalaureate','6A33734B7426482792951DD942080C05','A Baccalaureate','kuali.lu.type.credential.Baccalaureate',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Doctoral','57a7ddfe-24d9-4f9f-a393-edfa627337b8','A Doctoral','kuali.lu.type.credential.Doctoral',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Masters','b953a46e-c886-41c5-89b5-b9e6c6093cdc','A Masters','kuali.lu.type.credential.Masters',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Standardized Test','B01F8C874CA4C64EE040C60AF05B7C9A','Standardized Test','kuali.lu.type.standardized.test',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Standardized Test Component','B01F8C874CA5C64EE040C60AF05B7C9A','Standardized Test Component','kuali.lu.type.standardized.test.component',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Course','A8F346262C1B41B79C36BA0E27790BAB','A Shell Course','luType.shell.course',0)
  INTO KSLU_LUTYPE (EFF_DT,NAME,OBJ_ID,TYPE_DESC,TYPE_KEY,VER_NBR)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Program','0F7597C4E8DE4B6EBBCCCB8B631B81C6','A Shell Program','luType.shell.program',0)
SELECT * FROM DUAL
/
