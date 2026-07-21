CREATE INDEX IF NOT EXISTS idx_pacientes_nombres_apellidos ON pacientes (nombres, apellidos);
CREATE INDEX IF NOT EXISTS idx_pacientes_fecha_registro ON pacientes (fecha_registro DESC);
CREATE INDEX IF NOT EXISTS idx_pacientes_activo ON pacientes (activo);
CREATE INDEX IF NOT EXISTS idx_historias_locales_paciente ON historias_locales (id_paciente_regional);
