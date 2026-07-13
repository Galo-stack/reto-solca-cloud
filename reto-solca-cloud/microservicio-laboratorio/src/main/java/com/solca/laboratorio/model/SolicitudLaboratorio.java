package com.solca.laboratorio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_laboratorio")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudLaboratorio {
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

    @Column(columnDefinition = "TEXT")
    private String motivo;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "codigo_cie10", length = 10)
    private String codigoCie10;

    @Column(name = "observaciones_clinicas", columnDefinition = "TEXT")
    private String observacionesClinicas;

    @Column(name = "requiere_ayuno")
    @Builder.Default
    private Boolean requiereAyuno = false;

    @Column(name = "medicacion_actual", columnDefinition = "TEXT")
    private String medicacionActual;

    @Column
    @Builder.Default
    private Boolean embarazo = false;

    @Column(name = "sospecha_diagnostica", columnDefinition = "TEXT")
    private String sospechaDiagnostica;

    @Column(name = "medico_solicitante", length = 100, nullable = false)
    private String medicoSolicitante;

    @Column(length = 50)
    private String especialidad;

    @Column(length = 20)
    @Builder.Default
    private String estado = "PENDIENTE";

    @Column
    @Builder.Default
    private Boolean activo = true;
}
