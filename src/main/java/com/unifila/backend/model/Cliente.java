package com.unifila.backend.model;

import jakarta.persistence.*;

@Entity // Esta clase será una tabla
@Table(name = "clientes") // Nombre que tendrá la tabla
public class Cliente {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremental
    private Long id;

    private String nombre;

    private String ruc;

    private String direccion;

    private String telefono;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
