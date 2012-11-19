package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.krad.model.FlagModel;
import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Flag;

import java.util.Date;
import java.util.List;

/**
 * Created by: dmulderink on 10/6/12 at 2:28 PM
 */
public class FlagForm extends AbstractViewModel {


   private Account account;

   private Date fromDate;

   private Date toDate;

   private List<FlagModel> flagModels;

   private FlagModel flagModel;

   private String statusMessage;

   // resuable add edit or followup instructional text
   private String aeInstructionalText;

   /*
     Get/Set methods
   */

   public Account getAccount() {
      return account;
   }

   public void setAccount(Account account) {
      this.account = account;
   }

   public Date getFromDate() {
      return fromDate;
   }

   public void setFromDate(Date fromDate) {
      this.fromDate = fromDate;
   }

   public Date getToDate() {
      return toDate;
   }

   public void setToDate(Date toDate) {
      this.toDate = toDate;
   }

/*   public List<Flag> getFlags() {
      return flags;
   }

   public void setFlags(List<Flag> flags) {
      this.flags = flags;
   }

   public Flag getFlag() {
      return flag;
   }

   public void setFlag(Flag flag) {
      this.flag = flag;
   }*/

   public String getStatusMessage() {
      return statusMessage;
   }

   public void setStatusMessage(String statusMessage) {
      this.statusMessage = statusMessage;
   }

   public String getAeInstructionalText() {
      return aeInstructionalText;
   }

   public void setAeInstructionalText(String aeInstructionalText) {
      this.aeInstructionalText = aeInstructionalText;
   }

   public FlagModel getFlagModel() {
      return flagModel;
   }

   public void setFlagModel(FlagModel flagModel) {
      this.flagModel = flagModel;
   }

   public List<FlagModel> getFlagModels() {
      return flagModels;
   }

   public void setFlagModels(List<FlagModel> flagModels) {
      this.flagModels = flagModels;
   }
}
