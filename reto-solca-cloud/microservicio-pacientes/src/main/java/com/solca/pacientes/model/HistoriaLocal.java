package com.solca.pacientes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historias_locales")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaLocal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sede", length = 20, nullable = false)
    private String sede;

    @Column(name = "id_historia_local", length = 20, nullable = false)
    private String idHistoriaLocal;

    @Column(name = "fecha_asociacion")
    private LocalDateTime fechaAsociacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente_regional", nullable = false)
    private Paciente paciente;

    @PrePersist
    protected void onCreate() {
        fechaAsociacion = LocalDateTime.now();
    }
}