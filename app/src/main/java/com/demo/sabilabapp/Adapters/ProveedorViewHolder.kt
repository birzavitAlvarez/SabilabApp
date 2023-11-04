package com.demo.sabilabapp.Adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Proveedores.Result // otro
import com.demo.sabilabapp.Proveedores.Proveedor //para agregar
import com.demo.sabilabapp.databinding.ItemProveedorBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout



class ProveedorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemProveedorBinding = ItemProveedorBinding.bind(itemView)


    fun bind (query: Result){
        binding.tvRucProveedores.text = query.ruc
        binding.tvRazonSocialProveedores.text = query.razon_social
        binding.tvNombreContactoProveedores.text = query.nombre_contacto
        binding.tvTelefonoProveedores.text = query.telefono
        binding.tvCorreoProveedores.text = query.correo

        binding.ibProveedorEdit.setOnClickListener{
            showDialogEditProveedor(itemView.context,query.id_proveedores,query.ruc,query.razon_social,
                                        query.nombre_contacto,query.telefono,query.correo)
        }

        binding.ibProveedorDelete.setOnClickListener {
            showDialogDeleteProveedor(itemView.context,query.id_proveedores,query.razon_social)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun showDialogEditProveedor(context: Context,id:Int,ruc:String,razon_social:String, nombre_contacto:String, telefono:String, correo:String ){
        val dialog = Dialog(context)
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
        //llenando datos para el update
        tvAddProveedorTitle.text = "Actualizar Proveedor"
        tietAddProveedorRuc.setText(ruc)
        tietAddProveedorRazonSocial.setText(razon_social)
        tietAddProveedorNombreContacto.setText(nombre_contacto)
        tietAddProveedorTelefono.setText(telefono)
        tietAddProveedorCorreo.setText(correo)

        tietAddProveedorRuc.requestFocus()

        ibAddProveedorClose.setOnClickListener{
            dialog.dismiss()
        }

        btnAddProveedorGuardar.setOnClickListener{
            val rucProv = tietAddProveedorRuc.text.toString()
            val razProv = tietAddProveedorRazonSocial.text.toString()
            val nomProv = tietAddProveedorNombreContacto.text.toString()
            val telProv = tietAddProveedorTelefono.text.toString()
            val corProv = tietAddProveedorCorreo.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val proveedor = Proveedor(rucProv, razProv, nomProv, telProv,corProv,1)
                apiService.updateProveedor(proveedor, id)

                val updatedData = apiService.listProveedorTrue().body()?.data?.results

                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    if (updatedData != null) {
                        (itemView.context as? AppCompatActivity)?.let {
                            val adapter = it.findViewById<RecyclerView>(R.id.rvProveedor)
                            (adapter?.adapter as? ProveedorAdapter)?.updateList(updatedData)
                        }
                    }
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogDeleteProveedor(context: Context,id:Int,razon_social:String){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_confirm)

        val tvConfirmMessage: TextView = dialog.findViewById(R.id.tvConfirmMessage)
        val btnConfirmCancelar: Button = dialog.findViewById(R.id.btnConfirmCancelar)
        val btnConfirmConfirmar: Button = dialog.findViewById(R.id.btnConfirmConfirmar)
        tvConfirmMessage.text = "¿Seguro que quieres eliminar al proveedor $razon_social?"
        btnConfirmCancelar.setOnClickListener{
            dialog.dismiss()
        }
        btnConfirmConfirmar.setOnClickListener{
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    (itemView.context as? AppCompatActivity)?.let {
                        val adapter = it.findViewById<RecyclerView>(R.id.rvProveedor)
                        (adapter?.adapter as? ProveedorAdapter)?.removeItem(position)
                    }
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                apiService.deleteProveedor(id)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

}

// picasso par mostrar imagenes
// picasso.get().load(image).into(binding?.textViewUsername)