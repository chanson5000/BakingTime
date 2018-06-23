package com.nverno.bakingtime.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    boolean networkNotAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) { return true; }

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo == null || !networkInfo.isConnectedOrConnecting();
    }

    RemoteData remoteData(Class<RemoteData> remoteDataClass) {
        return buildRetrofit().create(remoteDataClass);
    }

    private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


}