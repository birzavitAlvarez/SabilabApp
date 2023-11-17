package com.demo.sabilabapp.Adapters.SequenceAprovisionamiento

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Adapters.ProveedorViewHolder
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Proveedores.Result

class AproProveedorAdapter(private val itemsList: MutableList<Result>) : RecyclerView.Adapter<AproProveedorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AproProveedorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_data_dialog_proveedor, parent, false)
        return AproProveedorViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AproProveedorViewHolder, position: Int) {
        val proveedor = itemsList[position]
        holder.bind(proveedor)
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