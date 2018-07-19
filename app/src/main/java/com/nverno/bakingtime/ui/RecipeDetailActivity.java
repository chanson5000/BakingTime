package com.nverno.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.util.FragmentViewChanger;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

public class RecipeDetailActivity extends AppCompatActivity implements FragmentViewChanger {

    private static final String RECIPE_ID = "RECIPE_ID";

    private RecipeViewModel mRecipeViewModel;
    private FragmentManager fm;

    private Fragment ingredientListFragment;
    private Fragment mediaFragment;
    private Fragment descriptionFragment;
    private View mDivider;
    private static boolean sDividerIsHidden;

    public void ingredientsView() {
        FragmentTransaction ft = fm.beginTransaction();

        if (ingredientListFragment.isHidden()) {
            ft.show(ingredientListFragment);
        }
        if (!mediaFragment.isHidden()) {
            ft.hide(mediaFragment);
        }
        if (!descriptionFragment.isHidden()) {
            ft.hide(descriptionFragment);
        }
        hideDivider();
        ft.commit();
    }

    public void detailsView() {
        FragmentTransaction ft = fm.beginTransaction();

        if (!ingredientListFragment.isHidden()) {
            ft.hide(ingredientListFragment);
        }
        if (mediaFragment.isHidden()) {
            ft.show(mediaFragment);
        }
        if (descriptionFragment.isHidden()) {
            ft.show(descriptionFragment);
        }
        showDivider();
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        fm = getSupportFragmentManager();

        ingredientListFragment = fm.findFragmentById(R.id.fragment_ingredients_list);
        mediaFragment = fm.findFragmentById(R.id.fragment_step_media);
        descriptionFragment = fm.findFragmentById(R.id.fragment_step_detail);
        mDivider = findViewById(R.id.divider_horizontal_constraint);

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

    private void hideDivider() {
        if (mDivider.getVisibility() == View.VISIBLE) {
            mDivider.setVisibility(View.GONE);
            sDividerIsHidden = true;
        }
    }

    private void showDivider() {
        if (mDivider.getVisibility() == View.GONE) {
            mDivider.setVisibility(View.VISIBLE);
            sDividerIsHidden = false;
        }
    }

    private void syncDividerState() {
        if (sDividerIsHidden) {
            hideDivider();
        }
    }

    private void initViewModel() {
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        mRecipeViewModel.getSelectedRecipe().observe(this, recipe -> {
            if (recipe != null) {
                setTitle(recipe.getName() + " Recipe");
            }
        });

        syncDividerState();
    }

    private void initViewModel(int recipeId) {
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        mRecipeViewModel.setSelectedRecipe(recipeId);
        mRecipeViewModel.setSelectedRecipeStep(0);

        FragmentTransaction ft = fm.beginTransaction();

        if (!ingredientListFragment.isHidden()) {
            ft.hide(ingredientListFragment);
        }

        ft.commit();

        mRecipeViewModel.getSelectedRecipe().observe(this, recipe -> {
            if (recipe != null) {
                setTitle(recipe.getName() + " Recipe");
            }
        });
    }
}
