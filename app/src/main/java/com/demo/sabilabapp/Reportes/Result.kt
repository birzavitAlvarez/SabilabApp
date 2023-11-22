package com.demo.sabilabapp.Reportes

data class Result(
    val productos: String,
    val laboratorio: String,
    val cantidad: Int,
    val venta_producto: Double,
    val total_venta_producto: Double,
    val compra_producto: Double,
    val total_compra_producto: Double
)