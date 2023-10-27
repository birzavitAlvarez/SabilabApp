package com.demo.sabilabapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Usuarios.Result

class UsuariosAdapter(private val usuariosList: MutableList<Result>) : RecyclerView.Adapter<UsuarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuariosList[position]
        holder.bind(usuario)
    }

    override fun getItemCount() = usuariosList.size

    fun removeItem(position: Int) {
        if (position >= 0 && position < usuariosList.size) {
            usuariosList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateUser(user: Result) {
        val position = usuariosList.indexOfFirst { it.id_usuarios == user.id_usuarios }
        if (position != -1) {
            usuariosList[position] = user
            notifyItemChanged(position)
        }
    }

}