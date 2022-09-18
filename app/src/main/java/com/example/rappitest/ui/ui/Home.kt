@file:Suppress("NAME_SHADOWING", "DEPRECATION")

package com.example.rappitest.ui.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rappitest.R
import com.example.rappitest.adaptader.AdaptadorPelicula
import com.example.rappitest.data.model.ModeloResult
import com.example.rappitest.databinding.ActivityHomeBinding
import com.example.rappitest.procesos.MostrarSeleccion
import com.example.rappitest.procesos.Preferencias
import com.example.rappitest.procesos.SpinnerData
import com.example.rappitest.viewModel.HomeViewModel

import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView


@Suppress("DEPRECATION", "DEPRECATION")
class Home : AppCompatActivity () {
    var canExitApp =false
    val preferencias= Preferencias()
    var spinnerData= SpinnerData (this)
    var mostrarSeleccion=MostrarSeleccion ()
    private val LIST_KEY_GENEROS = "PREFERENCIAS_GENERO"
    private val LIST_KEY_TENDENCIA = "PREFERENCIAS_TENDENCIA"
    private val LIST_KEY_PROXIMAMENTE = "PREFERENCIAS_PROXIMAMENTE"
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adaptadorProximamente: AdaptadorPelicula
    private lateinit var adaptadorTendencia: AdaptadorPelicula
    private lateinit var adaptadorRecomendados: AdaptadorPelicula
    var idiomas = mutableListOf<String>()
    var anos = mutableListOf<String>()



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //incializamos componentes
        ini()

        //conectamos el ViewHolder con la vista
        iniViewModel()

        //inicializamos los spinner para que ulitilizen la informacion actual de las Tendencias
        iniSpinner()

        //LISTENER o acciones en la Actividad
        accionesComponentes()

        Thread.sleep(1000)
        setTheme(R.style.Theme_RappiTest)

    }

    private fun accionesComponentes() {

        adaptadorProximamente.setOnClickItem {
            abriDetallesPelicula(it)
        }
        adaptadorTendencia.setOnClickItem {
            abriDetallesPelicula(it)
        }
        adaptadorRecomendados.setOnClickItem {
            abriDetallesPelicula(it)
        }


        binding.spinnerAnos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                adaptadorRecomendados.dataPelicula =  mostrarSeleccion.mostrarRecomendacionAnos(selectedItem)
                adaptadorRecomendados.notifyDataSetChanged()
            }
        }

        binding.spinnerIdiomas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()

                adaptadorRecomendados.dataPelicula =  mostrarSeleccion.mostrarRecomendacionIdioma(applicationContext,selectedItem)
                adaptadorRecomendados.notifyDataSetChanged()

            }
        }
    }

    private fun iniViewModel() {

        val homeViewModel: HomeViewModel  by viewModels ()
        homeViewModel.peliculas(this)

        homeViewModel.tendeciaViewModel.observe (this,{
            adaptadorTendencia.dataPelicula = it
            adaptadorTendencia.notifyDataSetChanged()
        })
        homeViewModel.proximamenteViewModel.observe (this,{
            adaptadorProximamente.dataPelicula = it
            adaptadorProximamente.notifyDataSetChanged()
        })
        homeViewModel.recomendadasViewModel.observe (this,{
            adaptadorRecomendados.dataPelicula = it
            adaptadorRecomendados.notifyDataSetChanged()
        })
    }


    private fun iniSpinner() {
                val adaptadorIdiomas = ArrayAdapter(this, R.layout.spinner_item_rappitest,spinnerData.spinnerIdioma())
                binding.spinnerIdiomas.adapter=adaptadorIdiomas
                val adaptadorAnos= ArrayAdapter(this,R.layout.spinner_item_rappitest,spinnerData.spinnerAnos())
                binding.spinnerAnos.adapter=adaptadorAnos
        }


    private fun ini() {
        val eMovieLogo: ShimmerTextView =findViewById(R.id.shimmer_logo)
        val shimmer = Shimmer()
        shimmer.start<ShimmerTextView>(eMovieLogo)

        adaptadorProximamente= AdaptadorPelicula(emptyArray<ModeloResult>().toMutableList())
        adaptadorTendencia= AdaptadorPelicula(emptyArray<ModeloResult>().toMutableList())
        adaptadorRecomendados= AdaptadorPelicula(emptyArray<ModeloResult>().toMutableList())

        binding.recyclerViewTendencia.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewTendencia.adapter=adaptadorTendencia
        binding.recyclerViewTendencia.setHasFixedSize(true)

        binding.recyclerViewProximos.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewProximos.adapter=adaptadorProximamente
        binding.recyclerViewProximos.setHasFixedSize(true)

        binding.recycerViewRecomendado.layoutManager= StaggeredGridLayoutManager (2, LinearLayoutManager.VERTICAL)
        binding.recycerViewRecomendado.adapter=adaptadorRecomendados
        binding.recycerViewRecomendado.setHasFixedSize(true)

    }


    private fun abriDetallesPelicula(it: ModeloResult) {
        val intent = Intent(this, DetallePelicula::class.java)
        intent.putExtra("pelicula", it)
        startActivity(intent)
    }


    //presionar 2 veces para salir
    override fun onBackPressed() {
        if (!canExitApp) {
            canExitApp = true
            Toast.makeText(this, getString(R.string.presioneDosVeces), Toast.LENGTH_SHORT)
                .show()
            Handler(Looper.getMainLooper()).postDelayed({
                canExitApp = false
            }, 2000)
        } else {
            super.onBackPressed()
        }
    }
}

