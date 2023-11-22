package com.demo.sabilabapp.Reportes

data class ReporteResponse(
    val status: Int,
    val statusMessage: String,
    val `data`: Data
)