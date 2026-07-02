CREATE TABLE IF NOT EXISTS estudios_imagen (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL CHECK (
        sede IN (
            'SOLCA-CUENCA',
            'SOLCA-MANABI',
            'SOLCA-QUITO'
        )
    ),
    fecha_estudio DATE NOT NULL,
    tipo_estudio VARCHAR(50) NOT NULL,
    formato VARCHAR(10) NOT NULL,
    url_archivo VARCHAR(500) NOT NULL,
    nombre_archivo VARCHAR(255),
    medico_solicitante VARCHAR(100),
    modalidad VARCHAR(50),
    descripcion TEXT,
    hallazgos TEXT,
    tamano_bytes BIGINT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_paciente (id_paciente_regional),
    INDEX idx_fecha_estudio (fecha_estudio),
    INDEX idx_sede (sede),
    INDEX idx_tipo_estudio (tipo_estudio)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
