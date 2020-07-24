package com.example.warmupappjava;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsResponse {
    private String status;
    private  int totalResults;
    @SerializedName("articles")
    private List<NewsArticle> newsArticleList;

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<NewsArticle> getNewsArticleList() {
        return newsArticleList;
    }
}
