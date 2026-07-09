package com.solca.pacientes.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solca.pacientes.dto.PacienteDTO;
import com.solca.pacientes.model.Paciente;
import com.solca.pacientes.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final ObjectMapper objectMapper;

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
                .grupoSanguineo(dto.getGrupoSanguineo())
                .estadoCivil(dto.getEstadoCivil())
                .ocupacion(dto.getOcupacion())
                .alergias(dto.getAlergias())
                .enfermedadActual(dto.getEnfermedadActual())
                .antecedentesPersonales(toJsonString(dto.getAntecedentesPersonales()))
                .antecedentesFamiliares(toJsonString(dto.getAntecedentesFamiliares()))
                .contactoEmergencia(toJsonString(dto.getContactoEmergencia()))
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

    @Transactional
    public PacienteDTO actualizarPaciente(String id, PacienteDTO dto) {
        log.info("Actualizando paciente: {}", id);

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado: " + id));

        if (!paciente.getCedula().equals(dto.getCedula()) &&
            pacienteRepository.existsByCedula(dto.getCedula())) {
            throw new RuntimeException("Ya existe otro paciente con esa cedula");
        }

        paciente.setCedula(dto.getCedula());
        paciente.setNombres(dto.getNombres());
        paciente.setApellidos(dto.getApellidos());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setGenero(dto.getGenero());
        paciente.setTelefono(dto.getTelefono());
        paciente.setEmail(dto.getEmail());
        paciente.setDireccion(dto.getDireccion());
        paciente.setGrupoSanguineo(dto.getGrupoSanguineo());
        paciente.setEstadoCivil(dto.getEstadoCivil());
        paciente.setOcupacion(dto.getOcupacion());
        paciente.setAlergias(dto.getAlergias());
        paciente.setEnfermedadActual(dto.getEnfermedadActual());
        paciente.setAntecedentesPersonales(toJsonString(dto.getAntecedentesPersonales()));
        paciente.setAntecedentesFamiliares(toJsonString(dto.getAntecedentesFamiliares()));
        paciente.setContactoEmergencia(toJsonString(dto.getContactoEmergencia()));

        Paciente saved = pacienteRepository.save(paciente);
        log.info("Paciente actualizado: {}", saved.getIdPacienteRegional());
        return convertirADTO(saved);
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
                .grupoSanguineo(p.getGrupoSanguineo())
                .estadoCivil(p.getEstadoCivil())
                .ocupacion(p.getOcupacion())
                .alergias(p.getAlergias())
                .enfermedadActual(p.getEnfermedadActual())
                .antecedentesPersonales(fromJsonString(p.getAntecedentesPersonales()))
                .antecedentesFamiliares(fromJsonString(p.getAntecedentesFamiliares()))
                .contactoEmergencia(fromJsonString(p.getContactoEmergencia()))
                .build();
    }

    private String toJsonString(Object obj) {
        if (obj == null) return null;
        try {
            if (obj instanceof String) return (String) obj;
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Error al serializar campo a JSON: {}", e.getMessage());
            return obj.toString();
        }
    }

    private Object fromJsonString(String json) {
        if (json == null || json.isBlank()) return null;
        try {
            return objectMapper.readValue(json, Object.class);
        } catch (JsonProcessingException e) {
            return json;
        }
    }
}