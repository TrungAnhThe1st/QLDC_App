package com.example.qrcode.retrofit;

import okhttp3.ResponseBody;
import okhttp3.internal.concurrent.Task;
import retrofit2.Call;
import retrofit2.http.*;

public interface APIServices {

    @FormUrlEncoded
    @POST("api/auths/customers/register")
    Call<Task> createCitizen(@Field("qrData") String qrData, @Field("email") String email);
}
