package com.solca.auditoria.repository;

import com.solca.auditoria.model.Auditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    Page<Auditoria> findByOrderByFechaDesc(Pageable pageable);
    Page<Auditoria> findByUsuarioOrderByFechaDesc(String usuario, Pageable pageable);
    Page<Auditoria> findByModuloOrderByFechaDesc(String modulo, Pageable pageable);
    Page<Auditoria> findByAccionOrderByFechaDesc(String accion, Pageable pageable);
    Page<Auditoria> findByNivelCriticidadOrderByFechaDesc(String nivelCriticidad, Pageable pageable);
    Page<Auditoria> findByPacienteRelacionadoOrderByFechaDesc(String pacienteRelacionado, Pageable pageable);
    Page<Auditoria> findByRolOrderByFechaDesc(String rol, Pageable pageable);
    Page<Auditoria> findBySubmoduloOrderByFechaDesc(String submodulo, Pageable pageable);
    Page<Auditoria> findByResultadoOrderByFechaDesc(String resultado, Pageable pageable);
    Page<Auditoria> findByDireccionIpOrderByFechaDesc(String direccionIp, Pageable pageable);
    List<Auditoria> findByFechaBetweenOrderByFechaDesc(LocalDateTime inicio, LocalDateTime fin);
    Page<Auditoria> findByFechaBetweenOrderByFechaDesc(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<Auditoria> findByUsuarioAndFechaBetweenOrderByFechaDesc(String usuario, LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<Auditoria> findByModuloAndFechaBetweenOrderByFechaDesc(String modulo, LocalDateTime inicio, LocalDateTime fin, Pageable pageable);

    @Query("SELECT a FROM Auditoria a WHERE " +
           "(:usuario IS NULL OR a.usuario LIKE %:usuario%) AND " +
           "(:rol IS NULL OR a.rol = :rol) AND " +
           "(:modulo IS NULL OR a.modulo = :modulo) AND " +
           "(:accion IS NULL OR a.accion LIKE %:accion%) AND " +
           "(:nivelCriticidad IS NULL OR a.nivelCriticidad = :nivelCriticidad) AND " +
           "(:resultado IS NULL OR a.resultado = :resultado) AND " +
           "(:paciente IS NULL OR a.pacienteRelacionado = :paciente) AND " +
           "(:direccionIp IS NULL OR a.direccionIp LIKE %:direccionIp%) AND " +
           "(:desde IS NULL OR a.fecha >= :desde) AND " +
           "(:hasta IS NULL OR a.fecha <= :hasta)")
    Page<Auditoria> searchAudits(
        @Param("usuario") String usuario,
        @Param("rol") String rol,
        @Param("modulo") String modulo,
        @Param("accion") String accion,
        @Param("nivelCriticidad") String nivelCriticidad,
        @Param("resultado") String resultado,
        @Param("paciente") String paciente,
        @Param("direccionIp") String direccionIp,
        @Param("desde") LocalDateTime desde,
        @Param("hasta") LocalDateTime hasta,
        Pageable pageable);

    long countByModuloAndFechaBetween(String modulo, LocalDateTime inicio, LocalDateTime fin);
    long countByAccionAndFechaBetween(String accion, LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT a.modulo, COUNT(a) FROM Auditoria a GROUP BY a.modulo ORDER BY COUNT(a) DESC")
    List<Object[]> countByModulo();

    @Query("SELECT a.accion, COUNT(a) FROM Auditoria a GROUP BY a.accion ORDER BY COUNT(a) DESC")
    List<Object[]> countByAccion();

    @Query("SELECT FUNCTION('DATE', a.fecha), COUNT(a) FROM Auditoria a GROUP BY FUNCTION('DATE', a.fecha) ORDER BY FUNCTION('DATE', a.fecha) DESC")
    List<Object[]> countByDay();
}