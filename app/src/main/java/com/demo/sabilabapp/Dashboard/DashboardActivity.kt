package com.demo.sabilabapp.Dashboard


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import android.widget.Toast
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.sabilabapp.Api.RetrofitClient.apiService //
import com.demo.sabilabapp.Dashboard.Dash6.Data as DataDash6 //
import com.demo.sabilabapp.databinding.ActivityDashboardBinding //
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt
//ADAPTER
import com.demo.sabilabapp.Dashboard.HojaRuta.Result as DataHojaRuta //
import com.demo.sabilabapp.Adapters.HojaRutaAdapter //
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DashboardActivity : AppCompatActivity() {

    private var binding: ActivityDashboardBinding? = null
    //rivate lateinit var binding: ActivityDashboardBinding
    private val animationDuration = 1000L
    //Adapter
    private lateinit var adapter: HojaRutaAdapter
    private val datitos = mutableListOf<DataHojaRuta>()
    private var currentPage: Int = 1
    private var totalPages: Int = 1
    private var verdura: Boolean = false
    //

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val fecha_del_dia = obtenerFechaActual()
        initHojaRuta()
        Dashboard6DataInit()
        listaAlEntrar(fecha_del_dia)

        binding?.apply {
            lineChartInit()
            barChartInit()
        }

        // pagina siguiente
        binding?.ibHojaRutaDashboardNext?.setOnClickListener {
            if (currentPage < totalPages) {
                currentPage += 1
                nextPage(fecha_del_dia, currentPage)
            }
        }
        // pagina anterior
        binding?.ibHojaRutaDashboardBefore?.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                nextPage(fecha_del_dia, currentPage)
            }
        }
        //

    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun nextPage(fecha:String,np: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.hojaRutaPage(fecha,np)
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
                        binding?.tvHojaRutaDashboardNumeroPagina?.text = "$currentPage/$totalPages"
                    } else {showError()}
                } else { showError() }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrar(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.hojaRuta(query)
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val dataItems = response0?.data?.results ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataItems)
                    adapter.notifyDataSetChanged()
                    getCurrentAndTotal(query)
                    verdura = false
                } else { showError() }
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getCurrentAndTotal(query:String){
        CoroutineScope(Dispatchers.IO).launch {
            val request0 = apiService.hojaRuta(query )
            val response0 = request0.body()
            runOnUiThread {
                if (request0.isSuccessful) {
                    val pagination = response0?.data?.pagination
                    if (pagination != null) {
                        currentPage = pagination.currentPage
                        totalPages = pagination.totalPages
                        binding?.tvHojaRutaDashboardNumeroPagina?.text = "$currentPage/$totalPages"
                    }
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerFechaActual(): String {
        val fechaActual = LocalDate.now()
        val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return fechaActual.format(formato)
    }

    private fun initHojaRuta() {
        adapter = HojaRutaAdapter(datitos)
        binding?.rvHojaRutaDashboard?.layoutManager = LinearLayoutManager(this)
        binding?.rvHojaRutaDashboard?.adapter = adapter
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun lineChartInit() {
        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                apiService.dashBoard3()
            }
            if (response.isSuccessful) {
                val dashboardData = response.body()?.data
                if (!dashboardData.isNullOrEmpty()) {
                    val lineSet = dashboardData.map {
                        it.Dia.takeLast(2) to it.Resultado.toFloat()
                    }
                    binding?.lineChartDashboard?.gradientFillColors = intArrayOf(
                        Color.parseColor("#79d9e8"),
                        Color.TRANSPARENT
                    )
                    binding?.lineChartDashboard?.animation?.duration = animationDuration
                    binding?.lineChartDashboard?.animate(lineSet)

                    binding?.lineChartDashboard?.onDataPointTouchListener={ index, _, _ ->
                        Toast.makeText(this@DashboardActivity, "Cumplidos: ${lineSet.toList()[index].second.toString()}%", Toast.LENGTH_SHORT).show()
                    }

                }
            } else {
                showError()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun barChartInit() {
        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                apiService.dashBoard4()
            }
            if (response.isSuccessful) {
                val dashboardData = response.body()?.data
                if (!dashboardData.isNullOrEmpty()) {
                    val barSet = dashboardData.map {
                        it.fecha_entrega.takeLast(2) to it.total_pedidos.toFloat().roundToInt().toFloat()
                    }
                    binding?.barChartDashboard?.animation?.duration = animationDuration
                    binding?.barChartDashboard?.animate(barSet)
                    binding?.barChartDashboard?.onDataPointTouchListener={ index, _, _ ->
                        Toast.makeText(this@DashboardActivity, "Total Pedidos: ${barSet.toList()[index].second.toString()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                showError()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun Dashboard6DataInit() {
        GlobalScope.launch(Dispatchers.Main) {
            val response = withContext(Dispatchers.IO) {
                apiService.dashBoard6()
            }
            if (response.isSuccessful) {
                val dashboard6Response = response.body()
                val data: List<DataDash6>? = dashboard6Response?.data

                data?.let {
                    for (dashboardData in it) {
                        val totalPedidos = dashboardData.total_pedidos
                        val totalIncompletos = dashboardData.total_incompletos
                        val totalCompletos = dashboardData.total_completos
                        val totalFechaNo = dashboardData.total_fecha_no
                        // ASIGNANDO VALORES
                        binding?.tvPedidosIngresadosDashboard?.text = totalPedidos.toString()
                        binding?.tvPedidosIncompletosDashboard?.text = totalIncompletos.toString()
                        binding?.tvPedidosCompletosDashboard?.text = totalCompletos.toString()
                        binding?.tvPedidosNoEntregadosTiempoDashboard?.text = totalFechaNo.toString()
                    }
                }

            } else {
                showError()
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }


}