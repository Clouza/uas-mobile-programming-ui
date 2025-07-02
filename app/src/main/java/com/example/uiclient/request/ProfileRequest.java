package com.example.uiclient.request;

public class ProfileRequest {
    private String id, email, password, apiKey;

    public ProfileRequest(String id) {
        this.id = id;
    }

    public ProfileRequest(String id, String email, String password, String apiKey) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.apiKey = apiKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
