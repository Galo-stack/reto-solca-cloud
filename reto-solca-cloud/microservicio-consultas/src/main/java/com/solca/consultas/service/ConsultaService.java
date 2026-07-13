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
                .examenFisico(dto.getExamenFisico())
                .observaciones(dto.getObservaciones())
                .recomendaciones(dto.getRecomendaciones())
                .peso(dto.getPeso() != null ? dto.getPeso() : java.math.BigDecimal.ZERO)
                .talla(dto.getTalla() != null ? dto.getTalla() : java.math.BigDecimal.ZERO)
                .imc(dto.getImc() != null ? dto.getImc() : java.math.BigDecimal.ZERO)
                .clasificacionOms(dto.getClasificacionOms() != null ? dto.getClasificacionOms() : "")
                .superficieCorporal(dto.getSuperficieCorporal() != null ? dto.getSuperficieCorporal() : java.math.BigDecimal.ZERO)
                .riesgoCardiovascular(dto.getRiesgoCardiovascular() != null ? dto.getRiesgoCardiovascular() : "")
                .presionArterial(dto.getPresionArterial() != null ? dto.getPresionArterial() : "")
                .frecuenciaCardiaca(dto.getFrecuenciaCardiaca() != null ? dto.getFrecuenciaCardiaca() : 0)
                .temperatura(dto.getTemperatura() != null ? dto.getTemperatura() : java.math.BigDecimal.ZERO)
                .frecuenciaRespiratoria(dto.getFrecuenciaRespiratoria() != null ? dto.getFrecuenciaRespiratoria() : 0)
                .saturacionOxigeno(dto.getSaturacionOxigeno() != null ? dto.getSaturacionOxigeno() : 0)
                .menarquia(dto.getMenarquia() != null ? dto.getMenarquia() : 0)
                .fum(dto.getFum())
                .fpp(dto.getFpp())
                .edadGestacional(dto.getEdadGestacional() != null ? dto.getEdadGestacional() : 0)
                .gestas(dto.getGestas() != null ? dto.getGestas() : 0)
                .partos(dto.getPartos() != null ? dto.getPartos() : 0)
                .cesareas(dto.getCesareas() != null ? dto.getCesareas() : 0)
                .abortos(dto.getAbortos() != null ? dto.getAbortos() : 0)
                .hijosVivos(dto.getHijosVivos() != null ? dto.getHijosVivos() : 0)
                .hijosMuertos(dto.getHijosMuertos() != null ? dto.getHijosMuertos() : 0)
                .embarazosEctopicos(dto.getEmbarazosEctopicos() != null ? dto.getEmbarazosEctopicos() : 0)
                .embarazosMultiples(dto.getEmbarazosMultiples() != null ? dto.getEmbarazosMultiples() : 0)
                .metodoAnticonceptivo(dto.getMetodoAnticonceptivo() != null ? dto.getMetodoAnticonceptivo() : "")
                .lactancia(dto.getLactancia() != null ? dto.getLactancia() : "")
                .menopausia(dto.getMenopausia() != null ? dto.getMenopausia() : "")
                .pap(dto.getPap() != null ? dto.getPap() : "")
                .colposcopia(dto.getColposcopia() != null ? dto.getColposcopia() : "")
                .mamografia(dto.getMamografia() != null ? dto.getMamografia() : "")
                .antecedentesGinecologicos(dto.getAntecedentesGinecologicos())
                .diagnosticoPrincipalCie10(dto.getDiagnosticoPrincipalCie10() != null ? dto.getDiagnosticoPrincipalCie10() : "")
                .diagnosticoPrincipalDesc(dto.getDiagnosticoPrincipalDesc())
                .diagnosticoSecundarioCie10(dto.getDiagnosticoSecundarioCie10() != null ? dto.getDiagnosticoSecundarioCie10() : "")
                .diagnosticoSecundarioDesc(dto.getDiagnosticoSecundarioDesc())
                .tipoDiagnostico(dto.getTipoDiagnostico() != null ? dto.getTipoDiagnostico() : "PRESUNTIVO")
                .alergiasConsulta(dto.getAlergiasConsulta())
                .planTratamiento(dto.getPlanTratamiento())
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

    @Transactional
    public ConsultaDTO actualizarConsulta(Long id, ConsultaDTO dto) {
        log.info("Actualizando consulta ID: {}", id);

        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada: " + id));

        consulta.setEspecialidad(dto.getEspecialidad());
        consulta.setDiagnostico(dto.getDiagnostico());
        consulta.setTratamiento(dto.getTratamiento());
        consulta.setMedico(dto.getMedico());
        consulta.setNotas(dto.getNotas());
        consulta.setExamenFisico(dto.getExamenFisico());
        consulta.setObservaciones(dto.getObservaciones());
        consulta.setRecomendaciones(dto.getRecomendaciones());
        consulta.setPeso(dto.getPeso());
        consulta.setTalla(dto.getTalla());
        consulta.setImc(dto.getImc());
        consulta.setClasificacionOms(dto.getClasificacionOms());
        consulta.setSuperficieCorporal(dto.getSuperficieCorporal());
        consulta.setRiesgoCardiovascular(dto.getRiesgoCardiovascular());
        consulta.setPresionArterial(dto.getPresionArterial());
        consulta.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        consulta.setTemperatura(dto.getTemperatura());
        consulta.setFrecuenciaRespiratoria(dto.getFrecuenciaRespiratoria());
        consulta.setSaturacionOxigeno(dto.getSaturacionOxigeno());
        consulta.setMenarquia(dto.getMenarquia());
        consulta.setFum(dto.getFum());
        consulta.setFpp(dto.getFpp());
        consulta.setEdadGestacional(dto.getEdadGestacional());
        consulta.setGestas(dto.getGestas());
        consulta.setPartos(dto.getPartos());
        consulta.setCesareas(dto.getCesareas());
        consulta.setAbortos(dto.getAbortos());
        consulta.setHijosVivos(dto.getHijosVivos());
        consulta.setHijosMuertos(dto.getHijosMuertos());
        consulta.setEmbarazosEctopicos(dto.getEmbarazosEctopicos());
        consulta.setEmbarazosMultiples(dto.getEmbarazosMultiples());
        consulta.setMetodoAnticonceptivo(dto.getMetodoAnticonceptivo());
        consulta.setLactancia(dto.getLactancia());
        consulta.setMenopausia(dto.getMenopausia());
        consulta.setPap(dto.getPap());
        consulta.setColposcopia(dto.getColposcopia());
        consulta.setMamografia(dto.getMamografia());
        consulta.setAntecedentesGinecologicos(dto.getAntecedentesGinecologicos());
        consulta.setDiagnosticoPrincipalCie10(dto.getDiagnosticoPrincipalCie10());
        consulta.setDiagnosticoPrincipalDesc(dto.getDiagnosticoPrincipalDesc());
        consulta.setDiagnosticoSecundarioCie10(dto.getDiagnosticoSecundarioCie10());
        consulta.setDiagnosticoSecundarioDesc(dto.getDiagnosticoSecundarioDesc());
        consulta.setTipoDiagnostico(dto.getTipoDiagnostico());
        consulta.setAlergiasConsulta(dto.getAlergiasConsulta());
        consulta.setPlanTratamiento(dto.getPlanTratamiento());
        consulta.setSede(dto.getSede());
        consulta.setFechaConsulta(dto.getFechaConsulta());

        Consulta saved = consultaRepository.save(consulta);
        log.info("Consulta actualizada ID: {}", saved.getId());
        return convertirADTO(saved);
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
                .examenFisico(c.getExamenFisico())
                .observaciones(c.getObservaciones())
                .recomendaciones(c.getRecomendaciones())
                .peso(c.getPeso())
                .talla(c.getTalla())
                .imc(c.getImc())
                .clasificacionOms(c.getClasificacionOms())
                .superficieCorporal(c.getSuperficieCorporal())
                .riesgoCardiovascular(c.getRiesgoCardiovascular())
                .presionArterial(c.getPresionArterial())
                .frecuenciaCardiaca(c.getFrecuenciaCardiaca())
                .temperatura(c.getTemperatura())
                .frecuenciaRespiratoria(c.getFrecuenciaRespiratoria())
                .saturacionOxigeno(c.getSaturacionOxigeno())
                .menarquia(c.getMenarquia())
                .fum(c.getFum())
                .fpp(c.getFpp())
                .edadGestacional(c.getEdadGestacional())
                .gestas(c.getGestas())
                .partos(c.getPartos())
                .cesareas(c.getCesareas())
                .abortos(c.getAbortos())
                .hijosVivos(c.getHijosVivos())
                .hijosMuertos(c.getHijosMuertos())
                .embarazosEctopicos(c.getEmbarazosEctopicos())
                .embarazosMultiples(c.getEmbarazosMultiples())
                .metodoAnticonceptivo(c.getMetodoAnticonceptivo())
                .lactancia(c.getLactancia())
                .menopausia(c.getMenopausia())
                .pap(c.getPap())
                .colposcopia(c.getColposcopia())
                .mamografia(c.getMamografia())
                .antecedentesGinecologicos(c.getAntecedentesGinecologicos())
                .diagnosticoPrincipalCie10(c.getDiagnosticoPrincipalCie10())
                .diagnosticoPrincipalDesc(c.getDiagnosticoPrincipalDesc())
                .diagnosticoSecundarioCie10(c.getDiagnosticoSecundarioCie10())
                .diagnosticoSecundarioDesc(c.getDiagnosticoSecundarioDesc())
                .tipoDiagnostico(c.getTipoDiagnostico())
                .alergiasConsulta(c.getAlergiasConsulta())
                .planTratamiento(c.getPlanTratamiento())
                .build();
    }
}