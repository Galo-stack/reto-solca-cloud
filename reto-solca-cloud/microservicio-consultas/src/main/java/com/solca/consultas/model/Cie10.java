package com.solca.consultas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cie10")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cie10 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String codigo;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(length = 100)
    private String capitulo;

    @Column(length = 10)
    private String categoria;
}
