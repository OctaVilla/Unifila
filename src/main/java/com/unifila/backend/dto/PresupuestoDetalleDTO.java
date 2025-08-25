// dto/PresupuestoDetalleDTO.java
package com.unifila.backend.dto;
import java.math.BigDecimal;


import java.time.LocalDate;
import java.util.List;

public class PresupuestoDetalleDTO {
    public Long id;
    public LocalDate fecha;
    public String estado;
    public ClienteMinDTO cliente;
    public List<ItemDTO> detalles;
    public BigDecimal  total; // si usás long/BigDecimal, ajustá

    public static class ClienteMinDTO {
        public Long id;
        public String nombre;
        public String email;
    }
    public static class ItemDTO {
        public Long productoId;
        public String productoNombre;
        public int cantidad;
        public BigDecimal  precioUnitario;
        public BigDecimal  subtotal;
    }
}
