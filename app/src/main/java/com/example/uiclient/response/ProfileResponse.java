package com.example.uiclient.response;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("success")
    private boolean isSuccess;
    @SerializedName("id")
    private String id;
    @SerializedName("email")
    private String email;
    @SerializedName("image")
    private String image;
    @SerializedName("apiKey")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public String getId() {
        return id;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }
}
