package com.example.warmupappjava;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    @GET("top-headlines")
    Call<NewsResponse> getTopStories(@Query("country") String country,
                                     @Query("apiKey") String apiKey,
                                     @Query("page") int page,
                                     @Query("pageSize") int pageSize);
}
