package com.solca.auditoria.controller;

import com.solca.auditoria.dto.ApiResponse;
import com.solca.auditoria.dto.AuditoriaDTO;
import com.solca.auditoria.model.Auditoria;
import com.solca.auditoria.service.AuditoriaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auditorias")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Auditoria>> registrar(@Valid @RequestBody AuditoriaDTO dto) {
        Auditoria auditoria = auditoriaService.registrarAuditoria(dto);
        return ResponseEntity.ok(ApiResponse.success("Auditoria registrada", auditoria));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Auditoria>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarAuditorias(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Auditoria>> obtener(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success(auditoriaService.obtenerPorId(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<ApiResponse<List<Auditoria>>> listarPorUsuario(@PathVariable String usuario) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorUsuario(usuario)));
    }

    @GetMapping("/modulo/{modulo}")
    public ResponseEntity<ApiResponse<?>> listarPorModulo(
            @PathVariable String modulo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorModulo(modulo, page, size)));
    }

    @GetMapping("/accion/{accion}")
    public ResponseEntity<ApiResponse<List<Auditoria>>> listarPorAccion(@PathVariable String accion) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorAccion(accion)));
    }

    @GetMapping("/fecha")
    public ResponseEntity<ApiResponse<List<Auditoria>>> listarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorFecha(fecha)));
    }

    @GetMapping("/contar")
    public ResponseEntity<ApiResponse<Map<String, Long>>> contar() {
        return ResponseEntity.ok(ApiResponse.success(Map.of("total", auditoriaService.contarAuditorias())));
    }
}
