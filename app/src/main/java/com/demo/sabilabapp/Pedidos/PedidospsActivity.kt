package com.demo.sabilabapp.Pedidos

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.sabilabapp.R
//
import com.demo.sabilabapp.Adapters.SequenceAdmin.OnItemUpdateListenerAdmin
import com.demo.sabilabapp.Adapters.SequenceAdmin.OnProductoSeleccionadoListenerAdmin
import com.demo.sabilabapp.Adapters.SequenceAdmin.PedidospsAdapter
import com.demo.sabilabapp.databinding.ActivityPedidospsBinding
import com.demo.sabilabapp.databinding.ItemDialogPedidos2spProductosBinding
import com.demo.sabilabapp.Adapters.SequenceAdmin.PedidosspProductosAdapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.DetallePedido.DetallePedidoPost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.demo.sabilabapp.Productos.Result as ResultProductos


class PedidospsActivity : AppCompatActivity(), OnProductoSeleccionadoListenerAdmin, OnItemUpdateListenerAdmin {

    private var binding: ActivityPedidospsBinding? = null
    // data para el seleccionar productos
    private lateinit var adapter: PedidospsAdapter
    private val datitos = mutableListOf<ProductosSeleccionados>()
    //
    private var id_cliente:Int? = null
    private var direccion:String? = null
    private var distrito:String? = null
    private var fecha_entrega:String? = null
    private var id_comprobante:Int? = null
    private var id_vendedor:Int? = null

    // adapter y data pal dialog productos
    private var bindingDialog: ItemDialogPedidos2spProductosBinding? = null
    private lateinit var adapterProductosDialog: PedidosspProductosAdapter
    private val datitosProductosDialog = mutableListOf<ResultProductos>()
    var verduraDialog: Boolean = false
    private var currentPageDialog: Int = 1
    private var totalPagesDialog: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidospsBinding.inflate(layoutInflater)
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

        binding?.btnPedidospsRegresar?.setOnClickListener {
            onBackPressed()
        }

        binding?.ibPedidospsCancelar?.setOnClickListener {
            val anny = Intent(this@PedidospsActivity, PedidosActivity::class.java)
            //anny.putExtra("id_vendedor", id_vendedor)
            startActivity(anny)
        }

        binding?.btnPedidospsAgregarProductos?.setOnClickListener {
            showDialogPedidospsProductos()
        }

        binding?.btnPedidospsFinalizarPedido?.setOnClickListener {
            postParaPedidos(
                direccion.toString(),
                distrito.toString(),
                fecha_entrega.toString(),
                binding?.tvPedidospsTotal?.text.toString().toDouble(),
                id_comprobante.toString().toInt(),
                id_vendedor.toString().toInt(),
                id_cliente.toString().toInt()
            )
        }
    }

    private fun postParaPedidos(direccionp: String, distritop: String, fecha_entregap: String, totalFinalp: Double,id_comprobantep: Int, id_vendedorp: Int, id_clientep: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val orders = Pedidos(direccionp, distritop, fecha_entregap, "", totalFinalp, 1, id_comprobantep, id_vendedorp, id_clientep)
                val response = apiService.createPedido(orders)

                if (response.isSuccessful) {
                    val idPedido = response.body()?.data?.id_pedido
                    runOnUiThread {
                        if (idPedido != null) {
                            lifecycleScope.launch {
                                for (productoSeleccionado in datitos) {
                                    val detallePedido = DetallePedidoPost(
                                        cantidad_objetiva = productoSeleccionado.cantidad,
                                        total_detalle = productoSeleccionado.total,
                                        id_pedido = idPedido,
                                        id_productos = productoSeleccionado.id_productos
                                    )
                                    apiService.createDetallePedido(detallePedido)
                                }
                            }
                            val anny = Intent(this@PedidospsActivity, PedidosActivity::class.java)
                            //anny.putExtra("id_vendedor", id_vendedorp)
                            startActivity(anny)
                        } else {
                            Toast.makeText(
                                this@PedidospsActivity,
                                "Error al obtener el ID del pedido",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@PedidospsActivity,
                            "Error al crear el pedido: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(
                        this@PedidospsActivity,
                        "Error al crear el pedido: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showDialogPedidospsProductos() {
        val dialog = Dialog(this)
        //dialog.setContentView(R.layout.item_dialog_pedidos2sp_productos)
        bindingDialog = ItemDialogPedidos2spProductosBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog?.root!!)

        var nombrepro: String = ""

        // iniciando el recycler view de productos
        val rvPedidos2spProductos: RecyclerView = dialog.findViewById(R.id.rvPedidos2spProductos)
        adapterProductosDialog = PedidosspProductosAdapter(datitosProductosDialog,this)
        rvPedidos2spProductos.layoutManager = LinearLayoutManager(this)
        rvPedidos2spProductos.adapter = adapterProductosDialog
        // lista para la vista del rv
        listaAlEntrarDialog(rvPedidos2spProductos, adapterProductosDialog)

        bindingDialog?.ibPedidos2spProductosClose?.setOnClickListener {
            dialog.dismiss()
        }
        //
        bindingDialog?.btnPedidos2spProductosBuscar?.setOnClickListener {
            nombrepro = bindingDialog?.tietPedidos2spProductosNombre?.text.toString()
            if (nombrepro.isEmpty()){
                listaAlEntrarDialog(rvPedidos2spProductos, adapterProductosDialog)
            } else {
                searchByItemDialog(nombrepro)
            }
        }
        //
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

    private fun hideKeyboardDialog() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(bindingDialog?.vistaPedidos2spProductosPadre?.windowToken,0)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrarDialog(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listProductosTrue()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    (adapter as PedidosspProductosAdapter).updateList(dataItems)
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
        adapter = PedidospsAdapter(datitos,this) // TODO
        binding?.rvPedidosps?.layoutManager = LinearLayoutManager(this)
        binding?.rvPedidosps?.adapter = adapter
    }

    override fun onItemUpdatedAdmin() {
        actualizarTotal()
    }

    private fun actualizarTotal() {
        var totalps: Double = 0.0
        for (producto in datitos) {
            totalps += producto.total
        }
        val totalFormateado = String.format("%.2f", totalps)
        binding?.tvPedidospsTotal?.text = totalFormateado
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onProductoSeleccionadoAdmin(productosSeleccionados: ProductosSeleccionados) {
        datitos.add(productosSeleccionados)
        adapter.notifyDataSetChanged()
        actualizarTotal()
    }


}