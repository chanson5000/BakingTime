package com.nverno.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

public class RecipeStepDetailActivity extends AppCompatActivity {

    private static final String RECIPE_ID = "RECIPE_ID";
    private static final String STEP_ID = "STEP_ID";

    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        Intent parentIntent = getIntent();

        if (parentIntent != null
                && parentIntent.hasExtra(RECIPE_ID)
                && parentIntent.hasExtra(STEP_ID)) {

            Bundle bundle = parentIntent.getExtras();

            if (bundle != null) {
                initViewModel(bundle.getInt(RECIPE_ID), bundle.getInt(STEP_ID));
            }
        }

    }

    private void initViewModel(int recipeId, int stepId) {
        recipeViewModel = ViewModelProviders.of(this)
                .get(RecipeViewModel.class);

        recipeViewModel.setSelectedRecipe(recipeId);
        recipeViewModel.setSelectedRecipeStep(stepId);

    }

}
