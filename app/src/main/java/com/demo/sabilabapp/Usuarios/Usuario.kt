package com.demo.sabilabapp.Usuarios

data class Usuario(
    val usuario: String,
    val password: String,
    val activo: Int,
    val id_roles: Int
)