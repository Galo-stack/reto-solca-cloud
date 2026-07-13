package com.solca.laboratorio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parametros_examen")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParametroExamen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_examen", length = 100, nullable = false)
    private String tipoExamen;

    @Column(nullable = false, length = 100)
    private String parametro;

    @Column(length = 50)
    private String unidad;

    @Column(name = "valor_referencia_min", length = 50)
    private String valorReferenciaMin;

    @Column(name = "valor_referencia_max", length = 50)
    private String valorReferenciaMax;

    @Column
    @Builder.Default
    private Integer orden = 0;

    @Column(name = "tipo_dato", length = 20)
    @Builder.Default
    private String tipoDato = "texto";
}
