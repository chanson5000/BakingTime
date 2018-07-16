package com.nverno.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.adapter.RecipeStepsAdapter;
import com.nverno.bakingtime.model.Ingredient;
import com.nverno.bakingtime.model.Recipe;
import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.util.IngredientStringHelper;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

import java.util.List;

public class StepsListFragment extends Fragment
        implements RecipeStepsAdapter.RecipeStepOnClickHandler {

    private static String LOG_TAG = StepsListFragment.class.getSimpleName();

    private static final String RECIPE_ID = "RECIPE_ID";
    private static final String STEP_ID = "STEP_ID";

    private RecipeStepsAdapter.RecipeStepOnClickHandler mClickHandler;
    private Context mContext;
    private RecipeViewModel recipeViewModel;

    private RecipeStepsAdapter mStepsAdapter;

    private boolean mWideLayout;

    private TextView mTextIngredients;

    public StepsListFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

        try {
            mClickHandler = (RecipeStepsAdapter.RecipeStepOnClickHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement RecipeStepOnClickHandler");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_steps_list,
                container, false);

        mTextIngredients = rootView.findViewById(R.id.recipe_steps_ingredients);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.recipe_steps_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mRecyclerView.setHasFixedSize(true);

        mStepsAdapter = new RecipeStepsAdapter(this);

        mRecyclerView.setAdapter(mStepsAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null) {
            Log.e(LOG_TAG, "FragmentActivity was null in onActivityCreated!");
            return;
        }

        FragmentActivity activity = getActivity();

        if (activity.findViewById(R.id.divider_vertical_constraint) != null) {
            mWideLayout = true;
        }

        initViewModel(activity);
    }

    private void initViewModel(FragmentActivity activity) {

        recipeViewModel = ViewModelProviders.of(activity).get(RecipeViewModel.class);

        recipeViewModel.getSelectedRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe != null) {
                    IngredientStringHelper.getInstance().setCurrentRecipe(getActivity(),
                            recipe);

                }
            }
        });

        recipeViewModel.getSelectedRecipeIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                if (ingredients != null && !ingredients.isEmpty()) {
                    String ingredientString = "Ingredients: " +
                            IngredientStringHelper.getInstance()
                                    .ListToFormattedString(getActivity(), ingredients);

                    mTextIngredients.setText(ingredientString);
                }
            }
        });

        recipeViewModel.getSelectedRecipeSteps().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                if (steps != null && !steps.isEmpty()) {
                    mStepsAdapter.setStepsData(steps);
                }
            }
        });
    }

    public void onStepClick(Step step) {
        if (step == null) {
            Log.e(LOG_TAG, "FragmentActivity was null in onStepClick!");
            return;
        }

        recipeViewModel.setSelectedRecipeStep(step.getStep());

        if (!mWideLayout) {
            Intent intent = new Intent(getContext(), StepDetailActivity.class);

            intent.putExtra(RECIPE_ID, step.getRecipeId());
            intent.putExtra(STEP_ID, step.getStep());

            startActivity(intent);
        }
    }
}
