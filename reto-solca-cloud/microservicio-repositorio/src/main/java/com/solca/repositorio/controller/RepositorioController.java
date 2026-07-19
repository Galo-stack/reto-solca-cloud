package com.solca.repositorio.controller;

import com.solca.repositorio.model.ConsultaRepositorio;
import com.solca.repositorio.model.RepositorioResponse;
import com.solca.repositorio.service.ConsultaCacheService;
import com.solca.repositorio.service.RepositorioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/repositorio")
@RequiredArgsConstructor
@Slf4j
public class RepositorioController {
    private final RepositorioService repositorioService;
    private final ConsultaCacheService consultaCacheService;

    @GetMapping("/paciente/{idPacienteRegional}")
    public ResponseEntity<RepositorioResponse> consultarPaciente(
            @PathVariable String idPacienteRegional,
            HttpServletRequest request) {
        log.info("Consulta al repositorio para paciente: {}", idPacienteRegional);

        String username = (String) request.getAttribute("jwtUsername");
        String rol = (String) request.getAttribute("jwtRol");
        String nombres = (String) request.getAttribute("jwtNombres");
        String apellidos = (String) request.getAttribute("jwtApellidos");
        String ip = request.getRemoteAddr();

        RepositorioResponse response = repositorioService.obtenerInformacionConsolidada(
                idPacienteRegional, username, rol, nombres, apellidos, ip);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<ConsultaRepositorio>> obtenerRecientes() {
        log.info("Obteniendo consultas recientes del repositorio local");
        return ResponseEntity.ok(consultaCacheService.obtenerRecientes());
    }
}
