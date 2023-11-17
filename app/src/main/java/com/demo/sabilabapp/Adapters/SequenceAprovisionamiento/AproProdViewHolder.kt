package com.demo.sabilabapp.Adapters.SequenceAprovisionamiento

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
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
import com.demo.sabilabapp.Adapters.ProductosAdapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.Productos.Result // otro
import com.demo.sabilabapp.databinding.ItemDataPedidos2spProductosBinding // OTRO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar
import java.util.Locale

class AproProdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemDataPedidos2spProductosBinding = ItemDataPedidos2spProductosBinding.bind(itemView)

    fun bind (query: Result){
        binding.tvNombrePedidos2psProductos.text = query.nombre
        binding.tvPrecioPedidos2psProductos.text = query.precio.toString()

        binding.ibSeleccionarPedidos2psProductos.setOnClickListener{
            showDialogEditProductos(itemView.context,query.id_productos)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun showDialogEditProductos(context: Context,id_productos:Int){
        // TODO MANDO ID_PRODUCTO Y ACA OBTENGO EL ID_PROVEEDOR Y CANTIDAD
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.item_cantidad_pedidos_ps)

        dialog.show()
    }

}