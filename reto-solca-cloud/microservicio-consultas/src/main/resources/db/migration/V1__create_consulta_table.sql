CREATE TABLE IF NOT EXISTS consultas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL,
    fecha_consulta DATE NOT NULL,
    especialidad VARCHAR(50) NOT NULL,
    diagnostico TEXT,
    tratamiento TEXT,
    medico VARCHAR(100) NOT NULL,
    notas TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_paciente (id_paciente_regional),
    INDEX idx_fecha_consulta (fecha_consulta),
    INDEX idx_sede (sede),
    INDEX idx_especialidad (especialidad)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
