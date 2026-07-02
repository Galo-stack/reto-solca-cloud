# Avance 2 — Repositorio Clínico Regional e Integración

## Documentos entregados

| # | Documento | Descripción |
|---|-----------|-------------|
| 1 | [Diagrama de Integración](./01-diagrama-integracion-microservicios.md) | Diagrama actualizado de integración entre microservicios, flujo de datos y principios |
| 2 | [Comunicación APIs REST](./02-comunicacion-api-rest.md) | Explicación detallada de la comunicación REST, uso de WebClient, formato de respuestas y manejo de errores |
| 3 | [Infraestructura de Datos](./03-infraestructura-datos.md) | Datos estructurados (DBaaS), no estructurados (Cloud Storage), estrategia de respaldo |
| 4 | [Matriz de Riesgos](./04-matriz-riesgos.md) | Identificación y evaluación de 6 riesgos con mapa de calor y plan de acción |
| 5 | [Evidencia de Integración](./05-evidencia-integracion.md) | JSON consolidado, llamadas REST, verificación de datos en BD, manejo de errores |

## Implementación

El Repositorio Clínico Regional está implementado en el microservicio `microservicio-repositorio` (puerto 8085).

### Endpoint principal

```
GET /api/repositorio/paciente/{idPacienteRegional}
```

### Consumo de microservicios

El repositorio consume los 4 microservicios de dominio vía REST (WebClient reactivo):

- **Pacientes** → `GET /api/pacientes/{id}`
- **Consultas** → `GET /api/consultas/paciente/{id}`
- **Laboratorio** → `GET /api/laboratorio/paciente/{id}`
- **Imagenología** → `GET /api/imagenes/paciente/{id}`

### Características

- ✅ Comunicación exclusiva por APIs REST
- ✅ Sin acceso directo a bases de datos
- ✅ Timeouts configurables (5s connect, 10s read)
- ✅ Tolerancia a fallos (respuesta parcial si un servicio falla)
- ✅ Manejo de errores HTTP y de conexión
- ✅ Health checks con Spring Actuator
