package com.demo.sabilabapp.Pedidos

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ActivityPedidos2psBinding
// para la lista de productos seleccionados
import com.demo.sabilabapp.Adapters.SequencePedidos.Pedidos2psAdapter
import com.demo.sabilabapp.Adapters.SequencePedidos.OnProductoSeleccionadoListener // interface
// dialog data
import com.demo.sabilabapp.Adapters.SequencePedidos.Pedidos2spProductosAdapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.demo.sabilabapp.Productos.Result as ResultProductos
//
import com.demo.sabilabapp.databinding.ItemDialogPedidos2spProductosBinding
import com.demo.sabilabapp.Adapters.SequencePedidos.OnItemUpdateListener

class Pedidos2psActivity : AppCompatActivity(), OnProductoSeleccionadoListener, OnItemUpdateListener {

    private var binding: ActivityPedidos2psBinding? = null
    private var bindingDialog: ItemDialogPedidos2spProductosBinding? = null
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
    var verduraDialog: Boolean = false
    private var currentPageDialog: Int = 1
    private var totalPagesDialog: Int = 1


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

        val totalEntrada:String = "Total: S/ 0.0"
        binding?.tvPedidos2psTotal?.text = totalEntrada

        binding?.btnPedidos2psFinalizarPedido?.setOnClickListener {
            // TODO MANDAR POST A PEDIDOS Y DETALLEPEDIDO
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onProductoSeleccionado(productosSeleccionados: ProductosSeleccionados) {
        datitos.add(productosSeleccionados)
        adapter.notifyDataSetChanged()
        actualizarTotal()
    }

    private fun actualizarTotal() {
        var totalps: Double = 0.0
        for (producto in datitos) {
            totalps += producto.total
        }
        val totalFormateado = String.format("%.2f", totalps)
        binding?.tvPedidos2psTotal?.text = "Total: S/ $totalFormateado"
    }



    private fun showDialogPedidos2psProductos() {
        val dialog = Dialog(this)
        //dialog.setContentView(R.layout.item_dialog_pedidos2sp_productos)
        bindingDialog = ItemDialogPedidos2spProductosBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog?.root!!)

        var nombrepro: String = ""

        // iniciando el recycler view de productos
        val rvPedidos2spProductos: RecyclerView = dialog.findViewById(R.id.rvPedidos2spProductos)
        adapterProductosDialog = Pedidos2spProductosAdapter(datitosProductosDialog,this)
        rvPedidos2spProductos.layoutManager = LinearLayoutManager(this)
        rvPedidos2spProductos.adapter = adapterProductosDialog
        // lista para la vista del rv
        listaAlEntrarDialog(rvPedidos2spProductos, adapterProductosDialog)


        bindingDialog?.ibPedidos2spProductosClose?.setOnClickListener {
            dialog.dismiss()
        }

        bindingDialog?.btnPedidos2spProductosBuscar?.setOnClickListener {
            nombrepro = bindingDialog?.tietPedidos2spProductosNombre?.text.toString()
            searchByItemDialog(nombrepro)
        }

        // pagina siguiente
        bindingDialog?.ibPedidos2spProductosNext?.setOnClickListener {
            if (currentPageDialog < totalPagesDialog) {
                currentPageDialog += 1
                if (verduraDialog) {
                    val query = bindingDialog?.tietPedidos2spProductosNombre?.text?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearchDialog(query,currentPageDialog)
                    }
                } else {
                    nextPageDialog(currentPageDialog)
                }
            }
        }
        // pagina anterior
        bindingDialog?.ibPedidos2spProductosBefore?.setOnClickListener {
            if (currentPageDialog > 1) {
                currentPageDialog -= 1
                if (verduraDialog) {
                    val query = bindingDialog?.tietPedidos2spProductosNombre?.text?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearchDialog(query,currentPageDialog)
                    }
                } else {
                    nextPageDialog(currentPageDialog)
                }
            }
        }


        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageDialog(np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.paginaProductos(np)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitosProductosDialog.clear()
                    datitosProductosDialog.addAll(dataItems)
                    adapterProductosDialog.notifyDataSetChanged()
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPageDialog = pagination.currentPage
                        totalPagesDialog = pagination.totalPages
                        bindingDialog?.tvPedidos2spProductosNumeroPagina?.text = "$currentPageDialog/$totalPagesDialog"
                    } else {showErrorDialog()}
                } else { showErrorDialog() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageSearchDialog(query:String,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarProductosPorNombreYPage(query, np)//.body()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitosProductosDialog.clear()
                    datitosProductosDialog.addAll(dataItems)
                    adapterProductosDialog.notifyDataSetChanged()
                    val pagination = response0?.data?.pagination
                    pagination?.currentPage!!.also { currentPageDialog = it }
                    pagination.totalPages.also { totalPagesDialog = it }
                    bindingDialog?.tvPedidos2spProductosNumeroPagina?.text = "$currentPageDialog/$totalPagesDialog"
                } else {
                    showErrorDialog()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByItemDialog(nombrepro: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarProductosPorFiltro(nombrepro)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitosProductosDialog.clear()
                    datitosProductosDialog.addAll(dataItems)
                    adapterProductosDialog.notifyDataSetChanged()
                    performSearchDialog(nombrepro)
                } else { showErrorDialog() }
                hideKeyboardDialog()
                getCurrentAndTotalDialog()
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun performSearchDialog(nombrepro: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listarProductosPorFiltro(nombrepro ?: "").body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val pagination = response.data.pagination
                    currentPageDialog = pagination.currentPage
                    totalPagesDialog = pagination.totalPages
                    bindingDialog?.tvPedidos2spProductosNumeroPagina?.text = "$currentPageDialog/$totalPagesDialog"
                    verduraDialog = true
                    adapterProductosDialog.notifyDataSetChanged()
                } else { showErrorDialog() }
            }
        }
    }

    private fun hideKeyboardDialog() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(bindingDialog?.vistaPedidos2spProductosPadre?.windowToken,0)
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
                    getCurrentAndTotalDialog()
                    verduraDialog = false
                } else {
                    showErrorDialog()
                }
            }
        }
    }

    private fun showErrorDialog() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getCurrentAndTotalDialog(){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listProductosTrue()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPageDialog = pagination.currentPage
                        totalPagesDialog = pagination.totalPages
                        // Actualiza la vista de paginación en tu diálogo principal
                        bindingDialog?.tvPedidos2spProductosNumeroPagina?.text = "$currentPageDialog/$totalPagesDialog"
                    }
                    adapterProductosDialog.notifyDataSetChanged()
                } else {
                    showErrorDialog()
                }
            }

        }
    }


    private fun initRecyclerView() {
        adapter = Pedidos2psAdapter(datitos,this)
        binding?.rvPedidos2ps?.layoutManager = LinearLayoutManager(this)
        binding?.rvPedidos2ps?.adapter = adapter
    }

    override fun onItemUpdated() {
        actualizarTotal()
    }


}