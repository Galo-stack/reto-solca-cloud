package com.solca.auditoria.service;

import com.solca.auditoria.dto.AuditoriaDTO;
import com.solca.auditoria.model.Auditoria;
import com.solca.auditoria.repository.AuditoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    public Auditoria registrarAuditoria(AuditoriaDTO dto) {
        Auditoria auditoria = new Auditoria();
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        auditoria.setUsuario(dto.getUsuario());
        auditoria.setRol(dto.getRol());
        auditoria.setDireccionIp(dto.getDireccionIp());
        auditoria.setModulo(dto.getModulo());
        auditoria.setAccion(dto.getAccion());
        auditoria.setDetalle(dto.getDetalle());
        auditoria.setResultado(dto.getResultado() != null ? dto.getResultado() : "EXITO");
        auditoria.setIdReferencia(dto.getIdReferencia());
        auditoria.setTipoReferencia(dto.getTipoReferencia());
        return auditoriaRepository.save(auditoria);
    }

    public Page<Auditoria> listarAuditorias(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditoriaRepository.findByOrderByFechaDesc(pageable);
    }

    public List<Auditoria> listarPorUsuario(String usuario) {
        return auditoriaRepository.findByUsuarioOrderByFechaDesc(usuario);
    }

    public List<Auditoria> listarPorModulo(String modulo) {
        return auditoriaRepository.findByModuloOrderByFechaDesc(modulo);
    }

    public List<Auditoria> listarPorAccion(String accion) {
        return auditoriaRepository.findByAccionOrderByFechaDesc(accion);
    }

    public List<Auditoria> listarPorFecha(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        return auditoriaRepository.findByFechaBetweenOrderByFechaDesc(inicio, fin);
    }

    public Page<Auditoria> listarPorModulo(String modulo, int page, int size) {
        return auditoriaRepository.findByModuloOrderByFechaDesc(modulo, PageRequest.of(page, size));
    }

    public long contarAuditorias() {
        return auditoriaRepository.count();
    }

    public Auditoria obtenerPorId(Long id) {
        return auditoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registro de auditoria no encontrado"));
    }
}
