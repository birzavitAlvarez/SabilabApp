package com.demo.sabilabapp.Adapters.SequencePedidos

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Api.RetrofitClient
import com.demo.sabilabapp.Productos.Result // otro
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ItemDataPedidos2spProductosBinding // otro
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

//


class Pedidos2spProductosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemDataPedidos2spProductosBinding = ItemDataPedidos2spProductosBinding.bind(itemView)

    fun bind (query: Result){
        binding.tvNombrePedidos2psProductos.text = query.nombre
        binding.tvPrecioPedidos2psProductos.text = query.precio.toString()


        binding.ibSeleccionarPedidos2psProductos.setOnClickListener {
            dialogPedidos2spProductosCantidad(itemView.context,query.id_productos)
        }

    }

    private fun dialogPedidos2spProductosCantidad(context: Context, id_productos:Int) {
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
            var cantidad:Int = tietCantidadPedidosPsCantidad.text.toString().toInt()
            id_productos

        }





        dialog.show()
    }


}