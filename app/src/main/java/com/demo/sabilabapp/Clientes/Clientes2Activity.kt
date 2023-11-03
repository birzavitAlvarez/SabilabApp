package com.demo.sabilabapp.Clientes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.sabilabapp.Adapters.Clientes2Adapter // OTRO
import com.demo.sabilabapp.Adapters.Productos2Adapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.Clientes.Result // OTRO
import com.demo.sabilabapp.Clientes.Clientes // OTRO
import com.demo.sabilabapp.databinding.ActivityClientes2Binding // OTRO
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
            showDialogCli(id_vendedor)
        }



    }
    private fun showDialogCli(id: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.item_add_clientes)

        val tvAddClientesTitle: TextView = dialog.findViewById(R.id.tvAddClientesTitle)
        val ibAddClientesClose: ImageButton = dialog.findViewById(R.id.ibAddClientesClose)
        val tilAddClientesRuc: TextInputLayout = dialog.findViewById(R.id.tilAddClientesRuc)
        val tietAddClientesRuc: TextInputEditText = dialog.findViewById(R.id.tietAddClientesRuc)
        val tilAddClientesRazonSocial: TextInputLayout = dialog.findViewById(R.id.tilAddClientesRazonSocial)
        val tietAddClientesRazonSocial: TextInputEditText = dialog.findViewById(R.id.tietAddClientesRazonSocial)
        val tilAddClientesNombreComercial: TextInputLayout = dialog.findViewById(R.id.tilAddClientesNombreComercial)
        val tietAddClientesNombreComercial: TextInputEditText = dialog.findViewById(R.id.tietAddClientesNombreComercial)
        val tilAddClientesDireccion1: TextInputLayout = dialog.findViewById(R.id.tilAddClientesDireccion1)
        val tietAddClientesDireccion1: TextInputEditText = dialog.findViewById(R.id.tietAddClientesDireccion1)
        val tilAddClientesDireccion2: TextInputLayout = dialog.findViewById(R.id.tilAddClientesDireccion2)
        val tietAddClientesDireccion2: TextInputEditText = dialog.findViewById(R.id.tietAddClientesDireccion2)
        val tilAddClientesTelefonoEmpresa: TextInputLayout = dialog.findViewById(R.id.tilAddClientesTelefonoEmpresa)
        val tietAddClientesTelefonoEmpresa: TextInputEditText = dialog.findViewById(R.id.tietAddClientesTelefonoEmpresa)
        val tilAddClientesContacto: TextInputLayout = dialog.findViewById(R.id.tilAddClientesContacto)
        val tietAddClientesContacto: TextInputEditText = dialog.findViewById(R.id.tietAddClientesContacto)
        val tilAddClientesTelefono1: TextInputLayout = dialog.findViewById(R.id.tilAddClientesTelefono1)
        val tietAddClientesTelefono1: TextInputEditText = dialog.findViewById(R.id.tietAddClientesTelefono1)
        val tilAddClientesTelefono2: TextInputLayout = dialog.findViewById(R.id.tilAddClientesTelefono2)
        val tietAddClientesTelefono2: TextInputEditText = dialog.findViewById(R.id.tietAddClientesTelefono2)
        val tilAddClientesDistrito: TextInputLayout = dialog.findViewById(R.id.tilAddClientesDistrito)
        val tietAddClientesDistrito: TextInputEditText = dialog.findViewById(R.id.tietAddClientesDistrito)
        val tilAddClientesProvincia: TextInputLayout = dialog.findViewById(R.id.tilAddClientesProvincia)
        val tietAddClientesProvincia: TextInputEditText = dialog.findViewById(R.id.tietAddClientesProvincia)
        val btnAddClientesGuardar: Button = dialog.findViewById(R.id.btnAddClientesGuardar)

        ibAddClientesClose.setOnClickListener{
            dialog.dismiss()
        }

        btnAddClientesGuardar.setOnClickListener{
            val rucCli = tietAddClientesRuc.text.toString()
            val razCli = tietAddClientesRazonSocial.text.toString()
            val nomCli = tietAddClientesNombreComercial.text.toString()
            val conCli = tietAddClientesContacto.text.toString()
            val di1Cli = tietAddClientesDireccion1.text.toString()
            val di2Cli = tietAddClientesDireccion2.text.toString()
            val te1Cli = tietAddClientesTelefono1.text.toString()
            val te2Cli = tietAddClientesTelefono2.text.toString()
            val empCli = tietAddClientesTelefonoEmpresa.text.toString()
            val proCli = tietAddClientesProvincia.text.toString()
            val disCli = tietAddClientesDistrito.text.toString()


            CoroutineScope(Dispatchers.IO).launch {
                val clients = Clientes(rucCli,razCli,nomCli,conCli,di1Cli,di2Cli,te1Cli,te2Cli,empCli,proCli,disCli,1,id)
                apiService.createClientes(clients)
                // Después de agregar el cliente Actualizo la lista
                val updatedData = apiService.listClientesTrue(id).body()?.data?.results
                runOnUiThread {
                    if (updatedData != null) {
                        adapter.updateList(updatedData)
                    }
                    hideKeyboard()
                    dialog.dismiss()
                }
            }
            getCurrentAndTotal(id)
        }
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