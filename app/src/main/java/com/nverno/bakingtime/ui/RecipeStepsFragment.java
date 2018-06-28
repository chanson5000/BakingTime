package com.nverno.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import com.nverno.bakingtime.adapter.RecipeStepOnClickHandler;
import com.nverno.bakingtime.adapter.RecipeStepsAdapter;
import com.nverno.bakingtime.model.Ingredient;
import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

import java.util.List;

public class RecipeStepsFragment extends Fragment
        implements RecipeStepOnClickHandler {

    private RecipeStepOnClickHandler mClickHandler;
    private Context mContext;
    private RecipeViewModel recipeViewModel;

    private RecyclerView mRecyclerView;
    private RecipeStepsAdapter mStepsAdapter;

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

        if (getActivity() != null) {
            recipeViewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
        }
    }

    public RecipeStepsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.recipe_steps_fragment,
                container,
                false);

        mTextIngredients = rootView.findViewById(R.id.recipe_steps_ingredients);

        mRecyclerView = rootView.findViewById(R.id.recipe_steps_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mRecyclerView.setHasFixedSize(true);

        mStepsAdapter = new RecipeStepsAdapter(mClickHandler);

        mRecyclerView.setAdapter(mStepsAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewModel();

    }

    private void initViewModel() {
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
        recipeViewModel.setSelectedRecipeStep(step.getStep());


    }
}
