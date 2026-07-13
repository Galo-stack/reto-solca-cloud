package com.solca.laboratorio.repository;

import com.solca.laboratorio.model.SolicitudExamen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudExamenRepository extends JpaRepository<SolicitudExamen, Long> {
    List<SolicitudExamen> findBySolicitudId(Long solicitudId);
}
