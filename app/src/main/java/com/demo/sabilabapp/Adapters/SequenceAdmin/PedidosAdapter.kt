package com.demo.sabilabapp.Adapters.SequenceAdmin

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Pedidos.Result

class PedidosAdapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<PedidosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidosViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pedidos, parent, false)
        return PedidosViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PedidosViewHolder, position: Int) {
        val pedidos = itemsList[position]
        holder.bind(pedidos)
    }

    override fun getItemCount() = itemsList.size

    fun removeItem(position: Int) {
        if (position >= 0 && position < itemsList.size) {
            itemsList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    //
    fun updateList(newData: List<Result>) {
        itemsList.clear()
        itemsList.addAll(newData)
        notifyDataSetChanged()
    }

}