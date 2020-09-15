package com.example.myapplication.modelClassPassResetRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassResetRequest {
    @SerializedName("e")
    @Expose
    private Integer e;
    @SerializedName("m")
    @Expose
    private com.example.myapplication.modelClassPassResetRequest.M m = null;
    public Integer getE() {
        return e;
    }
    public void setE(Integer e) {
        this.e = e;
    }
    public com.example.myapplication.modelClassPassResetRequest.M getM() {
        return m;
    }
    public void setM(M m) {
        this.m = m;
    }
}
