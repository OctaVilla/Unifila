package com.unifila.backend.repository;

import com.unifila.backend.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {
    Entrega findByFacturaId(Long facturaId);
}