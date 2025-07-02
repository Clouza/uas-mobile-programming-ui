package com.example.uiclient.response;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("key")
    private String key;

    public String getKey() {
        return key;
    }
}
