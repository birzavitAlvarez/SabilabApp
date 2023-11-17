package com.demo.sabilabapp.Adapters.SequencePedidos

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Pedidos.ProductosSeleccionados
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Productos.Result
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Pedidos2spProductosAdapter(private val itemsList: MutableList<Result>,
                                 private val listener: OnProductoSeleccionadoListener
) : RecyclerView.Adapter<Pedidos2spProductosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pedidos2spProductosViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_data_pedidos2sp_productos,
            parent,
            false
        )
        return Pedidos2spProductosViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: Pedidos2spProductosViewHolder, position: Int) {
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
