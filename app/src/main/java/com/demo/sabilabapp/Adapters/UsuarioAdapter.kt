package com.demo.sabilabapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Usuarios.Result

class UsuariosAdapter(private val usuariosList: List<Result>) : RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usuarioTextView: TextView = itemView.findViewById(R.id.textUsuario)
        val rolTextView: TextView = itemView.findViewById(R.id.textRol)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.usuario_item, parent, false)
        return UsuarioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuariosList[position]

        holder.usuarioTextView.text = usuario.usuario
        holder.rolTextView.text = usuario.rol
    }

    override fun getItemCount() = usuariosList.size
}