package com.demo.sabilabapp.Adapters.SequenceAdmin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.DetallePedido.Data

class CumplimientoPedidoAdapter(private val itemsList: MutableList<Data>) : RecyclerView.Adapter<CumplimientoPedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CumplimientoPedidoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_data_cumplimiento_pedido, parent, false)
        return CumplimientoPedidoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CumplimientoPedidoViewHolder, position: Int) {
        val cumpli_pedido = itemsList[position]
        holder.bind(cumpli_pedido)
    }

    override fun getItemCount() = itemsList.size

    fun removeItem(position: Int) {
        if (position >= 0 && position < itemsList.size) {
            itemsList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    //
    fun updateList(newData: List<Data>) {
        itemsList.clear()
        itemsList.addAll(newData)
        notifyDataSetChanged()
    }

}