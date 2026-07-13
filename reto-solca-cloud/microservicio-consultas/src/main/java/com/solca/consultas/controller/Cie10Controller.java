package com.solca.consultas.controller;

import com.solca.consultas.model.Cie10;
import com.solca.consultas.service.Cie10Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cie10")
@RequiredArgsConstructor
@Slf4j
public class Cie10Controller {
    private final Cie10Service cie10Service;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        List<Cie10> codigos = cie10Service.listarTodos();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", codigos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Map<String, Object>> buscarPorCodigo(@PathVariable String codigo) {
        List<Cie10> resultados = cie10Service.buscarPorCodigo(codigo);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", resultados);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/descripcion/{descripcion}")
    public ResponseEntity<Map<String, Object>> buscarPorDescripcion(@PathVariable String descripcion) {
        List<Cie10> resultados = cie10Service.buscarPorDescripcion(descripcion);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", resultados);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/capitulo/{capitulo}")
    public ResponseEntity<Map<String, Object>> buscarPorCapitulo(@PathVariable String capitulo) {
        List<Cie10> resultados = cie10Service.buscarPorCapitulo(capitulo);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", resultados);
        return ResponseEntity.ok(response);
    }
}
