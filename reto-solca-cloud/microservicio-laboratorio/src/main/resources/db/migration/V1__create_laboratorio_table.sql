CREATE TABLE IF NOT EXISTS resultados_laboratorio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL CHECK (
        sede IN (
            'SOLCA-CUENCA',
            'SOLCA-MANABI',
            'SOLCA-QUITO'
        )
    ),
    fecha_ejecucion DATE NOT NULL,
    tipo_examen VARCHAR(100) NOT NULL,
    resultado TEXT NOT NULL,
    valores_referencia TEXT,
    medico_solicitante VARCHAR(100),
    area VARCHAR(50),
    metodo VARCHAR(100),
    observaciones TEXT,
    fecha_resultado DATE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    anormal BOOLEAN DEFAULT FALSE,
    INDEX idx_paciente (id_paciente_regional),
    INDEX idx_fecha_ejecucion (fecha_ejecucion),
    INDEX idx_sede (sede),
    INDEX idx_tipo_examen (tipo_examen)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
