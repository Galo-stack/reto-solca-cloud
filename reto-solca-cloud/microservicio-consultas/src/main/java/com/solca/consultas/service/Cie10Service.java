package com.solca.consultas.service;

import com.solca.consultas.model.Cie10;
import com.solca.consultas.repository.Cie10Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class Cie10Service {
    private final Cie10Repository cie10Repository;

    public List<Cie10> buscarPorCodigo(String codigo) {
        return cie10Repository.findByCodigoStartingWithOrderByCodigo(codigo);
    }

    public List<Cie10> buscarPorDescripcion(String descripcion) {
        return cie10Repository.findByDescripcionContainingIgnoreCaseOrderByCodigo(descripcion);
    }

    public List<Cie10> buscarPorCapitulo(String capitulo) {
        return cie10Repository.findByCapituloOrderByCodigo(capitulo);
    }

    public List<Cie10> listarTodos() {
        return cie10Repository.findAll();
    }
}
