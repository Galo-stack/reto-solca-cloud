package com.solca.consultas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaDTO {
    private Long id;

    @NotBlank(message = "El ID del paciente es obligatorio")
    private String idPacienteRegional;

    @NotBlank(message = "La sede es obligatoria")
    private String sede;

    @NotNull(message = "La fecha de consulta es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaConsulta;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    private String diagnostico;
    private String tratamiento;

    @NotBlank(message = "El médico es obligatorio")
    private String medico;

    private String notas;
    private String examenFisico;
    private String observaciones;
    private String recomendaciones;
    private java.math.BigDecimal peso;
    private java.math.BigDecimal talla;
    private java.math.BigDecimal imc;
    private String clasificacionOms;
    private java.math.BigDecimal superficieCorporal;
    private String riesgoCardiovascular;
    private String presionArterial;
    private Integer frecuenciaCardiaca;
    private java.math.BigDecimal temperatura;
    private Integer frecuenciaRespiratoria;
    private Integer saturacionOxigeno;
    private Integer menarquia;
    private LocalDate fum;
    private LocalDate fpp;
    private Integer edadGestacional;
    private Integer gestas;
    private Integer partos;
    private Integer cesareas;
    private Integer abortos;
    private Integer hijosVivos;
    private Integer hijosMuertos;
    private Integer embarazosEctopicos;
    private Integer embarazosMultiples;
    private String metodoAnticonceptivo;
    private String lactancia;
    private String menopausia;
    private String pap;
    private String colposcopia;
    private String mamografia;
    private String antecedentesGinecologicos;
    private String diagnosticoPrincipalCie10;
    private String diagnosticoPrincipalDesc;
    private String diagnosticoSecundarioCie10;
    private String diagnosticoSecundarioDesc;
    private String tipoDiagnostico;
    private String alergiasConsulta;
    private String planTratamiento;
}