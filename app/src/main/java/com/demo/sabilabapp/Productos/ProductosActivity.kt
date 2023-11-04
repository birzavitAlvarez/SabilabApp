package com.demo.sabilabapp.Productos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.sabilabapp.Adapters.ProductosAdapter // OTRO
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.Productos.Result // OTRO
import com.demo.sabilabapp.Productos.Productos // OTRO
import com.demo.sabilabapp.databinding.ActivityProductosBinding // OTRO
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class ProductosActivity : AppCompatActivity(), OnQueryTextListener {

    private var binding: ActivityProductosBinding? = null
    private lateinit var adapter: ProductosAdapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1
    private var verdura: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //
        initRecyclerView()
        listaAlEntrar()
        //
        binding?.svProductosBusqueda?.setOnQueryTextListener(this)
        // boton buscar xd
        binding?.btnProductosBuscar?.setOnClickListener {
            val query = binding?.svProductosBusqueda?.query?.toString()
            if (!query.isNullOrBlank()) {
                searchByItem(query)
            }
        }
        // pagina siguiente
        binding?.ibProductosNext?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                if (verdura) {
                    val query = binding?.svProductosBusqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,currentPage)
                    }
                } else {
                    nextPage(currentPage)
                }
            }
        }
        // pagina anterior
        binding?.ibProductosBefore?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                if (verdura) {
                    val query = binding?.svProductosBusqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,currentPage)
                    }
                } else {
                    nextPage(currentPage)
                }
            }
        }
        // boton agregar
        binding?.btnProductosAgregar?.setOnClickListener {
            showDialogAddProductos()
        }

    }
    //
    private fun showDialogAddProductos(){
        val dialog = Dialog(this)
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
        tietAddProductosNombre.requestFocus()

        ibAddProductosClose.setOnClickListener{
            dialog.dismiss()
        }
        //---------------------------------------------------------------------------------------
        // fecha de calendario
        var selectedDate: String = ""

        tietAddProductosCaducidad.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                tietAddProductosCaducidad.setText(selectedDate)
            }, year, month, day)
            datePickerDialog.show()
        }

        // categorias para escoger id_Categorias
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listCategory().body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val categoriasList = response.data.map { it.tipo } // TODO waaaaaaa
                    val categoriasWithSelect = listOf("Seleccionar") + categoriasList
                    val adapterLoad = ArrayAdapter(this@ProductosActivity, android.R.layout.simple_spinner_item, categoriasWithSelect)
                    adapterLoad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spAddProductosCategoria.adapter = adapterLoad
                    // el log lo uso para ver la impresion de datos en consola :v
                    //Log.d("DataUsuario", rolesList.toString())
                    adapterLoad.notifyDataSetChanged()
                } else {
                    showError()
                }
            }
        }
        // -----------------------------
        var selectedCategoriaId: Int? = null

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listCategory().body()
            spAddProductosCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedCategoriaId = if (position > 0) {
                        response?.data?.get(position - 1)?.id_categoria
                    } else { null }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { selectedCategoriaId = null }
            }
        }

        btnAddProductosGuardar.setOnClickListener{
            val nomPro = tietAddProductosNombre.text.toString()
            val labPro = tietAddProductosLaboratorio.text.toString()
            val prePro = tietAddProductosPrecio.text.toString().toDouble()
            val lotPro = tietAddProductosLote.text.toString()
            val cadPro = selectedDate
            val actPro = 1

            CoroutineScope(Dispatchers.IO).launch {
                if (selectedCategoriaId != null) {
                    val productos = Productos(nomPro,labPro,prePro,lotPro,cadPro,actPro, selectedCategoriaId!!)
                    apiService.createProductos(productos)
                    // Después de agregar el usuario Actualizo la lista
                    val updatedData = apiService.listProductosTrue().body()?.data?.results
                    runOnUiThread {
                        if (updatedData != null) {
                            adapter.updateList(updatedData)
                        }
                        hideKeyboard()
                        dialog.dismiss()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ProductosActivity, "Error: Debe seleccionar una Categoria", Toast.LENGTH_SHORT).show()
                        spAddProductosCategoria.requestFocus()
                    }

                }
            }
            getCurrentAndTotal()
        }
        //
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPage(np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.paginaProductos(np)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvProductosNumeroPagina?.text = "$currentPage/$totalPages"
                    } else {showError()}
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageSearch(query:String,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarProductosPorNombreYPage(query, np)//.body()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    val pagination = response0?.data?.pagination
                    pagination?.currentPage!!.also { currentPage = it }
                    pagination.totalPages.also { totalPages = it }
                    binding?.tvProductosNumeroPagina?.text = "$currentPage/$totalPages"
                } else {
                    showError()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByItem(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarProductosPorFiltro(query)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    performSearch(query)
                } else { showError() }
                hideKeyboard()
                getCurrentAndTotal()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.vistaProductosPadre?.windowToken,0)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun performSearch(query: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listarProductosPorFiltro(query ?: "").body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val pagination = response.data.pagination
                    currentPage = pagination.currentPage
                    totalPages = pagination.totalPages
                    binding?.tvProductosNumeroPagina?.text = "$currentPage/$totalPages"
                    verdura = true
                    adapter.notifyDataSetChanged() //TODO
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrar() {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listProductosTrue()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    getCurrentAndTotal()
                    verdura = false
                } else { showError() }
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getCurrentAndTotal(){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listProductosTrue()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvProductosNumeroPagina?.text = "$currentPage/$totalPages"
                    }
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    private fun initRecyclerView() {
        adapter = ProductosAdapter(datitos)
        binding?.rvProductos?.layoutManager = LinearLayoutManager(this)
        binding?.rvProductos?.adapter = adapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchByItem(query.lowercase(Locale.ROOT))
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }


}