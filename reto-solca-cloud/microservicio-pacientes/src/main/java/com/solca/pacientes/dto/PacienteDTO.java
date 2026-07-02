package com.solca.pacientes.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {
    private String idPacienteRegional;

    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 10, max = 15, message = "La cédula debe tener entre 10 y 15 caracteres")
    private String cedula;

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    private String genero;
    private String telefono;
    private String email;
    private String direccion;
    private List<HistoriaLocalDTO> historiasLocales;
}