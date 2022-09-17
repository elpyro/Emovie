package com.example.rappitest.data.objeto

import com.example.rappitest.data.model.ModeloGeneroResultado

object ListaGenerosPeliculas {

    var genero = ArrayList<ModeloGeneroResultado> ()

    fun verificarGeneroPelicula(genreIds: List<Int>): String{
        var nombreGenero = "•  "
        for (x in genero){
            if (genreIds.contains(x.id)) nombreGenero= nombreGenero + x.name +"  •  "
        }
        return  nombreGenero
    }
}