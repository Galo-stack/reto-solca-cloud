package com.solca.repositorio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositorioResponse {
    private Map<String, Object> paciente;
    private List<Map<String, Object>> consultas;
    private List<Map<String, Object>> laboratorio;
    private List<Map<String, Object>> imagenes;
    private String status;
    private Map<String, String> errores;
}