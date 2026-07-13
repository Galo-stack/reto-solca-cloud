package com.solca.imagenologia.repository;

import com.solca.imagenologia.model.SolicitudImagen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudImagenRepository extends JpaRepository<SolicitudImagen, Long> {
    List<SolicitudImagen> findByIdPacienteRegionalOrderByFechaSolicitudDesc(String idPacienteRegional);
    List<SolicitudImagen> findByEstadoOrderByFechaSolicitudDesc(String estado);
}
