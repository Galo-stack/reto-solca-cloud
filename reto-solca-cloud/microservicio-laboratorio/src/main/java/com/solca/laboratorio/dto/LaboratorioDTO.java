package com.solca.laboratorio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaboratorioDTO {
    private Long id;

    @NotBlank(message = "El ID del paciente es obligatorio")
    private String idPacienteRegional;

    @NotBlank(message = "La sede es obligatoria")
    private String sede;

    @NotNull(message = "La fecha de ejecución es obligatoria")
    private LocalDate fechaEjecucion;

    @NotBlank(message = "El tipo de examen es obligatorio")
    private String tipoExamen;

    @NotBlank(message = "El resultado es obligatorio")
    private String resultado;

    private String valoresReferencia;
    private String medicoSolicitante;
    private String area;
    private String metodo;
    private String observaciones;
    private LocalDate fechaResultado;
    private Boolean anormal;
}