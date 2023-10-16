package com.demo.sabilabapp.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //setContentView(R.layout.activity_login)
    }
}