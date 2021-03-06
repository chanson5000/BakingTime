package com.nverno.bakingtime.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@SuppressWarnings("unused")
@Entity(foreignKeys = @ForeignKey(
        entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipeId",
        onDelete = CASCADE),
        indices = {@Index("recipeId")})
public class Step {
    @PrimaryKey(autoGenerate = true)
    private int uniqueId;

    @Expose
    private int recipeId;

    @SerializedName("id")
    private int stepNumber;

    @Expose
    private String shortDescription;

    @Expose
    private String description;

    @Expose
    private String videoURL;

    @Expose
    private String thumbnailURL;

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int step) {
        this.stepNumber = step;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }
}
