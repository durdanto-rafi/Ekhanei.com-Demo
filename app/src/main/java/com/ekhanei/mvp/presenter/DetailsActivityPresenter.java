package com.ekhanei.mvp.presenter;

import com.ekhanei.mvp.interfaces.DetailsActivityView;

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
