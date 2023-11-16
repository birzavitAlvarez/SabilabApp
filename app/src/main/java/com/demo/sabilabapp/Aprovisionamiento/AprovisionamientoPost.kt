package com.demo.sabilabapp.Aprovisionamiento

data class AprovisionamientoPost(
    val cantidad: Int,
    val fecha_compras: String,
    val id_proveedores: Int,
    val id_productos: Int
)