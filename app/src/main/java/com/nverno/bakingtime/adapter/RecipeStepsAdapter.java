package com.nverno.bakingtime.adapter;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.model.Step;

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsAdapterViewHolder> {

    private List<Step> steps;

    private OnClickHandler mClickHandler;

    public RecipeStepsAdapter(OnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class RecipeStepsAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView textView;

        RecipeStepsAdapterViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.recipe_step_description);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Step step = steps.get(adapterPosition);
            mClickHandler.onClick(step);
        }
    }

    @NonNull
    public RecipeStepsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId = R.layout.recipe_step_description;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(layoutId, viewGroup, false);

        view.setFocusable(true);

        return new RecipeStepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsAdapterViewHolder recipeStepsAdapterViewHolder, int position) {
        recipeStepsAdapterViewHolder.textView.setText(steps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (steps == null) return 0;
        return steps.size();
    }

    public void setStepsData(List<Step> stepsData) {
        steps = stepsData;
        notifyDataSetChanged();
    }
}
