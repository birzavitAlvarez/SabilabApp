package com.demo.sabilabapp.Api

import com.demo.sabilabapp.Login.Data
import com.demo.sabilabapp.Login.LoginRequest
import com.demo.sabilabapp.Login.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

}