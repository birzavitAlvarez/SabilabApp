package com.demo.sabilabapp.Adapters

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Aprovisionamiento.Result // otro
import com.demo.sabilabapp.Aprovisionamiento.AprovisionamientoPost //para agregar
import com.demo.sabilabapp.databinding.ItemAprovisionamientoBinding // OTRO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout



class AprovisionamientoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemAprovisionamientoBinding = ItemAprovisionamientoBinding.bind(itemView)

    fun bind (query: Result){
        binding.tvProductoAprovisionamiento.text = query.nombre
        binding.tvProveedorAprovisionamiento.text = query.razon_social
        binding.tvCantidadAprovisionamiento.text = query.cantidad.toString()
        binding.tvFechaAprovisionamiento.text = query.fecha_compras

        binding.ibEditAprovisionamiento.setOnClickListener{
            showDialogEditAprovisionamiento(itemView.context,query.id_compras,query.cantidad,query.fecha_compras,query.id_proveedores,query.id_productos)
        }

        binding.ibDeleteAprovisionamiento.setOnClickListener {
            showDialogDeleteProductos(itemView.context,query.id_compras,query.nombre)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun showDialogEditAprovisionamiento(context: Context, idCompras:Int, cantidad:Int, fecha:String, idProveedor:Int,idProductos:Int){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_cantidad_pedidos_ps)

        val tvCantidadPedidosPsTitle: TextView = dialog.findViewById(R.id.tvCantidadPedidosPsTitle)
        val ibCantidadPedidosPsClose: ImageButton = dialog.findViewById(R.id.ibCantidadPedidosPsClose)
        val tilCantidadPedidosPsCantidad: TextInputLayout = dialog.findViewById(R.id.tilCantidadPedidosPsCantidad)
        val tietCantidadPedidosPsCantidad: TextInputEditText = dialog.findViewById(R.id.tietCantidadPedidosPsCantidad)
        val btnCantidadPedidosPsGuardar: Button = dialog.findViewById(R.id.btnCantidadPedidosPsGuardar)

        //llenando datos para el update
        tvCantidadPedidosPsTitle.text = "Actualizar Compra"
        tietCantidadPedidosPsCantidad.setText(cantidad.toString())
        tietCantidadPedidosPsCantidad.requestFocus()

        ibCantidadPedidosPsClose.setOnClickListener{
            dialog.dismiss()
        }

        //TODO VALIDAR
        tietCantidadPedidosPsCantidad.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilCantidadPedidosPsCantidad.error = if (s?.any { it.isDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        //TODO FIN VALIDAR
        //
        btnCantidadPedidosPsGuardar.setOnClickListener{

            if (tietCantidadPedidosPsCantidad.text.toString().isEmpty()){
                tilCantidadPedidosPsCantidad.error = "ES REQUERIDO"
                return@setOnClickListener
            }

            val nuevaCantidad = tietCantidadPedidosPsCantidad.text.toString().toInt()

            CoroutineScope(Dispatchers.IO).launch {
                val aprovi = AprovisionamientoPost(nuevaCantidad, fecha, idProveedor, idProductos)
                apiService.updateAprovisionamiento(aprovi, idCompras)

                val updatedData = apiService.listAprovisionamiento().body()?.data?.results
                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    if (updatedData != null) {
                        (itemView.context as? AppCompatActivity)?.let {
                            val adapter = it.findViewById<RecyclerView>(R.id.rvAprovisionamiento)
                            (adapter?.adapter as? AprovisionamientoAdapter)?.updateList(updatedData)
                        }
                    }
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogDeleteProductos(context: Context,id:Int,nombre:String){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_confirm)

        val tvConfirmMessage: TextView = dialog.findViewById(R.id.tvConfirmMessage)
        val btnConfirmCancelar: Button = dialog.findViewById(R.id.btnConfirmCancelar)
        val btnConfirmConfirmar: Button = dialog.findViewById(R.id.btnConfirmConfirmar)
        tvConfirmMessage.text = "¿Seguro que quieres eliminar la compra del producto $nombre?"
        btnConfirmCancelar.setOnClickListener{
            dialog.dismiss()
        }
        btnConfirmConfirmar.setOnClickListener{
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    (itemView.context as? AppCompatActivity)?.let {
                        val adapter = it.findViewById<RecyclerView>(R.id.rvAprovisionamiento) //
                        (adapter?.adapter as? AprovisionamientoAdapter)?.removeItem(position) //
                    }
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                apiService.deleteAprovisionamiento(id)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

}