package com.demo.sabilabapp.Pedidos

data class ProductosSeleccionados(
    val id_productos: Int,
    val nombre: String,
    val precio: Double,
    var cantidad: Int,
    var total: Double
) {
    fun updateCantidadAndTotal(newCantidad: Int, newTotal:Double) {
        cantidad = newCantidad
        total = newTotal
    }
}
