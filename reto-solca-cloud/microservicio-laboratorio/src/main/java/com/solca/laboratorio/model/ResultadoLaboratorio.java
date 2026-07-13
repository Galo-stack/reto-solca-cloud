package com.solca.laboratorio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultados_laboratorio")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoLaboratorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_paciente_regional", length = 20, nullable = false)
    private String idPacienteRegional;

    @Column(name = "sede", length = 20, nullable = false)
    private String sede;

    @Column(name = "fecha_ejecucion", nullable = false)
    private LocalDate fechaEjecucion;

    @Column(name = "tipo_examen", length = 100, nullable = false)
    private String tipoExamen;

    @Column(name = "resultado", columnDefinition = "TEXT", nullable = false)
    private String resultado;

    @Column(name = "valores_referencia", columnDefinition = "TEXT")
    private String valoresReferencia;

    @Column(name = "medico_solicitante", length = 100)
    private String medicoSolicitante;

    @Column(name = "area", length = 50)
    private String area;

    @Column(name = "metodo", length = 100)
    private String metodo;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_resultado")
    private LocalDate fechaResultado;

    @Column(name = "fecha_registro")
    @CreationTimestamp
    private LocalDateTime fechaRegistro;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "anormal")
    @Builder.Default
    private Boolean anormal = false;

    @Column(name = "firma_digital", length = 255)
    @Builder.Default
    private String firmaDigital = "";

    @Column(name = "validado_por", length = 100)
    @Builder.Default
    private String validadoPor = "";

    @Column(name = "fecha_validacion")
    private LocalDateTime fechaValidacion;

    @Column(name = "interpretacion", columnDefinition = "TEXT")
    private String interpretacion;

    @Column(name = "solicitud_id")
    private Long solicitudId;

    @Column(name = "estado", length = 20)
    @Builder.Default
    private String estado = "PENDIENTE";
}