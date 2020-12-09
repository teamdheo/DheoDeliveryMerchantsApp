
package com.dheo.dheodeliverymerchantapp.ModelClassClientPaymentReceiptPDF;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("id_time")
    @Expose
    private String idTime;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getIdTime() {
        return idTime;
    }

    public void setIdTime(String idTime) {
        this.idTime = idTime;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
