package com.demo.sabilabapp.Api

import com.demo.sabilabapp.Aprovisionamiento.AprovisionamientoGetAll
import com.demo.sabilabapp.Aprovisionamiento.AprovisionamientoPost
import com.demo.sabilabapp.Categorias.Categoria
import com.demo.sabilabapp.Categorias.CategoriasResponse
import com.demo.sabilabapp.Clientes.Clientes
import com.demo.sabilabapp.Clientes.ClientesResponse

import com.demo.sabilabapp.Comprobante.ComprobanteResponse
import com.demo.sabilabapp.Dashboard.Dash3.Dashboard3Response
import com.demo.sabilabapp.Dashboard.Dash4.Dashboard4Response
import com.demo.sabilabapp.Dashboard.Dash6.Dashboard6Response
import com.demo.sabilabapp.Dashboard.HojaRuta.HojaRutaResponse
import com.demo.sabilabapp.DetallePedido.DetallePedidoCantidad
import com.demo.sabilabapp.DetallePedido.DetallePedidoPost
import com.demo.sabilabapp.DetallePedido.DetallePedidoResponse
import com.demo.sabilabapp.Login.LoginRequest
import com.demo.sabilabapp.Login.LoginResponse
import com.demo.sabilabapp.Pedidos.Pedidos
import com.demo.sabilabapp.Pedidos.PedidosResponse
import com.demo.sabilabapp.Pedidos.PedidosUpdate1
import com.demo.sabilabapp.Pedidos.PedidosUpdate4
import com.demo.sabilabapp.Pedidos.RespuestaPedido
import com.demo.sabilabapp.Productos.Productos
import com.demo.sabilabapp.Productos.ProductosResponse
import com.demo.sabilabapp.Proveedores.Proveedor
import com.demo.sabilabapp.Proveedores.ProveedorResponse
import com.demo.sabilabapp.Reportes.ReporteResponse
import com.demo.sabilabapp.Roles.RolesResponse
import com.demo.sabilabapp.Usuarios.UserNotUse.UserNotUseResponse
import com.demo.sabilabapp.Usuarios.Usuario
import com.demo.sabilabapp.Usuarios.UsuariosResponse
import com.demo.sabilabapp.Vendedores.Vendedores
import com.demo.sabilabapp.Vendedores.VendedoresResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import java.math.BigDecimal

interface ApiService {
    // Login
    @POST("api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


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

    // TODO COMPROBANTE
    // Lista de Comprobantes
    @GET("api/comprobante")
    suspend fun listComprobante(): Response<ComprobanteResponse>
    // TODO FIN COMPROBANTE



    // TODO PEDIDOS
    // lista por id vendedor y fecha al entrar
    @GET("api/pedido/vendedor/{id}")
    suspend fun listPedidosVendedorTrue(@Path("id") id: Int, @Query("fecha") fecha: String): Response<PedidosResponse>

    // paginacion de lista por id vendedor y fecha al entrar
    @GET("api/pedido/vendedor/{id}")
    suspend fun paginaPedidosVendedor(@Path("id") id: Int, @Query("fecha") fecha: String, @Query("page") pagina: Int): Response<PedidosResponse>

    // Filtrar por fecha y nombre
    @GET("api/pedido/vendedor/{id}")
    suspend fun listarPedidosPorFiltro(@Path("id") id: Int, @Query("fecha") fecha: String, @Query("nomcome") nomcome: String): Response<PedidosResponse>

    //Paginacion de filtrar por fecha y nombre
    @GET("api/pedido/vendedor/{id}")
    suspend fun listarPedidosPorNombreYPage(@Path("id") id: Int, @Query("fecha") fecha: String, @Query("nomcome") nomcome: String, @Query("page") pagina: Int): Response<PedidosResponse>

    //Crear Pedido
    @POST("api/pedido")
    suspend fun createPedido(@Body pedido: Pedidos): Response<RespuestaPedido>

    //Actualizar Pedido en 4 campos
    @PUT("api/pedido/up/{id}")
    suspend fun updatePedido4(@Body pedidosUpdate4: PedidosUpdate4, @Path("id") id: Int)

    //Actualizar Pedido en 4 campos
    @PUT("api/pedido/{id}")
    suspend fun updatePedido1(@Body pedidosUpdate1: PedidosUpdate1, @Path("id") id: Int)

    //Eliminar Vendedor(desactivar)
    @DELETE("api/pedido/desactivar/{id}")
    suspend fun deletePedido(@Path("id") id: Int)

    // TODO ADMIN LISTA al entrar
    @GET("api/pedido/filtrar")
    suspend fun listPedidosAdminTrue(@Query("fecha") fecha: String): Response<PedidosResponse>
    // paginacion de lista al entrar admin
    @GET("api/pedido/filtrar")
    suspend fun paginaPedidosAdminTrue(@Query("fecha") fecha: String, @Query("page") pagina: Int): Response<PedidosResponse>

    // Filtrar por fecha y nombre query
    @GET("api/pedido/filtrar")
    suspend fun listarAdminPedidosPorFiltro(@Query("fecha") fecha: String, @Query("nombre") nombre: String): Response<PedidosResponse>

    //Paginacion de filtrar por fecha y nombre query
    @GET("api/pedido/filtrar")
    suspend fun listarAdminPedidosPorNombreYPage(@Query("fecha") fecha: String, @Query("nombre") nombre: String, @Query("page") pagina: Int): Response<PedidosResponse>

    //


    // TODO FIN PEDIDOS

    // TODO DETALLEPEDIDO
    //LISTAR TODOS LOS DETALLES PEDIDO POR ID_PEDIDO
    @GET("api/detallepedido/pedido/{id}")
    suspend fun listDetallePedidoByIdPedido(@Path("id") id: Int): Response<DetallePedidoResponse>

    // CREAR DETALLE PEDIDO
    @POST("api/detallepedido")
    suspend fun createDetallePedido(@Body detallePedidoPost: DetallePedidoPost)


    //Actualizar Cantidad_obtenida
    @PUT("api/detallepedido/{id}")
    suspend fun updateDetallePedido(@Body detallePedidoCantidad: DetallePedidoCantidad, @Path("id") id: Int)

    // TODO FIN DETALLEPEDIDO

    // TODO Aprovisionamiento (compras)
    // GET ALL
    @GET("api/compras")
    suspend fun listAprovisionamiento(): Response<AprovisionamientoGetAll>

    //Pagina de todos
    @GET("api/compras")
    suspend fun paginaAprovisionamiento(@Query("page") pagina: Int): Response<AprovisionamientoGetAll>

    // GET ALL BY FECHA
    @GET("api/compras/buscar/{fecha}")
    suspend fun listAprovisionamientoByFecha(@Path("fecha") fecha: String): Response<AprovisionamientoGetAll>

    //Pagina de buscar por fecha
    @GET("api/compras/buscar/{fecha}")
    suspend fun listarAprovisionamientoPorFechaYPage(@Path("fecha") fecha: String, @Query("page") pagina: Int): Response<AprovisionamientoGetAll>

    // POST
    @POST("api/compras")
    suspend fun createAprovisionamiento(@Body aprovisionamiento: AprovisionamientoPost)

    // PUT
    @PUT("api/compras/{id}")
    suspend fun updateAprovisionamiento(@Body aprovisionamiento: AprovisionamientoPost, @Path("id") id: Int)

    //Eliminar COMPRAS
    @DELETE("api/compras/{id}")
    suspend fun deleteAprovisionamiento(@Path("id") id: Int)
    // TODO FIN DE APROVISIONAMIENTO

    //TODO REPORTE
    // Lista por path fecha y descuento, fecha del dia y descuento 0 al entrar
    @GET("api/pedido/listacompra/{fecha}/{descuento}")
    suspend fun listarReporte(@Path("fecha") fecha: String, @Path("descuento") descuento: Double): Response<ReporteResponse>

    //Paginacion de Lista por path fecha y descuento, fecha del dia y descuento 0 al entrar
    @GET("api/pedido/listacompra/{fecha}/{descuento}")
    suspend fun listarReportePage(@Path("fecha") fecha: String, @Path("descuento") descuento: Double, @Query("page") pagina: Int): Response<ReporteResponse>

    //GENERAR PDF
    @GET("api/pedido/listacompra/pdf/{fecha}/{descuento}")
    @Streaming
    suspend fun descargarReportePdfDirecto(@Path("fecha") fecha: String, @Path("descuento") descuento: Double): Response<ResponseBody>
    //TODO FIN REPORTE

    //TODO DASHBOARD

    // DASHBOARD3
    @GET("api/pedido/dashboard3")
    suspend fun dashBoard3(): Response<Dashboard3Response>

    // DASHBOARD4
    @GET("api/pedido/dashboard4")
    suspend fun dashBoard4(): Response<Dashboard4Response>


    // DASHBOARD6
    @GET("api/pedido/dashboard6")
    suspend fun dashBoard6(): Response<Dashboard6Response>

    // HOJA RUTA
    @GET("api/pedido/ruta/{fecha}")
    suspend fun hojaRuta(@Path("fecha") fecha: String): Response<HojaRutaResponse>

    // HOJA RUTA PAGINACION
    @GET("api/pedido/ruta/{fecha}")
    suspend fun hojaRutaPage(@Path("fecha") fecha: String, @Query("page") pagina: Int): Response<HojaRutaResponse>
    //TODO FIN DASHBOARD

}













