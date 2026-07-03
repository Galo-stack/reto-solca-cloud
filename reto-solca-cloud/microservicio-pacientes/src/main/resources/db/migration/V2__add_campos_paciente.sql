ALTER TABLE pacientes
  ADD COLUMN grupo_sanguineo VARCHAR(5) DEFAULT '',
  ADD COLUMN estado_civil VARCHAR(20) DEFAULT '',
  ADD COLUMN ocupacion VARCHAR(100) DEFAULT '',
  ADD COLUMN alergias TEXT,
  ADD COLUMN enfermedad_actual TEXT,
  ADD COLUMN antecedentes_personales TEXT,
  ADD COLUMN antecedentes_familiares TEXT,
  ADD COLUMN contacto_emergencia TEXT;
