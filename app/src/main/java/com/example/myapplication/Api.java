package com.example.myapplication;

import com.example.myapplication.modelClassAvaiablePickupSlot.AvailablePickupSlot;
import com.example.myapplication.modelClassClientInfo.ClientInfo;
import com.example.myapplication.modelClassPassResetRequest.PassResetRequest;
import com.example.myapplication.modelClassPickupAddresses.PickupAddresses;
import com.example.myapplication.modelClassResetDoneClientInfo.ResetDoneClientInfo;
import com.example.myapplication.modelClassSignupClientInfo.SignupClientInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    //client sign up
    @FormUrlEncoded
    @POST("sign_up")
    Call<SignupClientInfo> sign_up(
            @Field("businessName") String businessName,
            @Field("address") String address,
            @Field("phoneNo") String phoneNo,
            @Field("pass") String pass
    );
    //client logIn
    @FormUrlEncoded
    @POST("log_in")
    Call<ClientInfo> log_in(
            @Field("number") String number,
            @Field("password") String password
    );
    //reset request
    @FormUrlEncoded
    @POST("reset_request")
    Call<PassResetRequest> reset_request(
            @Field("client_number") String client_number
    );
    //reset pass done
    @FormUrlEncoded
    @POST("reset_password")
    Call<ResetDoneClientInfo> reset_password(
            @Field("password") String password,
            @Field("token") String token
    );
    @FormUrlEncoded
    @POST("get_pickup_address")
    Call<PickupAddresses> get_pickup_address(
            @Field("clientId") Integer clientId
    );

    @FormUrlEncoded
    @POST("get_avaiable_pickup_slot")
    Call<AvailablePickupSlot> get_avaiable_pickup_slot(
            @Field("clientId") Integer clientId
    );
    @FormUrlEncoded
    @POST("get_multiple_address_slot")
    Call<AvailablePickupSlot> get_multiple_address_slot(
            @Field("client_id") Integer client_id,
            @Field("address_id_get") String address_id_get
    );
    @FormUrlEncoded
    @POST("book_address_pickup")
    Call<ResponseBody> book_address_pickup(
            @Field("client_id") Integer client_id,
            @Field("address_id") String address_id,
            @Field("slot_id") String slot_id
    );
}
