package com.demo.sabilabapp.Clientes

data class Clientes(
    val ruc: String,
    val razon_social: String,
    val nombre_comercial: String,
    val contacto: String,
    val direccion1: String,
    val direccion2: String,
    val telefono1: String,
    val telefono2: String,
    val telefono_empresa: String,
    val provincia: String,
    val distrito: String,
    val activo: Int,
    val id_vendedor: Int,
)