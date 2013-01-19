package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.TransactionTypeForm;
import com.sigmasys.kuali.ksa.krad.model.TransactionTypeModel;
import com.sigmasys.kuali.ksa.krad.util.CreditDebitKeyValuesFinder;
import com.sigmasys.kuali.ksa.krad.util.KradTypeEntityKeyValuesFinder;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AuditableEntityService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.kim.impl.identity.name.EntityNameTypeBo;
import org.kuali.rice.krad.keyvalues.KeyValuesFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by: dmulderink on 10/10/12 at 8:06 PM
 */
@Controller
@RequestMapping(value = "/transactionTypeView")
public class TransactionTypeController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(TransactionTypeController.class);
    private volatile KeyValuesFinder creditDebitTypeOptionsFinder;

    @Autowired
    private AuditableEntityService auditableEntityService;

    @Autowired
    private TransactionService transactionService;

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected TransactionTypeForm createInitialForm(HttpServletRequest request) {
        TransactionTypeForm form = new TransactionTypeForm();
        form.setStatusMessage("");
        return form;
    }

    /**
     * @param form
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
    public ModelAndView get(@ModelAttribute("KualiForm") TransactionTypeForm form, HttpServletRequest request) {

        // do get stuff...
        String viewId = request.getParameter("viewId");
        String pageId = request.getParameter("pageId");

        logger.info("View: " + viewId + " Page: " + pageId);

        if ("TransactionTypePage".equals(pageId)) {
            List<TransactionType> entities = auditableEntityService.getAuditableEntities(TransactionType.class);
            logger.info("Transaction Type Count: " + entities.size());
            form.setTransactionTypes(entities);
        } else if ("TransactionTypeDetailsPage".equals(pageId)) {
            String entityId = request.getParameter("entityId");
            String subCode = request.getParameter("subCode");

            if (entityId == null || entityId.trim().isEmpty()) {
                throw new IllegalArgumentException("'entityId' request parameter must be specified");
            }

            if(subCode == null){ subCode = "1"; }

            TransactionTypeId ttId = new TransactionTypeId(entityId, Integer.parseInt(subCode));

            TransactionType tt = transactionService.getTransactionType(ttId);
            logger.info("Retrieved Transaction Type: " + tt.getDescription());

            form.setTransactionType(tt);
        }

        return getUIFModelAndView(form);
    }


    /**
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=create")
    public ModelAndView create(@ModelAttribute("KualiForm") TransactionTypeForm form) {

        form.reset();
        List<GeneralLedgerType> gltypes = auditableEntityService.getAuditableEntities(GeneralLedgerType.class);

        List<GlBreakdown> breakdowns = new ArrayList<GlBreakdown>(gltypes.size());

        for(GeneralLedgerType type : gltypes){
            GlBreakdown b = new GlBreakdown();
            b.setGeneralLedgerType(type);
            b.setGlOperation(type.getGlOperationOnCharge());
            b.setBreakdown(new BigDecimal(0.0));
            breakdowns.add(b);
        }

        form.setGlBreakdowns(breakdowns);

        form.setCreditDebitKeyValuesFinder(this.getCreditDebitTypeOptionsFinder());

        return getUIFModelAndView(form);
    }

        /**
        * @param form
        * @return
        */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=insert")
    public ModelAndView insert(@ModelAttribute("KualiForm") TransactionTypeForm form) {

        TransactionType transType = null;



        String type = form.getType();
        String code = form.getCode();
        Date startDate = form.getStartDate();
        Integer priority = form.getPriority();
        String description = form.getDescription();

        boolean typeExists = transactionService.transactionTypeExists(type);

        TransactionType tt = null;

        if("C".equalsIgnoreCase(type)){
            if(!typeExists){
                tt = transactionService.createCreditType(code, null, startDate, priority, description);
            } else {
                tt = transactionService.createCreditSubType(code, startDate);
            }

        } else if("D".equalsIgnoreCase(type)){
            if(!typeExists){
                tt = transactionService.createDebitType(code, null, startDate, priority, description);
            } else {
                tt = transactionService.createDebitSubType(code, startDate);
            }

        } else {
            String errMsg = "Invalid transaction type '" + type + "'";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        logger.info("Transaction Type saved: " + tt.getId());




        /*
        // Common fields
        String code = form.getCode();





        logger.debug("Entity code: " + entity.getCode());

        try {

            String code = entity.getCode();
            String name = entity.getName();
            String description = entity.getDescription();

            auditableEntityService.createAuditableEntity(code, name, description, TransactionType.class);

            List<TransactionType> entities = auditableEntityService.getAuditableEntities(TransactionType.class);

            form.setTransactionTypes(entities);

            // success in creating the currency.
            String statusMsg = "Success: Transaction Type saved, ID = " + entity.getId();
            form.setStatusMessage(statusMsg);
            logger.info(statusMsg);
        } catch (Exception e) {
            // failed to create the currency. Leave the currency information in the view
            String statusMsg = "Failure: Transaction Type entity did not save, ID = " + entity.getId() +
                    ". " + e.getMessage();
            form.setStatusMessage(statusMsg);
            logger.error(statusMsg);
        }
          */
        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=update")
    public <T extends AuditableEntity> ModelAndView update(@ModelAttribute("KualiForm") TransactionTypeForm form) {

        TransactionTypeModel entity = form.getTransactionType();

        if (entity == null) {
            String errMsg = "TransactionType cannot be null";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }

        logger.debug("Entity code: " + entity.getCode());

        try {
            // occurs in the detail page.
            auditableEntityService.persistAuditableEntity(entity);
            // success in updating the currency.
            String statusMsg = "Success: Transaction Type entity updated. Entity ID =  " + entity.getId();
            form.setStatusMessage(statusMsg);
            logger.info(statusMsg);
        } catch (Exception e) {
            // failed to update the currency. Leave the currency information in the view
            String statusMsg = "Failure: Transaction Type entity did not update, Entity ID =  " + entity.getId() + ". " + e.getMessage();
            form.setStatusMessage(statusMsg);
            logger.error(statusMsg);
        }

        return getUIFModelAndView(form);
    }


    public KeyValuesFinder getCreditDebitTypeOptionsFinder() {
        if (creditDebitTypeOptionsFinder == null) {
            synchronized(this) {
                if (creditDebitTypeOptionsFinder == null) {
                    creditDebitTypeOptionsFinder = new CreditDebitKeyValuesFinder();
                }
            }
        }

        return creditDebitTypeOptionsFinder;
    }


}