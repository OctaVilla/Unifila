package com.unifila.backend.controller;

import com.unifila.backend.model.Entrega;
import com.unifila.backend.service.EntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/entregas")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    @PostMapping("/registrar/{facturaId}")
    public Entrega registrarEntrega(@PathVariable Long facturaId) {
        return entregaService.registrarEntrega(facturaId);
    }

    @GetMapping("/factura/{facturaId}")
    public Entrega obtenerEntrega(@PathVariable Long facturaId) {
        return entregaService.obtenerEntregaPorFactura(facturaId);
    }
}