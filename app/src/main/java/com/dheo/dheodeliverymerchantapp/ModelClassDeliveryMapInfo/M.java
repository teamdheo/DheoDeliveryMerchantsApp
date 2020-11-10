
package com.dheo.dheodeliverymerchantapp.ModelClassDeliveryMapInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("courier_ping_map")
    @Expose
    private Boolean courierPingMap;
    @SerializedName("courier_name")
    @Expose
    private String courierName;
    @SerializedName("courier_phone")
    @Expose
    private String courierPhone;
    @SerializedName("courier_photo")
    @Expose
    private String courierPhoto;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("last_seen")
    @Expose
    private String lastSeen;

    public Boolean getCourierPingMap() {
        return courierPingMap;
    }

    public void setCourierPingMap(Boolean courierPingMap) {
        this.courierPingMap = courierPingMap;
    }

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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

}
