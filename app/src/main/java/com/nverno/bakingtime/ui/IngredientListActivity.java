package com.nverno.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

public class IngredientListActivity extends AppCompatActivity {

    private static final String RECIPE_ID = "RECIPE_ID";

    private RecipeViewModel mRecipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_list);
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        if (savedInstanceState != null) {
            if (mRecipeViewModel.getSelectedRecipe().getValue() == null) {
                setViewModel(savedInstanceState.getInt(RECIPE_ID));
            }
        } else {
            Intent parentIntent = getIntent();

            if (parentIntent != null && parentIntent.hasExtra(RECIPE_ID)) {

                Bundle bundle = parentIntent.getExtras();
                if (bundle != null) {
                    setViewModel(bundle.getInt(RECIPE_ID));
                }
                parentIntent.removeExtra(RECIPE_ID);
            }
        }
        setActionBarText();
    }

    private void setViewModel(int recipeId) {
        mRecipeViewModel.setSelectedRecipe(recipeId);
    }

    private void setActionBarText() {
        mRecipeViewModel.getSelectedRecipe().observe(this, recipe -> {
            if (recipe != null) {
                setTitle(recipe.getName() + " Ingredients");
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if (mRecipeViewModel.getSelectedRecipe().getValue() != null) {
            savedInstanceState.putInt(RECIPE_ID,
                    mRecipeViewModel.getSelectedRecipe().getValue().getId());
        }
    }
}
