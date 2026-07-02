package com.solca.imagenologia.repository;

import com.solca.imagenologia.model.EstudioImagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenologiaRepository extends JpaRepository<EstudioImagen, Long> {
    List<EstudioImagen> findByIdPacienteRegionalOrderByFechaEstudioDesc(String idPacienteRegional);

    List<EstudioImagen> findBySedeAndTipoEstudio(String sede, String tipoEstudio);
}