package com.demo.sabilabapp.Api

import com.demo.sabilabapp.Login.Data
import retrofit2.Call
import retrofit2.http.POST

interface ConsumirApi {

    @POST("api/usuarios/login")
    fun login(): Call<Data>

}