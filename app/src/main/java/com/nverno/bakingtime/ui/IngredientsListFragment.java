package com.nverno.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.adapter.IngredientsAdapter;
import com.nverno.bakingtime.model.Ingredient;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

import java.util.List;

public class IngredientsListFragment extends Fragment {

    private static String LOG_TAG = IngredientsListFragment.class.getSimpleName();

    private static final String RECIPE_ID = "RECIPE_ID";

    private Context mContext;
    private RecipeViewModel recipeViewModel;

    private IngredientsAdapter mIngredientsAdapter;

    public IngredientsListFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_ingredients_list,
                container, false);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.recycler_view_ingredients_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mRecyclerView.setHasFixedSize(true);

        mIngredientsAdapter = new IngredientsAdapter();

        mRecyclerView.setAdapter(mIngredientsAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null) {
            Log.e(LOG_TAG, "FragmentActivity was null in onActivityCreated!");
            return;
        }

        FragmentActivity activity = getActivity();

        initViewModel(activity);
    }

    private void initViewModel(FragmentActivity activity) {
        recipeViewModel = ViewModelProviders.of(activity).get(RecipeViewModel.class);

        recipeViewModel.getSelectedRecipeIngredients()
                .observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                if (ingredients != null && !ingredients.isEmpty()) {
                    mIngredientsAdapter.setIngredientsData(ingredients);
                }
            }
        });
    }

}
