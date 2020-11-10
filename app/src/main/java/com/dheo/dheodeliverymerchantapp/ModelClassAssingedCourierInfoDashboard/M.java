
package com.dheo.dheodeliverymerchantapp.ModelClassAssingedCourierInfoDashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("booking_date")
    @Expose
    private String bookingDate;
    @SerializedName("assigned_agent_name")
    @Expose
    private String assignedAgentName;
    @SerializedName("assigned_agent_photo")
    @Expose
    private String assignedAgentPhoto;
    @SerializedName("assigned_agent_number")
    @Expose
    private String assignedAgentNumber;
    @SerializedName("pickup_address")
    @Expose
    private String pickupAddress;
    @SerializedName("has_pickup")
    @Expose
    private Boolean has_pickup;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getAssignedAgentName() {
        return assignedAgentName;
    }

    public void setAssignedAgentName(String assignedAgentName) {
        this.assignedAgentName = assignedAgentName;
    }

    public String getAssignedAgentPhoto() {
        return assignedAgentPhoto;
    }

    public void setAssignedAgentPhoto(String assignedAgentPhoto) {
        this.assignedAgentPhoto = assignedAgentPhoto;
    }

    public String getAssignedAgentNumber() {
        return assignedAgentNumber;
    }

    public void setAssignedAgentNumber(String assignedAgentNumber) {
        this.assignedAgentNumber = assignedAgentNumber;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }
    public Boolean getHas_pickup() {
        return has_pickup;
    }

    public void setHas_pickup(Boolean has_pickup) {
        this.has_pickup = has_pickup;
    }

}
