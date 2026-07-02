# Avance 2 — Matriz Preliminar de Riesgos

## Matriz de Probabilidad vs Impacto

```
                ┌──────────┬──────────┬──────────┬──────────┐
                │  MUY BAJO│   BAJO   │  MEDIO   │   ALTO   │
                │  (1)     │   (2)    │   (3)    │   (4)    │
┌───────────────┼──────────┼──────────┼──────────┼──────────┤
│  MUY ALTO (4) │    4     │    8     │    12    │    16    │
│  ALTO (3)     │    3     │    6     │    9      │    12    │
│  MEDIO (2)    │    2     │    4     │    6      │    8     │
│  BAJO (1)     │    1     │    2     │    3      │    4     │
└───────────────┴──────────┴──────────┴──────────┴──────────┘

    Leyenda:
      1 - 4   = Bajo (Aceptable)
      5 - 9   = Medio (Requiere monitoreo)
     10 - 16  = Alto (Requiere mitigación inmediata)
```

## Identificación y Evaluación de Riesgos

### Riesgo 1: Caída de un microservicio de dominio
| Campo | Valor |
|-------|-------|
| **Descripción** | El servicio de Pacientes, Consultas, Laboratorio o Imagenología deja de responder |
| **Causa** | Error en la aplicación, falta de memoria, error de configuración |
| **Probabilidad** | 3 (Alta) |
| **Impacto** | 3 (Alto) |
| **Nivel de Riesgo** | **9 — Medio** |
| **Mitigación** | • Timeouts de 5s en WebClient<br>• Tolerancia a fallos: respuesta parcial con status PARTIAL<br>• Logs detallados para diagnóstico rápido<br>• Health checks con Spring Actuator |
| **Plan de Contingencia** | Reinicio del contenedor/servicio. Si persiste, escalar a operaciones. |

### Riesgo 2: Timeout de conexión entre servicios
| Campo | Valor |
|-------|-------|
| **Descripción** | El Repositorio no recibe respuesta de un microservicio dentro del tiempo límite |
| **Causa** | Red lenta, microservicio sobrecargado, base de datos lenta |
| **Probabilidad** | 3 (Alta) |
| **Impacto** | 2 (Medio) |
| **Nivel de Riesgo** | **6 — Medio** |
| **Mitigación** | • Connect timeout: 5 segundos<br>• Read timeout: 10 segundos<br>• Pool de conexiones con pendingAcquireTimeout |
| **Plan de Contingencia** | Aumentar timeouts temporalmente si hay picos de carga. Escalar horizontalmente. |

### Riesgo 3: Pérdida de datos en base de datos
| Campo | Valor |
|-------|-------|
| **Descripción** | Corrupción o pérdida de datos en una de las 4 bases MySQL |
| **Causa** | Fallo de disco, error humano (DROP accidental), bug en migración |
| **Probabilidad** | 1 (Baja) |
| **Impacto** | 4 (Muy Alto) |
| **Nivel de Riesgo** | **4 — Bajo** |
| **Mitigación** | • Backups diarios automatizados (mysqldump)<br>• Flyway con versionado de esquemas<br>• Base por servicio: el fallo de una BD no afecta las demás |
| **Plan de Contingencia** | Restaurar desde backup más reciente. Pruebas de restauración trimestrales. |

### Riesgo 4: Inconsistencia de datos entre servicios
| Campo | Valor |
|-------|-------|
| **Descripción** | Un paciente existe en Pacientes pero sus consultas referencian un ID inexistente |
| **Causa** | Eliminación manual en una BD, bug en lógica de negocio |
| **Probabilidad** | 2 (Media) |
| **Impacto** | 2 (Medio) |
| **Nivel de Riesgo** | **4 — Bajo** |
| **Mitigación** | • WebClient.onStatus() para 4xx: devuelve vacío en lugar de error<br>• El repositorio retorna arrays vacíos si no hay datos del paciente en un servicio<br>• Registro de auditoría en logs |
| **Plan de Contingencia** | Proceso ETL trimestral para validar consistencia entre bases de datos. |

### Riesgo 5: Fallo de red / conectividad
| Campo | Valor |
|-------|-------|
| **Descripción** | Problemas de red entre el Repositorio y los microservicios |
| **Causa** | Firewall, DNS, contenedores en diferentes hosts, VPN caída |
| **Probabilidad** | 2 (Media) |
| **Impacto** | 3 (Alto) |
| **Nivel de Riesgo** | **6 — Medio** |
| **Mitigación** | • Todos los servicios en misma red Docker (solca-network)<br>• URLs configuradas con variables de entorno<br>• WebClient detecta errores de conexión vs errores HTTP |
| **Plan de Contingencia** | Verificar conectividad con `curl` o `telnet`. Usar nombres de contenedor Docker. |

### Riesgo 6: Bug en la lógica de consolidación
| Campo | Valor |
|-------|-------|
| **Descripción** | Error al mapear/consolidar respuestas de múltiples servicios |
| **Causa** | Cambio en contrato de API de un microservicio, error en extracción de "data" |
| **Probabilidad** | 2 (Media) |
| **Impacto** | 2 (Medio) |
| **Nivel de Riesgo** | **4 — Bajo** |
| **Mitigación** | • Formato uniforme: `{ data: ..., status: ... }`<br>• Logs detallados con DEBUG<br>• Pruebas de integración periódicas |
| **Plan de Contingencia** | Revisar contratos de API. Corregir mapeo en RepositorioService. |

## Mapa de Calor de Riesgos

```
Probabilidad
    ▲
  4 │        │        │        │        │
    │        │        │        │        │
  3 │        │        │   R1   │        │
    │        │        │        │        │
  2 │        │   R4   │   R2   │        │
    │        │   R6   │   R5   │        │
  1 │        │        │        │   R3   │
    │        │        │        │        │
    └────────┴────────┴────────┴────────► Impacto
        1        2        3        4

    R1 = Caída de microservicio       (9)
    R2 = Timeout de conexión           (6)
    R3 = Pérdida de datos              (4)
    R4 = Inconsistencia de datos       (4)
    R5 = Fallo de red                  (6)
    R6 = Bug en consolidación          (4)
```

## Plan de Acción

| Riesgo | Acción | Responsable | Fecha Límite |
|--------|--------|-------------|-------------|
| **R1** | Implementar circuit breaker (Resilience4j) | DevOps | Próximo sprint |
| **R2** | Monitorear tiempos de respuesta con metrics | Backend | Continuo |
| **R3** | Automatizar backups y prueba de restauración | DBA | Semanal |
| **R4** | Crear script de validación de integridad | Backend | Próximo sprint |
| **R5** | Documentar topología de red y puertos | DevOps | Una vez |
| **R6** | Escribir tests de integración para el repositorio | QA | Próximo sprint |
