package com.solca.pacientes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaLocalDTO {
    @NotBlank(message = "La sede es obligatoria")
    private String sede;

    @NotBlank(message = "El ID de historia local es obligatorio")
    private String idHistoriaLocal;
}