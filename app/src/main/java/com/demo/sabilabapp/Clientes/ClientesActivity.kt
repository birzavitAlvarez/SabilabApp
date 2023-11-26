package com.demo.sabilabapp.Clientes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.Adapters.ClientesAdapter // OTRO
import com.demo.sabilabapp.Clientes.Result // OTRO
import com.demo.sabilabapp.Clientes.Clientes // OTRO
import com.demo.sabilabapp.databinding.ActivityClientesBinding // OTRO

class ClientesActivity : AppCompatActivity(), OnQueryTextListener {

    private var binding: ActivityClientesBinding? = null

    private lateinit var adapter: ClientesAdapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1
    private var verdura: Boolean = false
    var id_vendedor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientesBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //
        initRecyclerView()
        // debo llenar el spiner y obtener el id_vendedor para irlo pasando
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listVendedoresTrue().body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val vendedoresList = response.data.results.map { it.nombres } // TODO waaaaaaa
                    val categoriasWithSelect = listOf("Seleccionar") + vendedoresList
                    val adapterLoad = ArrayAdapter(this@ClientesActivity, android.R.layout.simple_spinner_item, categoriasWithSelect)
                    adapterLoad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.spClientesVendedor?.adapter = adapterLoad
                    adapterLoad.notifyDataSetChanged()
                } else {
                    showError()
                }
            }
        }
        // -----------------------------
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listVendedoresTrue().body()
            binding?.spClientesVendedor?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        binding?.svClientesBusqueda?.setOnQueryTextListener(this)

        binding?.btnClientesBuscar?.setOnClickListener {
            val query = binding?.svClientesBusqueda?.query?.toString()
            if (!query.isNullOrBlank()) {
                searchByItem(query, id_vendedor)
            }
        }
        // pagina siguiente
        binding?.ibClientesNext?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                if (verdura) {
                    val query = binding?.svClientesBusqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,id_vendedor ,currentPage)
                    }
                } else {
                    nextPage(id_vendedor,currentPage)
                }
            }
        }
        // pagina anterior
        binding?.ibClientesBefore?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                if (verdura) {
                    val query = binding?.svClientesBusqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,id_vendedor ,currentPage)
                    }
                } else {
                    nextPage(id_vendedor,currentPage)
                }
            }
        }
        // boton nuevo cliente
        binding?.btnClientesAgregar?.setOnClickListener{ showDialogAddClientes(id_vendedor)}

        binding?.ibBackClientes?.setOnClickListener { onBackPressed() }

    }

    private fun showDialogAddClientes(id: Int) {
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

        // TODO VALIDO CAMPOS
        tietAddClientesRuc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesRuc.error = if (s!!.length < 11) "RUC INVALIDO" else null
                if (tietAddClientesRuc.text.toString().isEmpty()){ tilAddClientesRuc.error = "RUC REQUERIDO" }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        tietAddClientesRazonSocial.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesRazonSocial.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddClientesNombreComercial.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesNombreComercial.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddClientesDireccion1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesDireccion1.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddClientesContacto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesContacto.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddClientesTelefono1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesTelefono1.error = if (s?.any { it.isDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddClientesDistrito.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesDistrito.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddClientesProvincia.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesProvincia.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        // TODO FIN VALIDO CAMPO

        btnAddClientesGuardar.setOnClickListener{

            if (tietAddClientesRuc.text.toString().isEmpty()){
                tilAddClientesRuc.error = "RUC REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesRazonSocial.text.toString().isEmpty()){
                tilAddClientesRazonSocial.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesNombreComercial.text.toString().isEmpty()){
                tilAddClientesNombreComercial.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesDireccion1.text.toString().isEmpty()){
                tilAddClientesDireccion1.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesContacto.text.toString().isEmpty()){
                tilAddClientesContacto.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesTelefono1.text.toString().isEmpty()){
                tilAddClientesTelefono1.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesDistrito.text.toString().isEmpty()){
                tilAddClientesDistrito.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesProvincia.text.toString().isEmpty()){
                tilAddClientesProvincia.error = "ES REQUERIDO"
                return@setOnClickListener
            }

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
                        binding?.tvClientesNumeroPagina?.text = "$currentPage/$totalPages"
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
                    binding?.tvClientesNumeroPagina?.text = "$currentPage/$totalPages"
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
        imm.hideSoftInputFromWindow(binding?.vistaClientesPadre?.windowToken,0)
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
                    binding?.tvClientesNumeroPagina?.text = "$currentPage/$totalPages"
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
                        binding?.tvClientesNumeroPagina?.text = "$currentPage/$totalPages"
                    }
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }


    private fun initRecyclerView() {
        adapter = ClientesAdapter(datitos)
        binding?.rvClientes?.layoutManager = LinearLayoutManager(this)
        binding?.rvClientes?.adapter = adapter
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