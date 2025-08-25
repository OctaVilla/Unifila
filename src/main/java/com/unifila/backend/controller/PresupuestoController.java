package com.unifila.backend.controller;

import com.unifila.backend.dto.PresupuestoDTO;
import com.unifila.backend.dto.PresupuestoDetalleDTO;
import com.unifila.backend.dto.PresupuestoResumenDTO;
import com.unifila.backend.model.Presupuesto;
import com.unifila.backend.service.PresupuestoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/presupuestos")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:3000"})
public class PresupuestoController {

    private final PresupuestoService presupuestoService;

    public PresupuestoController(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }

    // Crear presupuesto
    @PostMapping
    public ResponseEntity<?> crearPresupuesto(@RequestBody PresupuestoDTO dto) {
        Presupuesto creado = presupuestoService.crearPresupuesto(dto);
        var location = URI.create("/presupuestos/" + (creado != null ? creado.getId() : null));
        return ResponseEntity.created(location).body(Map.of(
                "success", true,
                "message", "Presupuesto creado",
                "data", creado
        ));
    }

    // Listar presupuestos (paginado + filtros opcionales)
    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long clienteId
    ) {
        var pageData = presupuestoService.listar(page, size, estado, clienteId); // Page<PresupuestoResumenDTO>
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", pageData.getContent(),
                "page", pageData.getNumber(),
                "size", pageData.getSize(),
                "total", pageData.getTotalElements()
        ));
    }

    // Detalle por ID (DTO listo para front)
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPresupuesto(@PathVariable Long id) {
        PresupuestoDetalleDTO dto = presupuestoService.obtenerDTOPorId(id);
        return ResponseEntity.ok(Map.of("success", true, "data", dto));
    }

    // PDF inline por defecto; ?download=true para descargar
    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> obtenerPdf(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean download
    ) {
        Presupuesto p = presupuestoService.obtenerPorId(id);
        byte[] pdfBytes = presupuestoService.generarPdfPresupuestoPorId(id);

        String fecha = (p.getFecha() != null)
                ? p.getFecha().format(DateTimeFormatter.ISO_DATE)
                : "sin_fecha";

        String cliente = (p.getCliente() != null && p.getCliente().getNombre() != null)
                ? p.getCliente().getNombre().replaceAll("[^\\p{L}\\p{N}_-]+", "_")
                : "cliente";

        String base = "presupuesto_" + cliente + "_" + fecha + ".pdf";
        String fileName = URLEncoder.encode(base, StandardCharsets.UTF_8);
        String disposition = (download ? "attachment" : "inline") + "; filename*=UTF-8''" + fileName;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                .cacheControl(CacheControl.noCache())
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(pdfBytes));
    }
}
