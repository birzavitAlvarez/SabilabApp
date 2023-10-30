package com.demo.sabilabapp.Proveedores

data class Result(
    val activo: Boolean,
    val correo: String,
    val id_proveedores: Int,
    val nombre_contacto: String,
    val razon_social: String,
    val ruc: String,
    val telefono: String
)