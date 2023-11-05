package com.demo.sabilabapp.Adapters

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Vendedores.Result // otro
import com.demo.sabilabapp.Vendedores.Vendedores //para agregar
import com.demo.sabilabapp.databinding.ItemVendedoresBinding // OTRO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar
import java.util.Locale


class VendedoresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemVendedoresBinding = ItemVendedoresBinding.bind(itemView)

    fun bind (query: Result){
        binding.tvNombreVendedores.text = query.nombres
        binding.tvUsuarioVendedores.text = query.usuario
        binding.tvCorreoVendedores.text = query.correo
        binding.tvDireccionVendedores.text = query.direccion
        binding.tvTelefonoVendedores.text = query.telefono1
        binding.tvFechaNacimientoVendedores.text = query.fecha_nacimiento

        binding.ibVendedoresEdit.setOnClickListener{
            showDialogEditProductos(itemView.context,query.id_vendedor,query.nombres,query.correo,query.direccion,
                query.fecha_nacimiento,query.telefono1,query.telefono2,query.activo,query.id_usuarios)
        }

        binding.ibVendedoresDelete.setOnClickListener {
            showDialogDeleteVendedores(itemView.context,query.id_vendedor,query.nombres)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun showDialogEditProductos(context: Context,id_vendedor:Int, nombres:String,correo:String, direccion:String,
                                        fecha_nacimiento:String, telefono1:String, telefono2:String, activo:Boolean, id_usuarios:Int){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_add_vendedores)

        val tvAddVendedoresTitle: TextView = dialog.findViewById(R.id.tvAddVendedoresTitle)
        val ibAddVendedoresClose: ImageButton = dialog.findViewById(R.id.ibAddVendedoresClose)
        val tilAddVendedoresNombre: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresNombre)
        val tietAddVendedoresNombre: TextInputEditText = dialog.findViewById(R.id.tietAddVendedoresNombre)
        val tilAddVendedoresCorreo: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresCorreo)
        val tietAddVendedoresCorreo: TextInputEditText = dialog.findViewById(R.id.tietAddVendedoresCorreo)
        val tilAddVendedoresDireccion: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresDireccion)
        val tietAddVendedoresDireccion: TextInputEditText = dialog.findViewById(R.id.tietAddVendedoresDireccion)
        // usa calendario
        val tilAddVendedoresFechaNacimiento: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresFechaNacimiento)
        val tietAddVendedoresFechaNacimiento: TextInputEditText = dialog.findViewById(R.id.tietAddVendedoresFechaNacimiento)
        //
        val tilAddVendedoresTelefono1: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresTelefono1)
        val tietAddVendedoresTelefono1: TextInputEditText = dialog.findViewById(R.id.tietAddVendedoresTelefono1)
        val tilAddVendedoresTelefono2: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresTelefono2)
        val tietAddVendedoresTelefono2: TextInputEditText = dialog.findViewById(R.id.tietAddVendedoresTelefono2)
        // SPINNER
        val tilAddVendedoresUsuarios: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresUsuarios)
        val spAddVendedoresUsuarios: Spinner = dialog.findViewById(R.id.spAddVendedoresUsuarios)
        //
        val btnAddVendedoresGuardar: Button = dialog.findViewById(R.id.btnAddVendedoresGuardar)


        //llenando datos para el update
        tvAddVendedoresTitle.text = "Actualizar Vendedor"
        tietAddVendedoresNombre.setText(nombres)
        tietAddVendedoresCorreo.setText(correo)
        tietAddVendedoresDireccion.setText(direccion)
        tietAddVendedoresFechaNacimiento.setText(fecha_nacimiento)
        tietAddVendedoresTelefono1.setText(telefono1)
        tietAddVendedoresTelefono2.setText(telefono2)
        // despues se pone la seleccion del spinner
        tietAddVendedoresNombre.requestFocus()

        ibAddVendedoresClose.setOnClickListener{
            dialog.dismiss()
        }

        var selectedDate: String = fecha_nacimiento

        tietAddVendedoresFechaNacimiento.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(context, { _, year, month, dayOfMonth ->
                selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                tietAddVendedoresFechaNacimiento.setText(selectedDate)
            }, year, month, day)
            datePickerDialog.show()
        }

        //
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listUserNotUse().body()
            (itemView.context as? AppCompatActivity)?.runOnUiThread {
                if (response != null && response.status == 200) {
                    val userNotUseList = response.data.map { it.usuario }
                    val categoriasWithSelect = listOf("Seleccionar") + userNotUseList
                    val adapterLoad = ArrayAdapter(context, android.R.layout.simple_spinner_item, categoriasWithSelect)
                    adapterLoad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spAddVendedoresUsuarios.adapter = adapterLoad
                    adapterLoad.notifyDataSetChanged()
                    // lleno el spinner y establezco posicion
                    //spAddVendedoresUsuarios.setSelection(id_usuarios)
                } else {
                    Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // -----------------------------
        var selectedUserId: Int? = id_usuarios

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listUserNotUse().body()
            spAddVendedoresUsuarios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedUserId = if (position > 0) {
                        response?.data?.get(position - 1)?.id_usuarios
                    } else { null }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { selectedUserId = null }
            }
        }

        //
        btnAddVendedoresGuardar.setOnClickListener{
            var actVen = 0
            val nomVen = tietAddVendedoresNombre.text.toString()
            val te1Ven = tietAddVendedoresTelefono1.text.toString()
            val te2Ven = tietAddVendedoresTelefono2.text.toString()
            val corVen = tietAddVendedoresCorreo.text.toString()
            val dirVen = tietAddVendedoresDireccion.text.toString()
            val fecVen = selectedDate
            if (activo){ actVen = 1 } // si es true manda 1 para el active true xd
            val idUserVen = selectedUserId


            CoroutineScope(Dispatchers.IO).launch {
                val vendedor = Vendedores(nomVen,te1Ven,te2Ven,corVen,dirVen,fecVen,actVen,idUserVen!!)
                apiService.updateVendedores(vendedor, id_vendedor)

                val updatedData = apiService.listVendedoresTrue().body()?.data?.results

                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    if (updatedData != null) {
                        (itemView.context as? AppCompatActivity)?.let {
                            val adapter = it.findViewById<RecyclerView>(R.id.rvVendedores)
                            (adapter?.adapter as? VendedoresAdapter)?.updateList(updatedData)
                        }
                    }
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogDeleteVendedores(context: Context,id:Int,nombres:String){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_confirm)

        val tvConfirmMessage: TextView = dialog.findViewById(R.id.tvConfirmMessage)
        val btnConfirmCancelar: Button = dialog.findViewById(R.id.btnConfirmCancelar)
        val btnConfirmConfirmar: Button = dialog.findViewById(R.id.btnConfirmConfirmar)
        tvConfirmMessage.text = "¿Seguro que quieres eliminar al vendedor $nombres?"
        btnConfirmCancelar.setOnClickListener{
            dialog.dismiss()
        }
        btnConfirmConfirmar.setOnClickListener{
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    (itemView.context as? AppCompatActivity)?.let {
                        val adapter = it.findViewById<RecyclerView>(R.id.rvVendedores) //
                        (adapter?.adapter as? VendedoresAdapter)?.removeItem(position) //
                    }
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                apiService.deleteVendedores(id)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

}