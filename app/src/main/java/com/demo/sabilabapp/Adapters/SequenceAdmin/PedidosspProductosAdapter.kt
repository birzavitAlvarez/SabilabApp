package com.demo.sabilabapp.Adapters.SequenceAdmin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Productos.Result


class PedidosspProductosAdapter(private val itemsList: MutableList<Result>, private val listener: OnProductoSeleccionadoListenerAdmin) : RecyclerView.Adapter<PedidosspProductosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidosspProductosViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_data_pedidos2sp_productos,
            parent,
            false
        )
        return PedidosspProductosViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: PedidosspProductosViewHolder, position: Int) {
        val pedidos2spproductos = itemsList[position]
        holder.bind(pedidos2spproductos)
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
