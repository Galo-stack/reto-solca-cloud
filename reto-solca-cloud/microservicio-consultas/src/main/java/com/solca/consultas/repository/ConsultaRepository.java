package com.solca.consultas.repository;

import com.solca.consultas.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByIdPacienteRegionalOrderByFechaConsultaDesc(String idPacienteRegional);

    boolean existsByIdPacienteRegional(String idPacienteRegional);
}