package com.demo.sabilabapp.Productos

data class Result(
    val id_productos: Int,
    val nombre: String,
    val laboratorio: String,
    val precio: Double,
    val lote: String,
    val fecha_caducidad: String,
    val activo: Boolean,
    val id_categoria: Int,
    val tipo: String
)