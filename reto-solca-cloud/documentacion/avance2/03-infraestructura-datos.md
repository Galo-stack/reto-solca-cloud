# Avance 2 — Infraestructura de Datos

## Clasificación de Datos

```
                    ┌─────────────────────────────────────────────┐
                    │         DATOS DEL PACIENTE ONCOLOGICO       │
                    ├──────────────────┬──────────────────────────┤
                    │   ESTRUCTURADOS  │     NO ESTRUCTURADOS     │
                    │   (Bases de      │     (Archivos, imágenes, │
                    │    datos SQL)    │      documentos)         │
                    ├──────────────────┼──────────────────────────┤
                    │ • Datos          │ • Imágenes DICOM         │
                    │   demográficos   │   (Rayos X, TAC,         │
                    │ • Cédula         │    Resonancias)          │
                    │ • Nombres        │ • Informes en PDF        │
                    │ • Fecha nac.     │ • Historiales clínicos   │
                    │ • Género         │   escaneados             │
                    │ • Teléfono       │ • Fotografías            │
                    │ • Email          │   dermatológicas         │
                    │ • Dirección      │ • Consentimientos        │
                    │ • Consultas      │   informados             │
                    │   clínicas       │ • Reportes de            │
                    │ • Exámenes de    │   laboratorio en PDF     │
                    │   laboratorio    │                          │
                    │ • Estudios de    │                          │
                    │   imagenología   │                          │
                    └──────────────────┴──────────────────────────┘
```

## Datos Estructurados (DBaaS)

### Estrategia: Base de Datos por Servicio (Database per Service)

Cada microservicio posee su propia base de datos MySQL 8.0, garantizando **aislamiento** y **desacoplamiento**.

```
┌──────────────────────────────────────────────────────────────┐
│                    CAPA DBaaS (Database as a Service)         │
│                                                              │
│  ┌────────────────────┐  ┌────────────────────┐              │
│  │  db_pacientes      │  │  db_consultas      │              │
│  │  MySQL 8.0         │  │  MySQL 8.0         │              │
│  │  Puerto: 3306      │  │  Puerto: 3306      │              │
│  │  Tabla:            │  │  Tabla:            │              │
│  │  pacientes         │  │  consultas         │              │
│  └────────────────────┘  └────────────────────┘              │
│                                                              │
│  ┌────────────────────┐  ┌────────────────────┐              │
│  │  db_laboratorio    │  │  db_imagenologia   │              │
│  │  MySQL 8.0         │  │  MySQL 8.0         │              │
│  │  Puerto: 3306      │  │  Puerto: 3306      │              │
│  │  Tabla:            │  │  Tabla:            │              │
│  │  resultados_       │  │  estudios_imagen   │              │
│  │  laboratorio       │  │                    │              │
│  └────────────────────┘  └────────────────────┘              │
│                                                              │
│  Motor: InnoDB (transacciones ACID, FK, row-level locking)   │
│  Migraciones: Flyway (versionado automático de esquemas)     │
│  Acceso: Solo desde el microservicio propietario             │
└──────────────────────────────────────────────────────────────┘
```

### Gestión de Esquemas con Flyway

Cada microservicio tiene migraciones Flyway en `src/main/resources/db/migration/`:

```sql
-- V1__create_paciente_table.sql
CREATE TABLE pacientes (
    id_paciente_regional VARCHAR(20) PRIMARY KEY,
    cedula VARCHAR(10) UNIQUE NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    genero VARCHAR(1) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(200)
);
```

| Microservicio | Tabla Principal | Columnas Clave |
|--------------|----------------|----------------|
| Pacientes | pacientes | id_paciente_regional, cedula, nombres, apellidos, fecha_nacimiento, genero |
| Consultas | consultas | id, id_paciente_regional, especialidad, diagnostico, medico |
| Laboratorio | resultados_laboratorio | id, id_paciente_regional, tipo_examen, resultado, area |
| Imagenología | estudios_imagen | id, id_paciente_regional, tipo_estudio, modalidad, hallazgos |

## Datos No Estructurados (Cloud Storage)

### Estrategia: Almacenamiento de Objetos (S3-Compatible)

Los archivos médicos (imágenes DICOM, PDFs) se almacenan en **cloud storage** con referencia desde la base de datos:

```
┌──────────────────────────────────────────────────────────────────┐
│            CLOUD STORAGE (S3-Compatible / Azure Blob)            │
│                                                                  │
│  Bucket: solca-imagenologia                                      │
│  ├── pacientes/PAC-00001/                                        │
│  │   ├── rx-torax-20260701.dcm                                   │
│  │   ├── tac-abdomen-20260701.dcm                                │
│  │   └── resonancia-cerebro-20260701.dcm                         │
│  ├── pacientes/PAC-00002/                                        │
│  │   └── ...                                                     │
│  └── informes/                                                   │
│      ├── informe-alta-PAC-00001-20260701.pdf                     │
│      └── consentimiento-PAC-00001-20260701.pdf                   │
│                                                                  │
│  Backup: Replicación entre regiones (multi-AZ)                   │
│  Retención: 10 años (regulatorio)                                │
│  Clase: S3 Standard + S3 Glacier para datos > 1 año              │
└──────────────────────────────────────────────────────────────────┘
```

**Referencia desde la BD:**

```sql
CREATE TABLE estudios_imagen (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    tipo_estudio VARCHAR(50),
    url_archivo VARCHAR(500),     -- ← URL al objeto en cloud storage
    nombre_archivo VARCHAR(200),  -- ← nombre original del archivo
    tamano_bytes BIGINT,
    -- ...
);
```

## DBaaS (Database as a Service)

### Beneficios del modelo DBaaS

| Característica | Beneficio |
|---------------|-----------|
| **Aislamiento** | Cada microservicio tiene su propia BD. Un fallo en una no afecta a las demás. |
| **Escalabilidad independiente** | Cada BD puede escalarse (CPU/RAM/almacenamiento) según demanda del servicio. |
| **Versionado de esquemas** | Flyway permite migraciones automatizadas y reversibles. |
| **Alta disponibilidad** | MySQL replication + backups automáticos. |
| **Seguridad por capas** | Cada microservicio solo accede a su propia BD con credenciales específicas. |

### Configuración de conexión (por microservicio)

```yaml
# application.yml de cada microservicio
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db_pacientes
    username: root
    password: galito2002
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: validate      # Solo validación, Flyway crea tablas
    show-sql: false
```

## Cloud Storage

### Comparativa de proveedores

| Proveedor | Servicio | Clase de almacenamiento | Ideal para |
|-----------|----------|------------------------|------------|
| AWS | S3 | Standard + Glacier | DICOM, PDFs, backups |
| Azure | Blob Storage | Hot + Cool + Archive | Informes, imágenes |
| Google Cloud | Cloud Storage | Standard + Nearline + Coldline | Datos clínicos |

### Estrategia de respaldo

```
┌─────────────────────────────────────────────┐
│         POLITICA DE RESPALDO                │
├─────────────────────────────────────────────┤
│ • Backups diarios de todas las BD (mysqldump)│
│ • Retención: 30 días diarios, 12 meses     │
│   mensuales                                 │
│ • Replicación multi-AZ para cloud storage   │
│ • Pruebas de restauración: cada 3 meses    │
│ • Cifrado en reposo (AES-256) y en tránsito │
│   (TLS 1.2+)                                │
└─────────────────────────────────────────────┘
```

## Matriz Almacenamiento vs Tipo de Dato

| Tipo de Dato | Almacenamiento | Estructura | Ejemplo |
|-------------|---------------|-----------|---------|
| Datos demográficos | MySQL (DBaaS) | Tabla: pacientes | Cédula, nombres, fecha nac. |
| Registros clínicos | MySQL (DBaaS) | Tabla: consultas | Diagnóstico, tratamiento |
| Resultados laboratorio | MySQL (DBaaS) | Tabla: resultados_laboratorio | Tipo examen, valores |
| Metadatos de imágenes | MySQL (DBaaS) | Tabla: estudios_imagen | URL, modalidad, fecha |
| Archivos DICOM | Cloud Storage (S3) | Bucket → carpeta paciente | .dcm, .zip |
| Informes PDF | Cloud Storage (S3) | Bucket → informes/ | .pdf |
| Logs de aplicación | Sistema de archivos / ELK | Log rotado | .log |
