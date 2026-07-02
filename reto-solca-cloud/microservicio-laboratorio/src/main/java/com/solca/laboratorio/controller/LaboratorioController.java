package com.solca.laboratorio.controller;

import com.solca.laboratorio.dto.LaboratorioDTO;
import com.solca.laboratorio.service.LaboratorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/laboratorio")
@RequiredArgsConstructor
@Slf4j
public class LaboratorioController {
    private final LaboratorioService laboratorioService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarResultado(@Valid @RequestBody LaboratorioDTO dto) {
        log.info("POST /api/laboratorio");
        LaboratorioDTO result = laboratorioService.registrarResultado(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Resultado registrado exitosamente");
        response.put("data", result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    public ResponseEntity<Map<String, Object>> consultarPorPaciente(@PathVariable String idPacienteRegional) {
        log.info("GET /api/laboratorio/paciente/{}", idPacienteRegional);
        List<LaboratorioDTO> resultados = laboratorioService.obtenerResultadosPorPaciente(idPacienteRegional);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", resultados);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> consultarPorId(@PathVariable Long id) {
        log.info("GET /api/laboratorio/{}", id);
        LaboratorioDTO resultado = laboratorioService.obtenerResultadoPorId(id);

        Map<String, Object> response = new HashMap<>();
        if (resultado != null) {
            response.put("status", "success");
            response.put("data", resultado);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Resultado no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}