package com.demo.sabilabapp.DetallePedido

data class Data(
    val id_detallepedido: Int,
    val cantidad_objetiva: Int,
    val cantidad_obtenida: Int,
    val total_detalle: Double,
    val id_pedido: Int,
    val id_productos: Int,
    val nombre: String
)