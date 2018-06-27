package com.nverno.bakingtime.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nverno.bakingtime.model.Ingredient;

import java.util.List;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM ingredient WHERE recipeId = :id")
    LiveData<List<Ingredient>> getIngredientsForRecipe(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<Ingredient> ingredients);

    @Query("SELECT * FROM ingredient")
    LiveData<List<Ingredient>> getAll();

}
