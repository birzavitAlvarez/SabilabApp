package com.demo.sabilabapp.Adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Reportes.Result
import com.demo.sabilabapp.databinding.ItemReporteBinding

class ReporteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemReporteBinding = ItemReporteBinding.bind(itemView)

    fun bind (query: Result){
        binding.tvProductoReporte.text = query.productos
        binding.tvLaboratorioReporte.text = query.laboratorio
        binding.tvCantidadReporte.text = query.cantidad.toString()
        binding.tvTotalVentaReporte.text = query.venta_producto.toString()
        binding.tvPrecioCompraReporte.text = query.compra_producto.toString()
        binding.tvTotalVentaReporte.text = query.total_venta_producto.toString()
        binding.tvTotalCompraReporte.text = query.total_compra_producto.toString()
    }
}