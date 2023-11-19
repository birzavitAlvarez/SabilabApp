package com.demo.sabilabapp.Pedidos

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.sabilabapp.Adapters.SequenceAdmin.PedidosAdapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ActivityPedidosBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class PedidosActivity : AppCompatActivity() {

    private var binding: ActivityPedidosBinding? = null
    var id_vendedor: Int = 0
    private lateinit var adapter: PedidosAdapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1
    private var verdura: Boolean = false

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //
        var selectedDate: String = ""
        var nomcome: String = ""
        val fecha_del_dia = obtenerFechaActual()
        //
        initRecyclerView()
        listaAlEntrar(fecha_del_dia)
        //
        binding?.tietPedidosFecha?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                binding?.tietPedidosFecha?.setText(selectedDate)
            }, year, month, day)
            datePickerDialog.show()
        }
        //
        binding?.ibPedidosRefresh?.setOnClickListener {
            listaAlEntrar(fecha_del_dia)
            binding?.tietPedidosFecha?.setText("")
            binding?.tietPedidosnomcme?.setText("")
            selectedDate = ""
        }
        //
        binding?.btnPedidosBuscar?.setOnClickListener {
            nomcome = binding?.tietPedidosnomcme?.text.toString()
            if (nomcome.isEmpty() && selectedDate.isEmpty()){
                listaAlEntrar(fecha_del_dia)
            } else if (selectedDate.isEmpty() && nomcome.isNotEmpty()) {
                searchByItem(fecha_del_dia,nomcome)
            } else if (selectedDate.isNotEmpty() && nomcome.isEmpty()){
                searchByItem(selectedDate,nomcome)
            } else {
                searchByItem(selectedDate,nomcome)
            }
        }
        //
        // pagina siguiente
        binding?.ibPedidosNext?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                if (verdura) {
                    nomcome = binding?.tietPedidosnomcme?.text.toString()
                    nextPageSearch(selectedDate, nomcome ,currentPage)
                } else {
                    nextPage(fecha_del_dia, currentPage)
                }
            }
        }
        // pagina anterior
        binding?.ibPedidosBefore?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                if (verdura) {
                    nomcome = binding?.tietPedidosnomcme?.text.toString()
                    nextPageSearch(selectedDate, nomcome, currentPage)
                } else {
                    nextPage(fecha_del_dia, currentPage)
                }
            }
        }
        //

        binding?.btnPedidosNuevoPedido?.setOnClickListener{
            val anny = Intent(this@PedidosActivity, PedidosscActivity::class.java)
            startActivity(anny)
        }

    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPage(fecha:String,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.paginaPedidosAdminTrue(fecha,np)
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
                        binding?.tvPedidosNumeroPagina?.text = "$currentPage/$totalPages"
                    } else {showError()}
                } else { showError() }
            }
        }
    }
    //listarPedidosPorNombreYPage
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageSearch(fecha:String, nomcome:String ,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarAdminPedidosPorNombreYPage(fecha, nomcome, np)//.body()
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
                    binding?.tvPedidosNumeroPagina?.text = "$currentPage/$totalPages"
                } else {
                    showError()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByItem(query: String, query2: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarAdminPedidosPorFiltro(query?:"",query2?:"")
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    performSearch(query, query2)
                } else { showError() }
                hideKeyboard()
                getCurrentAndTotal(query)
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.vistaPedidosPadre?.windowToken,0)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun performSearch(query: String?,query2: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listarAdminPedidosPorFiltro(query ?: "", query2 ?: "").body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val pagination = response.data.pagination
                    currentPage = pagination.currentPage
                    totalPages = pagination.totalPages
                    binding?.tvPedidosNumeroPagina?.text = "$currentPage/$totalPages"
                    verdura = true
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrar(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listPedidosAdminTrue(query)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    getCurrentAndTotal(query)
                    verdura = false
                } else { showError() }
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getCurrentAndTotal(query:String){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listPedidosAdminTrue(query )
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvPedidosNumeroPagina?.text = "$currentPage/$totalPages"
                    }
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    private fun initRecyclerView() {
        adapter = PedidosAdapter(datitos)
        binding?.rvPedidos?.layoutManager = LinearLayoutManager(this)
        binding?.rvPedidos?.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerFechaActual(): String {
        val fechaActual = LocalDate.now()
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return fechaActual.format(formato)
    }
}