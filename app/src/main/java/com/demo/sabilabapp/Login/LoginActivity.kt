package com.demo.sabilabapp.Login

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.MainActivity
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputLayout

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.tietUserLogin?.requestFocus()

        // TODO VALIDAR
        binding?.tietUserLogin?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.tilUserLogin?.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding?.tietPasswordLogin?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.tilPasswordLogin?.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        // TODO FIN VALIDAR
        binding?.btnLogin?.setOnClickListener {
            val username = binding?.tietUserLogin?.text.toString()
            val password = binding?.tietPasswordLogin?.text.toString()

            if (username.isEmpty() && password.isEmpty()){
                binding?.tilUserLogin?.error = "Usuario requerido"
                binding?.tilPasswordLogin?.error = "Password requerido"
                return@setOnClickListener
            } else if (username.isEmpty()) {
                binding?.tilUserLogin?.error = "Usuario requerido"
                return@setOnClickListener
            } else if (password.isEmpty()){
                binding?.tilPasswordLogin?.error = "Password requerido"
                return@setOnClickListener
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    loginUser(username, password)
                }
            }
        }
    }
    suspend fun loginUser(username: String, password: String) {
        val (status, statusMessage) = checkUserAndPassword(username, password)
        runOnUiThread {
            when (status) {
                200 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = apiService.login(LoginRequest(username, password))
                            withContext(Dispatchers.Main) {
                                handleLoginResponse(response)
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@LoginActivity, "Error en la solicitud de red", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                404 -> {
                    showErrorDialogLogin()
                }

                else -> {
                    showErrorDialogLogin()
                }
            }
        }
    }

    private fun showErrorDialogLogin() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_login_badrequest)
        val btndialogLoginBadRequestOk: Button = dialog.findViewById(R.id.btndialogLoginBadRequestOk)
        btndialogLoginBadRequestOk.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    private fun handleLoginResponse(response: Response<LoginResponse>) {
        if (response.isSuccessful) {
            val loginResponse = response.body()

            if (loginResponse != null && loginResponse.data.isNotEmpty()) {
                val datos = loginResponse.data[0]
                val id_usuarios = datos.id_usuarios
                val id_roles = datos.id_roles
                val id_vendedor = datos.id_vendedor
                val nombre = datos.nombre
                val userData = UserData(id_usuarios, id_roles, id_vendedor, nombre)
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra("userData", userData)
                startActivity(intent)
            } else {
                //
            }
        } else {
            //
        }
    }

    private suspend fun checkUserAndPassword(username: String, password: String): Pair<Int, String?> {
        val request = LoginRequest(username, password)
        try {
            val response = apiService.login(request)
            if (response.isSuccessful) {
                val loginResponse = response.body()

                if (loginResponse != null) {
                    return Pair(loginResponse.status, loginResponse.statusMessage)
                }
            }
        } catch (e: Exception) {
            //
        }
        return Pair(500, "Error en la solicitud")
    }

    //    suspend fun loginUser(username: String, password: String) {
//        val request = LoginRequest(username, password)
//
//        try {
//            val response = apiService.login(request)
//            if (response.isSuccessful) {
//                val loginResponse = response.body()
//
//                if ((loginResponse != null && loginResponse.status == 404)){
//                    Toast.makeText(this, "USUARIO O PASSWORD MAL", Toast.LENGTH_SHORT).show()
//                }
//
//                if (loginResponse != null && loginResponse.status == 200) {
//                    val itemList = loginResponse.data
//                    if (itemList.isNotEmpty()) {
//                        val datos = itemList[0]
//                        // aqui recupero datos para mandarlos como put extra
//                        val id_usuarios = datos.id_usuarios
//                        val id_roles = datos.id_roles
//                        val id_vendedor = datos.id_vendedor
//                        val nombre = datos.nombre
//
//                        val userData  = UserData(id_usuarios, id_roles, id_vendedor, nombre)
//                        //Log.d("LoginActivity", "Inicio de sesión EXITOSO id_usuarios: $id_usuarios id_roles: $id_roles id_vendedor: $id_vendedor nombre: $nombre")
//                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                        intent.putExtra("userData", userData )
//                        startActivity(intent)
//
//                    } else {
//                        //
//                    }
//                } else {
//                    //Toast.makeText(this, "USUARIO O PASSWORD MAL", Toast.LENGTH_SHORT).show()
//                    //Log.d("LoginActivity", "Inicio de sesión fallido: ${loginResponse?.statusMessage}")
//                }
//            } else {
////                Log.d("LoginActivity", "Error en la respuesta del servidor: ${response.message()}")
//                //Toast.makeText(this, "USUARIO O PASSWORD MALr", Toast.LENGTH_SHORT).show()
//            }
//        } catch (e: Exception) {
//            //Log.e("LoginActivity", "Error: ${e.message}")
//
//        }
//    }

}