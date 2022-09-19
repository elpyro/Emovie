package com.example.rappitest.procesos

import android.content.Context
import com.example.rappitest.R
import com.example.rappitest.data.model.ModeloResult
import com.example.rappitest.data.objeto.ListaTendencia
import javax.inject.Inject

class MostrarSeleccion @Inject constructor(){

    fun mostrarRecomendacionIdioma(context: Context, selectedItem: String  ) : MutableList<ModeloResult>{

        var count=0
        val peliculasIdioma: MutableList<ModeloResult> = ArrayList()
        val peliculaTendencia = ListaTendencia.Tendencia
        for ( x in peliculaTendencia) {
            //AGREGA 6 PELICULAS MAXIMO A LA LISTA
            if (count == 6){
                break
            }

            var idioma="Otro"

            if (selectedItem.equals(context.getString(R.string.espanol))) idioma="es"
            if (selectedItem.equals(context.getString(R.string.ingles))) idioma="en"
            if (selectedItem.equals(context.getString(R.string.ruso))) idioma="de"
            if (selectedItem.equals(context.getString(R.string.japones))) idioma="ja"
            if (selectedItem.equals(context.getString(R.string.koreano))) idioma="ko"
            if (selectedItem.equals(context.getString(R.string.italiano))) idioma="it"

            if (x.original_language.equals(idioma)){
                peliculasIdioma.add(x)
                count= count+1
            }

        }
        return peliculasIdioma

    }

    fun mostrarRecomendacionAnos(selectedItem: String) :  MutableList<ModeloResult>{

        var count=0
        val peliculasAnos: MutableList<ModeloResult> = ArrayList()
        val peliculaTendencia =ListaTendencia.Tendencia
        for ( x in peliculaTendencia) {
            //AGREGA 6 PELICULAS MAXIMO A LA LISTA
            if (count == 6){
                break
            }
            if (selectedItem.equals(x.release_date.substring(0,4)))  {
                peliculasAnos.add(x)
                count =count + 1
            }
        }

    return peliculasAnos
    }

}