package com.demo.sabilabapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Pedidos.Result

class Pedidos2Adapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<Pedidos2ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pedidos2ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pedidos2, parent, false)
        return Pedidos2ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Pedidos2ViewHolder, position: Int) {
        val pedidos = itemsList[position]
        holder.bind(pedidos)
    }

    override fun getItemCount() = itemsList.size

}