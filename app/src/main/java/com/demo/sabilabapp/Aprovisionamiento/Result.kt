package com.demo.sabilabapp.Aprovisionamiento

data class Result(
    val id_compras: Int,
    val cantidad: Int,
    val fecha_compras: String,
    val id_proveedores: Int,
    val razon_social: String,
    val id_productos: Int,
    val nombre: String,
)