package com.unifila.backend.service;

import com.unifila.backend.model.Factura;
import com.unifila.backend.model.FacturaDetalle;
import com.unifila.backend.model.Presupuesto;
import com.unifila.backend.model.PresupuestoDetalle;
import com.unifila.backend.repository.FacturaRepository;
import com.unifila.backend.repository.PresupuestoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FacturaService {

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    public Factura generarFacturaDesdePresupuesto(Long presupuestoId) {
        Presupuesto presupuesto = presupuestoRepository.findById(presupuestoId)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado"));

        Factura factura = new Factura();
        factura.setFecha(LocalDate.now());
        factura.setPresupuesto(presupuesto);

        List<FacturaDetalle> detallesFactura = new ArrayList<>();
        for (PresupuestoDetalle detallePresupuesto : presupuesto.getDetalles()) {
            FacturaDetalle detalleFactura = new FacturaDetalle();
            detalleFactura.setProducto(detallePresupuesto.getProducto());
            detalleFactura.setCantidad(detallePresupuesto.getCantidad());
            detalleFactura.setPrecioUnitario(detallePresupuesto.getPrecioUnitario());
            detalleFactura.setFactura(factura);
            detallesFactura.add(detalleFactura);
        }

        factura.setDetalles(detallesFactura);

        return facturaRepository.save(factura);
    }
}
