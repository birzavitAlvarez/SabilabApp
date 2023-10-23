package com.demo.sabilabapp.Adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Usuarios.Result
import com.demo.sabilabapp.databinding.ItemUsuarioBinding


class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding : ItemUsuarioBinding? = null

//    private val textViewUsername: TextView = itemView.findViewById(R.id.rvUsuarios)
//
//    fun render(result: Result, onItemSelected: (Int) -> Unit) {
//        // Rellena las vistas con los datos del objeto Result
//        textViewUsername.text = result.usuario
//        // Configura otras vistas según sea necesario
//    }

    fun bind (query: Result){
        //picasso.get().load(image).into(binding?.textViewUsername)
        binding?.tvIdUsuarios
        binding?.tvUsuario
        binding?.tvPassword
        binding?.tvActivo
        binding?.tvRol
    }
}
