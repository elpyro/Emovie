package com.example.rappitest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rappitest.data.model.ModeloPeliculas
import com.example.rappitest.data.model.ModeloResult
import com.example.rappitest.data.objeto.ListaUpComing
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class HomeViewModel : ViewModel () {

    var recomendadasViewModel= MutableLiveData <ArrayList <ModeloResult>> ()
    var proximamenteViewModel= MutableLiveData <ArrayList <ModeloResult>> ()


    fun peliculas(){
        recomendadasViewModel.postValue(ListaUpComing.Upcoming)
        proximamenteViewModel.postValue(ListaUpComing.Upcoming)
    }
}