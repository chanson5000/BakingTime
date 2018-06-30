package com.nverno.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.RecipeStepDetailActivity;
import com.nverno.bakingtime.adapter.RecipeStepOnClickHandler;
import com.nverno.bakingtime.adapter.RecipeStepsAdapter;
import com.nverno.bakingtime.model.Ingredient;
import com.nverno.bakingtime.model.Recipe;
import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

import java.util.List;

public class RecipeStepsFragment extends Fragment
        implements RecipeStepOnClickHandler {

    private RecipeStepOnClickHandler mClickHandler;
    private Context mContext;
    private static RecipeViewModel recipeViewModel;

    private RecyclerView mRecyclerView;
    private RecipeStepsAdapter mStepsAdapter;
    private Recipe mRecipe;

    private static final String RECIPE_ID = "RECIPE_ID";
    private static final String STEP_ID = "STEP_ID";

    private boolean mWideLayout;


    private TextView mTextIngredients;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

        try {
            mClickHandler = (RecipeStepOnClickHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement RecipeStepOnClickHandler");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public RecipeStepsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_steps_list,
                container,
                false);

        mTextIngredients = rootView.findViewById(R.id.recipe_steps_ingredients);

        mRecyclerView = rootView.findViewById(R.id.recipe_steps_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mRecyclerView.setHasFixedSize(true);

        mStepsAdapter = new RecipeStepsAdapter(this);

        mRecyclerView.setAdapter(mStepsAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            if (getActivity().findViewById(R.id.divider_vertical_constraint) != null) {
                mWideLayout = true;
            }
        }
        initViewModel();
    }

    private void initViewModel() {
        if (getActivity() != null) {
            recipeViewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
        }
        recipeViewModel.getSelectedRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe != null) {
                    mRecipe = recipe;
                }
            }
        });

        recipeViewModel.getSelectedRecipeIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                if (ingredients != null && !ingredients.isEmpty()) {
                    StringBuilder ingredientString = new StringBuilder();

                    ingredientString.append("Ingredients: ");

                    for (int i = 0; i < ingredients.size(); i++) {
                        if (i == ingredients.size() - 1) {
                            ingredientString.append(ingredients.get(i).getName()).append(".");
                        } else {
                            ingredientString.append(ingredients.get(i).getName()).append(", ");
                        }
                    }

                    String textToSet = ingredientString.toString();

                    mTextIngredients.setText(textToSet);
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
        if (!mWideLayout) {
            Intent intent = new Intent(getContext(), RecipeStepDetailActivity.class);

            if (step != null) {
                intent.putExtra(RECIPE_ID, step.getRecipeId());
                intent.putExtra(STEP_ID, step.getStep());
            }

            startActivity(intent);
        } else {
            recipeViewModel.setSelectedRecipeStep(step.getStep());
        }
    }
}
