package com.example.qrcode.retrofit;

import com.example.qrcode.model.Response;
import com.google.gson.JsonArray;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface APIServices {

    @FormUrlEncoded
    @POST("api/create-citizen.php")
    Call<Response> createCitizen(@Field("qrCitizenData") String qrCitizenData ,
                                 @Field("qrUnitData") String qrUnitData,
                                 @Field("additional") String addData);
}
