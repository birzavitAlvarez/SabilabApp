package com.demo.sabilabapp.Dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private var binding: ActivityDashboardBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.apply {

        }

    }
}