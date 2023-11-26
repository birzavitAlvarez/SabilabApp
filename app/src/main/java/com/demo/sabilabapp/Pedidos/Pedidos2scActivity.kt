package com.demo.sabilabapp.Pedidos

import androidx.appcompat.app.AppCompatActivity//
import android.os.Bundle//
import com.demo.sabilabapp.R//
import android.annotation.SuppressLint//
import android.app.Dialog//
import android.util.Log//
import android.view.inputmethod.InputMethodManager//
import android.widget.Button//
import android.widget.ImageButton//
import android.widget.TextView//
import android.widget.Toast//
import androidx.appcompat.widget.SearchView.OnQueryTextListener//
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText//
import com.google.android.material.textfield.TextInputLayout//
import kotlinx.coroutines.CoroutineScope//
import kotlinx.coroutines.Dispatchers//
import kotlinx.coroutines.launch//
import java.util.Locale//
//
import com.demo.sabilabapp.Adapters.SequencePedidos.Pedidos2scAdapter // OTRO
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.Clientes.Result // OTRO
import com.demo.sabilabapp.Clientes.Clientes // OTRO
import com.demo.sabilabapp.databinding.ActivityPedidos2scBinding // OTRO

class Pedidos2scActivity : AppCompatActivity() {

    var binding: ActivityPedidos2scBinding? = null

    var id_vendedor:Int = 0
    //var id_vendedor:Int? = null
    var nomcome: String = ""

    private lateinit var adapter: Pedidos2scAdapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1
    private var verdura: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidos2scBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val extras = intent.extras
        if (extras != null) {
            id_vendedor = extras.getInt("id_vendedor")
        }

        initRecyclerView()
        listaAlEntrar(id_vendedor)

        binding?.btnPedidos2scBuscar?.setOnClickListener {
            nomcome = binding?.tietPedidos2scnomcome?.text.toString()
            if (nomcome.isEmpty()){
                listaAlEntrar(id_vendedor)
            } else {
                searchByItem(nomcome, id_vendedor)
            }
        }

        // pagina siguiente
        binding?.ibPedidos2scNext?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                if (verdura) {
                    val query = binding?.tietPedidos2scnomcome?.text?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,id_vendedor ,currentPage)
                    }
                } else {
                    nextPage(id_vendedor,currentPage)
                }
            }
        }
        // pagina anterior
        binding?.ibPedidos2scBefore?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                if (verdura) {
                    val query = binding?.tietPedidos2scnomcome?.text?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,id_vendedor ,currentPage)
                    }
                } else {
                    nextPage(id_vendedor,currentPage)
                }
            }
        }

        binding?.btnPedidos2scCancelar?.setOnClickListener {
            onBackPressed()
        }

        binding?.ibPedidos2scRefresh?.setOnClickListener{
            listaAlEntrar(id_vendedor)
            binding?.tietPedidos2scnomcome?.setText("")
            binding?.tietPedidos2scnomcome?.clearFocus()
        }
        binding?.ibBackPedidos2sc?.setOnClickListener { onBackPressed() }

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
                        binding?.tvPedidos2scNumeroPagina?.text = "$currentPage/$totalPages"
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
                    binding?.tvPedidos2scNumeroPagina?.text = "$currentPage/$totalPages"
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
        imm.hideSoftInputFromWindow(binding?.vistaPedidos2scPadre?.windowToken,0)
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
                    binding?.tvPedidos2scNumeroPagina?.text = "$currentPage/$totalPages"
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
                        binding?.tvPedidos2scNumeroPagina?.text = "$currentPage/$totalPages"
                    }
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = Pedidos2scAdapter(datitos)
        binding?.rvPedidos2sc?.layoutManager = LinearLayoutManager(this)
        binding?.rvPedidos2sc?.adapter = adapter
    }

}