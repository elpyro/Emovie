package com.example.rappitest.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rappitest.R
import com.example.rappitest.data.model.ModeloGeneroResultado
import com.example.rappitest.data.model.ModeloPeliculas
import com.example.rappitest.data.model.ModeloResult
import com.example.rappitest.data.objeto.ListaGenerosPeliculas
import com.example.rappitest.data.objeto.ListaRecomendadas
import com.example.rappitest.data.objeto.ListaTendencia
import com.example.rappitest.data.objeto.ListaUpComing
import com.example.rappitest.network.RetrofitHelper
import com.example.rappitest.procesos.ApiConsumo
import com.example.rappitest.procesos.Preferencias
import com.example.rappitest.procesos.SeisRecomendaciones
import com.example.rappitest.procesos.SpinnerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiConsumo:ApiConsumo,
    private val seisRecomendaciones: SeisRecomendaciones,
    private val preferencias: Preferencias,
    private val spinnerData: SpinnerData
)  : ViewModel () {
    private val LIST_KEY_GENEROS = "PREFERENCIAS_GENERO"
    private val LIST_KEY_TENDENCIA = "PREFERENCIAS_TENDENCIA"
    private val LIST_KEY_PROXIMAMENTE = "PREFERENCIAS_PROXIMAMENTE"
    var recomendadasViewModel= MutableLiveData <ArrayList <ModeloResult>> ()
    var proximamenteViewModel= MutableLiveData <ArrayList <ModeloResult>> ()
    var tendeciaViewModel= MutableLiveData <ArrayList <ModeloResult>> ()
    var idiomasViewModel=MutableLiveData <List <String>> ()
    var anosViewModel=MutableLiveData <List <String>> ()

    fun peliculas(context:Context){

        viewModelScope.launch{

            val api_key = context.getString(R.string.api_key)


            //CREA LOS ARRAYLIST COMO OBJETOS
            //SI ALGUNA API NO CARGA USAR EL ARRAYLIST GUARDADO
            //GUARDA EL ARRAYLIST COMO PREFERNCIA CUANDO CARGA EXITOSAMENTE
            try {

                val generos = RetrofitHelper.service.getGeneros(api_key)
                ListaGenerosPeliculas.genero =generos?.genres as ArrayList<ModeloGeneroResultado>

                preferencias.crearPreferencia(context, ListaGenerosPeliculas.genero ,LIST_KEY_GENEROS)
            }catch (e:Exception){

                ListaGenerosPeliculas.genero = preferencias.cargarPreferencia(context,LIST_KEY_GENEROS) as ArrayList<ModeloGeneroResultado>
            }

            try {
                val peliculasTendencia= RetrofitHelper.service.getTendencia(api_key)
                ListaTendencia.Tendencia=peliculasTendencia?.results as ArrayList<ModeloResult>

                tendeciaViewModel.postValue(peliculasTendencia?.results as ArrayList<ModeloResult>)
                preferencias.crearPreferencia(context, ListaTendencia.Tendencia ,LIST_KEY_TENDENCIA)

            }catch (e:Exception){
                ListaTendencia.Tendencia = preferencias.cargarPreferencia(context, LIST_KEY_TENDENCIA) as ArrayList<ModeloResult>
            }

            try{
                val peliculaUpcoming= RetrofitHelper.service.getProximamente(api_key)
                ListaUpComing.Upcoming=peliculaUpcoming?.results as ArrayList<ModeloResult>

                preferencias.crearPreferencia(context, ListaUpComing.Upcoming,LIST_KEY_PROXIMAMENTE)
            }catch (e:Exception){
                ListaUpComing.Upcoming= preferencias.cargarPreferencia(context,LIST_KEY_PROXIMAMENTE) as ArrayList<ModeloResult>
            }

            //calcular y mostrar solo 6 peliculas recomendadas tal se requiere en la prueba
            seisRecomendaciones.SeisPeliculas()
            val peliculasRecomendadas= ListaRecomendadas.SeisRecomendadas

            //        //Asignando valores a los observers
            recomendadasViewModel.postValue(peliculasRecomendadas)
            proximamenteViewModel.postValue(ListaUpComing.Upcoming)
            tendeciaViewModel.postValue(ListaTendencia.Tendencia)
            anosViewModel.postValue(spinnerData.spinnerAnos())
            idiomasViewModel.postValue(spinnerData.spinnerIdioma(context))
        }


    }
}