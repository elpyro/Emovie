package com.example.rappitest.procesos

import android.content.Context
import android.preference.PreferenceManager
import com.example.rappitest.data.model.ModeloGeneroResultado
import com.example.rappitest.data.model.ModeloResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

 class Preferencias () {

     fun cargarPreferencia(context: Context, key: String) : ArrayList<*> {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
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

     fun crearPreferencia(context: Context, lista: ArrayList<*>, key:String) {
        val gson = Gson()
        val jsonString = gson.toJson(lista)
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = pref.edit()
        editor.putString(key, jsonString)
        editor.apply()
    }

}