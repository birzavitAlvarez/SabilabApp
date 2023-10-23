package com.demo.sabilabapp.Usuarios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.sabilabapp.Adapters.UsuariosAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuarioBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initRecyclerView()

//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = apiService.listUsuariosTrue()
//                if (response.isSuccessful) {
//                    val listUsuariosTrue = response.body()
//                    if (listUsuariosTrue != null && listUsuariosTrue.status == 200) {
//                        val results = listUsuariosTrue.data.results
//
//                        // Crear y configurar el adaptador
//                        val adapter = UsuariosAdapter(results)
//                        recyclerView.adapter = adapter
//
//                        // Verificar que se están recibiendo datos
//                        Log.d("UsuariosAdapter", "Cantidad de usuarios: ${results.size}")
//                    } else {
//                        // Maneja el caso en el que la solicitud se realizó con éxito, pero el estado es diferente de 200.
//                    }
//                } else {
//                    // Maneja el caso en el que la solicitud no fue exitosa.
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                // Maneja errores de red u otros errores.
//            }
//        }
        binding?.svUsuarioBusqueda?.setOnQueryTextListener(this)


    }

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
                } else {
                    showError()
                }
            }

        }
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