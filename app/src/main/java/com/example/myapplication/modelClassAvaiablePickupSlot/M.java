
package com.example.myapplication.modelClassAvaiablePickupSlot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("next_day")
    @Expose
    private Boolean nextDay;
    @SerializedName("already_booked")
    @Expose
    private Boolean alreadyBooked;
    @SerializedName("unbookable")
    @Expose
    private Boolean unbookable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Boolean getNextDay() {
        return nextDay;
    }

    public void setNextDay(Boolean nextDay) {
        this.nextDay = nextDay;
    }

    public Boolean getAlreadyBooked() {
        return alreadyBooked;
    }

    public void setAlreadyBooked(Boolean alreadyBooked) {
        this.alreadyBooked = alreadyBooked;
    }
    public Boolean getUnbookable() {
        return unbookable;
    }

    public void setUnbookable(Boolean unbookable) {
        this.unbookable = unbookable;
    }

}
