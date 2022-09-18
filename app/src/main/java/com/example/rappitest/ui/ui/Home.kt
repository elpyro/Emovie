package com.example.rappitest.ui.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
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
import com.example.rappitest.viewModel.DetallePeliculaViewModel
import com.example.rappitest.viewModel.HomeViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView
import java.lang.reflect.Type
import kotlin.concurrent.thread


class Home : AppCompatActivity () {
    var canExitApp =false

    private val LIST_KEY_GENEROS = "PREFERENCIAS_GENERO"
    private val LIST_KEY_TENDENCIA = "PREFERENCIAS_TENDENCIA"
    private val LIST_KEY_PROXIMAMENTE = "PREFERENCIAS_PROXIMAMENTE"
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adaptadorProximamente: AdaptadorPelicula
    private lateinit var adaptadorTendencia: AdaptadorPelicula
    private lateinit var adaptadorRecomendados: AdaptadorPelicula
    var idiomas = mutableListOf<String>()
    var anos = mutableListOf<String>()
    var bodyPeliculasTendencia: ModeloPeliculas? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ini()

        adaptadorProximamente?.setOnClickItem {
            abriDetalles(it)
        }
        adaptadorTendencia.setOnClickItem {
            abriDetalles(it)
        }
        adaptadorRecomendados.setOnClickItem {
            abriDetalles(it)
        }


        binding.spinnerAnos?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                mostrarRecomendacionAnos(selectedItem)
            }
        }

        binding.spinnerIdiomas?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                mostrarRecomendacionIdioma(selectedItem)
            }
        }

        consumoApi()


        Thread.sleep(1000)
        setTheme(R.style.Theme_RappiTest)

    }

    private fun consumoApi() {
        thread {

            val api_key=getString(R.string.api_key)
//
//            //crear objeto de generos
            try {
                val generos = RetrofitHelper.service.getGeneros(api_key)
                val bodyGeneros = generos?.execute()?.body()
                ListaGenerosPeliculas.genero =bodyGeneros?.genres as ArrayList<ModeloGeneroResultado>
                //crearPreferenciaGenero()
                crearPreferencia(ListaGenerosPeliculas.genero as ArrayList<String> ,LIST_KEY_GENEROS)
            }catch (e:Exception){
                //SI NO CARGA USAR EL ARRAYLIST GUARDADO
                //     cargarPreferenciaGenero()
                ListaGenerosPeliculas.genero = cargarPreferencia(LIST_KEY_GENEROS) as ArrayList<ModeloGeneroResultado>
            }


            // CONSUMO DE API PARA MOSTRAR LISTA DE PELICULAS

            try {
                val peliculasTendencia= RetrofitHelper.service.getTendencia(api_key)
                bodyPeliculasTendencia= peliculasTendencia?.execute()?.body()
                ListaTendencia.Tendencia=bodyPeliculasTendencia?.results as ArrayList<ModeloResult>

                //SI CARGA  CREAR ARRAY LIST PARA GUARDARLO COMO PREFERENCIA
                crearPreferencia(ListaTendencia.Tendencia as ArrayList<String>,LIST_KEY_TENDENCIA)

            }catch (e:Exception){
                //SI NO CARGA USAR EL ARRAYLIST GUARDADO
                ListaTendencia.Tendencia = cargarPreferencia(LIST_KEY_TENDENCIA) as ArrayList<ModeloResult>
            }

            try{
                val peliculaUpcoming= RetrofitHelper.service.getProximamente(api_key)
                val bodyPeliculasUpcoming= peliculaUpcoming?.execute()?.body()
                ListaUpComing.Upcoming=bodyPeliculasUpcoming?.results as ArrayList<ModeloResult>

                //SI CARGA  CREAR ARRAY LIST PARA GUARDARLO COMO PREFERENCIA
                crearPreferencia(ListaUpComing.Upcoming as ArrayList<String>,LIST_KEY_PROXIMAMENTE)
            }catch (e:Exception){
                ListaUpComing.Upcoming= cargarPreferencia(LIST_KEY_PROXIMAMENTE) as ArrayList<ModeloResult>
            }

            val peliculasRecomendadas= valoresSpinner()

            //MOSTRAR RESULTADOS EN RECYCLERVIEW Y SPINNER
            runOnUiThread{

                adaptadorTendencia.images = ListaTendencia.Tendencia
                adaptadorTendencia.notifyDataSetChanged()

                adaptadorProximamente.images = ListaUpComing.Upcoming
                adaptadorProximamente.notifyDataSetChanged()

                adaptadorRecomendados.images = peliculasRecomendadas
                adaptadorRecomendados.notifyDataSetChanged()

                val adaptadorIdiomas = ArrayAdapter(this, R.layout.spinner_item_rappitest,idiomas)
                binding.spinnerIdiomas.adapter=adaptadorIdiomas
                val adaptadorAnos= ArrayAdapter(this,R.layout.spinner_item_rappitest,anos)
                binding.spinnerAnos.adapter=adaptadorAnos
            }


        }
    }

    private fun ini() {
        var eMovieLogo: ShimmerTextView =findViewById(R.id.shimmer_logo)
        val shimmer = Shimmer()
        shimmer.start<ShimmerTextView>(eMovieLogo)

        adaptadorProximamente= AdaptadorPelicula(emptyArray<ModeloResult>().toMutableList())
        adaptadorTendencia= AdaptadorPelicula(emptyArray<ModeloResult>().toMutableList())
        adaptadorRecomendados= AdaptadorPelicula(emptyArray<ModeloResult>().toMutableList())

        binding.recyclerViewTendencia.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewTendencia.adapter=adaptadorTendencia
        binding.recyclerViewTendencia.setHasFixedSize(true);

        binding.recyclerViewProximos.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerViewProximos.adapter=adaptadorProximamente
        binding.recyclerViewProximos.setHasFixedSize(true);

        binding.recycerViewRecomendado.layoutManager= StaggeredGridLayoutManager (2, LinearLayoutManager.VERTICAL)
        binding.recycerViewRecomendado.adapter=adaptadorRecomendados
        binding.recycerViewRecomendado.setHasFixedSize(true);

    }

    private fun cargarPreferencia(key: String) : ArrayList<String> {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val jsonString = pref.getString(key, "")
        val gson = Gson()
        val listPrefer: ArrayList<String>
        val type: Type
        if (key.equals("PREFERENCIAS_GENERO")){
             type = object : TypeToken<ArrayList<ModeloGeneroResultado?>?>() {}.type
            listPrefer= gson.fromJson(jsonString, type)
        }else{
             type = object : TypeToken<ArrayList<ModeloResult?>?>() {}.type
            listPrefer= gson.fromJson(jsonString, type)
        }
        return listPrefer
    }


    private fun crearPreferencia(lista: ArrayList<String>, key:String) {
        val gson = Gson()
        val jsonString = gson.toJson(lista)
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.putString(key, jsonString)
        editor.apply()
    }

    private fun valoresSpinner() :ArrayList<ModeloResult> {

        //AGREGA 6 PELICULAS MAXIMO A LA LISTA DE RECOMENDADOS
        var peliculasRecomendadas = ArrayList<ModeloResult> ()
        var  count=0
        for( x in  ListaTendencia.Tendencia ){
            count =count + 1
            if (count > 7){
                peliculasRecomendadas.add(x)
            }

            // ESCANEA LA  DATA CREAR PARA SPINNER DE IDIOMAS Y AÑOS SEGUN LAS PELICULAS EN TENDENCIA

            for ( x in ListaTendencia.Tendencia) {
                var idiomaPelicula: String? =null
                if (x.original_language.equals("es")) idiomaPelicula=getString(R.string.espanol)
                if (x.original_language.equals("en")) idiomaPelicula=getString(R.string.ingles)
                if (x.original_language.equals("de")) idiomaPelicula=getString(R.string.ruso)
                if (x.original_language.equals("ja")) idiomaPelicula=getString(R.string.japones)
                if (x.original_language.equals("ko")) idiomaPelicula=getString(R.string.koreano)
                if (x.original_language.equals("it")) idiomaPelicula=getString(R.string.italiano)

                if (idiomaPelicula!= null && !idiomas.contains(idiomaPelicula)) {
                    idiomas.add(idiomaPelicula)
                }
                var anoPelicula=x.release_date.toString().substring(0,4)
                if (!anos.contains(anoPelicula)) {
                    anos.add(anoPelicula)
                }
            }

        }
        return peliculasRecomendadas
    }

    private fun mostrarRecomendacionAnos(selectedItem: String) {

            var count=0
            var peliculasAnos: MutableList<ModeloResult> = ArrayList()
            val peliculaTendencia =ListaTendencia.Tendencia
            for ( x in peliculaTendencia) {
                //AGREGA 6 PELICULAS MAXIMO A LA LISTA
                if (count == 6){
                    break
                }
                if (selectedItem.equals(x.release_date.toString().substring(0,4)))  {
                    peliculasAnos.add(x)
                    count =count + 1
                }
            }
            //Actualizar recycler
            adaptadorRecomendados.images = peliculasAnos
            adaptadorRecomendados.notifyDataSetChanged()

    }


    private fun mostrarRecomendacionIdioma(selectedItem: String  ) {

            var count=0
            var peliculasIdioma: ArrayList<ModeloResult> = ArrayList()
            var peliculaTendencia =ListaTendencia.Tendencia
            for ( x in peliculaTendencia) {
                //AGREGA 6 PELICULAS MAXIMO A LA LISTA
                if (count == 6){
                    break
                }

                var idioma:String="Otro"

                if (selectedItem.equals(getString(R.string.espanol))) idioma="es"
                if (selectedItem.equals(getString(R.string.ingles))) idioma="en"
                if (selectedItem.equals(getString(R.string.ruso))) idioma="de"
                if (selectedItem.equals(getString(R.string.japones))) idioma="ja"
                if (selectedItem.equals(getString(R.string.koreano))) idioma="ko"
                if (selectedItem.equals(getString(R.string.italiano))) idioma="it"

                if (x.original_language.equals(idioma)){
                    peliculasIdioma.add(x)
                    count= count+1
                }

            }
        //Actualizar recycler
        adaptadorRecomendados.images = peliculasIdioma
        adaptadorRecomendados.notifyDataSetChanged()


        }



    private fun abriDetalles(it: ModeloResult) {
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
            Handler().postDelayed(Runnable { canExitApp = false }, 2000)
        } else {
            super.onBackPressed()
        }
    }
}

