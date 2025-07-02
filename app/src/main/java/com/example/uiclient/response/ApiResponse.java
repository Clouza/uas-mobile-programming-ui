package com.example.uiclient.response;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("success")
    private boolean isSuccess;
    @SerializedName("apiKey")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
