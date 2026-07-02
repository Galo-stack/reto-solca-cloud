package com.solca.laboratorio.repository;

import com.solca.laboratorio.model.ResultadoLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaboratorioRepository extends JpaRepository<ResultadoLaboratorio, Long> {
    List<ResultadoLaboratorio> findByIdPacienteRegionalOrderByFechaEjecucionDesc(String idPacienteRegional);

    List<ResultadoLaboratorio> findBySedeAndTipoExamen(String sede, String tipoExamen);
}