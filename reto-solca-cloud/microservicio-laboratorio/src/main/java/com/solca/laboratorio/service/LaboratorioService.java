package com.solca.laboratorio.service;

import com.solca.laboratorio.dto.LaboratorioDTO;
import com.solca.laboratorio.model.ResultadoLaboratorio;
import com.solca.laboratorio.repository.LaboratorioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LaboratorioService {
    private final LaboratorioRepository laboratorioRepository;

    @Transactional
    public LaboratorioDTO registrarResultado(LaboratorioDTO dto) {
        log.info("Registrando resultado de laboratorio para: {}", dto.getIdPacienteRegional());

        ResultadoLaboratorio resultado = ResultadoLaboratorio.builder()
                .idPacienteRegional(dto.getIdPacienteRegional())
                .sede(dto.getSede())
                .fechaEjecucion(dto.getFechaEjecucion())
                .tipoExamen(dto.getTipoExamen())
                .resultado(dto.getResultado())
                .valoresReferencia(dto.getValoresReferencia())
                .medicoSolicitante(dto.getMedicoSolicitante())
                .area(dto.getArea())
                .metodo(dto.getMetodo())
                .observaciones(dto.getObservaciones())
                .fechaResultado(dto.getFechaResultado())
                .anormal(dto.getAnormal() != null ? dto.getAnormal() : false)
                .activo(true)
                .build();

        ResultadoLaboratorio saved = laboratorioRepository.save(resultado);
        dto.setId(saved.getId());
        log.info("Resultado registrado con ID: {}", saved.getId());
        return dto;
    }

    public List<LaboratorioDTO> obtenerResultadosPorPaciente(String idPaciente) {
        log.debug("Obteniendo resultados para paciente: {}", idPaciente);
        return laboratorioRepository.findByIdPacienteRegionalOrderByFechaEjecucionDesc(idPaciente)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public LaboratorioDTO obtenerResultadoPorId(Long id) {
        log.debug("Obteniendo resultado por ID: {}", id);
        return laboratorioRepository.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    private LaboratorioDTO convertirADTO(ResultadoLaboratorio r) {
        return LaboratorioDTO.builder()
                .id(r.getId())
                .idPacienteRegional(r.getIdPacienteRegional())
                .sede(r.getSede())
                .fechaEjecucion(r.getFechaEjecucion())
                .tipoExamen(r.getTipoExamen())
                .resultado(r.getResultado())
                .valoresReferencia(r.getValoresReferencia())
                .medicoSolicitante(r.getMedicoSolicitante())
                .area(r.getArea())
                .metodo(r.getMetodo())
                .observaciones(r.getObservaciones())
                .fechaResultado(r.getFechaResultado())
                .anormal(r.getAnormal())
                .build();
    }
}