package com.unifila.backend.service;

import com.unifila.backend.dto.PresupuestoDTO;
import com.unifila.backend.model.*;
import com.unifila.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public PresupuestoService(PresupuestoRepository presupuestoRepository,
                              ClienteRepository clienteRepository,
                              ProductoRepository productoRepository) {
        this.presupuestoRepository = presupuestoRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    public Presupuesto crearPresupuesto(PresupuestoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setCliente(cliente);
        presupuesto.setFecha(dto.getFecha());
        presupuesto.setEstado(dto.getEstado());
        presupuesto.setDetalles(new ArrayList<>());

        for (PresupuestoDTO.ItemDetalleDTO detalleDTO : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            PresupuestoDetalle detalle = new PresupuestoDetalle();
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            detalle.setProducto(producto);
            detalle.setPresupuesto(presupuesto);

            presupuesto.getDetalles().add(detalle);
        }

        return presupuestoRepository.save(presupuesto);
    }
}
