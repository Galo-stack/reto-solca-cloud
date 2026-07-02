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
