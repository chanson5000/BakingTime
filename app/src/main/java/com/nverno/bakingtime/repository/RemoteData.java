package com.nverno.bakingtime.repository;

import com.nverno.bakingtime.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface RemoteData {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> recipes();

}
