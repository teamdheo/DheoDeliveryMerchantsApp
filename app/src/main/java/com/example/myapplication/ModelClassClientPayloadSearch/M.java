
package com.example.myapplication.ModelClassClientPayloadSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("short_id")
    @Expose
    private String shortId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("delivery_done")
    @Expose
    private Boolean deliveryDone;
    @SerializedName("return_started")
    @Expose
    private Boolean returnStarted;
    @SerializedName("courier_drop")
    @Expose
    private Boolean courierDrop;
    @SerializedName("has_review")
    @Expose
    private Boolean hasReview;
    @SerializedName("delayed")
    @Expose
    private Boolean delayed;
    @SerializedName("delivery_started")
    @Expose
    private Boolean deliveryStarted;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("payload_cancelled")
    @Expose
    private Boolean payloadCancelled;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("return_done")
    @Expose
    private Boolean returnDone;
    @SerializedName("on_hold")
    @Expose
    private Boolean onHold;
    @SerializedName("enable_edit")
    @Expose
    private Boolean enableEdit;
    @SerializedName("on_hold_label")
    @Expose
    private String onHoldLabel;
    @SerializedName("payload_id")
    @Expose
    private Integer payloadId;
    @SerializedName("courier_memo_added")
    @Expose
    private Boolean courierMemoAdded;

    public Boolean getCourierMemoAdded() {
        return courierMemoAdded;
    }

    public void setCourierMemoAdded(Boolean courierMemoAdded) {
        this.courierMemoAdded = courierMemoAdded;
    }

    public Integer getPayloadId() {
        return payloadId;
    }

    public void setPayloadId(Integer payloadId) {
        this.payloadId = payloadId;
    }

    public Boolean getOnHold() {
        return onHold;
    }

    public void setOnHold(Boolean onHold) {
        this.onHold = onHold;
    }

    public Boolean getEnableEdit() {
        return enableEdit;
    }

    public void setEnableEdit(Boolean enableEdit) {
        this.enableEdit = enableEdit;
    }

    public String getOnHoldLabel() {
        return onHoldLabel;
    }

    public void setOnHoldLabel(String onHoldLabel) {
        this.onHoldLabel = onHoldLabel;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getDeliveryDone() {
        return deliveryDone;
    }

    public void setDeliveryDone(Boolean deliveryDone) {
        this.deliveryDone = deliveryDone;
    }

    public Boolean getHasReview() {
        return hasReview;
    }

    public void setHasReview(Boolean hasReview) {
        this.hasReview = hasReview;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Boolean getPayloadCancelled() {
        return payloadCancelled;
    }

    public void setPayloadCancelled(Boolean payloadCancelled) {
        this.payloadCancelled = payloadCancelled;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Boolean getReturnDone() {
        return returnDone;
    }

    public void setReturnDone(Boolean returnDone) {
        this.returnDone = returnDone;
    }
    public Boolean getReturnStarted() {
        return returnStarted;
    }

    public void setReturnStarted(Boolean returnStarted) {
        this.returnStarted = returnStarted;
    }

    public Boolean getCourierDrop() {
        return courierDrop;
    }

    public void setCourierDrop(Boolean courierDrop) {
        this.courierDrop = courierDrop;
    }

    public Boolean getDelayed() {
        return delayed;
    }

    public void setDelayed(Boolean delayed) {
        this.delayed = delayed;
    }

    public Boolean getDeliveryStarted() {
        return deliveryStarted;
    }

    public void setDeliveryStarted(Boolean deliveryStarted) {
        this.deliveryStarted = deliveryStarted;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


}
