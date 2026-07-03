package com.solca.consultas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_paciente_regional", length = 20, nullable = false)
    private String idPacienteRegional;

    @Column(name = "sede", length = 20, nullable = false)
    private String sede;

    @Column(name = "fecha_consulta", nullable = false)
    private LocalDate fechaConsulta;

    @Column(name = "especialidad", length = 50, nullable = false)
    private String especialidad;

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "tratamiento", columnDefinition = "TEXT")
    private String tratamiento;

    @Column(name = "medico", length = 100, nullable = false)
    private String medico;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @Column(name = "examen_fisico", columnDefinition = "TEXT")
    private String examenFisico;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "recomendaciones", columnDefinition = "TEXT")
    private String recomendaciones;

    @Column(name = "fecha_registro")
    @CreationTimestamp
    private LocalDateTime fechaRegistro;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;
}