//package com.demo.sabilabapp.Adapters.SequencePedidos
//
//import android.app.Dialog
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ImageButton
//import androidx.recyclerview.widget.RecyclerView
//import com.demo.sabilabapp.Pedidos.ProductosSeleccionados // interface
//import com.demo.sabilabapp.R
//import com.demo.sabilabapp.Productos.Result
//import com.google.android.material.textfield.TextInputEditText
//import com.google.android.material.textfield.TextInputLayout
//
//
//class Pedidos2spProductosAdapter(private val itemsList: MutableList<Result>, private val listener: OnProductoSeleccionadoListener) : RecyclerView.Adapter<Pedidos2spProductosViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pedidos2spProductosViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_data_pedidos2sp_productos, parent, false)
//        return Pedidos2spProductosViewHolder(itemView,listener)
//    }
//
//    override fun onBindViewHolder(holder: Pedidos2spProductosViewHolder, position: Int) {
//        val pedidos2spproductos = itemsList[position]
//        holder.bind(pedidos2spproductos)
//    }
//
//    override fun getItemCount() = itemsList.size
//
//    fun updateList(newData: List<Result>) {
//        itemsList.clear()
//        itemsList.addAll(newData)
//        notifyDataSetChanged()
//    }
//
//    fun removeItem(position: Int) {
//        if (position >= 0 && position < itemsList.size) {
//            itemsList.removeAt(position)
//            notifyItemRemoved(position)
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
//            listener.onProductoSeleccionado(productosSeleccionados)
//            dialog.dismiss()
//        }
//
//        dialog.show()
//    }
//
//}


///////////////////////////////////////////////////


package com.demo.sabilabapp.Adapters.SequencePedidos

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Pedidos.ProductosSeleccionados
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Productos.Result
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Pedidos2spProductosAdapter(
    private val itemsList: MutableList<Result>,
    private val listener: OnProductoSeleccionadoListener
) : RecyclerView.Adapter<Pedidos2spProductosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pedidos2spProductosViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_data_pedidos2sp_productos,
            parent,
            false
        )
        return Pedidos2spProductosViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: Pedidos2spProductosViewHolder, position: Int) {
        val pedidos2spproductos = itemsList[position]
        holder.bind(pedidos2spproductos)
    }

    override fun getItemCount() = itemsList.size

    fun updateList(newData: List<Result>) {
        itemsList.clear()
        itemsList.addAll(newData)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < itemsList.size) {
            itemsList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

}
