package com.solca.imagenologia.service;

import com.solca.imagenologia.model.SolicitudImagen;
import com.solca.imagenologia.repository.SolicitudImagenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolicitudImagenService {
    private final SolicitudImagenRepository solicitudImagenRepository;

    @Transactional
    public SolicitudImagen crearSolicitud(SolicitudImagen solicitud) {
        SolicitudImagen saved = solicitudImagenRepository.save(solicitud);
        log.info("Solicitud de imagen creada ID: {}", saved.getId());
        return saved;
    }

    public List<SolicitudImagen> obtenerPorPaciente(String idPacienteRegional) {
        return solicitudImagenRepository.findByIdPacienteRegionalOrderByFechaSolicitudDesc(idPacienteRegional);
    }

    public List<SolicitudImagen> obtenerPorEstado(String estado) {
        return solicitudImagenRepository.findByEstadoOrderByFechaSolicitudDesc(estado);
    }

    @Transactional
    public SolicitudImagen actualizarEstado(Long id, String estado) {
        SolicitudImagen solicitud = solicitudImagenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + id));
        solicitud.setEstado(estado);
        return solicitudImagenRepository.save(solicitud);
    }
}
