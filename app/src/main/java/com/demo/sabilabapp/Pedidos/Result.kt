package com.demo.sabilabapp.Pedidos

data class Result(
    val id_pedido: Int,
    val direccion: String,
    val distrito: String,
    val fecha_pedido: String,
    val fecha_entrega: String,
    val fecha_llegada: Any,
    val total_pedido: Double,
    val activo: Boolean,
    val id_comprobante: Int,
    val tipo_comprobante: String,
    val id_vendedor: Int,
    val nombres: String,
    val id_cliente: Int,
    val nombre_comercial: String
)