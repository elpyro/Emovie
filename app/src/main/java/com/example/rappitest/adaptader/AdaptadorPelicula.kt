package com.example.rappitest.adaptader

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.rappitest.R
import com.example.rappitest.data.model.ModeloResult
import com.squareup.picasso.Picasso
import java.security.AccessController.getContext


class AdaptadorPelicula (var images: MutableList<ModeloResult> = ArrayList()): RecyclerView.Adapter<AdaptadorPelicula.ViewHolder>() {
    private var onClickItem :  ((ModeloResult)-> Unit)?=null
    var context: Context? = null
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imagen: ImageView

        init {

            // Define click listener for the ViewHolder's View.
            imagen = view.findViewById(R.id.imagenPelicula)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pelicula, parent, false)
        context= parent.context
        return AdaptadorPelicula.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: String = images[position].poster_path
        val itemSeleccionado= images[position]
        val imagen: String = context!!.getString(R.string.rutaImagenes)+item
        Picasso.get().load(imagen).into(holder.imagen)
        holder.imagen.setOnClickListener{
            onClickItem?.invoke(itemSeleccionado)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun setOnClickItem(callback: (ModeloResult)-> Unit){
        this.onClickItem = callback
    }


}