package com.demo.sabilabapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Proveedores.Result

class ProveedorAdapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<ProveedorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProveedorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_proveedor, parent, false)
        return ProveedorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProveedorViewHolder, position: Int) {
        val usuario = itemsList[position]
        holder.bind(usuario)
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