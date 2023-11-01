package com.demo.sabilabapp.Adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Productos.Result
import com.demo.sabilabapp.databinding.ItemProductos2Binding

class Productos2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemProductos2Binding = ItemProductos2Binding.bind(itemView)

    fun bind (query: Result){
        binding.tvNombreProductos2.text = query.nombre
        binding.tvLaboratorioProductos2.text = query.laboratorio
        binding.tvPrecioProductos2.text = query.precio.toString()
        binding.tvLoteProductos2.text = query.lote
        binding.tvFechaCaducidadProductos2.text = query.fecha_caducidad
        binding.tvCategoriaProductos2.text = query.tipo
    }
}