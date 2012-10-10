package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.KsaMemoForm;
import com.sigmasys.kuali.ksa.krad.model.MemoModel;
import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Memo;
import com.sigmasys.kuali.ksa.service.InformationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: dmulderink on 10/4/12 at 7:53 AM
 */
@Controller
@RequestMapping(value = "/ksaMemoVw")
public class KsaMemoController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(KsaMemoController.class);

    @Autowired
    private InformationService informationService;

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected KsaMemoForm createInitialForm(HttpServletRequest request) {
        KsaMemoForm form = new KsaMemoForm();
        String userId = request.getParameter("userId");

        if (userId != null) {

            Account account = accountService.getFullAccount(userId);

            if (account == null) {
                String errMsg = "Cannot find Account by ID = " + userId;
                logger.error(errMsg);
                throw new IllegalStateException(errMsg);
            }

            form.setAccount(account);
        }/* else {
          String errMsg = "'userId' request parameter cannot be null";
          logger.error(errMsg);
          throw new IllegalStateException(errMsg);
       }*/

        return form;
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
    public ModelAndView get(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {
        // do get stuff...

       String viewId = request.getParameter("viewId");
       String pageId = request.getParameter("pageId");
       // example user1
       String userId = request.getParameter("userId");
       // a record index from a table selection or a known memoId
       String memoId = request.getParameter("memoId");

       logger.info("View: " + viewId + " User: " + userId + " Memo ID: " + memoId);

        if (pageId != null && pageId.equals("MemosPage")) {

            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("'userId' request parameter must be specified");
            }

            List<Memo> memoList = informationService.getMemos(userId);
            List<MemoModel> memoModelList = new ArrayList<MemoModel>();

            for (Memo memo : memoList) {
               MemoModel memoModel = new MemoModel(memo);
               memoModelList.add(memoModel);
            }

            form.setMemoModels(memoModelList);

        } else if (pageId != null && pageId.equals("AddMemoPage")) {
           if (userId == null || userId.isEmpty()) {
              throw new IllegalArgumentException("'userId' request parameter must be specified");
           }

           // create a Memo, set defaults for the view
           Account account = accountService.getFullAccount(userId);
           String accountId = account.getId();
           // don't create a persistent memo when adding a memo, just initialize one for use
           // rather persist when the user submits (button control) an insert on the memo
           Memo memo = new Memo();
           memo.setAccount(account);
           memo.setAccountId(accountId);
           memo.setEffectiveDate(new Date());
           memo.setText("");
           memo.setAccessLevel(0);

           MemoModel memoModel = new MemoModel(memo);
           form.setMemoModel(memoModel);

           form.setAefInstructionalText("Add a memo");

        } else if (pageId != null && pageId.equals("ViewMemoPage")) {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("'userId' request parameter must be specified");
            }

            try {
               Memo memo = informationService.getMemo(new Long(memoId));
               if (memo != null) {
                  form.setMemoModel(new MemoModel(memo));
               }
            } catch (Exception exp) {
               String errMsg = "'memoId' request parameter cannot be null";
               logger.error(errMsg);
            }

            form.setAefInstructionalText("View a memo");

        } else if (pageId != null && pageId.equals("EditMemoPage")) {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("'userId' request parameter must be specified");
            }

            try {
               Memo memo = informationService.getMemo(new Long(memoId));
               if (memo != null) {
                  form.setMemoModel(new MemoModel(memo));
               }
            } catch (Exception exp) {
               String errMsg = "'memoId' request parameter cannot be null";
               logger.error(errMsg);
            }

            form.setAefInstructionalText("Edit a memo");

        } else if (pageId != null && pageId.equals("FollowUpMemoPage")) {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("'userId' request parameter must be specified");
            }

           try {
              Memo memo = informationService.getMemo(new Long(memoId));
              if (memo != null) {
                 form.setMemoModel(new MemoModel(memo));
              }
           } catch (Exception exp) {
              String errMsg = "'memoId' request parameter cannot be null";
              logger.error(errMsg);
           }

           form.setAefInstructionalText("Follow-up a memo");
        }

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=submit")
    @Transactional(readOnly = false)
    public ModelAndView submit(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        // do submit stuff...

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
    @Transactional(readOnly = false)
    public ModelAndView save(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {

        // do save stuff...

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=cancel")
    public ModelAndView cancel(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        // do cancel stuff...
        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        // do refresh stuff...
        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=insertMemo")
    public ModelAndView insertMemo(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        // do insert stuff...

       String viewId = request.getParameter("viewId");
       // example user1
       String userId = request.getParameter("userId");
       String userIdap = request.getParameter("actionParameters[userId]");

       logger.info("View: " + viewId + " User: " + userId);

        // TODO validate the field entries before inserting

        MemoModel memoModel = form.getMemoModel();

        String accountId = memoModel.getAccountId();
        String memoText = memoModel.getText();
        String memoAccessLevel = memoModel.getInfoAccessLevel();
        int accessLevel = Integer.parseInt(memoAccessLevel);
        Date effectiveDate = memoModel.getEffectiveDate();
        Date expirationDate = memoModel.getExpirationDate();
        Memo previousMemo = memoModel.getPreviousMemo();
        Long previousMemoId = previousMemo != null ? previousMemo.getId() : null;

        try {

          Memo memo = informationService.createMemo(accountId, memoText, accessLevel, effectiveDate, expirationDate, previousMemoId);

           Long persistResult = informationService.persistInformation(memo);

           if (persistResult >= 0) {
              form.setStatusMessage("Success");
              logger.info("Successful insert of memo number " + memo.getId().toString());
           } else {
              String failedMsg = "Failed to add memo. result code: " + persistResult.toString();
              form.setStatusMessage(failedMsg);

              form.setStatusMessage(failedMsg);
           }
        } catch(Exception exp) {
           String errMsg = "'Failed to add memo. " + exp.getLocalizedMessage();
           logger.error(errMsg);
        }

       return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=updateMemo")
    public ModelAndView updateMemo(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        // do updateMemo stuff...

       String viewId = request.getParameter("viewId");
       // example user1
       String userId = request.getParameter("actionParameters[userId]");
       // a record index from a table selection or a known memoId
       String memoId = request.getParameter("actionParameters[memoId]");

       logger.info("View: " + viewId + " User: " + userId + " Memo ID: " + memoId);

       this.updateMemo(form, memoId);

       return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=followUpMemo")
    public ModelAndView followUpMemo(@ModelAttribute("KualiForm") KsaMemoForm form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        // do follow-up stuff...

       String viewId = request.getParameter("viewId");
       // example user1
       String userId = request.getParameter("actionParameters[userId]");
       // a record index from a table selection or a known memoId
       String memoId = request.getParameter("actionParameters[memoId]");

       logger.info("View: " + viewId + " User: " + userId + " Memo ID: " + memoId);

       this.updateMemo(form, memoId);

       return getUIFModelAndView(form);
    }

   private void updateMemo(KsaMemoForm form, String memoId) {

      Memo memo = null;
      try {
         memo = informationService.getMemo(new Long(memoId));

         if (memo != null) {
            MemoModel memoModel = form.getMemoModel();
            String memoAccessLevel = memoModel.getInfoAccessLevel();
            int accessLevel = Integer.parseInt(memoAccessLevel);
            memo.setEffectiveDate(memoModel.getEffectiveDate());
            memo.setExpirationDate(memoModel.getExpirationDate());
            memo.setText(memoModel.getText());
            memo.setAccessLevel(accessLevel);

            Long persistResult = informationService.persistInformation(memo);

            if (persistResult >= 0) {
               form.setStatusMessage("Success");
               logger.info("Successful update of memo number " + memoId);
            } else {
               String failedMsg = "Failed to update memo number " + memoId + " result code: " + persistResult.toString();
               form.setStatusMessage(failedMsg);
               logger.error(failedMsg);
            }
         }
      } catch (Exception exp) {
         String errMsg = "'memoId' request parameter cannot be null";
         logger.error(errMsg);
      }

      // refresh the view page section
      memo = informationService.getMemo(new Long(memoId));
      if (memo != null) {
         form.setMemoModel(new MemoModel(memo));
      }
   }
}
