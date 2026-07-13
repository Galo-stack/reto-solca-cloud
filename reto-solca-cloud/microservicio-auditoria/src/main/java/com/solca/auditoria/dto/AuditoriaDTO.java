package com.solca.auditoria.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaDTO {
    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    private String nombreCompleto;

    @NotBlank(message = "El rol es obligatorio")
    private String rol;

    private Long idUsuario;
    private String direccionIp;
    private String dispositivo;
    private String navegador;
    private String sistemaOperativo;

    @NotBlank(message = "El modulo es obligatorio")
    private String modulo;

    private String submodulo;

    @NotBlank(message = "La accion es obligatoria")
    private String accion;

    private String detalle;
    private String valorAnterior;
    private String valorNuevo;
    private String pacienteRelacionado;
    private String historiaClinica;
    private String numeroConsulta;
    private String tipoRegistro;
    private String estadoRegistro;
    private String resultado;
    private Integer codigoHttp;
    private Long tiempoEjecucionMs;
    private String sesionUsuario;
    private String uuidEvento;
    private String nivelCriticidad;
    private Long idReferencia;
    private String tipoReferencia;
    private LocalDateTime fecha;
}