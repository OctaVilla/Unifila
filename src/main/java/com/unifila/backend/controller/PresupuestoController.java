package com.unifila.backend.controller;

import com.unifila.backend.dto.PresupuestoDTO;
import com.unifila.backend.model.Presupuesto;
import com.unifila.backend.service.PresupuestoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/presupuestos")
public class PresupuestoController {

    private final PresupuestoService presupuestoService;

    public PresupuestoController(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }

    // Crear presupuesto
    @PostMapping
    public ResponseEntity<Presupuesto> crearPresupuesto(@RequestBody PresupuestoDTO dto) {
        Presupuesto nuevo = presupuestoService.crearPresupuesto(dto);
        return ResponseEntity.ok(nuevo);
    }

    // Obtener presupuesto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Presupuesto> obtenerPresupuesto(@PathVariable Long id) {
        Presupuesto presupuesto = presupuestoService.obtenerPorId(id);
        return ResponseEntity.ok(presupuesto);
    }

    // Generar PDF de presupuesto
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> obtenerPdf(@PathVariable Long id) {
        byte[] pdfBytes = presupuestoService.generarPdfPresupuestoPorId(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=presupuesto_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
