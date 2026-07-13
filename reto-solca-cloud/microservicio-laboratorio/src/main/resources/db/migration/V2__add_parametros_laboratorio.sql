-- Parametros predefinidos por tipo de examen
CREATE TABLE IF NOT EXISTS parametros_examen (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_examen VARCHAR(100) NOT NULL,
    parametro VARCHAR(100) NOT NULL,
    unidad VARCHAR(50) DEFAULT '',
    valor_referencia_min VARCHAR(50) DEFAULT '',
    valor_referencia_max VARCHAR(50) DEFAULT '',
    orden INT DEFAULT 0,
    tipo_dato VARCHAR(20) DEFAULT 'texto',
    INDEX idx_tipo_examen (tipo_examen)
);

INSERT IGNORE INTO parametros_examen (tipo_examen, parametro, unidad, valor_referencia_min, valor_referencia_max, orden, tipo_dato) VALUES
-- Biometría Hemática
('BIOMETRIA_HEMATICA', 'Hemoglobina', 'g/dL', '13', '17', 1, 'decimal'),
('BIOMETRIA_HEMATICA', 'Hematocrito', '%', '40', '50', 2, 'decimal'),
('BIOMETRIA_HEMATICA', 'Eritrocitos', '10^6/µL', '4.5', '5.9', 3, 'decimal'),
('BIOMETRIA_HEMATICA', 'Leucocitos', '/mm³', '4500', '11000', 4, 'numero'),
('BIOMETRIA_HEMATICA', 'Plaquetas', '/mm³', '150000', '450000', 5, 'numero'),
('BIOMETRIA_HEMATICA', 'VCM', 'fL', '80', '100', 6, 'decimal'),
('BIOMETRIA_HEMATICA', 'HCM', 'pg', '27', '34', 7, 'decimal'),
('BIOMETRIA_HEMATICA', 'CHCM', 'g/dL', '32', '36', 8, 'decimal'),
('BIOMETRIA_HEMATICA', 'RDW', '%', '11.5', '14.5', 9, 'decimal'),
('BIOMETRIA_HEMATICA', 'Neutrófilos', '%', '40', '75', 10, 'decimal'),
('BIOMETRIA_HEMATICA', 'Linfocitos', '%', '20', '45', 11, 'decimal'),
('BIOMETRIA_HEMATICA', 'Monocitos', '%', '2', '10', 12, 'decimal'),
('BIOMETRIA_HEMATICA', 'Eosinófilos', '%', '1', '4', 13, 'decimal'),
('BIOMETRIA_HEMATICA', 'Basófilos', '%', '0', '1', 14, 'decimal'),
('BIOMETRIA_HEMATICA', 'Neutrófilos Abs', '/mm³', '1800', '7700', 15, 'numero'),
('BIOMETRIA_HEMATICA', 'Linfocitos Abs', '/mm³', '1000', '4800', 16, 'numero'),
-- Química Clínica
('QUIMICA_CLINICA', 'Glucosa', 'mg/dL', '70', '110', 1, 'decimal'),
('QUIMICA_CLINICA', 'Urea', 'mg/dL', '10', '50', 2, 'decimal'),
('QUIMICA_CLINICA', 'Creatinina', 'mg/dL', '0.6', '1.2', 3, 'decimal'),
('QUIMICA_CLINICA', 'Ácido Úrico', 'mg/dL', '3.4', '7.0', 4, 'decimal'),
('QUIMICA_CLINICA', 'Colesterol Total', 'mg/dL', '0', '200', 5, 'decimal'),
('QUIMICA_CLINICA', 'HDL', 'mg/dL', '40', '60', 6, 'decimal'),
('QUIMICA_CLINICA', 'LDL', 'mg/dL', '0', '130', 7, 'decimal'),
('QUIMICA_CLINICA', 'Triglicéridos', 'mg/dL', '0', '150', 8, 'decimal'),
('QUIMICA_CLINICA', 'Bilirrubina Total', 'mg/dL', '0.3', '1.2', 9, 'decimal'),
('QUIMICA_CLINICA', 'Bilirrubina Directa', 'mg/dL', '0', '0.3', 10, 'decimal'),
('QUIMICA_CLINICA', 'AST/TGO', 'U/L', '10', '40', 11, 'decimal'),
('QUIMICA_CLINICA', 'ALT/TGP', 'U/L', '10', '40', 12, 'decimal'),
('QUIMICA_CLINICA', 'GGT', 'U/L', '5', '40', 13, 'decimal'),
('QUIMICA_CLINICA', 'Fosfatasa Alcalina', 'U/L', '44', '147', 14, 'decimal'),
('QUIMICA_CLINICA', 'Proteínas Totales', 'g/dL', '6.4', '8.3', 15, 'decimal'),
('QUIMICA_CLINICA', 'Albúmina', 'g/dL', '3.5', '5.0', 16, 'decimal'),
-- Coagulación
('COAGULACION', 'TP', 'seg', '11', '13.5', 1, 'decimal'),
('COAGULACION', 'INR', '', '0.8', '1.2', 2, 'decimal'),
('COAGULACION', 'TTP', 'seg', '25', '35', 3, 'decimal'),
('COAGULACION', 'Fibrinógeno', 'mg/dL', '200', '400', 4, 'decimal'),
('COAGULACION', 'Dímero D', 'ng/mL', '0', '500', 5, 'decimal'),
-- EGO
('EGO', 'Color', '', '', '', 1, 'texto'),
('EGO', 'Aspecto', '', '', '', 2, 'texto'),
('EGO', 'Densidad', '', '1.005', '1.030', 3, 'decimal'),
('EGO', 'pH', '', '4.5', '8.0', 4, 'decimal'),
('EGO', 'Proteínas', '', '', '', 5, 'texto'),
('EGO', 'Glucosa', '', '', '', 6, 'texto'),
('EGO', 'Cetonas', '', '', '', 7, 'texto'),
('EGO', 'Bilirrubina', '', '', '', 8, 'texto'),
('EGO', 'Sangre', '', '', '', 9, 'texto'),
('EGO', 'Nitritos', '', '', '', 10, 'texto'),
('EGO', 'Leucocitos', '', '', '', 11, 'texto'),
('EGO', 'Eritrocitos', '', '', '', 12, 'texto'),
('EGO', 'Cilindros', '', '', '', 13, 'texto'),
('EGO', 'Cristales', '', '', '', 14, 'texto'),
('EGO', 'Bacterias', '', '', '', 15, 'texto'),
-- Coproparasitario
('COPROPARASITARIO', 'Color', '', '', '', 1, 'texto'),
('COPROPARASITARIO', 'Consistencia', '', '', '', 2, 'texto'),
('COPROPARASITARIO', 'pH', '', '', '', 3, 'decimal'),
('COPROPARASITARIO', 'Sangre Oculta', '', '', '', 4, 'texto'),
('COPROPARASITARIO', 'Moco', '', '', '', 5, 'texto'),
('COPROPARASITARIO', 'Grasas', '', '', '', 6, 'texto'),
('COPROPARASITARIO', 'Levaduras', '', '', '', 7, 'texto'),
('COPROPARASITARIO', 'Protozoarios', '', '', '', 8, 'texto'),
('COPROPARASITARIO', 'Huevos', '', '', '', 9, 'texto'),
('COPROPARASITARIO', 'Larvas', '', '', '', 10, 'texto'),
('COPROPARASITARIO', 'Parásitos', '', '', '', 11, 'texto'),
('COPROPARASITARIO', 'Leucocitos', '', '', '', 12, 'texto'),
('COPROPARASITARIO', 'Eritrocitos', '', '', '', 13, 'texto'),
-- Perfil Hepático
('PERFIL_HEPATICO', 'AST/TGO', 'U/L', '10', '40', 1, 'decimal'),
('PERFIL_HEPATICO', 'ALT/TGP', 'U/L', '10', '40', 2, 'decimal'),
('PERFIL_HEPATICO', 'GGT', 'U/L', '5', '40', 3, 'decimal'),
('PERFIL_HEPATICO', 'Fosfatasa Alcalina', 'U/L', '44', '147', 4, 'decimal'),
('PERFIL_HEPATICO', 'Bilirrubina Total', 'mg/dL', '0.3', '1.2', 5, 'decimal'),
('PERFIL_HEPATICO', 'Bilirrubina Directa', 'mg/dL', '0', '0.3', 6, 'decimal'),
('PERFIL_HEPATICO', 'Proteínas Totales', 'g/dL', '6.4', '8.3', 7, 'decimal'),
('PERFIL_HEPATICO', 'Albúmina', 'g/dL', '3.5', '5.0', 8, 'decimal'),
-- Perfil Renal
('PERFIL_RENAL', 'Urea', 'mg/dL', '10', '50', 1, 'decimal'),
('PERFIL_RENAL', 'Creatinina', 'mg/dL', '0.6', '1.2', 2, 'decimal'),
('PERFIL_RENAL', 'Ácido Úrico', 'mg/dL', '3.4', '7.0', 3, 'decimal'),
('PERFIL_RENAL', 'Sodio', 'mEq/L', '135', '145', 4, 'decimal'),
('PERFIL_RENAL', 'Potasio', 'mEq/L', '3.5', '5.1', 5, 'decimal'),
('PERFIL_RENAL', 'Cloro', 'mEq/L', '98', '107', 6, 'decimal'),
-- Perfil Lipídico
('PERFIL_LIPIDICO', 'Colesterol Total', 'mg/dL', '0', '200', 1, 'decimal'),
('PERFIL_LIPIDICO', 'HDL', 'mg/dL', '40', '60', 2, 'decimal'),
('PERFIL_LIPIDICO', 'LDL', 'mg/dL', '0', '130', 3, 'decimal'),
('PERFIL_LIPIDICO', 'Triglicéridos', 'mg/dL', '0', '150', 4, 'decimal'),
('PERFIL_LIPIDICO', 'VLDL', 'mg/dL', '5', '40', 5, 'decimal');

-- Solicitudes de laboratorio (desde el médico)
CREATE TABLE IF NOT EXISTS solicitudes_laboratorio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL,
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    prioridad VARCHAR(20) DEFAULT 'NORMAL',
    motivo TEXT,
    diagnostico TEXT,
    codigo_cie10 VARCHAR(10),
    observaciones_clinicas TEXT,
    requiere_ayuno BOOLEAN DEFAULT FALSE,
    medicacion_actual TEXT,
    embarazo BOOLEAN DEFAULT FALSE,
    sospecha_diagnostica TEXT,
    medico_solicitante VARCHAR(100) NOT NULL,
    especialidad VARCHAR(50),
    estado VARCHAR(20) DEFAULT 'PENDIENTE',
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_solicitud_paciente (id_paciente_regional),
    INDEX idx_solicitud_fecha (fecha_solicitud),
    INDEX idx_solicitud_estado (estado)
);

-- Perfiles/examenes solicitados en cada solicitud
CREATE TABLE IF NOT EXISTS solicitud_examenes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    solicitud_id BIGINT NOT NULL,
    tipo_examen VARCHAR(100) NOT NULL,
    FOREIGN KEY (solicitud_id) REFERENCES solicitudes_laboratorio(id) ON DELETE CASCADE
);

-- Resultados estructurados por parametro
CREATE TABLE IF NOT EXISTS resultados_parametros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resultado_laboratorio_id BIGINT NOT NULL,
    parametro_examen_id BIGINT NOT NULL,
    valor_resultado VARCHAR(100),
    valor_referencia VARCHAR(100),
    anormal BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (resultado_laboratorio_id) REFERENCES resultados_laboratorio(id) ON DELETE CASCADE,
    FOREIGN KEY (parametro_examen_id) REFERENCES parametros_examen(id)
);

ALTER TABLE resultados_laboratorio
  ADD COLUMN firma_digital VARCHAR(255) DEFAULT '' AFTER observaciones,
  ADD COLUMN validado_por VARCHAR(100) DEFAULT '' AFTER firma_digital,
  ADD COLUMN fecha_validacion TIMESTAMP NULL AFTER validado_por,
  ADD COLUMN interpretacion TEXT AFTER fecha_validacion,
  ADD COLUMN solicitud_id BIGINT AFTER interpretacion,
  ADD COLUMN estado VARCHAR(20) DEFAULT 'PENDIENTE' AFTER solicitud_id;