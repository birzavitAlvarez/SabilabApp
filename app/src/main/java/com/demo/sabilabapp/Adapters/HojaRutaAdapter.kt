package com.demo.sabilabapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Dashboard.HojaRuta.Result

class HojaRutaAdapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<HojaRutaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HojaRutaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_hoja_ruta, parent, false)
        return HojaRutaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HojaRutaViewHolder, position: Int) {
        val hojaRuta = itemsList[position]
        holder.bind(hojaRuta)
    }

    override fun getItemCount() = itemsList.size

}