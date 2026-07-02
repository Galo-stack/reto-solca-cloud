# Avance 2 — Comunicación mediante APIs REST

## Modelo de Comunicación

La comunicación entre el **Repositorio Clínico Regional** y los microservicios de dominio se realiza mediante **APIs REST síncronas** sobre HTTP/1.1.

```
┌──────────────┐         GET /api/pacientes/{id}          ┌──────────────┐
│              │  ──────────────────────────────────────►  │              │
│ REPOSITORIO  │         JSON Response                    │  PACIENTES   │
│   (Cliente   │  ◄──────────────────────────────────────  │  (Servidor)  │
│   WebClient) │                                           │              │
│              │         GET /api/consultas/paciente/{id}  │  CONSULTAS   │
│              │  ──────────────────────────────────────►  │              │
│              │         JSON Response                    │              │
│              │  ◄──────────────────────────────────────  │              │
│              │                                           │  LABORATORIO │
│              │         GET /api/laboratorio/paciente/{id}│              │
│              │  ──────────────────────────────────────►  │              │
│              │         JSON Response                    │              │
│              │  ◄──────────────────────────────────────  │              │
│              │                                           │ IMAGENOLOGIA │
│              │         GET /api/imagenes/paciente/{id}   │              │
│              │  ──────────────────────────────────────►  │              │
│              │         JSON Response                    │              │
│              │  ◄──────────────────────────────────────  │              │
└──────────────┘                                           └──────────────┘
```

## Tecnología: Spring WebClient (Reactivo)

El Repositorio utiliza **WebClient** de Spring WebFlux para las llamadas REST. A diferencia de `RestTemplate` (bloqueante), WebClient soporta:

- **Timeouts configurables** (conexión, lectura, escritura)
- **Manejo de errores** con `onStatus()`
- **Pool de conexiones** con `ConnectionProvider`
- **Códecs configurables** para límite de memoria

### Configuración de WebClient

```java
@Bean
public HttpClient httpClient() {
    return HttpClient.create(
            ConnectionProvider.builder("solca-pool")
                    .maxConnections(50)
                    .pendingAcquireTimeout(Duration.ofSeconds(10))
                    .build())
            .responseTimeout(Duration.ofSeconds(10));
}

@Bean
public WebClient webClient(HttpClient httpClient) {
    return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
            .build();
}
```

### Uso en RepositorioService

```java
public RepositorioResponse obtenerInformacionConsolidada(String idPacienteRegional) {
    // 1. Llama a pacientes
    Map<String, Object> paciente = obtenerDatosPaciente(idPacienteRegional);
    response.setPaciente(paciente);

    // 2. Llama a consultas
    List<Map<String, Object>> consultas = obtenerConsultas(idPacienteRegional);
    response.setConsultas(consultas);

    // 3. Llama a laboratorio
    List<Map<String, Object>> laboratorio = obtenerLaboratorio(idPacienteRegional);
    response.setLaboratorio(laboratorio);

    // 4. Llama a imagenologia
    List<Map<String, Object>> imagenes = obtenerImagenes(idPacienteRegional);
    response.setImagenes(imagenes);

    return response; // JSON consolidado
}
```

## Formato de Respuesta Uniforme

Cada microservicio responde con una estructura JSON envolvente:

```json
{
  "data": { ... },        // Objeto único (paciente) o arreglo (consultas, etc.)
  "status": "success"     // "success" | "error" | "not_found"
}
```

### Endpoints Expuestos

| Microservicio | Endpoint | Método | Parámetro | Respuesta |
|--------------|----------|--------|-----------|-----------|
| **Pacientes** | `/api/pacientes/{id}` | GET | idPacienteRegional | `{ data: { paciente }, status }` |
| **Consultas** | `/api/consultas/paciente/{id}` | GET | idPacienteRegional | `{ data: [consulta,...], status }` |
| **Laboratorio** | `/api/laboratorio/paciente/{id}` | GET | idPacienteRegional | `{ data: [examen,...], status }` |
| **Imagenología** | `/api/imagenes/paciente/{id}` | GET | idPacienteRegional | `{ data: [imagen,...], status }` |
| **Repositorio** | `/api/repositorio/paciente/{id}` | GET | idPacienteRegional | `{ paciente, consultas, laboratorio, imagenes, status, errores }` |

## Manejo de Errores

### Errores HTTP (4xx/5xx)

Cuando un microservicio devuelve un error HTTP, WebClient lo captura como `WebClientResponseException`:

```java
} catch (WebClientResponseException e) {
    log.error("Error HTTP en {} - {}: {}", uri, e.getStatusCode(), e.getMessage());
    throw new RuntimeException("Error HTTP en " + uri + ": " + e.getStatusCode(), e);
}
```

### Errores de Conexión (timeout, red)

Si un microservicio no responde por timeout o problemas de red, se captura `WebClientRequestException`:

```java
} catch (WebClientRequestException e) {
    log.error("Error de conexion en {}: {}", uri, e.getMessage());
    throw new RuntimeException("Servicio no disponible en " + uri + " (timeout/conexion)", e);
}
```

### Respuesta Parcial

El repositorio **no falla completamente** si un microservicio está caído. En su lugar:

```json
{
  "paciente":    { "idPacienteRegional": "PAC-00001", ... },
  "consultas":   [],
  "laboratorio": [],
  "imagenes":    [],
  "status":      "PARTIAL",
  "errores": {
    "consultas":   "SERVICIO NO DISPONIBLE",
    "laboratorio": "SERVICIO NO DISPONIBLE",
    "imagenes":    "SERVICIO NO DISPONIBLE"
  }
}
```

## Sin Acceso Directo a Bases de Datos

El repositorio **no tiene** dependencias JDBC, JPA, ni Flyway. Su `pom.xml` solo incluye:

- `spring-boot-starter-web` (REST API)
- `spring-boot-starter-webflux` (WebClient reactivo)
- `spring-boot-starter-actuator` (health checks)
- `lombok` (código boilerplate)

No existe configuración de `spring.datasource` ni driver MySQL en el repositorio, garantizando que **solo se comunica via REST**.

## Timeouts y Resiliencia

| Parámetro | Valor | Descripción |
|-----------|-------|-------------|
| connectTimeout | 5s | Tiempo máximo para establecer conexión TCP |
| readTimeout | 10s | Tiempo máximo para recibir respuesta |
| pendingAcquireTimeout | 10s | Tiempo máximo para obtener conexión del pool |
| maxConnections | 50 | Conexiones simultáneas máximas en el pool |
