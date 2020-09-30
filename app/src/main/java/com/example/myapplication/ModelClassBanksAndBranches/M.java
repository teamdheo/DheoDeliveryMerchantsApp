
package com.example.myapplication.ModelClassBanksAndBranches;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("bank_branches")
    @Expose
    private List<String> bankBranches = null;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<String> getBankBranches() {
        return bankBranches;
    }

    public void setBankBranches(List<String> bankBranches) {
        this.bankBranches = bankBranches;
    }

}
