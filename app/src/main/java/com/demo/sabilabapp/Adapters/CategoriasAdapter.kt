package com.demo.sabilabapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Categorias.Data

class CategoriasAdapter(private val itemsList: MutableList<Data>) : RecyclerView.Adapter<CategoriasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriasViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        return CategoriasViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoriasViewHolder, position: Int) {
        val categorias = itemsList[position]
        holder.bind(categorias)
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




