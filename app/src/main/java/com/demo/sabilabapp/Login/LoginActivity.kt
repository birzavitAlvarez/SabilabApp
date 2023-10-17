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
        //setContentView(R.layout.activity_login)

        binding?.btnLogin?.setOnClickListener {
            //val username = findViewById<TextInputEditText>(R.id.tiet_user_login).text.toString()
            val username = binding?.tietUserLogin?.text.toString()
            //val password = findViewById<TextInputEditText>(R.id.tiet_password_login).text.toString()
            val password = binding?.tietPasswordLogin?.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    loginUser(username, password)
                }
//                Toast.makeText(this, "DATOS INGRESADOS", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, complete ambos campos.", Toast.LENGTH_SHORT).show()
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
                    val userDataList = loginResponse.data
                    if (userDataList.isNotEmpty()) {
                        val userData = userDataList[0]
                        val userId = userData.id_usuarios
                        val userName = userData.nombre
                        Log.d("LoginActivity", "Inicio de sesión EXITOSO")
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
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