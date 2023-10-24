package com.demo.sabilabapp.Adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Usuarios.Result
import com.demo.sabilabapp.databinding.ItemUsuarioBinding


class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemUsuarioBinding = ItemUsuarioBinding.bind(itemView)

    fun bind (query: Result){
        //picasso.get().load(image).into(binding?.textViewUsername)
        binding.tvUsuario.text = query.usuario
        binding.tvRol.text = query.rol
    }


}