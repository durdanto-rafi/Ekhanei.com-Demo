package com.androidtime.mvp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.androidtime.mvp.interfaces.ApiInterface;
import com.androidtime.mvp.interfaces.MainActivityView;
import com.androidtime.mvp.interfaces.OnRecyclerViewClickListener;
import com.androidtime.mvp.model.Recipe;
import com.androidtime.mvp.model.RecipeDetail;
import com.androidtime.mvp.presenter.Adapter;
import com.androidtime.mvp.presenter.MainActivityPresenter;
import com.androidtime.mvp.utilities.ApiClient;
import com.mancj.materialsearchbar.MaterialSearchBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MainActivityView, MaterialSearchBar.OnSearchActionListener {
    MainActivityPresenter presenter;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.searchBar)
    MaterialSearchBar searchBar;

    List<RecipeDetail> recipeDetails;
    Adapter adapter;
    MainActivity mainActivity;
    int pageIndex = 1;
    Boolean query = false;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainActivityPresenter(this);
        mainActivity = this;

        recipeDetails = new ArrayList<>();
        adapter = new Adapter(this, recipeDetails, new OnRecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
                Intent intent = new Intent(mainActivity, DetailsActivity.class);
                intent.putExtra("TITLE", recipeDetails.get(position).getTitle());
                intent.putExtra("HREF", recipeDetails.get(position).getHref());
                startActivity(intent);
                finish();
            }
        });
        adapter.setLoadMoreListener(new Adapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                rvList.post(new Runnable() {
                    @Override
                    public void run() {
                        recipeDetails.add(new RecipeDetail(""));
                        adapter.notifyItemInserted(recipeDetails.size() - 1);
                        pageIndex++;
                        if (query) {
                            presenter.getRecipeData(pageIndex, searchBar.getText());
                        } else {
                            presenter.getRecipeData(pageIndex, "");
                        }
                    }
                });
            }
        });


        rvList.setHasFixedSize(true);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setLayoutManager(new LinearLayoutManager(this));
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvList.setLayoutManager(gridLayoutManager);
        rvList.setAdapter(adapter);

        presenter.getRecipeData(pageIndex, "");

        searchBar.setOnSearchActionListener(mainActivity);
        searchBar.enableSearch();

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public void load(List<RecipeDetail> recipeDetails) {
        if (pageIndex > 1) {
            this.recipeDetails.remove(this.recipeDetails.size() - 1);
        }
        if (recipeDetails.size() > 0) {
            this.recipeDetails.addAll(recipeDetails);
            adapter.notifyDataChanged();
        } else {
            adapter.setMoreDataAvailable(false);
            Toast.makeText(mainActivity, "No More Data Available", Toast.LENGTH_LONG).show();
        }
    }


    private void loadMore(int index, String query) {
        //add loading progress view
        recipeDetails.add(new RecipeDetail(""));
        adapter.notifyItemInserted(recipeDetails.size() - 1);


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Recipe> call = apiService.getRecipe(index, query);
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful()) {
                    recipeDetails.remove(recipeDetails.size() - 1);
                    List<RecipeDetail> recipeDetailsList = response.body().getRecipeDetailsList();
                    if (recipeDetailsList.size() > 0) {
                        recipeDetails.addAll(recipeDetailsList);
                        adapter.setMoreDataAvailable(false);
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

    @Override
    public void onSearchStateChanged(boolean b) {
        if (!b)
            searchBar.enableSearch();
    }

    @Override
    public void onSearchConfirmed(CharSequence charSequence) {
        query = true;
        pageIndex = 1;
        recipeDetails.clear();
        adapter.notifyDataChanged();
        rvList.swapAdapter(adapter, false);
        presenter.getRecipeData(pageIndex, searchBar.getText());
    }

    @Override
    public void onButtonClicked(int i) {

    }
}
