package com.nverno.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

import butterknife.BindView;

public class RecipeStepDetailFragment extends Fragment {

    private RecipeViewModel recipeViewModel;
    private TextView mTextRecipeStepInstruction;

    private Button mButtonPreviousStep;

    private Button mButtonNextStep;

    public RecipeStepDetailFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            recipeViewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
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

        mButtonNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeViewModel.nextRecipeStep();
            }
        });

        mButtonPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeViewModel.previousRecipeStep();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recipeViewModel.getSelectedRecipeStep().observe(this, new Observer<Step>() {
            @Override
            public void onChanged(@Nullable Step step) {
                if (step != null) {
                    mTextRecipeStepInstruction.setText(step.getDescription());

                }
            }
        });
    }
}
