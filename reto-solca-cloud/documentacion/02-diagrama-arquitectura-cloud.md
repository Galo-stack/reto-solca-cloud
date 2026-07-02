# Diagrama de Arquitectura Cloud - Repositorio Clínico Regional SOLCA

## Diagrama de Arquitectura

```
                    ┌─────────────────────────────────────────────────────────────┐
                    │                    INTERNET / VPN                           │
                    └──────────────────────┬──────────────────────────────────────┘
                                           │
                    ┌──────────────────────┴──────────────────────────────────────┐
                    │                    API GATEWAY                              │
                    │              (Balanceador de Carga)                         │
                    └──┬──────────┬──────────┬──────────┬──────────┬──────────────┘
                       │          │          │          │          │
              ┌────────┴─┐  ┌────┴────┐  ┌──┴────┐  ┌──┴────┐  ┌─┴──────────┐
              │  8081    │  │  8082   │  │ 8083  │  │ 8084  │  │   8085     │
              │Pacientes │  │Consultas│  │Lab.   │  │Imagen.│  │Repositorio │
              │  SaaS    │  │  SaaS   │  │ SaaS  │  │ SaaS  │  │   SaaS     │
              └────┬─────┘  └────┬────┘  └───┬───┘  └───┬───┘  └──────┬─────┘
                   │              │           │           │              │
              ┌────┴─────┐  ┌────┴────┐  ┌───┴───┐  ┌───┴───┐          │
              │ DBaaS    │  │ DBaaS   │  │ DBaaS │  │ DBaaS │          │
              │ MySQL 8.0│  │ MySQL8.0│  │MySQL8 │  │MySQL8 │          │
              │db_pacient│  │db_consul│  │db_lab │  │db_imag│          │
              └──────────┘  └─────────┘  └───────┘  └───────┘          │
                                                                        │
              ┌──────────────────────────────────────────────────────────┘
              │
     ┌────────┴──────────────────────────────────────────────────────┐
     │                    MONITOREO (PaaS)                           │
     │              Spring Actuator + Health Checks                  │
     └──────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                             INFRAESTRUCTURA (IaaS)                          │
│                  Docker Compose / Docker Swarm / Kubernetes                │
│                  Almacenamiento persistente (volúmenes)                    │
│                  Red privada (solca-network)                               │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Capas de la Arquitectura

### 1. Capa de Infraestructura (IaaS)
- **Docker Engine**: Contenedorización de todos los servicios
- **Volúmenes persistentes**: Almacenamiento de datos MySQL
- **Red virtual**: `solca-network` para comunicación interna entre contenedores
- **Orquestación**: Docker Compose para desarrollo, Docker Swarm/Kubernetes para producción

### 2. Capa de Plataforma (PaaS)
- **Spring Boot 3.2**: Framework de desarrollo de microservicios
- **Actuator**: Monitoreo de salud de cada servicio
- **Flyway**: Migraciones de base de datos automáticas
- **Maven**: Gestión de dependencias y construcción

### 3. Capa de Software (SaaS)
- **Microservicio Pacientes**: Gestión del maestro de pacientes
- **Microservicio Consultas**: Historial de consultas clínicas
- **Microservicio Laboratorio**: Resultados de laboratorio
- **Microservicio Imagenología**: Estudios de imagen (PACS simulado)
- **Microservicio Repositorio**: Agregación y consulta consolidada

### 4. Capa de Base de Datos (DBaaS)
- **MySQL 8.0**: Motor de base de datos relacional
- **4 bases independientes**: Una por dominio de negocio
- **Flyway Migrations**: Control de versiones de esquemas

## Flujo de Datos

```
Petición HTTP → API Gateway → Microservicio específico → MySQL propia
                              ↓
                    Repositorio Clínico → Consulta a todos los servicios
                                       → Consolidación de resultados
                                       → Respuesta única al cliente
```

## Principios de Diseño

- **Desacoplamiento**: Cada microservicio tiene su propia base de datos
- **Tolerancia a fallos**: Si un servicio falla, el repositorio responde con datos parciales
- **Escalabilidad horizontal**: Cada servicio puede escalar independientemente
- **API First**: Comunicación exclusivamente por API REST
- **Seguridad por capas**: Red interna, autenticación por servicio, HTTPS
