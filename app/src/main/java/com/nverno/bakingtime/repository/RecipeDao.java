package com.nverno.bakingtime.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.graphics.Movie;

import com.nverno.bakingtime.model.Recipe;

import java.util.List;

public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<Recipe> recipes);

    @Delete
    void delete(Movie movie);

}
