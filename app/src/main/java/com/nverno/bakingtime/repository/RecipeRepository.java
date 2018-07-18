package com.nverno.bakingtime.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nverno.bakingtime.model.Ingredient;
import com.nverno.bakingtime.model.Recipe;
import com.nverno.bakingtime.model.Step;
import com.nverno.bakingtime.util.AppExecutors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeRepository {

    private static final String LOG_TAG = RecipeRepository.class.getSimpleName();
    private static boolean sDatabaseUpdated;

    private final RecipeDatabase mRecipeDatabase;
    private final Context mContext;

    public RecipeRepository(Context context) {
        mRecipeDatabase = RecipeDatabase.getInstance(context);
        mContext = context;
        loadData();
    }

    public LiveData<Recipe> getRecipeById(int recipeId) {
        return mRecipeDatabase.recipeDao().getRecipeById(recipeId);
    }

    public LiveData<List<Recipe>> getAll() {
        return mRecipeDatabase.recipeDao().getAll();
    }

    public LiveData<List<Ingredient>> getIngredientsForRecipe(int recipeId) {
        return mRecipeDatabase.ingredientDao().getIngredientsForRecipe(recipeId);
    }

    public LiveData<List<Ingredient>> getAllIngredients() {
        return mRecipeDatabase.ingredientDao().getAll();
    }

    public LiveData<List<Step>> getStepsForRecipe(int recipeId) {
        return mRecipeDatabase.stepDao().getStepsForRecipe(recipeId);
    }

    private void loadData() {

        // Check to see if database has already been updated.
        if (sDatabaseUpdated) {
            Log.d(LOG_TAG, "Skipped data fetched, already fetched.");
            return;
        }

        // Check to see if network is available.
        if (networkNotAvailable(mContext)) {
            Log.d(LOG_TAG, "Skipping data fetch, network not available.");
            return;
        }

        final RemoteData remoteData = getRemoteData(RemoteData.class);

        Call<List<Recipe>> call = remoteData.recipes();

        // Attempting to retrieve our data from the internet.
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call,
                                   @NonNull Response<List<Recipe>> response) {

                switch (response.code()) {

                    // Server code is "Not Found"
                    case 404:
                        Log.e(LOG_TAG, "Server return \"Not Found\" error.");
                        break;

                    // Server code is successful.
                    case 200:
                        final List<Recipe> recipes = response.body();

                        if (recipes != null && !recipes.isEmpty()) {
                            final List<Recipe> modRecipes = fixMissingData(recipes);
                            Log.d(LOG_TAG, "Successfully fetched data.");

                            // Updating the recipe database with our internet data in new thread.
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    // Using "fixMissingData" for injecting images that weren't included.
                                    mRecipeDatabase.recipeDao().insertMany(fixMissingData(modRecipes));
                                }
                            });

                            // Iterating through the recipe object to insert ingredients and
                            // steps data into the database.
                            for (Recipe recipe : recipes) {

                                final List<Ingredient> ingredients = recipe.getIngredients();

                                final List<Step> steps = fixStepIndexes(recipe.getSteps());

                                for (Ingredient ingredient : ingredients) {
                                    ingredient.setRecipeId(recipe.getId());
                                }

                                for (Step step : steps) {
                                    step.setRecipeId(recipe.getId());
                                }

                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRecipeDatabase.ingredientDao().insertMany(ingredients);
                                        mRecipeDatabase.stepDao().insertMany(steps);
                                    }
                                });
                            }
                            sDatabaseUpdated = true;
                        } else {
                            Log.d(LOG_TAG, "Failed to fetch internet data: empty response.");
                        }
                        break;
                    default:
                        Log.e(LOG_TAG, "Failed to fetch internet data.");
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.e(LOG_TAG, "Failed to fetch internet data.");
            }
        });
    }

    private List<Recipe> fixMissingData(List<Recipe> internetRecipes) {
        String localRecipeData;

        // Populate localRecipeData for conversion to JSON Array.
        try {
            // Our existing local version of the data for custom or missing data.
            InputStream is = mContext.getAssets().open("baking.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            localRecipeData = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        // Converting to the JSON Array for iteration.
        try {
            JSONArray localRecipesArray = new JSONArray(localRecipeData);

            int length = localRecipesArray.length();

            // Iterating through our custom local data and the internet data to make any
            // changes we would like.
            for (int i = 0; i < length; i++) {
                JSONObject localRecipe = localRecipesArray.getJSONObject(i);

                // Adding custom images in this case.
                // Iterates through all items in list until it finds matching Id.
                // Note: Not using .get(Index) in case order has somehow changed,
                // and not using .stream() because of API level.
                for (Recipe internetRecipe : internetRecipes) {
                    if (internetRecipe.getId() == localRecipe.getInt("id")) {
                        internetRecipe.setImage(localRecipe.getString("image"));
                    }
                }
            }

            return internetRecipes;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Step> fixStepIndexes(List<Step> steps) {
        for (int stepOrder = 0; stepOrder < steps.size(); stepOrder++) {
            if (steps.get(stepOrder).getStepNumber() != stepOrder) {
                steps.get(stepOrder).setStepNumber(stepOrder);
            }
        }
        return steps;
    }

    private boolean networkNotAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) {
            return true;
        }

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo == null || !networkInfo.isConnectedOrConnecting();
    }

    private RemoteData getRemoteData(Class<RemoteData> remoteDataClass) {
        return buildRetrofit().create(remoteDataClass);
    }

    private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
