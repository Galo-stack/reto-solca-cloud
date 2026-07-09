package com.solca.imagenologia.controller;

import com.solca.imagenologia.dto.ImagenologiaDTO;
import com.solca.imagenologia.service.ImagenologiaService;
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
@RequestMapping("/imagenes")
@RequiredArgsConstructor
@Slf4j
public class ImagenologiaController {
    private final ImagenologiaService imagenologiaService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarEstudio(@Valid @RequestBody ImagenologiaDTO dto) {
        log.info("POST /api/imagenes");
        ImagenologiaDTO result = imagenologiaService.registrarEstudio(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Estudio registrado exitosamente");
        response.put("data", result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    public ResponseEntity<Map<String, Object>> consultarPorPaciente(@PathVariable String idPacienteRegional) {
        log.info("GET /api/imagenes/paciente/{}", idPacienteRegional);
        List<ImagenologiaDTO> estudios = imagenologiaService.obtenerEstudiosPorPaciente(idPacienteRegional);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", estudios);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> consultarPorId(@PathVariable Long id) {
        log.info("GET /api/imagenes/{}", id);
        ImagenologiaDTO estudio = imagenologiaService.obtenerEstudioPorId(id);

        Map<String, Object> response = new HashMap<>();
        if (estudio != null) {
            response.put("status", "success");
            response.put("data", estudio);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Estudio no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarEstudio(@PathVariable Long id, @Valid @RequestBody ImagenologiaDTO dto) {
        log.info("PUT /api/imagenes/{} - Actualizando estudio", id);
        ImagenologiaDTO result = imagenologiaService.actualizarEstudio(id, dto);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Estudio de imagenologia actualizado exitosamente");
        response.put("data", result);

        return ResponseEntity.ok(response);
    }
}