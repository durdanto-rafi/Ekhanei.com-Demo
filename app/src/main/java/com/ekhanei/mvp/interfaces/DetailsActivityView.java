package com.ekhanei.mvp.interfaces;

import android.content.Context;

/**
 * Created by RAFI on 05-Dec-16.
 */

public interface DetailsActivityView {
    void startLoading();

    void stopLoading();

    Context getAppContext();

    void loadRecipeDetail(String url, String title);

    void navigateToMainScreen();
}
