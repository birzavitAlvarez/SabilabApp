package com.demo.sabilabapp.Login

data class LoginResponse(
    val status: Int,
    val statusMessage: String,
    val data: List<Data>
)