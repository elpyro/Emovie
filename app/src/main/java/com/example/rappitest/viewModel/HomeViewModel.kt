package com.example.rappitest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rappitest.data.model.ModeloPeliculas


class HomeViewModel : ViewModel() {

    val peliculaModel= MutableLiveData<ModeloPeliculas>()

    fun peliculas(){


    }
}