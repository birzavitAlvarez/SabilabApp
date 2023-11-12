package com.demo.sabilabapp.Pedidos

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.Adapters.ProductosAdapter
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ActivityPedidos2psBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
// para la lista de productos seleccionados
import com.demo.sabilabapp.Adapters.SequencePedidos.Pedidos2psAdapter
import com.demo.sabilabapp.Pedidos.ProductosSeleccionados
// dialog data
import com.demo.sabilabapp.Adapters.SequencePedidos.Pedidos2spProductosAdapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.demo.sabilabapp.Productos.Result as ResultProductos

class Pedidos2psActivity : AppCompatActivity() {

    private var binding: ActivityPedidos2psBinding? = null
    // data para el seleccionar productos
    private lateinit var adapter: Pedidos2psAdapter
    private val datitos = mutableListOf<ProductosSeleccionados>()
    //

    private var id_cliente:Int? = null
    private var direccion:String? = null
    private var distrito:String? = null
    private var fecha_entrega:String? = null
    private var id_comprobante:Int? = null
    private var id_vendedor:Int? = null
    // adapter y data pal dialog productos
    private lateinit var adapterProductosDialog: Pedidos2spProductosAdapter
    private val datitosProductosDialog = mutableListOf<ResultProductos>()
    var verduradialog: Boolean = false
    private var currentPage: Int = 1
    private var totalPages: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidos2psBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val extras = intent.extras

        if (extras != null) {
            id_cliente = extras.getInt("id_cliente")
            direccion = extras.getString("direccion")
            distrito = extras.getString("distrito")
            fecha_entrega = extras.getString("fecha_entrega")
            id_comprobante = extras.getInt("id_comprobante")
            id_vendedor = extras.getInt("id_vendedor")
        }
//        binding?.tvIdCliente?.text = idCliente.toString()
//        binding?.tvDireccion?.text = direccion.toString()
//        binding?.tvDistrito?.text = distrito.toString()
//        binding?.tvFechaEntrega?.text = fechaEntrega.toString()
//        binding?.tvIdComprobante?.text = idComprobante.toString()
//        binding?.tvIdVendedor?.text = idVendedor.toString()
        initRecyclerView()

        binding?.btnPedidos2psRegresar?.setOnClickListener {
            onBackPressed()
        }

        binding?.ibPedidos2psCancelar?.setOnClickListener {
            val anny = Intent(this@Pedidos2psActivity, Pedidos2Activity::class.java)
            anny.putExtra("id_vendedor", id_vendedor)
            startActivity(anny)
        }

        binding?.btnPedidos2psAgregarProductos?.setOnClickListener {
            showDialogPedidos2psProductos()
        }

    }



    private fun showDialogPedidos2psProductos() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.item_dialog_pedidos2sp_productos)

        val ibPedidos2spProductosClose: ImageButton = dialog.findViewById(R.id.ibPedidos2spProductosClose)
        val tilPedidos2spProductosNombre: TextInputLayout = dialog.findViewById(R.id.tilPedidos2spProductosNombre)
        val tietPedidos2spProductosNombre: TextInputEditText = dialog.findViewById(R.id.tietPedidos2spProductosNombre)
        val btnPedidos2spProductosBuscar: Button = dialog.findViewById(R.id.btnPedidos2spProductosBuscar)
        val ibPedidos2spProductosBefore: AppCompatImageButton = dialog.findViewById(R.id.ibPedidos2spProductosBefore)
        val tvPedidos2spProductosNumeroPagina: TextView = dialog.findViewById(R.id.tvPedidos2spProductosNumeroPagina)
        val ibPedidos2spProductosNext: AppCompatImageButton = dialog.findViewById(R.id.ibPedidos2spProductosNext)

        val rvPedidos2spProductos: RecyclerView = dialog.findViewById(R.id.rvPedidos2spProductos)
        adapterProductosDialog = Pedidos2spProductosAdapter(datitosProductosDialog)
        rvPedidos2spProductos.layoutManager = LinearLayoutManager(this)
        rvPedidos2spProductos.adapter = adapterProductosDialog

        listaAlEntrarDialog(rvPedidos2spProductos, adapterProductosDialog)



        ibPedidos2spProductosClose.setOnClickListener {
            dialog.dismiss()
        }

        btnPedidos2spProductosBuscar.setOnClickListener {
            var nombre = tietPedidos2spProductosNombre.text
        }

        ibPedidos2spProductosBefore.setOnClickListener{
            var pagi = tvPedidos2spProductosNumeroPagina.text
        }

        ibPedidos2spProductosNext.setOnClickListener {
            var pagi = tvPedidos2spProductosNumeroPagina.text
        }



        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrarDialog(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listProductosTrue()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    (adapter as Pedidos2spProductosAdapter).updateList(dataItems)
                    recyclerView.layoutManager?.scrollToPosition(0)
                    getCurrentAndTotal()
                    verduradialog = false
                } else {
                    showError()
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getCurrentAndTotal(){
        //pruebna
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.item_dialog_pedidos2sp_productos)
        val tvPedidos2spProductosNumeroPagina: TextView = dialog.findViewById(R.id.tvPedidos2spProductosNumeroPagina)
        //fin prueba
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listProductosTrue()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        //binding?.tvPedidos2spProductosNumeroPagina?.text = "$currentPage/$totalPages"
                        tvPedidos2spProductosNumeroPagina?.text = "$currentPage/$totalPages"
                    }
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = Pedidos2psAdapter(datitos)
        binding?.rvPedidos2ps?.layoutManager = LinearLayoutManager(this)
        binding?.rvPedidos2ps?.adapter = adapter
    }


}