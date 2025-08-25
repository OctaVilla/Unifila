package com.unifila.backend.controller;

import com.unifila.backend.model.ItemPresupuesto;
import com.unifila.backend.model.Presupuesto;
import com.unifila.backend.service.PdfGeneratorService;
import com.unifila.backend.service.PresupuestoService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/presupuestos")
public class PdfController {

    private final PdfGeneratorService pdfService;
    private final PresupuestoService presupuestoService;

    public PdfController(PdfGeneratorService pdfService, PresupuestoService presupuestoService) {
        this.pdfService = pdfService;
        this.presupuestoService = presupuestoService;
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generarPdfDesdeBase(@PathVariable Long id) throws Exception {
        Presupuesto presupuesto = presupuestoService.obtenerPorId(id);

        String cliente = presupuesto.getCliente().getNombre();
        String fecha = presupuesto.getFecha()
                .format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "PY")));

        var items = presupuesto.getDetalles().stream()
                .map(detalle -> new ItemPresupuesto(
                        detalle.getCantidad(),
                        detalle.getProducto().getNombre(),
                        // --- ELIGE UNA de estas dos líneas según tu tipo de dato ---
                        // Si precioUnitario es BigDecimal:
                        // detalle.getPrecioUnitario().doubleValue()
                        // Si precioUnitario es Double:
                        detalle.getPrecioUnitario()
                ))
                .collect(Collectors.toList());

        byte[] pdf = pdfService.generarPresupuesto(cliente, fecha, items);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.inline().filename("presupuesto_" + id + ".pdf").build()
        );

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
