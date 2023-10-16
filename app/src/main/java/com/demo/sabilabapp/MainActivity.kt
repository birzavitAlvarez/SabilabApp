package com.demo.sabilabapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //setContentView(R.layout.activity_main)

    }
}