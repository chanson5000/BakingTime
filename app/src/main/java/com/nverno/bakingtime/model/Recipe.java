package com.nverno.bakingtime.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.nverno.bakingtime.util.DataConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe {
    @PrimaryKey
    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private int servings;

    @Expose
    private String image;

    @Expose
    @TypeConverters(DataConverter.class)
    private List<Ingredient> ingredients;

    @Expose
    @TypeConverters(DataConverter.class)
    private List<Step> steps;

    public List<Ingredient> getIngredients() {
        if (ingredients == null) {
            ingredients = new ArrayList<>();
        }

        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        if (steps == null) {
            steps = new ArrayList<>();
        }

        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

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

}
