package com.demo.sabilabapp.Api

import com.demo.sabilabapp.Categorias.Categoria
import com.demo.sabilabapp.Categorias.CategoriasResponse
import com.demo.sabilabapp.Clientes.Clientes
import com.demo.sabilabapp.Clientes.ClientesResponse
import com.demo.sabilabapp.Compras.Compras
import com.demo.sabilabapp.Compras.ComprasResponse
import com.demo.sabilabapp.Login.LoginRequest
import com.demo.sabilabapp.Login.LoginResponse
import com.demo.sabilabapp.Productos.Productos
import com.demo.sabilabapp.Productos.ProductosResponse
import com.demo.sabilabapp.Proveedores.Proveedor
import com.demo.sabilabapp.Proveedores.ProveedorResponse
import com.demo.sabilabapp.Roles.RolesResponse
import com.demo.sabilabapp.Usuarios.UserNotUse.UserNotUseResponse
import com.demo.sabilabapp.Usuarios.Usuario
import com.demo.sabilabapp.Usuarios.UsuariosResponse
import com.demo.sabilabapp.Vendedores.Vendedores
import com.demo.sabilabapp.Vendedores.VendedoresResponse
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
    // Paginacion de true
    @GET("api/usuarios/true")
    suspend fun paginaUsuarios(@Query("page") pagina: Int): Response<UsuariosResponse>

    // Paginacion de filtrar
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

    //TODO PROVEEDOR
    // Lista all true
    @GET("api/proveedores/true")
    suspend fun listProveedorTrue(): Response<ProveedorResponse>
    // Filtrar por razon_social
    @GET("api/proveedores/filtrar/{nombre}")
    suspend fun listarProveedorPorFiltro(@Path("nombre") nombre: String): Response<ProveedorResponse>
    // Paginacion de true
    @GET("api/proveedores/true")
    suspend fun paginaProveedor(@Query("page") pagina: Int): Response<ProveedorResponse>

    //Paginacion de filtrar
    @GET("api/proveedores/filtrar/{nombre}")
    suspend fun listarProveedorPorNombreYPage(@Path("nombre") nombre: String, @Query("page") pagina: Int): Response<ProveedorResponse>

    //Crear Proveedor
    @POST("api/proveedores")
    suspend fun createProveedor(@Body proveedor: Proveedor)

    //Actualizar Proveedor
    @PUT("api/proveedores/{id}")
    suspend fun updateProveedor(@Body proveedor: Proveedor, @Path("id") id: Int)

    //Eliminar Proveedor(desactivar)
    @DELETE("api/proveedores/desactivar/{id}")
    suspend fun deleteProveedor(@Path("id") id: Int)
    //TODO FIN PROVEEDOR


    //TODO PRODUCTOS
    // Lista all true
    @GET("api/productos/true")
    suspend fun listProductosTrue(): Response<ProductosResponse>

    // Paginacion de true
    @GET("api/productos/true")
    suspend fun paginaProductos(@Query("page") pagina: Int): Response<ProductosResponse>

    // Filtrar por nombre producto
    @GET("api/productos/filtrar/{nombre}")
    suspend fun listarProductosPorFiltro(@Path("nombre") nombre: String): Response<ProductosResponse>

    //Paginacion de filtrar
    @GET("api/productos/filtrar/{nombre}")
    suspend fun listarProductosPorNombreYPage(@Path("nombre") nombre: String, @Query("page") pagina: Int): Response<ProductosResponse>

    //Crear Producto
    @POST("api/productos")
    suspend fun createProductos(@Body productos: Productos)

    //Actualizar Producto
    @PUT("api/productos/{id}")
    suspend fun updateProductos(@Body productos: Productos, @Path("id") id: Int)

    //Eliminar Proveedor(desactivar)
    @DELETE("api/productos/desactivar/{id}")
    suspend fun deleteProductos(@Path("id") id: Int)
    //TODO FIN PRODUCTOS



    // TODO CLIENTES
    // Lista all true por id_vendedor  lista todos los clientes pertenicientes al id_vendedor
    @GET("api/clientes/vendedor/true/{id}")
    suspend fun listClientesTrue(@Path("id") id: Int): Response<ClientesResponse>

    // Paginacion de all true por id_vendedor
    @GET("api/clientes/vendedor/true/{id}")
    suspend fun paginaClientes(@Path("id") id: Int, @Query("page") pagina: Int): Response<ClientesResponse>

    // Filtrar por NOMBRE COMERCIAL + id_vendedor
    @GET("api/clientes/filtrar2/{nombre}/{id}")
    suspend fun listarClientesPorFiltro(@Path("nombre") nombre: String, @Path("id") id: Int ): Response<ClientesResponse>

    //Paginacion de filtrar por NOMBRE COMERCIAL + id_vendedor
    @GET("api/clientes/filtrar2/{nombre}/{id}")
    suspend fun listarClientesPorNombreYPage(@Path("nombre") nombre: String, @Path("id") id: Int, @Query("page") pagina: Int): Response<ClientesResponse>

    //Crear clientes
    @POST("api/clientes")
    suspend fun createClientes(@Body clientes: Clientes)

    //Actualizar clientes
    @PUT("api/clientes/{id}")
    suspend fun updateClientes(@Body clientes: Clientes, @Path("id") id: Int)

    //Eliminar clientes(desactivar)
    @DELETE("api/clientes/desactivar/{id}")
    suspend fun deleteClientes(@Path("id") id: Int)
    //TODO FIN CLIENTES

    //TODO VENDEDORES
    // Lista all true
    @GET("api/vendedor/true")
    suspend fun listVendedoresTrue(): Response<VendedoresResponse>

    // Paginacion de true
    @GET("api/vendedor/true")
    suspend fun paginaVendedores(@Query("page") pagina: Int): Response<VendedoresResponse>

    // Filtrar por nombres vendedor
    @GET("api/vendedor/filtrar/{nombres}")
    suspend fun listarVendedoresPorFiltro(@Path("nombres") nombres: String): Response<VendedoresResponse>

    //Paginacion de filtrar por nombres vendedor
    @GET("api/vendedor/filtrar/{nombres}")
    suspend fun listarVendedoresPorNombreYPage(@Path("nombres") nombres: String, @Query("page") pagina: Int): Response<VendedoresResponse>

    //Crear Vendedor
    @POST("api/vendedor")
    suspend fun createVendedores(@Body vendedores: Vendedores)

    //Actualizar Vendedor
    @PUT("api/vendedor/{id}")
    suspend fun updateVendedores(@Body vendedores: Vendedores, @Path("id") id: Int)

    //Eliminar Vendedor(desactivar)
    @DELETE("api/vendedor/desactivar/{id}")
    suspend fun deleteVendedores(@Path("id") id: Int)
    //TODO FIN PRODUCTOS

    // TODO USUARIOS SIN USAR
    @GET("api/usuarios/usu")
    suspend fun listUserNotUse(): Response<UserNotUseResponse>
    // TODO FIN DE USUARIOS SIN USAR

}














