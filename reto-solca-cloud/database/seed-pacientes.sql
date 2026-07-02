-- ============================================================
-- INSERTAR DATOS DE PRUEBA
-- ============================================================

-- -----------------------------------------------------------
-- 1. PACIENTES (db_pacientes)
-- -----------------------------------------------------------
INSERT INTO pacientes (id_paciente_regional, cedula, nombres, apellidos, fecha_nacimiento, genero, telefono, email, direccion)
VALUES
    ('PAC-00001', '0102030405', 'Juan Carlos', 'Mendoza López', '1985-03-15', 'M', '0987654321', 'juan.mendoza@email.com', 'Av. Primera 123, Cuenca'),
    ('PAC-00002', '0203040506', 'María Elena', 'Ramírez Torres', '1992-07-22', 'F', '0998765432', 'maria.ramirez@email.com', 'Calle Secundaria 456, Cuenca'),
    ('PAC-00003', '0304050607', 'Carlos Andrés', 'Sánchez Vega', '1978-11-08', 'M', '0976543210', 'carlos.sanchez@email.com', 'Av. Central 789, Manta'),
    ('PAC-00004', '0405060708', 'Ana Lucía', 'García Montenegro', '1995-05-30', 'F', '0965432109', 'ana.garcia@email.com', 'Calle Norte 321, Quito'),
    ('PAC-00005', '0506070809', 'Pedro José', 'Torres Alcívar', '1965-09-12', 'M', '0954321098', 'pedro.torres@email.com', 'Av. Sur 654, Portoviejo');

INSERT INTO historias_locales (id_paciente_regional, sede, id_historia_local)
VALUES
    ('PAC-00001', 'SOLCA-CUENCA', 'HC-001'),
    ('PAC-00001', 'SOLCA-QUITO', 'HC-002'),
    ('PAC-00002', 'SOLCA-CUENCA', 'HC-003'),
    ('PAC-00003', 'SOLCA-MANABI', 'HC-004'),
    ('PAC-00004', 'SOLCA-QUITO', 'HC-005'),
    ('PAC-00005', 'SOLCA-MANABI', 'HC-006');
