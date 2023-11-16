package com.demo.sabilabapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Aprovisionamiento.Result

class AprovisionamientoAdapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<AprovisionamientoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AprovisionamientoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_aprovisionamiento, parent, false)
        return AprovisionamientoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AprovisionamientoViewHolder, position: Int) {
        val items = itemsList[position]
        holder.bind(items)
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