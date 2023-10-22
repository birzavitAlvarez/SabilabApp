package com.demo.sabilabapp.Compras;



public class Compras {
    private Long id_compras;
    private int cantidad;
    private String fecha_compras;
    private Long id_proveedores;
    private String razon_social;
    private Long id_productos;
    private String nombre;

    public Compras() {
    }
    public Compras(int cantidad,String fecha_compras,Long id_proveedores,
                   Long id_productos) {
        this.cantidad=cantidad;
        this.fecha_compras=fecha_compras;
        this.id_proveedores=id_proveedores;
        this.id_productos=id_productos;
    }
    public Compras(Long id_compras,int cantidad,String fecha_compras,Long id_proveedores,String razon_social,
                   Long id_productos,String nombre) {
        this.id_compras = id_compras;
        this.cantidad=cantidad;
        this.fecha_compras=fecha_compras;
        this.id_proveedores=id_proveedores;
        this.razon_social=razon_social;
        this.id_productos=id_productos;
        this.nombre=nombre;
    }

    public Long getId_compras() {
        return id_compras;
    }

    public void setId_compras(Long id_compras) {
        this.id_compras = id_compras;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha_compras() {
        return fecha_compras;
    }

    public void setFecha_compras(String fecha_compras) {
        this.fecha_compras = fecha_compras;
    }

    public Long getId_proveedores() {
        return id_proveedores;
    }

    public void setId_proveedores(Long id_proveedores) {
        this.id_proveedores = id_proveedores;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public Long getId_productos() {
        return id_productos;
    }

    public void setId_productos(Long id_productos) {
        this.id_productos = id_productos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "id_compras=" + id_compras +
                ", cantidad=" + cantidad +
                ", fecha_compras=" + fecha_compras +
                ", id_proveedores=" + id_proveedores +
                ", razon_social='" + razon_social + '\'' +
                ", id_productos=" + id_productos +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
