package com.demo.sabilabapp.Proveedores

data class Proveedor(
    val ruc: String,
    val razon_social: String,
    val nombre_contacto: String,
    val telefono: String,
    val correo: String,
    val activo: Int
)