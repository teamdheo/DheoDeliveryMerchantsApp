
package com.dheo.myapplication.ModelClassClientDashboardPayloads;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientDashboardPayloads {

    @SerializedName("e")
    @Expose
    private Integer e;
    @SerializedName("m")
    @Expose
    private List<M> m = null;

    public Integer getE() {
        return e;
    }

    public void setE(Integer e) {
        this.e = e;
    }

    public List<M> getM() {
        return m;
    }

    public void setM(List<M> m) {
        this.m = m;
    }
}
