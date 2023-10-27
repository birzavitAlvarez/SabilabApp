package com.demo.sabilabapp.Adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Usuarios.Result
import com.demo.sabilabapp.databinding.ItemUsuarioBinding


class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemUsuarioBinding = ItemUsuarioBinding.bind(itemView)

    fun bind (query: Result){
        binding.tvUsuario.text = query.usuario
        binding.tvRol.text = query.rol
        //binding.ibUsuarioDelete
        //binding.ibUsuarioEdit
    }


}


// picasso par mostrar imagenes
// picasso.get().load(image).into(binding?.textViewUsername)