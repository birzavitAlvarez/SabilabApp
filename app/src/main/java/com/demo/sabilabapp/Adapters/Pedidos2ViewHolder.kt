package com.demo.sabilabapp.Adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Pedidos.Result
import com.demo.sabilabapp.databinding.ItemPedidos2Binding

class Pedidos2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemPedidos2Binding = ItemPedidos2Binding.bind(itemView)

    fun bind (query: Result){
        binding.tvNroPedidos2.text = query.id_pedido.toString()
        binding.tvClientePedidos2.text = query.nombre_comercial
        binding.tvDireccionPedidos2.text = query.direccion
        binding.tvDistritoPedidos2.text = query.distrito
        binding.tvFechaPedidoPedidos2.text = query.fecha_pedido
        binding.tvFechaEntregaPedidos2.text = query.fecha_entrega
        binding.tvTotalPedidos2.text = query.total_pedido.toString()
    }
}