package com.nverno.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nverno.bakingtime.R;
import com.nverno.bakingtime.adapter.IngredientsAdapter;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

public class IngredientsListFragment extends Fragment {

    private FragmentActivity mFragmentActivity;
    private Context mContext;

    private IngredientsAdapter mIngredientsAdapter;
    RecipeViewModel recipeViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentActivity = getActivity();
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeViewModel = ViewModelProviders.of(mFragmentActivity).get(RecipeViewModel.class);

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

        initViewModel();
    }

    private void initViewModel() {
        recipeViewModel.getSelectedRecipeIngredients().observe(this, ingredients -> {
                    if (ingredients != null && !ingredients.isEmpty()) {
                        mIngredientsAdapter.setIngredientsData(ingredients);
                    }
                });
    }
}
