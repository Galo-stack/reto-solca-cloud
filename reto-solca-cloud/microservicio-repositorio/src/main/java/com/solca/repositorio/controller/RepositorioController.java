package com.solca.repositorio.controller;

import com.solca.repositorio.model.RepositorioResponse;
import com.solca.repositorio.service.RepositorioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repositorio")
@RequiredArgsConstructor
@Slf4j
public class RepositorioController {
    private final RepositorioService repositorioService;

    @GetMapping("/paciente/{idPacienteRegional}")
    public ResponseEntity<RepositorioResponse> consultarPaciente(
            @PathVariable String idPacienteRegional) {
        log.info("Consulta al repositorio para paciente: {}", idPacienteRegional);

        RepositorioResponse response = repositorioService.obtenerInformacionConsolidada(idPacienteRegional);
        return ResponseEntity.ok(response);
    }
}