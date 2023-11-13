//package com.demo.sabilabapp.Adapters.SequencePedidos
//
//import android.app.Dialog
//import android.content.Context
//import android.view.View
//import android.widget.Button
//import android.widget.ImageButton
//import androidx.recyclerview.widget.RecyclerView
//import com.demo.sabilabapp.Pedidos.ProductosSeleccionados // para listener
//import com.demo.sabilabapp.Productos.Result // otro
//import com.demo.sabilabapp.R
//import com.demo.sabilabapp.databinding.ItemDataPedidos2spProductosBinding // otro
//import com.google.android.material.textfield.TextInputEditText
//import com.google.android.material.textfield.TextInputLayout
//import kotlinx.coroutines.launch
//import java.util.Calendar
//import java.util.Locale
////import com.demo.sabilabapp.Pedidos.ProductosSeleccionados
//
////
//
//
//class Pedidos2spProductosViewHolder(itemView: View, private val listener: ProductosSeleccionados ) : RecyclerView.ViewHolder(itemView) {
//
//    private val binding: ItemDataPedidos2spProductosBinding = ItemDataPedidos2spProductosBinding.bind(itemView)
//
//    fun bind(query: Result) {
//        binding.tvNombrePedidos2psProductos.text = query.nombre
//        binding.tvPrecioPedidos2psProductos.text = query.precio.toString()
//
//        binding.ibSeleccionarPedidos2psProductos.setOnClickListener {
//            dialogPedidos2spProductosCantidad(itemView.context, query.id_productos, query.nombre, query.precio)
//        }
//    }
//
//    private fun dialogPedidos2spProductosCantidad(context: Context, id_productos: Int, nombre: String, precio: Double) {
//        val dialog = Dialog(context)
//        dialog.setContentView(R.layout.item_cantidad_pedidos_ps)
//
//        val ibCantidadPedidosPsClose: ImageButton = dialog.findViewById(R.id.ibCantidadPedidosPsClose)
//        val tilCantidadPedidosPsCantidad: TextInputLayout = dialog.findViewById(R.id.tilCantidadPedidosPsCantidad)
//        val tietCantidadPedidosPsCantidad: TextInputEditText = dialog.findViewById(R.id.tietCantidadPedidosPsCantidad)
//        val btnCantidadPedidosPsGuardar: Button = dialog.findViewById(R.id.btnCantidadPedidosPsGuardar)
//
//        ibCantidadPedidosPsClose.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        btnCantidadPedidosPsGuardar.setOnClickListener {
//            val cantidad: Int = tietCantidadPedidosPsCantidad.text.toString().toInt()
//            val total = cantidad * precio
//            val productosSeleccionados = ProductosSeleccionados(id_productos, nombre, precio, cantidad, total)
//
//            dialog.dismiss()
//        }
//
//        dialog.show()
//    }
//}
/////////////////////////////////////////////////////////////////////////////////////////////////

package com.demo.sabilabapp.Adapters.SequencePedidos

import android.app.Dialog
import android.content.Context
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

class Pedidos2spProductosViewHolder(
    itemView: View,
    private val listener: OnProductoSeleccionadoListener
) : RecyclerView.ViewHolder(itemView) {

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

        btnCantidadPedidosPsGuardar.setOnClickListener {
            val cantidad: Int = tietCantidadPedidosPsCantidad.text.toString().toInt()
            val calcular: Double = cantidad * precio
            val totalFormateado = String.format("%.2f", calcular)
            val total = totalFormateado.toDouble()
            val productosSeleccionados = ProductosSeleccionados(id_productos, nombre, precio, cantidad, total)
            listener.onProductoSeleccionado(productosSeleccionados)
            dialog.dismiss()
        }

        dialog.show()
    }
}
