TRUNCATE TABLE KSOR_ORG_HIRCHY DROP STORAGE
/
INSERT INTO KSOR_ORG_HIRCHY (DESCR,EFF_DT,EXPIR_DT,ID,NAME,ROOT_ORG)
  VALUES ('Hierarchy used to Manage Curriculum',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20001231000000', 'YYYYMMDDHH24MISS' ),'kuali.org.hierarchy.Curriculum','Curriculum','141')
/
INSERT INTO KSOR_ORG_HIRCHY (DESCR,EFF_DT,EXPIR_DT,ID,NAME,ROOT_ORG)
  VALUES ('Main Organizational Hierarchy',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20001231000000', 'YYYYMMDDHH24MISS' ),'kuali.org.hierarchy.Main','Main','4')
/
