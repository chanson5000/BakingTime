package com.nverno.bakingtime.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.repository.StepRepository;

import java.util.List;

public class StepViewModel extends AndroidViewModel {

    private LiveData<List<Step>> steps;

    private StepRepository stepRepository;

    public StepViewModel(@NonNull Application app) {
        super(app);

        stepRepository = new StepRepository(this.getApplication());

        steps = new MutableLiveData<>();
    }

    public LiveData<List<Step>> getStepsForRecipe() {
        return steps;
    }

    public void setSelectedRecipe(int recipeId) {
        steps = stepRepository.getStepsForRecipe(recipeId);
    }
}
