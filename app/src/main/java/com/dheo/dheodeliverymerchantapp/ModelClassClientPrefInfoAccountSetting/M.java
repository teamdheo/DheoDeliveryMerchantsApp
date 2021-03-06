
package com.dheo.dheodeliverymerchantapp.ModelClassClientPrefInfoAccountSetting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("stat_date")
    @Expose
    private String statDate;
    @SerializedName("prefers_bank")
    @Expose
    private Boolean prefersBank;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("branch_name")
    @Expose
    private String branchName;
    @SerializedName("account_name")
    @Expose
    private String accountName;
    @SerializedName("account_number")
    @Expose
    private String accountNumber;
    @SerializedName("routing_number")
    @Expose
    private String routingNumber;
    @SerializedName("prefers_cash")
    @Expose
    private Boolean prefersCash;
    @SerializedName("prefers_bkash")
    @Expose
    private Boolean prefersBkash;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("prefers_nagad")
    @Expose
    private Boolean prefersNagad;
    @SerializedName("is_submitted")
    @Expose
    private Boolean isSubmitted;
    @SerializedName("phone_no")
    @Expose
    private String phoneNo;
    @SerializedName("submitted_date")
    @Expose
    private String submittedDate;
    @SerializedName("link")
    @Expose
    private String link;

    public Boolean getSubmitted() {
        return isSubmitted;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setSubmitted(Boolean submitted) {
        isSubmitted = submitted;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public Boolean getPrefersBank() {
        return prefersBank;
    }

    public void setPrefersBank(Boolean prefersBank) {
        this.prefersBank = prefersBank;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Boolean getPrefersCash() {
        return prefersCash;
    }

    public void setPrefersCash(Boolean prefersCash) {
        this.prefersCash = prefersCash;
    }

    public Boolean getPrefersBkash() {
        return prefersBkash;
    }

    public void setPrefersBkash(Boolean prefersBkash) {
        this.prefersBkash = prefersBkash;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getPrefersNagad() {
        return prefersNagad;
    }

    public void setPrefersNagad(Boolean prefersNagad) {
        this.prefersNagad = prefersNagad;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }
}
