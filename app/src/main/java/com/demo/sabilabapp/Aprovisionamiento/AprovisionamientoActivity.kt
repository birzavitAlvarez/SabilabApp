package com.demo.sabilabapp.Aprovisionamiento

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
//
import com.demo.sabilabapp.Adapters.AprovisionamientoAdapter// otro
import com.demo.sabilabapp.Adapters.SequenceAprovisionamiento.AproProdAdapter
//import com.demo.sabilabapp.Adapters.SequencePedidos.Pedidos2spProductosAdapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.Aprovisionamiento.Result // otro
import com.demo.sabilabapp.databinding.ActivityAprovisionamientoBinding //
import com.demo.sabilabapp.databinding.ItemDialogPedidos2spProductosBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import com.demo.sabilabapp.Productos.Result as ResultProductos

class AprovisionamientoActivity : AppCompatActivity() {

    private var binding: ActivityAprovisionamientoBinding? = null

    private lateinit var adapter: AprovisionamientoAdapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1
    private var verdura: Boolean = false

    var idProveedores: Int = 0
    var idProductos: Int = 0

    //
    // adapter y data pal dialog productos
    private var bindingDialog: ItemDialogPedidos2spProductosBinding? = null
    private lateinit var adapterProductosDialog: AproProdAdapter
    private val datitosProductosDialog = mutableListOf<ResultProductos>()
    var verduraDialog: Boolean = false
    private var currentPageDialog: Int = 1
    private var totalPagesDialog: Int = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAprovisionamientoBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        var selectedDate: String = ""
        initRecyclerView()
        listaAlEntrar()
        // calendario
        binding?.tietAprovisionamientoFecha?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                binding?.tietAprovisionamientoFecha?.setText(selectedDate)
            }, year, month, day)
            datePickerDialog.show()
        }

        binding?.ibAprovisionamientoRefresh?.setOnClickListener {
            listaAlEntrar()
            binding?.tietAprovisionamientoFecha?.setText("")
            selectedDate = ""
        }

        binding?.btnAprovisionamientoBuscar?.setOnClickListener {
            if (selectedDate.isEmpty()){
                listaAlEntrar()
            } else {
                searchByItem(selectedDate)
            }
        }

        // pagina siguiente
        binding?.ibAprovisionamientoNext?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                if (verdura) {
                    nextPageSearch(selectedDate ,currentPage)
                } else {
                    nextPage(currentPage)
                }
            }
        }
        // pagina anterior
        binding?.ibAprovisionamientoBefore?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                if (verdura) {
                    nextPageSearch(selectedDate, currentPage)
                } else {
                    nextPage(currentPage)
                }
            }
        }

        binding?.btnAprovisionamientoNuevaCompra?.setOnClickListener {
            showDialogProductosAprovi()
        }

        binding?.ibBackAprovisionamiento?.setOnClickListener { onBackPressed() }
    }

    private fun showDialogProductosAprovi() {
        val dialog = Dialog(this)
        bindingDialog = ItemDialogPedidos2spProductosBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog?.root!!)

        // iniciando el recycler view de productos
        val rvPedidos2spProductos: RecyclerView = dialog.findViewById(R.id.rvPedidos2spProductos)
        adapterProductosDialog = AproProdAdapter(datitosProductosDialog)
        rvPedidos2spProductos.layoutManager = LinearLayoutManager(this)
        rvPedidos2spProductos.adapter = adapterProductosDialog
        //listar
        listaAlEntrarDialog(rvPedidos2spProductos, adapterProductosDialog)
        var nombrepro: String = ""

        bindingDialog?.ibPedidos2spProductosClose?.setOnClickListener {
            dialog.dismiss()
        }

        bindingDialog?.btnPedidos2spProductosBuscar?.setOnClickListener {
            nombrepro = bindingDialog?.tietPedidos2spProductosNombre?.text.toString()
            if (nombrepro.isEmpty()){
                listaAlEntrarDialog(rvPedidos2spProductos, adapterProductosDialog)
            } else {
                searchByItemDialog(nombrepro)
            }
        }

        // pagina siguiente
        bindingDialog?.ibPedidos2spProductosNext?.setOnClickListener {
            if (currentPageDialog < totalPagesDialog) {
                currentPageDialog += 1
                if (verduraDialog) {
                    val query = bindingDialog?.tietPedidos2spProductosNombre?.text?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearchDialog(query,currentPageDialog)
                    }
                } else {
                    nextPageDialog(currentPageDialog)
                }
            }
        }
        // pagina anterior
        bindingDialog?.ibPedidos2spProductosBefore?.setOnClickListener {
            if (currentPageDialog > 1) {
                currentPageDialog -= 1
                if (verduraDialog) {
                    val query = bindingDialog?.tietPedidos2spProductosNombre?.text?.toString()
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
            val request0 = apiService.paginaProductos(np)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitosProductosDialog.clear()
                    datitosProductosDialog.addAll(dataItems)
                    adapterProductosDialog.notifyDataSetChanged()
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPageDialog = pagination.currentPage
                        totalPagesDialog = pagination.totalPages
                        bindingDialog?.tvPedidos2spProductosNumeroPagina?.text = "$currentPageDialog/$totalPagesDialog"
                    } else {showErrorDialog()}
                } else { showErrorDialog() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageSearchDialog(query:String,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarProductosPorNombreYPage(query, np)//.body()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitosProductosDialog.clear()
                    datitosProductosDialog.addAll(dataItems)
                    adapterProductosDialog.notifyDataSetChanged()
                    val pagination = response0?.data?.pagination
                    pagination?.currentPage!!.also { currentPageDialog = it }
                    pagination.totalPages.also { totalPagesDialog = it }
                    bindingDialog?.tvPedidos2spProductosNumeroPagina?.text = "$currentPageDialog/$totalPagesDialog"
                } else {
                    showErrorDialog()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByItemDialog(nombrepro: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarProductosPorFiltro(nombrepro)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitosProductosDialog.clear()
                    datitosProductosDialog.addAll(dataItems)
                    adapterProductosDialog.notifyDataSetChanged()
                    performSearchDialog(nombrepro)
                } else { showErrorDialog() }
                hideKeyboardDialog()
                getCurrentAndTotalDialog()
            }
        }
    }

    private fun hideKeyboardDialog() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(bindingDialog?.vistaPedidos2spProductosPadre?.windowToken,0)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun performSearchDialog(nombrepro: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listarProductosPorFiltro(nombrepro ?: "").body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val pagination = response.data.pagination
                    currentPageDialog = pagination.currentPage
                    totalPagesDialog = pagination.totalPages
                    bindingDialog?.tvPedidos2spProductosNumeroPagina?.text = "$currentPageDialog/$totalPagesDialog"
                    verduraDialog = true
                    adapterProductosDialog.notifyDataSetChanged()
                } else { showErrorDialog() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrarDialog(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listProductosTrue()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    (adapter as AproProdAdapter).updateList(dataItems)
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
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listProductosTrue()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPageDialog = pagination.currentPage
                        totalPagesDialog = pagination.totalPages
                        // Actualiza la vista de paginación en tu diálogo principal
                        bindingDialog?.tvPedidos2spProductosNumeroPagina?.text = "$currentPageDialog/$totalPagesDialog"
                    }
                    adapterProductosDialog.notifyDataSetChanged()
                } else {
                    showErrorDialog()
                }
            }

        }
    }

    private fun showErrorDialog() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPage(np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.paginaAprovisionamiento(np)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvAprovisionamientoNumeroPagina?.text = "$currentPage/$totalPages"
                    } else {showError()}
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageSearch(fecha:String, np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarAprovisionamientoPorFechaYPage(fecha, np)//.body()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    val pagination = response0?.data?.pagination
                    pagination?.currentPage!!.also { currentPage = it }
                    pagination.totalPages.also { totalPages = it }
                    binding?.tvAprovisionamientoNumeroPagina?.text = "$currentPage/$totalPages"
                } else {
                    showError()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByItem(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listAprovisionamientoByFecha(query?:"") //query ?: ""
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    performSearch(query)
                } else { showError() }
                hideKeyboard()
                getCurrentAndTotal2(query)
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getCurrentAndTotal2(query:String){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listAprovisionamientoByFecha(query)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvAprovisionamientoNumeroPagina?.text = "$currentPage/$totalPages"
                    }
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.vistaAprovisionamientoPadre?.windowToken,0)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun performSearch(query: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listAprovisionamientoByFecha(query ?: "").body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val pagination = response.data.pagination
                    currentPage = pagination.currentPage
                    totalPages = pagination.totalPages
                    binding?.tvAprovisionamientoNumeroPagina?.text = "$currentPage/$totalPages"
                    verdura = true
                    adapter.notifyDataSetChanged() //TODO
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrar() {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listAprovisionamiento()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    getCurrentAndTotal()
                    verdura = false
                } else { showError() }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getCurrentAndTotal(){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listAprovisionamiento()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvAprovisionamientoNumeroPagina?.text = "$currentPage/$totalPages"
                    }
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = AprovisionamientoAdapter(datitos)
        binding?.rvAprovisionamiento?.layoutManager = LinearLayoutManager(this)
        binding?.rvAprovisionamiento?.adapter = adapter
    }

}