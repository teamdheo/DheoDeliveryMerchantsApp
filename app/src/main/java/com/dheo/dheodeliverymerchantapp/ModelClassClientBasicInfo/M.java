
package com.dheo.dheodeliverymerchantapp.ModelClassClientBasicInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("balance")
    @Expose
    private Integer balance;
    @SerializedName("oob_ux")
    @Expose
    private Boolean oobUx;
    @SerializedName("pro_pic")
    @Expose
    private String proPic;

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

    public Boolean getOobUx() {
        return oobUx;
    }

    public void setOobUx(Boolean oobUx) {
        this.oobUx = oobUx;
    }

    public String getProPic() {
        return proPic;
    }

    public void setProPic(String proPic) {
        this.proPic = proPic;
    }
}
