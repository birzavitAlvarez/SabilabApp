package com.demo.sabilabapp.DetallePedido

data class DetallePedidoPost(
    val cantidad_objetiva: Int,
    val total_detalle: Double,
    val id_pedido: Int,
    val id_productos: Int
)