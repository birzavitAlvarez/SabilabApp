package com.demo.sabilabapp.Api

import com.demo.sabilabapp.Compras.Compras
import com.demo.sabilabapp.Compras.ComprasResponse
import com.demo.sabilabapp.Login.LoginRequest
import com.demo.sabilabapp.Login.LoginResponse
import com.demo.sabilabapp.Usuarios.UsuariosResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    // Login
    @POST("api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    // COMPRAS
    @GET("api/compras")
    fun listarCompras(): Call<ComprasResponse>
    @GET("api/compras/buscar/{fecha}")
    fun listarPorFecha(@Path("fecha") fecha: String): Call<ComprasResponse>
    @POST("api/compras")
    fun guardar(@Body compra: Compras): Call<ComprasResponse>
    @GET("api/compras/{id}")
    fun obtenerPorId(@Path("id") id: Long): Call<ComprasResponse>

    // USUARIOS
    // Lista all por true
    @GET("api/usuarios/true")
    suspend fun listUsuariosTrue(): Response<UsuariosResponse>
    // Filtrar por nombre usuario
    @GET("api/usuarios/filtrar/{nombre}")
    suspend fun listarUsuariosPorFiltro(@Path("nombre") nombre: String): Response<UsuariosResponse>

}