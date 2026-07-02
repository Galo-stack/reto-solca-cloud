package com.solca.pacientes.controller;

import com.solca.pacientes.dto.PacienteDTO;
import com.solca.pacientes.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
@Slf4j
public class PacienteController {
    private final PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarPacientes() {
        log.info("GET /api/pacientes - Listando pacientes");
        var pacientes = pacienteService.listarTodos();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", pacientes);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarPaciente(@Valid @RequestBody PacienteDTO dto) {
        log.info("POST /api/pacientes - Registrando paciente");
        PacienteDTO result = pacienteService.registrarPaciente(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Paciente registrado exitosamente");
        response.put("data", result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Map<String, Object>> buscarPorCedula(@PathVariable String cedula) {
        log.info("GET /api/pacientes/cedula/{}", cedula);
        PacienteDTO paciente = pacienteService.buscarPorCedula(cedula);

        Map<String, Object> response = new HashMap<>();
        if (paciente != null) {
            response.put("status", "success");
            response.put("data", paciente);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Paciente no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable String id) {
        log.info("GET /api/pacientes/{}", id);
        PacienteDTO paciente = pacienteService.buscarPorId(id);

        Map<String, Object> response = new HashMap<>();
        if (paciente != null) {
            response.put("status", "success");
            response.put("data", paciente);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Paciente no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}