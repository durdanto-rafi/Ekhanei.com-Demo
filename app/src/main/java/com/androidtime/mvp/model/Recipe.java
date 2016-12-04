package com.androidtime.mvp.model;

import java.util.List;

/**
 * Created by RAFI on 04-Dec-16.
 */

public class Recipe {
    private String title;
    private String version;
    private String href;
    private List<RecipeDetail> results;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<RecipeDetail> getRecipeDetailsList() {
        return results;
    }

    public void setRecipeDetailsList(List<RecipeDetail> recipeDetailList) {
        this.results = recipeDetailList;
    }



}
