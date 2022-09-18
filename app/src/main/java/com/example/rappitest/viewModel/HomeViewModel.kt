package com.example.rappitest.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rappitest.data.model.ModeloResult
import com.example.rappitest.data.objeto.ListaRecomendadas
import com.example.rappitest.data.objeto.ListaTendencia
import com.example.rappitest.data.objeto.ListaUpComing
import com.example.rappitest.procesos.ApiConsumo
import com.example.rappitest.procesos.SeisRecomendaciones


class HomeViewModel : ViewModel () {

    var recomendadasViewModel= MutableLiveData <ArrayList <ModeloResult>> ()
    var proximamenteViewModel= MutableLiveData <ArrayList <ModeloResult>> ()
    var tendeciaViewModel= MutableLiveData <ArrayList <ModeloResult>> ()

    fun peliculas(context:Context){

        //consumo de apis
        var apiConsumo=ApiConsumo()
        val api=apiConsumo.apis(context)

        //crear lista de precomendaciones
        var seisRecomendaciones= SeisRecomendaciones()
        seisRecomendaciones.SeisPeliculas()
        val peliculasRecomendadas= ListaRecomendadas.SeisRecomendadas

        //Asignando valores a los observers
        recomendadasViewModel.postValue(peliculasRecomendadas)
        proximamenteViewModel.postValue(ListaUpComing.Upcoming)
        tendeciaViewModel.postValue(ListaTendencia.Tendencia)

    }
}