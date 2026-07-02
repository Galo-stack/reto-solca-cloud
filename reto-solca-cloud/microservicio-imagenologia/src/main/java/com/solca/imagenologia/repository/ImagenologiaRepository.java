package com.solca.imagenologia.repository;

import com.solca.imagenologia.model.EstudioImagen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagenologiaRepository extends JpaRepository<EstudioImagen, Long> {
    List<EstudioImagen> findByIdPacienteRegionalOrderByFechaEstudioDesc(String idPacienteRegional);

    List<EstudioImagen> findBySedeAndTipoEstudio(String sede, String tipoEstudio);
}