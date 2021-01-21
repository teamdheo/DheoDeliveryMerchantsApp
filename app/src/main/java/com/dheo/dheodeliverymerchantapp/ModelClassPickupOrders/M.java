
package com.dheo.dheodeliverymerchantapp.ModelClassPickupOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("pickup_date")
    @Expose
    private String pickupDate;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_address")
    @Expose
    private String customerAddress;
    @SerializedName("customer_phone")
    @Expose
    private String customerPhone;
    @SerializedName("cod_amount")
    @Expose
    private String codAmount;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("order_created")
    @Expose
    private Boolean orderCreated;
    @SerializedName("picked_up")
    @Expose
    private Boolean pickedUp;
    @SerializedName("intake_done")
    @Expose
    private Boolean intakeDone;
    @SerializedName("has_results")
    @Expose
    private Boolean hasResults;
    @SerializedName("page_number")
    @Expose
    private Integer pageNumber;
    @SerializedName("pickup_id")
    @Expose
    private Integer pickupId;
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

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCodAmount() {
        return codAmount;
    }

    public void setCodAmount(String codAmount) {
        this.codAmount = codAmount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public Boolean getOrderCreated() {
        return orderCreated;
    }

    public void setOrderCreated(Boolean orderCreated) {
        this.orderCreated = orderCreated;
    }

    public Boolean getPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(Boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    public Boolean getIntakeDone() {
        return intakeDone;
    }

    public void setIntakeDone(Boolean intakeDone) {
        this.intakeDone = intakeDone;
    }

    public Integer getPickupId() {
        return pickupId;
    }

    public void setPickupId(Integer pickupId) {
        this.pickupId = pickupId;
    }
}
