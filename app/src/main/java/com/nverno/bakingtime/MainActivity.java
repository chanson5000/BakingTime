package com.nverno.bakingtime;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.nverno.bakingtime.adapter.RecipeCardAdapter;
import com.nverno.bakingtime.adapter.RecipeCardAdapterOnClickHandler;
import com.nverno.bakingtime.model.Recipe;
import com.nverno.bakingtime.viewmodel.RecipeViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeCardAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecipeCardAdapter mRecipeCardAdapter;

    private static final String RECIPE_ID = "RECIPE_ID";

    @BindView(R.id.recipe_card_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        initViewModel();
    }

    private void initView() {
        if (findViewById(R.id.is_sw600dp) != null) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        mRecyclerView.setHasFixedSize(true);

        mRecipeCardAdapter = new RecipeCardAdapter(this, this);

        mRecyclerView.setAdapter(mRecipeCardAdapter);
    }

    private void initViewModel() {
        RecipeViewModel recipeViewModel = ViewModelProviders.of(this)
                .get(RecipeViewModel.class);

        recipeViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null && !recipes.isEmpty()) {
                mRecipeCardAdapter.setRecipeData(recipes);
                }
            }
        });
    }

    @Override
    public void onClick(Recipe recipe) {
        final Intent intent = new Intent(this, RecipeDetailActivity.class);

        intent.putExtra(RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}
