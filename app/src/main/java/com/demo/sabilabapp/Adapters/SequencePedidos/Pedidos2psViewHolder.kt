package com.demo.sabilabapp.Adapters.SequencePedidos

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
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
import com.demo.sabilabapp.Api.RetrofitClient
import com.demo.sabilabapp.Pedidos.ProductosSeleccionados
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ItemPedidos2psBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class Pedidos2psViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemPedidos2psBinding = ItemPedidos2psBinding.bind(itemView)

    fun bind (query: ProductosSeleccionados){
        binding.tvPedidos2psNombre.text = query.nombre
        binding.tvPedidos2psPrecio.text = query.precio.toString()
        binding.tvPedidos2psCantidad.text = query.cantidad.toString()
        binding.tvPedidos2psTotal.text = query.total.toString()


        binding.ibPedidos2psEdit.setOnClickListener {

        }

        binding.ibPedidos2psDelete.setOnClickListener {

        }

    }



}