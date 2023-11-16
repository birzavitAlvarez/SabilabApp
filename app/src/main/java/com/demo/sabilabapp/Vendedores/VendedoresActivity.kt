package com.demo.sabilabapp.Vendedores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.text.Editable
import android.text.TextWatcher
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
import com.demo.sabilabapp.Adapters.VendedoresAdapter // OTRO
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.Vendedores.Result // OTRO
import com.demo.sabilabapp.Vendedores.Vendedores // OTRO
import com.demo.sabilabapp.databinding.ActivityVendedoresBinding // OTRO
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class VendedoresActivity : AppCompatActivity(), OnQueryTextListener {

    private var binding: ActivityVendedoresBinding? = null
    private lateinit var adapter: VendedoresAdapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1
    private var verdura: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendedoresBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //
        initRecyclerView()
        listaAlEntrar()
        //
        binding?.svVendedoresBusqueda?.setOnQueryTextListener(this)
        // boton buscar xd
        binding?.btnVendedoresBuscar?.setOnClickListener {
            val query = binding?.svVendedoresBusqueda?.query?.toString()
            if (!query.isNullOrBlank()) {
                searchByItem(query)
            } else {
                listaAlEntrar()
            }
        }
        // pagina siguiente
        binding?.ibVendedoresNext?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                if (verdura) {
                    val query = binding?.svVendedoresBusqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,currentPage)
                    }
                } else {
                    nextPage(currentPage)
                }
            }
        }
        // pagina anterior
        binding?.ibVendedoresBefore?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                if (verdura) {
                    val query = binding?.svVendedoresBusqueda?.query?.toString()
                    if (!query.isNullOrBlank()) {
                        nextPageSearch(query,currentPage)
                    }
                } else {
                    nextPage(currentPage)
                }
            }
        }
        // boton agregar
        binding?.btnVendedoresAgregar?.setOnClickListener {
            showDialogAddVendedores()
        }

        binding?.ibVendedoresRefresh?.setOnClickListener{
            listaAlEntrar()
            binding?.svVendedoresBusqueda?.setQuery("", false)
        }

    }

    private fun showDialogAddVendedores(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.item_add_vendedores)

        val tvAddVendedoresTitle: TextView = dialog.findViewById(R.id.tvAddVendedoresTitle)
        val ibAddVendedoresClose: ImageButton = dialog.findViewById(R.id.ibAddVendedoresClose)
        val tilAddVendedoresNombre: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresNombre)
        val tietAddVendedoresNombre: TextInputEditText = dialog.findViewById(R.id.tietAddVendedoresNombre)
        val tilAddVendedoresCorreo: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresCorreo)
        val tietAddVendedoresCorreo: TextInputEditText = dialog.findViewById(R.id.tietAddVendedoresCorreo)
        val tilAddVendedoresDireccion: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresDireccion)
        val tietAddVendedoresDireccion: TextInputEditText = dialog.findViewById(R.id.tietAddVendedoresDireccion)
        // usa calendario
        val tilAddVendedoresFechaNacimiento: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresFechaNacimiento)
        val tietAddVendedoresFechaNacimiento: TextInputEditText = dialog.findViewById(R.id.tietAddVendedoresFechaNacimiento)
        //
        val tilAddVendedoresTelefono1: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresTelefono1)
        val tietAddVendedoresTelefono1: TextInputEditText = dialog.findViewById(R.id.tietAddVendedoresTelefono1)
        val tilAddVendedoresTelefono2: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresTelefono2)
        val tietAddVendedoresTelefono2: TextInputEditText = dialog.findViewById(R.id.tietAddVendedoresTelefono2)
        // SPINNER
        val tilAddVendedoresUsuarios: TextInputLayout = dialog.findViewById(R.id.tilAddVendedoresUsuarios)
        val spAddVendedoresUsuarios: Spinner = dialog.findViewById(R.id.spAddVendedoresUsuarios)
        //
        val btnAddVendedoresGuardar: Button = dialog.findViewById(R.id.btnAddVendedoresGuardar)

        tietAddVendedoresNombre.requestFocus()

        ibAddVendedoresClose.setOnClickListener{
            dialog.dismiss()
        }
        //---------------------------------------------------------------------------------------
        // fecha de calendario
        var selectedDate: String = ""

        tietAddVendedoresFechaNacimiento.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                tietAddVendedoresFechaNacimiento.setText(selectedDate)
            }, year, month, day)
            datePickerDialog.show()
        }

        // LLENAR EL SPINNER
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listUserNotUse().body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val userNotUseList = response.data.map { it.usuario }
                    val userNotUseWithSelect = listOf("Seleccionar") + userNotUseList
                    val adapterLoad = ArrayAdapter(this@VendedoresActivity, android.R.layout.simple_spinner_item, userNotUseWithSelect)
                    adapterLoad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spAddVendedoresUsuarios.adapter = adapterLoad
                    adapterLoad.notifyDataSetChanged()
                } else {
                    showError()
                }
            }
        }
        // -----------------------------
        var selectedUserId: Int? = null

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listUserNotUse().body()
            spAddVendedoresUsuarios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedUserId = if (position > 0) {
                        tilAddVendedoresUsuarios.error = null
                        response?.data?.get(position - 1)?.id_usuarios
                    } else { null }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { selectedUserId = null }
            }
        }

        tietAddVendedoresNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddVendedoresNombre.error = if (s.isNullOrBlank()) "Este campo es requerido" else null
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddVendedoresTelefono1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddVendedoresTelefono1.error = if (s.isNullOrBlank()) "Este campo es requerido" else null
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddVendedoresCorreo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //validateEmail(s?.toString())
                if (s.isNullOrBlank()) {
                    tilAddVendedoresCorreo.error = null
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    tilAddVendedoresCorreo.error = "Formato no valido"
                } else {
                    tilAddVendedoresCorreo.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnAddVendedoresGuardar.setOnClickListener{
            val vendedoresUsuarios = spAddVendedoresUsuarios.selectedItem.toString()

            if (tietAddVendedoresNombre.text.toString().isEmpty()){
                tilAddVendedoresNombre.error = "Este campo es requerido"
                return@setOnClickListener
            } else if (tietAddVendedoresTelefono1.text.toString().isEmpty()){
                tilAddVendedoresTelefono1.error = "Este campo es requerido"
                return@setOnClickListener
            } else if (vendedoresUsuarios == "Seleccionar") {
                tilAddVendedoresUsuarios.error = "Este campo es requerido"
                return@setOnClickListener
            } else if (tietAddVendedoresCorreo.text.toString().isNotEmpty() &&
                !android.util.Patterns.EMAIL_ADDRESS.matcher(tietAddVendedoresCorreo.text.toString()).matches()){
                tilAddVendedoresCorreo.error = "Formato no válido"
                return@setOnClickListener
            } else {

                val nomVen = tietAddVendedoresNombre.text.toString()
                val te1Ven = tietAddVendedoresTelefono1.text.toString()
                val te2Ven = tietAddVendedoresTelefono2.text.toString()
                val corVen = tietAddVendedoresCorreo.text.toString()
                val dirVen = tietAddVendedoresDireccion.text.toString()
                val fecVen = selectedDate
                val actVen = 1
                val idUserVen = selectedUserId

                CoroutineScope(Dispatchers.IO).launch {
                    if (selectedUserId != null) {
                        val vendedor = Vendedores(nomVen,te1Ven,te2Ven,corVen,dirVen,fecVen,actVen,idUserVen!!) // ,selectedUserId!!
                        apiService.createVendedores(vendedor)
                        val updatedData = apiService.listVendedoresTrue().body()?.data?.results
                        runOnUiThread {
                            if (updatedData != null) { adapter.updateList(updatedData) }
                            hideKeyboard()
                            dialog.dismiss()
                        }
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
            val request0 = apiService.paginaVendedores(np)
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
                        binding?.tvVendedoresNumeroPagina?.text = "$currentPage/$totalPages"
                    } else {showError()}
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageSearch(query:String,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarVendedoresPorNombreYPage(query, np)//.body()
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
                    binding?.tvVendedoresNumeroPagina?.text = "$currentPage/$totalPages"
                } else {
                    showError()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByItem(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarVendedoresPorFiltro(query)
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
        imm.hideSoftInputFromWindow(binding?.vistaVendedoresPadre?.windowToken,0)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun performSearch(query: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listarVendedoresPorFiltro(query ?: "").body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val pagination = response.data.pagination
                    currentPage = pagination.currentPage
                    totalPages = pagination.totalPages
                    binding?.tvVendedoresNumeroPagina?.text = "$currentPage/$totalPages"
                    verdura = true
                    adapter.notifyDataSetChanged() //TODO
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrar() {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listVendedoresTrue()
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
            val request0 = apiService.listVendedoresTrue()
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvVendedoresNumeroPagina?.text = "$currentPage/$totalPages"
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
        adapter = VendedoresAdapter(datitos)
        binding?.rvVendedores?.layoutManager = LinearLayoutManager(this)
        binding?.rvVendedores?.adapter = adapter
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