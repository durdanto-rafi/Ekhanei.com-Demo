package com.androidtime.mvp.model;

/**
 * Created by RAFI on 04-Dec-16.
 */

public class RecipeDetail {

    private String href;
    private String ingredients;
    private String thumbnail;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public RecipeDetail(String title) {
        this.title = title;
    }
}
