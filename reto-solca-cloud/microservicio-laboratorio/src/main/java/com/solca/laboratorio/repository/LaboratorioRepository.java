package com.solca.laboratorio.repository;

import com.solca.laboratorio.model.ResultadoLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaboratorioRepository extends JpaRepository<ResultadoLaboratorio, Long> {
    List<ResultadoLaboratorio> findByIdPacienteRegionalOrderByFechaEjecucionDesc(String idPacienteRegional);

    List<ResultadoLaboratorio> findBySedeAndTipoExamen(String sede, String tipoExamen);
}