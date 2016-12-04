package com.androidtime.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.androidtime.mvp.interfaces.ApiInterface;
import com.androidtime.mvp.interfaces.MainActivityView;
import com.androidtime.mvp.model.Recipe;
import com.androidtime.mvp.model.RecipeDetail;
import com.androidtime.mvp.presenter.Adapter;
import com.androidtime.mvp.presenter.ListAdapter;
import com.androidtime.mvp.presenter.MainActivityPresenter;
import com.androidtime.mvp.utilities.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MainActivityView {

    TextView textViewIp;
    TextView textViewCountry;
    TextView textViewLocation;
    ProgressBar progressBar;
    MainActivityPresenter presenter;
    RecyclerView rvList;

    List<RecipeDetail> recipeDetails;
    ListAdapter listAdapter;
    Adapter adapter;
    MainActivity mainActivity;
    int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainActivityPresenter(this);
        rvList = (RecyclerView) findViewById(R.id.rvList);


        recipeDetails = new ArrayList<>();

        adapter = new Adapter(this, recipeDetails);
        adapter.setLoadMoreListener(new Adapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                rvList.post(new Runnable() {
                    @Override
                    public void run() {
                        // int index = recipeDetails.size() - 1;
                        pageIndex++;
                        loadMore(pageIndex);
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });


        rvList.setHasFixedSize(true);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setLayoutManager(new LinearLayoutManager(this));
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(gridLayoutManager);
        rvList.setAdapter(adapter);

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getIpInformation();
            }
        });*/
        load(pageIndex);

    }

    @Override
    public void showIpInfo(HashMap infoData) {
        textViewIp.setText("IP " + infoData.get("ip").toString());
        textViewCountry.setText("Country " + infoData.get("country").toString());
        textViewLocation.setText("Location " + infoData.get("location").toString());
    }

    @Override
    public void startLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    private void load(int index) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Recipe> call = apiService.getRecipe(index, "Omelet");
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful()) {
                    int statusCode = response.code();
                    //List<RecipeDetail> recipeDetails = response.body().getRecipeDetailsList();
                    recipeDetails.addAll(response.body().getRecipeDetailsList());
                    adapter.notifyDataChanged();
                    //recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
                } else {
                    Toast.makeText(mainActivity, "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                // Log error here since request failed
                //Log.e(TAG, t.toString());
            }
        });
    }

    private void loadMore(int index) {

        //add loading progress view
        recipeDetails.add(new RecipeDetail(""));
        adapter.notifyItemInserted(recipeDetails.size() - 1);


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Recipe> call = apiService.getRecipe(index, "Omelet");
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful()) {
                    recipeDetails.remove(recipeDetails.size() - 1);

                    List<RecipeDetail> recipeDetailsList = response.body().getRecipeDetailsList();
                    if (recipeDetailsList.size() > 0) {
                        //add loaded data
                        recipeDetails.addAll(recipeDetailsList);
                    } else {//result size 0 means there is no more data available at server
                        adapter.setMoreDataAvailable(false);
                        //telling adapter to stop calling load more as no more server data available
                        Toast.makeText(mainActivity, "No More Data Available", Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataChanged();
                } else {
                    Toast.makeText(mainActivity, "Error", Toast.LENGTH_LONG).show();
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
