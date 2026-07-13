ALTER TABLE auditorias
  ADD COLUMN nombre_completo VARCHAR(150) DEFAULT '' AFTER usuario,
  ADD COLUMN id_usuario BIGINT AFTER rol,
  ADD COLUMN dispositivo VARCHAR(100) DEFAULT '' AFTER direccion_ip,
  ADD COLUMN navegador VARCHAR(100) DEFAULT '' AFTER dispositivo,
  ADD COLUMN sistema_operativo VARCHAR(100) DEFAULT '' AFTER navegador,
  ADD COLUMN submodulo VARCHAR(100) DEFAULT '' AFTER modulo,
  ADD COLUMN valor_anterior TEXT AFTER detalle,
  ADD COLUMN valor_nuevo TEXT AFTER valor_anterior,
  ADD COLUMN paciente_relacionado VARCHAR(20) DEFAULT '' AFTER valor_nuevo,
  ADD COLUMN historia_clinica VARCHAR(50) DEFAULT '' AFTER paciente_relacionado,
  ADD COLUMN numero_consulta VARCHAR(20) DEFAULT '' AFTER historia_clinica,
  ADD COLUMN tipo_registro VARCHAR(50) DEFAULT '' AFTER numero_consulta,
  ADD COLUMN estado_registro VARCHAR(50) DEFAULT '' AFTER tipo_registro,
  ADD COLUMN codigo_http INT DEFAULT 200 AFTER resultado,
  ADD COLUMN tiempo_ejecucion_ms BIGINT DEFAULT 0 AFTER codigo_http,
  ADD COLUMN sesion_usuario VARCHAR(100) DEFAULT '' AFTER tiempo_ejecucion_ms,
  ADD COLUMN uuid_evento VARCHAR(36) DEFAULT '' AFTER sesion_usuario,
  ADD COLUMN nivel_criticidad VARCHAR(10) DEFAULT 'BAJO' AFTER uuid_evento;

CREATE INDEX idx_auditoria_criticidad ON auditorias(nivel_criticidad);
CREATE INDEX idx_auditoria_submodulo ON auditorias(submodulo);
CREATE INDEX idx_auditoria_paciente ON auditorias(paciente_relacionado);
CREATE INDEX idx_auditoria_uuid ON auditorias(uuid_evento);