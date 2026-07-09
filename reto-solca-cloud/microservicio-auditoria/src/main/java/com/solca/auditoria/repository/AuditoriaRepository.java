package com.solca.auditoria.repository;

import com.solca.auditoria.model.Auditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    Page<Auditoria> findByOrderByFechaDesc(Pageable pageable);
    List<Auditoria> findByUsuarioOrderByFechaDesc(String usuario);
    List<Auditoria> findByModuloOrderByFechaDesc(String modulo);
    List<Auditoria> findByAccionOrderByFechaDesc(String accion);
    List<Auditoria> findByFechaBetweenOrderByFechaDesc(LocalDateTime inicio, LocalDateTime fin);
    Page<Auditoria> findByModuloOrderByFechaDesc(String modulo, Pageable pageable);
    Page<Auditoria> findByUsuarioOrderByFechaDesc(String usuario, Pageable pageable);
}
