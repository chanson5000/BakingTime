package com.nverno.bakingtime;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nverno.bakingtime.adapter.RecipeStepOnClickHandler;
import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;


public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeStepOnClickHandler {

    private static final String RECIPE_ID = "RECIPE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent parentIntent = getIntent();

        if (parentIntent != null
                && parentIntent.hasExtra(RECIPE_ID)) {

            Bundle bundle = parentIntent.getExtras();

            if (bundle != null) {
                initViewModel(bundle.getInt(RECIPE_ID));
            }
        }
    }

    private void initViewModel(int recipeId) {
        RecipeViewModel recipeViewModel = ViewModelProviders.of(this)
                .get(RecipeViewModel.class);

        recipeViewModel.setSelectedRecipe(recipeId);
    }

    public void onStepClick(Step step) {

    }
}
