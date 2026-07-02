package com.solca.imagenologia.dto;

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
public class ImagenologiaDTO {
    private Long id;

    @NotBlank(message = "El ID del paciente es obligatorio")
    private String idPacienteRegional;

    @NotBlank(message = "La sede es obligatoria")
    private String sede;

    @NotNull(message = "La fecha de estudio es obligatoria")
    private LocalDate fechaEstudio;

    @NotBlank(message = "El tipo de estudio es obligatorio")
    private String tipoEstudio;

    @NotBlank(message = "El formato es obligatorio")
    private String formato;

    @NotBlank(message = "La URL del archivo es obligatoria")
    private String urlArchivo;

    private String nombreArchivo;
    private String medicoSolicitante;
    private String modalidad;
    private String descripcion;
    private String hallazgos;
    private Long tamanoBytes;
}