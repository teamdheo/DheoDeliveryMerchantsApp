
package com.dheo.dheodeliverymerchantapp.ModelClassOrderStatusPageInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("courier_name")
    @Expose
    private String courierName;
    @SerializedName("courier_phone")
    @Expose
    private String courierPhone;
    @SerializedName("courier_photo")
    @Expose
    private String courierPhoto;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_phone")
    @Expose
    private String customerPhone;
    @SerializedName("has_review")
    @Expose
    private Boolean hasReview;
    @SerializedName("cash_payment")
    @Expose
    private Boolean cashPayment;
    @SerializedName("customer_review")
    @Expose
    private String customerReview;
    @SerializedName("customer_rating")
    @Expose
    private Integer customerRating;

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getCourierPhone() {
        return courierPhone;
    }

    public void setCourierPhone(String courierPhone) {
        this.courierPhone = courierPhone;
    }

    public String getCourierPhoto() {
        return courierPhoto;
    }

    public void setCourierPhoto(String courierPhoto) {
        this.courierPhoto = courierPhoto;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Boolean getHasReview() {
        return hasReview;
    }

    public void setHasReview(Boolean hasReview) {
        this.hasReview = hasReview;
    }

    public String getCustomerReview() {
        return customerReview;
    }

    public void setCustomerReview(String customerReview) {
        this.customerReview = customerReview;
    }

    public Integer getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Integer customerRating) {
        this.customerRating = customerRating;
    }

    public Boolean getCashPayment() {
        return cashPayment;
    }

    public void setCashPayment(Boolean cashPayment) {
        this.cashPayment = cashPayment;
    }
}
