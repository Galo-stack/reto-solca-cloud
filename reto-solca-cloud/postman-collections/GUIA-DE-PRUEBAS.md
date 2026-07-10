# Guia de Pruebas - Repositorio Clinico Regional SOLCA

## Requisitos
- Postman instalado (https://www.postman.com/downloads/)
- Proyecto corriendo: `docker compose up -d` desde la raiz del proyecto
- Frontend (opcional): `python -m http.server 3000` en `frontend/`

---

## Paso 0: Obtener Token JWT (NUEVO - Requerido para todo)

Todos los endpoints protegidos requieren un token JWT en el header `Authorization`.

### 0.1 Login - Obtener Token

**Metodo:** POST
**URL:** `http://localhost:8086/api/auth/login`
**Body (JSON):**
```json
{
  "username": "medico1",
  "password": "admin123"
}
```

**Respuesta esperada (200 OK):**
```json
{
  "status": "success",
  "data": {
    "token": "eyJhbGciOiJIUzM4NCJ9...",
    "usuario": {
      "username": "medico1",
      "nombres": "Andres",
      "apellidos": "Rivera",
      "rol": "MEDICO"
    }
  }
}
```

> **Importante:** En Postman, en la pestana **Tests** del login, agrega este script para guardar automaticamente el token:
> ```javascript
> var jsonData = pm.response.json();
> if (jsonData.data && jsonData.data.token) {
>     pm.collectionVariables.set('token', jsonData.data.token);
> }
> ```

### 0.2 Usar Token en las demas peticiones

Agrega el header a todas las peticiones protegidas:
```
Authorization: Bearer {{token}}
```

---

## Usuarios de Prueba (Seed Data)

Todos los usuarios tienen contrasena: **admin123**

| Usuario   | Rol           | Permisos de Edicion      |
|-----------|---------------|--------------------------|
| admin     | ADMIN         | Solo lectura             |
| medico1   | MEDICO        | Editar pacientes         |
| medico2   | MEDICO        | Editar pacientes         |
| lab1      | LABORATORIO   | Editar resultados lab    |
| lab2      | LABORATORIO   | Editar resultados lab    |
| imagen1   | IMAGENOLOGIA  | Editar estudios imagen   |
| imagen2   | IMAGENOLOGIA  | Editar estudios imagen   |
| auditor1  | ADMIN         | Solo lectura             |

---

## Flujo Completo de Prueba

### Paso 1: Verificar servicios activos

**Health checks** (no requieren token):

| Servicio       | URL                                             |
|----------------|-------------------------------------------------|
| Seguridad      | `http://localhost:8086/api/actuator/health`     |
| Pacientes      | `http://localhost:8081/api/actuator/health`     |
| Consultas      | `http://localhost:8082/api/actuator/health`     |
| Laboratorio    | `http://localhost:8083/api/actuator/health`     |
| Imagenologia   | `http://localhost:8084/api/actuator/health`     |
| Repositorio    | `http://localhost:8085/api/actuator/health`     |
| Auditoria      | `http://localhost:8087/api/actuator/health`     |

Todos deben responder: `{"status": "UP"}`

---

### Paso 2: Obtener Token

```http
POST http://localhost:8086/api/auth/login
Content-Type: application/json

{
  "username": "medico1",
  "password": "admin123"
}
```

Guarda el `token` de la respuesta para usarlo en todas las llamadas siguientes como header:
```
Authorization: Bearer <token>
```

---

### Paso 3: Registrar un Paciente (POST)

```http
POST http://localhost:8081/api/pacientes
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "cedula": "0102030405",
  "nombres": "Juan Carlos",
  "apellidos": "Perez Gonzalez",
  "fechaNacimiento": "1990-05-15",
  "genero": "M",
  "telefono": "0999123456",
  "email": "juan.perez@email.com",
  "direccion": "Av. 12 de Abril y Loja, Cuenca",
  "grupoSanguineo": "O+",
  "estadoCivil": "SOLTERO",
  "ocupacion": "Ingeniero",
  "alergias": "Ninguna",
  "enfermedadActual": "Control rutinario",
  "antecedentesPersonales": {
    "cronicas": "Ninguna",
    "cirugias": "Ninguna",
    "otros": "Ninguno"
  },
  "antecedentesFamiliares": {
    "padre": "Diabetes",
    "madre": "Hipertension",
    "hermanos": "Ninguno",
    "hijos": "Ninguno"
  },
  "contactoEmergencia": {
    "nombre": "Maria Gonzalez",
    "telefono": "0999000000",
    "parentesco": "Madre"
  }
}
```

**Respuesta esperada (201 Created):**
```json
{
  "status": "success",
  "message": "Paciente registrado exitosamente",
  "data": {
    "idPacienteRegional": "PAC-00001",
    ...
  }
}
```

> El `idPacienteRegional` se auto-asigna (PAC-00001, PAC-00002, etc.)

---

### Paso 4: Listar todos los pacientes (GET)

```http
GET http://localhost:8081/api/pacientes
Authorization: Bearer {{token}}
```

---

### Paso 5: Buscar paciente por cedula (GET)

```http
GET http://localhost:8081/api/pacientes/cedula/0102030405
Authorization: Bearer {{token}}
```

---

### Paso 6: Editar un paciente (PUT - NUEVO)

> Solo disponible para rol **MEDICO**

```http
PUT http://localhost:8081/api/pacientes/PAC-00001
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "cedula": "0102030405",
  "nombres": "Juan Carlos Editado",
  "apellidos": "Perez Gonzalez Actualizado",
  "fechaNacimiento": "1990-05-15",
  "genero": "M",
  "telefono": "0999123456",
  "email": "juan.editado@email.com",
  "direccion": "Nueva direccion actualizada",
  "grupoSanguineo": "A+",
  "estadoCivil": "CASADO",
  "ocupacion": "Medico",
  "alergias": "Penicilina",
  "enfermedadActual": "Ninguna",
  "antecedentesPersonales": {
    "cronicas": "Asma",
    "cirugias": "Apendicectomia 2010",
    "otros": "Ninguno"
  },
  "antecedentesFamiliares": {
    "padre": "Diabetes tipo 2",
    "madre": "Hipertension arterial",
    "hermanos": "Ninguno",
    "hijos": "Ninguno"
  },
  "contactoEmergencia": {
    "nombre": "Maria Gonzalez",
    "telefono": "0999000000",
    "parentesco": "Madre"
  }
}
```

**Respuesta esperada (200 OK):**
```json
{
  "status": "success",
  "message": "Paciente actualizado exitosamente",
  "data": { ... }
}
```

---

### Paso 7: Registrar una Consulta (POST)

```http
POST http://localhost:8082/api/consultas
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "idPacienteRegional": "PAC-00001",
  "sede": "SOLCA-CUENCA",
  "fechaConsulta": "2026-06-25",
  "especialidad": "MEDICINA GENERAL",
  "diagnostico": "Paciente presenta cefalea tensional",
  "tratamiento": "Reposo, AINEs por 5 dias",
  "medico": "Dr. Andres Rivera",
  "notas": "Paciente refiere dolor de cabeza persistente"
}
```

**Respuesta esperada (201 Created):**
```json
{
  "status": "success",
  "message": "Consulta registrada exitosamente",
  "data": { ... }
}
```

---

### Paso 8: Buscar consultas por paciente (GET)

```http
GET http://localhost:8082/api/consultas/paciente/PAC-00001
Authorization: Bearer {{token}}
```

---

### Paso 9: Registrar Resultado de Laboratorio (POST)

```http
POST http://localhost:8083/api/laboratorio
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "idPacienteRegional": "PAC-00001",
  "sede": "SOLCA-CUENCA",
  "fechaEjecucion": "2026-06-25",
  "tipoExamen": "HEMOGRAMA COMPLETO",
  "resultado": "Pendiente",
  "valoresReferencia": "Hb: 13.5-17.5 g/dL, Hto: 39-49%",
  "medicoSolicitante": "Dra. Maria Torres",
  "area": "HEMATOLOGIA",
  "metodo": "MANUAL",
  "anormal": false
}
```

**Respuesta esperada (201 Created)**

> Nota: Si el `resultado` es `"Pendiente"`, aparecera en la pestana "Pendientes" del frontend.

---

### Paso 10: Editar resultado de laboratorio (PUT - NUEVO)

> Solo disponible para rol **LABORATORIO**

```http
PUT http://localhost:8083/api/laboratorio/1
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "idPacienteRegional": "PAC-00001",
  "sede": "SOLCA-CUENCA",
  "fechaEjecucion": "2026-06-25",
  "tipoExamen": "HEMOGRAMA COMPLETO",
  "resultado": "Globulos Rojos: 5.2M/uL, Hemoglobina: 15.3 g/dL, Hematocrito: 45%",
  "valoresReferencia": "Hb: 13.5-17.5 g/dL, Hto: 39-49%",
  "medicoSolicitante": "Dra. Maria Torres",
  "area": "HEMATOLOGIA",
  "metodo": "CITOMETRIA DE FLUJO",
  "observaciones": "Resultados dentro de parametros normales",
  "fechaResultado": "2026-06-25",
  "anormal": false
}
```

**Respuesta esperada (200 OK):**
```json
{
  "status": "success",
  "message": "Resultado de laboratorio actualizado exitosamente",
  "data": { ... }
}
```

---

### Paso 11: Registrar Estudio de Imagen (POST)

```http
POST http://localhost:8084/api/imagenes
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "idPacienteRegional": "PAC-00001",
  "sede": "SOLCA-CUENCA",
  "fechaEstudio": "2026-06-25",
  "tipoEstudio": "RX TORAX",
  "formato": "DICOM",
  "urlArchivo": "https://pacs.solca.ec/estudios/2026/06/rx-torax-001.dcm",
  "nombreArchivo": "rx-torax-001.dcm",
  "medicoSolicitante": "Dr. Andres Rivera",
  "modalidad": "RX",
  "descripcion": "Radiografia de torax AP y lateral",
  "hallazgos": "Pendiente",
  "tamanoBytes": 5242880
}
```

**Respuesta esperada (201 Created)**

---

### Paso 12: Editar estudio de imagen (PUT - NUEVO)

> Solo disponible para rol **IMAGENOLOGIA**

```http
PUT http://localhost:8084/api/imagenes/1
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "idPacienteRegional": "PAC-00001",
  "sede": "SOLCA-CUENCA",
  "fechaEstudio": "2026-06-25",
  "tipoEstudio": "RX TORAX",
  "formato": "DICOM",
  "urlArchivo": "https://pacs.solca.ec/estudios/2026/06/rx-torax-001.dcm",
  "nombreArchivo": "rx-torax-001.dcm",
  "medicoSolicitante": "Dr. Andres Rivera",
  "modalidad": "RX",
  "descripcion": "Radiografia de torax AP y lateral",
  "hallazgos": "Campos pulmonares sin alteraciones. Senos costofrenicos libres.",
  "tamanoBytes": 5242880
}
```

**Respuesta esperada (200 OK):**
```json
{
  "status": "success",
  "message": "Estudio de imagenologia actualizado exitosamente",
  "data": { ... }
}
```

---

### Paso 13: Consultar Repositorio Consolidado (GET)

```http
GET http://localhost:8085/api/repositorio/paciente/PAC-00001
Authorization: Bearer {{token}}
```

**Respuesta esperada (200 OK):**
```json
{
  "paciente": { ... },
  "consultas": [ ... ],
  "laboratorio": [ ... ],
  "imagenes": [ ... ],
  "status": "COMPLETE",
  "errores": {}
}
```

Este endpoint **agrupa toda la informacion del paciente** en una sola respuesta. Si `status` es `"COMPLETE"`, todos los servicios respondieron correctamente.

---

### Paso 14: Auditoria (NUEVO)

#### 14.1 Registrar evento de auditoria (POST)

```http
POST http://localhost:8087/api/auditorias
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "usuario": "medico1",
  "rol": "MEDICO",
  "modulo": "PACIENTES",
  "accion": "Actualizacion de paciente",
  "detalle": "ID Regional: PAC-00001 - Datos actualizados",
  "resultado": "EXITO"
}
```

#### 14.2 Listar auditorias (GET)

```http
GET http://localhost:8087/api/auditorias?page=0&size=50
Authorization: Bearer {{token}}
```

#### 14.3 Filtrar por modulo (GET)

```http
GET http://localhost:8087/api/auditorias/modulo/PACIENTES?page=0&size=50
Authorization: Bearer {{token}}
```

#### 14.4 Contar registros (GET)

```http
GET http://localhost:8087/api/auditorias/contar
Authorization: Bearer {{token}}
```

---

## Resumen de Nuevos Endpoints (Agregados Recientemente)

| Metodo | URL                                    | Descripcion                          | Rol Requerido  |
|--------|----------------------------------------|--------------------------------------|----------------|
| POST   | `/api/auth/login`                      | Autenticacion y obtencion de token   | Publico        |
| POST   | `/api/auditorias`                      | Registrar evento de auditoria        | Cualquiera     |
| GET    | `/api/auditorias`                      | Listar auditorias (paginado)         | ADMIN          |
| GET    | `/api/auditorias/modulo/{modulo}`      | Filtrar por modulo                   | ADMIN          |
| GET    | `/api/auditorias/usuario/{usuario}`    | Filtrar por usuario                  | ADMIN          |
| GET    | `/api/auditorias/fecha?fecha=YYYY-MM-DD` | Filtrar por fecha                  | ADMIN          |
| GET    | `/api/auditorias/contar`               | Contar total de registros            | ADMIN          |
| PUT    | `/api/pacientes/{id}`                  | Editar datos del paciente            | MEDICO         |
| PUT    | `/api/laboratorio/{id}`                | Editar resultado de laboratorio      | LABORATORIO    |
| PUT    | `/api/imagenes/{id}`                   | Editar estudio de imagenologia       | IMAGENOLOGIA   |

---

## Resumen de Puertos y Servicios

| Servicio       | Puerto | Base URL                         |
|----------------|--------|----------------------------------|
| Pacientes      | 8081   | `http://localhost:8081/api`      |
| Consultas      | 8082   | `http://localhost:8082/api`      |
| Laboratorio    | 8083   | `http://localhost:8083/api`      |
| Imagenologia   | 8084   | `http://localhost:8084/api`      |
| Repositorio    | 8085   | `http://localhost:8085/api`      |
| Seguridad      | 8086   | `http://localhost:8086/api`      |
| Auditoria      | 8087   | `http://localhost:8087/api`      |
| phpMyAdmin     | 8080   | `http://localhost:8080`          |

**phpMyAdmin:** Servidor: `mysql-pacientes` (o `mysql-consultas`, etc.), Usuario: `root`, Contrasena: `root123`

---

## Escenarios de Error para Probar

### 401 - Token invalido o expirado
```
GET http://localhost:8081/api/pacientes
Authorization: Bearer token-invalido
```
Respuesta: HTTP 401 Unauthorized

### 401 - Sin token
```
GET http://localhost:8081/api/pacientes
```
Respuesta: `{"status":"ERROR","message":"Token de autenticacion requerido"}` (HTTP 401)

### 404 - Paciente no encontrado
```
GET http://localhost:8081/api/pacientes/PAC-99999
Authorization: Bearer {{token}}
```
Respuesta: `{"status": "error", "message": "Paciente no encontrado"}` (HTTP 404)

### 400 - Validacion falla (campos obligatorios)
```
POST http://localhost:8081/api/pacientes
Authorization: Bearer {{token}}
Content-Type: application/json

{ "nombres": "Incompleto" }
```
Respuesta: HTTP 400 con errores de validacion

### 409 - Cedula duplicada
Registrar dos veces el mismo paciente con la misma cedula -> HTTP 500 con mensaje "Ya existe un paciente con esa cedula"

### Repositorio con servicio caido
Si detienes un servicio (`docker stop consulta-service`) y consultas el repositorio:
```json
{
  "status": "PARTIAL",
  "errores": { "consultas": "SERVICIO NO DISPONIBLE" },
  "consultas": []
}
```

---

## Comandos Utiles Durante Pruebas

```powershell
# Iniciar todos los servicios
docker compose up -d

# Detener todos los servicios
docker compose down

# Ver logs de un servicio especifico
docker compose logs -f paciente-service

# Ver logs de todos los servicios
docker compose logs -f

# Ver estado de contenedores
docker compose ps

# Iniciar frontend (desde la carpeta frontend/)
python -m http.server 3000

# Ver logs de un servicio directamente
docker logs -f reto-solca-cloud-paciente-service

# Acceder a la base de datos MySQL
docker exec -it mysql-pacientes mysql -uroot -proot123 db_pacientes
```

---

## Nota sobre CORS

Los microservicios aceptan peticiones **OPTIONS** (preflight CORS) sin requerir autenticacion, necesarias para el correcto funcionamiento del frontend desde el navegador.
