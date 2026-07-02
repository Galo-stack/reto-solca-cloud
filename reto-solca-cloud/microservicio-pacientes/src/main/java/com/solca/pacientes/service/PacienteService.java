package com.solca.pacientes.service;

import com.solca.pacientes.dto.PacienteDTO;
import com.solca.pacientes.model.Paciente;
import com.solca.pacientes.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PacienteService {
    private final PacienteRepository pacienteRepository;

    public List<PacienteDTO> listarTodos() {
        return pacienteRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public PacienteDTO registrarPaciente(PacienteDTO dto) {
        log.info("Registrando paciente: {}", dto.getCedula());

        if (pacienteRepository.existsByCedula(dto.getCedula())) {
            throw new RuntimeException("Ya existe un paciente con esa cédula");
        }

        long count = pacienteRepository.count() + 1;
        String idRegional = String.format("PAC-%05d", count);

        Paciente paciente = Paciente.builder()
                .idPacienteRegional(idRegional)
                .cedula(dto.getCedula())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(dto.getFechaNacimiento())
                .genero(dto.getGenero())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .direccion(dto.getDireccion())
                .activo(true)
                .build();

        Paciente saved = pacienteRepository.save(paciente);
        dto.setIdPacienteRegional(saved.getIdPacienteRegional());
        return dto;
    }

    public PacienteDTO buscarPorCedula(String cedula) {
        return pacienteRepository.findByCedula(cedula)
                .map(this::convertirADTO)
                .orElse(null);
    }

    public PacienteDTO buscarPorId(String id) {
        return pacienteRepository.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    private PacienteDTO convertirADTO(Paciente p) {
        return PacienteDTO.builder()
                .idPacienteRegional(p.getIdPacienteRegional())
                .cedula(p.getCedula())
                .nombres(p.getNombres())
                .apellidos(p.getApellidos())
                .fechaNacimiento(p.getFechaNacimiento())
                .genero(p.getGenero())
                .telefono(p.getTelefono())
                .email(p.getEmail())
                .direccion(p.getDireccion())
                .build();
    }
}