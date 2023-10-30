package com.demo.sabilabapp.Proveedores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.sabilabapp.Adapters.ProveedorAdapter
import com.demo.sabilabapp.Adapters.UsuariosAdapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.Proveedores.Result // OTRO
import com.demo.sabilabapp.Usuarios.Usuario
import com.demo.sabilabapp.databinding.ActivityProveedorBinding  // OTRO
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class ProveedorActivity : AppCompatActivity(), OnQueryTextListener {

    private var binding: ActivityProveedorBinding? = null

    private lateinit var adapter: ProveedorAdapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1

    private var verdura: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProveedorBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //
        initRecyclerView()
        listaAlEntrar()
        // buscador
        binding?.svProveedorBusqueda?.setOnQueryTextListener(this)
        // boton buscar xd
        binding?.btnProveedorBuscar?.setOnClickListener {
            val query = binding?.svProveedorBusqueda?.query?.toString()
            if (!query.isNullOrBlank()) {
                searchByRazonSocial(query)
            }
        }
        // pagina siguiente
        binding?.ibProveedorNext?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                if (verdura) {
                    val query = binding?.svProveedorBusqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,currentPage)
                    }
                } else {
                    nextPage(currentPage)
                }
            }
        }
        // pagina anterior
        binding?.ibProveedorBefore?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                if (verdura) {
                    val query = binding?.svProveedorBusqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,currentPage)
                    }
                } else {
                    nextPage(currentPage)
                }
            }
        }
        // boton agregar
        binding?.btnProveedorAgregar?.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.item_add_proveedores)

        val tvAddProveedorTitle: TextView = dialog.findViewById(R.id.tvAddProveedorTitle)
        val ibAddProveedorClose: ImageButton = dialog.findViewById(R.id.ibAddProveedorClose)
        val tilAddProveedorRuc: TextInputLayout = dialog.findViewById(R.id.tilAddProveedorRuc)
        val tietAddProveedorRuc: TextInputEditText = dialog.findViewById(R.id.tietAddProveedorRuc)
        val tilAddProveedorRazonSocial: TextInputLayout = dialog.findViewById(R.id.tilAddProveedorRazonSocial)
        val tietAddProveedorRazonSocial: TextInputEditText = dialog.findViewById(R.id.tietAddProveedorRazonSocial)
        val tilAddProveedorNombreContacto: TextInputLayout = dialog.findViewById(R.id.tilAddProveedorNombreContacto)
        val tietAddProveedorNombreContacto: TextInputEditText = dialog.findViewById(R.id.tietAddProveedorNombreContacto)
        val tilAddProveedorTelefono: TextInputLayout = dialog.findViewById(R.id.tilAddProveedorTelefono)
        val tietAddProveedorTelefono: TextInputEditText = dialog.findViewById(R.id.tietAddProveedorTelefono)
        val tilAddProveedorCorreo: TextInputLayout = dialog.findViewById(R.id.tilAddProveedorCorreo)
        val tietAddProveedorCorreo: TextInputEditText = dialog.findViewById(R.id.tietAddProveedorCorreo)
        val btnAddProveedorGuardar: Button = dialog.findViewById(R.id.btnAddProveedorGuardar)

        tietAddProveedorRuc.requestFocus()
        ibAddProveedorClose.setOnClickListener{
            dialog.dismiss()
        }
        //

        btnAddProveedorGuardar.setOnClickListener{
            val rucProv = tietAddProveedorRuc.text.toString()
            val razProv = tietAddProveedorRazonSocial.text.toString()
            val nomProv = tietAddProveedorNombreContacto.text.toString()
            val telProv = tietAddProveedorTelefono.text.toString()
            val corProv = tietAddProveedorCorreo.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val usuario = Proveedor(rucProv, razProv, nomProv,telProv,corProv,1)
                    apiService.createProveedor(usuario)
                    // Después de agregar el usuario Actualizo la lista
                    val updatedData = apiService.listProveedorTrue().body()?.data?.results
                    runOnUiThread {
                        if (updatedData != null) {
                            adapter.updateList(updatedData)
                        }
                        hideKeyboard()
                        dialog.dismiss()
                    }

            }
            getCurrentAndTotal()
        }
        //
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageSearch(query:String,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarProveedorPorNombreYPage(query, np)//.body()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val itemsData = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(itemsData)
                    adapter.notifyDataSetChanged()
                    val pagination = response0?.data?.pagination
                    pagination?.currentPage!!.also { currentPage = it }
                    pagination.totalPages.also { totalPages = it }
                    binding?.tvProveedorNumeroPagina?.text = "$currentPage/$totalPages"
                } else {
                    showError()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPage(np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.paginaProveedor(np)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val itemsData = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(itemsData)
                    adapter.notifyDataSetChanged()
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvProveedorNumeroPagina?.text = "$currentPage/$totalPages"
                    } else {showError()}
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrar() {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listProveedorTrue()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataitems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataitems)
                    adapter.notifyDataSetChanged()
                    getCurrentAndTotal()
                    verdura = false
                } else { showError() }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = ProveedorAdapter(datitos)
        binding?.rvProveedor?.layoutManager = LinearLayoutManager(this)
        binding?.rvProveedor?.adapter = adapter
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchByRazonSocial(query.lowercase(Locale.ROOT))
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByRazonSocial(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarProveedorPorFiltro(query)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataitems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataitems)
                    adapter.notifyDataSetChanged()
                    performSearch(query)
                } else { showError() }
                hideKeyboard()
                getCurrentAndTotal()
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun performSearch(query: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val response0 = apiService.listarProveedorPorFiltro(query ?: "").body()
            runOnUiThread {
                if (response0 != null && response0.status == 200) {
                    val pagination = response0.data.pagination
                    currentPage = pagination.currentPage
                    totalPages = pagination.totalPages
                    binding?.tvProveedorNumeroPagina?.text = "$currentPage/$totalPages"
                    verdura = true
                    adapter.notifyDataSetChanged() //TODO
                } else { showError() }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.vistaProveedorPadre?.windowToken,0)
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getCurrentAndTotal(){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listProveedorTrue()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvProveedorNumeroPagina?.text = "$currentPage/$totalPages"
                    }
                    adapter.notifyDataSetChanged() //TODO
                } else { showError() }
            }
        }
    }

}