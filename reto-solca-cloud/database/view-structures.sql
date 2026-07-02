-- ============================================================
-- VIEW TABLE STRUCTURES AND DATA FOR ALL MICROSERVICES
-- Execute each block against the respective database container
-- ============================================================

-- -----------------------------------------------------------
-- 1. PACIENTES (db_pacientes)
-- Container: mysql-pacientes (port 3307)
-- -----------------------------------------------------------
SELECT '=== PACIENTES TABLE ===' AS info;
DESCRIBE pacientes;
SELECT '=== HISTORIAS LOCALES TABLE ===' AS info;
DESCRIBE historias_locales;
SELECT '=== PACIENTES DATA ===' AS info;
SELECT id_paciente_regional, cedula, nombres, apellidos, fecha_nacimiento, genero, activo
FROM pacientes;
SELECT '=== HISTORIAS LOCALES DATA ===' AS info;
SELECT * FROM historias_locales;
SELECT '=== FLYWAY MIGRATION ===' AS info;
SELECT version, description, installed_on, success FROM flyway_schema_history;

-- -----------------------------------------------------------
-- 2. CONSULTAS (db_consultas)
-- Container: mysql-consultas (port 3308)
-- -----------------------------------------------------------
SELECT '=== CONSULTAS TABLE ===' AS info;
DESCRIBE consultas;
SELECT '=== CONSULTAS DATA ===' AS info;
SELECT id, id_paciente_regional, sede, fecha_consulta, especialidad, medico
FROM consultas
ORDER BY fecha_consulta DESC;
SELECT '=== FLYWAY MIGRATION ===' AS info;
SELECT version, description, installed_on, success FROM flyway_schema_history;

-- -----------------------------------------------------------
-- 3. LABORATORIO (db_laboratorio)
-- Container: mysql-laboratorio (port 3309)
-- -----------------------------------------------------------
SELECT '=== RESULTADOS LABORATORIO TABLE ===' AS info;
DESCRIBE resultados_laboratorio;
SELECT '=== LABORATORIO DATA ===' AS info;
SELECT id, id_paciente_regional, tipo_examen, fecha_ejecucion, anormal
FROM resultados_laboratorio
ORDER BY anormal DESC, fecha_ejecucion DESC;
SELECT '=== FLYWAY MIGRATION ===' AS info;
SELECT version, description, installed_on, success FROM flyway_schema_history;

-- -----------------------------------------------------------
-- 4. IMAGENOLOGIA (db_imagenologia)
-- Container: mysql-imagenologia (port 3310)
-- -----------------------------------------------------------
SELECT '=== ESTUDIOS IMAGEN TABLE ===' AS info;
DESCRIBE estudios_imagen;
SELECT '=== IMAGENOLOGIA DATA ===' AS info;
SELECT id, id_paciente_regional, tipo_estudio, fecha_estudio, formato, modalidad
FROM estudios_imagen
ORDER BY fecha_estudio DESC;
SELECT '=== FLYWAY MIGRATION ===' AS info;
SELECT version, description, installed_on, success FROM flyway_schema_history;
