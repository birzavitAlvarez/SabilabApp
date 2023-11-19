package com.demo.sabilabapp.Adapters.SequenceAdmin

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Pedidos.ProductosSeleccionados
import com.demo.sabilabapp.Productos.Result
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ItemDataPedidos2spProductosBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PedidosspProductosViewHolder(itemView: View, private val listener: OnProductoSeleccionadoListenerAdmin) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemDataPedidos2spProductosBinding =
        ItemDataPedidos2spProductosBinding.bind(itemView)

    fun bind(query: Result) {
        binding.tvNombrePedidos2psProductos.text = query.nombre
        binding.tvPrecioPedidos2psProductos.text = query.precio.toString()

        binding.ibSeleccionarPedidos2psProductos.setOnClickListener {
            dialogPedidos2spProductosCantidad(
                itemView.context,
                query.id_productos,
                query.nombre,
                query.precio
            )
        }
    }

    private fun dialogPedidos2spProductosCantidad(
        context: Context,
        id_productos: Int,
        nombre: String,
        precio: Double
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_cantidad_pedidos_ps)

        val ibCantidadPedidosPsClose: ImageButton = dialog.findViewById(R.id.ibCantidadPedidosPsClose)
        val tilCantidadPedidosPsCantidad: TextInputLayout = dialog.findViewById(R.id.tilCantidadPedidosPsCantidad)
        val tietCantidadPedidosPsCantidad: TextInputEditText = dialog.findViewById(R.id.tietCantidadPedidosPsCantidad)
        val btnCantidadPedidosPsGuardar: Button = dialog.findViewById(R.id.btnCantidadPedidosPsGuardar)

        ibCantidadPedidosPsClose.setOnClickListener {
            dialog.dismiss()
        }

        tietCantidadPedidosPsCantidad.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s?.toString() ?: ""
                if (input.isEmpty() || input.toInt() <= 0) {
                    tilCantidadPedidosPsCantidad.error = "La cantidad debe ser mayor a 0"
                } else { tilCantidadPedidosPsCantidad.error = null }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnCantidadPedidosPsGuardar.setOnClickListener {
            val cantidadInput = tietCantidadPedidosPsCantidad.text.toString()
            if (cantidadInput.isNotEmpty()) {
                val cantidad: Int = cantidadInput.toInt()
                if (cantidad <= 0) {
                    tilCantidadPedidosPsCantidad.error = "La cantidad debe ser mayor a 0"
                    return@setOnClickListener
                }
                val calcular: Double = cantidad * precio
                val totalFormateado = String.format("%.2f", calcular)
                val total = totalFormateado.toDouble()
                val productosSeleccionados = ProductosSeleccionados(id_productos, nombre, precio, cantidad, total)
                listener.onProductoSeleccionadoAdmin(productosSeleccionados)
                dialog.dismiss()
            } else {
                tilCantidadPedidosPsCantidad.error = "La cantidad no puede estar vacía"
            }
        }


        dialog.show()
    }
}
