package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.config.ConfigService;
import com.sigmasys.kuali.ksa.krad.form.KsaQuickViewForm;
import com.sigmasys.kuali.ksa.krad.model.MemoModel;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import com.sigmasys.kuali.ksa.service.InformationService;
import com.sigmasys.kuali.ksa.util.TransactionListUtils;
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
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by: dmulderink on 9/28/12 at 2:25 PM
 */
@Controller
@RequestMapping(value = "/ksaQuickVw")
public class KsaQuickViewController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(KsaQuickViewController.class);

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private InformationService informationService;

    @Autowired
    protected ConfigService configService;


    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected KsaQuickViewForm createInitialForm(HttpServletRequest request) {
        KsaQuickViewForm form = new KsaQuickViewForm();
        String userId = request.getParameter("userId");

        if (userId != null) {

            Account account = accountService.getFullAccount(userId);

            if (account == null) {
                String errMsg = "Cannot find Account by ID = " + userId;
                logger.error(errMsg);
                throw new IllegalStateException(errMsg);
            }

             form.setAccount(account);
        } /*else {
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
    public ModelAndView get(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

        String viewId = request.getParameter("viewId");
        String pageId = request.getParameter("pageId");
        String userId = request.getParameter("userId");

        logger.info("View: " + viewId + " User: " + userId);

        if ("KsaQuickView".equals(viewId)) {
            if (!accountService.accountExists(userId)) {
                throw new IllegalArgumentException("Unknown account for userid '" + userId + "'");
            }

            if (pageId == null) {
               populateForm(userId, form);
            }  else if (pageId != null && pageId.equals("QuickViewAddMemoPage")) {
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

            }
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
    public ModelAndView submit(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
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
    public ModelAndView save(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
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
    public ModelAndView cancel(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
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
    public ModelAndView refresh(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {

        String viewId = request.getParameter("viewId");
        String userId = request.getParameter("actionParameters[userId]");

        logger.info("View: " + viewId + " User: " + userId);

        if ("KsaQuickView".equals(viewId)) {
            if (!accountService.accountExists(userId)) {
                throw new IllegalArgumentException("Unknown account for userid '" + userId + "'");
            }

            populateForm(userId, form);

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
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=ageDebt")
    public ModelAndView ageDebt(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {

        // do aging of transactions stuff...
        String accountId = form.getAccount().getId();
        boolean ignoreDeferment = Boolean.parseBoolean(form.getIgnoreDeferment());

        if (accountId != null && !accountId.trim().isEmpty()) {
            // age the indexed Account Transactions
            ChargeableAccount chargeableAccount = accountService.ageDebt(accountId, ignoreDeferment);
            // populate the form using the id
            populateForm(accountId, form);
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
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=insertMemo")
    public ModelAndView insertMemo(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        // do insert stuff...

       String viewId = request.getParameter("viewId");
       // example user1
       String userId = request.getParameter("actionParameters[userId]");

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
     * Populate the form per business needs for a single account by the account identifier
     *
     * @param userId
     * @param form
     */
    private void populateForm(String userId, KsaQuickViewForm form) {

        // store the selected account ID
        //form.setSelectedId(id);

        boolean ignoreDeferment = Boolean.parseBoolean(form.getIgnoreDeferment());

        Account accountById = accountService.getFullAccount(userId);
        if (accountById == null) {
            throw new IllegalStateException("Cannot find Account by ID = " + userId);
        }

        form.setAccount(accountById);

        ChargeableAccount chargeableAccount = (ChargeableAccount) accountById;

        List<Account> accountList = new ArrayList<Account>();
        accountList.add(accountById);

        // no session scope
        //form.setStudentLookupByName(accountById.getDefaultPersonName().getLastName());
        // a list of one
        //form.setAccountBrowseList(accountList);
        form.setCompositeDefaultPersonName(accountById.getCompositeDefaultPersonName());
        form.setCompositeDefaultPostalAddress(accountById.getCompositeDefaultPostalAddress());

        BigDecimal pastDue = accountService.getOutstandingBalance(userId, ignoreDeferment) != null ? accountService.getOutstandingBalance(userId, ignoreDeferment) : BigDecimal.ZERO;
        BigDecimal balance = accountService.getDueBalance(userId, ignoreDeferment) != null ? accountService.getDueBalance(userId, ignoreDeferment) : BigDecimal.ZERO;
        BigDecimal future = accountService.getUnallocatedBalance(userId) != null ? accountService.getUnallocatedBalance(userId) : BigDecimal.ZERO;
        BigDecimal deferment = accountService.getDeferredAmount(userId) != null ? accountService.getDeferredAmount(userId) : BigDecimal.ZERO;

        // Aging

        Date lastAgeDate = chargeableAccount.getLateLastUpdate();
        form.setLastAgeDate(lastAgeDate);

        Currency currency = currencyService.getCurrency("USD");
        form.setCurrency(currency);

        LatePeriod latePeriod = chargeableAccount.getLatePeriod();

        form.setDaysLate1(latePeriod.getDaysLate1() != null ? latePeriod.getDaysLate1().toString() : "30");
        form.setDaysLate2(latePeriod.getDaysLate2() != null ? latePeriod.getDaysLate2().toString() : "60");
        form.setDaysLate3(latePeriod.getDaysLate3() != null ? latePeriod.getDaysLate3().toString() : "90");

        // getOutstandingBalance above always ignores deferment per Paul setting the pastDue to the ageTotal
        pastDue = BigDecimal.ZERO;
        if (chargeableAccount.getAmountLate1() != null) {
           form.setAged30(chargeableAccount.getAmountLate1().toString());
           pastDue = pastDue.add(chargeableAccount.getAmountLate1());
        } else {
           form.setAged30(BigDecimal.ZERO.toString());
        }
        if (chargeableAccount.getAmountLate2() != null) {
          form.setAged60(chargeableAccount.getAmountLate2().toString());
           pastDue = pastDue.add(chargeableAccount.getAmountLate2());
        } else {
           form.setAged60(BigDecimal.ZERO.toString());
        }
        if (chargeableAccount.getAmountLate3() != null) {
          form.setAged90(chargeableAccount.getAmountLate3().toString());
           pastDue = pastDue.add(chargeableAccount.getAmountLate3());
        } else {
           form.setAged90(BigDecimal.ZERO.toString());
        }

        BigDecimal agedTotal = BigDecimal.ZERO;

        if (chargeableAccount.getAmountLate1() != null &&
                chargeableAccount.getAmountLate2() != null &&
                chargeableAccount.getAmountLate3() != null) {
            agedTotal = agedTotal.add(chargeableAccount.getAmountLate1());
            agedTotal = agedTotal.add(chargeableAccount.getAmountLate2());
            agedTotal = agedTotal.add(chargeableAccount.getAmountLate3());
        }

        form.setAgedTotal(agedTotal.toString());

        form.setPastDueAmount(pastDue.toString());
        form.setBalanceAmount(balance.toString());
        form.setFutureAmount(future.toString());
        if (deferment != null) {
           form.setDefermentAmount(deferment.toString());
        }

        String sizeStr = configService.getInitialParameter(Constants.QUICKVIEW_INFORMATION_COUNT);
        Integer size = null;
        try{
            size = new Integer(sizeStr);
        } catch(NumberFormatException e){}
        if(size == null){
            size = new Integer(4);
        }


        List<Alert> alertsAll = informationService.getAlerts(userId);
        AlertDateComparatorAscending comparitor1 = new AlertDateComparatorAscending();
        Collections.sort(alertsAll, comparitor1);

        List<Alert> alerts = new ArrayList<Alert>();
        for (int i=0; i < size.intValue() && i < alertsAll.size(); i++) {
            alerts.add(alertsAll.get(i));
        }

        form.setAlerts(alerts);

        List<Flag> flagAll = informationService.getFlags(userId);
        FlagDateComparatorAscending comparitor2 = new FlagDateComparatorAscending();
        Collections.sort(flagAll, comparitor2);

        List<Flag> flags = new ArrayList<Flag>();
        for (int i=0; i < size.intValue() && i < flagAll.size(); i++) {
            flags.add(flagAll.get(i));
        }

        form.setFlags(flags);


        List<Memo> memoList = informationService.getMemos(userId);
        List<MemoModel> memoModelList = new ArrayList<MemoModel>();

        MemoDateComparatorAscending comparator3 = new MemoDateComparatorAscending();
        Collections.sort(memoList, comparator3);

        for (int i=0; i < size.intValue() && i < memoList.size(); i++) {
           Memo memo = memoList.get(i);
           MemoModel memoModel = new MemoModel(memo);
           memoModelList.add(memoModel);
        }

        form.setMemoModels(memoModelList);
    }
}

class MemoDateComparatorAscending implements Comparator<Memo> {
    @Override
    public int compare(Memo m1, Memo m2) {
        Date d1 = m1.getEffectiveDate();
        Date d2 = m2.getEffectiveDate();
        if(d1 == null && d2 == null){ return 0; }
        if(d1 == null){ return 1; }
        if(d2 == null){ return -1; }
        return d1.compareTo(d2);
    }
}

class AlertDateComparatorAscending implements Comparator<Alert> {
    @Override
    public int compare(Alert a1, Alert a2) {
        Date d1 = a1.getEffectiveDate();
        Date d2 = a2.getEffectiveDate();
        if(d1 == null && d2 == null){ return 0; }
        if(d1 == null){ return 1; }
        if(d2 == null){ return -1; }
        return d1.compareTo(d2);
    }
}
class FlagDateComparatorAscending implements Comparator<Flag> {
    @Override
    public int compare(Flag f1, Flag f2) {
        Date d1 = f1.getEffectiveDate();
        Date d2 = f2.getEffectiveDate();
        if(d1 == null && d2 == null){ return 0; }
        if(d1 == null){ return 1; }
        if(d2 == null){ return -1; }
        return d1.compareTo(d2);
    }
}