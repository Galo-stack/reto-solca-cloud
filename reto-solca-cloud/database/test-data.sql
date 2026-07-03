-- ============================================================
-- TEST DATA FOR SOLCA REGIONAL MICROSERVICES
-- Execute in order: pacientes -> consultas -> laboratorio -> imagenologia
-- ============================================================

-- -----------------------------------------------------------
-- 1. PACIENTES (db_pacientes)
-- -----------------------------------------------------------
INSERT INTO pacientes (id_paciente_regional, cedula, nombres, apellidos, fecha_nacimiento, genero, telefono, email, direccion, grupo_sanguineo, estado_civil, ocupacion, alergias, enfermedad_actual, antecedentes_personales, antecedentes_familiares, contacto_emergencia)
VALUES
    ('PAC-00001', '0102030405', 'Juan Carlos', 'Mendoza López', '1985-03-15', 'M', '0987654321', 'juan.mendoza@email.com', 'Av. Primera 123, Cuenca', 'O+', 'CASADO', 'Ingeniero Civil', 'Penicilina', 'Dolor abdominal crónico en epigastrio', '{\"cronicas\":\"Ninguna\",\"cirugias\":\"Apendicectomía (2010)\",\"otros\":\"Ninguno\"}', '{\"padre\":\"Vive, HTA\",\"madre\":\"Vive, sana\",\"hermanos\":\"Sano\",\"hijos\":\"Sano\"}', '{\"nombre\":\"María López V.\",\"telefono\":\"0991112233\",\"parentesco\":\"Madre\"}'),
    ('PAC-00002', '0203040506', 'María Elena', 'Ramírez Torres', '1992-07-22', 'F', '0998765432', 'maria.ramirez@email.com', 'Calle Secundaria 456, Cuenca', 'A+', 'SOLTERO', 'Médico', 'Sulfa, Mariscos', 'Nódulo tiroideo sospechoso', '{\"cronicas\":\"Hipotiroidismo\",\"cirugias\":\"Ninguna\",\"otros\":\"Ninguno\"}', '{\"padre\":\"Vive, sano\",\"madre\":\"Vive, HTA\",\"hermanos\":\"Sano\",\"hijos\":\"N/A\"}', '{\"nombre\":\"Carlos Ramírez\",\"telefono\":\"0992223344\",\"parentesco\":\"Padre\"}'),
    ('PAC-00003', '0304050607', 'Carlos Andrés', 'Sánchez Vega', '1978-11-08', 'M', '0976543210', 'carlos.sanchez@email.com', 'Av. Central 789, Manta', 'B+', 'CASADO', 'Agricultor', 'Ninguna', 'Adenocarcinoma de colon Estadio II', '{\"cronicas\":\"Ninguna\",\"cirugias\":\"Ninguna\",\"otros\":\"Tabaquismo suspendido\"}', '{\"padre\":\"Fallecido por CA gástrico\",\"madre\":\"Vive, diabetes\",\"hermanos\":\"Sano\",\"hijos\":\"Sano\"}', '{\"nombre\":\"Lucía Vega de Sánchez\",\"telefono\":\"0993334455\",\"parentesco\":\"Esposa\"}'),
    ('PAC-00004', '0405060708', 'Ana Lucía', 'García Montenegro', '1995-05-30', 'F', '0965432109', 'ana.garcia@email.com', 'Calle Norte 321, Quito', 'AB+', 'SOLTERO', 'Abogada', 'Ibuprofeno', 'Leucemia linfocítica crónica', '{\"cronicas\":\"Ninguna\",\"cirugias\":\"Ninguna\",\"otros\":\"Ninguno\"}', '{\"padre\":\"Vive, sano\",\"madre\":\"Vive, sana\",\"hermanos\":\"Sano\",\"hijos\":\"N/A\"}', '{\"nombre\":\"Pedro García\",\"telefono\":\"0994445566\",\"parentesco\":\"Hermano\"}'),
    ('PAC-00005', '0506070809', 'Pedro José', 'Torres Alcívar', '1965-09-12', 'M', '0954321098', 'pedro.torres@email.com', 'Av. Sur 654, Portoviejo', 'O-', 'VIUDO', 'Jubilado', 'Codeína', 'Cáncer de próstata localizado', '{\"cronicas\":\"HTA, Diabetes tipo 2\",\"cirugias\":\"Ninguna\",\"otros\":\"Ninguno\"}', '{\"padre\":\"Fallecido por CA próstata\",\"madre\":\"Fallecida por DM\",\"hermanos\":\"HTA\",\"hijos\":\"Sano\"}', '{\"nombre\":\"José Torres M.\",\"telefono\":\"0995556677\",\"parentesco\":\"Hijo\"}');

-- -----------------------------------------------------------
-- Historias Locales
-- -----------------------------------------------------------
INSERT INTO historias_locales (id_paciente_regional, sede, id_historia_local)
VALUES
    ('PAC-00001', 'SOLCA-CUENCA', 'HC-001'),
    ('PAC-00001', 'SOLCA-QUITO', 'HC-002'),
    ('PAC-00002', 'SOLCA-CUENCA', 'HC-003'),
    ('PAC-00003', 'SOLCA-MANABI', 'HC-004'),
    ('PAC-00004', 'SOLCA-QUITO', 'HC-005'),
    ('PAC-00005', 'SOLCA-MANABI', 'HC-006');

-- -----------------------------------------------------------
-- 2. CONSULTAS (db_consultas)
-- -----------------------------------------------------------
INSERT INTO consultas (id_paciente_regional, sede, fecha_consulta, especialidad, diagnostico, tratamiento, medico, notas, examen_fisico, observaciones, recomendaciones)
VALUES
    ('PAC-00001', 'SOLCA-CUENCA', '2026-01-15', 'Oncología', 'Carcinoma basocelular en etapa temprana', 'Resección quirúrgica programada', 'Dr. Ricardo Merchan', 'Paciente refiere lesión en región malar izquierda de crecimiento lento', 'PA: 120/80, FC: 72, T: 36.5°C. Lesión nodular pigmentada de 8mm en región malar izquierda', 'Paciente en buenas condiciones generales', 'Programar resección ambulatoria, control en 7 días'),
    ('PAC-00001', 'SOLCA-CUENCA', '2026-02-20', 'Cirugía Oncológica', 'Post-operatorio de resección', 'Cuidados de herida, control en 30 días', 'Dr. Ricardo Merchan', 'Paciente asintomático, herida quirúrgica en evolución favorable', 'PA: 118/75, FC: 68, T: 36.6°C. Herida quirúrgica limpia, sin signos de infección', 'Evolución favorable, bordes quirúrgicos negativos', 'Continuar cuidados locales, control en 30 días'),
    ('PAC-00001', 'SOLCA-QUITO', '2026-04-10', 'Radioterapia', 'Evaluación para tratamiento adyuvante', 'Programar sesiones de radioterapia', 'Dra. Patricia Suárez', 'Paciente refiere molestias leves en zona operatoria', 'PA: 122/78, FC: 70, T: 36.4°C. Cicatriz normocrómica', 'No contraindicaciones para radioterapia', 'Iniciar sesiones de radioterapia en 2 semanas'),
    ('PAC-00002', 'SOLCA-CUENCA', '2026-03-05', 'Ginecología Oncológica', 'Nódulo tiroideo sospechoso', 'Biopsia por aspiración con aguja fina', 'Dra. Carmen Torres', 'Paciente refiere aumento de volumen en cuello, disfagia leve', 'PA: 130/85, FC: 76, T: 36.7°C. Nódulo tiroideo derecho palpable de 2cm', 'Se solicita ecografía tiroidea y perfil hormonal', 'Realizar BAAF guiada por ecografía, control en 15 días'),
    ('PAC-00003', 'SOLCA-MANABI', '2026-02-28', 'Oncología', 'Adenocarcinoma de colon Estadio II', 'Colectomía parcial + quimioterapia adyuvante', 'Dr. Luis Zambrano', 'Paciente refiere dolor abdominal tipo cólico, sangrado oculto positivo', 'PA: 140/90, FC: 80, T: 36.8°C. Abdomen blando, doloroso en FID', 'Se confirma diagnóstico por colonoscopia', 'Programar cirugía y valoración prequirúrgica completa'),
    ('PAC-00004', 'SOLCA-QUITO', '2026-01-10', 'Hematología', 'Leucemia linfocítica crónica', 'Protocolo de quimioterapia FCR', 'Dr. Marco Jiménez', 'Paciente asintomática, hallazgo incidental en hemograma', 'PA: 110/70, FC: 65, T: 36.5°C. Adenopatías cervicales y axilares palpables', 'Citometría de flujo confirma LLC', 'Iniciar protocolo FCR, controles semanales'),
    ('PAC-00005', 'SOLCA-MANABI', '2026-03-22', 'Oncología', 'Cáncer de próstata localizado', 'Prostatectomía radical robótica', 'Dr. Luis Zambrano', 'Paciente refiere disuria, nicturia, PSA elevado en control rutinario', 'PA: 145/92, FC: 74, T: 36.6°C. Tacto rectal: próstata aumentada de tamaño, nódulo palpable', 'Biopsia confirmó adenocarcinoma Gleason 7', 'Programar prostatectomía radical robótica, suspender anticoagulantes');

-- -----------------------------------------------------------
-- 3. LABORATORIO (db_laboratorio)
-- -----------------------------------------------------------
INSERT INTO resultados_laboratorio (id_paciente_regional, sede, fecha_ejecucion, tipo_examen, resultado, valores_referencia, medico_solicitante, area, metodo, anormal)
VALUES
    ('PAC-00001', 'SOLCA-CUENCA', '2026-01-14', 'Biometría Hemática', 'Hb: 14.2 g/dL, Leucocitos: 6800/mm³, Plaquetas: 250000/mm³', 'Hb: 13-17 g/dL, Leuc: 4500-11000/mm³', 'Dr. Ricardo Merchan', 'Hematología', 'Automatizado', false),
    ('PAC-00001', 'SOLCA-CUENCA', '2026-01-14', 'Química Sanguínea', 'Glucosa: 95 mg/dL, Creatinina: 0.9 mg/dL, TGP: 28 U/L', 'Glu: 70-110 mg/dL, Creat: 0.6-1.2 mg/dL', 'Dr. Ricardo Merchan', 'Química Clínica', 'Espectrofotometría', false),
    ('PAC-00001', 'SOLCA-CUENCA', '2026-02-19', 'Biometría Hemática', 'Hb: 13.8 g/dL, Leucocitos: 7200/mm³, Plaquetas: 240000/mm³', 'Hb: 13-17 g/dL, Leuc: 4500-11000/mm³', 'Dr. Ricardo Merchan', 'Hematología', 'Automatizado', false),
    ('PAC-00002', 'SOLCA-CUENCA', '2026-03-04', 'Perfil Tiroideo', 'TSH: 6.8 µUI/mL, T4L: 0.9 ng/dL, T3: 120 ng/dL', 'TSH: 0.4-4.0 µUI/mL', 'Dra. Carmen Torres', 'Hormonas', 'Quimioluminiscencia', true),
    ('PAC-00002', 'SOLCA-CUENCA', '2026-03-04', 'Biopsia Tiroidea', 'Categoría Bethesda III - Lesión folicular atípica', 'N/A', 'Dra. Carmen Torres', 'Patología', 'Citología', true),
    ('PAC-00003', 'SOLCA-MANABI', '2026-02-27', 'Biometría Hemática', 'Hb: 11.2 g/dL, Leucocitos: 9200/mm³, Plaquetas: 180000/mm³', 'Hb: 13-17 g/dL', 'Dr. Luis Zambrano', 'Hematología', 'Automatizado', true),
    ('PAC-00003', 'SOLCA-MANABI', '2026-02-27', 'CEA (Antígeno Carcinoembrionario)', 'CEA: 15.8 ng/mL', '< 5.0 ng/mL', 'Dr. Luis Zambrano', 'Marcadores Tumorales', 'Quimioluminiscencia', true),
    ('PAC-00004', 'SOLCA-QUITO', '2026-01-09', 'Citometría de Flujo', 'CD5+/CD19+: 65%, CD23+: 58%', 'N/A', 'Dr. Marco Jiménez', 'Hematología', 'Citometría', true),
    ('PAC-00004', 'SOLCA-QUITO', '2026-01-09', 'Biometría Hemática', 'Hb: 10.5 g/dL, Leucocitos: 45000/mm³, Plaquetas: 150000/mm³', 'Hb: 12-16 g/dL, Leuc: 4500-11000/mm³', 'Dr. Marco Jiménez', 'Hematología', 'Automatizado', true),
    ('PAC-00005', 'SOLCA-MANABI', '2026-03-21', 'PSA Total', 'PSA: 12.4 ng/mL', '< 4.0 ng/mL', 'Dr. Luis Zambrano', 'Marcadores Tumorales', 'Quimioluminiscencia', true),
    ('PAC-00005', 'SOLCA-MANABI', '2026-03-21', 'Biopsia Prostática', 'Adenocarcinoma Gleason 7 (3+4)', 'N/A', 'Dr. Luis Zambrano', 'Patología', 'Histopatología', true);

-- -----------------------------------------------------------
-- 4. IMAGENOLOGIA (db_imagenologia)
-- -----------------------------------------------------------
INSERT INTO estudios_imagen (id_paciente_regional, sede, fecha_estudio, tipo_estudio, formato, url_archivo, nombre_archivo, medico_solicitante, modalidad, descripcion, hallazgos, tamano_bytes)
VALUES
    ('PAC-00001', 'SOLCA-CUENCA', '2026-01-14', 'Ecografía', 'DICOM', '/estudios/PAC-00001/eco-001.dcm', 'eco-parte-blanda.dcm', 'Dr. Ricardo Merchan', 'Ultrasonido', 'Ecografía de partes blandas de región facial', 'Lesión nodular hipoecoica de 8x6 mm en región malar izquierda', 5242880),
    ('PAC-00001', 'SOLCA-CUENCA', '2026-02-19', 'Tomografía', 'DICOM', '/estudios/PAC-00001/tac-001.dcm', 'tac-control-postqx.dcm', 'Dr. Ricardo Merchan', 'TC', 'TAC de control post-quirúrgico', 'Cambios post-quirúrgicos sin evidencia de lesión residual', 15728640),
    ('PAC-00001', 'SOLCA-QUITO', '2026-04-09', 'Resonancia Magnética', 'DICOM', '/estudios/PAC-00001/rm-001.dcm', 'rm-cara-superior.dcm', 'Dra. Patricia Suárez', 'RM', 'RM de planificación de radioterapia', 'Sin hallazgos patológicos significativos', 31457280),
    ('PAC-00002', 'SOLCA-CUENCA', '2026-03-04', 'Ecografía Tiroidea', 'DICOM', '/estudios/PAC-00002/eco-tiroides.dcm', 'eco-tiroides.dcm', 'Dra. Carmen Torres', 'Ultrasonido', 'Ecografía de tiroides con Doppler', 'Nódulo sólido hipoecoico de 15x12x10mm en lóbulo tiroideo derecho con microcalcificaciones (TI-RADS 4)', 6291456),
    ('PAC-00003', 'SOLCA-MANABI', '2026-02-26', 'Colonoscopía Virtual', 'DICOM', '/estudios/PAC-00003/tac-colon.dcm', 'tc-colon-virtual.dcm', 'Dr. Luis Zambrano', 'TC', 'TAC de abdomen con protocolo de colonoscopía virtual', 'Engrosamiento parietal en colon sigmoides de 3cm de extensión con estenosis parcial de la luz', 20971520),
    ('PAC-00004', 'SOLCA-QUITO', '2026-01-08', 'Tomografía', 'DICOM', '/estudios/PAC-00004/tac-torax.dcm', 'tac-torax-abdomen.dcm', 'Dr. Marco Jiménez', 'TC', 'TAC de tórax, abdomen y pelvis', 'Adenopatías mediastinales y retroperitoneales generalizadas', 25165824),
    ('PAC-00005', 'SOLCA-MANABI', '2026-03-20', 'Resonancia Magnética', 'DICOM', '/estudios/PAC-00005/rm-prostata.dcm', 'rm-prostata.dcm', 'Dr. Luis Zambrano', 'RM', 'RM de próstata multiparamétrica', 'Lesión nodular en zona periférica de próstata (PIRADS 5) de 18x15mm con extensión extracapsular', 36700160);
