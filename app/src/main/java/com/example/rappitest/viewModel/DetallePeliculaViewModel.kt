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

//
//class LibroViewModel @Inject constructor(
//    //INYECTAMOS
//    private val getRandomCodeUsesCase : GetRandomCodeUsesCase ,
//    private  val getQuoteUseCases : GetQuoteUseCases
//): ViewModel( ) {
//
//    //DATOS DISPONIBLES PARA EL OBSERVADOR
//    var libroModel = MutableLiveData<LibroModel> ()
//
//    val isLoading= MutableLiveData<Boolean> ()
//
//
//
//
//    fun onCreate() {
//        //viewModelScope: Cuando trabajamos con corrutinas destruye la actividad
//        viewModelScope.launch {
//            isLoading.postValue(true)
//            var result = getQuoteUseCases()
//
//            if (!result.isNullOrEmpty()){
//
//                ramdomLibro()
//
//                isLoading.postValue(false)
//            }
//        }
//    }
//
//    fun ramdomLibro (){
//        // DAMOS LOS DATOS PARA QUE EL LIVEDATA SE LOS DE AL OBSERVADOR
//        isLoading.postValue(true)
//
//        val libro = getRandomCodeUsesCase()
//        libroModel.postValue(libro)
//
//        isLoading.postValue(false)
//    }
//
//
//
//}