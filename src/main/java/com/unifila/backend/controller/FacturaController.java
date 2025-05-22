package com.unifila.backend.controller;

import com.unifila.backend.model.Factura;
import com.unifila.backend.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @PostMapping("/generar/{presupuestoId}")
    public ResponseEntity<Factura> generarFactura(@PathVariable Long presupuestoId) {
        Factura factura = facturaService.generarFacturaDesdePresupuesto(presupuestoId);
        return ResponseEntity.ok(factura);
    }
}
