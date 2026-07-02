# Avance 2 — Evidencia de Integración

## 1. Llamadas REST entre servicios

El **Repositorio Clínico Regional** (puerto 8085) realiza llamadas REST a los 4 microservicios de dominio utilizando **Spring WebClient** (reactivo):

| Origen | Destino | Endpoint | Método |
|--------|---------|----------|--------|
| Repositorio (8085) | Pacientes (8081) | `/api/pacientes/PAC-00001` | GET |
| Repositorio (8085) | Consultas (8082) | `/api/consultas/paciente/PAC-00001` | GET |
| Repositorio (8085) | Laboratorio (8083) | `/api/laboratorio/paciente/PAC-00001` | GET |
| Repositorio (8085) | Imagenología (8084) | `/api/imagenes/paciente/PAC-00001` | GET |

**Código de la llamada REST** (RepositorioService.java):

```java
private List<Map<String, Object>> obtenerLista(WebClient client, String uri) {
    Map<String, Object> wrapped = client.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(Map.class)
            .block();
    Object data = wrapped != null ? wrapped.get("data") : null;
    return data != null ? (List<Map<String, Object>>) data : new ArrayList<>();
}
```

## 2. Respuesta JSON Consolidada

**Endpoint:** `GET /api/repositorio/paciente/PAC-00001`

**Respuesta (200 OK):**

```json
{
  "paciente": {
    "idPacienteRegional": "PAC-00001",
    "cedula": "0102030405",
    "nombres": "Juan Carlos",
    "apellidos": "Perez Gonzalez",
    "fechaNacimiento": "1990-05-15",
    "genero": "M",
    "telefono": "0999123456",
    "email": "juan@email.com",
    "direccion": "Cuenca"
  },
  "consultas": [
    {
      "id": 1,
      "idPacienteRegional": "PAC-00001",
      "sede": "SOLCA-CUENCA",
      "fechaConsulta": "2026-07-01",
      "especialidad": "MEDICINA GENERAL",
      "diagnostico": "Cefalea",
      "tratamiento": "AINEs",
      "medico": "Dr. Rivera",
      "notas": "Estable"
    }
  ],
  "laboratorio": [
    {
      "id": 1,
      "idPacienteRegional": "PAC-00001",
      "sede": "SOLCA-CUENCA",
      "fechaEjecucion": "2026-07-01",
      "tipoExamen": "HEMOGRAMA",
      "resultado": "Hb:15.3",
      "valoresReferencia": "13-17",
      "medicoSolicitante": "Dra.Torres",
      "area": "HEMATO",
      "metodo": "FLUJO",
      "observaciones": "Normal",
      "fechaResultado": "2026-07-01",
      "anormal": false
    }
  ],
  "imagenes": [
    {
      "id": 1,
      "idPacienteRegional": "PAC-00001",
      "sede": "SOLCA-CUENCA",
      "fechaEstudio": "2026-07-01",
      "tipoEstudio": "RX TORAX",
      "formato": "DICOM",
      "urlArchivo": "https://pacs.solca.ec/t.dcm",
      "nombreArchivo": "t.dcm",
      "medicoSolicitante": "Dr.Rivera",
      "modalidad": "RX",
      "descripcion": "Rx torax",
      "hallazgos": "Normal",
      "tamanoBytes": 1024
    }
  ],
  "status": "COMPLETE",
  "errores": {}
}
```

## 3. Sin Acceso Directo a Bases de Datos

El repositorio **no tiene** ninguna dependencia de base de datos:

- **No** incluye `mysql-connector-j`, `spring-boot-starter-data-jpa`, ni `flyway-core`
- **No** tiene configuración `spring.datasource` en `application.yml`
- **No** tiene anotaciones `@Entity`, `@Table`, `@Repository`, ni `@Transactional`
- Su única comunicación es REST/HTTP mediante WebClient

**Dependencias del repositorio:**
```
- spring-boot-starter-web        (API REST)
- spring-boot-starter-webflux    (WebClient reactivo)
- spring-boot-starter-actuator   (Health checks)
- lombok                         (Código boilerplate)
- spring-boot-starter-test       (Tests)
- reactor-test                   (Testing reactivo)
```

**Sin:** `mysql`, `jpa`, `jdbc`, `flyway`, `hikari`

## 4. Manejo Básico de Errores

### Caso: Servicio no disponible

Cuando un microservicio no responde, el repositorio captura la excepción y retorna datos parciales:

```json
{
  "paciente": { ... },
  "consultas": [],
  "laboratorio": [],
  "imagenes": [],
  "status": "PARTIAL",
  "errores": {
    "consultas": "SERVICIO NO DISPONIBLE",
    "laboratorio": "SERVICIO NO DISPONIBLE",
    "imagenes": "SERVICIO NO DISPONIBLE"
  }
}
```

### Tipos de error capturados

| Excepción | Causa | Mensaje en errores |
|-----------|-------|-------------------|
| `WebClientResponseException` | Error HTTP (4xx/5xx) | `"Error HTTP en servicio X: 500"` |
| `WebClientRequestException` | Timeout / Conexión rechazada | `"Servicio X no disponible (timeout/conexion)"` |
| `Exception` | Cualquier otro error | `"SERVICIO NO DISPONIBLE"` |

### Timeouts configurados

| Parámetro | Valor |
|-----------|-------|
| Connect timeout | 5 segundos |
| Read/Response timeout | 10 segundos |
| Pool de conexiones | 50 máx, 10s acquire timeout |

## 5. Datos en Bases de Datos (verificación)

```
db_pacientes.pacientes:
  PAC-00001 | 0102030405 | Juan Carlos | Perez Gonzalez | M | 1990-05-15

db_consultas.consultas:
  1 | PAC-00001 | MEDICINA GENERAL | Cefalea | Dr. Rivera | 2026-07-01

db_laboratorio.resultados_laboratorio:
  1 | PAC-00001 | HEMOGRAMA | Hb:15.3 | HEMATO | 2026-07-01

db_imagenologia.estudios_imagen:
  1 | PAC-00001 | RX TORAX | RX | Normal | 2026-07-01
```

## 6. Health Checks

Todos los servicios responden correctamente:

| Servicio | Health Check | Estado |
|----------|-------------|--------|
| Pacientes | `GET /api/actuator/health` | ✅ UP (200) |
| Consultas | `GET /api/actuator/health` | ✅ UP (200) |
| Laboratorio | `GET /api/actuator/health` | ✅ UP (200) |
| Imagenología | `GET /api/actuator/health` | ✅ UP (200) |
| Repositorio | `GET /api/actuator/health` | ✅ UP (200) |
