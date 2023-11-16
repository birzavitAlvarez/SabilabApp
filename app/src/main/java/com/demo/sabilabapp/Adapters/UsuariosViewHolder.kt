package com.demo.sabilabapp.Adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Usuarios.Result // otro
import com.demo.sabilabapp.Usuarios.Usuario //para agregar
import com.demo.sabilabapp.databinding.ItemUsuarioBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout



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
            showDialogEditUser(itemView.context,query.id_usuarios,query.usuario,query.password,idRoles)
        }

        binding.ibUsuarioDelete.setOnClickListener {
            showDialogDeleteUser(itemView.context,query.id_usuarios,query.usuario)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun showDialogEditUser(context: Context,id:Int,user:String,password:String,id_roles:Int){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_update_usuario)

        val tvUpdateUsuarioTitle: TextView = dialog.findViewById(R.id.tvUpdateUsuarioTitle)
        val ibAUpdateUsuarioClose: ImageButton = dialog.findViewById(R.id.ibAUpdateUsuarioClose)
        val tilUpdateUsuarioNombre: TextInputLayout = dialog.findViewById(R.id.tilUpdateUsuarioNombre)
        val tietUpdateUsuarioNombre: TextInputEditText = dialog.findViewById(R.id.tietUpdateUsuarioNombre)
        val tilUpdateUsuarioPassword: TextInputLayout = dialog.findViewById(R.id.tilUpdateUsuarioPassword)
        val tietUpdateUsuarioPassword: TextInputEditText = dialog.findViewById(R.id.tietUpdateUsuarioPassword)
        val btnUpdateUsuarioGuardar: Button = dialog.findViewById(R.id.btnUpdateUsuarioGuardar)
        tietUpdateUsuarioNombre.setText(user)
        tietUpdateUsuarioPassword.setText(password)
        tietUpdateUsuarioNombre.requestFocus()
        ibAUpdateUsuarioClose.setOnClickListener{
            dialog.dismiss()
        }


        // TODO VALIDAR
        tietUpdateUsuarioNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilUpdateUsuarioNombre.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietUpdateUsuarioPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilUpdateUsuarioPassword.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        // TODO FIN VALIDAR

        btnUpdateUsuarioGuardar.setOnClickListener{
            // TODO VALIDAR
            if (tietUpdateUsuarioNombre.text.toString().isEmpty()){
                tilUpdateUsuarioNombre.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if(tietUpdateUsuarioPassword.text.toString().isEmpty()){
                tilUpdateUsuarioPassword.error = "ES REQUERIDO"
                return@setOnClickListener
            }
            // TODO FIN VALIDAR
            val nameUser = tietUpdateUsuarioNombre.text.toString()
            val passwordUser = tietUpdateUsuarioPassword.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val usuario = Usuario(nameUser, passwordUser, 1, id_roles)
                apiService.updateUser(usuario, id)

                val updatedData = apiService.listUsuariosTrue().body()?.data?.results

                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    if (updatedData != null) {
                        (itemView.context as? AppCompatActivity)?.let {
                            val adapter = it.findViewById<RecyclerView>(R.id.rvUsuarios)
                            (adapter?.adapter as? UsuariosAdapter)?.updateUserList(updatedData)
                        }
                    }
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogDeleteUser(context: Context,id:Int,usuario:String){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_confirm)

        val tvConfirmMessage: TextView = dialog.findViewById(R.id.tvConfirmMessage)
        val btnConfirmCancelar: Button = dialog.findViewById(R.id.btnConfirmCancelar)
        val btnConfirmConfirmar: Button = dialog.findViewById(R.id.btnConfirmConfirmar)
        tvConfirmMessage.text = "¿Seguro que quieres eliminar al usuario $usuario?"
        btnConfirmCancelar.setOnClickListener{
            dialog.dismiss()
        }
        btnConfirmConfirmar.setOnClickListener{
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    (itemView.context as? AppCompatActivity)?.let {
                        val adapter = it.findViewById<RecyclerView>(R.id.rvUsuarios)
                        (adapter?.adapter as? UsuariosAdapter)?.removeItem(position)
                    }
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                apiService.deleteUser(id)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

}

// picasso par mostrar imagenes
// picasso.get().load(image).into(binding?.textViewUsername)