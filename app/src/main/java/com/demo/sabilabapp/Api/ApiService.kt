package com.demo.sabilabapp.Api

import com.demo.sabilabapp.Login.Data
import com.demo.sabilabapp.Login.LoginRequest
import com.demo.sabilabapp.Login.LoginResponse
import com.demo.sabilabapp.Usuarios.ListUsuariosTrue
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/usuarios/true")
    suspend fun listUsuariosTrue(): Response<ListUsuariosTrue>


}