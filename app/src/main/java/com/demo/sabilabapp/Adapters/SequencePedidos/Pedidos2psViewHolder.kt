package com.demo.sabilabapp.Adapters.SequencePedidos

import android.app.Dialog
import android.content.Context
import android.view.View

import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Adapters.ClientesAdapter
import com.demo.sabilabapp.Pedidos.ProductosSeleccionados
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ItemPedidos2psBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

    class Pedidos2psViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemPedidos2psBinding = ItemPedidos2psBinding.bind(itemView)

    fun bind(query: ProductosSeleccionados, adapter: Pedidos2psAdapter) {
        binding.tvPedidos2psNombre.text = query.nombre
        binding.tvPedidos2psPrecio.text = query.precio.toString()
        binding.tvPedidos2psCantidad.text = query.cantidad.toString()
        binding.tvPedidos2psTotal.text = query.total.toString()

        binding.ibPedidos2psEdit.setOnClickListener {
            dialogEditPedidos2ps(itemView.context, query, query.precio, adapter)
        }

        binding.ibPedidos2psDelete.setOnClickListener {

        }
    }

    private fun dialogEditPedidos2ps(
        context: Context,
        productosSeleccionados: ProductosSeleccionados,
        precio: Double,
        adapter: Pedidos2psAdapter
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

        btnCantidadPedidosPsGuardar.setOnClickListener {
            val cantidad: Int = tietCantidadPedidosPsCantidad.text.toString().toInt()
            val nuevoTotal = cantidad * precio
            productosSeleccionados.updateCantidadAndTotal(cantidad, nuevoTotal)

            adapter.updateItem(productosSeleccionados)

            dialog.dismiss()
        }

        dialog.show()
    }
}