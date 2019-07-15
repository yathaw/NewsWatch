package com.example.newswatch.api;

import com.example.newswatch.models.topheadline.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface
{
    @GET("top-headlines")
    Call<News> getNews (
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );
}
