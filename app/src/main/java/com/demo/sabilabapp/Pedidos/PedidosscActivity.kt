package com.demo.sabilabapp.Pedidos

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity//
import android.os.Bundle//
import android.os.Handler
import android.os.Looper
import com.demo.sabilabapp.R//
//
import android.view.View//
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView//
import android.widget.ArrayAdapter//
import android.widget.Toast//
import androidx.recyclerview.widget.LinearLayoutManager//
import com.demo.sabilabapp.Adapters.SequenceAdmin.PedidosscAdapter// otre
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.Clientes.Result// otre
import com.demo.sabilabapp.databinding.ActivityPedidosscBinding // otre
import androidx.appcompat.widget.SearchView.OnQueryTextListener//
import kotlinx.coroutines.CoroutineScope//
import kotlinx.coroutines.Dispatchers//
import kotlinx.coroutines.launch//
import java.util.Locale

class PedidosscActivity : AppCompatActivity(), OnQueryTextListener {

    private var binding: ActivityPedidosscBinding? = null

    private lateinit var adapter: PedidosscAdapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1
    private var verdura: Boolean = false
    var id_vendedor:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosscBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initRecyclerView()
        // debo llenar el spiner y obtener el id_vendedor para irlo pasando
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listVendedoresTrue().body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val vendedoresList = response.data.results.map { it.nombres } // TODO waaaaaaa
                    val categoriasWithSelect = listOf("Seleccionar") + vendedoresList
                    val adapterLoad = ArrayAdapter(this@PedidosscActivity, android.R.layout.simple_spinner_item, categoriasWithSelect)
                    adapterLoad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.spPedidosscClientesVendedor?.adapter = adapterLoad
                    adapterLoad.notifyDataSetChanged()
                } else {
                    showError()
                }
            }
        }
        // -----------------------------
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listVendedoresTrue().body()
            binding?.spPedidosscClientesVendedor?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position > 0) {
                        val selectedVendedor = response?.data?.results?.get(position - 1)
                        id_vendedor = selectedVendedor?.id_vendedor ?: 0
                        listaAlEntrar(id_vendedor)
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // No hacer nada :v
                }
            }
        }
        listaAlEntrar(id_vendedor)
        //
        binding?.svPedidosscBusqueda?.setOnQueryTextListener(this)

        binding?.btnPedidosscBuscar?.setOnClickListener {
            val query = binding?.svPedidosscBusqueda?.query?.toString()
            if (!query.isNullOrBlank()) {
                searchByItem(query, id_vendedor)
            }
        }
        //
        // pagina siguiente
        binding?.ibPedidosscNext?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                if (verdura) {
                    val query = binding?.svPedidosscBusqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,id_vendedor ,currentPage)
                    }
                } else {
                    nextPage(id_vendedor,currentPage)
                }
            }
        }
        // pagina anterior
        binding?.ibPedidosscBefore?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                if (verdura) {
                    val query = binding?.svPedidosscBusqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,id_vendedor ,currentPage)
                    }
                } else {
                    nextPage(id_vendedor,currentPage)
                }
            }
        }

        binding?.btnPedidosscCancelar?.setOnClickListener {
            onBackPressed()
        }

        binding?.ibBackPedidossc?.setOnClickListener { onBackPressed() }
    }



    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPage(id: Int,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.paginaClientes(id,np)
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
                        binding?.tvPedidosscNumeroPagina?.text = "$currentPage/$totalPages"
                    } else {showError()}
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageSearch(query:String,id: Int ,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarClientesPorNombreYPage(query, id, np)//.body()
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
                    binding?.tvPedidosscNumeroPagina?.text = "$currentPage/$totalPages"
                } else {
                    showError()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrar(id:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listClientesTrue(id)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    getCurrentAndTotal(id)
                    verdura = false
                } else { showError() }
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getCurrentAndTotal(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listClientesTrue(id)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvPedidosscNumeroPagina?.text = "$currentPage/$totalPages"
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
        adapter = PedidosscAdapter(datitos)
        binding?.rvPedidossc?.layoutManager = LinearLayoutManager(this)
        binding?.rvPedidossc?.adapter = adapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchByItem(query.lowercase(Locale.ROOT), id_vendedor)
        }
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByItem(query: String, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarClientesPorFiltro(query, id)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    performSearch(query, id)
                } else { showError() }
                hideKeyboard()
                getCurrentAndTotal(id)
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.vistaPedidosscPadre?.windowToken,0)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun performSearch(query: String?, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listarClientesPorFiltro(query ?: "", id).body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val pagination = response.data.pagination
                    currentPage = pagination.currentPage
                    totalPages = pagination.totalPages
                    binding?.tvPedidosscNumeroPagina?.text = "$currentPage/$totalPages"
                    verdura = true
                    adapter.notifyDataSetChanged() //TODO
                } else { showError() }
            }
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}