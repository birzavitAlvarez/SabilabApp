package com.demo.sabilabapp.Productos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ActivityProductoBinding
import com.demo.sabilabapp.databinding.ActivityUsuarioBinding

class ProductoActivity : AppCompatActivity() {

    private var binding: ActivityProductoBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)
    }
}