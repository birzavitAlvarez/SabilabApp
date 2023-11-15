package com.demo.sabilabapp.Adapters.SequencePedidos

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Api.RetrofitClient
import com.demo.sabilabapp.Clientes.Result
import com.demo.sabilabapp.Pedidos.Pedidos2psActivity
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ItemPedidos2scBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class Pedidos2scViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemPedidos2scBinding = ItemPedidos2scBinding.bind(itemView)

    fun bind (query: Result){
        binding.tvRucPedidos2sc.text = query.ruc
        binding.tvNombreComercialPedidos2sc.text = query.nombre_comercial

        binding.ibSeleccionarPedidos2sc.setOnClickListener {
            showDialogDatosEnvio(itemView.context,query.id_cliente,query.direccion1,query.distrito, query.id_vendedor)
        }

    }

    private fun showDialogDatosEnvio(context: Context, id_cliente: Int, direccion1: String, distrito:String, id_vendedor:Int) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_add_datosenvio)
        val tvAddDatosEnvioTitle: TextView = dialog.findViewById(R.id.tvAddDatosEnvioTitle)
        val ibDatosEnvioClose: ImageButton = dialog.findViewById(R.id.ibDatosEnvioClose)
        val tilDatosEnvioDireccion: TextInputLayout = dialog.findViewById(R.id.tilDatosEnvioDireccion)
        val tietDatosEnvioDireccion: TextInputEditText = dialog.findViewById(R.id.tietDatosEnvioDireccion)
        val tilDatosEnvioDistrito: TextInputLayout = dialog.findViewById(R.id.tilDatosEnvioDistrito)
        val tietDatosEnvioDistrito: TextInputEditText = dialog.findViewById(R.id.tietDatosEnvioDistrito)
        val tilDatosEnvioFechaEntrega: TextInputLayout = dialog.findViewById(R.id.tilDatosEnvioFechaEntrega)
        val tietDatosEnvioFechaEntrega: TextInputEditText = dialog.findViewById(R.id.tietDatosEnvioFechaEntrega)
        val tilDatosEnvioComprobante: TextInputLayout = dialog.findViewById(R.id.tilDatosEnvioComprobante)
        val spDatosEnvioComprobante: Spinner = dialog.findViewById(R.id.spDatosEnvioComprobante)
        val btnDatosEnvioSiguiente: Button = dialog.findViewById(R.id.btnDatosEnvioSiguiente)

        tietDatosEnvioDireccion.setText(direccion1)
        tietDatosEnvioDistrito.setText(distrito)
        tietDatosEnvioDireccion.requestFocus()

        ibDatosEnvioClose.setOnClickListener {
            dialog.dismiss()
        }

        var selectedDate: String = ""

        tietDatosEnvioFechaEntrega.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(context, { _, year, month, dayOfMonth ->
                selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                tietDatosEnvioFechaEntrega.setText(selectedDate)
            }, year, month, day)
            // corrgiendo error del TIL
            tilDatosEnvioFechaEntrega.error = null
            datePickerDialog.show()
        }

        // llenar spinner comprobantes
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.apiService.listComprobante().body()
            (itemView.context as? AppCompatActivity)?.runOnUiThread {
                if (response != null && response.status == 200) {
                    val categoriasList = response.data.map { it.tipo_comprobante }
                    val categoriasWithSelect = listOf("Seleccionar") + categoriasList
                    val adapterLoad = ArrayAdapter(context, android.R.layout.simple_spinner_item, categoriasWithSelect)
                    adapterLoad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spDatosEnvioComprobante.adapter = adapterLoad
                    adapterLoad.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // ----------------------------- seleccionar id
        var selectedComprobanteId: Int? = null

        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.apiService.listCategory().body()
            spDatosEnvioComprobante.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedComprobanteId = if (position > 0) {
                        // corrgiendo error del TIL
                        tilDatosEnvioComprobante.error = null
                        response?.data?.get(position - 1)?.id_categoria
                    } else { null }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { selectedComprobanteId = null }
            }
        }
        // ------limpiando error mandado
        //
        tietDatosEnvioDireccion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilDatosEnvioDireccion.error = null
            }
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) { tilDatosEnvioDireccion.error = "Este campo es requerido" }
            }
        })
        //
        tietDatosEnvioDistrito.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilDatosEnvioDistrito.error = null
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) { tilDatosEnvioDistrito.error = "Este campo es requerido" }
            }
        })

        //----

        btnDatosEnvioSiguiente.setOnClickListener {
            val comprobanteSeleccionado = spDatosEnvioComprobante.selectedItem.toString()

            if (tietDatosEnvioDireccion.text.toString().isEmpty()){
                tilDatosEnvioDireccion.error = "Este campo es requerido"
                return@setOnClickListener
            } else if (tietDatosEnvioDistrito.text.toString().isEmpty()) {
                tilDatosEnvioDistrito.error = "Este campo es requerido"
                return@setOnClickListener
            } else if (selectedDate.isEmpty()) {
                tilDatosEnvioFechaEntrega.error = "Este campo es requerido"
                return@setOnClickListener
            } else if (comprobanteSeleccionado == "Seleccionar") {
                tilDatosEnvioComprobante.error = "Este campo es requerido"
            } else {
                val intent = Intent(context, Pedidos2psActivity::class.java)
                intent.putExtra("id_cliente", id_cliente)
                intent.putExtra("direccion", tietDatosEnvioDireccion.text.toString())
                intent.putExtra("distrito", tietDatosEnvioDistrito.text.toString())
                intent.putExtra("fecha_entrega", selectedDate)
                intent.putExtra("id_comprobante", selectedComprobanteId)
                intent.putExtra("id_vendedor", id_vendedor)
                context.startActivity(intent)
                dialog.dismiss()
            }
        }
        dialog.show()
    }



}