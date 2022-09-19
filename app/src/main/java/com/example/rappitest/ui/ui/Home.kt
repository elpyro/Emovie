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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rappitest.R
import com.example.rappitest.adaptader.AdaptadorPelicula
import com.example.rappitest.data.model.ModeloGeneroResultado
import com.example.rappitest.data.model.ModeloPeliculas
import com.example.rappitest.data.model.ModeloResult
import com.example.rappitest.data.objeto.ListaGenerosPeliculas
import com.example.rappitest.data.objeto.ListaTendencia
import com.example.rappitest.data.objeto.ListaUpComing
import com.example.rappitest.databinding.ActivityHomeBinding
import com.example.rappitest.network.RetrofitHelper
import com.example.rappitest.procesos.MostrarSeleccion
import com.example.rappitest.procesos.SpinnerData
import com.example.rappitest.viewModel.HomeViewModel
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.concurrent.thread


@Suppress("DEPRECATION", "DEPRECATION")
@AndroidEntryPoint
class Home  : AppCompatActivity () {

    @set:Inject var spinnerData: SpinnerData? = null
    @set:Inject var mostrarSeleccion: MostrarSeleccion?= null

    var canExitApp =false

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adaptadorProximamente: AdaptadorPelicula
    private lateinit var adaptadorTendencia: AdaptadorPelicula
    private lateinit var adaptadorRecomendados: AdaptadorPelicula

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

        lifecycleScope
        thread{
            runOnUiThread{

            }
     //       val api_key="c06fce03255ef224fabad691249b6019"
//
            //CREA LOS ARRAYLIST COMO OBJETOS
            //SI ALGUNA API NO CARGA USAR EL ARRAYLIST GUARDADO
            //GUARDA EL ARRAYLIST COMO PREFERNCIA CUANDO CARGA EXITOSAMENTE
//
//                val generos = RetrofitHelper.service.getGeneros(api_key)
//                val bodyGeneros = generos?.execute()?.body()
//                ListaGenerosPeliculas.genero =bodyGeneros?.genres as ArrayList<ModeloGeneroResultado>

//                //       preferencias.crearPreferencia(context, ListaGenerosPeliculas.genero ,LIST_KEY_GENEROS)


                //  ListaGenerosPeliculas.genero = preferencias.cargarPreferencia(context,LIST_KEY_GENEROS) as ArrayList<ModeloGeneroResultado>

//
//            try {
//                val peliculasTendencia= RetrofitHelper.service.getTendencia(api_key)
//                var bodyPeliculasTendencia=  peliculasTendencia.execute().body()
//                ListaTendencia.Tendencia=bodyPeliculasTendencia?.results as ArrayList<ModeloResult>
//
//                //     preferencias.crearPreferencia(context, ListaTendencia.Tendencia ,LIST_KEY_TENDENCIA)
//
//            }catch (e:Exception){
//                //      ListaTendencia.Tendencia = preferencias.cargarPreferencia(context, LIST_KEY_TENDENCIA) as ArrayList<ModeloResult>
//            }
//
//            try{
//                val peliculaUpcoming= RetrofitHelper.service.getProximamente(api_key)
//                val bodyPeliculasUpcoming= peliculaUpcoming?.execute()?.body()
//                ListaUpComing.Upcoming=bodyPeliculasUpcoming?.results as ArrayList<ModeloResult>
//
//                //    preferencias.crearPreferencia(context, ListaUpComing.Upcoming,LIST_KEY_PROXIMAMENTE)
//            }catch (e:Exception){
//                //        ListaUpComing.Upcoming= preferencias.cargarPreferencia(context,LIST_KEY_PROXIMAMENTE) as ArrayList<ModeloResult>
//            }
//
        }


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
                adaptadorRecomendados.dataPelicula =  mostrarSeleccion!!.mostrarRecomendacionAnos(selectedItem)
                adaptadorRecomendados.notifyDataSetChanged()
            }
        }

        binding.spinnerIdiomas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()

                adaptadorRecomendados.dataPelicula =  mostrarSeleccion!!.mostrarRecomendacionIdioma(applicationContext,selectedItem)
                adaptadorRecomendados.notifyDataSetChanged()

            }
        }
    }

    private fun iniViewModel() {

        val homeViewModel: HomeViewModel  by viewModels ()
        homeViewModel.peliculas(this)

        homeViewModel.tendeciaViewModel.observe (this) {
            adaptadorTendencia.dataPelicula = it
            adaptadorTendencia.notifyDataSetChanged()
        }
        homeViewModel.proximamenteViewModel.observe (this) {
            adaptadorProximamente.dataPelicula = it
            adaptadorProximamente.notifyDataSetChanged()
        }
        homeViewModel.recomendadasViewModel.observe (this) {
            adaptadorRecomendados.dataPelicula = it
            adaptadorRecomendados.notifyDataSetChanged()
        }

        homeViewModel.anosViewModel.observe(this){
        val adaptadorAnos= ArrayAdapter(this,R.layout.spinner_item_rappitest,it)
        binding.spinnerAnos.adapter=adaptadorAnos
        }

        homeViewModel.idiomasViewModel.observe(this){
        val adaptadorIdiomas = ArrayAdapter(this, R.layout.spinner_item_rappitest,it)
        binding.spinnerIdiomas.adapter=adaptadorIdiomas
        }
    }


    private fun iniSpinner() {

        }


    private fun ini() {

        val shimmer = Shimmer()
        shimmer.start<ShimmerTextView>(binding.shimmerLogo)

        adaptadorProximamente= AdaptadorPelicula(emptyArray<ModeloResult>().toMutableList())
        adaptadorTendencia= AdaptadorPelicula(emptyArray<ModeloResult>().toMutableList())
        adaptadorRecomendados= AdaptadorPelicula(emptyArray<ModeloResult>().toMutableList())

        binding.recyclerViewTendencia.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewTendencia.adapter=adaptadorTendencia
        binding.recyclerViewTendencia.setHasFixedSize(true)

        binding.recyclerViewProximos.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewProximos.adapter=adaptadorProximamente
        binding.recyclerViewProximos.setHasFixedSize(true)


        binding.recycerViewRecomendado.layoutManager= StaggeredGridLayoutManager (2, 1)
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

