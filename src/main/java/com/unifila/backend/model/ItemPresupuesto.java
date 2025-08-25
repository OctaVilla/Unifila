package com.unifila.backend.model;

import java.math.BigDecimal;

public class ItemPresupuesto {
    private int cantidad;
    private String descripcion;
    private BigDecimal precioUnitario; // <- BigDecimal

    public ItemPresupuesto(int cantidad, String descripcion, BigDecimal precioUnitario) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
    }


    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
