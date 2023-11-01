package com.demo.sabilabapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Productos.Result

class Productos2Adapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<Productos2ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Productos2ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_productos2, parent, false)
        return Productos2ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Productos2ViewHolder, position: Int) {
        val productos = itemsList[position]
        holder.bind(productos)
    }

    override fun getItemCount() = itemsList.size

}