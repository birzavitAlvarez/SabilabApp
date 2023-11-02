package com.demo.sabilabapp.Clientes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.sabilabapp.Adapters.Clientes2Adapter // OTRO
import com.demo.sabilabapp.Adapters.Productos2Adapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.Clientes.Result // OTRO
import com.demo.sabilabapp.Clientes.Clientes // OTRO
import com.demo.sabilabapp.databinding.ActivityClientes2Binding // OTRO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class Clientes2Activity : AppCompatActivity(), OnQueryTextListener {

    var id_vendedor: Int = 4

    private var binding: ActivityClientes2Binding? = null

    private lateinit var adapter: Clientes2Adapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1
    private var verdura: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientes2Binding.inflate(layoutInflater)
        setContentView(binding?.root)

        initRecyclerView()
        listaAlEntrar(id_vendedor)

        binding?.svClientes2Busqueda?.setOnQueryTextListener(this)

        binding?.btnClientes2Buscar?.setOnClickListener {
            val query = binding?.svClientes2Busqueda?.query?.toString()
            if (!query.isNullOrBlank()) {
                searchByItem(query, id_vendedor)
            }
        }

        // pagina siguiente
        binding?.ibClientes2Next?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                if (verdura) {
                    val query = binding?.svClientes2Busqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,id_vendedor ,currentPage)
                    }
                } else {
                    nextPage(id_vendedor,currentPage)
                }
            }
        }
        // pagina anterior
        binding?.ibClientes2Before?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                if (verdura) {
                    val query = binding?.svClientes2Busqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,id_vendedor ,currentPage)
                    }
                } else {
                    nextPage(id_vendedor,currentPage)
                }
            }
        }
        // boton nuevo cliente
        binding?.btnClientes2Agregar?.setOnClickListener{
            showDialog()
        }



    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.item_add_clientes)

        Toast.makeText(this, "PRONTO PODRAS AGREGAR RAAA", Toast.LENGTH_SHORT).show()
        //
        dialog.show()
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
                        binding?.tvClientes2NumeroPagina?.text = "$currentPage/$totalPages"
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
                    binding?.tvClientes2NumeroPagina?.text = "$currentPage/$totalPages"
                } else {
                    showError()
                }
            }
        }
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
        imm.hideSoftInputFromWindow(binding?.vistaClientes2Padre?.windowToken,0)
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
                    binding?.tvClientes2NumeroPagina?.text = "$currentPage/$totalPages"
                    verdura = true
                    adapter.notifyDataSetChanged() //TODO
                } else { showError() }
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

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
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
                        binding?.tvClientes2NumeroPagina?.text = "$currentPage/$totalPages"
                    }
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = Clientes2Adapter(datitos)
        binding?.rvClientes2?.layoutManager = LinearLayoutManager(this)
        binding?.rvClientes2?.adapter = adapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchByItem(query.lowercase(Locale.ROOT), id_vendedor)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}