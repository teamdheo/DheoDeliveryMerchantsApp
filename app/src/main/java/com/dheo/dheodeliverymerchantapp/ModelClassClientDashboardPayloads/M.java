
package com.dheo.dheodeliverymerchantapp.ModelClassClientDashboardPayloads;

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
    @SerializedName("return_claim")
    @Expose
    private Boolean returnClaim;
    @SerializedName("has_review")
    @Expose
    private Boolean hasReview;
    @SerializedName("delayed")
    @Expose
    private Boolean delayed;
    @SerializedName("claim_pending")
    @Expose
    private Boolean claimPending;
    @SerializedName("claim_received")
    @Expose
    private Boolean claimReceived;
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
    @SerializedName("has_results")
    @Expose
    private Boolean hasResults;
    @SerializedName("page_number")
    @Expose
    private Integer pageNumber;
    @SerializedName("show_next")
    @Expose
    private Boolean showNext;
    @SerializedName("show_prev")
    @Expose
    private Boolean showPrev;
    @SerializedName("records_remaining")
    @Expose
    private Integer recordsRemaining;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("count")
    @Expose
    private Integer count;

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

    public Boolean getReturnClaim() {
        return returnClaim;
    }

    public void setReturnClaim(Boolean returnClaim) {
        this.returnClaim = returnClaim;
    }

    public Boolean getClaimReceived() {
        return claimReceived;
    }

    public void setClaimReceived(Boolean claimReceived) {
        this.claimReceived = claimReceived;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Boolean getHasResults() {
        return hasResults;
    }

    public void setHasResults(Boolean hasResults) {
        this.hasResults = hasResults;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Boolean getShowNext() {
        return showNext;
    }

    public void setShowNext(Boolean showNext) {
        this.showNext = showNext;
    }

    public Boolean getShowPrev() {
        return showPrev;
    }

    public void setShowPrev(Boolean showPrev) {
        this.showPrev = showPrev;
    }

    public Integer getRecordsRemaining() {
        return recordsRemaining;
    }

    public void setRecordsRemaining(Integer recordsRemaining) {
        this.recordsRemaining = recordsRemaining;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Boolean getClaimPending() {
        return claimPending;
    }

    public void setClaimPending(Boolean claimPending) {
        this.claimPending = claimPending;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
