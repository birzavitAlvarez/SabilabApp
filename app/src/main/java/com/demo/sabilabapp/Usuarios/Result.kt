package com.demo.sabilabapp.Usuarios

data class Result(
    val activo: Boolean,
    val id_usuarios: Int,
    val password: String,
    val rol: String,
    val usuario: String
)