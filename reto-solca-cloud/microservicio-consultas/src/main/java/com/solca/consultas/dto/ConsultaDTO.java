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
}