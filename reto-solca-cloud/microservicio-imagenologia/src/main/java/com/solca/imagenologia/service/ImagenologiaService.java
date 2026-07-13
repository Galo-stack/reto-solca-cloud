package com.solca.imagenologia.service;

import com.solca.imagenologia.dto.ImagenologiaDTO;
import com.solca.imagenologia.model.EstudioImagen;
import com.solca.imagenologia.repository.ImagenologiaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImagenologiaService {
    private final ImagenologiaRepository imagenologiaRepository;

    @Transactional
    public ImagenologiaDTO registrarEstudio(ImagenologiaDTO dto) {
        log.info("Registrando estudio de imagen para: {}", dto.getIdPacienteRegional());

        EstudioImagen estudio = EstudioImagen.builder()
                .idPacienteRegional(dto.getIdPacienteRegional())
                .sede(dto.getSede())
                .fechaEstudio(dto.getFechaEstudio())
                .tipoEstudio(dto.getTipoEstudio())
                .formato(dto.getFormato())
                .urlArchivo(dto.getUrlArchivo())
                .nombreArchivo(dto.getNombreArchivo())
                .medicoSolicitante(dto.getMedicoSolicitante())
                .modalidad(dto.getModalidad())
                .descripcion(dto.getDescripcion())
                .hallazgos(dto.getHallazgos())
                .tamanoBytes(dto.getTamanoBytes())
                .informeRadiologico(dto.getInformeRadiologico())
                .recomendaciones(dto.getRecomendaciones())
                .tecnicaUtilizada(dto.getTecnicaUtilizada())
                .firmaRadiologo(dto.getFirmaRadiologo() != null ? dto.getFirmaRadiologo() : "")
                .regionAnatomica(dto.getRegionAnatomica())
                .lateralidad(dto.getLateralidad())
                .requiereContraste(dto.getRequiereContraste() != null ? dto.getRequiereContraste() : false)
                .solicitudId(dto.getSolicitudId())
                .estado(dto.getEstado() != null ? dto.getEstado() : "PENDIENTE")
                .activo(true)
                .build();

        EstudioImagen saved = imagenologiaRepository.save(estudio);
        dto.setId(saved.getId());
        log.info("Estudio registrado con ID: {}", saved.getId());
        return dto;
    }

    public List<ImagenologiaDTO> obtenerEstudiosPorPaciente(String idPaciente) {
        log.debug("Obteniendo estudios para paciente: {}", idPaciente);
        return imagenologiaRepository.findByIdPacienteRegionalOrderByFechaEstudioDesc(idPaciente)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ImagenologiaDTO obtenerEstudioPorId(Long id) {
        log.debug("Obteniendo estudio por ID: {}", id);
        return imagenologiaRepository.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    @Transactional
    public ImagenologiaDTO actualizarEstudio(Long id, ImagenologiaDTO dto) {
        log.info("Actualizando estudio de imagen ID: {}", id);

        EstudioImagen estudio = imagenologiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudio no encontrado: " + id));

        estudio.setTipoEstudio(dto.getTipoEstudio());
        estudio.setHallazgos(dto.getHallazgos());
        estudio.setModalidad(dto.getModalidad());
        estudio.setDescripcion(dto.getDescripcion());
        estudio.setFormato(dto.getFormato());
        estudio.setUrlArchivo(dto.getUrlArchivo());
        estudio.setNombreArchivo(dto.getNombreArchivo());
        estudio.setMedicoSolicitante(dto.getMedicoSolicitante());
        estudio.setSede(dto.getSede());
        estudio.setFechaEstudio(dto.getFechaEstudio());
        estudio.setTamanoBytes(dto.getTamanoBytes());
        estudio.setInformeRadiologico(dto.getInformeRadiologico());
        estudio.setRecomendaciones(dto.getRecomendaciones());
        estudio.setTecnicaUtilizada(dto.getTecnicaUtilizada());
        estudio.setFirmaRadiologo(dto.getFirmaRadiologo());
        estudio.setRegionAnatomica(dto.getRegionAnatomica());
        estudio.setLateralidad(dto.getLateralidad());
        estudio.setRequiereContraste(dto.getRequiereContraste());
        estudio.setSolicitudId(dto.getSolicitudId());
        if (dto.getEstado() != null) estudio.setEstado(dto.getEstado());

        EstudioImagen saved = imagenologiaRepository.save(estudio);
        log.info("Estudio de imagen actualizado ID: {}", saved.getId());
        return convertirADTO(saved);
    }

    private ImagenologiaDTO convertirADTO(EstudioImagen e) {
        return ImagenologiaDTO.builder()
                .id(e.getId())
                .idPacienteRegional(e.getIdPacienteRegional())
                .sede(e.getSede())
                .fechaEstudio(e.getFechaEstudio())
                .tipoEstudio(e.getTipoEstudio())
                .formato(e.getFormato())
                .urlArchivo(e.getUrlArchivo())
                .nombreArchivo(e.getNombreArchivo())
                .medicoSolicitante(e.getMedicoSolicitante())
                .modalidad(e.getModalidad())
                .descripcion(e.getDescripcion())
                .hallazgos(e.getHallazgos())
                .tamanoBytes(e.getTamanoBytes())
                .informeRadiologico(e.getInformeRadiologico())
                .recomendaciones(e.getRecomendaciones())
                .tecnicaUtilizada(e.getTecnicaUtilizada())
                .firmaRadiologo(e.getFirmaRadiologo())
                .regionAnatomica(e.getRegionAnatomica())
                .lateralidad(e.getLateralidad())
                .requiereContraste(e.getRequiereContraste())
                .solicitudId(e.getSolicitudId())
                .estado(e.getEstado())
                .build();
    }
}