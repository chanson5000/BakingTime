package com.nverno.bakingtime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.model.Recipe;
import com.squareup.picasso.Picasso;


import java.util.List;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeCardAdapterViewHolder> {

    private final Context mContext;
    private List<Recipe> recipes;

    private final RecipeCardOnClickHandler mClickHandler;

    public RecipeCardAdapter(Context context, RecipeCardOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class RecipeCardAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView textView;
        final ImageView imageView;

        RecipeCardAdapterViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.recipe_card_text);
            imageView = view.findViewById(R.id.recipe_card_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = recipes.get(adapterPosition);
            mClickHandler.onCardClick(recipe);
        }
    }

    @NonNull
    public RecipeCardAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId = R.layout.item_recipe_card;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutId, viewGroup, false);

        view.setFocusable(true);

        return new RecipeCardAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCardAdapterViewHolder recipeCardAdapterViewHolder, int position) {
        recipeCardAdapterViewHolder.textView.setText(recipes.get(position).getName());
        String imageToInsert = recipes.get(position).getImage();
        if (!imageToInsert.isEmpty()) {
            Picasso.with(mContext).load(imageToInsert).resize(500,200)
                    .centerCrop()
                    .into(recipeCardAdapterViewHolder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        return recipes.size();
    }

    public void setRecipeData(List<Recipe> recipeData) {
        recipes = recipeData;
        notifyDataSetChanged();
    }
}
