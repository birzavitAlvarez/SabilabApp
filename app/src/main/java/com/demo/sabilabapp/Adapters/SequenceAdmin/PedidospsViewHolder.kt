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
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ItemPedidos2psBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PedidospsViewHolder(itemView: View, private val adapter: PedidospsAdapter) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemPedidos2psBinding = ItemPedidos2psBinding.bind(itemView)

    fun bind(query: ProductosSeleccionados, adapter: PedidospsAdapter) {
        binding.tvPedidos2psNombre.text = query.nombre
        binding.tvPedidos2psPrecio.text = query.precio.toString()
        binding.tvPedidos2psCantidad.text = query.cantidad.toString()
        binding.tvPedidos2psTotal.text = query.total.toString()

        binding.ibPedidos2psEdit.setOnClickListener {
            dialogEditPedidos2ps(itemView.context, query, query.precio, adapter)
        }

        binding.ibPedidos2psDelete.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                adapter.removeItem(position)
            }
        }
    }

    private fun dialogEditPedidos2ps(
        context: Context,
        productosSeleccionados: ProductosSeleccionados,
        precio: Double,
        adapter: PedidospsAdapter
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_cantidad_pedidos_ps)

        val ibCantidadPedidosPsClose: ImageButton = dialog.findViewById(R.id.ibCantidadPedidosPsClose)
        val tilCantidadPedidosPsCantidad: TextInputLayout =
            dialog.findViewById(R.id.tilCantidadPedidosPsCantidad)
        val tietCantidadPedidosPsCantidad: TextInputEditText =
            dialog.findViewById(R.id.tietCantidadPedidosPsCantidad)
        val btnCantidadPedidosPsGuardar: Button =
            dialog.findViewById(R.id.btnCantidadPedidosPsGuardar)

        tietCantidadPedidosPsCantidad.setText(productosSeleccionados.cantidad.toString())

        ibCantidadPedidosPsClose.setOnClickListener {
            dialog.dismiss()
        }

        tietCantidadPedidosPsCantidad.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s?.toString() ?: ""
                if (input.isEmpty()) {
                    tilCantidadPedidosPsCantidad.error = "La cantidad no puede estar vacía"
                }else if(input.toInt() <= 0){
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
                val nuevoTotal = cantidad * precio
                val totalFormateado = String.format("%.2f", nuevoTotal)
                productosSeleccionados.updateCantidadAndTotal(cantidad, totalFormateado.toDouble())

                adapter.updateItem(productosSeleccionados)

                (context as? OnItemUpdateListenerAdmin)?.onItemUpdatedAdmin()
                dialog.dismiss()
            } else {
                tilCantidadPedidosPsCantidad.error = "La cantidad no puede estar vacía"
            }

        }

        dialog.show()
    }
}