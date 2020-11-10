
package com.dheo.myapplication.ModelClassClientEditPayload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("editable_amount")
    @Expose
    private String editableAmount;
    @SerializedName("ediable_number")
    @Expose
    private String ediableNumber;

    public String getEditableAmount() {
        return editableAmount;
    }

    public void setEditableAmount(String editableAmount) {
        this.editableAmount = editableAmount;
    }

    public String getEdiableNumber() {
        return ediableNumber;
    }

    public void setEdiableNumber(String ediableNumber) {
        this.ediableNumber = ediableNumber;
    }

}
