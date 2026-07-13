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

    @Column(precision = 5, scale = 2)
    @Builder.Default
    private java.math.BigDecimal peso = java.math.BigDecimal.ZERO;

    @Column(precision = 5, scale = 2)
    @Builder.Default
    private java.math.BigDecimal talla = java.math.BigDecimal.ZERO;

    @Column(precision = 4, scale = 1)
    @Builder.Default
    private java.math.BigDecimal imc = java.math.BigDecimal.ZERO;

    @Column(name = "clasificacion_oms", length = 50)
    @Builder.Default
    private String clasificacionOms = "";

    @Column(name = "superficie_corporal", precision = 4, scale = 2)
    @Builder.Default
    private java.math.BigDecimal superficieCorporal = java.math.BigDecimal.ZERO;

    @Column(name = "riesgo_cardiovascular", length = 20)
    @Builder.Default
    private String riesgoCardiovascular = "";

    @Column(name = "presion_arterial", length = 10)
    @Builder.Default
    private String presionArterial = "";

    @Column(name = "frecuencia_cardiaca")
    @Builder.Default
    private Integer frecuenciaCardiaca = 0;

    @Column(precision = 4, scale = 1)
    @Builder.Default
    private java.math.BigDecimal temperatura = java.math.BigDecimal.ZERO;

    @Column(name = "frecuencia_respiratoria")
    @Builder.Default
    private Integer frecuenciaRespiratoria = 0;

    @Column(name = "saturacion_oxigeno")
    @Builder.Default
    private Integer saturacionOxigeno = 0;

    @Column
    @Builder.Default
    private Integer menarquia = 0;

    @Column
    private LocalDate fum;

    @Column
    private LocalDate fpp;

    @Column(name = "edad_gestacional")
    @Builder.Default
    private Integer edadGestacional = 0;

    @Column
    @Builder.Default
    private Integer gestas = 0;

    @Column
    @Builder.Default
    private Integer partos = 0;

    @Column
    @Builder.Default
    private Integer cesareas = 0;

    @Column
    @Builder.Default
    private Integer abortos = 0;

    @Column(name = "hijos_vivos")
    @Builder.Default
    private Integer hijosVivos = 0;

    @Column(name = "hijos_muertos")
    @Builder.Default
    private Integer hijosMuertos = 0;

    @Column(name = "embarazos_ectopicos")
    @Builder.Default
    private Integer embarazosEctopicos = 0;

    @Column(name = "embarazos_multiples")
    @Builder.Default
    private Integer embarazosMultiples = 0;

    @Column(name = "metodo_anticonceptivo", length = 100)
    @Builder.Default
    private String metodoAnticonceptivo = "";

    @Column(length = 50)
    @Builder.Default
    private String lactancia = "";

    @Column(length = 50)
    @Builder.Default
    private String menopausia = "";

    @Column(length = 100)
    @Builder.Default
    private String pap = "";

    @Column(length = 100)
    @Builder.Default
    private String colposcopia = "";

    @Column(length = 100)
    @Builder.Default
    private String mamografia = "";

    @Column(name = "antecedentes_ginecologicos", columnDefinition = "TEXT")
    private String antecedentesGinecologicos;

    @Column(name = "diagnostico_principal_cie10", length = 10)
    @Builder.Default
    private String diagnosticoPrincipalCie10 = "";

    @Column(name = "diagnostico_principal_desc", columnDefinition = "TEXT")
    private String diagnosticoPrincipalDesc;

    @Column(name = "diagnostico_secundario_cie10", length = 10)
    @Builder.Default
    private String diagnosticoSecundarioCie10 = "";

    @Column(name = "diagnostico_secundario_desc", columnDefinition = "TEXT")
    private String diagnosticoSecundarioDesc;

    @Column(name = "tipo_diagnostico", length = 20)
    @Builder.Default
    private String tipoDiagnostico = "PRESUNTIVO";

    @Column(name = "alergias_consulta", columnDefinition = "TEXT")
    private String alergiasConsulta;

    @Column(name = "plan_tratamiento", columnDefinition = "TEXT")
    private String planTratamiento;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;
}