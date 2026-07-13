package com.solca.laboratorio.service;

import com.solca.laboratorio.model.SolicitudExamen;
import com.solca.laboratorio.model.SolicitudLaboratorio;
import com.solca.laboratorio.repository.SolicitudExamenRepository;
import com.solca.laboratorio.repository.SolicitudLaboratorioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolicitudLaboratorioService {
    private final SolicitudLaboratorioRepository solicitudLaboratorioRepository;
    private final SolicitudExamenRepository solicitudExamenRepository;

    @Transactional
    public SolicitudLaboratorio crearSolicitud(SolicitudLaboratorio solicitud, List<String> examenes) {
        SolicitudLaboratorio saved = solicitudLaboratorioRepository.save(solicitud);
        if (examenes != null) {
            for (String examen : examenes) {
                SolicitudExamen se = SolicitudExamen.builder()
                    .solicitudId(saved.getId())
                    .tipoExamen(examen)
                    .build();
                solicitudExamenRepository.save(se);
            }
        }
        log.info("Solicitud de laboratorio creada ID: {}", saved.getId());
        return saved;
    }

    public List<SolicitudLaboratorio> obtenerPorPaciente(String idPacienteRegional) {
        return solicitudLaboratorioRepository.findByIdPacienteRegionalOrderByFechaSolicitudDesc(idPacienteRegional);
    }

    public List<SolicitudLaboratorio> obtenerPorEstado(String estado) {
        return solicitudLaboratorioRepository.findByEstadoOrderByFechaSolicitudDesc(estado);
    }

    public List<SolicitudExamen> obtenerExamenesPorSolicitud(Long solicitudId) {
        return solicitudExamenRepository.findBySolicitudId(solicitudId);
    }

    @Transactional
    public SolicitudLaboratorio actualizarEstado(Long id, String estado) {
        SolicitudLaboratorio solicitud = solicitudLaboratorioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + id));
        solicitud.setEstado(estado);
        return solicitudLaboratorioRepository.save(solicitud);
    }
}
