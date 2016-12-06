package com.ekhanei.mvp.interfaces;

import android.content.Context;

import com.ekhanei.mvp.model.RecipeDetail;

import java.util.List;

public interface MainActivityView {

    void startLoading();

    void stopLoading();

    Context getAppContext();

    void load(List<RecipeDetail> recipeDetails);

    void navigateToDetailsScreen(int position);
}
