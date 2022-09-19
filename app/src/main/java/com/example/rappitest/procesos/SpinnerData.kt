package com.example.rappitest.procesos

import android.content.Context
import com.example.rappitest.R
import com.example.rappitest.data.objeto.ListaTendencia
import javax.inject.Inject

class SpinnerData @Inject constructor () {

    var idiomas = mutableListOf<String>()
    var anos = mutableListOf<String>()

    //Evalua las peliculas en Tendencia para indicar los idiomas y años disponibles para filtrar

    fun spinnerIdioma(context: Context): List<String>  {

        for (x in ListaTendencia.Tendencia)  {
            var idiomaPelicula: String? = null
            if (x.original_language.equals("es")) idiomaPelicula = context.getString(R.string.espanol)
            if (x.original_language.equals("en")) idiomaPelicula = context.getString(R.string.ingles)
            if (x.original_language.equals("de")) idiomaPelicula = context.getString(R.string.ruso)
            if (x.original_language.equals("ja")) idiomaPelicula = context.getString(R.string.japones)
            if (x.original_language.equals("ko")) idiomaPelicula = context.getString(R.string.koreano)
            if (x.original_language.equals("it")) idiomaPelicula = context.getString(R.string.italiano)

            if (idiomaPelicula != null && !idiomas.contains(idiomaPelicula)) {
                idiomas.add(idiomaPelicula)
            }
         }
        return idiomas
    }

    fun spinnerAnos():List<String> {
        for (x in ListaTendencia.Tendencia) {
            val anoPelicula = x.release_date.substring(0, 4)
            if (!anos.contains(anoPelicula)) {
                anos.add(anoPelicula)
            }
        }
        return anos

    }

    }
