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
import com.demo.sabilabapp.Categorias.Data
import com.demo.sabilabapp.Categorias.Categoria // para agregar
import com.demo.sabilabapp.databinding.ItemCategoriaBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class CategoriasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemCategoriaBinding = ItemCategoriaBinding.bind(itemView)


    fun bind (query: Data){
        // pinta al cargar el texto de tipo y descripcion
        binding.tvTipo.text = query.tipo
        binding.tvDescripcion.text = query.descripcion

        binding.ibCategoriasEdit.setOnClickListener{
            showDialogEditCategory(itemView.context,query.id_categoria,query.tipo,query.descripcion)
        }

        binding.ibCategoriasDelete.setOnClickListener {
            showDialogDeleteCategory(itemView.context,query.id_categoria,query.tipo)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun showDialogEditCategory(context: Context,id:Int,tipo:String,descripcion:String){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_add_categoria)

        val tvAddCategoriaTitle: TextView = dialog.findViewById(R.id.tvAddCategoriaTitle)
        val ibAddCategoriaClose: ImageButton = dialog.findViewById(R.id.ibAddCategoriaClose)
        val tilAddCategoriaTipo: TextInputLayout = dialog.findViewById(R.id.tilAddCategoriaTipo)
        val tietAddCategoriaTipo: TextInputEditText = dialog.findViewById(R.id.tietAddCategoriaTipo)
        val tilAddCategoriaDescripcion: TextInputLayout = dialog.findViewById(R.id.tilAddCategoriaDescripcion)
        val tietAddCategoriaDescripcion: TextInputEditText = dialog.findViewById(R.id.tietAddCategoriaDescripcion)
        val btnAddCategoriaGuardar: Button = dialog.findViewById(R.id.btnAddCategoriaGuardar)
        tvAddCategoriaTitle.text = "Actualizar Categoria"
        tietAddCategoriaTipo.setText(tipo)
        tietAddCategoriaDescripcion.setText(descripcion)
        tietAddCategoriaTipo.requestFocus()
        ibAddCategoriaClose.setOnClickListener{
            dialog.dismiss()
        }

        btnAddCategoriaGuardar.setOnClickListener{
            val texTipo = tietAddCategoriaTipo.text.toString()
            val textDescripcion = tietAddCategoriaDescripcion.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val categoria = Categoria(texTipo, textDescripcion)
                apiService.updateCategory(categoria, id)  // TODO

                val updatedData = apiService.listCategory().body()?.data

                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    if (updatedData != null) {
                        (itemView.context as? AppCompatActivity)?.let {
                            val adapter = it.findViewById<RecyclerView>(R.id.rvCategoria)
                            (adapter?.adapter as? CategoriasAdapter)?.updateList(updatedData)
                        }
                    }
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogDeleteCategory(context: Context,id:Int,tipo:String){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_confirm)

        val tvConfirmMessage: TextView = dialog.findViewById(R.id.tvConfirmMessage)
        val btnConfirmCancelar: Button = dialog.findViewById(R.id.btnConfirmCancelar)
        val btnConfirmConfirmar: Button = dialog.findViewById(R.id.btnConfirmConfirmar)
        tvConfirmMessage.text = "¿Seguro que quieres eliminar la categoria $tipo?"
        btnConfirmCancelar.setOnClickListener{
            dialog.dismiss()
        }
        btnConfirmConfirmar.setOnClickListener{
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    (itemView.context as? AppCompatActivity)?.let {
                        val adapter = it.findViewById<RecyclerView>(R.id.rvCategoria)
                        (adapter?.adapter as? CategoriasAdapter)?.removeItem(position)
                    }
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                apiService.deleteCategory(id)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

}

// picasso par mostrar imagenes
// picasso.get().load(image).into(binding?.textViewUsername)