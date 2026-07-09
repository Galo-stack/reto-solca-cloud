package com.solca.seguridad.controller;

import com.solca.seguridad.dto.ApiResponse;
import com.solca.seguridad.model.Usuario;
import com.solca.seguridad.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Usuario>>> listarUsuarios() {
        return ResponseEntity.ok(ApiResponse.success(usuarioService.listarUsuarios()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> obtenerUsuario(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success(usuarioService.obtenerUsuario(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        }
    }
}
