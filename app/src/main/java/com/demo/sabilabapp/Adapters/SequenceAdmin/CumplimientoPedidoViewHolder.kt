package com.demo.sabilabapp.Adapters.SequenceAdmin

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.DetallePedido.Data // otro
import com.demo.sabilabapp.DetallePedido.DetallePedidoCantidad //para agregar
import com.demo.sabilabapp.databinding.ItemDataCumplimientoPedidoBinding // OTRO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.withContext

class CumplimientoPedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemDataCumplimientoPedidoBinding = ItemDataCumplimientoPedidoBinding.bind(itemView)

    fun bind (query: Data){
        binding.tvDataCumplimientoPedidoNombreProductos.text = query.nombre
        binding.tvDataCumplimientoPedidoObjetivo.text = query.cantidad_objetiva.toString()

        binding.ibDataCumplimientoPedidoEdit.setOnClickListener{
            showDialogEditCumpliPedido(itemView.context,query.id_detallepedido,query.cantidad_obtenida, query.id_pedido, query.cantidad_objetiva)
        }



    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun showDialogEditCumpliPedido(context: Context,id_detallepedido:Int,cantidad_obtenida:Int, id_pedido:Int, cantidad_objetiva:Int){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_cantidad_pedidos_ps)

        val tvCantidadPedidosPsTitle: TextView = dialog.findViewById(R.id.tvCantidadPedidosPsTitle)
        val ibCantidadPedidosPsClose: ImageButton = dialog.findViewById(R.id.ibCantidadPedidosPsClose)
        val tilCantidadPedidosPsCantidad: TextInputLayout = dialog.findViewById(R.id.tilCantidadPedidosPsCantidad)
        val tietCantidadPedidosPsCantidad: TextInputEditText = dialog.findViewById(R.id.tietCantidadPedidosPsCantidad)
        val btnCantidadPedidosPsGuardar: Button = dialog.findViewById(R.id.btnCantidadPedidosPsGuardar)

        tvCantidadPedidosPsTitle.setText("Cantidad Obtenida")
        tietCantidadPedidosPsCantidad.setText(cantidad_obtenida.toString())

        ibCantidadPedidosPsClose.setOnClickListener {
            dialog.dismiss()
        }

        tietCantidadPedidosPsCantidad.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s?.toString() ?: ""
                if (input.isEmpty() || input.toInt() <= 0) {
                    tilCantidadPedidosPsCantidad.error = "DEBE SER MAYOR A 0"
                } else if (input.isEmpty() || input.toInt() > cantidad_objetiva) {
                    tilCantidadPedidosPsCantidad.error = "Este valor excede la cantidad máxima $cantidad_objetiva"
                } else { tilCantidadPedidosPsCantidad.error = null }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnCantidadPedidosPsGuardar.setOnClickListener {
            val cantidadInput = tietCantidadPedidosPsCantidad.text.toString()
            if (cantidadInput.isNotEmpty()) {
                val cantidad: Int = cantidadInput.toInt()
                if (cantidad <= 0) {
                    tilCantidadPedidosPsCantidad.error = "DEBE SER MAYOR A 0"
                    return@setOnClickListener
                }

                if (cantidad > cantidad_objetiva) {
                    tilCantidadPedidosPsCantidad.error = "Este valor excede la cantidad máxima $cantidad_objetiva"
                    return@setOnClickListener
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val detallepedido = DetallePedidoCantidad(cantidadInput.toInt())
                    apiService.updateDetallePedido(detallepedido, id_detallepedido)
                    dialog.dismiss()
                }


            } else {
                tilCantidadPedidosPsCantidad.error = "ES REQUERIDO"
            }
        }

        dialog.show()
    }


}
