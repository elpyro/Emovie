package com.example.rappitest.procesos

import com.example.rappitest.data.objeto.ListaRecomendadas
import com.example.rappitest.data.objeto.ListaTendencia

class  SeisRecomendaciones() {

     fun SeisPeliculas() {
         var count =0
         for (x in ListaTendencia.Tendencia) {
             count = count + 1
             if (count < 7) {
                 ListaRecomendadas.SeisRecomendadas.add(x)
             }else{
                 break
             }
     }
    }
}