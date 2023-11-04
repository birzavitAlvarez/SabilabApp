package com.demo.sabilabapp.Vendedores

data class Result(
    val id_vendedor: Int,
    val nombres: String,
    val telefono1: String,
    val telefono2: String,
    val correo: String,
    val direccion: String,
    val fecha_nacimiento: String,
    val activo: Boolean,
    val id_usuarios: Int,
    val usuario: String
)