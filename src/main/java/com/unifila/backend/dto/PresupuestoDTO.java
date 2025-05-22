package com.unifila.backend.dto;

import java.time.LocalDate;
import java.util.List;

public class PresupuestoDTO {

    private LocalDate fecha;
    private String estado;
    private Long clienteId;
    private List<ItemDetalleDTO> detalles;

    public static class ItemDetalleDTO {
        private Long productoId;
        private Integer cantidad;
        private Double precioUnitario;

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public Double getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(Double precioUnitario) {
            this.precioUnitario = precioUnitario;
        }
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemDetalleDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<ItemDetalleDTO> detalles) {
        this.detalles = detalles;
    }
}
