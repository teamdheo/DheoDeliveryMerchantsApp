
package com.dheo.dheodeliverymerchantapp.ModelClassGrowthPerDay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("day")
    @Expose
    private Integer day;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("pretty_date")
    @Expose
    private String prettyDate;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPrettyDate() {
        return prettyDate;
    }

    public void setPrettyDate(String prettyDate) {
        this.prettyDate = prettyDate;
    }

}
