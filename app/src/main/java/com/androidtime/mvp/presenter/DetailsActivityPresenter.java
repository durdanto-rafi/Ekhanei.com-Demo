package com.androidtime.mvp.presenter;

import android.content.Intent;

import com.androidtime.mvp.MainActivity;
import com.androidtime.mvp.interfaces.DetailsActivityView;
import com.androidtime.mvp.interfaces.MainActivityView;
import com.androidtime.mvp.interfaces.OnDataProcess;
import com.androidtime.mvp.model.InvokeRecipeApi;
import com.androidtime.mvp.model.RecipeDetail;

import java.util.List;

/**
 * Created by RAFI on 05-Dec-16.
 */

public class DetailsActivityPresenter {
    DetailsActivityView view;

    public DetailsActivityPresenter(DetailsActivityView view) {
        this.view = view;
    }

    public void getRecipeDetails(String url, String title) {
        view.startLoading();
        view.loadRecipeDetail(url, title);
    }

    public void stopProgressDialog() {
        view.stopLoading();
    }

    public void gotoMainScreen() {
        view.navigateToMainScreen();
    }
}
