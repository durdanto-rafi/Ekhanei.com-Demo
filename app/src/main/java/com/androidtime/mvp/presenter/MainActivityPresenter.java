package com.androidtime.mvp.presenter;

import java.util.HashMap;
import java.util.List;

import com.androidtime.mvp.interfaces.MainActivityView;
import com.androidtime.mvp.interfaces.OnDataProcess;
import com.androidtime.mvp.interfaces.OnRequestComplete;
import com.androidtime.mvp.model.InvokeApi;
import com.androidtime.mvp.model.RecipeDetail;
import com.androidtime.mvp.model.InvokeRecipeApi;


public class MainActivityPresenter {
    MainActivityView view;

    public MainActivityPresenter(MainActivityView view) {
        this.view = view;
    }


    public void getRecipeData(int pageIndex, String query) {
        view.startLoading();
        new InvokeRecipeApi(view.getAppContext(), pageIndex, query, new OnDataProcess() {
            @Override
            public void OnDataProcess(List<RecipeDetail> recipeDetails) {
                view.stopLoading();
                view.load(recipeDetails);
            }
        });
    }
}
