package com.solca.imagenologia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "estudios_imagen")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstudioImagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_paciente_regional", length = 20, nullable = false)
    private String idPacienteRegional;

    @Column(name = "sede", length = 20, nullable = false)
    private String sede;

    @Column(name = "fecha_estudio", nullable = false)
    private LocalDate fechaEstudio;

    @Column(name = "tipo_estudio", length = 50, nullable = false)
    private String tipoEstudio;

    @Column(name = "formato", length = 10, nullable = false)
    private String formato;

    @Column(name = "url_archivo", length = 500, nullable = false)
    private String urlArchivo;

    @Column(name = "nombre_archivo", length = 255)
    private String nombreArchivo;

    @Column(name = "medico_solicitante", length = 100)
    private String medicoSolicitante;

    @Column(name = "modalidad", length = 50)
    private String modalidad;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "hallazgos", columnDefinition = "TEXT")
    private String hallazgos;

    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @Column(name = "fecha_registro")
    @CreationTimestamp
    private LocalDateTime fechaRegistro;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;
}