package com.demo.sabilabapp.Productos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import android.annotation.SuppressLint
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.sabilabapp.Adapters.Productos2Adapter // OTRO
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.Productos.Result // OTRO
import com.demo.sabilabapp.Productos.Productos // OTRO
import com.demo.sabilabapp.databinding.ActivityProductos2Binding // OTRO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class Productos2Activity : AppCompatActivity(), OnQueryTextListener {

    private var binding: ActivityProductos2Binding? = null

    private lateinit var adapter: Productos2Adapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1
    private var verdura: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductos2Binding.inflate(layoutInflater)
        setContentView(binding?.root)

        initRecyclerView()
        listaAlEntrar()

        binding?.svProductos2Busqueda?.setOnQueryTextListener(this)

        binding?.btnProductos2Buscar?.setOnClickListener {
            val query = binding?.svProductos2Busqueda?.query?.toString()
            if (!query.isNullOrBlank()) {
                searchByItem(query)
            }
        }

        // pagina siguiente
        binding?.ibProductos2Next?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                if (verdura) {
                    val query = binding?.svProductos2Busqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,currentPage)
                    }
                } else {
                    nextPage(currentPage)
                }
            }
        }
        // pagina anterior
        binding?.ibProductos2Before?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                if (verdura) {
                    val query = binding?.svProductos2Busqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,currentPage)
                    }
                } else {
                    nextPage(currentPage)
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPage(np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.paginaProductos(np)
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
                        binding?.tvProductos2NumeroPagina?.text = "$currentPage/$totalPages"
                    } else {showError()}
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageSearch(query:String,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarProductosPorNombreYPage(query, np)//.body()
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
                    binding?.tvProductos2NumeroPagina?.text = "$currentPage/$totalPages"
                } else {
                    showError()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByItem(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarProductosPorFiltro(query)
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
                getCurrentAndTotal()
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getCurrentAndTotal(){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listProductosTrue()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvProductos2NumeroPagina?.text = "$currentPage/$totalPages"
                    }
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrar() {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listProductosTrue()
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

    private fun initRecyclerView() {
        adapter = Productos2Adapter(datitos)
        binding?.rvProductos2?.layoutManager = LinearLayoutManager(this)
        binding?.rvProductos2?.adapter = adapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchByItem(query.lowercase(Locale.ROOT))
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.vistaProductos2Padre?.windowToken,0)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun performSearch(query: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listarProductosPorFiltro(query ?: "").body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val pagination = response.data.pagination
                    currentPage = pagination.currentPage
                    totalPages = pagination.totalPages
                    binding?.tvProductos2NumeroPagina?.text = "$currentPage/$totalPages"
                    verdura = true
                    adapter.notifyDataSetChanged() //TODO
                } else { showError() }
            }
        }
    }
}