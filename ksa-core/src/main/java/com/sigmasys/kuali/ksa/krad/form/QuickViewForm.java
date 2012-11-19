package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.krad.model.MemoModel;
import com.sigmasys.kuali.ksa.model.*;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by: dmulderink on 9/28/12 at 2:25 PM
 */
public class QuickViewForm extends AbstractViewModel {


   private Account account;

   private Currency currency;

   // Account Overview

   private String pastDueAmount;

   private String balanceAmount;

   private String futureAmount;

   private String defermentAmount;

   // Biographic Information

   private String compositeDefaultPersonName;

   private String compositeDefaultPostalAddress;

   // Alert

   private List<Alert> alerts;

   // Flag

   private List<Flag> flags;

   // Aging

   // the last aging date
   private Date lastAgeDate;

   // sum of aged values
   private String agedTotal;

   // amountLate1
   private String aged30;

   // amountLate2
   private String aged60;

   // amountLate3
   private String aged90;

   // Aged debit flag
   private String ignoreDeferment;

   private String daysLate1;

   private String daysLate2;

   private String daysLate3;

   // Memos

   private List<MemoModel> memoModels;

   private MemoModel memoModel;

   private String statusMessage;

   /*
      Get / Set methods
    */

   public Account getAccount() {
      return account;
   }

   public void setAccount(Account account) {
      this.account = account;
   }

   public String getPastDueAmount() {
      return this.getFormattedUSDAmount(pastDueAmount);
   }

   public void setPastDueAmount(String pastDueAmount) {
      this.pastDueAmount = pastDueAmount;
   }

   public String getBalanceAmount() {
      return this.getFormattedUSDAmount(balanceAmount);
   }

   public void setBalanceAmount(String balanceAmount) {
      this.balanceAmount = balanceAmount;
   }

   public String getFutureAmount() {
      return this.getFormattedUSDAmount(futureAmount);
   }

   public void setFutureAmount(String futureAmount) {
      this.futureAmount = futureAmount;
   }

   public String getDefermentAmount() {
      return this.getFormattedUSDAmount(defermentAmount);
   }

   public void setDefermentAmount(String defermentAmount) {
      this.defermentAmount = defermentAmount;
   }


   public String getCompositeDefaultPersonName() {
      return compositeDefaultPersonName;
   }

   public void setCompositeDefaultPersonName(String compositeDefaultPersonName) {
      this.compositeDefaultPersonName = compositeDefaultPersonName;
   }

   public String getCompositeDefaultPostalAddress() {
      return compositeDefaultPostalAddress;
   }

   public void setCompositeDefaultPostalAddress(String compositeDefaultPostalAddress) {
      this.compositeDefaultPostalAddress = compositeDefaultPostalAddress;
   }


   public List<Alert> getAlerts() {
      return alerts;
   }

   public void setAlerts(List<Alert> alerts) {
      this.alerts = alerts;
   }


   public List<Flag> getFlags() {
      return flags;
   }

   public void setFlags(List<Flag> flags) {
      this.flags = flags;
   }


   public Date getLastAgeDate() {
      return lastAgeDate;
   }

   public void setLastAgeDate(Date lastAgeDate) {
      this.lastAgeDate = lastAgeDate;
   }

   public String getAgedTotal() {
      return this.getFormattedUSDAmount(agedTotal);
   }

   public void setAgedTotal(String agedTotal) {
      this.agedTotal = agedTotal;
   }

   public String getAged30() {
      return this.getFormattedUSDAmount(aged30);
   }

   public void setAged30(String aged30) {
      this.aged30 = aged30;
   }

   public String getAged60() {
      return this.getFormattedUSDAmount(aged60);
   }

   public void setAged60(String aged60) {
      this.aged60 = aged60;
   }

   public String getAged90() {
      return this.getFormattedUSDAmount(aged90);
   }

   public void setAged90(String aged90) {
      this.aged90 = aged90;
   }


   public String getIgnoreDeferment() {
      return ignoreDeferment;
   }

   public void setIgnoreDeferment(String ignoreDeferment) {
      this.ignoreDeferment = ignoreDeferment;
   }

   public List<MemoModel> getMemoModels() {
      return memoModels;
   }

   public void setMemoModels(List<MemoModel> memoModels) {
      this.memoModels = memoModels;
   }

   public MemoModel getMemoModel() {
      return memoModel;
   }

   public void setMemoModel(MemoModel memoModel) {
      this.memoModel = memoModel;
   }


   public String getDaysLate1() {
      return daysLate1;
   }

   public void setDaysLate1(String daysLate1) {
      this.daysLate1 = daysLate1;
   }

   public String getDaysLate2() {
      return daysLate2;
   }

   public void setDaysLate2(String daysLate2) {
      this.daysLate2 = daysLate2;
   }

   public String getDaysLate3() {
      return daysLate3;
   }

   public void setDaysLate3(String daysLate3) {
      this.daysLate3 = daysLate3;
   }

   public Currency getCurrency() {
      return currency;
   }

   public void setCurrency(Currency currency) {
      this.currency = currency;
   }

   public String getFormattedAmount(String amount) {

      String formattedNumber = "";

      if (getCurrency() != null && amount != null) {
         NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
         numberFormat.setCurrency(java.util.Currency.getInstance(getCurrency().getCode()));
         double doubleAmount = Double.parseDouble(amount);
         formattedNumber = numberFormat.format(doubleAmount);
      }
      return formattedNumber;
   }

   public String getFormattedUSDAmount(String value) {

      String formattedNumber = "";

      if (value != null) {
         NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
         double doubleValue = Double.parseDouble(value);
         String usdCurrency = numberFormat.format(doubleValue);
         formattedNumber = usdCurrency.substring(1);
      }
      return formattedNumber;
   }

   public String getStatusMessage() {
      return statusMessage;
   }

   public void setStatusMessage(String statusMessage) {
      this.statusMessage = statusMessage;
   }
}
