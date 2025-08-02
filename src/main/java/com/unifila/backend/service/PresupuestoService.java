package com.unifila.backend.service;

import com.unifila.backend.dto.PresupuestoDTO;
import com.unifila.backend.model.*;
import com.unifila.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    public PresupuestoService(PresupuestoRepository presupuestoRepository,
                              ClienteRepository clienteRepository,
                              ProductoRepository productoRepository) {
        this.presupuestoRepository = presupuestoRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    // Crear presupuesto
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

    // Obtener presupuesto por ID
    public Presupuesto obtenerPorId(Long id) {
        return presupuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado"));
    }

    // Generar PDF a partir del ID de presupuesto
    public byte[] generarPdfPresupuestoPorId(Long idPresupuesto) {
        Presupuesto presupuesto = obtenerPorId(idPresupuesto);

        String nombreCliente = presupuesto.getCliente().getNombre();

        // ✅ Corregido: si presupuesto.getFecha() es LocalDate
        String fechaTexto = presupuesto.getFecha()
                .format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "PY")));

        List<ItemPresupuesto> items = presupuesto.getDetalles().stream().map(det -> {
            return new ItemPresupuesto(
                    det.getCantidad(),
                    det.getProducto().getNombre(),
                    det.getPrecioUnitario().doubleValue()
            );
        }).toList();

        try {
            return pdfGeneratorService.generarPresupuesto(nombreCliente, fechaTexto, items);
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }
}
