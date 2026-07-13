package com.solca.laboratorio.controller;

import com.solca.laboratorio.model.SolicitudExamen;
import com.solca.laboratorio.model.SolicitudLaboratorio;
import com.solca.laboratorio.service.SolicitudLaboratorioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/solicitudes-laboratorio")
@RequiredArgsConstructor
@Slf4j
public class SolicitudLaboratorioController {
    private final SolicitudLaboratorioService solicitudLaboratorioService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearSolicitud(@RequestBody Map<String, Object> body) {
        log.info("POST /api/solicitudes-laboratorio");
        String idPacienteRegional = (String) body.get("idPacienteRegional");
        String sede = (String) body.get("sede");
        String prioridad = (String) body.getOrDefault("prioridad", "NORMAL");
        String motivo = (String) body.get("motivo");
        String codigoCie10 = (String) body.get("codigoCie10");
        String medicoSolicitante = (String) body.get("medicoSolicitante");
        String especialidad = (String) body.get("especialidad");
        @SuppressWarnings("unchecked")
        List<String> examenes = (List<String>) body.get("examenes");

        SolicitudLaboratorio solicitud = SolicitudLaboratorio.builder()
            .idPacienteRegional(idPacienteRegional)
            .sede(sede)
            .prioridad(prioridad)
            .motivo(motivo)
            .codigoCie10(codigoCie10)
            .medicoSolicitante(medicoSolicitante)
            .especialidad(especialidad)
            .build();

        SolicitudLaboratorio saved = solicitudLaboratorioService.crearSolicitud(solicitud, examenes);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Solicitud creada exitosamente");
        response.put("data", saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    public ResponseEntity<Map<String, Object>> obtenerPorPaciente(@PathVariable String idPacienteRegional) {
        List<SolicitudLaboratorio> solicitudes = solicitudLaboratorioService.obtenerPorPaciente(idPacienteRegional);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", solicitudes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/examenes")
    public ResponseEntity<Map<String, Object>> obtenerExamenes(@PathVariable Long id) {
        List<SolicitudExamen> examenes = solicitudLaboratorioService.obtenerExamenesPorSolicitud(id);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", examenes);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String estado = body.get("estado");
        SolicitudLaboratorio updated = solicitudLaboratorioService.actualizarEstado(id, estado);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Estado actualizado");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }
}
