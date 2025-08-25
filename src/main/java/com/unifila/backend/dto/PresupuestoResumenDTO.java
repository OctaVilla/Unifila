// dto/PresupuestoResumenDTO.java
package com.unifila.backend.dto;
import java.math.BigDecimal;


import java.time.LocalDate;

public class PresupuestoResumenDTO {
    public Long id;
    public LocalDate fecha;
    public String estado;
    public String clienteNombre;
    public BigDecimal total;
}
