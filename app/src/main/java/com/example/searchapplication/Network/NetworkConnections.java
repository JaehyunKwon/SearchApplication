package com.example.searchapplication.Network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NetworkConnections {

    // 이미지 검색
    @GET("image")
    Call<JsonObject> repoImage(
            @Header("Authorization") String app_key,
            @Query("query") String query);

    // 동영상 검색
    @GET("vclip")
    Call<JsonObject> repoVclip(
            @Header("Authorization") String app_key,
            @Query("query") String query);


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
