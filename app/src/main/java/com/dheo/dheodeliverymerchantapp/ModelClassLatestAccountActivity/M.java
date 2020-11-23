
package com.dheo.dheodeliverymerchantapp.ModelClassLatestAccountActivity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("lebel")
    @Expose
    private String lebel;
    @SerializedName("is_negative")
    @Expose
    private Boolean isNegative;
    @SerializedName("payload_id")
    @Expose
    private String payloadId;
    @SerializedName("id")
    @Expose
    private String Id;
    @SerializedName("short_id")
    @Expose
    private String shortId;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("tagged_accounting_entry")
    @Expose
    private String taggedAccountingEntry;
    @SerializedName("has_tagged_entry")
    @Expose
    private Boolean hasTaggedEntry;
    @SerializedName("has_painding")
    @Expose
    private Boolean hasPainding;
    @SerializedName("has_rejected")
    @Expose
    private Boolean hasRejected;

    public Boolean getNegative() {
        return isNegative;
    }

    public void setNegative(Boolean negative) {
        isNegative = negative;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLebel() {
        return lebel;
    }

    public void setLebel(String lebel) {
        this.lebel = lebel;
    }

    public Boolean getIsNegative() {
        return isNegative;
    }

    public void setIsNegative(Boolean isNegative) {
        this.isNegative = isNegative;
    }

    public String getPayloadId() {
        return payloadId;
    }

    public void setPayloadId(String payloadId) {
        this.payloadId = payloadId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTaggedAccountingEntry() {
        return taggedAccountingEntry;
    }

    public void setTaggedAccountingEntry(String taggedAccountingEntry) {
        this.taggedAccountingEntry = taggedAccountingEntry;
    }

    public Boolean getHasTaggedEntry() {
        return hasTaggedEntry;
    }

    public void setHasTaggedEntry(Boolean hasTaggedEntry) {
        this.hasTaggedEntry = hasTaggedEntry;
    }

    public Boolean getHasPainding() {
        return hasPainding;
    }

    public void setHasPainding(Boolean hasPainding) {
        this.hasPainding = hasPainding;
    }

    public Boolean getHasRejected() {
        return hasRejected;
    }

    public void setHasRejected(Boolean hasRejected) {
        this.hasRejected = hasRejected;
    }
}
