# SabilabApp

![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/bb58392c-f2b2-4d9e-a0e8-d075d28393c2)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/f1c9f51e-cc91-4966-a712-20643878661d)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/01fea55e-5b32-4af0-832a-f64b572ebe2a)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/2f1d1eec-2123-467f-af6a-0abfbd1e4f83)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/5c0efd1b-1fba-4979-bbe6-a2f171ef7825)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/ca026240-05be-433b-b5a2-16adabc1215f)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/88ff61b9-009d-4312-a7d3-ff15838e226d)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/c66ce916-370a-412a-8bdf-c9ddcef18dcf)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/c4903ea1-b7e7-4693-9768-f66b911fbe8b)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/055b5cdd-b89d-4810-80a0-e8c80e553f9f)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/493df254-acee-4750-94a8-d036ea227acf)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/59db93f0-7681-4894-b742-90f1ae3a5068)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/aee44e70-a99d-41e7-8534-28f8260fa3a8)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/2b5998f4-02f4-45bb-9f77-bf82575760cc)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/928fe5e2-6987-4be2-81d8-cce253553f62)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/6562c17b-b2ae-4a9f-b069-24d715d07d6c)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/6ef0e2cc-ecba-49e9-8b7f-ee5a5f017d0c)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/654c8fa6-aeb1-4708-811c-eae1a6b8f611)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/2af00f12-2aea-42e6-b073-a0bb5ef51d21)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/fd1f5958-122e-4b5e-8cf3-7df691c42094)
![image](https://github.com/birzavitAlvarez/SabilabApp/assets/67138065/e5435d6c-5a01-4920-93ba-6659e0c1dcc0)




# Ultima version de android y SDK 34

# sabilab_v1

#BASE URL
```
https://sabilab11.onrender.com/
```
# ROLES
Listar Roles metodo GET
```
https://sabilab11.onrender.com/api/roles
```
Listar Roles por id metodo GET
```
https://sabilab11.onrender.com/api/roles/1
```
Crear Roles metodo POST / Actualizar Roles PUT  +/id
```
https://sabilab11.onrender.com/api/roles
```
```json
{
    "rol": "Prueba"
}
```
Eliminar Roles por id metodo DELETE
```
https://sabilab11.onrender.com/api/roles/1
```

# USUARIOS
Listar Todos los usuarios metodo GET
```
https://sabilab11.onrender.com/api/usuarios
```
Listar los usuarios activos true metodo GET
```
https://sabilab11.onrender.com/api/usuarios/true
```
Listar los usuarios que no estan siendo usados por vendedor metodo GET
```
https://sabilab11.onrender.com/api/usuarios/usu
```
Filtro que trae usuarios por parametro y lo lista metodo GET
```
https://sabilab11.onrender.com/api/usuarios/filtrar/parametro
```
Listar usuarios por id metodo GET
```
https://sabilab11.onrender.com/api/usuarios/2
```
Registrar usuarios Metodo POST / Actualizar usuarios metodo PUT  +/id
```
https://sabilab11.onrender.com/api/usuarios
```
```json
{
    "usuario": "prueba10",
    "password": "prueba10",
    "activo": 1,
    "id_roles":2

}
```
Eliminar usuarios por id metodo DELETE / este cambia el activo de true a false
```
https://sabilab11.onrender.com/api/usuarios/desactivar/2
```
Iniciar Sesion Usuarios metodo Post
```
https://sabilab11.onrender.com/api/usuarios/login
```
Cuerpo del login
```json
{
  "usuario": "birzavit",
  "password": "birzavit"
}
```

# VENDEDOR
Listar Todos los vendedores metodo GET
```
https://sabilab11.onrender.com/api/vendedor
```
Listar los vendedores activos true metodo GET
```
https://sabilab11.onrender.com/api/vendedor/true
```
Filtro que trae vendedores por parametro y lo lista metodo GET
```
https://sabilab11.onrender.com/api/vendedor/filtrar/parametro
```
Listar vendedores por id metodo GET
```
https://sabilab11.onrender.com/api/vendedor/2
```
Registrar vendedores Metodo POST / Actualizar usuarios metodo PUT  +/id
```
https://sabilab11.onrender.com/api/vendedor
```
```json
    {
        "nombres": "prueba pruebit222222a",
        "telefono1": "123123333",
        "telefono2": "123123111",
        "correo": "prueba@gmail.com",
        "direccion": "Nueva Prueba",
        "fecha_nacimiento": "1994-12-04",
        "activo": 1,
        "id_usuarios": 16
    }
```
Eliminar vendedores por id metodo DELETE / este cambia el activo de true a false
```
https://sabilab11.onrender.com/api/vendedor/desactivar/2
```

# CLIENTES

Listar los clientes Activos true por id de vendedor metodo GET
```
https://sabilab11.onrender.com/api/clientes/vendedor/true/4
```

Listar los clientes Activos true por id de vendedor metodo GET PAGINACION
```
https://sabilab11.onrender.com/api/clientes/vendedor/true/4?page=2
```

Filtro de busqueda de los clientes por parametro {nombre} + {id_vendedor} metodo GET
```
https://sabilab11.onrender.com/api/clientes/filtrar2/FARMACIA CRISTO REY E.I.R.L./4
```

Filtro de busqueda de los clientes por parametro {nombre} + {id_vendedor} metodo GET PAGINACION
```
https://sabilab11.onrender.com/api/clientes/filtrar2/FARMACIA CRISTO REY E.I.R.L./4?page=4
```

Registrar clientes Metodo POST / Actualizar clientes metodo PUT  +/id
```
https://sabilab11.onrender.com/api/clientes
```
```json
    {
        "ruc": "12312312306",
        "razon_social": "emmpresa6 s.a.c.",
        "nombre_comercial": "empresa6",
        "contacto": "jose66666",
        "direccion1": "direccion x6",
        "direccion2": "",
        "telefono1": "123123106",
        "telefono2": "",
        "telefono_empresa": "",
        "provincia": "LIMA",
        "distrito": "La Molina6",
        "activo": 1,
        "id_vendedor": 2
    }
```
Eliminar clientes por id metodo DELETE / este cambia el activo de true a false
```
https://sabilab11.onrender.com/api/clientes/desactivar/5
```

# COMPROBANTE
Listar TODOS los comprobantes metodo GET
```
https://sabilab11.onrender.com/api/comprobante
```
Listar comprobantes por id metodo GET
```
https://sabilab11.onrender.com/api/comprobante/2
```
Agregar comprobantes metodo POST / Actualizar comprobante metodo PUT  +/id
```
https://sabilab11.onrender.com/api/comprobante
```
```json
    {
        "tipo_comprobante": "Factura"
    }
```
Eliminar comprobantes metodo DELETE
```
https://sabilab11.onrender.com/api/comprobante/4
```

# PROVEEDORES
Listar TODOS los proveedores metodo GET
```
https://sabilab11.onrender.com/api/proveedores
```
Listar proveedores activos true metodo GET
```
https://sabilab11.onrender.com/api/proveedores/true
```
Filtro que trae proveedores por parametro y lo lista metodo GET
```
https://sabilab11.onrender.com/api/proveedores/filtrar/{parametro}
```
Listar proveedores por id metodo GET
```
https://sabilab11.onrender.com/api/proveedores/3
```
Agregar proveedores metodo POST / Actualizar proveedores metodo PUT  +/id
```
https://sabilab11.onrender.com/api/proveedores
```
```json
    {
        "ruc": "12312312311",
        "razon_social": "asdasda s.a.c",
        "nonbre_contacto": "Felipe",
        "telefono": "123123123",
        "correo": "Felipe222@gmail.com",
        "activo": 1
    }
```
Eliminar proveedores por id metodo DELETE / este cambia el activo de true a false
```
https://sabilab11.onrender.com/api/proveedores/desactivar/3
```

# CATEGORIA
Listar TODAS las categorias metodo GET
```
https://sabilab11.onrender.com/api/categoria
```
Listar categorias por id metodo GET
```
https://sabilab11.onrender.com/api/categoria/3
```
Agregar categorias metodo POST / Actualizar categorias metodo PUT +/id
```
https://sabilab11.onrender.com/api/categoria
```
```json
{
    "tipo": "gatoss22222",
    "descripcion": "MICHISSSSS2222222"
}
```
Eliminar categorias por id metodo DELETE
```
https://sabilab11.onrender.com/api/categoria/3
```

# PRODUCTOS
Listar TODOS los productos metodo GET
```
https://sabilab11.onrender.com/api/productos
```
Listar los productos ACTIVOS TRUE metodo GET
```
https://sabilab11.onrender.com/api/productos/true
```
Filtro que trae productos por parametro y lo lista metodo GET
```
https://sabilab11.onrender.com/api/productos/filtrar/{parametro}
```
Listar productos por id metodo GET
```
https://sabilab11.onrender.com/api/productos/2
```
Registrar productos Metodo POST / Actualizar pedidos metodo PUT  +/id
```
https://sabilab11.onrender.com/api/productos
```
```json
    {
        "nombre": "3-GEL SUSPENSIÓN ORAL X 20 SOBRES1111",
        "laboratorio": "DRGP",
        "precio": 27.00,
        "lote": "",
        "fecha_caducidad": "2024-10-19",
        "activo": 1,
        "id_categoria": 1
    }
```
Eliminar productos por id metodo DELETE / este cambia el activo de true a false
```
https://sabilab11.onrender.com/api/productos/desactivar/3
```


# PEDIDO
Listar TODOS los pedidos metodo GET
```
https://sabilab11.onrender.com/api/pedido
```
Listar los pedidos activos true metodo GET
```
https://sabilab11.onrender.com/api/pedido/true
```
Filtro que trae pedidos por query param fecha(fecha_pedido) y query param nombre(nombre_comercial)
```
https://sabilab11.onrender.com/api/pedido/filtrar?fecha=2023-09-22&nombre=MYM
```
Listar pedidos por id metodo GET
```
https://sabilab11.onrender.com/api/pedido/2
```
Registrar pedidos Metodo POST / Actualizar pedidos metodo PUT  +/id
```
https://sabilab11.onrender.com/api/pedido
```
```json
{
        "direccion": "Av. La meca 115",
        "distrito": "Surco2",
        "fecha_entrega": "2023-09-11",
        "fecha_llegada": null,
        "total_pedido": 215.00,
        "activo": 1,
        "id_comprobante":2,
        "id_vendedor":4,
        "id_cliente":99
}
```
Actualizar pedidos fecha_llegada metodo PUT  +/id
```
https://sabilab11.onrender.com/api/pedido/1
```
```json
{
    "fecha_llegada": "2023-09-03"
}
```

Actualizar pedidos direccion, distrito, fecha_entrega, id_comprobante metodo PUT  +/id
```
https://sabilab11.onrender.com/api/pedido/up/54
```
```json
{
    "direccion": "Av. La meca 666",
    "distrito": "Surco666",
    "fecha_entrega": "2023-09-25",
    "id_comprobante": 2
}
```
Eliminar pedido por id metodo DELETE / este cambia el activo de true a false
```
https://sabilab11.onrender.com/api/pedido/desactivar/3
```


# DETALLE PEDIDO
Listar TODOS los detallepedido metodo GET
```
https://sabilab11.onrender.com/api/detallepedido
```
Listar detallepedido por id metodo GET
```
https://sabilab11.onrender.com/api/detallepedido/2
```
Listar todos los detallepedido por id de pedido metodo GET
```
https://sabilab11.onrender.com/api/detallepedido/pedido/1
```
Registrar detallepedido Metodo POST
```
https://sabilab11.onrender.com/api/detallepedido
```
```json
    {
        "cantidad_objetiva": 20.00,
        "cantidad_obtenida": 15.00,
        "total_detalle": 335.5,
        "id_pedido": 1,
        "id_productos": 2
     }
```
Actualizar detallepedido Metodo PUT solo actualizar cantidad_obtenida con el id
```
https://sabilab11.onrender.com/api/detallepedido/+id
```
```json
{
    "cantidad_obtenida": 20.00
}
```
Eliminar detallepedido por id metodo DELETE
```
https://sabilab11.onrender.com/api/detallepedido/3
```


# Compras
Listar TODAS las compras metodo GET
```
https://sabilab11.onrender.com/api/compras
```
Listar compras por parametro fecha metodo GET
```
https://sabilab11.onrender.com/api/compras/buscar/2023-09-09
```
Listar compras por id metodo GET
```
https://sabilab11.onrender.com/api/compras/2
```
Registrar compras Metodo POST / Actualizar compras metodo PUT  +/id
```
https://sabilab11.onrender.com/api/compras
```
```json
    {
        "cantidad": 15,
		"fecha_compras": "2023-05-17",
        "id_proveedores": 1,
        "id_productos": 1
    }
```
Eliminar compras por id metodo DELETE
```
https://sabilab11.onrender.com/api/compras/3
```










# FILTROS PARA EL DASHBOARD
Hoja de Ruta metodo GET / el formato es java.sql.Date o YYYY-MM-DD
```
https://sabilab11.onrender.com/api/pedido/ruta/2023-09-01
```

Lista de compra GET / el formato es java.sql.Date o YYYY-MM-DD , y luego un segundo parametro que sera el pocentaje del descuento que se ingresara
```
https://sabilab11.onrender.com/api/pedido/listacompra/2023-09-01/0.012
```

Generar PDF Lista de compra GET / el formato es java.sql.Date o YYYY-MM-DD , y luego un segundo parametro que sera el pocentaje del descuento que se ingresara
```
https://sabilab11.onrender.com/api/pedido/listacompra/pdf/2023-09-01/0.012
```


Lista de pedidos entregados completos, incompletos metodo GET 
```
https://sabilab11.onrender.com/api/pedido/dashboard3
```

Lista de pedidos entregados a tiempo o no, metodo GET
```
https://sabilab11.onrender.com/api/pedido/dashboard4
```

Lista todos los pedidos de la tabla pedido por id_vendedor + fecha, metodo GET
```
https://sabilab11.onrender.com/api/pedido/vendedor/4/2023-09-22
```

Dashboard 6 trae los resultados de total_pedido, incompletos, completos, fecha_no(no entregado a tiempo)  metodo GET
```
https://sabilab11.onrender.com/api/pedido/dashboard6
```


# ULTIMOS ENDPOINT

VENDEDOR = getPedidosByIdVendedorAndNombreComercial metodo GET te trae pedido por, id_vendedor, fecha, y nombre_comercial
```
https://sabilab11.onrender.com/api/pedido/vendedor/4?fecha=2023-09-22&nomcome=
```

ADMINISTRADOR = findByFechaAndNombreComercial metodo GET te trae pedido por, fecha y nombre_comercial
```
https://sabilab11.onrender.com/api/pedido/filtrar?fecha=2023-09-22&nombre=
```

Actualizar por 4 campos el pedido metodo PUT
```
https://sabilab11.onrender.com/api/pedido/up/5
```
```json
{
    "direccion": "Av. La meca 666",
    "distrito": "Surco666",
    "fecha_entrega": "2023-09-25",
    "id_comprobante": 2
}
```
