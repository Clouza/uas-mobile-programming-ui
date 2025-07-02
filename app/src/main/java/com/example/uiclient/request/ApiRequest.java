package com.example.uiclient.request;

public class ApiRequest {
    private String key;

    public ApiRequest(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
