package com.unifila.backend.service;

import com.unifila.backend.model.Entrega;
import com.unifila.backend.model.Factura;
import com.unifila.backend.repository.EntregaRepository;
import com.unifila.backend.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    public Entrega registrarEntrega(Long facturaId) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        Entrega entrega = new Entrega();
        entrega.setFactura(factura);
        entrega.setFechaEntrega(LocalDate.now());
        entrega.setEstado("ENTREGADO");

        return entregaRepository.save(entrega);
    }

    public Entrega obtenerEntregaPorFactura(Long facturaId) {
        return entregaRepository.findByFacturaId(facturaId);
    }
}