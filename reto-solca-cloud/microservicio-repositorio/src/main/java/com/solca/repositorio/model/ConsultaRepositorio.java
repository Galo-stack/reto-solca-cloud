package com.solca.repositorio.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas_repositorio")
public class ConsultaRepositorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_paciente_regional", length = 50)
    private String idPacienteRegional;

    @Column(name = "nombres_paciente", length = 200)
    private String nombresPaciente;

    @Column(name = "apellidos_paciente", length = 200)
    private String apellidosPaciente;

    @Column(name = "cedula_paciente", length = 20)
    private String cedulaPaciente;

    @Column(name = "consultado_por", length = 100)
    private String consultadoPor;

    @Column(name = "rol_usuario", length = 50)
    private String rolUsuario;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "detalle", columnDefinition = "TEXT")
    private String detalle;

    @Column(name = "fecha_consulta")
    private LocalDateTime fechaConsulta;

    @Column(name = "tiempo_respuesta_ms")
    private Long tiempoRespuestaMs;

    public ConsultaRepositorio() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdPacienteRegional() { return idPacienteRegional; }
    public void setIdPacienteRegional(String idPacienteRegional) { this.idPacienteRegional = idPacienteRegional; }
    public String getNombresPaciente() { return nombresPaciente; }
    public void setNombresPaciente(String nombresPaciente) { this.nombresPaciente = nombresPaciente; }
    public String getApellidosPaciente() { return apellidosPaciente; }
    public void setApellidosPaciente(String apellidosPaciente) { this.apellidosPaciente = apellidosPaciente; }
    public String getCedulaPaciente() { return cedulaPaciente; }
    public void setCedulaPaciente(String cedulaPaciente) { this.cedulaPaciente = cedulaPaciente; }
    public String getConsultadoPor() { return consultadoPor; }
    public void setConsultadoPor(String consultadoPor) { this.consultadoPor = consultadoPor; }
    public String getRolUsuario() { return rolUsuario; }
    public void setRolUsuario(String rolUsuario) { this.rolUsuario = rolUsuario; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    public LocalDateTime getFechaConsulta() { return fechaConsulta; }
    public void setFechaConsulta(LocalDateTime fechaConsulta) { this.fechaConsulta = fechaConsulta; }
    public Long getTiempoRespuestaMs() { return tiempoRespuestaMs; }
    public void setTiempoRespuestaMs(Long tiempoRespuestaMs) { this.tiempoRespuestaMs = tiempoRespuestaMs; }
}
