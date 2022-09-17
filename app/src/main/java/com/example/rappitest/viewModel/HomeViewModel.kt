package com.example.rappitest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rappitest.data.model.ModeloPeliculas
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class HomeViewModel : ViewModel () {

    val peliculaModel= MutableLiveData<ModeloPeliculas>()

    fun peliculas(){


    }
}