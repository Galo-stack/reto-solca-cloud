package com.solca.repositorio.service;

import com.solca.repositorio.model.ConsultaRepositorio;
import com.solca.repositorio.repository.ConsultaRepositorioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultaCacheService {

    private final ConsultaRepositorioRepository repository;

    public void registrarConsulta(
            String idPacienteRegional,
            String nombresPaciente,
            String apellidosPaciente,
            String cedulaPaciente,
            String consultadoPor,
            String rolUsuario,
            String estado,
            String detalle,
            Long tiempoRespuestaMs
    ) {
        try {
            ConsultaRepositorio c = new ConsultaRepositorio();
            c.setIdPacienteRegional(idPacienteRegional);
            c.setNombresPaciente(nombresPaciente);
            c.setApellidosPaciente(apellidosPaciente);
            c.setCedulaPaciente(cedulaPaciente);
            c.setConsultadoPor(consultadoPor);
            c.setRolUsuario(rolUsuario);
            c.setEstado(estado);
            c.setDetalle(detalle);
            c.setFechaConsulta(LocalDateTime.now());
            c.setTiempoRespuestaMs(tiempoRespuestaMs);
            repository.save(c);
            log.info("Consulta registrada: {} por {} con estado {}", idPacienteRegional, consultadoPor, estado);
        } catch (Exception e) {
            log.error("Error al registrar consulta en BD: {}", e.getMessage());
        }
    }

    public List<ConsultaRepositorio> obtenerRecientes() {
        return repository.findTop20ByOrderByFechaConsultaDesc();
    }

    public Map<String, Object> obtenerResumen() {
        long total = repository.count();
        return Map.of(
            "totalConsultas", total,
            "mensaje", "Repositorio Clinico Regional - Base de datos operativa"
        );
    }
}
