package com.example.rappitest.ui.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
//    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var eMovieLogo: ShimmerTextView =findViewById(R.id.shimmer_logo)
        val shimmer = Shimmer()
        shimmer.start<ShimmerTextView>(eMovieLogo)

//        binding.recyclerViewProximos.adapter
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



//        val mIntent = Intent(this, DetallePelicula::class.java)
//        startActivity(mIntent)


//        homeViewModel.peliculaModel.observe(this, Observer {
//
//        })

//        lifecycleScope.launch {
//            val api_key=getString(R.string.api_key)
//
//            val peliculasUpcoming = RetrofitHelper.service.getProximamente(api_key)
//            val bodyPeliculasUpcoming= peliculasUpcoming.results
//            if (bodyPeliculasUpcoming!=null){
//            adaptadorProximamente.images= bodyPeliculasUpcoming as MutableList<ModeloResult>
//            adaptadorProximamente.notifyDataSetChanged()
//
//            val peliculasTendencia= RetrofitHelper.service.getTendencia(api_key)
//            val bodyPeliculasTendencia= peliculasTendencia.results
//            val ArrayPeliculasTendencia = bodyPeliculasTendencia as MutableList<ModeloResult>
//            adaptadorTendencia.images =  ArrayPeliculasTendencia
//            adaptadorTendencia.notifyDataSetChanged()
//
//
//                    CoroutineScope(Dispatchers.Main).launch{
//                     var idiomas = mutableListOf<String>()
//                    for ( x in ArrayPeliculasTendencia){
//                    if (!idiomas.contains("Aleman")){
//                        idiomas.add("Alemandddd")
//                    }
//                    val adaptadorIdiomas = ArrayAdapter(this, android.R.layout.simple_spinner_item,idiomas)
//                    binding.spinnerIdiomas.adapter=adaptadorIdiomas
//                    }
//
//                }
//
//
//             var peliculasRecomendadas = ArrayList<ModeloResult> ()
//
//             var  count=0
//             for( x in  ArrayPeliculasTendencia){
//                count =count + 1
//                 peliculasRecomendadas.add(x)
//                 if (count == 6){
//                     break
//                 }
//                 adaptadorRecomendados.images = peliculasRecomendadas
//                 adaptadorRecomendados.notifyDataSetChanged()
//             }
//
//
//
//            }
//        }



        //CONSUMO API
        thread {

            val api_key=getString(R.string.api_key)
//
//            //crear objeto de generos
            try {
                val generos = RetrofitHelper.service.getGeneros(api_key)
                val bodyGeneros = generos?.execute()?.body()
                ListaGenerosPeliculas.genero =bodyGeneros?.genres as ArrayList<ModeloGeneroResultado>

                crearPreferenciaGenero()

            }catch (e:Exception){
                //SI NO CARGA USAR EL ARRAYLIST GUARDADO
                cargarPreferenciaGenero()
            }


            // CONSUMO DE API PARA MOSTRAR LISTA DE PELICULAS

            try {
            val peliculasTendencia= RetrofitHelper.service.getTendencia(api_key)
                bodyPeliculasTendencia= peliculasTendencia?.execute()?.body()
                ListaTendencia.Tendencia=bodyPeliculasTendencia?.results as ArrayList<ModeloResult>

                //SI CARGA  CREAR ARRAY LIST PARA GUARDARLO COMO PREFERENCIA
                crearPreferenciaPeliculas(ListaTendencia.Tendencia,LIST_KEY_TENDENCIA)

            }catch (e:Exception){
                //SI NO CARGA USAR EL ARRAYLIST GUARDADO
                ListaTendencia.Tendencia = cargarPreferenciaPeliculas(LIST_KEY_TENDENCIA)
            }

        try{
            val peliculaUpcoming= RetrofitHelper.service.getProximamente(api_key)
            val bodyPeliculasUpcoming= peliculaUpcoming?.execute()?.body()
            ListaUpComing.Upcoming=bodyPeliculasUpcoming?.results as ArrayList<ModeloResult>

            //SI CARGA  CREAR ARRAY LIST PARA GUARDARLO COMO PREFERENCIA
            crearPreferenciaPeliculas(ListaUpComing.Upcoming,LIST_KEY_PROXIMAMENTE)
        }catch (e:Exception){
            ListaUpComing.Upcoming= cargarPreferenciaPeliculas(LIST_KEY_PROXIMAMENTE)
        }

        val peliculasRecomendadas= valoresSpinner()

            //MOSTRAR RESULTADOS
            runOnUiThread{

                    adaptadorTendencia.images =
                        ListaTendencia.Tendencia
                    adaptadorTendencia.notifyDataSetChanged()


                if (ListaUpComing.Upcoming !=null) {
                    adaptadorProximamente.images = ListaUpComing.Upcoming
                    adaptadorProximamente.notifyDataSetChanged()
                }

                adaptadorRecomendados.images = peliculasRecomendadas
                adaptadorRecomendados.notifyDataSetChanged()

                val adaptadorIdiomas = ArrayAdapter(this, R.layout.spinner_item_rappitest,idiomas)
                binding.spinnerIdiomas.adapter=adaptadorIdiomas
                val adaptadorAnos= ArrayAdapter(this,R.layout.spinner_item_rappitest,anos)
                binding.spinnerAnos.adapter=adaptadorAnos
            }


        }
        Thread.sleep(1000)
        setTheme(R.style.Theme_RappiTest)

    }

    private fun cargarPreferenciaPeliculas(key: String) : ArrayList<ModeloResult> {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val jsonString = pref.getString(key, "")
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<ModeloResult?>?>() {}.type
        val listPrefer: ArrayList<ModeloResult> = gson.fromJson(jsonString, type)
        return listPrefer
    }

    private fun cargarPreferenciaGenero() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val jsonString = pref.getString(LIST_KEY_GENEROS, "")
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<ModeloGeneroResultado?>?>() {}.type
        val listPrefer: ArrayList<ModeloGeneroResultado> = gson.fromJson(jsonString, type)
        ListaGenerosPeliculas.genero =listPrefer
    }

    private fun crearPreferenciaGenero() {
        //SI CARGA  CREAR ARRAY LIST PARA GUARDARLO COMO PREFERENCIA
        val listaPreferenciaGeneros: ArrayList<ModeloGeneroResultado> =ListaGenerosPeliculas.genero
        val gson = Gson()
        val jsonString = gson.toJson(listaPreferenciaGeneros)
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.putString(LIST_KEY_GENEROS, jsonString)
        editor.apply()

    }

    private fun crearPreferenciaPeliculas(lista: ArrayList<ModeloResult>, key:String) {
        val listaPreferencia: ArrayList<ModeloResult> = lista
        val gson = Gson()
        val jsonString = gson.toJson(listaPreferencia)
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
        try {
            var count=0
            var peliculasAnos: MutableList<ModeloResult> = ArrayList()
            val peliculaTendencia =bodyPeliculasTendencia?.results as MutableList<ModeloResult>
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
        }catch (e: Exception){

        }

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

private operator fun AdapterView.OnItemClickListener?.invoke(function: () -> Unit) {

}
