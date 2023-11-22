package com.demo.sabilabapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Reportes.Result

class ReporteAdapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<ReporteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_reporte, parent, false)
        return ReporteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReporteViewHolder, position: Int) {
        val reporte = itemsList[position]
        holder.bind(reporte)
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