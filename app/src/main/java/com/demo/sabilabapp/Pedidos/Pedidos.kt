package com.demo.sabilabapp.Pedidos

data class Pedidos(
    val direccion: String,
    val distrito: String,
    val fecha_entrega: String,
    val fecha_llegada: String,
    val total_pedido: Double,
    val activo: Int,
    val id_comprobante: Int,
    val id_vendedor: Int,
    val id_cliente: Int
)