package com.androidtime.mvp.model;

import android.content.Context;
import android.widget.Toast;

import com.androidtime.mvp.interfaces.ApiInterface;
import com.androidtime.mvp.interfaces.OnDataProcess;
import com.androidtime.mvp.interfaces.OnRequestComplete;
import com.androidtime.mvp.utilities.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by RAFI on 05-Dec-16.
 */

public class InvokeRecipeApi {
    OnDataProcess dataProcess;

    public InvokeRecipeApi(Context context, int pageIndex, String query, final OnDataProcess onDataProcess) {
        this.dataProcess = onDataProcess;
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Recipe> call = apiService.getRecipe(pageIndex, query);
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful()) {
                    //recipeDetails.addAll(response.body().getRecipeDetailsList());
                    //adapter.notifyDataChanged();
                    dataProcess.OnDataProcess(response.body().getRecipeDetailsList());
                } else {
                    //Toast.makeText(mainActivity, "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                // Log error here since request failed
                //Log.e(TAG, t.toString());
            }
        });
    }
}
