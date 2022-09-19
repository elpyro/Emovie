package com.example.rappitest.ui.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.rappitest.R
import com.example.rappitest.data.model.ModeloResult
import com.example.rappitest.data.objeto.ListaGenerosPeliculas
import com.example.rappitest.databinding.ActivityDetallePeliculaBinding
import com.example.rappitest.viewModel.DetallePeliculaViewModel
import com.squareup.picasso.Picasso
import java.text.DecimalFormat


class DetallePelicula : AppCompatActivity() {
    var sideAnimation: Animation? = null
    var animation: Animation? =null
    //inicializando componenetes
    private lateinit var binding: ActivityDetallePeliculaBinding
    var pelicula: ModeloResult?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetallePeliculaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //incializamos componentes
        ini()

        //conectamos el ViewHolder con la vista
        iniViewModel()

        //LISTENER o acciones en la Actividad
        accionesComponentes()
    }

    private fun ini() {
        animation=AnimationUtils.loadAnimation(this,R.anim.slide_down)
        sideAnimation= AnimationUtils.loadAnimation(this,R.anim.slide)

        //informacion recibida del activity home
        pelicula=intent.getSerializableExtra("pelicula") as ModeloResult
    }

    private fun accionesComponentes() {
        binding.buttonTrailer.setOnClickListener{
            val intent = Intent(this, VideoPlayerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniViewModel() {
        val detallePeliculaViewModel: DetallePeliculaViewModel by viewModels()
        detallePeliculaViewModel.onCreate(pelicula)

        detallePeliculaViewModel.detallePeliculaModel.observe(this) {

            Picasso.get().load(getString(R.string.rutaImagenes) + it[0].poster_path)
                .into(binding.imageViewPelicula)
            binding.imageViewPelicula.startAnimation(animation)
            binding.textViewTitulo.setText(it[0].title)
            binding.textViewSinopsis.setText(it[0].overview)

            binding.buttonTrailer.startAnimation(sideAnimation)

            binding.buttonAno.setText(it[0].release_date.toString().substring(0, 4))
            binding.buttonLenguaje.setText(it[0].original_language)
            val df = DecimalFormat("#.#")
            binding.buttonRanking.setText("" + df.format(it[0].vote_average))

            val mostrarGeneros = ListaGenerosPeliculas.verificarGeneroPelicula(it[0].genre_ids)
            binding.textViewGenero.setText(mostrarGeneros)
        }
    }
}