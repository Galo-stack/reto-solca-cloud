package com.solca.pacientes.repository;

import com.solca.pacientes.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, String> {
    Optional<Paciente> findByCedula(String cedula);

    boolean existsByCedula(String cedula);
}