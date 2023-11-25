package com.demo.sabilabapp.Adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Dashboard.HojaRuta.Result
import com.demo.sabilabapp.databinding.ItemHojaRutaBinding

class HojaRutaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemHojaRutaBinding = ItemHojaRutaBinding.bind(itemView)

    fun bind (query: Result){
        binding.tvHojaRutaCliente.text = query.nombre_comercial
        binding.tvHojaRutaVendedor.text = query.nombres
        binding.tvHojaRutaDireccion.text = query.direccion
        binding.tvHojaRutaDistrito.text = query.distrito
        binding.tvHojaRutaFechaPedido.text = query.fecha_pedido
        binding.tvHojaRutaTotal.text = query.total_pedido.toString()
    }
}