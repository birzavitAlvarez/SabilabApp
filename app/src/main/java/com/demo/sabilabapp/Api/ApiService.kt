package com.demo.sabilabapp.Api

import com.demo.sabilabapp.Categorias.Categoria
import com.demo.sabilabapp.Categorias.CategoriasResponse
import com.demo.sabilabapp.Compras.Compras
import com.demo.sabilabapp.Compras.ComprasResponse
import com.demo.sabilabapp.Login.LoginRequest
import com.demo.sabilabapp.Login.LoginResponse
import com.demo.sabilabapp.Roles.RolesResponse
import com.demo.sabilabapp.Usuarios.Usuario
import com.demo.sabilabapp.Usuarios.UsuariosResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    // TODO USUARIOS
    // Lista all por true
    @GET("api/usuarios/true")
    suspend fun listUsuariosTrue(): Response<UsuariosResponse>
    // Filtrar por nombre usuario
    @GET("api/usuarios/filtrar/{nombre}")
    suspend fun listarUsuariosPorFiltro(@Path("nombre") nombre: String): Response<UsuariosResponse>
    // Paginacion
    @GET("api/usuarios/true")
    suspend fun paginaUsuarios(@Query("page") pagina: Int): Response<UsuariosResponse>

    // buscar por nombre
    @GET("api/usuarios/filtrar/{nombre}")
    suspend fun listarUsuariosPorNombreYPage(@Path("nombre") nombre: String, @Query("page") pagina: Int): Response<UsuariosResponse>

    //Crear Usuarios
    @POST("api/usuarios")
    suspend fun createUser(@Body usuario: Usuario)

    //Actualizar Usuario
    @PUT("api/usuarios/{id}")
    suspend fun updateUser(@Body usuario:Usuario, @Path("id") id: Int)

    //Eliminar usuario(desactivar)
    @DELETE("api/usuarios/desactivar/{id}")
    suspend fun deleteUser(@Path("id") id: Int)
    // TODO FIN DE USUARIOS


    // TODO ROLES
    // Lista de Roles
    @GET("api/roles")
    suspend fun listRoles(): Response<RolesResponse>
    // TODO FIN ROLES


    // TODO CATEGORIAS
    // LISTAR TODO
    @GET("api/categoria")
    suspend fun listCategory(): Response<CategoriasResponse>

    //Crear Usuarios
    @POST("api/categoria")
    suspend fun createCategory(@Body categoria: Categoria)

    // Actualizar
    @PUT("api/categoria/{id}")
    suspend fun updateCategory(@Body categoria:Categoria, @Path("id") id: Int)

    // ELIMINAR
    @DELETE("api/categoria/{id}")
    suspend fun deleteCategory(@Path("id") id: Int)

    //TODO FIN CATEGORIAS

}














