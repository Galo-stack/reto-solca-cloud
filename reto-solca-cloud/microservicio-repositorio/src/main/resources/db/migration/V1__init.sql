CREATE TABLE consultas_repositorio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(50),
    nombres_paciente VARCHAR(200),
    apellidos_paciente VARCHAR(200),
    cedula_paciente VARCHAR(20),
    consultado_por VARCHAR(100),
    rol_usuario VARCHAR(50),
    estado VARCHAR(20) NOT NULL DEFAULT 'EXITO',
    detalle TEXT,
    fecha_consulta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tiempo_respuesta_ms BIGINT,
    INDEX idx_fecha_consulta (fecha_consulta DESC),
    INDEX idx_id_paciente (id_paciente_regional),
    INDEX idx_consultado_por (consultado_por)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
