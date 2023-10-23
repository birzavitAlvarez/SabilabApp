package com.demo.sabilabapp.Usuarios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Adapters.UsuariosAdapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ActivityUsuarioBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuarioActivity : AppCompatActivity() {

    private var binding: ActivityUsuarioBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuarioBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //setContentView(R.layout.activity_usuario)
        // usuarios

        val recyclerView = findViewById<RecyclerView>(R.id.rvUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.listUsuariosTrue()
                if (response.isSuccessful) {
                    val listUsuariosTrue = response.body()
                    if (listUsuariosTrue != null && listUsuariosTrue.status == 200) {
                        val results = listUsuariosTrue.data.results
                        // Listar usuarios en adapter tmare xd
                        Log.d("Response", results.toString())
                        //
                        val adapter = UsuariosAdapter(results) // 'results' es tu lista de resultados
                        recyclerView.adapter = adapter
                        //
                        Log.d("UsuariosAdapter", "Cantidad de usuarios: ${results.size}")


                    } else {
                        // Maneja el caso en el que la solicitud se realizó con éxito, pero el estado es diferente de 200.
                    }
                } else {
                    // Maneja el caso en el que la solicitud no fue exitosa.
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Maneja errores de red u otros errores.
            }
        }
//        val recyclerView = findViewById<RecyclerView>(R.id.rvUsuarios)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        val adapter = UsuariosAdapter(results) // 'results' es tu lista de resultados
//        recyclerView.adapter = adapter

    }
}