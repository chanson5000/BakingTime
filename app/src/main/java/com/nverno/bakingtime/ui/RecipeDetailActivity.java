package com.nverno.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.adapter.StepsAdapter;
import com.nverno.bakingtime.model.Recipe;
import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;


public class RecipeDetailActivity extends AppCompatActivity
        implements StepsAdapter.RecipeStepOnClickHandler {

    private static final String RECIPE_ID = "RECIPE_ID";
    private static final String STEP_ID = "STEP_ID";

    private boolean mSmallLayout;

    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

//        ActionBar actionBar = getSupportActionBar();

//        if (actionBar != null) {
            // This has hidden the action bar back navigation button which I consider redundant.

//                actionBar.setDisplayHomeAsUpEnabled(false);

            // An internet resource suggested using these as well but it seems I can just rid of it
            // using only the method above... even though the documentation description kind of
            // suggests that the following methods may also have something to do with it.
            // Sort of confused on this one, maybe figure out details later but it works for now.

            // actionBar.setHomeButtonEnabled(false);
            // actionBar.setDisplayShowHomeEnabled(false);
//        }

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

    private void initViewModel(final int recipeId) {
        recipeViewModel = ViewModelProviders.of(this)
                .get(RecipeViewModel.class);

        recipeViewModel.setSelectedRecipe(recipeId);

        if (recipeViewModel.getSelectedRecipeStep().getValue() == null) {
            recipeViewModel.setSelectedRecipeStep(0);
        }

        recipeViewModel.getSelectedRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe != null) {
                    setTitle(recipe.getName() + " Recipe");
                }
            }
        });
    }

    public void onStepClick(Step step) {
        if (mSmallLayout) {
            Intent intent = new Intent(this, StepDetailActivity.class);

            if (step != null) {
                intent.putExtra(RECIPE_ID, step.getRecipeId());
                intent.putExtra(STEP_ID, step.getStep());
            }

            startActivity(intent);
        }
    }
}
