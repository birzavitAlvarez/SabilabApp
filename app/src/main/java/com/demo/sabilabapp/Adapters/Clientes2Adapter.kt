package com.demo.sabilabapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Clientes.Result

class Clientes2Adapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<Clientes2ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Clientes2ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_clientes2, parent, false)
        return Clientes2ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Clientes2ViewHolder, position: Int) {
        val clientes = itemsList[position]
        holder.bind(clientes)
    }

    override fun getItemCount() = itemsList.size

    fun updateList(newData: List<Result>) {
        itemsList.clear()
        itemsList.addAll(newData)
        notifyDataSetChanged()
    }

}