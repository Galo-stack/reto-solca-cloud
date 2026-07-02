# Avance 2 — Diagrama de Integración entre Microservicios

## Arquitectura General

```
                    ╔═══════════════════════════════════╗
                    ║     CLIENTE EXTERNO (Postman)     ║
                    ╚══════════════════════╦════════════╝
                                           ║
                                           ▼
                    ╔═══════════════════════════════════╗
                    ║        API GATEWAY (Opcional)     ║
                    ║   Enruta, autentica, rate-limit   ║
                    ╚══════════════════════╦════════════╝
                                           ║
                    ┌──────────────────────╫──────────────────────┐
                    │                      ║                      │
                    ▼                      ▼                      ▼
        ╔═══════════════════╗  ╔═══════════════════╗  ╔═══════════════════╗
        ║   Microservicio   ║  ║   Microservicio   ║  ║   Microservicio   ║
        ║    PACIENTES      ║  ║    CONSULTAS      ║  ║   LABORATORIO     ║
        ║   Puerto: 8081    ║  ║   Puerto: 8082    ║  ║   Puerto: 8083    ║
        ║   /api/pacientes  ║  ║   /api/consultas  ║  ║   /api/laboratorio║
        ╚══════════╦════════╝  ╚══════════╦════════╝  ╚══════════╦════════╝
                   ║                       ║                       ║
                   ▼                       ▼                       ▼
        ╔═══════════════════╗  ╔═══════════════════╗  ╔═══════════════════╗
        ║  MySQL db_pacientes║  ║ MySQL db_consultas║  ║MySQL db_laboratorio║
        ║  Tabla: pacientes  ║  ║ Tabla: consultas  ║  ║resultados_lab.    ║
        ╚═══════════════════╝  ╚═══════════════════╝  ╚═══════════════════╝

        ╔═══════════════════════════════════════════════════════════════╗
        ║                    Microservicio                             ║
        ║               IMAGENOLOGIA                                   ║
        ║               Puerto: 8084                                   ║
        ║               /api/imagenes                                  ║
        ╚══════════════════╦═══════════════════════════════════════════╝
                           ║
                           ▼
        ╔═══════════════════════════════════════════════════════════════╗
        ║            MySQL db_imagenologia                              ║
        ║            Tabla: estudios_imagen                             ║
        ╚═══════════════════════════════════════════════════════════════╝

    ╔═══════════════════════════════════════════════════════════════════════╗
    ║              REPOSITORIO CLINICO REGIONAL                            ║
    ║                  Puerto: 8085                                         ║
    ║              /api/repositorio/paciente/{id}                           ║
    ║                                                                       ║
    ║   ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐            ║
    ║   │ WebClient│  │ WebClient│  │ WebClient│  │ WebClient│            ║
    ║   │:8081/api │  │:8082/api │  │:8083/api │  │:8084/api │            ║
    ║   └─────┬────┘  └─────┬────┘  └─────┬────┘  └─────┬────┘            ║
    ║         │              │              │              │                ║
    ║         └──────────────┴──────────────┴──────────────┘                ║
    ║                    (Llamadas REST sincronas)                          ║
    ╚═══════════════════════════════════════════════════════════════════════╝
```

## Flujo de Datos

```
POSTMAN / CLIENTE
     │
     │ GET /api/repositorio/paciente/PAC-00001
     ▼
┌─────────────────────────────────────────────────┐
│          REPOSITORIO CLINICO REGIONAL            │
│                                                  │
│  1. GET /api/pacientes/PAC-00001  ──────────► PACIENTES (8081)
│  2. GET /api/consultas/paciente/PAC-00001 ───► CONSULTAS  (8082)
│  3. GET /api/laboratorio/paciente/PAC-00001 ──► LABORATORIO (8083)
│  4. GET /api/imagenes/paciente/PAC-00001  ───► IMAGENOLOGIA (8084)
│                                                  │
│  ┌─────────────────────────────────────────────┐ │
│  │     CONSOLIDA RESPUESTAS                    │ │
│  │  {                                          │ │
│  │    "paciente":    {...}   ←  de pacientes   │ │
│  │    "consultas":   [...]   ←  de consultas   │ │
│  │    "laboratorio": [...]   ←  de laboratorio │ │
│  │    "imagenes":    [...]   ←  de imagenologia│ │
│  │    "status": "COMPLETE"                     │ │
│  │  }                                          │ │
│  └─────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────┘
     │
     │ JSON Consolidado
     ▼
    CLIENTE
```

## Principios de Integración

| Principio | Implementación |
|-----------|---------------|
| **Desacoplamiento** | Cada microservicio tiene su propia BD y API. El repositorio solo conoce URLs y contratos REST. |
| **Tolerancia a Fallos** | Si un servicio falla, el repositorio devuelve datos parciales con `status: "PARTIAL"` y errores detallados. |
| **API First** | Todos los servicios se comunican exclusivamente por REST/HTTP. No hay acceso directo a bases de datos. |
| **Contrato Uniforme** | Cada microservicio expone endpoints REST con respuesta en formato `{"data": ..., "status": "success"}`. |
| **Escalabilidad Horizontal** | Cada servicio puede escalarse independientemente según demanda. |
