package com.solca.laboratorio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resultados_parametros")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoParametro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resultado_laboratorio_id", nullable = false)
    private Long resultadoLaboratorioId;

    @Column(name = "parametro_examen_id", nullable = false)
    private Long parametroExamenId;

    @Column(name = "valor_resultado", length = 100)
    private String valorResultado;

    @Column(name = "valor_referencia", length = 100)
    private String valorReferencia;

    @Column
    @Builder.Default
    private Boolean anormal = false;
}
