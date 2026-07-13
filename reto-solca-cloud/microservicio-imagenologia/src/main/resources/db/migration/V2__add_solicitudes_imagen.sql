-- Solicitudes de imagenología
CREATE TABLE IF NOT EXISTS solicitudes_imagen (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL,
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    prioridad VARCHAR(20) DEFAULT 'NORMAL',
    tipo_estudio VARCHAR(100) NOT NULL,
    region_anatomica VARCHAR(100),
    lateralidad VARCHAR(20),
    requiere_contraste BOOLEAN DEFAULT FALSE,
    motivo_clinico TEXT,
    diagnostico_presuntivo TEXT,
    codigo_cie10 VARCHAR(10),
    medico_solicitante VARCHAR(100) NOT NULL,
    especialidad VARCHAR(50),
    observaciones TEXT,
    estado VARCHAR(20) DEFAULT 'PENDIENTE',
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_solimg_paciente (id_paciente_regional),
    INDEX idx_solimg_fecha (fecha_solicitud),
    INDEX idx_solimg_estado (estado)
);

ALTER TABLE estudios_imagen
  ADD COLUMN informe_radiologico TEXT AFTER hallazgos,
  ADD COLUMN recomendaciones TEXT AFTER informe_radiologico,
  ADD COLUMN tecnica_utilizada TEXT AFTER recomendaciones,
  ADD COLUMN firma_radiologo VARCHAR(100) DEFAULT '' AFTER tecnica_utilizada,
  ADD COLUMN region_anatomica VARCHAR(100) AFTER modalidad,
  ADD COLUMN lateralidad VARCHAR(20) AFTER region_anatomica,
  ADD COLUMN requiere_contraste BOOLEAN DEFAULT FALSE AFTER lateralidad,
  ADD COLUMN solicitud_id BIGINT AFTER requiere_contraste,
  ADD COLUMN estado VARCHAR(20) DEFAULT 'PENDIENTE' AFTER solicitud_id;