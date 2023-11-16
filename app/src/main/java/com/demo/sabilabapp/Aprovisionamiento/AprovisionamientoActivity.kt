package com.demo.sabilabapp.Aprovisionamiento

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.sabilabapp.R
//
import com.demo.sabilabapp.Adapters.AprovisionamientoAdapter// otro
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.Aprovisionamiento.Result // otro
import com.demo.sabilabapp.databinding.ActivityAprovisionamientoBinding //
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class AprovisionamientoActivity : AppCompatActivity() {

    private var binding: ActivityAprovisionamientoBinding? = null

    private lateinit var adapter: AprovisionamientoAdapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1
    private var verdura: Boolean = false

    var idProveedores: Int = 0
    var idProductos: Int = 0


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


    }

    private fun showDialogProductosAprovi() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.item_dialog_pedidos2sp_productos)

        dialog.show()
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