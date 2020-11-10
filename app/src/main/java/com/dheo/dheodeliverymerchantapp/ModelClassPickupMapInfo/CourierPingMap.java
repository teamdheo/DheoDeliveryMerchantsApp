
package com.dheo.dheodeliverymerchantapp.ModelClassPickupMapInfo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourierPingMap {

    @SerializedName("agents")
    @Expose
    private List<Agent> agents = null;

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

}
