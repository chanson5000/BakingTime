package com.nverno.bakingtime.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.model.Step;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {

    private List<Step> mSteps;

    private final RecipeStepOnClickHandler mClickHandler;

    public interface RecipeStepOnClickHandler {
        void onStepClick(Step step);
    }


    public StepsAdapter(RecipeStepOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class StepsAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView textView;

        StepsAdapterViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.recipe_step_description);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Step step = mSteps.get(adapterPosition);
            mClickHandler.onStepClick(step);
        }
    }

    @NonNull
    public StepsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId = R.layout.item_step_short_description;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(layoutId, viewGroup, false);

        view.setFocusable(true);

        return new StepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapterViewHolder stepsAdapterViewHolder,
                                 int position) {
        stepsAdapterViewHolder.textView.setText(mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 0;
        return mSteps.size();
    }

    public void setStepsData(List<Step> stepsData) {
        mSteps = stepsData;
        notifyDataSetChanged();
    }
}
