package com.demo.sabilabapp.Adapters.SequenceAprovisionamiento

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Adapters.ProveedorAdapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Proveedores.Result // otro
import com.demo.sabilabapp.Aprovisionamiento.AprovisionamientoPost//para agregar
import com.demo.sabilabapp.databinding.ItemDataDialogProveedorBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class AproProveedorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemDataDialogProveedorBinding = ItemDataDialogProveedorBinding.bind(itemView)



    @RequiresApi(Build.VERSION_CODES.O)
    fun bind (query: Result){
        binding.tvRazonSocialDialogProveedor.text = query.razon_social

        binding.ibSeleccionarDialogProveedor.setOnClickListener{
            showDialogCantidadProveedor(itemView.context,query.id_proveedores)
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun showDialogCantidadProveedor(context: Context,id_proveedores:Int){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_cantidad_pedidos_ps)

        val tvCantidadPedidosPsTitle: TextView = dialog.findViewById(R.id.tvCantidadPedidosPsTitle)
        val ibCantidadPedidosPsClose: ImageButton = dialog.findViewById(R.id.ibCantidadPedidosPsClose)
        val tilCantidadPedidosPsCantidad: TextInputLayout = dialog.findViewById(R.id.tilCantidadPedidosPsCantidad)
        val tietCantidadPedidosPsCantidad: TextInputEditText = dialog.findViewById(R.id.tietCantidadPedidosPsCantidad)
        val btnCantidadPedidosPsGuardar: Button = dialog.findViewById(R.id.btnCantidadPedidosPsGuardar)
        tvCantidadPedidosPsTitle.text = "Registrar compra"
        tietCantidadPedidosPsCantidad.requestFocus()

        ibCantidadPedidosPsClose.setOnClickListener{
            dialog.dismiss()
        }

        // TODO VALIDAR
        tietCantidadPedidosPsCantidad.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilCantidadPedidosPsCantidad.error = if (s?.any { it.isDigit() } == true) null else "ES REQUERIDO"
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        // TODO FIN VALIDAR

        btnCantidadPedidosPsGuardar.setOnClickListener{
            // TODO VALIDAR
            if (tietCantidadPedidosPsCantidad.text.toString().isEmpty()){
                tilCantidadPedidosPsCantidad.error = "ES REQUERIDO"
                return@setOnClickListener
            }
            // TODO FIN VALIDAR

            val cantidad = tietCantidadPedidosPsCantidad.text.toString().toInt()
            val fecha_del_dia = obtenerFechaActual()

            CoroutineScope(Dispatchers.IO).launch {
                val proveedor = AprovisionamientoPost(cantidad,fecha_del_dia,id_proveedores,1)
                apiService.createAprovisionamiento(proveedor)

                //
                dialog.dismiss()
            }

        }

        dialog.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerFechaActual(): String {
        val fechaActual = LocalDate.now()
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return fechaActual.format(formato)
    }

}