package com.example.myapplication.modelClassPassResetRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {
    @SerializedName("sms_token")
    @Expose
    private String sms_token;
    public String getSms_token() {
        return sms_token;
    }
    public void setSms_token(String sms_token) {
        this.sms_token = sms_token;
    }
}