package com.androidtime.mvp.rest;

import com.androidtime.mvp.model.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("api")
    Call<Recipe> getRecipe(@Query("p") int page, @Query("q") String query);
}
