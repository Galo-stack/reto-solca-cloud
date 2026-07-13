package com.solca.laboratorio.repository;

import com.solca.laboratorio.model.SolicitudLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudLaboratorioRepository extends JpaRepository<SolicitudLaboratorio, Long> {
    List<SolicitudLaboratorio> findByIdPacienteRegionalOrderByFechaSolicitudDesc(String idPacienteRegional);
    List<SolicitudLaboratorio> findByEstadoOrderByFechaSolicitudDesc(String estado);
}
