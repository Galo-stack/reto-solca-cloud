package com.solca.imagenologia.controller;

import com.solca.imagenologia.model.SolicitudImagen;
import com.solca.imagenologia.service.SolicitudImagenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/solicitudes-imagen")
@RequiredArgsConstructor
@Slf4j
public class SolicitudImagenController {
    private final SolicitudImagenService solicitudImagenService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearSolicitud(@RequestBody SolicitudImagen solicitud) {
        log.info("POST /api/solicitudes-imagen");
        SolicitudImagen saved = solicitudImagenService.crearSolicitud(solicitud);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Solicitud de imagen creada exitosamente");
        response.put("data", saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    public ResponseEntity<Map<String, Object>> obtenerPorPaciente(@PathVariable String idPacienteRegional) {
        List<SolicitudImagen> solicitudes = solicitudImagenService.obtenerPorPaciente(idPacienteRegional);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", solicitudes);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String estado = body.get("estado");
        SolicitudImagen updated = solicitudImagenService.actualizarEstado(id, estado);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Estado actualizado");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }
}
