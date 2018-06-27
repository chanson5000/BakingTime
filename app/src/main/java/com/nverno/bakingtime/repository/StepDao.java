package com.nverno.bakingtime.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.nverno.bakingtime.model.Step;

import java.util.List;

@Dao
public interface StepDao {

    @Query("SELECT * FROM step WHERE recipeId IS :id ORDER BY step ASC")
    LiveData<List<Step>> getStepsForRecipe(int id);

}
