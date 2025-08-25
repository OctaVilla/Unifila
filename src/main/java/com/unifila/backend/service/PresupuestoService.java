package com.unifila.backend.service;

import com.unifila.backend.dto.PresupuestoDTO;
import com.unifila.backend.dto.PresupuestoDetalleDTO;
import com.unifila.backend.dto.PresupuestoResumenDTO;
import com.unifila.backend.dto.PresupuestosDTOMapper;
import com.unifila.backend.model.Cliente;
import com.unifila.backend.model.ItemPresupuesto;
import com.unifila.backend.model.Presupuesto;
import com.unifila.backend.model.PresupuestoDetalle;
import com.unifila.backend.model.Producto;
import com.unifila.backend.repository.ClienteRepository;
import com.unifila.backend.repository.PresupuestoRepository;
import com.unifila.backend.repository.ProductoRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final PdfGeneratorService pdfGeneratorService;

    public PresupuestoService(PresupuestoRepository presupuestoRepository,
                              ClienteRepository clienteRepository,
                              ProductoRepository productoRepository,
                              PdfGeneratorService pdfGeneratorService) {
        this.presupuestoRepository = presupuestoRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    // ========= Crear presupuesto =========
    @Transactional
    public Presupuesto crearPresupuesto(PresupuestoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setCliente(cliente);
        presupuesto.setFecha(dto.getFecha());
        presupuesto.setEstado(dto.getEstado());
        presupuesto.setDetalles(new ArrayList<>());

        dto.getDetalles().forEach(d -> {
            Producto producto = productoRepository.findById(d.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + d.getProductoId()));

            String tipo = d.getTipoOperacion();
            if (tipo == null) {
                throw new RuntimeException("Debe indicar tipoOperacion (VENTA/ALQUILER) para el producto " + d.getProductoId());
            }

            BigDecimal precioElegido;
            if ("VENTA".equalsIgnoreCase(tipo)) {
                if (producto.getPrecioVenta() == null) {
                    throw new RuntimeException("El producto " + producto.getId() + " no tiene precio de VENTA configurado.");
                }
                precioElegido = producto.getPrecioVenta();
            } else if ("ALQUILER".equalsIgnoreCase(tipo)) {
                if (producto.getPrecioAlquiler() == null) {
                    throw new RuntimeException("El producto " + producto.getId() + " no tiene precio de ALQUILER configurado.");
                }
                precioElegido = producto.getPrecioAlquiler();
            } else {
                throw new RuntimeException("tipoOperacion inválido (use VENTA o ALQUILER). Valor: " + tipo);
            }

            PresupuestoDetalle det = new PresupuestoDetalle();
            det.setCantidad(d.getCantidad());
            det.setPrecioUnitario(precioElegido);
            det.setTipoOperacion(tipo.toUpperCase());
            det.setProducto(producto);
            det.setPresupuesto(presupuesto);

            presupuesto.getDetalles().add(det);
        });

        return presupuestoRepository.save(presupuesto);
    }

    // ========= Obtener entidad por ID =========
    @Transactional(readOnly = true)
    public Presupuesto obtenerPorId(Long id) {
        return presupuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado"));
    }

    // ========= Generar PDF a partir del ID =========
    @Transactional(readOnly = true)
    public byte[] generarPdfPresupuestoPorId(Long idPresupuesto) {
        Presupuesto presupuesto = obtenerPorId(idPresupuesto);

        String nombreCliente = presupuesto.getCliente().getNombre();
        String fechaTexto = presupuesto.getFecha()
                .format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "PY")));

        List<ItemPresupuesto> items = presupuesto.getDetalles().stream()
                .map(det -> new ItemPresupuesto(
                        det.getCantidad(),
                        det.getProducto().getNombre(),         // podés concatenar tipo si querés
                        det.getPrecioUnitario()                // BigDecimal
                ))
                .collect(Collectors.toList());

        try {
            return pdfGeneratorService.generarPresupuesto(nombreCliente, fechaTexto, items);
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }

    // ========= NUEVO: Listado paginado con filtros =========
    @Transactional(readOnly = true)
    public Page<PresupuestoResumenDTO> listar(int page, int size, String estado, Long clienteId) {
        var pageable = PageRequest.of(page, size);

        Page<Presupuesto> pagina;
        if (estado != null && clienteId != null) {
            pagina = presupuestoRepository.findByEstadoAndCliente_Id(estado, clienteId, pageable);
        } else if (estado != null) {
            pagina = presupuestoRepository.findByEstado(estado, pageable);
        } else if (clienteId != null) {
            pagina = presupuestoRepository.findByCliente_Id(clienteId, pageable);
        } else {
            pagina = presupuestoRepository.findAll(pageable);
        }

        return pagina.map(PresupuestosDTOMapper::toResumen);
    }

    // ========= NUEVO: Detalle listo para el front =========
    @Transactional(readOnly = true)
    public PresupuestoDetalleDTO obtenerDTOPorId(Long id) {
        Presupuesto p = obtenerPorId(id);

        // Inicializar relaciones LAZY por las dudas
        if (p.getDetalles() != null) {
            p.getDetalles().forEach(d -> {
                if (d.getProducto() != null) d.getProducto().getNombre();
            });
        }

        return PresupuestosDTOMapper.toDetalle(p);
    }
}
