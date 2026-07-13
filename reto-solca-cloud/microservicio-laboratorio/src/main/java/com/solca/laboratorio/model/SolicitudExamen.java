package com.solca.laboratorio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitud_examenes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudExamen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "solicitud_id", nullable = false)
    private Long solicitudId;

    @Column(name = "tipo_examen", length = 100, nullable = false)
    private String tipoExamen;
}
