package com.nverno.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

public class StepDetailActivity extends AppCompatActivity {

    private static final String RECIPE_ID = "RECIPE_ID";
    private static final String STEP_ID = "STEP_ID";

    private RecipeViewModel mRecipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (findViewById(R.id.view_media_only) != null && actionBar.isShowing()) {
                actionBar.hide();
            } else if (!actionBar.isShowing()) {
                actionBar.show();
            }
        }

        // We have a saved instance state, so this activity has already loaded once.
        if (savedInstanceState != null) {
            // We should have a recipe selected as well.
            if (mRecipeViewModel.getSelectedRecipe().getValue() != null) {
                // We have a recipe selected so we don't need to do much, but check just in case
                // we don't have a recipe step selected for some reason.
                if (mRecipeViewModel.getSelectedRecipeStep().getValue() == null) {
                    mRecipeViewModel.setSelectedRecipeStep(0);
                }
                // Our view model turned out to not have any recipe selected... It may have been
                // destroyed somehow. Let's see if we can retrieve it from savedInstanceState.
            } else {
                setViewModel(savedInstanceState.getInt(RECIPE_ID),
                        savedInstanceState.getInt(STEP_ID));
            }
            setActionBarText();
            // If we don't have a saved instance state, this probably means this is the first time
            // we are starting this activity and we should have some data from our parent intent.
        } else {
            Intent parentIntent = getIntent();

            if (parentIntent != null
                    && parentIntent.hasExtra(RECIPE_ID)
                    && parentIntent.hasExtra(STEP_ID)) {

                Bundle bundle = parentIntent.getExtras();
                if (bundle != null) {
                    setViewModel(bundle.getInt(RECIPE_ID), bundle.getInt(STEP_ID));
                    setActionBarText();
                }
                // If all else fails, lets just set everything to 0 as a last resort.
            } else {
                setViewModel(0, 0);
                setActionBarText();
            }
        }
    }

    private void setViewModel(int recipeId, int stepId) {
        mRecipeViewModel.setSelectedRecipe(recipeId);
        mRecipeViewModel.setSelectedRecipeStep(stepId);
    }

    private void setActionBarText() {
        mRecipeViewModel.getSelectedRecipe().observe(this, recipe -> {
            if (recipe != null) {
                setTitle(recipe.getName() + " Recipe");
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (mRecipeViewModel.getSelectedRecipe().getValue() != null) {
            savedInstanceState.putInt(RECIPE_ID, mRecipeViewModel.getSelectedRecipe().getValue().getId());
        }
        if (mRecipeViewModel.getSelectedRecipeStep().getValue() != null) {
            savedInstanceState.putInt(STEP_ID, mRecipeViewModel.getSelectedRecipeStep().getValue().getStepNumber());
        }
    }
}
