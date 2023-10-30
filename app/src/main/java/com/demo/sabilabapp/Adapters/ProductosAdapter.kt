package com.demo.sabilabapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Productos.Result

class ProductosAdapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<ProductosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductosViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_productos, parent, false)
        return ProductosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductosViewHolder, position: Int) {
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