package com.example.rappitest.ui.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.rappitest.R
import com.example.rappitest.data.model.ModeloGenero
import com.example.rappitest.data.model.ModeloResult
import com.example.rappitest.databinding.ActivityDetallePeliculaBinding
import com.example.rappitest.network.RetrofitHelper
import com.example.rappitest.viewModel.DetallePeliculaViewModel
import com.squareup.picasso.Picasso
import java.text.DecimalFormat


class DetallePelicula : AppCompatActivity() {

    private lateinit var binding: ActivityDetallePeliculaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetallePeliculaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animation=AnimationUtils.loadAnimation(this,R.anim.slide_down)
        val pelicula=intent.getSerializableExtra("pelicula") as ModeloResult
//
        val detallePeliculaViewModel: DetallePeliculaViewModel by viewModels()


        detallePeliculaViewModel.onCreate(pelicula)
        detallePeliculaViewModel.detallePeliculaModel.observe(this,{
            Picasso.get().load(getString(R.string.rutaImagenes)+it[0].poster_path).into(binding.imageViewPelicula)
            binding.textViewTitulo.setText(it[0].title)
            binding.textViewSinopsis.setText(it[0].overview)

            binding.buttonAno.setText(it[0].release_date.toString().substring(0,4))
            binding.buttonLenguaje.setText(it[0].original_language)
            val df = DecimalFormat("#.#")
            binding.buttonRanking.setText(""+  df.format(it[0].vote_average))

        })
        val api_key=getString(R.string.api_key)
        val generos=RetrofitHelper.service.getGeneros(api_key)

        binding.buttonTrailer.setOnClickListener{
            val intent = Intent(this, VideoPlayerActivity::class.java)
            startActivity(intent)
        }
    }
}