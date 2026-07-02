package com.solca.repositorio.service;

import com.solca.repositorio.model.RepositorioResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.*;

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

    public RepositorioResponse obtenerInformacionConsolidada(String idPacienteRegional) {
        log.info("Consultando información consolidada para paciente: {}", idPacienteRegional);

        RepositorioResponse response = RepositorioResponse.builder()
                .status("COMPLETE")
                .errores(new HashMap<>())
                .build();

        // 1. Obtener paciente
        try {
            Map<String, Object> paciente = obtenerDatosPaciente(idPacienteRegional);
            response.setPaciente(paciente);
            log.info("Datos del paciente obtenidos exitosamente");
        } catch (Exception e) {
            log.error("Error al obtener paciente: {}", e.getMessage());
            response.setStatus("PARTIAL");
            response.getErrores().put("paciente", "SERVICIO NO DISPONIBLE");
        }

        // 2. Obtener consultas
        try {
            List<Map<String, Object>> consultas = obtenerConsultas(idPacienteRegional);
            response.setConsultas(consultas != null ? consultas : new ArrayList<>());
            log.info("Consultas obtenidas: {}", consultas != null ? consultas.size() : 0);
        } catch (Exception e) {
            log.error("Error al obtener consultas: {}", e.getMessage());
            response.setConsultas(new ArrayList<>());
            response.setStatus("PARTIAL");
            response.getErrores().put("consultas", "SERVICIO NO DISPONIBLE");
        }

        // 3. Obtener laboratorio
        try {
            List<Map<String, Object>> laboratorio = obtenerLaboratorio(idPacienteRegional);
            response.setLaboratorio(laboratorio != null ? laboratorio : new ArrayList<>());
            log.info("Resultados de laboratorio obtenidos: {}", laboratorio != null ? laboratorio.size() : 0);
        } catch (Exception e) {
            log.error("Error al obtener laboratorio: {}", e.getMessage());
            response.setLaboratorio(new ArrayList<>());
            response.setStatus("PARTIAL");
            response.getErrores().put("laboratorio", "SERVICIO NO DISPONIBLE");
        }

        // 4. Obtener imágenes
        try {
            List<Map<String, Object>> imagenes = obtenerImagenes(idPacienteRegional);
            response.setImagenes(imagenes != null ? imagenes : new ArrayList<>());
            log.info("Imágenes obtenidas: {}", imagenes != null ? imagenes.size() : 0);
        } catch (Exception e) {
            log.error("Error al obtener imágenes: {}", e.getMessage());
            response.setImagenes(new ArrayList<>());
            response.setStatus("PARTIAL");
            response.getErrores().put("imagenes", "SERVICIO NO DISPONIBLE");
        }

        return response;
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