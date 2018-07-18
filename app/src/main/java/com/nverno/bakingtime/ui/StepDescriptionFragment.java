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

    private RecipeViewModel mRecipeViewModel;
    private FragmentActivity mFragmentActivity;

    private TextView mTextRecipeStepInstruction;
    private Button mBtnPreviousStep;
    private Button mBtnNextStep;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentActivity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_step_detail,
                container, false);

        mTextRecipeStepInstruction = rootView.findViewById(R.id.recipe_step_instructions);
        mBtnNextStep = rootView.findViewById(R.id.button_next_step);
        mBtnPreviousStep = rootView.findViewById(R.id.button_previous_step);
        mBtnNextStep.setOnClickListener(v -> mRecipeViewModel.nextRecipeStep());
        mBtnPreviousStep.setOnClickListener(v -> mRecipeViewModel.previousRecipeStep());

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewModel();
    }

    private void initViewModel() {
        mRecipeViewModel = ViewModelProviders.of(mFragmentActivity).get(RecipeViewModel.class);

        mRecipeViewModel.getSelectedRecipeStep().observe(this, step -> {
            if (step != null) {
                mTextRecipeStepInstruction.setText(step.getDescription());

                mRecipeViewModel.getSelectedRecipeSteps().observe(this, steps -> {
                    if (steps != null && !steps.isEmpty()) {
                        // Enable or disable Next and Previous buttons based on step number.
                        if (step.getStepNumber() == 0) {
                            if (mBtnPreviousStep.isEnabled()) {
                                mBtnPreviousStep.setEnabled(false);
                            }
                        } else if (step.getStepNumber() == steps.size() - 1) {
                            if (mBtnNextStep.isEnabled()) {
                                mBtnNextStep.setEnabled(false);
                            }
                        } else {
                            if (!mBtnPreviousStep.isEnabled()) {
                                mBtnPreviousStep.setEnabled(true);
                            }
                            if (!mBtnNextStep.isEnabled()) {
                                mBtnNextStep.setEnabled(true);
                            }
                        }
                    }
                });
            }
        });
    }
}
