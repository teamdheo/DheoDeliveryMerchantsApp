
package com.dheo.myapplication.ModelClassBankBranches;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("bank_branches")
    @Expose
    private List<String> bankBranches = null;

    public List<String> getBankBranches() {
        return bankBranches;
    }

    public void setBankBranches(List<String> bankBranches) {
        this.bankBranches = bankBranches;
    }

}
