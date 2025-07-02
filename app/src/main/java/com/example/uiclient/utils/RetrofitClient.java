package com.example.uiclient.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static String BASE_URL = "http://10.0.2.2:3000";

    public static Retrofit client() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static String getAssetPath() {
        return BASE_URL + "/public/files/";
    }
}
