package com.example.uiclient.utils;

import com.example.uiclient.request.ApiRequest;
import com.example.uiclient.request.LoginRequest;
import com.example.uiclient.request.ProfileRequest;
import com.example.uiclient.response.ApiResponse;
import com.example.uiclient.response.LoginResponse;
import com.example.uiclient.response.NewsResponse;
import com.example.uiclient.response.ProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("user")
    Call<ProfileResponse> userInfo(@Query("id") String userId);

    @POST("user")
    Call<ProfileResponse> updateUserInfo(@Body ProfileRequest profileRequest);

    @POST("news")
    Call<NewsResponse> news();

    @POST("key")
    Call<ApiResponse> apiKey(@Body ApiRequest apiRequest);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
