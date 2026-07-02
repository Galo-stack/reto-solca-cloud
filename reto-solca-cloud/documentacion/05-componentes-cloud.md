# Identificación de Componentes Cloud

## Modelo de Responsabilidad Compartida

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        MODELO CLOUD IMPLEMENTADO                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                     SOFTWARE COMO SERVICIO (SaaS)                   │   │
│  │                                                                     │   │
│  │  ● Microservicio Pacientes        ● Microservicio Repositorio       │   │
│  │  ● Microservicio Consultas        ● phpMyAdmin                      │   │
│  │  ● Microservicio Laboratorio                                        │   │
│  │  ● Microservicio Imagenología                                       │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                      ─                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                     PLATAFORMA COMO SERVICIO (PaaS)                │   │
│  │                                                                     │   │
│  │  ● Spring Boot Framework (Runtime Java)                             │   │
│  │  ● Spring Actuator (Monitoreo y Health Checks)                      │   │
│  │  ● Flyway (Migraciones automáticas de BD)                           │   │
│  │  ● Maven (Build y gestión de dependencias)                          │   │
│  │  ● API REST (Endpoints HTTP)                                        │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                      ─                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                   INFRAESTRUCTURA COMO SERVICIO (IaaS)             │   │
│  │                                                                     │   │
│  │  ● Docker Engine (Contenedores)                                     │   │
│  │  ● Docker Compose (Orquestación local)                              │   │
│  │  ● Red virtual (solca-network)                                      │   │
│  │  ● Volúmenes persistentes para datos                                │   │
│  │  ● Sistema operativo (Alpine Linux / Windows/Linux Host)            │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                      ─                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                   BASE DE DATOS COMO SERVICIO (DBaaS)              │   │
│  │                                                                     │   │
│  │  ● MySQL 8.0 (4 instancias independientes)                         │   │
│  │  ● db_pacientes (Paciente Maestro Regional)                        │   │
│  │  ● db_consultas (Consultas Clínicas)                                │   │
│  │  ● db_laboratorio (Resultados de Laboratorio)                      │   │
│  │  ● db_imagenologia (Estudios de Imagen)                            │   │
│  │  ● phpMyAdmin (Gestión visual de BD)                                │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Desglose por Componente

### IaaS — Infraestructura como Servicio

| Componente | Descripción | Beneficio |
|------------|-------------|-----------|
| **Docker Engine** | Motor de contenedores para empaquetar microservicios | Portabilidad entre entornos, consistencia |
| **Docker Compose** | Orquestación multi-contenedor con un solo comando | Despliegue reproducible con `docker-compose up` |
| **Red solca-network** | Red virtual aislada tipo bridge | Comunicación segura entre contenedores |
| **Volúmenes MySQL** | Almacenamiento persistente para bases de datos | Los datos sobreviven a reinicios de contenedores |
| **Alpine Linux** | Imagen base (5MB) para los contenedores Java | Mínima huella de recursos |

### PaaS — Plataforma como Servicio

| Componente | Descripción |
|------------|-------------|
| **Spring Boot 3.2** | Framework que proporciona auto-configuración, servidor web embebido (Tomcat) |
| **Spring Actuator** | Endpoints de monitoreo: `/actuator/health`, `/actuator/info`, `/actuator/metrics` |
| **Flyway** | Control de versiones de esquemas de base de datos. Migraciones automáticas al iniciar |
| **Hibernate/JPA** | ORM para mapeo objeto-relacional entre entidades Java y tablas MySQL |
| **API REST** | Comunicación basada en HTTP/JSON entre servicios y clientes |
| **WebClient** | Cliente HTTP reactivo para comunicación entre microservicios |

### SaaS — Software como Servicio

| Microservicio | Función | Endpoints |
|---------------|---------|-----------|
| **Pacientes** | Registro y consulta de pacientes | `POST /api/pacientes`, `GET /api/pacientes/{id}`, `GET /api/pacientes/cedula/{cedula}` |
| **Consultas** | Gestión de consultas clínicas | `POST /api/consultas`, `GET /api/consultas/paciente/{id}`, `GET /api/consultas/{id}` |
| **Laboratorio** | Resultados de exámenes | `POST /api/laboratorio`, `GET /api/laboratorio/paciente/{id}`, `GET /api/laboratorio/{id}` |
| **Imagenología** | Estudios de imagen (PACS) | `POST /api/imagenes`, `GET /api/imagenes/paciente/{id}`, `GET /api/imagenes/{id}` |
| **Repositorio** | Agregación de datos del paciente | `GET /api/repositorio/paciente/{id}` |
| **phpMyAdmin** | Interfaz web para administrar MySQL | `http://localhost:8080` |

### DBaaS — Base de Datos como Servicio

| Instancia | Base de Datos | Puerto | Tablas |
|-----------|--------------|--------|--------|
| MySQL Pacientes | `db_pacientes` | 3306/3306 | pacientes, historias_locales |
| MySQL Consultas | `db_consultas` | 3307/3306 | consultas |
| MySQL Laboratorio | `db_laboratorio` | 3308/3306 | resultados_laboratorio |
| MySQL Imagenología | `db_imagenologia` | 3309/3306 | estudios_imagen |

## Beneficios del Modelo Cloud

1. **Escalabilidad**: Cada microservicio escala independientemente
2. **Disponibilidad**: Health checks y reinicio automático de contenedores
3. **Mantenibilidad**: Despliegue independiente por servicio
4. **Portabilidad**: Ejecutable en cualquier proveedor cloud (AWS, GCP, Azure)
5. **Aislamiento**: Fallos limitados a un solo servicio sin afectar al resto
