package com.androidtime.mvp.interfaces;

import android.content.Context;

import com.androidtime.mvp.model.RecipeDetail;

import java.util.HashMap;
import java.util.List;

public interface MainActivityView {

    void startLoading();

    void stopLoading();

    Context getAppContext();

    void load(List<RecipeDetail> recipeDetails);

    void navigateToDetailsScreen(int position);
}
