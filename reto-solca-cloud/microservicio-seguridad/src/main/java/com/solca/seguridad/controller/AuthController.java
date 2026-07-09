package com.solca.seguridad.controller;

import com.solca.seguridad.dto.ApiResponse;
import com.solca.seguridad.dto.LoginRequest;
import com.solca.seguridad.dto.LoginResponse;
import com.solca.seguridad.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = usuarioService.autenticar(request);
            return ResponseEntity.ok(ApiResponse.success("Inicio de sesion exitoso", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/verificar")
    public ResponseEntity<ApiResponse<String>> verificarToken() {
        return ResponseEntity.ok(ApiResponse.success("Token valido", "OK"));
    }
}
