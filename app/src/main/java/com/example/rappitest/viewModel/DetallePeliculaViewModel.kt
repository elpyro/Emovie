package com.example.rappitest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rappitest.data.model.ModeloResult
import java.io.Serializable


class DetallePeliculaViewModel: ViewModel() {

    var detallePeliculaModel= MutableLiveData<ArrayList<ModeloResult>>(arrayListOf())


    fun onCreate(pelicula: Serializable?) {
        var datosPelicula= pelicula as ModeloResult

        detallePeliculaModel.value?.add(
            ModeloResult(datosPelicula.adult,
                datosPelicula.backdrop_path,
                datosPelicula.genre_ids,
                datosPelicula.id,
                datosPelicula.original_language,
                datosPelicula.original_title,
                datosPelicula.overview,
                datosPelicula.popularity,
                datosPelicula.poster_path,
                datosPelicula.release_date,
                datosPelicula.title,
                datosPelicula.video,
                datosPelicula.vote_average,
                datosPelicula.vote_count,
            )

        )
    }
}
