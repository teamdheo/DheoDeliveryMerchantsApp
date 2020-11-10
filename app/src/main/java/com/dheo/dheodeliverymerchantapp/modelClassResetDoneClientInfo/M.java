package com.dheo.dheodeliverymerchantapp.modelClassResetDoneClientInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {
    @SerializedName("photo_url")
    @Expose
    private String photoUrl;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("balance")
    @Expose
    private Integer balance;
    @SerializedName("phone_number")
    @Expose
    private String phone_number;
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
