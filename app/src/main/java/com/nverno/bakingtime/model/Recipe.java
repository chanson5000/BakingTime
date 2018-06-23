package com.nverno.bakingtime.model;

import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Recipe {

    @PrimaryKey
    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private List<Ingredient> ingredients;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Expose
    private List<Step> steps;

    @Expose
    private int servings;

    @Expose
    private String image;

}
