package com.demo.sabilabapp.Adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Clientes.Result
import com.demo.sabilabapp.databinding.ItemClientes2Binding

class Clientes2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemClientes2Binding = ItemClientes2Binding.bind(itemView)

    fun bind (query: Result){
        binding.tvRucClientes2.text = query.ruc
        binding.tvRazonSocialClientes2.text = query.razon_social
        binding.tvNombreComercialClientes2.text = query.nombre_comercial
        binding.tvDireccionClientes2.text = query.direccion1
        binding.tvDistritoClientes2.text = query.distrito
        binding.tvContactoClientes2.text = query.contacto
        binding.tvTelefonoClientes2.text = query.telefono1
    }
}