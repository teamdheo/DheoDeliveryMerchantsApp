
package com.dheo.dheodeliverymerchantapp.ModelClassGrowthPerMonth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("month")
    @Expose
    private Integer month;
    @SerializedName("pretty_month")
    @Expose
    private String prettyMonth;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("total")
    @Expose
    private Integer total;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getPrettyMonth() {
        return prettyMonth;
    }

    public void setPrettyMonth(String prettyMonth) {
        this.prettyMonth = prettyMonth;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
