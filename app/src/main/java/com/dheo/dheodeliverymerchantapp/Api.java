package com.dheo.dheodeliverymerchantapp;

import com.dheo.dheodeliverymerchantapp.ModelClassAssingedCourierInfoDashboard.AssingedCourierInfoDashboard;
import com.dheo.dheodeliverymerchantapp.ModelClassBankBranches.BankBranches;
import com.dheo.dheodeliverymerchantapp.ModelClassBanksAndBranches.BanksAndBranches;
import com.dheo.dheodeliverymerchantapp.ModelClassBlogUpdateTitle.BlogUpdateTitle;
import com.dheo.dheodeliverymerchantapp.ModelClassClientBasicInfo.ClientBasicInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassClientDashboardPayloads.ClientDashboardPayloads;
import com.dheo.dheodeliverymerchantapp.ModelClassClientEditPayload.ClientEditPayload;
import com.dheo.dheodeliverymerchantapp.ModelClassClientMonthlyStatementDate.ClientMonthlyStatementDate;
import com.dheo.dheodeliverymerchantapp.ModelClassClientPayloadSearch.ClientPayloadSearch;
import com.dheo.dheodeliverymerchantapp.ModelClassClientPaymentPerfInfo.ClientPaymentPerfInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassClientPaymentReceiptPDF.ClientPaymentReceiptPDF;
import com.dheo.dheodeliverymerchantapp.ModelClassClientPrefInfoAccountSetting.ClientPrefInfoAccountSetting;
import com.dheo.dheodeliverymerchantapp.ModelClassDeliveryMapInfo.DeliveryMapInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassLatestAccountActivity.LatestAccountActivity;
import com.dheo.dheodeliverymerchantapp.ModelClassOrderStatusPageInfo.OrderStatusPageInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassPickupMapInfo.PickupMapInfo;
import com.dheo.dheodeliverymerchantapp.ModelClassTrackerLogEntry.TrackerLogEntry;
import com.dheo.dheodeliverymerchantapp.modelClassAvaiablePickupSlot.AvailablePickupSlot;
import com.dheo.dheodeliverymerchantapp.modelClassPickupAddresses.PickupAddresses;
import com.dheo.dheodeliverymerchantapp.modelClassClientInfo.ClientInfo;
import com.dheo.dheodeliverymerchantapp.modelClassPassResetRequest.PassResetRequest;
import com.dheo.dheodeliverymerchantapp.modelClassResetDoneClientInfo.ResetDoneClientInfo;
import com.dheo.dheodeliverymerchantapp.modelClassSignupClientInfo.SignupClientInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    //client sign up
    @FormUrlEncoded
    @POST("sign_up")
    Call<SignupClientInfo> sign_up(
            @Field("business_name") String business_name,
            @Field("address") String address,
            @Field("phone_no") String phone_no,
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

    @FormUrlEncoded
    @POST("cancel_pickup")
    Call<ResponseBody> cancel_pickup(
            @Field("client_id") Integer client_id,
            @Field("slot_id") String slot_id
    );

    @FormUrlEncoded
    @POST("assigned_agent_info_dashboard")
    Call<AssingedCourierInfoDashboard> assigned_agent_info_dashboard(
            @Field("client_id") Integer client_id
    );

    @FormUrlEncoded
    @POST("client_payment_perf_info")
    Call<ClientPaymentPerfInfo> client_payment_perf_info(
            @Field("client_id") Integer client_id
    );

    @FormUrlEncoded
    @POST("latest_account_activity")
    Call<LatestAccountActivity> latest_account_activity(
            @Field("client_id") Integer client_id
    );

    @FormUrlEncoded
    @POST("client_payment_receipt_pdf")
    Call<ClientPaymentReceiptPDF> client_payment_receipt_pdf(
            @Field("client_id") Integer client_id,
            @Field("page_number") Integer page_number
    );

    @FormUrlEncoded
    @POST("client_dashboard_payloads")
    Call<ClientDashboardPayloads> client_dashboard_payloads(
            @Field("client_id") Integer client_id,
            @Field("page_number") Integer page_number
    );

    @FormUrlEncoded
    @POST("client_load_payload_page")
    Call<ClientDashboardPayloads> client_load_payload_page(
            @Field("client_id") Integer client_id,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @FormUrlEncoded
    @POST("client_payment_statement_date")
    Call<ClientMonthlyStatementDate> client_payment_statement_date(
            @Field("client_id") Integer client_id
    );

    @FormUrlEncoded
    @POST("client_editable_payload")
    Call<ClientEditPayload> client_editable_payload(
            @Field("payload_id") Integer payload_id
    );

    @FormUrlEncoded
    @POST("client_update_payload")
    Call<ResponseBody> client_update_payload(
            @Field("payload_id") Integer payload_id,
            @Field("edited_amount") String edited_amount,
            @Field("edited_phone") String edited_phone
    );

    @GET("bank_and_branches")
    Call<BanksAndBranches> bank_and_branches(
    );

    @FormUrlEncoded
    @POST("branches")
    Call<BankBranches> branches(
            @Field("bank_id") Integer bank_id
    );

    @FormUrlEncoded
    @POST("client_account_pref_setting_info")
    Call<ClientPrefInfoAccountSetting> client_account_pref_setting_info(
            @Field("client_id") Integer client_id
    );

    @FormUrlEncoded
    @POST("client_payment_settings_update")
    Call<ResponseBody> client_payment_settings_update(
            @Field("client_id") Integer client_id,
            @Field("mode") String mode,
            @Field("bank_name") String bank_name,
            @Field("bank_branch") String bank_branch,
            @Field("account_name") String account_name,
            @Field("account_number") String account_number,
            @Field("bkash_number") String bkash_number,
            @Field("nagad_num") String nagad_num
    );

    @FormUrlEncoded
    @POST("delete_pickup_address")
    Call<ResponseBody> delete_pickup_address(
            @Field("Pickup_address_id") String Pickup_address_id
    );

    @FormUrlEncoded
    @POST("add_new_address")
    Call<ResponseBody> add_new_address(
            @Field("client_id") Integer client_id,
            @Field("number") String number,
            @Field("address") String address
    );

    @FormUrlEncoded
    @POST("update_link")
    Call<ResponseBody> update_link(
            @Field("client_id") Integer client_id,
            @Field("link") String link
    );

    @FormUrlEncoded
    @POST("update_number")
    Call<ResponseBody> update_number(
            @Field("client_id") Integer client_id,
            @Field("number") String number
    );

    @FormUrlEncoded
    @POST("update_address")
    Call<ResponseBody> update_address(
            @Field("address_id") String address_id,
            @Field("pickup_address") String pickup_address,
            @Field("pickup_phone") String pickup_phone
    );

    @FormUrlEncoded
    @POST("upload_national_id")
    Call<ResponseBody> upload_national_id(
            @Field("client_id") Integer client_id,
            @Field("national_id") String national_id
    );

    @FormUrlEncoded
    @POST("client_payload_search")
    Call<ClientPayloadSearch> client_payload_search(
            @Field("client_id") Integer client_id,
            @Field("number") String number
    );

    @FormUrlEncoded
    @POST("client_pickup_map")
    Call<PickupMapInfo> client_pickup_map(
            @Field("client_id") Integer client_id
    );

    @FormUrlEncoded
    @POST("client_basic_info")
    Call<ClientBasicInfo> client_basic_info(
            @Field("client_id") Integer client_id
    );

    @FormUrlEncoded
    @POST("order_tracker_log_entry")
    Call<TrackerLogEntry> order_tracker_log_entry(
            @Field("payload_id") Integer payload_id
    );

    @FormUrlEncoded
    @POST("delivery_map_info")
    Call<DeliveryMapInfo> delivery_map_info(
            @Field("payload_id") Integer payload_id
    );

    @FormUrlEncoded
    @POST("order_status_page_info")
    Call<OrderStatusPageInfo> order_status_page_info(
            @Field("payload_id") Integer payload_id
    );

    @GET("blog_update_title")
    Call<BlogUpdateTitle> blog_update_title(
    );

    @FormUrlEncoded
    @POST("add_pickup_note")
    Call<ResponseBody> add_pickup_note(
            @Field("client_id") Integer client_id,
            @Field("slot_id") String slot_id,
            @Field("pickup_note") String pickup_note
    );

    @FormUrlEncoded
    @POST("user_agreement")
    Call<ResponseBody> user_agreement(
            @Field("client_id") Integer client_id,
            @Field("status") String status

    );
    @FormUrlEncoded
    @POST("cancel_delivery")
    Call<ResponseBody> cancel_delivery(
            @Field("client_id") Integer client_id,
            @Field("payload_id") Integer payload_id

    );
    @FormUrlEncoded
    @POST("client_profile_photo_upload")
    Call<ResponseBody> client_profile_photo_upload(
            @Field("client_id") Integer client_id,
            @Field("pro_pic") String pro_pic
    );
    @FormUrlEncoded
    @POST("client_bill_payment")
    Call<ResponseBody> client_bill_payment(
            @Field("client_id") Integer client_id,
            @Field("payment_amount") String payment_amount,
            @Field("bkash_id") String bkash_id
    );

    @FormUrlEncoded
    @POST("client_return_claim")
    Call<ResponseBody> client_return_claim(
            @Field("payload_id") Integer payload_id,
            @Field("client_id") Integer client_id,
            @Field("reason") String reason,
            @Field("feedback") String feedback
    );
    @FormUrlEncoded
    @POST("version_check")
    Call<ResponseBody> version_check(
            @Field("version") String version
    );
    @FormUrlEncoded
    @POST("notification_token_update")
    Call<ResponseBody> notification_token_update(
            @Field("client_id") Integer client_id,
            @Field("notification_token") String notification_token
    );

}
