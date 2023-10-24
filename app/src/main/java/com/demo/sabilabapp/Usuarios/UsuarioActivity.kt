package com.demo.sabilabapp.Usuarios

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.sabilabapp.Adapters.UsuariosAdapter
import com.demo.sabilabapp.Api.ApiService
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.databinding.ActivityUsuarioBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.demo.sabilabapp.Usuarios.Result
import java.util.Locale

class UsuarioActivity : AppCompatActivity(), OnQueryTextListener {

    private var binding: ActivityUsuarioBinding? = null

    private lateinit var adapter: UsuariosAdapter
    private val datitos = mutableListOf<Result>()
    private var currentPage = 0
    private var totalPages: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuarioBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //
        initRecyclerView()
        listaAlEntrar()
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

    }

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
                    cosita()
//                    val pagination = pruebita?.data?.pagination
//                    if (pagination != null) {
//                        // Obtén los valores de currentPage y totalPages desde la respuesta.
//                        val currentPage = pagination.currentPage
//                        val totalPages = pagination.totalPages
//
//                        binding?.tvUsuarioNumeroPagina?.text = "$currentPage/$totalPages"
//                    }
                } else {
                    showError()
                }
            }
        }
    }

    private fun cosita(){
        CoroutineScope(Dispatchers.IO).launch {
            val call2 = apiService.listUsuariosTrue()
            val pruebita = call2.body()
            runOnUiThread {
                if (call2.isSuccessful) {
                    val pagination = pruebita?.data?.pagination
                    if (pagination != null) {
                        // Obtén los valores de currentPage y totalPages desde la respuesta.
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages

                        binding?.tvUsuarioNumeroPagina?.text = "$currentPage/$totalPages"
                    }
                } else {
                    showError()
                }
            }
        }
    }

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
                } else { showError() }
                hideKeyboard()
            }
        }
    }

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


}