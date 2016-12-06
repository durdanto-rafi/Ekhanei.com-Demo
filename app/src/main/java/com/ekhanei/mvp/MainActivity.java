package com.ekhanei.mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.ekhanei.mvp.interfaces.MainActivityView;
import com.ekhanei.mvp.interfaces.OnRecyclerViewClickListener;
import com.ekhanei.mvp.model.RecipeDetail;
import com.ekhanei.mvp.presenter.RecipeAdapter;
import com.ekhanei.mvp.presenter.MainActivityPresenter;
import com.ekhanei.mvp.utilities.CustomToast;
import com.mancj.materialsearchbar.MaterialSearchBar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ekhanei.mvp.ConstantValues.INTENT_HREF;
import static com.ekhanei.mvp.ConstantValues.INTENT_TITLE;

public class MainActivity extends AppCompatActivity implements MainActivityView, MaterialSearchBar.OnSearchActionListener {
    MainActivityPresenter presenter;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.sbRecipe)
    MaterialSearchBar sbRecipe;
    @BindView(R.id.sfRefresh)
    SwipeRefreshLayout sfRefresh;

    List<RecipeDetail> recipeDetails;
    RecipeAdapter recipeAdapter;
    MainActivity mainActivity;
    int pageIndex = 1;
    Boolean query = false;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainActivityPresenter(this);
        mainActivity = this;

        recipeDetails = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, recipeDetails, new OnRecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
                presenter.gotoDetailsScreen(position);
            }
        });
        recipeAdapter.setLoadMoreListener(new RecipeAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                rvList.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!presenter.checkConnectivity(mainActivity)) {
                            CustomToast.T(mainActivity, getResources().getString(R.string.no_connectivity));
                            return;
                        }

                        recipeDetails.add(new RecipeDetail(""));
                        recipeAdapter.notifyItemInserted(recipeDetails.size() - 1);
                        pageIndex++;
                        if (query) {
                            presenter.getRecipeData(pageIndex, sbRecipe.getText());
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
        rvList.setAdapter(recipeAdapter);

        sbRecipe.setOnSearchActionListener(mainActivity);
        sbRecipe.enableSearch();

        sfRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!presenter.checkConnectivity(mainActivity)) {
                    CustomToast.T(mainActivity, getResources().getString(R.string.no_connectivity));
                    if (sfRefresh.isRefreshing()) {
                        sfRefresh.setRefreshing(false);
                    }
                    return;
                }

                //Refreshing data on server
                sbRecipe.setText("");
                pageIndex = 1;
                recipeDetails.clear();
                recipeAdapter.notifyDataChanged();
                rvList.swapAdapter(recipeAdapter, false);
                presenter.getRecipeData(pageIndex, "");
            }
        });

        if (!presenter.checkConnectivity(mainActivity)) {
            CustomToast.T(mainActivity, getResources().getString(R.string.no_connectivity));
            return;
        }
        presenter.getRecipeData(pageIndex, "");
    }

    @Override
    public void startLoading() {
        progressDialog = new ProgressDialog(this);
        CustomToast.T(mainActivity, getResources().getString(R.string.no_more_data));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
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
            recipeAdapter.notifyDataChanged();
        } else {
            recipeAdapter.setMoreDataAvailable(false);
            CustomToast.T(mainActivity, getResources().getString(R.string.no_more_data));
        }
        if (sfRefresh.isRefreshing()) {
            sfRefresh.setRefreshing(false);
        }
    }

    @Override
    public void navigateToDetailsScreen(int position) {
        Intent intent = new Intent(mainActivity, DetailsActivity.class);
        intent.putExtra(INTENT_TITLE, recipeDetails.get(position).getTitle());
        intent.putExtra(INTENT_HREF, recipeDetails.get(position).getHref());
        startActivity(intent);
        finish();
    }

    @Override
    public void onSearchStateChanged(boolean b) {
        
    }

    @Override
    public void onSearchConfirmed(CharSequence charSequence) {
        if (!presenter.checkConnectivity(mainActivity)) {
            CustomToast.T(mainActivity, getResources().getString(R.string.no_connectivity));
            return;
        }
        query = true;
        pageIndex = 1;
        recipeDetails.clear();
        recipeAdapter.notifyDataChanged();
        rvList.swapAdapter(recipeAdapter, false);
        presenter.getRecipeData(pageIndex, sbRecipe.getText());
    }

    @Override
    public void onButtonClicked(int i) {

    }

    @Override
    public void onBackPressed() {
        mainActivity.finish();
    }
}
