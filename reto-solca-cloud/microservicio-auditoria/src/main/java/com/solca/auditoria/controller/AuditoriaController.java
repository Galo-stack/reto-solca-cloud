package com.solca.auditoria.controller;

import com.solca.auditoria.dto.ApiResponse;
import com.solca.auditoria.dto.AuditoriaDTO;
import com.solca.auditoria.model.Auditoria;
import com.solca.auditoria.service.AuditoriaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<ApiResponse<Page<Auditoria>>> listarPorUsuario(
            @PathVariable String usuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorUsuario(usuario, page, size)));
    }

    @GetMapping("/modulo/{modulo}")
    public ResponseEntity<ApiResponse<Page<Auditoria>>> listarPorModulo(
            @PathVariable String modulo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorModulo(modulo, page, size)));
    }

    @GetMapping("/accion/{accion}")
    public ResponseEntity<ApiResponse<Page<Auditoria>>> listarPorAccion(
            @PathVariable String accion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorAccion(accion, page, size)));
    }

    @GetMapping("/criticidad/{criticidad}")
    public ResponseEntity<ApiResponse<Page<Auditoria>>> listarPorCriticidad(
            @PathVariable String criticidad,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorCriticidad(criticidad, page, size)));
    }

    @GetMapping("/paciente/{paciente}")
    public ResponseEntity<ApiResponse<Page<Auditoria>>> listarPorPaciente(
            @PathVariable String paciente,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorPaciente(paciente, page, size)));
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<ApiResponse<Page<Auditoria>>> listarPorRol(
            @PathVariable String rol,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorRol(rol, page, size)));
    }

    @GetMapping("/submodulo/{submodulo}")
    public ResponseEntity<ApiResponse<Page<Auditoria>>> listarPorSubmodulo(
            @PathVariable String submodulo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorSubmodulo(submodulo, page, size)));
    }

    @GetMapping("/resultado/{resultado}")
    public ResponseEntity<ApiResponse<Page<Auditoria>>> listarPorResultado(
            @PathVariable String resultado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorResultado(resultado, page, size)));
    }

    @GetMapping("/ip/{ip}")
    public ResponseEntity<ApiResponse<Page<Auditoria>>> listarPorIp(
            @PathVariable String ip,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorIp(ip, page, size)));
    }

    @GetMapping("/fecha")
    public ResponseEntity<ApiResponse<Page<Auditoria>>> listarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorFecha(fecha, page, size)));
    }

    @GetMapping("/rango")
    public ResponseEntity<ApiResponse<Page<Auditoria>>> listarPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(auditoriaService.listarPorRangoFechas(desde, hasta, page, size)));
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<Page<Auditoria>>> buscar(
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) String modulo,
            @RequestParam(required = false) String accion,
            @RequestParam(required = false) String criticidad,
            @RequestParam(required = false) String resultado,
            @RequestParam(required = false) String paciente,
            @RequestParam(required = false) String ip,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(
            auditoriaService.searchAudits(usuario, rol, modulo, accion, criticidad, resultado, paciente, ip, desde, hasta, pageable)));
    }

    @GetMapping("/contar")
    public ResponseEntity<ApiResponse<Map<String, Object>>> contar() {
        Map<String, Object> stats = Map.of(
            "total", auditoriaService.contarAuditorias(),
            "porModulo", auditoriaService.contarPorModulo(),
            "porAccion", auditoriaService.contarPorAccion(),
            "porDia", auditoriaService.contarPorDia()
        );
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}