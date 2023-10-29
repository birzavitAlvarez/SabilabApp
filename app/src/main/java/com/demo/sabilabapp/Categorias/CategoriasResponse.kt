package com.demo.sabilabapp.Categorias

data class CategoriasResponse(
    val `data`: List<Data>,
    val status: Int,
    val statusMessage: String
)