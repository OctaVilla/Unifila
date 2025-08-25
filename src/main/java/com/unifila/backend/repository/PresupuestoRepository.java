// repository/PresupuestoRepository.java
package com.unifila.backend.repository;

import com.unifila.backend.model.Presupuesto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    Page<Presupuesto> findByEstado(String estado, Pageable pageable);
    Page<Presupuesto> findByCliente_Id(Long clienteId, Pageable pageable);
    Page<Presupuesto> findByEstadoAndCliente_Id(String estado, Long clienteId, Pageable pageable);
}
