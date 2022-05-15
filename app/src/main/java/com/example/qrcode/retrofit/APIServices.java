package com.example.qrcode.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface APIServices {


    @FormUrlEncoded
    @POST("api/create-citizen.php")
    Call<ResponseBody> createCitizen(@Field("qrData") String qrData, @Field("email") String email);
}
