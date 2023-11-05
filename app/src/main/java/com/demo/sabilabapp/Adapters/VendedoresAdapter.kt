package com.demo.sabilabapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Vendedores.Result

class VendedoresAdapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<VendedoresViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendedoresViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_vendedores, parent, false)
        return VendedoresViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VendedoresViewHolder, position: Int) {
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