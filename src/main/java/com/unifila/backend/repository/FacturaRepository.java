package com.unifila.backend.repository;

import com.unifila.backend.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Long> {}
