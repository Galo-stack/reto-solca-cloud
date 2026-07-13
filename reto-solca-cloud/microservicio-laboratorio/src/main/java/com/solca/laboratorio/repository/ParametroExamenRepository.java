package com.solca.laboratorio.repository;

import com.solca.laboratorio.model.ParametroExamen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParametroExamenRepository extends JpaRepository<ParametroExamen, Long> {
    List<ParametroExamen> findByTipoExamenOrderByOrdenAsc(String tipoExamen);
}
