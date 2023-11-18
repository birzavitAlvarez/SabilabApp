package com.demo.sabilabapp.Adapters.SequenceAprovisionamiento

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
// producto data
import com.demo.sabilabapp.Productos.Result // otro
import com.demo.sabilabapp.databinding.ItemDataPedidos2spProductosBinding // OTRO
// proveedor dialog
import com.demo.sabilabapp.Proveedores.Result as ResultProveedor
import com.demo.sabilabapp.databinding.ItemDialogProveedorBinding // proveedor
//
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
// TODO PROBANDO
import kotlinx.coroutines.withContext

class AproProdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemDataPedidos2spProductosBinding = ItemDataPedidos2spProductosBinding.bind(itemView)

    // PROVEEDOR
    private var bindingDialog: ItemDialogProveedorBinding? = null
    private lateinit var adapterDialog: AproProveedorAdapter
    private val datitosDialog = mutableListOf<ResultProveedor>()
    var verduraDialog: Boolean = false
    private var currentPageDialog: Int = 1
    private var totalPagesDialog: Int = 1
    //

    fun bind (query: Result){
        binding.tvNombrePedidos2psProductos.text = query.nombre
        binding.tvPrecioPedidos2psProductos.text = query.precio.toString()

        binding.ibSeleccionarPedidos2psProductos.setOnClickListener{
            showDialogSelectProducto(itemView.context,query.id_productos)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun showDialogSelectProducto(context: Context,id_productos:Int){
        // TODO QUIERO MANDAR EL ID_PRODUCTO A
        val dialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        //dialog.setContentView(R.layout.item_dialog_proveedor)
        bindingDialog = ItemDialogProveedorBinding.inflate(inflater)
        dialog.setContentView(bindingDialog?.root!!)

        // iniciando el recycler view de proveedores
        val rvProveedorApro: RecyclerView = dialog.findViewById(R.id.rvDialogProveedor)
        adapterDialog = AproProveedorAdapter(datitosDialog)
        rvProveedorApro.layoutManager = LinearLayoutManager(context)
        rvProveedorApro.adapter = adapterDialog
        //listar
        listaAlEntrarDialog(rvProveedorApro, adapterDialog)
        var razonSocial: String = ""

        bindingDialog?.ibDialogProveedorClose?.setOnClickListener {
            dialog.dismiss()
        }

        bindingDialog?.btnDialogProveedorBuscar?.setOnClickListener {
            razonSocial = bindingDialog?.tietDialogProveedorNombre?.text.toString()
            if (razonSocial.isEmpty()){
                listaAlEntrarDialog(rvProveedorApro, adapterDialog)
            } else {
                searchByItemDialog(razonSocial)
            }
        }
        // pagina siguiente
        bindingDialog?.ibDialogProveedorNext?.setOnClickListener {
            if (currentPageDialog < totalPagesDialog) {
                currentPageDialog += 1
                if (verduraDialog) {
                    val query = bindingDialog?.tietDialogProveedorNombre?.text?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearchDialog(query,currentPageDialog)
                    }
                } else {
                    nextPageDialog(currentPageDialog)
                }
            }
        }
        // pagina anterior
        bindingDialog?.ibDialogProveedorBefore?.setOnClickListener {
            if (currentPageDialog > 1) {
                currentPageDialog -= 1
                if (verduraDialog) {
                    val query = bindingDialog?.tietDialogProveedorNombre?.text?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearchDialog(query,currentPageDialog)
                    }
                } else {
                    nextPageDialog(currentPageDialog)
                }
            }
        }

        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageDialog(np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.paginaProveedor(np)
            val response0 = request0.body()
            withContext(Dispatchers.Main) {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitosDialog.clear()
                    datitosDialog.addAll(dataItems)
                    adapterDialog.notifyDataSetChanged()
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPageDialog = pagination.currentPage
                        totalPagesDialog = pagination.totalPages
                        bindingDialog?.tvDialogProveedorNumeroPagina?.text = "$currentPageDialog/$totalPagesDialog"
                    } else {showErrorDialog()}
                } else { showErrorDialog() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageSearchDialog(query:String,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarProveedorPorNombreYPage(query, np)//.body()
            val response0 = request0.body()
            withContext(Dispatchers.Main) {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitosDialog.clear()
                    datitosDialog.addAll(dataItems)
                    adapterDialog.notifyDataSetChanged()
                    val pagination = response0?.data?.pagination
                    pagination?.currentPage!!.also { currentPageDialog = it }
                    pagination.totalPages.also { totalPagesDialog = it }
                    bindingDialog?.tvDialogProveedorNumeroPagina?.text = "$currentPageDialog/$totalPagesDialog"
                } else {
                    showErrorDialog()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByItemDialog(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarProveedorPorFiltro(query)
            val response0 = request0.body()
            withContext(Dispatchers.Main) {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitosDialog.clear()
                    datitosDialog.addAll(dataItems)
                    adapterDialog.notifyDataSetChanged()
                    performSearchDialog(query)
                } else { showErrorDialog() }
                hideKeyboardDialog()
                getCurrentAndTotalDialog()
            }
        }
    }

    private fun hideKeyboardDialog() {
        val imm = itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(bindingDialog?.vistaDialogProveedor?.windowToken, 0)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun performSearchDialog(query: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listarProveedorPorFiltro(query ?: "").body()
            withContext(Dispatchers.Main) {
                if (response != null && response.status == 200) {
                    val pagination = response.data.pagination
                    currentPageDialog = pagination.currentPage
                    totalPagesDialog = pagination.totalPages
                    bindingDialog?.tvDialogProveedorNumeroPagina?.text = "$currentPageDialog/$totalPagesDialog"
                    verduraDialog = true
                    adapterDialog.notifyDataSetChanged()
                } else { showErrorDialog() }
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrarDialog(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        CoroutineScope(Dispatchers.Main).launch {
            val request0 = apiService.listProveedorTrue()
            val response0 = request0.body()
            withContext(Dispatchers.Main) {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    (adapter as AproProveedorAdapter).updateList(dataItems)
                    recyclerView.layoutManager?.scrollToPosition(0)
                    getCurrentAndTotalDialog()
                    verduraDialog = false
                } else {
                    showErrorDialog()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getCurrentAndTotalDialog(){
        CoroutineScope(Dispatchers.Main).launch {
            val request0 = apiService.listProveedorTrue()
            val response0 = request0.body()
            withContext(Dispatchers.Main) {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPageDialog = pagination.currentPage
                        totalPagesDialog = pagination.totalPages
                        // Actualiza la vista de paginación en tu diálogo principal
                        bindingDialog?.tvDialogProveedorNumeroPagina?.text = "$currentPageDialog/$totalPagesDialog"
                    }
                    adapterDialog.notifyDataSetChanged()
                } else {
                    showErrorDialog()
                }
            }
        }
    }

    private fun showErrorDialog() {
        Toast.makeText(itemView.context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

}