package com.example.rappitest.network

import com.example.rappitest.data.model.ModeloGenero
import com.example.rappitest.data.model.ModeloPeliculas
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PeliculaBdService {

    @GET("movie/upcoming")
    suspend fun getProximamente(@Query("api_key")apiKey:String): ModeloPeliculas

    @GET("movie/popular")
    suspend fun getTendencia(@Query("api_key")apiKey:String):  ModeloPeliculas

    @GET("genre/movie/list")
    suspend fun getGeneros (@Query("api_key")apiKey:String):ModeloGenero
}