package com.demo.sabilabapp.Dashboard

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ActivityDashboardBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.roundToInt
import kotlin.math.roundToLong

import com.demo.sabilabapp.Dashboard.Dash6.Data as DataDash6

class DashboardActivity : AppCompatActivity() {

    private var binding: ActivityDashboardBinding? = null
    //rivate lateinit var binding: ActivityDashboardBinding
    private val animationDuration = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.apply {
            lineChartInit()
            barChartInit()
            Dashboard6DataInit()
//            barChartDashboard.animation.duration = animationDuration
//            barChartDashboard.animate()
        }

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

                        // Haz lo que necesites con estos valores.
                        binding?.tvPedidosIngresadosDashboard?.setText(totalPedidos.toString())
                        binding?.tvPedidosIncompletosDashboard?.setText(totalIncompletos.toString())
                        binding?.tvPedidosCompletosDashboard?.setText(totalCompletos.toString())
                        binding?.tvPedidosNoEntregadosTiempoDashboard?.setText(totalFechaNo.toString())
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