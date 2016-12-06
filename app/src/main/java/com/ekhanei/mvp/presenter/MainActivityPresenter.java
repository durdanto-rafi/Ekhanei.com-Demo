package com.ekhanei.mvp.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

import com.ekhanei.mvp.interfaces.MainActivityView;
import com.ekhanei.mvp.interfaces.OnDataProcess;
import com.ekhanei.mvp.model.RecipeDetail;
import com.ekhanei.mvp.model.InvokeRecipeApi;


public class MainActivityPresenter {
    MainActivityView view;
    Context context;

    public MainActivityPresenter(MainActivityView view) {
        this.view = view;
    }


    public void getRecipeData(int pageIndex, String query) {
        //view.startLoading();
        new InvokeRecipeApi(view.getAppContext(), pageIndex, query, new OnDataProcess() {
            @Override
            public void OnDataProcess(List<RecipeDetail> recipeDetails) {
                //view.stopLoading();
                view.load(recipeDetails);
            }
        });
    }

    // Checking Internet connectivity
    public boolean checkConnectivity(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public void gotoDetailsScreen(int position) {
        view.navigateToDetailsScreen(position);
    }
}
