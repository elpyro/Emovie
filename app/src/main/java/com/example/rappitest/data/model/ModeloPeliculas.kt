package com.example.rappitest.data.model

data class ModeloPeliculas(
    val dates: Dates,
    val page: Int,
    val results: List<ModeloResult>,
    val total_pages: Int,
    val total_results: Int
)