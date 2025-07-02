package com.example.uiclient.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("success")
    private boolean isSuccess;
    @SerializedName("message")
    private String message;

    public String getId() {
        return id;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }
}
