CREATE TABLE IF NOT EXISTS pacientes (
    id_paciente_regional VARCHAR(20) PRIMARY KEY,
    cedula VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    genero VARCHAR(1),
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS historias_locales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL,
    id_historia_local VARCHAR(20) NOT NULL,
    fecha_asociacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paciente_regional) REFERENCES pacientes (id_paciente_regional) ON DELETE CASCADE,
    UNIQUE KEY unique_historia_local (sede, id_historia_local)
);