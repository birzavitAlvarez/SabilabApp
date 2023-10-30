package com.demo.sabilabapp.Adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Productos.Result // otro
import com.demo.sabilabapp.Productos.Productos //para agregar
import com.demo.sabilabapp.databinding.ItemProductosBinding // OTRO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout



class ProductosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemProductosBinding = ItemProductosBinding.bind(itemView)

    fun bind (query: Result){
        binding.tvNombreProductos.text = query.nombre
        binding.tvLaboratorioProductos.text = query.laboratorio
        binding.tvPrecioProductos.text = query.precio.toString()
        binding.tvLoteProductos.text = query.lote
        binding.tvFechaCaducidadProductos.text = query.fecha_caducidad
        binding.tvCategoriaProductos.text = query.tipo

        binding.ibProductosEdit.setOnClickListener{
            showDialog(itemView.context,query.id_productos,query.nombre,query.laboratorio,query.lote,
                query.precio,query.fecha_caducidad,query.activo,query.id_categoria)
        }

        binding.ibProductosDelete.setOnClickListener {
            showDialog2(itemView.context,query.id_productos,query.nombre)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun showDialog(context: Context,id_productos:Int,nombre:String,laboratorio:String, lote:String, precio:Double, fecha_caducidad:String, activo:Boolean, id_categoria:Int){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_add_productos)

        val tvAddProductosTitle: TextView = dialog.findViewById(R.id.tvAddProductosTitle)
        val ibAddProductosClose: ImageButton = dialog.findViewById(R.id.ibAddProductosClose)
        val tilAddProductosNombre: TextInputLayout = dialog.findViewById(R.id.tilAddProductosNombre)
        val tietAddProductosNombre: TextInputEditText = dialog.findViewById(R.id.tietAddProductosNombre)
        val tilAddProductosLaboratorio: TextInputLayout = dialog.findViewById(R.id.tilAddProductosLaboratorio)
        val tietAddProductosLaboratorio: TextInputEditText = dialog.findViewById(R.id.tietAddProductosLaboratorio)
        val tilAddProductosLote: TextInputLayout = dialog.findViewById(R.id.tilAddProductosLote)
        val tietAddProductosLote: TextInputEditText = dialog.findViewById(R.id.tietAddProductosLote)
        val tilAddProductosPrecio: TextInputLayout = dialog.findViewById(R.id.tilAddProductosPrecio)
        val tietAddProductosPrecio: TextInputEditText = dialog.findViewById(R.id.tietAddProductosPrecio)
        val tilAddProductosCaducidad: TextInputLayout = dialog.findViewById(R.id.tilAddProductosCaducidad)
        val tietAddProductosCaducidad: TextInputEditText = dialog.findViewById(R.id.tietAddProductosCaducidad)
        val tilAddProductosCategoria: TextInputLayout = dialog.findViewById(R.id.tilAddProductosCategoria)
        val spAddProductosCategoria: Spinner = dialog.findViewById(R.id.spAddProductosCategoria)
        val btnAddProductosGuardar: Button = dialog.findViewById(R.id.btnAddProductosGuardar)
        //llenando datos para el update
        tvAddProductosTitle.text = "Actualizar Producto"
        tietAddProductosNombre.setText(nombre)
        tietAddProductosLaboratorio.setText(laboratorio)
        tietAddProductosLote.setText(lote)
        tietAddProductosPrecio.setText(precio.toString())
        tietAddProductosCaducidad.setText(fecha_caducidad)
        spAddProductosCategoria.setSelection(id_categoria)
        tietAddProductosNombre.requestFocus()

        ibAddProductosClose.setOnClickListener{
            dialog.dismiss()
        }

        btnAddProductosGuardar.setOnClickListener{
            var actVal = 0
            val nomPro = tietAddProductosNombre.text.toString()
            val labPro = tietAddProductosLaboratorio.text.toString()
            val prePro = tietAddProductosPrecio.text.toString().toDouble()
            val lotPro = tietAddProductosLote.text.toString()
            val cadPro = tietAddProductosCaducidad.text.toString()
            if (activo){ // si es true
                actVal = 1
            }
            val catPro = spAddProductosCategoria.selectedItem
//            actualizar id_categoria

            CoroutineScope(Dispatchers.IO).launch {
                val producto = Productos(nomPro, labPro, prePro, lotPro, cadPro,actVal,id_categoria)
                apiService.updateProductos(producto, id_productos)

                val updatedData = apiService.listProductosTrue().body()?.data?.results

                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    if (updatedData != null) {
                        (itemView.context as? AppCompatActivity)?.let {
                            val adapter = it.findViewById<RecyclerView>(R.id.rvProductos)
                            (adapter?.adapter as? ProductosAdapter)?.updateList(updatedData)
                        }
                    }
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog2(context: Context,id:Int,nombre:String){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_confirm)

        val tvConfirmMessage: TextView = dialog.findViewById(R.id.tvConfirmMessage)
        val btnConfirmCancelar: Button = dialog.findViewById(R.id.btnConfirmCancelar)
        val btnConfirmConfirmar: Button = dialog.findViewById(R.id.btnConfirmConfirmar)
        tvConfirmMessage.text = "¿Seguro que quieres eliminar el producto $nombre?"
        btnConfirmCancelar.setOnClickListener{
            dialog.dismiss()
        }
        btnConfirmConfirmar.setOnClickListener{
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                (itemView.context as? AppCompatActivity)?.runOnUiThread {
                    (itemView.context as? AppCompatActivity)?.let {
                        val adapter = it.findViewById<RecyclerView>(R.id.rvProductos) //
                        (adapter?.adapter as? ProductosAdapter)?.removeItem(position) //
                    }
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                apiService.deleteProductos(id)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

}

// picasso par mostrar imagenes
// picasso.get().load(image).into(binding?.textViewUsername)