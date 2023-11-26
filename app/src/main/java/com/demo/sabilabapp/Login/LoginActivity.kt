package com.demo.sabilabapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.MainActivity
import com.demo.sabilabapp.databinding.ActivityLoginBinding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.tietUserLogin?.requestFocus()

        binding?.btnLogin?.setOnClickListener {
            val username = binding?.tietUserLogin?.text.toString()
            val password = binding?.tietPasswordLogin?.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    loginUser(username, password)
                }
            } else {
                Toast.makeText(this, "Por favor, complete los 2 campos campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun loginUser(username: String, password: String) {
        val request = LoginRequest(username, password)

        try {
            val response = apiService.login(request)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null && loginResponse.status == 200) {
                    val itemList = loginResponse.data
                    if (itemList.isNotEmpty()) {
                        val datos = itemList[0]
                        // aqui recupero datos para mandarlos como put extra
                        val id_usuarios = datos.id_usuarios
                        val id_roles = datos.id_roles
                        val id_vendedor = datos.id_vendedor
                        val nombre = datos.nombre

                        val userData  = UserData(id_usuarios, id_roles, id_vendedor, nombre)
                        //Log.d("LoginActivity", "Inicio de sesión EXITOSO id_usuarios: $id_usuarios id_roles: $id_roles id_vendedor: $id_vendedor nombre: $nombre")
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("userData", userData )
                        startActivity(intent)

                    } else {
                        Log.d("LoginActivity", "Inicio de sesión fallido: Lista de usuarios vacía")
                    }
                } else {
                    Log.d("LoginActivity", "Inicio de sesión fallido: ${loginResponse?.statusMessage}")
                }
            } else {
                Log.d("LoginActivity", "Error en la respuesta del servidor: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error: ${e.message}")
        }
    }
}