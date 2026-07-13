package com.solca.auditoria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditorias")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 10)
    private String hora;

    @Column(nullable = false, length = 50)
    private String usuario;

    @Column(length = 150)
    private String nombreCompleto;

    @Column(nullable = false, length = 30)
    private String rol;

    @Column
    private Long idUsuario;

    @Column(length = 50)
    private String direccionIp;

    @Column(length = 100)
    private String dispositivo;

    @Column(length = 100)
    private String navegador;

    @Column(length = 100)
    private String sistemaOperativo;

    @Column(nullable = false, length = 50)
    private String modulo;

    @Column(length = 100)
    private String submodulo;

    @Column(nullable = false, length = 100)
    private String accion;

    @Column(columnDefinition = "TEXT")
    private String detalle;

    @Column(columnDefinition = "TEXT")
    private String valorAnterior;

    @Column(columnDefinition = "TEXT")
    private String valorNuevo;

    @Column(length = 20)
    private String pacienteRelacionado;

    @Column(length = 50)
    private String historiaClinica;

    @Column(length = 20)
    private String numeroConsulta;

    @Column(length = 50)
    private String tipoRegistro;

    @Column(length = 50)
    private String estadoRegistro;

    @Column(nullable = false, length = 20)
    private String resultado;

    @Column
    @Builder.Default
    private Integer codigoHttp = 200;

    @Column
    @Builder.Default
    private Long tiempoEjecucionMs = 0L;

    @Column(length = 100)
    private String sesionUsuario;

    @Column(length = 36)
    private String uuidEvento;

    @Column(length = 10)
    @Builder.Default
    private String nivelCriticidad = "BAJO";

    @Column
    private Long idReferencia;

    @Column(length = 50)
    private String tipoReferencia;
}