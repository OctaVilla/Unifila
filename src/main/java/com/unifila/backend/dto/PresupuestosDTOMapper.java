// src/main/java/com/unifila/backend/dto/PresupuestosDTOMapper.java
package com.unifila.backend.dto;

import com.unifila.backend.model.Presupuesto;
import java.math.BigDecimal;
import java.util.stream.Collectors;

public class PresupuestosDTOMapper {

    public static PresupuestoResumenDTO toResumen(Presupuesto p) {
        PresupuestoResumenDTO r = new PresupuestoResumenDTO();
        r.id = p.getId();
        r.fecha = p.getFecha();
        r.estado = p.getEstado();
        r.clienteNombre = (p.getCliente() != null) ? p.getCliente().getNombre() : null;

        r.total = (p.getDetalles() == null) ? BigDecimal.ZERO
                : p.getDetalles().stream()
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return r;
    }

    public static PresupuestoDetalleDTO toDetalle(Presupuesto p) {
        PresupuestoDetalleDTO dto = new PresupuestoDetalleDTO();
        dto.id = p.getId();
        dto.fecha = p.getFecha();
        dto.estado = p.getEstado();

        // Cliente mínimo
        PresupuestoDetalleDTO.ClienteMinDTO c = new PresupuestoDetalleDTO.ClienteMinDTO();
        if (p.getCliente() != null) {
            c.id = p.getCliente().getId();
            c.nombre = p.getCliente().getNombre();
        }
        dto.cliente = c;

        // Items
        dto.detalles = (p.getDetalles() == null) ? java.util.List.of()
                : p.getDetalles().stream().map(d -> {
            PresupuestoDetalleDTO.ItemDTO i = new PresupuestoDetalleDTO.ItemDTO();
            i.productoId = d.getProducto().getId();
            i.productoNombre = d.getProducto().getNombre();
            i.cantidad = d.getCantidad();
            i.precioUnitario = d.getPrecioUnitario(); // BigDecimal
            i.subtotal = d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad()));
            return i;
        }).collect(Collectors.toList());

        // Total
        dto.total = dto.detalles.stream()
                .map(item -> item.subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return dto;
    }
}
