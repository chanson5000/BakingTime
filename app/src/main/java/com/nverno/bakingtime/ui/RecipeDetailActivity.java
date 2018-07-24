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
    private static final String STEP_ID = "STEP_ID";
    private static final String INGREDIENTS_SELECTED = "INGREDIENTS_SELECTED";

    private RecipeViewModel mRecipeViewModel;
    private FragmentManager fm;

    private Fragment ingredientListFragment;
    private Fragment mediaFragment;
    private Fragment descriptionFragment;

    private boolean mLargeLayout;
    private View mDivider;
    private Boolean mIngredientsSelected;
    private static boolean sDividerIsHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        fm = getSupportFragmentManager();

        ingredientListFragment = fm.findFragmentById(R.id.fragment_ingredients_list);
        mediaFragment = fm.findFragmentById(R.id.fragment_step_media);
        descriptionFragment = fm.findFragmentById(R.id.fragment_step_description);
        mDivider = findViewById(R.id.divider_horizontal_constraint);

        if (findViewById(R.id.is_sw600dp) != null) {
            mLargeLayout = true;
        }

        if (savedInstanceState != null) {
            if (mRecipeViewModel.getSelectedRecipe().getValue() != null) {

                if (mRecipeViewModel.getSelectedRecipeStep().getValue() == null && mLargeLayout) {
                    initialIngredientsListState();
                }

            } else {
                if (mLargeLayout) {
                    setViewModel(savedInstanceState.getInt(RECIPE_ID),
                            savedInstanceState.getInt(STEP_ID));
                    if (savedInstanceState.getBoolean(INGREDIENTS_SELECTED)) {
                        ingredientsView();
                    } else {
                        detailsView();
                    }
                } else {
                    setViewModel(savedInstanceState.getInt(RECIPE_ID));
                }
            }
            setActionBarText();
            if (mLargeLayout) {
                syncDividerState();
            }
        } else {
            Intent parentIntent = getIntent();

            if (parentIntent != null && parentIntent.hasExtra(RECIPE_ID)) {

                Bundle bundle = parentIntent.getExtras();
                if (bundle != null) {
                    setViewModel(bundle.getInt(RECIPE_ID));
                    setActionBarText();
                    if (mLargeLayout) {
                        initialIngredientsListState();
                    }
                }
            }
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
        } else {
            showDivider();
        }
    }

    private void initialIngredientsListState() {
        // On the first initialization of our activity we will set the selected step to the
        // introduction and hide the ingredients list fragment.
        mRecipeViewModel.setSelectedRecipeStep(0);

        FragmentTransaction ft = fm.beginTransaction();
        if (!ingredientListFragment.isHidden()) {
            ft.hide(ingredientListFragment);
        }
        ft.commit();
    }

    private void setViewModel(int recipeId) {
        mRecipeViewModel.setSelectedRecipe(recipeId);
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
            savedInstanceState.putInt(RECIPE_ID,
                    mRecipeViewModel.getSelectedRecipe().getValue().getId());
        }
        if (mRecipeViewModel.getSelectedRecipeStep().getValue() != null) {
            savedInstanceState.putInt(STEP_ID,
                    mRecipeViewModel.getSelectedRecipeStep().getValue().getStepNumber());
        }
        if (mIngredientsSelected != null) {
            savedInstanceState.putBoolean(INGREDIENTS_SELECTED,
                    mIngredientsSelected);
        }
    }

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
        mIngredientsSelected = true;
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
        mIngredientsSelected = false;
        showDivider();
        ft.commit();
    }
}
