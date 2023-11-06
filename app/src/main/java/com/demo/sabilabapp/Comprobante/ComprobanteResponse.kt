package com.demo.sabilabapp.Comprobante

data class ComprobanteResponse(
    val status: Int,
    val statusMessage: String,
    val `data`: List<Data>,
)