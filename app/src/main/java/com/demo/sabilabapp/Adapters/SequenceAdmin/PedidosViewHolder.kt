package com.demo.sabilabapp.Adapters.SequenceAdmin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Adapters.ProductosAdapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Pedidos.Result // otro
import com.demo.sabilabapp.Pedidos.PedidosUpdate4 //para update
import com.demo.sabilabapp.databinding.ItemAddDatosenvioBinding // para el dialog de Edit
import com.demo.sabilabapp.databinding.ItemPedidosBinding // OTRO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar
import java.util.Locale


class PedidosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemPedidosBinding = ItemPedidosBinding.bind(itemView)
    // EDIT
    private var bindingEdit: ItemAddDatosenvioBinding? = null

    fun bind (query: Result){
        binding.tvClientePedidos.text = query.nombre_comercial
        binding.tvDireccionPedidos.text = query.direccion
        binding.tvFechaPedidoPedidos.text = query.fecha_pedido
        binding.tvFechaEntregaPedidos.text = query.fecha_entrega
        binding.tvFechaLlegadaPedidos.text = query.fecha_llegada
        binding.tvTotalPedidos.text = query.total_pedido.toString()

        binding.ibPedidosEdit.setOnClickListener{
            showDialogEditPedidos(itemView.context,query.id_pedido)
        }

        binding.ibPedidosDelete.setOnClickListener {
//            showDialogDeletePedidos(itemView.context,query.id_productos,query.nombre)
        }

        binding.ibPedidosSheck.setOnClickListener {

        }


    }

    @SuppressLint("SetTextI18n")
    private fun showDialogEditPedidos(context: Context,id_pedido:Int){
        val dialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        //dialog.setContentView(R.layout.item_add_datosenvio)
        bindingEdit = ItemAddDatosenvioBinding.inflate(inflater)
        dialog.setContentView(bindingEdit?.root!!)


//        val tvAddProductosTitle: TextView = dialog.findViewById(R.id.tvAddProductosTitle)
//        val ibAddProductosClose: ImageButton = dialog.findViewById(R.id.ibAddProductosClose)
//        val tilAddProductosNombre: TextInputLayout = dialog.findViewById(R.id.tilAddProductosNombre)
//        val tietAddProductosNombre: TextInputEditText = dialog.findViewById(R.id.tietAddProductosNombre)
//        val tilAddProductosLaboratorio: TextInputLayout = dialog.findViewById(R.id.tilAddProductosLaboratorio)
//        val tietAddProductosLaboratorio: TextInputEditText = dialog.findViewById(R.id.tietAddProductosLaboratorio)
//        val tilAddProductosLote: TextInputLayout = dialog.findViewById(R.id.tilAddProductosLote)
//        val tietAddProductosLote: TextInputEditText = dialog.findViewById(R.id.tietAddProductosLote)
//        val tilAddProductosPrecio: TextInputLayout = dialog.findViewById(R.id.tilAddProductosPrecio)
//        val tietAddProductosPrecio: TextInputEditText = dialog.findViewById(R.id.tietAddProductosPrecio)
//        val tilAddProductosCaducidad: TextInputLayout = dialog.findViewById(R.id.tilAddProductosCaducidad)
//        val tietAddProductosCaducidad: TextInputEditText = dialog.findViewById(R.id.tietAddProductosCaducidad)
//        val tilAddProductosCategoria: TextInputLayout = dialog.findViewById(R.id.tilAddProductosCategoria)
//        val spAddProductosCategoria: Spinner = dialog.findViewById(R.id.spAddProductosCategoria)
//        val btnAddProductosGuardar: Button = dialog.findViewById(R.id.btnAddProductosGuardar)
//        //llenando datos para el update
//        tvAddProductosTitle.text = "Actualizar Producto"
//        tietAddProductosNombre.setText(nombre)
//        tietAddProductosLaboratorio.setText(laboratorio)
//        tietAddProductosLote.setText(lote)
//        tietAddProductosPrecio.setText(precio.toString())
//        tietAddProductosCaducidad.setText(fecha_caducidad)
//        tietAddProductosNombre.requestFocus()
//
//        ibAddProductosClose.setOnClickListener{
//            dialog.dismiss()
//        }
//
//        var selectedDate: String = fecha_caducidad
//
//        tietAddProductosCaducidad.setOnClickListener {
//            val calendar = Calendar.getInstance()
//            val year = calendar.get(Calendar.YEAR)
//            val month = calendar.get(Calendar.MONTH)
//            val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//            val datePickerDialog = DatePickerDialog(context, { _, year, month, dayOfMonth ->
//                selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
//                tietAddProductosCaducidad.setText(selectedDate)
//            }, year, month, day)
//            tilAddProductosCaducidad.error = null
//            datePickerDialog.show()
//        }
//
//        //
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = apiService.listCategory().body()
//            (itemView.context as? AppCompatActivity)?.runOnUiThread {
//                if (response != null && response.status == 200) {
//                    val categoriasList = response.data.map { it.tipo }
//                    val categoriasWithSelect = listOf("Seleccionar") + categoriasList
//                    val adapterLoad = ArrayAdapter(context, android.R.layout.simple_spinner_item, categoriasWithSelect)
//                    adapterLoad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                    spAddProductosCategoria.adapter = adapterLoad
//                    adapterLoad.notifyDataSetChanged()
//                    // lleno el spinner y establezco posicion
//                    spAddProductosCategoria.setSelection(id_categoria)
//                } else {
//                    Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//        // -----------------------------
//        var selectedCategoriaId: Int? = id_categoria
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = apiService.listCategory().body()
//            spAddProductosCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                    selectedCategoriaId = if (position > 0) {
//                        tilAddProductosCategoria.error = null
//                        response?.data?.get(position - 1)?.id_categoria
//                    } else { null }
//                }
//                override fun onNothingSelected(parent: AdapterView<*>?) { selectedCategoriaId = null }
//            }
//        }
//        //TODO VALIDAR
//        tietAddProductosNombre.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                tilAddProductosNombre.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//            }
//        })
//
//        tietAddProductosLaboratorio.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                tilAddProductosLaboratorio.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//            }
//        })
//
//        tietAddProductosPrecio.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                tilAddProductosPrecio.error = if (s?.any { it.isDigit() } == true) null else "ES REQUERIDO"
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//            }
//        })
//        //TODO FIN VALIDAR
//        //
//        btnAddProductosGuardar.setOnClickListener{
//
//            val categoriaSeleccionada = spAddProductosCategoria.selectedItem.toString()
//
//            if (tietAddProductosNombre.text.toString().isEmpty()){
//                tilAddProductosNombre.error = "ES REQUERIDO"
//                return@setOnClickListener
//            } else if (tietAddProductosLaboratorio.text.toString().isEmpty()){
//                tilAddProductosLaboratorio.error = "ES REQUERIDO"
//                return@setOnClickListener
//            } else if (tietAddProductosPrecio.text.toString().isEmpty()){
//                tilAddProductosPrecio.error = "ES REQUERIDO"
//                return@setOnClickListener
//            } else if (tietAddProductosCaducidad.text.toString().isEmpty()){
//                tilAddProductosCaducidad.error = "ES REQUERIDO"
//                return@setOnClickListener
//            } else if (categoriaSeleccionada == "Seleccionar"){
//                tilAddProductosCategoria.error = "ES REQUERIDO"
//                return@setOnClickListener
//            }
//
//            var actPro = 0
//            val nomPro = tietAddProductosNombre.text.toString()
//            val labPro = tietAddProductosLaboratorio.text.toString()
//            val prePro = tietAddProductosPrecio.text.toString().toDouble()
//            val lotPro = tietAddProductosLote.text.toString()
//            val cadPro = selectedDate
//            if (activo){ actPro = 1 } // si es true manda 1 para el active true xd
//
//            CoroutineScope(Dispatchers.IO).launch {
//                val producto = Productos(nomPro, labPro, prePro, lotPro, cadPro, actPro, selectedCategoriaId!!)
//                apiService.updateProductos(producto, id_productos)
//
//                val updatedData = apiService.listProductosTrue().body()?.data?.results
//
//                (itemView.context as? AppCompatActivity)?.runOnUiThread {
//                    if (updatedData != null) {
//                        (itemView.context as? AppCompatActivity)?.let {
//                            val adapter = it.findViewById<RecyclerView>(R.id.rvProductos)
//                            (adapter?.adapter as? ProductosAdapter)?.updateList(updatedData)
//                        }
//                    }
//                    dialog.dismiss()
//                }
//            }
//        }
//
        dialog.show()
    }

//    @SuppressLint("SetTextI18n")
//    private fun showDialogDeletePedidos(context: Context,id:Int,nombre:String){
//        val dialog = Dialog(context)
//        dialog.setContentView(R.layout.item_confirm)
//
//        val tvConfirmMessage: TextView = dialog.findViewById(R.id.tvConfirmMessage)
//        val btnConfirmCancelar: Button = dialog.findViewById(R.id.btnConfirmCancelar)
//        val btnConfirmConfirmar: Button = dialog.findViewById(R.id.btnConfirmConfirmar)
//        tvConfirmMessage.text = "¿Seguro que quieres eliminar el producto $nombre?"
//        btnConfirmCancelar.setOnClickListener{
//            dialog.dismiss()
//        }
//        btnConfirmConfirmar.setOnClickListener{
//            val position = adapterPosition
//            if (position != RecyclerView.NO_POSITION) {
//                (itemView.context as? AppCompatActivity)?.runOnUiThread {
//                    (itemView.context as? AppCompatActivity)?.let {
//                        val adapter = it.findViewById<RecyclerView>(R.id.rvProductos) //
//                        (adapter?.adapter as? ProductosAdapter)?.removeItem(position) //
//                    }
//                }
//            }
//            CoroutineScope(Dispatchers.IO).launch {
//                apiService.deleteProductos(id)
//            }
//            dialog.dismiss()
//        }
//        dialog.show()
//    }

}