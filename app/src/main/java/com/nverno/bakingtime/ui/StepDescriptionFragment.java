package com.nverno.bakingtime.ui;

import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

public class StepDescriptionFragment extends Fragment {

    private RecipeViewModel recipeViewModel;
    private TextView mTextRecipeStepInstruction;
    private Button mButtonPreviousStep;
    private Button mButtonNextStep;

    private FragmentActivity mFragmentContext;

    public StepDescriptionFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            mFragmentContext = getActivity();
            recipeViewModel = ViewModelProviders.of(mFragmentContext).get(RecipeViewModel.class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_step_detail,
                container, false);

        mTextRecipeStepInstruction = rootView.findViewById(R.id.recipe_step_instructions);
        mButtonNextStep = rootView.findViewById(R.id.button_next_step);
        mButtonPreviousStep = rootView.findViewById(R.id.button_previous_step);

        mButtonNextStep.setOnClickListener(v -> recipeViewModel.nextRecipeStep());
        mButtonPreviousStep.setOnClickListener(v -> recipeViewModel.previousRecipeStep());

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recipeViewModel.getSelectedRecipeStep().observe(mFragmentContext, step -> {
            if (step != null) {
                mTextRecipeStepInstruction.setText(step.getDescription());

                recipeViewModel.getSelectedRecipeSteps().observe(mFragmentContext, steps -> {
                    if (steps != null && !steps.isEmpty()) {
                        if (step.getStepNumber() == 0) {
                            if (mButtonPreviousStep.isEnabled()) {
                                mButtonPreviousStep.setEnabled(false);
                            }
                        } else if (step.getStepNumber() == steps.size() - 1) {
                            if (mButtonNextStep.isEnabled()) {
                                mButtonNextStep.setEnabled(false);
                            }
                        } else {
                            if (!mButtonPreviousStep.isEnabled()) {
                                mButtonPreviousStep.setEnabled(true);
                            }
                            if (!mButtonNextStep.isEnabled()) {
                                mButtonNextStep.setEnabled(true);
                            }
                        }
                    }
                });
            }
        });
    }
}
