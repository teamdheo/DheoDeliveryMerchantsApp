
package com.example.myapplication.ModelClassClientPaymentPerfInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("positive_balance")
    @Expose
    private Boolean positiveBalance;
    @SerializedName("negative_balance")
    @Expose
    private Boolean negativeBalance;
    @SerializedName("time_left")
    @Expose
    private Integer timeLeft;
    @SerializedName("prefers_bkash")
    @Expose
    private Boolean prefersBkash;
    @SerializedName("prefers_bank")
    @Expose
    private Boolean prefersBank;
    @SerializedName("prefers_cash")
    @Expose
    private Boolean prefersCash;

    public Boolean getNegativeBalance() {
        return negativeBalance;
    }

    public void setNegativeBalance(Boolean negativeBalance) {
        this.negativeBalance = negativeBalance;
    }

    public Boolean getPrefersBank() {
        return prefersBank;
    }

    public void setPrefersBank(Boolean prefersBank) {
        this.prefersBank = prefersBank;
    }

    public Boolean getPrefersCash() {
        return prefersCash;
    }

    public void setPrefersCash(Boolean prefersCash) {
        this.prefersCash = prefersCash;
    }

    public Boolean getPrefersNagad() {
        return prefersNagad;
    }

    public void setPrefersNagad(Boolean prefersNagad) {
        this.prefersNagad = prefersNagad;
    }

    @SerializedName("prefers_nagad")
    @Expose
    private Boolean prefersNagad;
    @SerializedName("stat_date")
    @Expose
    private String statDate;

    public Boolean getPositiveBalance() {
        return positiveBalance;
    }

    public void setPositiveBalance(Boolean positiveBalance) {
        this.positiveBalance = positiveBalance;
    }

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Boolean getPrefersBkash() {
        return prefersBkash;
    }

    public void setPrefersBkash(Boolean prefersBkash) {
        this.prefersBkash = prefersBkash;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

}
