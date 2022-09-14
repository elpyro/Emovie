package com.example.rappitest.ui.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rappitest.R
import com.example.rappitest.adaptader.AdaptadorPelicula
import com.example.rappitest.data.model.ModeloGenero
import com.example.rappitest.data.model.ModeloGeneroResultado
import com.example.rappitest.data.model.ModeloResult
import com.example.rappitest.data.objeto.ListaGeneros
import com.example.rappitest.databinding.ActivityHomeBinding
import com.example.rappitest.network.RetrofitHelper
import kotlin.concurrent.thread


class Home : AppCompatActivity() {
    var canExitApp =false
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adaptadorProximamente: AdaptadorPelicula
    private lateinit var adaptadorTendencia: AdaptadorPelicula
    private lateinit var adaptadorRecomendados: AdaptadorPelicula
    private  var imagenes= mutableListOf<ModeloResult>()
//
//    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


//        val mIntent = Intent(this, DetallePelicula::class.java)
//        startActivity(mIntent)


//        homeViewModel.peliculaModel.observe(this, Observer {
//
//        })
        thread {
            val api_key=getString(R.string.api_key)

            val generos = RetrofitHelper.service.getGeneros(api_key)
            val bodyGeneros = generos.execute().body()

            val peliculasUpcoming = RetrofitHelper.service.getProximamente(api_key)
            val bodyPeliculasUpcoming=peliculasUpcoming.execute().body()

            val peliculasTendencia= RetrofitHelper.service.getTendencia(api_key)
            val bodyPeliculasTendencia= peliculasTendencia.execute().body()

            val peliculasRecomendaciones= RetrofitHelper.service.getRecomendaciones(api_key)
            val bodyPeliculasRecomendaciones= peliculasRecomendaciones.execute().body()

            runOnUiThread{
                val valor=  bodyGeneros?.genres as MutableList<ModeloGeneroResultado>

                Toast.makeText(this,"Generos ${valor[0].name}",Toast.LENGTH_LONG).show()

                if (bodyPeliculasUpcoming!=null){
                    adaptadorProximamente.images= bodyPeliculasUpcoming?.results as MutableList<ModeloResult>
                    adaptadorProximamente.notifyDataSetChanged()
                }

                if (bodyPeliculasTendencia!=null) {
                    adaptadorTendencia.images =
                        bodyPeliculasTendencia?.results as MutableList<ModeloResult>
                    adaptadorTendencia.notifyDataSetChanged()
                }
                if (bodyPeliculasRecomendaciones!=null) {
                    adaptadorRecomendados.images =
                        bodyPeliculasRecomendaciones?.results as MutableList<ModeloResult>
                    adaptadorRecomendados.notifyDataSetChanged()
                }


            }


        }
    }

    private fun abriDetalles(it: ModeloResult) {
        val intent = Intent(this, DetallePelicula::class.java)
        intent.putExtra("pelicula", it)
        startActivity(intent)
    }

    private fun toast(size: Int?) {
        Toast.makeText(this,"cuenta = $size", Toast.LENGTH_LONG).show()
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