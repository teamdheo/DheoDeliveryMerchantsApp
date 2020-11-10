
package com.dheo.dheodeliverymerchantapp.ModelClassPickupMapInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("courier_ping_map")
    @Expose
    private CourierPingMap courierPingMap;

    public CourierPingMap getCourierPingMap() {
        return courierPingMap;
    }

    public void setCourierPingMap(CourierPingMap courierPingMap) {
        this.courierPingMap = courierPingMap;
    }

}
