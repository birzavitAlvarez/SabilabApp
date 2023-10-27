package com.demo.sabilabapp.Adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Usuarios.Result
import com.demo.sabilabapp.Usuarios.Usuario
import com.demo.sabilabapp.Usuarios.UsuarioActivity
import com.demo.sabilabapp.databinding.ItemUsuarioBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.withContext


class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemUsuarioBinding = ItemUsuarioBinding.bind(itemView)


    fun bind (query: Result){
        binding.tvUsuario.text = query.usuario
        binding.tvRol.text = query.rol

        binding.ibUsuarioEdit.setOnClickListener{
            var idRoles = 0
            if (query.rol == "Administrador"){ idRoles = 1 }
            else if (query.rol == "Trabajador"){ idRoles = 2 }
            else if (query.rol == "Soporte"){ idRoles = 3 }
            showDialog(itemView.context,query.id_usuarios,query.usuario,query.password,idRoles)
        }

        binding.ibUsuarioDelete.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    (itemView.context as? AppCompatActivity)?.let {
                        val adapter = it.findViewById<RecyclerView>(R.id.rvUsuarios)
                        (adapter?.adapter as? UsuariosAdapter)?.removeItem(position)
                    }
                }
            }
            val id_usuarios = query.id_usuarios
            CoroutineScope(Dispatchers.IO).launch {
                apiService.deleteUser(id_usuarios)
            }
        }


    }

    @SuppressLint("SetTextI18n")
    private fun showDialog(context: Context,id:Int,user:String,password:String,id_roles:Int){
        val dialog = Dialog(context)
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
        tvAddUsuarioTitle.text = "Actualizar Usuario"
        tietAddUsuarioNombre.setText(user)
        tietAddUsuarioPassword.setText(password)
        spAddUsuarioRol.visibility = View.GONE
        tietAddUsuarioNombre.requestFocus()
        ibAddUsuarioClose.setOnClickListener{
            dialog.dismiss()
        }

        btnAddUsuarioGuardar.setOnClickListener{
            val nameUser = tietAddUsuarioNombre.text.toString()
            val passwordUser = tietAddUsuarioPassword.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val usuario = Usuario(nameUser, passwordUser, 1, id_roles)
                apiService.updateUser(usuario, id)

                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    val intent = Intent(itemView.context, UsuarioActivity::class.java)
                    itemView.context.startActivity(intent)
                    dialog.dismiss()
                }

            }
        }


        //
        dialog.show()
    }


}

// picasso par mostrar imagenes
// picasso.get().load(image).into(binding?.textViewUsername)