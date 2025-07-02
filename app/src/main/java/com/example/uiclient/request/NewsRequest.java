package com.example.uiclient.request;

public class NewsRequest {
    private String apiKey;

    public NewsRequest(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
