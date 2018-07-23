package com.nverno.bakingtime;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.nverno.bakingtime.ui.RecipeDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityUITests {

    @Rule
    public final ActivityTestRule<RecipeDetailActivity> recipeDetailActivityRule
            = new ActivityTestRule<>(RecipeDetailActivity.class);

    @Test
    public void onRecipeDetailViewLargeLayout_LargeLayoutViewTagExists() {
        if (recipeDetailActivityRule.getActivity().getApplicationContext()
                .getResources().getConfiguration().screenWidthDp >= 600) {

            onView(withId(R.id.is_sw600dp))
                    .check(matches(withEffectiveVisibility(Visibility.GONE)));
        }
    }

    @Test
    public void onRecipeDetailView_DisplaysStepsListFragment() {
        onView(withId(R.id.fragment_steps_list))
                .check(matches(isDisplayed()));

        onView(withId(R.id.static_text_recipe_steps_ingredients))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recipe_steps_recycler_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void onRecipeDetailViewLarge_DisplaysMediaFragment() {
        if (recipeDetailActivityRule.getActivity().getApplicationContext()
                .getResources().getConfiguration().screenWidthDp >= 600) {

            onView(withId(R.id.fragment_step_media));
        }
    }

    @Test
    public void onRecipeDetailViewLarge_DisplayStepDescriptionFragment() {
        if (recipeDetailActivityRule.getActivity().getApplicationContext()
            .getResources().getConfiguration().screenWidthDp >= 600) {

            onView(withId(R.id.fragment_step_description))
                    .check(matches(isDisplayed()));

            onView(withId(R.id.button_previous_step))
                    .check(matches(isDisplayed()));

            onView(withId(R.id.button_next_step))
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void onRecipeDetailViewLarge_InitialIngredientsListStateIsHidden() {
        if (recipeDetailActivityRule.getActivity().getApplicationContext()
                .getResources().getConfiguration().screenWidthDp >= 600) {

            onView(withId(R.id.fragment_ingredients_list))
                    .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)));
        }
    }

}
