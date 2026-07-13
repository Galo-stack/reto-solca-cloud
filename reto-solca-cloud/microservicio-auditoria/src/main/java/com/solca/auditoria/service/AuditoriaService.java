package com.solca.auditoria.service;

import com.solca.auditoria.dto.AuditoriaDTO;
import com.solca.auditoria.model.Auditoria;
import com.solca.auditoria.repository.AuditoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    public Auditoria registrarAuditoria(AuditoriaDTO dto) {
        Auditoria auditoria = Auditoria.builder()
            .fecha(LocalDateTime.now())
            .hora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
            .usuario(dto.getUsuario())
            .nombreCompleto(dto.getNombreCompleto() != null ? dto.getNombreCompleto() : "")
            .rol(dto.getRol())
            .idUsuario(dto.getIdUsuario())
            .direccionIp(dto.getDireccionIp())
            .dispositivo(dto.getDispositivo() != null ? dto.getDispositivo() : "")
            .navegador(dto.getNavegador() != null ? dto.getNavegador() : "")
            .sistemaOperativo(dto.getSistemaOperativo() != null ? dto.getSistemaOperativo() : "")
            .modulo(dto.getModulo())
            .submodulo(dto.getSubmodulo() != null ? dto.getSubmodulo() : "")
            .accion(dto.getAccion())
            .detalle(dto.getDetalle())
            .valorAnterior(dto.getValorAnterior())
            .valorNuevo(dto.getValorNuevo())
            .pacienteRelacionado(dto.getPacienteRelacionado() != null ? dto.getPacienteRelacionado() : "")
            .historiaClinica(dto.getHistoriaClinica() != null ? dto.getHistoriaClinica() : "")
            .numeroConsulta(dto.getNumeroConsulta() != null ? dto.getNumeroConsulta() : "")
            .tipoRegistro(dto.getTipoRegistro() != null ? dto.getTipoRegistro() : "")
            .estadoRegistro(dto.getEstadoRegistro() != null ? dto.getEstadoRegistro() : "")
            .resultado(dto.getResultado() != null ? dto.getResultado() : "EXITO")
            .codigoHttp(dto.getCodigoHttp() != null ? dto.getCodigoHttp() : 200)
            .tiempoEjecucionMs(dto.getTiempoEjecucionMs() != null ? dto.getTiempoEjecucionMs() : 0L)
            .sesionUsuario(dto.getSesionUsuario() != null ? dto.getSesionUsuario() : "")
            .uuidEvento(dto.getUuidEvento() != null ? dto.getUuidEvento() : UUID.randomUUID().toString())
            .nivelCriticidad(dto.getNivelCriticidad() != null ? dto.getNivelCriticidad() : "BAJO")
            .idReferencia(dto.getIdReferencia())
            .tipoReferencia(dto.getTipoReferencia())
            .build();
        return auditoriaRepository.save(auditoria);
    }

    public Page<Auditoria> listarAuditorias(int page, int size) {
        return auditoriaRepository.findByOrderByFechaDesc(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fecha")));
    }

    public Page<Auditoria> listarPorUsuario(String usuario, int page, int size) {
        return auditoriaRepository.findByUsuarioOrderByFechaDesc(usuario, PageRequest.of(page, size));
    }

    public Page<Auditoria> listarPorModulo(String modulo, int page, int size) {
        return auditoriaRepository.findByModuloOrderByFechaDesc(modulo, PageRequest.of(page, size));
    }

    public Page<Auditoria> listarPorAccion(String accion, int page, int size) {
        return auditoriaRepository.findByAccionOrderByFechaDesc(accion, PageRequest.of(page, size));
    }

    public Page<Auditoria> listarPorCriticidad(String criticidad, int page, int size) {
        return auditoriaRepository.findByNivelCriticidadOrderByFechaDesc(criticidad, PageRequest.of(page, size));
    }

    public Page<Auditoria> listarPorPaciente(String paciente, int page, int size) {
        return auditoriaRepository.findByPacienteRelacionadoOrderByFechaDesc(paciente, PageRequest.of(page, size));
    }

    public Page<Auditoria> listarPorRol(String rol, int page, int size) {
        return auditoriaRepository.findByRolOrderByFechaDesc(rol, PageRequest.of(page, size));
    }

    public Page<Auditoria> listarPorSubmodulo(String submodulo, int page, int size) {
        return auditoriaRepository.findBySubmoduloOrderByFechaDesc(submodulo, PageRequest.of(page, size));
    }

    public Page<Auditoria> listarPorResultado(String resultado, int page, int size) {
        return auditoriaRepository.findByResultadoOrderByFechaDesc(resultado, PageRequest.of(page, size));
    }

    public Page<Auditoria> listarPorIp(String ip, int page, int size) {
        return auditoriaRepository.findByDireccionIpOrderByFechaDesc(ip, PageRequest.of(page, size));
    }

    public Page<Auditoria> listarPorFecha(LocalDate fecha, int page, int size) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        return auditoriaRepository.findByFechaBetweenOrderByFechaDesc(inicio, fin, PageRequest.of(page, size));
    }

    public Page<Auditoria> listarPorRangoFechas(LocalDate desde, LocalDate hasta, int page, int size) {
        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime fin = hasta.atTime(LocalTime.MAX);
        return auditoriaRepository.findByFechaBetweenOrderByFechaDesc(inicio, fin, PageRequest.of(page, size));
    }

    public Page<Auditoria> searchAudits(String usuario, String rol, String modulo, String accion,
                                          String nivelCriticidad, String resultado, String paciente,
                                          String direccionIp, LocalDate desde, LocalDate hasta, Pageable pageable) {
        LocalDateTime desdeDt = desde != null ? desde.atStartOfDay() : null;
        LocalDateTime hastaDt = hasta != null ? hasta.atTime(LocalTime.MAX) : null;
        return auditoriaRepository.searchAudits(usuario, rol, modulo, accion, nivelCriticidad,
            resultado, paciente, direccionIp, desdeDt, hastaDt, pageable);
    }

    public long contarAuditorias() {
        return auditoriaRepository.count();
    }

    public Map<String, Long> contarPorModulo() {
        Map<String, Long> result = new HashMap<>();
        List<Object[]> data = auditoriaRepository.countByModulo();
        for (Object[] row : data) result.put((String) row[0], (Long) row[1]);
        return result;
    }

    public Map<String, Long> contarPorAccion() {
        Map<String, Long> result = new HashMap<>();
        List<Object[]> data = auditoriaRepository.countByAccion();
        for (Object[] row : data) result.put((String) row[0], (Long) row[1]);
        return result;
    }

    public Map<String, Long> contarPorDia() {
        Map<String, Long> result = new HashMap<>();
        List<Object[]> data = auditoriaRepository.countByDay();
        for (Object[] row : data) result.put(String.valueOf(row[0]), (Long) row[1]);
        return result;
    }

    public Auditoria obtenerPorId(Long id) {
        return auditoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registro de auditoria no encontrado"));
    }
}