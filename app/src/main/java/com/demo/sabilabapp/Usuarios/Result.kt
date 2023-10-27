package com.demo.sabilabapp.Usuarios

data class Result(
    val id_usuarios: Int,
    val usuario: String,
    val password: String,
    val activo: Boolean,
    val rol: String

)