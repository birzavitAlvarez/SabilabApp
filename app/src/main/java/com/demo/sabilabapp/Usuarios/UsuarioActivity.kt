package com.demo.sabilabapp.Usuarios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ActivityUsuarioBinding

class UsuarioActivity : AppCompatActivity() {

    private var binding: ActivityUsuarioBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuarioBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //setContentView(R.layout.activity_usuario)
        // usuarios

    }
}