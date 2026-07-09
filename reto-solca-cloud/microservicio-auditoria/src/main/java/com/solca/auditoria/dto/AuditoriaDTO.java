package com.solca.auditoria.dto;

import jakarta.validation.constraints.NotBlank;

public class AuditoriaDTO {
    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    @NotBlank(message = "El rol es obligatorio")
    private String rol;

    private String direccionIp;

    @NotBlank(message = "El modulo es obligatorio")
    private String modulo;

    @NotBlank(message = "La accion es obligatoria")
    private String accion;

    private String detalle;
    private String resultado;
    private Long idReferencia;
    private String tipoReferencia;

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getDireccionIp() { return direccionIp; }
    public void setDireccionIp(String direccionIp) { this.direccionIp = direccionIp; }
    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public Long getIdReferencia() { return idReferencia; }
    public void setIdReferencia(Long idReferencia) { this.idReferencia = idReferencia; }
    public String getTipoReferencia() { return tipoReferencia; }
    public void setTipoReferencia(String tipoReferencia) { this.tipoReferencia = tipoReferencia; }
}
