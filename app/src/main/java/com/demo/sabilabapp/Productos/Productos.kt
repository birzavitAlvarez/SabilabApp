package com.demo.sabilabapp.Productos

data class Productos(
    val nombre: String,
    val laboratorio: String,
    val precio: Double,
    val lote: String,
    val fecha_caducidad: String,
    val activo: Int,
    val id_categoria: Int,
)