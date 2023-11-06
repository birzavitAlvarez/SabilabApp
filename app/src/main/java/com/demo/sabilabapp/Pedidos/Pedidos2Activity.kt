package com.demo.sabilabapp.Pedidos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.sabilabapp.Adapters.Pedidos2Adapter // OTRO
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.Pedidos.Result // OTRO
import com.demo.sabilabapp.Pedidos.Pedidos // OTRO
import com.demo.sabilabapp.databinding.ActivityPedidos2Binding // OTRO
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

// Para obtener la fecha
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar


class Pedidos2Activity : AppCompatActivity() {

    var id_vendedor: Int = 4


    private var binding: ActivityPedidos2Binding? = null

    private lateinit var adapter: Pedidos2Adapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1
    private var verdura: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidos2Binding.inflate(layoutInflater)
        setContentView(binding?.root)
        var selectedDate: String = ""
        var nomcome: String = ""
        //id_vendedor = intent.getIntExtra("id_vendedor", 0)
        //Log.d("MiApp", "Valor de id_vendedor antes de iniciar actividad: $id_vendedor")
        val fecha_del_dia = obtenerFechaActual()

        initRecyclerView()
        listaAlEntrar(id_vendedor, fecha_del_dia)
        //
        binding?.ibPedidos2Refresh?.setOnClickListener {
            listaAlEntrar(id_vendedor, fecha_del_dia)
        }

        binding?.tietPedidos2Fecha?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                binding?.tietPedidos2Fecha?.setText(selectedDate)
            }, year, month, day)
            datePickerDialog.show()
        }

        binding?.btnPedidos2Buscar?.setOnClickListener {
                nomcome = binding?.tietPedidos2nomcme?.text.toString()
                searchByItem(id_vendedor,selectedDate,nomcome)
        }
        // pagina siguiente
        binding?.ibPedidos2Next?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                if (verdura) {
                    nomcome = binding?.tietPedidos2nomcme?.text.toString()
                    nextPageSearch(id_vendedor, selectedDate, nomcome ,currentPage)
                } else {
                    nextPage(id_vendedor,fecha_del_dia, currentPage)
                }
            }
        }
        // pagina anterior
        binding?.ibPedidos2Before?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                if (verdura) {
                    nomcome = binding?.tietPedidos2nomcme?.text.toString()
                        nextPageSearch(id_vendedor, selectedDate, nomcome, currentPage)
                } else {
                    nextPage(id_vendedor,fecha_del_dia, currentPage)
                }
            }
        }
        // boton nuevo cliente
        binding?.btnPedidos2NuevoPedido?.setOnClickListener{
            //showDialogAddClientes2(id_vendedor)
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerFechaActual(): String {
        val fechaActual = LocalDate.now()
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return fechaActual.format(formato)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPage(id: Int, fecha:String,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.paginaPedidosVendedor(id,fecha,np)
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
                        binding?.tvPedidos2NumeroPagina?.text = "$currentPage/$totalPages"
                    } else {showError()}
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageSearch(id: Int, fecha:String, nomcome:String ,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarPedidosPorNombreYPage(id, fecha, nomcome, np)//.body()
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
                    binding?.tvPedidos2NumeroPagina?.text = "$currentPage/$totalPages"
                } else {
                    showError()
                }
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrar(id:Int, query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listPedidosVendedorTrue(id, query)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    getCurrentAndTotal(id, query)
                    verdura = false
                } else { showError() }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = Pedidos2Adapter(datitos)
        binding?.rvPedidos2?.layoutManager = LinearLayoutManager(this)
        binding?.rvPedidos2?.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByItem(id: Int,query: String, query2: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarPedidosPorFiltro(id, query,query2) //query ?: ""
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    performSearch(id, query, query2)
                } else { showError() }
                hideKeyboard()
                getCurrentAndTotal(id, query)
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getCurrentAndTotal(id:Int, query:String){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listPedidosVendedorTrue(id,query )
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvPedidos2NumeroPagina?.text = "$currentPage/$totalPages"
                    }
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.vistaPedidos2Padre?.windowToken,0)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun performSearch(id: Int,query: String?,query2: String? ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listarPedidosPorFiltro(id, query ?: "", query2 ?: "").body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val pagination = response.data.pagination
                    currentPage = pagination.currentPage
                    totalPages = pagination.totalPages
                    binding?.tvPedidos2NumeroPagina?.text = "$currentPage/$totalPages"
                    verdura = true
                    adapter.notifyDataSetChanged() //TODO
                } else { showError() }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

}