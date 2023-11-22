package com.demo.sabilabapp.Reportes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
//
import android.os.Build
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.sabilabapp.Adapters.ReporteAdapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.Reportes.Result
import com.demo.sabilabapp.databinding.ActivityReporteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import android.util.Base64
import androidx.core.content.FileProvider
import de.hdodenhof.circleimageview.BuildConfig

///
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.FileOutputStream

class ReporteActivity : AppCompatActivity() {

    private var binding: ActivityReporteBinding? = null

    private lateinit var adapter: ReporteAdapter
    private val datitos = mutableListOf<Result>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1
    private var verdura: Boolean = false

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReporteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //
        var selectedDate: String = ""
//        var margen_ganancia:BigDecimal = BigDecimal.ZERO
        var margen_ganancia:Double = 0.00
        //
        val fecha_del_dia = obtenerFechaActual()
        //
        initRecyclerView()
        listaAlEntrar(fecha_del_dia, margen_ganancia)
        //
        binding?.tietReporteFecha?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                binding?.tietReporteFecha?.setText(selectedDate)
            }, year, month, day)
            datePickerDialog.show()
        }
        binding?.tietReporteMargenGanancia?.setText("0.0")
        binding?.tietReporteFecha?.setText(obtenerFechaActual())

        //
        binding?.ibReporteRefresh?.setOnClickListener {
            listaAlEntrar(fecha_del_dia, margen_ganancia)
            binding?.tietReporteFecha?.setText(obtenerFechaActual())
            binding?.tietReporteMargenGanancia?.setText("0.0")
            selectedDate = ""
        }
        //
        binding?.tietReporteMargenGanancia?.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = binding?.tietReporteMargenGanancia?.text.toString()
                if (input.length > 0) {
                    val firstChar = input[0]
                    if (firstChar != '0') {
                        binding?.tilReporteMargenGanancia?.error = "Primer dígito debe ser 0."
                    } else {
                        val decimals = input.split(".").getOrNull(1)
                        if (decimals != null && decimals.length > 2) {
                            binding?.tilReporteMargenGanancia?.error = "Maximo 2 decimales"
                        } else {
                            binding?.tilReporteMargenGanancia?.error = null
                        }
                    }

                } else {
                    binding?.tilReporteMargenGanancia?.error = null
                }

            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding?.btnReporteBuscar?.setOnClickListener {

            if (binding?.tietReporteMargenGanancia?.text.toString().isEmpty()){
                binding?.tilReporteMargenGanancia?.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if (binding?.tilReporteMargenGanancia?.error == "Primer dígito debe ser 0." ){
                return@setOnClickListener
            } else if (binding?.tilReporteMargenGanancia?.error == "Maximo 2 decimales" ){
                return@setOnClickListener
            }

            val ganancia = binding?.tietReporteMargenGanancia?.text.toString().toDouble()
            val fechaDia = binding?.tietReporteFecha?.text.toString()

            searchByItem(fechaDia,ganancia)
        }

        // pagina siguiente
        binding?.ibReporteNext?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                if (verdura) {
                    val ganancia = binding?.tietReporteMargenGanancia?.text.toString().toDouble()
                    val fechaDia = binding?.tietReporteFecha?.text.toString()
                    nextPageSearch(fechaDia,ganancia,currentPage)
                } else {
                    nextPage(fecha_del_dia,margen_ganancia , currentPage)
                }
            }
        }
        // pagina anterior
        binding?.ibReporteBefore?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                if (verdura) {
                    val ganancia = binding?.tietReporteMargenGanancia?.text.toString().toDouble()
                    val fechaDia = binding?.tietReporteFecha?.text.toString()
                    nextPageSearch(fechaDia,ganancia,currentPage)
                } else {
                    nextPage(fecha_del_dia,margen_ganancia , currentPage)
                }
            }
        }
        //
        // Generar PDF
        binding?.btnReporteGenerarPdf?.setOnClickListener {
            val fechaDia = binding?.tietReporteFecha?.text.toString()
            val ganancia = binding?.tietReporteMargenGanancia?.text.toString().toDouble()
            generarPDF(fechaDia,ganancia)
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun generarPDF(fecha: String, descuento: Double) {
        val url = "https://sabilab11.onrender.com/api/pedido/listacompra/pdf/$fecha/$descuento"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun positiveMessage() {
        Toast.makeText(this, "PDF GENERADO", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPage(fechaDelDia: String, margenGanancia: Double, np: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarReportePage(fechaDelDia,margenGanancia,np)
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
                        binding?.tvReporteNumeroPagina?.text = "$currentPage/$totalPages"
                    } else {showError()}
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPageSearch(fechaDia: String, ganancia: Double, np: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarReportePage(fechaDia, ganancia, np)//.body()
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
                    binding?.tvReporteNumeroPagina?.text = "$currentPage/$totalPages"
                } else {
                    showError()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByItem(fechaDia: String, margenGanancia: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarReporte(fechaDia, margenGanancia)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    performSearch(fechaDia, margenGanancia)
                } else { showError() }
                hideKeyboard()
                getCurrentAndTotal(fechaDia, margenGanancia)
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun performSearch(fechaDia: String, margenGanancia: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.listarReporte(fechaDia, margenGanancia).body()
            runOnUiThread {
                if (response != null && response.status == 200) {
                    val pagination = response.data.pagination
                    currentPage = pagination.currentPage
                    totalPages = pagination.totalPages
                    binding?.tvReporteNumeroPagina?.text = "$currentPage/$totalPages"
                    verdura = true
                    adapter.notifyDataSetChanged() //TODO
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrar(fecha:String, margen_gancia: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarReporte(fecha, margen_gancia)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    getCurrentAndTotal(fecha, margen_gancia)
                    verdura = false
                } else { showError() }
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getCurrentAndTotal(fecha:String, margen_gancia: Double){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.listarReporte(fecha,margen_gancia)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvReporteNumeroPagina?.text = "$currentPage/$totalPages"
                    }
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.vistaReportePadre?.windowToken,0)
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    private fun initRecyclerView() {
        adapter = ReporteAdapter(datitos)
        binding?.rvReporte?.layoutManager = LinearLayoutManager(this)
        binding?.rvReporte?.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerFechaActual(): String {
        val fechaActual = LocalDate.now()
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return fechaActual.format(formato)
    }


}