INSERT INTO KREW_DOC_TYP_T (DOC_TYP_ID,PARNT_ID,DOC_TYP_NM,DOC_TYP_VER_NBR,ACTV_IND,CUR_IND,DOC_TYP_DESC,LBL,PREV_DOC_TYP_VER_NBR,DOC_HDR_ID,DOC_HDLR_URL,HELP_DEF_URL,DOC_SEARCH_HELP_URL,POST_PRCSR,GRP_ID,BLNKT_APPR_GRP_ID,BLNKT_APPR_PLCY,RPT_GRP_ID,RTE_VER_NBR,NOTIFY_ADDR,SEC_XML,EMAIL_XSL,APPL_ID,OBJ_ID,VER_NBR)
    VALUES (KREW_DOC_HDR_S.NEXTVAL,(SELECT KREW_DOC_TYP_T.DOC_TYP_ID FROM KREW_DOC_TYP_T WHERE KREW_DOC_TYP_T.DOC_TYP_NM='RiceDocument'),'FormatOfferingInfoMaintenanceDocument',0,1,1,'Create a New Format Offering Maintenance Document','org.kuali.student.r2.lum.course.infc.Format Offering Info Maintenance Document',NULL,'','${application.url}/kr-krad/maintenance?methodToCall=docHandler&dataObjectClassName=org.kuali.student.enrollment.class2.courseoffering.dto.FormatOfferingInfo','','','org.kuali.rice.krad.workflow.postprocessor.KualiPostProcessor','1','1','','','2','','','','',SYS_GUID(),1)
/

INSERT INTO KREW_RTE_NODE_T (RTE_NODE_ID,DOC_TYP_ID,NM,TYP,RTE_MTHD_NM,FNL_APRVR_IND,MNDTRY_RTE_IND,GRP_ID,RTE_MTHD_CD,ACTVN_TYP,BRCH_PROTO_ID,NEXT_DOC_STAT,VER_NBR)
    VALUES (KREW_RTE_NODE_S.NEXTVAL,(SELECT KREW_DOC_TYP_T.DOC_TYP_ID FROM KREW_DOC_TYP_T WHERE KREW_DOC_TYP_T.DOC_TYP_NM='FormatOfferingInfoMaintenanceDocument'),'Initiated','org.kuali.rice.kew.engine.node.InitialNode','',0,0,'1','','P','','','1')
/

INSERT INTO KREW_RTE_NODE_CFG_PARM_T (RTE_NODE_CFG_PARM_ID,RTE_NODE_ID,KEY_CD,VAL) VALUES (KREW_RTE_NODE_CFG_PARM_S.NEXTVAL,KREW_RTE_NODE_S.CURRVAL,'contentFragment','<start name="Initiated">
<activationType>P</activationType>
<mandatoryRoute>false</mandatoryRoute>
<finalApproval>false</finalApproval>
</start>
')
/

INSERT INTO KREW_RTE_NODE_CFG_PARM_T (RTE_NODE_CFG_PARM_ID,RTE_NODE_ID,KEY_CD,VAL) VALUES (KREW_RTE_NODE_CFG_PARM_S.NEXTVAL,KREW_RTE_NODE_S.CURRVAL,'activationType','P')
/

INSERT INTO KREW_RTE_NODE_CFG_PARM_T (RTE_NODE_CFG_PARM_ID,RTE_NODE_ID,KEY_CD,VAL) VALUES (KREW_RTE_NODE_CFG_PARM_S.NEXTVAL,KREW_RTE_NODE_S.CURRVAL,'mandatoryRoute',0)
/

INSERT INTO KREW_RTE_NODE_CFG_PARM_T (RTE_NODE_CFG_PARM_ID,RTE_NODE_ID,KEY_CD,VAL) VALUES (KREW_RTE_NODE_CFG_PARM_S.NEXTVAL,KREW_RTE_NODE_S.CURRVAL,'finalApproval',0)
/

INSERT INTO KREW_RTE_NODE_CFG_PARM_T (RTE_NODE_CFG_PARM_ID,RTE_NODE_ID,KEY_CD,VAL) VALUES (KREW_RTE_NODE_CFG_PARM_S.NEXTVAL,KREW_RTE_NODE_S.CURRVAL,'ruleSelector','Template')
/

INSERT INTO KREW_DOC_TYP_PROC_T (DOC_TYP_PROC_ID,DOC_TYP_ID,INIT_RTE_NODE_ID,NM,INIT_IND,VER_NBR) VALUES (KREW_DOC_HDR_S.NEXTVAL,(SELECT KREW_DOC_TYP_T.DOC_TYP_ID FROM KREW_DOC_TYP_T WHERE KREW_DOC_TYP_T.DOC_TYP_NM='FormatOfferingInfoMaintenanceDocument'),KREW_RTE_NODE_S.CURRVAL,'PRIMARY',1,'1')
/
