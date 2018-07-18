package com.nverno.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String RECIPE_ID = "RECIPE_ID";

    private RecipeViewModel mRecipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent parentIntent = getIntent();

        if (parentIntent != null && parentIntent.hasExtra(RECIPE_ID)) {
            Bundle bundle = parentIntent.getExtras();
            if (bundle != null) {
                initViewModel(bundle.getInt(RECIPE_ID));
            }
            // Remove extras to prevent resetting our ViewModel.
            parentIntent.removeExtra(RECIPE_ID);

        } else {
            initViewModel();
        }
    }

    private void initViewModel() {
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        mRecipeViewModel.getSelectedRecipe().observe(this, recipe -> {
            if (recipe != null) {
                setTitle(recipe.getName() + " Recipe");
            }
        });
    }

    private void initViewModel(int recipeId) {
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        mRecipeViewModel.setSelectedRecipe(recipeId);
        mRecipeViewModel.setSelectedRecipeStep(0);

        mRecipeViewModel.getSelectedRecipe().observe(this, recipe -> {
            if (recipe != null) {
                setTitle(recipe.getName() + " Recipe");
            }
        });
    }
}
