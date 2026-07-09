CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    rol VARCHAR(30) NOT NULL DEFAULT 'MEDICO',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ultimo_acceso TIMESTAMP NULL
);

INSERT INTO usuarios (username, password, nombres, apellidos, email, rol, activo) VALUES
('admin', '$2b$12$xB.mTZHt5jct/irf7kR.wOiRmNPdvTl3NEXi15/pMU9VcDBfydcdu', 'Administrador', 'Sistema', 'admin@solca.gob.ec', 'ADMIN', TRUE),
('medico1', '$2b$12$xB.mTZHt5jct/irf7kR.wOiRmNPdvTl3NEXi15/pMU9VcDBfydcdu', 'Carlos', 'Mendoza Rivera', 'carlos.mendoza@solca.gob.ec', 'MEDICO', TRUE),
('medico2', '$2b$12$xB.mTZHt5jct/irf7kR.wOiRmNPdvTl3NEXi15/pMU9VcDBfydcdu', 'Ana', 'Guerrero Paz', 'ana.guerrero@solca.gob.ec', 'MEDICO', TRUE),
('lab1', '$2b$12$xB.mTZHt5jct/irf7kR.wOiRmNPdvTl3NEXi15/pMU9VcDBfydcdu', 'Roberto', 'Castro Lopez', 'roberto.castro@solca.gob.ec', 'LABORATORIO', TRUE),
('lab2', '$2b$12$xB.mTZHt5jct/irf7kR.wOiRmNPdvTl3NEXi15/pMU9VcDBfydcdu', 'Maria', 'Fernandez Diaz', 'maria.fernandez@solca.gob.ec', 'LABORATORIO', TRUE),
('imagen1', '$2b$12$xB.mTZHt5jct/irf7kR.wOiRmNPdvTl3NEXi15/pMU9VcDBfydcdu', 'Pedro', 'Jimenez Torres', 'pedro.jimenez@solca.gob.ec', 'IMAGENOLOGIA', TRUE),
('imagen2', '$2b$12$xB.mTZHt5jct/irf7kR.wOiRmNPdvTl3NEXi15/pMU9VcDBfydcdu', 'Laura', 'Silva Campos', 'laura.silva@solca.gob.ec', 'IMAGENOLOGIA', TRUE);

INSERT INTO usuarios (username, password, nombres, apellidos, email, rol, activo) VALUES
('auditor1', '$2b$12$xB.mTZHt5jct/irf7kR.wOiRmNPdvTl3NEXi15/pMU9VcDBfydcdu', 'Victor', 'Ramirez Ortiz', 'victor.ramirez@solca.gob.ec', 'ADMIN', TRUE);
