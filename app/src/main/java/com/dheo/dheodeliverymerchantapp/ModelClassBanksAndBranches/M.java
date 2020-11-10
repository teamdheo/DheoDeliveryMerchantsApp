
package com.dheo.dheodeliverymerchantapp.ModelClassBanksAndBranches;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("bank_id")
    @Expose
    private Integer bankId;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

}