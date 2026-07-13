package com.solca.imagenologia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_imagen")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudImagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_paciente_regional", length = 20, nullable = false)
    private String idPacienteRegional;

    @Column(length = 20, nullable = false)
    private String sede;

    @Column(name = "fecha_solicitud")
    @CreationTimestamp
    private LocalDateTime fechaSolicitud;

    @Column(length = 20)
    @Builder.Default
    private String prioridad = "NORMAL";

    @Column(name = "tipo_estudio", length = 100, nullable = false)
    private String tipoEstudio;

    @Column(name = "region_anatomica", length = 100)
    private String regionAnatomica;

    @Column(length = 20)
    private String lateralidad;

    @Column(name = "requiere_contraste")
    @Builder.Default
    private Boolean requiereContraste = false;

    @Column(name = "motivo_clinico", columnDefinition = "TEXT")
    private String motivoClinico;

    @Column(name = "diagnostico_presuntivo", columnDefinition = "TEXT")
    private String diagnosticoPresuntivo;

    @Column(name = "codigo_cie10", length = 10)
    private String codigoCie10;

    @Column(name = "medico_solicitante", length = 100, nullable = false)
    private String medicoSolicitante;

    @Column(length = 50)
    private String especialidad;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(length = 20)
    @Builder.Default
    private String estado = "PENDIENTE";

    @Column
    @Builder.Default
    private Boolean activo = true;
}
