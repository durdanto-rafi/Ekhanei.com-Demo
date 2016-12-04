package com.androidtime.mvp.interfaces;

import com.androidtime.mvp.model.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    /*@GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);*/

    @GET("api")
    Call<Recipe> getRecipe(@Query("p") int page, @Query("q") String query);
}
