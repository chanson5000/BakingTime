package com.nverno.bakingtime.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.model.Ingredient;

import java.util.List;
import java.util.Locale;

public class IngredientsAdapter
        extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {

    private List<Ingredient> mIngredients;

    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView textView;

        IngredientsAdapterViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.recipe_ingredient_detail);
        }
    }

    @NonNull
    public IngredientsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,
                                                           int viewType) {

        int layoutId = R.layout.item_ingredient_detail;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(layoutId, viewGroup, false);

        view.setFocusable(true);

        return new IngredientsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IngredientsAdapterViewHolder ingredientsAdapterViewHolder,
            int position) {

        String textToSet =
                String.format(Locale.getDefault(),
                        "%.1f", mIngredients.get(position).getQuantity())
                        + " " + mIngredients.get(position).getMeasure()
                        + " " + mIngredients.get(position).getName();

        ingredientsAdapterViewHolder.textView.setText(textToSet);
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) return 0;
        return mIngredients.size();
    }

    public void setIngredientsData(List<Ingredient> ingredientsData) {
        mIngredients = ingredientsData;
        notifyDataSetChanged();
    }
}
