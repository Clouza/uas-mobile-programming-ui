package com.example.uiclient.request;

public class DetailNewsRequest {
    private String text;

    public DetailNewsRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
