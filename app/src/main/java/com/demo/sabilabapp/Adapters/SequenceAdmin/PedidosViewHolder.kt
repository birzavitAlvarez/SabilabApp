package com.demo.sabilabapp.Adapters.SequenceAdmin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Pedidos.Result // otro
import com.demo.sabilabapp.Pedidos.PedidosUpdate4 //para update
import com.demo.sabilabapp.databinding.ItemAddDatosenvioBinding // para el dialog de Edit
import com.demo.sabilabapp.databinding.ItemDialogCumplimientoPedidoBinding
import com.demo.sabilabapp.databinding.ItemPedidosBinding // OTRO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import com.demo.sabilabapp.DetallePedido.Data as DataDetalle


class PedidosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemPedidosBinding = ItemPedidosBinding.bind(itemView)
    // EDIT
    private var bindingEdit: ItemAddDatosenvioBinding? = null
    //
    private var bindingDialog: ItemDialogCumplimientoPedidoBinding? = null

    private lateinit var adapterCumpliPedidosDialog: CumplimientoPedidoAdapter
    private val datitosCumpliPedidosDialog = mutableListOf<DataDetalle>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind (query: Result){
        binding.tvClientePedidos.text = query.nombre_comercial
        binding.tvDireccionPedidos.text = query.direccion
        binding.tvFechaPedidoPedidos.text = query.fecha_pedido
        binding.tvFechaEntregaPedidos.text = query.fecha_entrega
        binding.tvFechaLlegadaPedidos.text = query.fecha_llegada
        binding.tvTotalPedidos.text = query.total_pedido.toString()

        binding.ibPedidosEdit.setOnClickListener{
            showDialogEditPedidos(itemView.context,query.id_pedido, query.direccion,query.distrito,query.fecha_entrega,query.id_comprobante)
        }

        binding.ibPedidosDelete.setOnClickListener {
            showDialogDeletePedidos(itemView.context,query.id_pedido,query.nombre_comercial)
        }

        binding.ibPedidosSheck.setOnClickListener {
            showDialogSheckPedidos(itemView.context,query.id_pedido,query.fecha_llegada)
        }


    }

    private fun showDialogSheckPedidos(context: Context, idPedido: Int, fechaLlegada: String) {
        val dialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        //dialog.setContentView(R.layout.item_dialog_cumplimiento_pedido)
        bindingDialog = ItemDialogCumplimientoPedidoBinding.inflate(inflater)
        dialog.setContentView(bindingDialog?.root!!)


        val rvCumplimientoPedidoDialog: RecyclerView = dialog.findViewById(R.id.rvCumplimientoPedidoDialog)
        adapterCumpliPedidosDialog = CumplimientoPedidoAdapter(datitosCumpliPedidosDialog)
        rvCumplimientoPedidoDialog.layoutManager = LinearLayoutManager(context)
        rvCumplimientoPedidoDialog.adapter = adapterCumpliPedidosDialog

        listaAlEntrarDialog(idPedido, rvCumplimientoPedidoDialog, adapterCumpliPedidosDialog)




        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrarDialog(idPedido:Int, recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listDetallePedidoByIdPedido(idPedido)
            val response0 = request0.body()
            withContext(Dispatchers.Main) {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data
                    (adapter as CumplimientoPedidoAdapter).updateList(dataItems!!)
                    recyclerView.layoutManager?.scrollToPosition(0)
                } else {
                    showErrorDialog()
                }
            }
        }
    }

    private fun showErrorDialog() {
        Toast.makeText(itemView.context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun showDialogEditPedidos(context: Context,id_pedido:Int,direccion:String, distrito:String,fecha_entrega:String,id_comprobante:Int){
        val dialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        //dialog.setContentView(R.layout.item_add_datosenvio)
        bindingEdit = ItemAddDatosenvioBinding.inflate(inflater)
        dialog.setContentView(bindingEdit?.root!!)


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

        //llenando datos para el update
        tvAddDatosEnvioTitle.text = "Actualizar pedido"
        btnDatosEnvioSiguiente.setText("Guardar")
        tietDatosEnvioDireccion.setText(direccion)
        tietDatosEnvioDistrito.setText(distrito)
        tietDatosEnvioFechaEntrega.setText(fecha_entrega)
        tietDatosEnvioDireccion.requestFocus()

        ibDatosEnvioClose.setOnClickListener{
            dialog.dismiss()
        }
        //
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
            val response = apiService.listComprobante().body()
            (itemView.context as? AppCompatActivity)?.runOnUiThread {
                if (response != null && response.status == 200) {
                    val categoriasList = response.data.map { it.tipo_comprobante }
                    val categoriasWithSelect = listOf("Seleccionar") + categoriasList
                    val adapterLoad = ArrayAdapter(context, android.R.layout.simple_spinner_item, categoriasWithSelect)
                    adapterLoad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spDatosEnvioComprobante.adapter = adapterLoad
                    adapterLoad.notifyDataSetChanged()
                    spDatosEnvioComprobante.setSelection(id_comprobante)
                } else {
                    Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // ----------------------------- seleccionar id
        var selectedComprobanteId: Int? = null

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listCategory().body()
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


        //TODO VALIDAR
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
        //TODO FIN VALIDAR
        //
        btnDatosEnvioSiguiente.setOnClickListener{

            val tipoComprobamte = spDatosEnvioComprobante.selectedItem.toString()
//            val fechita = selectedDate.toString()

            if (tietDatosEnvioDireccion.text.toString().isEmpty()){
                tilDatosEnvioDireccion.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietDatosEnvioDistrito.text.toString().isEmpty()){
                tilDatosEnvioDistrito.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietDatosEnvioFechaEntrega.text.toString().isEmpty()){
                tilDatosEnvioFechaEntrega.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tipoComprobamte == "Seleccionar"){
                tilDatosEnvioComprobante.error = "ES REQUERIDO"
                return@setOnClickListener
            }

            val direcc = tietDatosEnvioDireccion.text.toString()
            val distri = tietDatosEnvioDistrito.text.toString()
            val fecEnt = tietDatosEnvioFechaEntrega.text.toString()
            val idcomp = selectedComprobanteId!!

            // no me juzguen xd
            val fecha_del_dia = obtenerFechaActual()

            CoroutineScope(Dispatchers.IO).launch {
                val producto = PedidosUpdate4(direcc ,distri ,fecEnt,idcomp)
                apiService.updatePedido4(producto, id_pedido)

                val updatedData = apiService.listPedidosAdminTrue(fecha_del_dia).body()?.data?.results

                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    if (updatedData != null) {
                        (itemView.context as? AppCompatActivity)?.let {
                            val adapter = it.findViewById<RecyclerView>(R.id.rvPedidos)
                            (adapter?.adapter as? PedidosAdapter)?.updateList(updatedData)
                        }
                    }
                    dialog.dismiss()
                }
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

    @SuppressLint("SetTextI18n")
    private fun showDialogDeletePedidos(context: Context,id:Int,nombre:String){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_confirm)

        val tvConfirmMessage: TextView = dialog.findViewById(R.id.tvConfirmMessage)
        val btnConfirmCancelar: Button = dialog.findViewById(R.id.btnConfirmCancelar)
        val btnConfirmConfirmar: Button = dialog.findViewById(R.id.btnConfirmConfirmar)
        tvConfirmMessage.text = "¿Seguro que quieres eliminar el pedido de $nombre?"
        btnConfirmCancelar.setOnClickListener{
            dialog.dismiss()
        }
        btnConfirmConfirmar.setOnClickListener{
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    (itemView.context as? AppCompatActivity)?.let {
                        val adapter = it.findViewById<RecyclerView>(R.id.rvPedidos) //
                        (adapter?.adapter as? PedidosAdapter)?.removeItem(position) //
                    }
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                apiService.deletePedido(id)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

}