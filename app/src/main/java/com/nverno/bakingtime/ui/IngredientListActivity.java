package com.nverno.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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

        Intent parentIntent = getIntent();

        if (parentIntent != null && parentIntent.hasExtra(RECIPE_ID)) {
            Bundle bundle = parentIntent.getExtras();
            if (bundle != null) {
                initViewModel(bundle.getInt(RECIPE_ID));
            }
            parentIntent.removeExtra(RECIPE_ID);
        } else {
            initViewModel();
        }
    }

    private void initViewModel() {
        setViewModel();
        setTitleText();
    }

    private void initViewModel(int recipeId) {
        setViewModel();
        mRecipeViewModel.setSelectedRecipe(recipeId);
        setTitleText();
    }

    private void setViewModel() {
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
    }

    private void setTitleText() {
        mRecipeViewModel.getSelectedRecipe().observe(this, recipe -> {
            if (recipe != null) {
                setTitle(recipe.getName() + " Ingredients");
            }
        });
    }
}
