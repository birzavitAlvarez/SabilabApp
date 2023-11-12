package com.demo.sabilabapp.Adapters.SequencePedidos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Productos.Result

class Pedidos2spProductosAdapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<Pedidos2spProductosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pedidos2spProductosViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_data_pedidos2sp_productos, parent, false)
        return Pedidos2spProductosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Pedidos2spProductosViewHolder, position: Int) {
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