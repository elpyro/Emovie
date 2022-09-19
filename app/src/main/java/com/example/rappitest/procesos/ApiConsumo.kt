package com.example.rappitest.procesos

import android.content.Context
import com.example.rappitest.R
import com.example.rappitest.data.model.ModeloGeneroResultado
import com.example.rappitest.data.model.ModeloPeliculas
import com.example.rappitest.data.model.ModeloResult
import com.example.rappitest.data.objeto.ListaGenerosPeliculas
import com.example.rappitest.data.objeto.ListaRecomendadas
import com.example.rappitest.data.objeto.ListaTendencia
import com.example.rappitest.data.objeto.ListaUpComing
import com.example.rappitest.network.RetrofitHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.thread

class ApiConsumo @Inject constructor(

) {

    var preferencias= Preferencias ()
    private val LIST_KEY_GENEROS = "PREFERENCIAS_GENERO"
    private val LIST_KEY_TENDENCIA = "PREFERENCIAS_TENDENCIA"
    private val LIST_KEY_PROXIMAMENTE = "PREFERENCIAS_PROXIMAMENTE"

//    fun apis (context:Context) {
//        thread {
//            val api_key = "c06fce03255ef224fabad691249b6019"
//
//
//           //CREA LOS ARRAYLIST COMO OBJETOS
//           //SI ALGUNA API NO CARGA USAR EL ARRAYLIST GUARDADO
//           //GUARDA EL ARRAYLIST COMO PREFERNCIA CUANDO CARGA EXITOSAMENTE
//           try {
//
//               val generos = RetrofitHelper.service.getGeneros(api_key)
//                val bodyGeneros = generos?.execute()?.body()
//                ListaGenerosPeliculas.genero =bodyGeneros?.genres as ArrayList<ModeloGeneroResultado>
//
//               preferencias.crearPreferencia(context, ListaGenerosPeliculas.genero ,LIST_KEY_GENEROS)
//           }catch (e:Exception){
//
//               ListaGenerosPeliculas.genero = preferencias.cargarPreferencia(context,LIST_KEY_GENEROS) as ArrayList<ModeloGeneroResultado>
//           }
//
//           try {
//               val peliculasTendencia= RetrofitHelper.service.getTendencia(api_key)
//               var bodyPeliculasTendencia: ModeloPeliculas? =  peliculasTendencia?.execute()?.body()
//               ListaTendencia.Tendencia=bodyPeliculasTendencia?.results as ArrayList<ModeloResult>
//
//               preferencias.crearPreferencia(context, ListaTendencia.Tendencia ,LIST_KEY_TENDENCIA)
//
//           }catch (e:Exception){
//               ListaTendencia.Tendencia = preferencias.cargarPreferencia(context, LIST_KEY_TENDENCIA) as ArrayList<ModeloResult>
//           }
//
//           try{
//               val peliculaUpcoming= RetrofitHelper.service.getProximamente(api_key)
//               val bodyPeliculasUpcoming= peliculaUpcoming?.execute()?.body()
//               ListaUpComing.Upcoming=bodyPeliculasUpcoming?.results as ArrayList<ModeloResult>
//
//               preferencias.crearPreferencia(context, ListaUpComing.Upcoming,LIST_KEY_PROXIMAMENTE)
//           }catch (e:Exception){
//               ListaUpComing.Upcoming= preferencias.cargarPreferencia(context,LIST_KEY_PROXIMAMENTE) as ArrayList<ModeloResult>
//           }
//
//
//        }
//    }


}