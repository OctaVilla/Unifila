package com.unifila.backend.controller;

import com.unifila.backend.model.Presupuesto;
import com.unifila.backend.model.PresupuestoDetalle;
import com.unifila.backend.service.PdfGeneratorService;
import com.unifila.backend.service.PresupuestoService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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

        String cliente = presupuesto.getCliente().getNombre(); // adaptá si usás nombreCompleto
        String fecha = presupuesto.getFecha().toString(); // podés formatear si querés
        var items = presupuesto.getDetalles().stream()
                .map(detalle -> new com.unifila.backend.model.ItemPresupuesto(
                        detalle.getCantidad(),
                        detalle.getProducto().getNombre(),
                        detalle.getPrecioUnitario()
                ))
                .collect(Collectors.toList());

        byte[] pdf = pdfService.generarPresupuesto(cliente, fecha, items);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("presupuesto_" + id + ".pdf").build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
