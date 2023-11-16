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
import com.demo.sabilabapp.Api.RetrofitClient
import com.demo.sabilabapp.Clientes.Clientes
import com.demo.sabilabapp.Clientes.Result
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ItemClientesBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClientesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemClientesBinding = ItemClientesBinding.bind(itemView)

    fun bind (query: Result){
        binding.tvRucClientes.text = query.ruc
        binding.tvRazonSocialClientes.text = query.razon_social
        binding.tvNombreComercialClientes.text = query.nombre_comercial
        binding.tvDireccionClientes.text = query.direccion1
        binding.tvDistritoClientes.text = query.distrito
        binding.tvContactoClientes.text = query.contacto
        binding.tvTelefonoClientes.text = query.telefono1


        binding.ibClientesEdit.setOnClickListener{
            showDialogEditClient(itemView.context,query.id_cliente,
                                                  query.ruc,query.razon_social,
                                                  query.nombre_comercial,query.contacto,
                                                  query.direccion1, query.direccion2,
                                                  query.telefono1, query.telefono2,
                                                  query.telefono_empresa, query.provincia,query.distrito,
                                                  query.activo, query.id_vendedor)
        }

        binding.ibClientesDelete.setOnClickListener{
            showDialogDeleteClient(itemView.context,query.id_cliente, query.nombre_comercial)
        }
    }

    private fun showDialogEditClient(context: Context,id_cliente: Int, ruc:String, razon_social:String, nombre_comercial:String, contacto:String,
                                     direccion1:String, direccion2:String, telefono1:String, telefono2:String,
                                     telefono_empresa:String, provincia:String,distrito:String ,activo:Boolean, id_vendedor: Int ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_add_clientes)

        val tvAddClientesTitle: TextView = dialog.findViewById(R.id.tvAddClientesTitle)
        val ibAddClientesClose: ImageButton = dialog.findViewById(R.id.ibAddClientesClose)
        val tilAddClientesRuc: TextInputLayout = dialog.findViewById(R.id.tilAddClientesRuc)
        val tietAddClientesRuc: TextInputEditText = dialog.findViewById(R.id.tietAddClientesRuc)
        val tilAddClientesRazonSocial: TextInputLayout = dialog.findViewById(R.id.tilAddClientesRazonSocial)
        val tietAddClientesRazonSocial: TextInputEditText = dialog.findViewById(R.id.tietAddClientesRazonSocial)
        val tilAddClientesNombreComercial: TextInputLayout = dialog.findViewById(R.id.tilAddClientesNombreComercial)
        val tietAddClientesNombreComercial: TextInputEditText = dialog.findViewById(R.id.tietAddClientesNombreComercial)
        val tilAddClientesDireccion1: TextInputLayout = dialog.findViewById(R.id.tilAddClientesDireccion1)
        val tietAddClientesDireccion1: TextInputEditText = dialog.findViewById(R.id.tietAddClientesDireccion1)
        val tilAddClientesDireccion2: TextInputLayout = dialog.findViewById(R.id.tilAddClientesDireccion2)
        val tietAddClientesDireccion2: TextInputEditText = dialog.findViewById(R.id.tietAddClientesDireccion2)
        val tilAddClientesTelefonoEmpresa: TextInputLayout = dialog.findViewById(R.id.tilAddClientesTelefonoEmpresa)
        val tietAddClientesTelefonoEmpresa: TextInputEditText = dialog.findViewById(R.id.tietAddClientesTelefonoEmpresa)
        val tilAddClientesContacto: TextInputLayout = dialog.findViewById(R.id.tilAddClientesContacto)
        val tietAddClientesContacto: TextInputEditText = dialog.findViewById(R.id.tietAddClientesContacto)
        val tilAddClientesTelefono1: TextInputLayout = dialog.findViewById(R.id.tilAddClientesTelefono1)
        val tietAddClientesTelefono1: TextInputEditText = dialog.findViewById(R.id.tietAddClientesTelefono1)
        val tilAddClientesTelefono2: TextInputLayout = dialog.findViewById(R.id.tilAddClientesTelefono2)
        val tietAddClientesTelefono2: TextInputEditText = dialog.findViewById(R.id.tietAddClientesTelefono2)
        val tilAddClientesDistrito: TextInputLayout = dialog.findViewById(R.id.tilAddClientesDistrito)
        val tietAddClientesDistrito: TextInputEditText = dialog.findViewById(R.id.tietAddClientesDistrito)
        val tilAddClientesProvincia: TextInputLayout = dialog.findViewById(R.id.tilAddClientesProvincia)
        val tietAddClientesProvincia: TextInputEditText = dialog.findViewById(R.id.tietAddClientesProvincia)
        val btnAddClientesGuardar: Button = dialog.findViewById(R.id.btnAddClientesGuardar)

        //llenando datos para el update
        tvAddClientesTitle.text = "Actualizar Cliente"
        tietAddClientesRuc.setText(ruc)
        tietAddClientesRazonSocial.setText(razon_social)
        tietAddClientesNombreComercial.setText(nombre_comercial)
        tietAddClientesDireccion1.setText(direccion1)
        tietAddClientesDireccion2.setText(direccion2)
        tietAddClientesTelefonoEmpresa.setText(telefono_empresa)
        tietAddClientesContacto.setText(contacto)
        tietAddClientesTelefono1.setText(telefono1)
        tietAddClientesTelefono2.setText(telefono2)
        tietAddClientesDistrito.setText(distrito)
        tietAddClientesProvincia.setText(provincia)

        tietAddClientesRuc.requestFocus()

        ibAddClientesClose.setOnClickListener{
            dialog.dismiss()
        }

        // TODO VALIDO CAMPOS
        tietAddClientesRuc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesRuc.error = if (s!!.length < 11) "RUC INVALIDO" else null
                if (tietAddClientesRuc.text.toString().isEmpty()){ tilAddClientesRuc.error = "RUC REQUERIDO" }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        tietAddClientesRazonSocial.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesRazonSocial.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddClientesNombreComercial.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesNombreComercial.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddClientesDireccion1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesDireccion1.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddClientesContacto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesContacto.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddClientesTelefono1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesTelefono1.error = if (s?.any { it.isDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddClientesDistrito.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesDistrito.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddClientesProvincia.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddClientesProvincia.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        // TODO FIN VALIDO CAMPO

        btnAddClientesGuardar.setOnClickListener{

            //TODO VALIDO ANTES DE GUARDAR
            if (tietAddClientesRuc.text.toString().isEmpty()){
                tilAddClientesRuc.error = "RUC REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesRazonSocial.text.toString().isEmpty()){
                tilAddClientesRazonSocial.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesNombreComercial.text.toString().isEmpty()){
                tilAddClientesNombreComercial.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesDireccion1.text.toString().isEmpty()){
                tilAddClientesDireccion1.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesContacto.text.toString().isEmpty()){
                tilAddClientesContacto.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesTelefono1.text.toString().isEmpty()){
                tilAddClientesTelefono1.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesDistrito.text.toString().isEmpty()){
                tilAddClientesDistrito.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (tietAddClientesProvincia.text.toString().isEmpty()){
                tilAddClientesProvincia.error = "ES REQUERIDO"
                return@setOnClickListener
            }
            // TODO FIN DE VALIDAR


            var valor:Int = 0
            // id_cliente
            val rucCli = tietAddClientesRuc.text.toString()
            val razCli = tietAddClientesRazonSocial.text.toString()
            val nomCli = tietAddClientesNombreComercial.text.toString()
            val conCli = tietAddClientesContacto.text.toString()
            val di1Cli = tietAddClientesDireccion1.text.toString()
            val di2Cli = tietAddClientesDireccion2.text.toString()
            val te1Cli = tietAddClientesTelefono1.text.toString()
            val te2Cli = tietAddClientesTelefono2.text.toString()
            val empCli = tietAddClientesTelefonoEmpresa.text.toString()
            val proCli = tietAddClientesProvincia.text.toString()
            val disCli = tietAddClientesDistrito.text.toString()
            if (activo){
                valor = 1
            }

            CoroutineScope(Dispatchers.IO).launch {
                val proveedor = Clientes(rucCli,razCli,nomCli,conCli,di1Cli,di2Cli,te1Cli,te2Cli,empCli,proCli,disCli,valor,id_vendedor)
                RetrofitClient.apiService.updateClientes(proveedor, id_cliente)

                val updatedData = RetrofitClient.apiService.listClientesTrue(id_vendedor).body()?.data?.results

                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    if (updatedData != null) {
                        (itemView.context as? AppCompatActivity)?.let {
                            val adapter = it.findViewById<RecyclerView>(R.id.rvClientes)
                            (adapter?.adapter as? ClientesAdapter)?.updateList(updatedData)
                        }
                    }
                    dialog.dismiss()
                }
            }
        }
        //
        dialog.show()
    }


    @SuppressLint("SetTextI18n")
    private fun showDialogDeleteClient(context: Context, id:Int, nombre_comercial:String){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_confirm)

        val tvConfirmMessage: TextView = dialog.findViewById(R.id.tvConfirmMessage)
        val btnConfirmCancelar: Button = dialog.findViewById(R.id.btnConfirmCancelar)
        val btnConfirmConfirmar: Button = dialog.findViewById(R.id.btnConfirmConfirmar)
        tvConfirmMessage.text = "¿Seguro que quieres eliminar al cliente $nombre_comercial?"
        btnConfirmCancelar.setOnClickListener{
            dialog.dismiss()
        }
        btnConfirmConfirmar.setOnClickListener{
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    (itemView.context as? AppCompatActivity)?.let {
                        val adapter = it.findViewById<RecyclerView>(R.id.rvClientes)
                        (adapter?.adapter as? ClientesAdapter)?.removeItem(position)
                    }
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient.apiService.deleteClientes(id)
            }
            dialog.dismiss()
        }
        dialog.show()
    }
}