package com.unifila.backend.model;

public class ItemPresupuesto {
    private int cantidad;
    private String descripcion;
    private double precioUnitario;

    // Constructor
    public ItemPresupuesto(int cantidad, String descripcion, double precioUnitario) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
    }

    public ItemPresupuesto() {

    }

    // Getters y setters
    public int getCantidad() { return cantidad; }
    public String getDescripcion() { return descripcion; }
    public double getPrecioUnitario() { return precioUnitario; }

    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
}
