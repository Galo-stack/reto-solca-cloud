package com.solca.seguridad.dto;

public class LoginResponse {
    private String token;
    private String tipo;
    private long expiracion;
    private UsuarioDTO usuario;

    public LoginResponse() {}

    public LoginResponse(String token, String tipo, long expiracion, UsuarioDTO usuario) {
        this.token = token;
        this.tipo = tipo;
        this.expiracion = expiracion;
        this.usuario = usuario;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public long getExpiracion() { return expiracion; }
    public void setExpiracion(long expiracion) { this.expiracion = expiracion; }
    public UsuarioDTO getUsuario() { return usuario; }
    public void setUsuario(UsuarioDTO usuario) { this.usuario = usuario; }
}
