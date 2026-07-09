CREATE TABLE auditorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    hora VARCHAR(10) NOT NULL,
    usuario VARCHAR(50) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    direccion_ip VARCHAR(50),
    modulo VARCHAR(50) NOT NULL,
    accion VARCHAR(100) NOT NULL,
    detalle TEXT,
    resultado VARCHAR(20) NOT NULL DEFAULT 'EXITO',
    id_referencia BIGINT,
    tipo_referencia VARCHAR(50)
);

CREATE INDEX idx_auditoria_fecha ON auditorias(fecha);
CREATE INDEX idx_auditoria_usuario ON auditorias(usuario);
CREATE INDEX idx_auditoria_modulo ON auditorias(modulo);
CREATE INDEX idx_auditoria_accion ON auditorias(accion);
