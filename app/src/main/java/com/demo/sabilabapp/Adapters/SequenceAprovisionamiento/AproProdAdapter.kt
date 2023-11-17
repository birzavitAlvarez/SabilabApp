package com.demo.sabilabapp.Adapters.SequenceAprovisionamiento

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Adapters.SequenceAprovisionamiento.AproProdViewHolder
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Productos.Result

class AproProdAdapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<AproProdViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AproProdViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_data_pedidos2sp_productos, parent, false)
        return AproProdViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AproProdViewHolder, position: Int) {
        val productos = itemsList[position]
        holder.bind(productos)
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