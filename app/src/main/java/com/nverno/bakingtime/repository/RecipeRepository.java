package com.nverno.bakingtime.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nverno.bakingtime.model.Recipe;
import com.nverno.bakingtime.util.AppExecutors;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeRepository {

    private static final String LOG_TAG = RecipeRepository.class.getSimpleName();

    private final RecipeDatabase recipeDatabase;

    private final Context mContext;

    private static boolean databaseUpdated = false;

    public RecipeRepository(Context context) {
        recipeDatabase = RecipeDatabase.getInstance(context);

        mContext = context;

        loadData();
    }

    private void loadData() {

        if (networkNotAvailable(mContext)) {
            Log.d(LOG_TAG, "Skipping data fetch, network not available.");
            return;
        }

        if (databaseUpdated) {
            Log.d(LOG_TAG, "Skipped data fetched, already fetched.");
            return;
        }

        final RemoteData remoteData = getRemoteData(RemoteData.class);

        Call<List<Recipe>> call = remoteData.recipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call,
                                   @NonNull Response<List<Recipe>> response) {
                switch (response.code()) {
                    case 404:
                        Log.e(LOG_TAG, "Server return \"Not Found\" error.");
                        break;
                    case 200:
                        final List<Recipe> recipes = response.body();

                        Log.d(LOG_TAG, "Successfully fetched data.");

                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                recipeDatabase.recipeDao().insertMany(recipes);
                            }
                        });

                        databaseUpdated = true;
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

    private void fillInBlanks() {

    }

    public LiveData<List<Recipe>> getAll() {
        return recipeDatabase.recipeDao().getAll();
    }

    private boolean networkNotAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) { return true; }

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