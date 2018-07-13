package com.nverno.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.RecipeStepDetailActivity;
import com.nverno.bakingtime.adapter.RecipeStepOnClickHandler;
import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;


public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeStepOnClickHandler {

    private static final String RECIPE_ID = "RECIPE_ID";
    private static final String STEP_ID = "STEP_ID";

    private boolean mSmallLayout;

    private RecipeViewModel recipeViewModel;

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
        } else {
            initViewModel();
        }

        if (findViewById(R.id.divider_vertical_constraint) != null) {
            mSmallLayout = true;
        }

    }

    private void initViewModel() {
        recipeViewModel = ViewModelProviders.of(this)
                .get(RecipeViewModel.class);
    }

    private void initViewModel(int recipeId) {
        recipeViewModel = ViewModelProviders.of(this)
                .get(RecipeViewModel.class);

        recipeViewModel.setSelectedRecipe(recipeId);

        if (recipeViewModel.getSelectedRecipeStep().getValue() == null) {
            recipeViewModel.setSelectedRecipeStep(0);
        }
    }

    public void onStepClick(Step step) {
        if (mSmallLayout) {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);

            if (step != null) {
                intent.putExtra(RECIPE_ID, step.getRecipeId());
                intent.putExtra(STEP_ID, step.getStep());
            }

            startActivity(intent);
        }
    }
}
