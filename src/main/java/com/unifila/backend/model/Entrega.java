package com.unifila.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaEntrega;

    @OneToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    private String estado; // Ej: "ENTREGADO", "PENDIENTE"

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
