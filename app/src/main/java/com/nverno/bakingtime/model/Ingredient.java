package com.nverno.bakingtime.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(
        entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipeId",
        onDelete = CASCADE),
        indices = {@Index("recipeId")})
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    private int uniqueId;

    @Expose
    private int recipeId;

    @Expose
    private Double quantity;

    @Expose
    private String measure;

    @SerializedName("ingredient")
    private String name;

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String ingredient) {
        this.name = ingredient;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    @SuppressWarnings("unused")
    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }
}
