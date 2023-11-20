package com.demo.sabilabapp.DetallePedido

data class DetallePedidoResponse(
    val `data`: List<Data>,
    val status: Int,
    val statusMessage: String
)