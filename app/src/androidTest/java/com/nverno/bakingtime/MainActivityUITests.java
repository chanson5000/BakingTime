package com.nverno.bakingtime;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import com.nverno.bakingtime.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityUITests {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void onMainActivityView_RecyclerViewIsDisplayed() {
        onView(withId(R.id.recipe_card_recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void clickViewItem_OpensRecipeDetailActivity() {
        onView(withId(R.id.recipe_card_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.static_text_recipe_steps_ingredients))
                .check(matches(isDisplayed()))
                .check(matches(withText("Ingredients")));
        onView(withId(R.id.recipe_steps_recycler_view))
                .check(matches(isDisplayed()));
    }
}
