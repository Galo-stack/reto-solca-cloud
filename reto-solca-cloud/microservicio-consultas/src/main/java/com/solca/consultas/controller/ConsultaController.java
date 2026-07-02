package com.solca.consultas.controller;

import com.solca.consultas.dto.ConsultaDTO;
import com.solca.consultas.service.ConsultaService;
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
@RequestMapping("/consultas")
@RequiredArgsConstructor
@Slf4j
public class ConsultaController {
    private final ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarConsulta(@Valid @RequestBody ConsultaDTO dto) {
        log.info("POST /api/consultas");
        ConsultaDTO result = consultaService.registrarConsulta(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Consulta registrada exitosamente");
        response.put("data", result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    public ResponseEntity<Map<String, Object>> consultarPorPaciente(@PathVariable String idPacienteRegional) {
        log.info("GET /api/consultas/paciente/{}", idPacienteRegional);
        List<ConsultaDTO> consultas = consultaService.obtenerConsultasPorPaciente(idPacienteRegional);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", consultas);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> consultarPorId(@PathVariable Long id) {
        log.info("GET /api/consultas/{}", id);
        ConsultaDTO consulta = consultaService.obtenerConsultaPorId(id);

        Map<String, Object> response = new HashMap<>();
        if (consulta != null) {
            response.put("status", "success");
            response.put("data", consulta);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Consulta no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}