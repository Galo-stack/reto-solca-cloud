package com.solca.laboratorio.repository;

import com.solca.laboratorio.model.ResultadoParametro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultadoParametroRepository extends JpaRepository<ResultadoParametro, Long> {
    List<ResultadoParametro> findByResultadoLaboratorioId(Long resultadoLaboratorioId);
}
