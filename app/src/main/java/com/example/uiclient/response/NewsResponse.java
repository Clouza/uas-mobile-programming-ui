package com.example.uiclient.response;

import com.example.uiclient.model.NewsItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsResponse {
    @SerializedName("success")
    private boolean isSuccess;
    @SerializedName("news")
    private List<NewsItem> news;

    public List<NewsItem> getNews() {
        return news;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
