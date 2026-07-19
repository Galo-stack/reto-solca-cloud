package com.solca.repositorio.repository;

import com.solca.repositorio.model.ConsultaRepositorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepositorioRepository extends JpaRepository<ConsultaRepositorio, Long> {
    List<ConsultaRepositorio> findTop20ByOrderByFechaConsultaDesc();
}
