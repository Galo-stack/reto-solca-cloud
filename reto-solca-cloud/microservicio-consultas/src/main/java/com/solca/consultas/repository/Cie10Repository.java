package com.solca.consultas.repository;

import com.solca.consultas.model.Cie10;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Cie10Repository extends JpaRepository<Cie10, Long> {
    List<Cie10> findByCodigoStartingWithOrderByCodigo(String codigo);
    List<Cie10> findByDescripcionContainingIgnoreCaseOrderByCodigo(String descripcion);
    List<Cie10> findByCapituloOrderByCodigo(String capitulo);
}
