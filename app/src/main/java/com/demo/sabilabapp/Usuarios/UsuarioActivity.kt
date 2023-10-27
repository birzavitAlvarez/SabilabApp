package com.demo.sabilabapp.Usuarios

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.demo.sabilabapp.Adapters.UsuariosAdapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ActivityUsuarioBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import com.demo.sabilabapp.Roles.Data as DataRolesImport

class UsuarioActivity : AppCompatActivity(), OnQueryTextListener {

    private var binding: ActivityUsuarioBinding? = null

    private lateinit var adapter: UsuariosAdapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 0
    private var totalPages: Int = 0
    private var currentPageSearch: Int = 0
    private var totalPagesSearch: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuarioBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //
        initRecyclerView()
        listaAlEntrar()
        //
        binding?.svUsuarioBusqueda?.setOnQueryTextListener(this)
        //
        binding?.btnUsuarioBuscar?.setOnClickListener {
            val query = binding?.svUsuarioBusqueda?.query?.toString()
            if (!query.isNullOrBlank()) {
                searchByUsuario(query)
            }
        }

        binding?.ibUsuarioNext?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                nextPage(currentPage)
            }
        }

        binding?.ibUsuarioBefore?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                nextPage(currentPage)
            }
        }

        binding?.btnUsuarioAgregar?.setOnClickListener {
            showDialog()
        }


    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPage(np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val call3 = apiService.paginaUsuarios(np)
            val pruebita = call3.body()
            runOnUiThread {
                if (call3.isSuccessful) {
                    val dataUsuario = pruebita?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataUsuario)
                    adapter.notifyDataSetChanged()
                    val pagination = pruebita?.data?.pagination
                    if (pagination != null) {
                        val currentPage = pagination.currentPage
                        val totalPages = pagination.totalPages
                        binding?.tvUsuarioNumeroPagina?.text = "$currentPage/$totalPages"
                    } else {showError()}
                } else { showError() }
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrar() {
        CoroutineScope(Dispatchers.IO).launch {
            val call2 = apiService.listUsuariosTrue()
            val pruebita = call2.body()
            runOnUiThread {
                if (call2.isSuccessful) {
                    val dataUsuario = pruebita?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataUsuario)
                    adapter.notifyDataSetChanged()
                    getCurrentAndTotal()
                } else { showError() }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun getCurrentAndTotal(){
        CoroutineScope(Dispatchers.IO).launch {
            val call2 = apiService.listUsuariosTrue()
            val pruebita = call2.body()
            runOnUiThread {
                if (call2.isSuccessful) {
                    val pagination = pruebita?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvUsuarioNumeroPagina?.text = "$currentPage/$totalPages"
                    }
                } else { showError() }
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NotifyDataSetChanged")
    private fun searchByUsuario(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = apiService.listarUsuariosPorFiltro(query)
            val pruebita = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    val dataUsuario = pruebita?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataUsuario)
                    adapter.notifyDataSetChanged()
                    performSearch(query)
                } else { showError() }
                hideKeyboard()
                getCurrentAndTotal()
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun performSearch(query: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listarUsuariosPorFiltro(query ?: "").body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val pagination = response.data.pagination
                    currentPage = pagination.currentPage
                    totalPages = pagination.totalPages
                    binding?.tvUsuarioNumeroPagina?.text = "$currentPage/$totalPages"
                } else { showError() }
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.vistaUsuariosPadre?.windowToken,0)
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    private fun initRecyclerView() {
        adapter = UsuariosAdapter(datitos)
        binding?.rvUsuarios?.layoutManager = LinearLayoutManager(this)
        binding?.rvUsuarios?.adapter = adapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchByUsuario(query.lowercase(Locale.ROOT))
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun showDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.item_add_usuario)

        val tvAddUsuarioTitle: TextView = dialog.findViewById(R.id.tvAddUsuarioTitle)
        val ibAddUsuarioClose: ImageButton = dialog.findViewById(R.id.ibAddUsuarioClose)
        val tilAddUsuarioNombre: TextInputLayout = dialog.findViewById(R.id.tilAddUsuarioNombre)
        val tietAddUsuarioNombre: TextInputEditText = dialog.findViewById(R.id.tietAddUsuarioNombre)
        val tilAddUsuarioPassword: TextInputLayout = dialog.findViewById(R.id.tilAddUsuarioPassword)
        val tietAddUsuarioPassword: TextInputEditText = dialog.findViewById(R.id.tietAddUsuarioPassword)
        val tilAddUsuarioRol: TextInputLayout = dialog.findViewById(R.id.tilAddUsuarioRol)
        val spAddUsuarioRol: Spinner = dialog.findViewById(R.id.spAddUsuarioRol)
        val btnAddUsuarioGuardar: Button = dialog.findViewById(R.id.btnAddUsuarioGuardar)
        //tvAddUsuarioTitle.text = "Actualizar Usuario"
        tietAddUsuarioNombre.requestFocus()
        ibAddUsuarioClose.setOnClickListener{
            dialog.dismiss()
        }
        //
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listRoles().body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val rolesList = response.data?.map { it.rol } ?: emptyList()
                    val rolesWithSelect = listOf("Seleccionar") + rolesList
                    val adapterLoad = ArrayAdapter(this@UsuarioActivity, android.R.layout.simple_spinner_item, rolesWithSelect)
                    adapterLoad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spAddUsuarioRol.adapter = adapterLoad
                    Log.d("DataUsuario", rolesList.toString())
                    adapterLoad.notifyDataSetChanged()
                } else {
                    showError()
                }
            }
        }
        // -----------------------------
        var selectedRoleId: Int? = null

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listRoles().body()
            spAddUsuarioRol.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedRoleId = if (position > 0) {
                        response?.data?.get(position - 1)?.id_roles
                    } else { null }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) { selectedRoleId = null }
            }
        }

        btnAddUsuarioGuardar.setOnClickListener{
            val nombreUsuario = tietAddUsuarioNombre.text.toString()
            val contrasena = tietAddUsuarioPassword.text.toString()

            CoroutineScope(Dispatchers.IO).launch {

                if (selectedRoleId != null) {
                    val usuario = Usuario(nombreUsuario, contrasena, 1, selectedRoleId!!)
                    apiService.createUser(usuario)
                    hideKeyboard()
                    nextPage(currentPage)
                    dialog.dismiss()
                } else {
                    runOnUiThread {
                        Toast.makeText(this@UsuarioActivity, "Error: Debe seleccionar un rol", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
        //
        dialog.show()
    }
    //---------------------------------------------------------------------------

    // Listener para el SearchView





}