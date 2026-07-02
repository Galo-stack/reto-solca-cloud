package com.solca.consultas.service;

import com.solca.consultas.dto.ConsultaDTO;
import com.solca.consultas.model.Consulta;
import com.solca.consultas.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultaService {
    private final ConsultaRepository consultaRepository;

    @Transactional
    public ConsultaDTO registrarConsulta(ConsultaDTO dto) {
        log.info("Registrando consulta para paciente: {}", dto.getIdPacienteRegional());

        Consulta consulta = Consulta.builder()
                .idPacienteRegional(dto.getIdPacienteRegional())
                .sede(dto.getSede())
                .fechaConsulta(dto.getFechaConsulta())
                .especialidad(dto.getEspecialidad())
                .diagnostico(dto.getDiagnostico())
                .tratamiento(dto.getTratamiento())
                .medico(dto.getMedico())
                .notas(dto.getNotas())
                .activo(true)
                .build();

        Consulta saved = consultaRepository.save(consulta);
        dto.setId(saved.getId());
        log.info("Consulta registrada con ID: {}", saved.getId());
        return dto;
    }

    public List<ConsultaDTO> obtenerConsultasPorPaciente(String idPaciente) {
        log.debug("Obteniendo consultas para paciente: {}", idPaciente);
        return consultaRepository.findByIdPacienteRegionalOrderByFechaConsultaDesc(idPaciente)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ConsultaDTO obtenerConsultaPorId(Long id) {
        log.debug("Obteniendo consulta por ID: {}", id);
        return consultaRepository.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    private ConsultaDTO convertirADTO(Consulta c) {
        return ConsultaDTO.builder()
                .id(c.getId())
                .idPacienteRegional(c.getIdPacienteRegional())
                .sede(c.getSede())
                .fechaConsulta(c.getFechaConsulta())
                .especialidad(c.getEspecialidad())
                .diagnostico(c.getDiagnostico())
                .tratamiento(c.getTratamiento())
                .medico(c.getMedico())
                .notas(c.getNotas())
                .build();
    }
}