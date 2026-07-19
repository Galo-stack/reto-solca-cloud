package com.solca.repositorio.service;

import com.solca.repositorio.model.RepositorioResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepositorioService {

    @Qualifier("pacientesWebClient")
    private final WebClient pacientesWebClient;

    @Qualifier("consultasWebClient")
    private final WebClient consultasWebClient;

    @Qualifier("laboratorioWebClient")
    private final WebClient laboratorioWebClient;

    @Qualifier("imagenologiaWebClient")
    private final WebClient imagenologiaWebClient;

    @Qualifier("auditoriaWebClient")
    private final WebClient auditoriaWebClient;

    private final ConsultaCacheService consultaCacheService;

    public RepositorioResponse obtenerInformacionConsolidada(
            String idPacienteRegional, String username, String rol,
            String nombres, String apellidos, String ip) {
        log.info("Consultando informacion consolidada para paciente: {}", idPacienteRegional);

        RepositorioResponse response = RepositorioResponse.builder()
                .status("COMPLETE")
                .errores(new HashMap<>())
                .build();

        // Capture request context for async threads (JWT forwarding)
        RequestAttributes requestAttrs = RequestContextHolder.getRequestAttributes();

        // Parallel async calls to all microservices
        CompletableFuture<Map<String, Object>> pacienteFuture =
                CompletableFuture.supplyAsync(() -> {
                    RequestContextHolder.setRequestAttributes(requestAttrs);
                    try { return obtenerDatosPaciente(idPacienteRegional); }
                    catch (Exception e) {
                        log.error("Error al obtener paciente: {}", e.getMessage());
                        return null;
                    }
                });

        CompletableFuture<List<Map<String, Object>>> consultasFuture =
                CompletableFuture.supplyAsync(() -> {
                    RequestContextHolder.setRequestAttributes(requestAttrs);
                    try { return obtenerConsultas(idPacienteRegional); }
                    catch (Exception e) {
                        log.error("Error al obtener consultas: {}", e.getMessage());
                        return null;
                    }
                });

        CompletableFuture<List<Map<String, Object>>> laboratorioFuture =
                CompletableFuture.supplyAsync(() -> {
                    RequestContextHolder.setRequestAttributes(requestAttrs);
                    try { return obtenerLaboratorio(idPacienteRegional); }
                    catch (Exception e) {
                        log.error("Error al obtener laboratorio: {}", e.getMessage());
                        return null;
                    }
                });

        CompletableFuture<List<Map<String, Object>>> imagenesFuture =
                CompletableFuture.supplyAsync(() -> {
                    RequestContextHolder.setRequestAttributes(requestAttrs);
                    try { return obtenerImagenes(idPacienteRegional); }
                    catch (Exception e) {
                        log.error("Error al obtener imagenes: {}", e.getMessage());
                        return null;
                    }
                });

        // Wait for all to complete
        CompletableFuture.allOf(pacienteFuture, consultasFuture, laboratorioFuture, imagenesFuture).join();

        // Process results
        try {
            Map<String, Object> paciente = pacienteFuture.get();
            response.setPaciente(paciente);
        } catch (Exception e) {
            response.setStatus("PARTIAL");
            response.getErrores().put("paciente", "SERVICIO NO DISPONIBLE: " + e.getMessage());
        }

        try {
            List<Map<String, Object>> consultas = consultasFuture.get();
            response.setConsultas(consultas != null ? consultas : new ArrayList<>());
        } catch (Exception e) {
            response.setConsultas(new ArrayList<>());
            response.setStatus("PARTIAL");
            response.getErrores().put("consultas", "SERVICIO NO DISPONIBLE: " + e.getMessage());
        }

        try {
            List<Map<String, Object>> laboratorio = laboratorioFuture.get();
            response.setLaboratorio(laboratorio != null ? laboratorio : new ArrayList<>());
        } catch (Exception e) {
            response.setLaboratorio(new ArrayList<>());
            response.setStatus("PARTIAL");
            response.getErrores().put("laboratorio", "SERVICIO NO DISPONIBLE: " + e.getMessage());
        }

        try {
            List<Map<String, Object>> imagenes = imagenesFuture.get();
            response.setImagenes(imagenes != null ? imagenes : new ArrayList<>());
        } catch (Exception e) {
            response.setImagenes(new ArrayList<>());
            response.setStatus("PARTIAL");
            response.getErrores().put("imagenes", "SERVICIO NO DISPONIBLE: " + e.getMessage());
        }

        // Asynchronous audit logging (fire-and-forget)
        String nombreCompleto = (nombres != null ? nombres : "") + " " + (apellidos != null ? apellidos : "");
        registrarAuditoria(username, rol, nombreCompleto.trim(), ip,
                idPacienteRegional, response.getStatus());

        // Save to local database
        try {
            Map<String, Object> p = response.getPaciente();
            String cedula = p != null ? (String) p.getOrDefault("cedula", "") : "";
            String pNombres = p != null ? (String) p.getOrDefault("nombres", "") : "";
            String pApellidos = p != null ? (String) p.getOrDefault("apellidos", "") : "";
            String estado = response.getStatus();
            consultaCacheService.registrarConsulta(
                idPacienteRegional, pNombres, pApellidos, cedula,
                username, rol, estado,
                "Consulta de historia clinica consolidada", null
            );
        } catch (Exception e) {
            log.warn("Error al guardar en cache local: {}", e.getMessage());
        }

        return response;
    }

    private void registrarAuditoria(String username, String rol, String nombreCompleto,
                                     String ip, String idPacienteRegional, String resultado) {
        try {
            Map<String, Object> auditBody = new HashMap<>();
            auditBody.put("usuario", username);
            auditBody.put("nombreCompleto", nombreCompleto);
            auditBody.put("rol", rol);
            auditBody.put("direccionIp", ip);
            auditBody.put("modulo", "REPOSITORIO");
            auditBody.put("submodulo", "Historia Clinica");
            auditBody.put("accion", "CONSULTA DE HISTORIA CLINICA");
            auditBody.put("detalle", "Consulta de historia clinica consolidada del paciente: " + idPacienteRegional);
            auditBody.put("pacienteRelacionado", idPacienteRegional);
            auditBody.put("resultado", resultado);
            auditBody.put("nivelCriticidad", "BAJO");
            auditBody.put("fecha", LocalDateTime.now().toString());

            auditoriaWebClient.post()
                    .uri("/auditorias")
                    .bodyValue(auditBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .subscribe(
                            success -> log.info("Auditoria registrada exitosamente"),
                            error -> log.warn("No se pudo registrar auditoria: {}", error.getMessage())
                    );

            log.info("Auditoria enviada para consulta de paciente: {}", idPacienteRegional);
        } catch (Exception e) {
            log.warn("Error al enviar auditoria (no bloqueante): {}", e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> obtenerDatosPaciente(String idPacienteRegional) {
        try {
            Map<String, Object> wrapped = pacientesWebClient.get()
                    .uri("/pacientes/{id}", idPacienteRegional)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), response -> {
                        log.warn("Paciente no encontrado: {}", idPacienteRegional);
                        return Mono.empty();
                    })
                    .bodyToMono(Map.class)
                    .block();
            return wrapped != null ? (Map<String, Object>) wrapped.get("data") : null;
        } catch (WebClientResponseException e) {
            log.error("Error HTTP en servicio de pacientes: {} - {}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Error HTTP al obtener paciente: " + e.getStatusCode(), e);
        } catch (WebClientRequestException e) {
            log.error("Error de conexion en servicio de pacientes: {}", e.getMessage());
            throw new RuntimeException("Servicio de pacientes no disponible (timeout/conexion)", e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> obtenerLista(WebClient client, String uri) {
        try {
            Map<String, Object> wrapped = client.get()
                    .uri(uri)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), response -> {
                        log.warn("No se encontraron datos para: {}", uri);
                        return Mono.empty();
                    })
                    .bodyToMono(Map.class)
                    .block();
            Object data = wrapped != null ? wrapped.get("data") : null;
            return data != null ? (List<Map<String, Object>>) data : new ArrayList<>();
        } catch (WebClientResponseException e) {
            log.error("Error HTTP en {} - {}: {}", uri, e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Error HTTP en " + uri + ": " + e.getStatusCode(), e);
        } catch (WebClientRequestException e) {
            log.error("Error de conexion en {}: {}", uri, e.getMessage());
            throw new RuntimeException("Servicio no disponible en " + uri + " (timeout/conexion)", e);
        }
    }

    private List<Map<String, Object>> obtenerConsultas(String idPacienteRegional) {
        return obtenerLista(consultasWebClient, "/consultas/paciente/" + idPacienteRegional);
    }

    private List<Map<String, Object>> obtenerLaboratorio(String idPacienteRegional) {
        return obtenerLista(laboratorioWebClient, "/laboratorio/paciente/" + idPacienteRegional);
    }

    private List<Map<String, Object>> obtenerImagenes(String idPacienteRegional) {
        return obtenerLista(imagenologiaWebClient, "/imagenes/paciente/" + idPacienteRegional);
    }
}
