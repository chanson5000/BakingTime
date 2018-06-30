package com.nverno.bakingtime;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.nverno.bakingtime.adapter.RecipeStepOnClickHandler;
import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.ui.RecipeStepDetailFragment;
import com.nverno.bakingtime.ui.RecipeStepsFragment;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;


public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeStepOnClickHandler {

    private static final String RECIPE_ID = "RECIPE_ID";
    private static final String STEP_ID = "STEP_ID";

    private boolean mWideLayout;

    private RecipeViewModel recipeViewModel;

    FragmentManager fragmentManager;

    Fragment mRecipeStepDetailFragment;
    Fragment mRecipeStepsFragment;

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

        fragmentManager = getSupportFragmentManager();

        mRecipeStepDetailFragment = new RecipeStepDetailFragment();
        mRecipeStepsFragment = new RecipeStepsFragment();

        if (findViewById(R.id.divider_vertical_constraint) != null) {
            mWideLayout = true;
        }
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

        if (mWideLayout) {
//            fragmentManager.beginTransaction()
//                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                    .hide(mRecipeStepsFragment)
//                    .show(mRecipeStepDetailFragment)
//                    .commit();

            Intent intent = new Intent(this, RecipeStepDetailActivity.class);

//            Step selectedRecipeStep = recipeViewModel.getSelectedRecipeStep().getValue();

            if (step != null) {
                intent.putExtra(RECIPE_ID, step.getRecipeId());
                intent.putExtra(STEP_ID, step.getStep());
            }

            startActivity(intent);

        }
    }
}
