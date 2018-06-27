package com.nverno.bakingtime.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.nverno.bakingtime.model.Step;

import java.util.List;

public class StepRepository {

    private static final String LOG_TAG = StepRepository.class.getSimpleName();

    private final StepDatabase stepDatabase;

    private final Context mContext;

    public StepRepository(Context context) {
        stepDatabase = StepDatabase.getInstance(context);

        mContext = context;


    }

    public LiveData<List<Step>> getStepsForRecipe(int recipeId) {
        return stepDatabase.StepDao().getStepsForRecipe(recipeId);
    }
}
