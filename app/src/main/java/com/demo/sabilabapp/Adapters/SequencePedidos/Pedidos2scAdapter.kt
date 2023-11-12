package com.demo.sabilabapp.Adapters.SequencePedidos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Clientes.Result

class Pedidos2scAdapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<Pedidos2scViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pedidos2scViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pedidos2sc, parent, false)
        return Pedidos2scViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Pedidos2scViewHolder, position: Int) {
        val clientes = itemsList[position]
        holder.bind(clientes)
    }

    override fun getItemCount() = itemsList.size

    fun updateList(newData: List<Result>) {
        itemsList.clear()
        itemsList.addAll(newData)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < itemsList.size) {
            itemsList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

}