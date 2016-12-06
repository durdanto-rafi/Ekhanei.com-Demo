package com.ekhanei.mvp.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ekhanei.mvp.ConstantValues.URL;


public class ApiClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
