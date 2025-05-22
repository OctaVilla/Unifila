package com.unifila.backend.controller;

import com.unifila.backend.dto.PresupuestoDTO;
import com.unifila.backend.model.Presupuesto;
import com.unifila.backend.service.PresupuestoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/presupuestos")
@CrossOrigin
public class PresupuestoController {

    private final PresupuestoService presupuestoService;

    public PresupuestoController(PresupuestoService presupuestoService) {
        this.presupuestoService = presupuestoService;
    }

    @PostMapping
    public Presupuesto crearPresupuesto(@RequestBody PresupuestoDTO dto) {
        return presupuestoService.crearPresupuesto(dto);
    }
}
