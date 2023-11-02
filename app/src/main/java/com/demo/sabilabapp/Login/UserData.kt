package com.demo.sabilabapp.Login

import java.io.Serializable

data class UserData(
    val id_usuarios: Int,
    val id_roles: Int,
    val id_vendedor: Int,
    val nombre: String
) : Serializable