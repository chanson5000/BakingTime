package com.nverno.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.adapter.StepsAdapter;
import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.util.FragmentViewChanger;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

public class StepsListFragment extends Fragment implements StepsAdapter.RecipeStepOnClickHandler {

    private static final String RECIPE_ID = "RECIPE_ID";
    private static final String STEP_ID = "STEP_ID";

    private FragmentActivity mFragmentActivity;
    private Context mContext;
    private RecipeViewModel mRecipeViewModel;
    private StepsAdapter mStepsAdapter;
    private boolean mLargeLayout;

    private FragmentViewChanger mFragmentViewChanger;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        mFragmentViewChanger = (FragmentViewChanger) context;
        mFragmentActivity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView
                = inflater.inflate(R.layout.fragment_steps_list, container, false);

        TextView mTextIngredients = rootView.findViewById(R.id.static_text_recipe_steps_ingredients);
        mTextIngredients.setOnClickListener(v -> onIngredientClick());
        RecyclerView recyclerView = rootView.findViewById(R.id.recipe_steps_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        mStepsAdapter = new StepsAdapter(this);
        recyclerView.setAdapter(mStepsAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mFragmentActivity.findViewById(R.id.is_sw600dp) != null) {
            mLargeLayout = true;
        }

        initViewModel();
    }

    private void initViewModel() {
        mRecipeViewModel = ViewModelProviders.of(mFragmentActivity).get(RecipeViewModel.class);

        mRecipeViewModel.getSelectedRecipeSteps().observe(this, steps -> {
            if (steps != null && !steps.isEmpty()) {
                mStepsAdapter.setStepsData(steps);
            }
        });
    }

    // Separate actions for these depending on the layout size.
    public void onIngredientClick() {
        if (!mLargeLayout) {
            Intent intent = new Intent(getContext(), IngredientListActivity.class);

            if (mRecipeViewModel.getSelectedRecipe().getValue() != null) {
                intent.putExtra(RECIPE_ID, mRecipeViewModel.getSelectedRecipe().getValue().getId());

                startActivity(intent);
            }
        } else {
            mFragmentViewChanger.ingredientsView();
        }
    }

    public void onStepClick(Step step) {
        // If the screen layout is not large, we will launch a new Activity.
        if (!mLargeLayout) {
            Intent intent = new Intent(getContext(), StepDetailActivity.class);

            intent.putExtra(RECIPE_ID, step.getRecipeId());
            intent.putExtra(STEP_ID, step.getStepNumber());

            startActivity(intent);
        } else {
            mFragmentViewChanger.detailsView();
            mRecipeViewModel.setSelectedRecipeStep(step.getStepNumber());
        }
    }
}
