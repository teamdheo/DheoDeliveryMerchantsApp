
package com.dheo.dheodeliverymerchantapp.modelClassPickupAddresses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class M {

    @SerializedName("client_pickup_address")
    @Expose
    private String clientPickupAddress;

    @SerializedName("phone_no")
    @Expose
    private String phone_no;

    @SerializedName("address_id")
    @Expose
    private String address_id;

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getClientPickupAddress() {
        return clientPickupAddress;
    }

    public void setClientPickupAddress(String clientPickupAddress) {
        this.clientPickupAddress = clientPickupAddress;
    }
    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }


}
