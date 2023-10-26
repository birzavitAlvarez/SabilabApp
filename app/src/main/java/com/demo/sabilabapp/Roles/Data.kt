package com.demo.sabilabapp.Roles

data class Data(
    val id_roles: Int,
    val rol: String
)
{
    override fun toString(): String {
        return rol
    }
}