package com.demo.sabilabapp.Adapters.SequencePedidos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Pedidos.ProductosSeleccionados
// private val itemsList
class Pedidos2psAdapter(internal val itemsList: MutableList<ProductosSeleccionados>) : RecyclerView.Adapter<Pedidos2psViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pedidos2psViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pedidos2ps, parent, false)
        return Pedidos2psViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Pedidos2psViewHolder, position: Int) {
        val items_data = itemsList[position]
        holder.bind(items_data,this)
    }

    override fun getItemCount() = itemsList.size

    fun updateItem(producto: ProductosSeleccionados) {
        val index = itemsList.indexOfFirst { it.id_productos == producto.id_productos }
        if (index != -1) {
            itemsList[index] = producto
            notifyItemChanged(index)
        }
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < itemsList.size) {
            itemsList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

}