package com.example.rappitest.network

import com.example.rappitest.data.model.ModeloGenero
import com.example.rappitest.data.model.ModeloPeliculas
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PeliculaBdService {

    @GET("movie/upcoming")
    fun getProximamente(@Query("api_key")apiKey:String): Call<ModeloPeliculas>

    @GET("movie/popular")
    fun getTendencia(@Query("api_key")apiKey:String): Call<ModeloPeliculas>

    @GET("movie/550/recommendations")
    fun getRecomendaciones(@Query("api_key")apiKey:String): Call<ModeloPeliculas>

    @GET("genre/movie/list")
    fun getGeneros (@Query("api_key")apiKey:String): Call<ModeloGenero>
}