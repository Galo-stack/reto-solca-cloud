# Diseño de Bases de Datos Independientes

## Estrategia

Cada microservicio posee su propia base de datos MySQL 8.0 independiente (patrón **Database per Service**). Esto asegura:
- **Desacoplamiento total** entre dominios
- **Escalabilidad independiente** por servicio
- **Aislamiento de fallos**: un problema en una BD no afecta a las demás
- **Evolución tecnológica** independiente por servicio

## Bases de Datos

| Microservicio | Base de Datos | Puerto MySQL | Tablas |
|---------------|---------------|--------------|--------|
| Pacientes | `db_pacientes` | 3306 | `pacientes`, `historias_locales` |
| Consultas | `db_consultas` | 3306 | `consultas` |
| Laboratorio | `db_laboratorio` | 3306 | `resultados_laboratorio` |
| Imagenología | `db_imagenologia` | 3306 | `estudios_imagen` |

*Nota: En entorno Docker cada base de datos usa su propio contenedor MySQL en puertos separados (3306-3309).*

## Esquemas

### 1. Base: db_pacientes

```sql
CREATE TABLE pacientes (
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

CREATE TABLE historias_locales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL,
    id_historia_local VARCHAR(20) NOT NULL,
    fecha_asociacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paciente_regional) REFERENCES pacientes(id_paciente_regional) ON DELETE CASCADE,
    UNIQUE KEY unique_historia_local (sede, id_historia_local)
);
```

### 2. Base: db_consultas

```sql
CREATE TABLE consultas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL,
    fecha_consulta DATE NOT NULL,
    especialidad VARCHAR(50) NOT NULL,
    diagnostico TEXT,
    tratamiento TEXT,
    medico VARCHAR(100) NOT NULL,
    notas TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_paciente (id_paciente_regional),
    INDEX idx_fecha (fecha_consulta),
    INDEX idx_sede (sede),
    INDEX idx_especialidad (especialidad)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 3. Base: db_laboratorio

```sql
CREATE TABLE resultados_laboratorio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL,
    fecha_ejecucion DATE NOT NULL,
    tipo_examen VARCHAR(100) NOT NULL,
    resultado TEXT NOT NULL,
    valores_referencia TEXT,
    medico_solicitante VARCHAR(100),
    area VARCHAR(50),
    metodo VARCHAR(100),
    observaciones TEXT,
    fecha_resultado DATE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    anormal BOOLEAN DEFAULT FALSE,
    INDEX idx_paciente (id_paciente_regional),
    INDEX idx_fecha (fecha_ejecucion),
    INDEX idx_sede (sede),
    INDEX idx_examen (tipo_examen)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 4. Base: db_imagenologia

```sql
CREATE TABLE estudios_imagen (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL,
    fecha_estudio DATE NOT NULL,
    tipo_estudio VARCHAR(50) NOT NULL,
    formato VARCHAR(10) NOT NULL,
    url_archivo VARCHAR(500) NOT NULL,
    nombre_archivo VARCHAR(255),
    medico_solicitante VARCHAR(100),
    modalidad VARCHAR(50),
    descripcion TEXT,
    hallazgos TEXT,
    tamano_bytes BIGINT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_paciente (id_paciente_regional),
    INDEX idx_fecha (fecha_estudio),
    INDEX idx_sede (sede),
    INDEX idx_estudio (tipo_estudio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

## Migraciones con Flyway

Todas las migraciones se gestionan automáticamente con Flyway:
- Archivos SQL versionados en `src/main/resources/db/migration/V1__*.sql`
- Ejecución automática al iniciar cada microservicio
- Historial de migraciones en tabla `flyway_schema_history`

## Relación entre Bases de Datos

No existen foreign keys entre bases de datos diferentes. La integridad referencial se mantiene a nivel de aplicación mediante el `idPacienteRegional`, que actúa como **clave de integración** entre todos los servicios.
