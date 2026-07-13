package com.solca.laboratorio.service;

import com.solca.laboratorio.model.ParametroExamen;
import com.solca.laboratorio.repository.ParametroExamenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParametroExamenService {
    private final ParametroExamenRepository parametroExamenRepository;

    public List<ParametroExamen> obtenerPorTipoExamen(String tipoExamen) {
        return parametroExamenRepository.findByTipoExamenOrderByOrdenAsc(tipoExamen);
    }

    public List<ParametroExamen> listarTodos() {
        return parametroExamenRepository.findAll();
    }
}
