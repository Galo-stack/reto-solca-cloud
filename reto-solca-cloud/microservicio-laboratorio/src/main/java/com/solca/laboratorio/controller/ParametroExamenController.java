package com.solca.laboratorio.controller;

import com.solca.laboratorio.model.ParametroExamen;
import com.solca.laboratorio.service.ParametroExamenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/parametros-examen")
@RequiredArgsConstructor
@Slf4j
public class ParametroExamenController {
    private final ParametroExamenService parametroExamenService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        List<ParametroExamen> params = parametroExamenService.listarTodos();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", params);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{tipoExamen}")
    public ResponseEntity<Map<String, Object>> obtenerPorTipo(@PathVariable String tipoExamen) {
        List<ParametroExamen> params = parametroExamenService.obtenerPorTipoExamen(tipoExamen);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", params);
        return ResponseEntity.ok(response);
    }
}
