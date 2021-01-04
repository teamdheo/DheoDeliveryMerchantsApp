
package com.dheo.dheodeliverymerchantapp.ModelClassPickupHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("total_pickup")
    @Expose
    private Integer totalPickup;
    @SerializedName("pickup_date")
    @Expose
    private String pickupDate;
    @SerializedName("agent_name")
    @Expose
    private String agentName;
    @SerializedName("agent_photo")
    @Expose
    private String agentPhoto;
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

    public Integer getTotalPickup() {
        return totalPickup;
    }

    public void setTotalPickup(Integer totalPickup) {
        this.totalPickup = totalPickup;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentPhoto() {
        return agentPhoto;
    }

    public void setAgentPhoto(String agentPhoto) {
        this.agentPhoto = agentPhoto;
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

}
