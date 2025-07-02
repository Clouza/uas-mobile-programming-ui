package com.example.uiclient.utils;

import com.example.uiclient.request.ApiRequest;
import com.example.uiclient.request.LoginRequest;
import com.example.uiclient.request.NewsRequest;
import com.example.uiclient.request.ProfileRequest;
import com.example.uiclient.response.ApiResponse;
import com.example.uiclient.response.LoginResponse;
import com.example.uiclient.response.NewsResponse;
import com.example.uiclient.response.ProfileResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @GET("user")
    Call<ProfileResponse> userInfo(@Query("id") String userId);

    @Multipart
    @POST("user")
    Call<ProfileResponse> updateUserInfo(
            @Part MultipartBody.Part file,
            @Part("id") RequestBody id,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("apiKey") RequestBody apiKey
    );

    @POST("news")
    Call<NewsResponse> news(@Body NewsRequest newsRequest);

    @POST("key")
    Call<ApiResponse> apiKey(@Body ApiRequest apiRequest);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
