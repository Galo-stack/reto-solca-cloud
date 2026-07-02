# Guía de Pruebas - Repositorio Clínico Regional SOLCA

## Requisitos
- Postman instalado (https://www.postman.com/downloads/)
- Proyecto corriendo: `.\run.ps1` (o `docker-compose up -d` si ya construiste las imágenes)

## Importar la Colección

1. Abre Postman
2. Click **File → Import** (o Ctrl+O)
3. Selecciona: `postman-collections/SOLCA-Cloud-API.postman_collection.json`
4. Click **Import**

---

## Paso a Paso: Flujo Completo de Prueba

### Paso 1: Verificar que los servicios están vivos

Abre la carpeta **6. HEALTH CHECKS** y haz click en **Send** en cada uno:
- Todos deben responder con `{"status": "UP"}`

Si alguno falla, espera 10-15 segundos (MySQL tarda en inicializar) e intenta de nuevo.

---

### Paso 2: Crear un Paciente

**Carpeta:** `1. PACIENTES → 1.1 Registrar Paciente`

**Método:** POST
**URL:** `http://localhost:8081/api/pacientes`

```json
{
  "cedula": "0102030405",
  "nombres": "Juan Carlos",
  "apellidos": "Pérez González",
  "fechaNacimiento": "1990-05-15",
  "genero": "M",
  "telefono": "0999123456",
  "email": "juan.perez@email.com",
  "direccion": "Av. 12 de Abril y Loja, Cuenca"
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

> **Nota:** El `idPacienteRegional` (ej: `PAC-00001`) se auto-asigna. Postman lo guarda automáticamente como variable `{{pacienteId}}`.

---

### Paso 3: Buscar el paciente por cédula

**Carpeta:** `1. PACIENTES → 1.2 Buscar por Cédula`

**Método:** GET
**URL:** `http://localhost:8081/api/pacientes/cedula/0102030405`

Debe devolver los datos del paciente registrado.

---

### Paso 4: Registrar una Consulta

**Carpeta:** `2. CONSULTAS → 2.1 Registrar Consulta`

**Método:** POST
**URL:** `http://localhost:8082/api/consultas`

```json
{
  "idPacienteRegional": "PAC-00001",
  "sede": "SOLCA-CUENCA",
  "fechaConsulta": "2026-06-25",
  "especialidad": "MEDICINA GENERAL",
  "diagnostico": "Paciente presenta cefalea tensional",
  "tratamiento": "Reposo, AINEs por 5 días",
  "medico": "Dr. Andrés Rivera",
  "notas": "Paciente refiere dolor de cabeza persistente"
}
```

**Respuesta esperada (201 Created):** `{"status": "success", "message": "Consulta registrada exitosamente", "data": {...}}`

---

### Paso 5: Buscar consultas del paciente

**Carpeta:** `2. CONSULTAS → 2.2 Buscar Consultas por Paciente`

**Método:** GET
**URL:** `http://localhost:8082/api/consultas/paciente/PAC-00001`

Debe devolver un array con la consulta registrada.

---

### Paso 6: Registrar Resultado de Laboratorio

**Carpeta:** `3. LABORATORIO → 3.1 Registrar Resultado`

**Método:** POST
**URL:** `http://localhost:8083/api/laboratorio`

```json
{
  "idPacienteRegional": "PAC-00001",
  "sede": "SOLCA-CUENCA",
  "fechaEjecucion": "2026-06-25",
  "tipoExamen": "HEMOGRAMA COMPLETO",
  "resultado": "Glóbulos Rojos: 5.2M/uL, Hemoglobina: 15.3 g/dL, Hematocrito: 45%",
  "valoresReferencia": "Hb: 13.5-17.5 g/dL, Hto: 39-49%",
  "medicoSolicitante": "Dra. María Torres",
  "area": "HEMATOLOGÍA",
  "metodo": "CITOMETRÍA DE FLUJO",
  "observaciones": "Resultados dentro de parámetros normales",
  "fechaResultado": "2026-06-25",
  "anormal": false
}
```

**Respuesta esperada (201 Created):** `{"status": "success", "message": "Resultado registrado exitosamente", ...}`

---

### Paso 7: Registrar Estudio de Imagen

**Carpeta:** `4. IMAGENOLOGÍA → 4.1 Registrar Estudio`

**Método:** POST
**URL:** `http://localhost:8084/api/imagenes`

```json
{
  "idPacienteRegional": "PAC-00001",
  "sede": "SOLCA-CUENCA",
  "fechaEstudio": "2026-06-25",
  "tipoEstudio": "RX TÓRAX",
  "formato": "DICOM",
  "urlArchivo": "https://pacs.solca.ec/estudios/2026/06/rx-torax-001.dcm",
  "nombreArchivo": "rx-torax-001.dcm",
  "medicoSolicitante": "Dr. Andrés Rivera",
  "modalidad": "RADIOGRAFÍA DIGITAL",
  "descripcion": "Radiografía de tórax AP y lateral",
  "hallazgos": "Campos pulmonares sin alteraciones. Senos costofrénicos libres.",
  "tamanoBytes": 5242880
}
```

**Respuesta esperada (201 Created):** `{"status": "success", "message": "Estudio registrado exitosamente", ...}`

---

### Paso 8: Consultar el Repositorio Consolidado ⭐

**Carpeta:** `5. REPOSITORIO CLÍNICO → 5.1 Consultar Expediente Consolidado`

**Método:** GET
**URL:** `http://localhost:8085/api/repositorio/paciente/PAC-00001`

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

Este endpoint **agrupa toda la información del paciente** en una sola respuesta. Si `status` es `"COMPLETE"`, todos los servicios respondieron correctamente.

---

## Escenarios de Error para Probar

### 404 - Paciente no encontrado
```
GET http://localhost:8081/api/pacientes/PAC-99999
```
Respuesta: `{"status": "error", "message": "Paciente no encontrado"}` (HTTP 404)

### 400 - Validación falla (campos obligatorios)
```
POST http://localhost:8081/api/pacientes
{ "nombres": "Incompleto" }
```
Respuesta: HTTP 400 con errores de validación

### 409 - Cédula duplicada
Registrar dos veces el mismo paciente con la misma cédula → HTTP 500 con mensaje "Ya existe un paciente con esa cédula"

### Repositorio con servicio caído
Si detienes un servicio (ej: `docker stop consulta-service`) y consultas el repositorio:
```json
{
  "status": "PARTIAL",
  "errores": { "consultas": "SERVICIO NO DISPONIBLE" },
  "consultas": []
}
```

---

## Comandos Útiles Durante Pruebas

```powershell
# Ver logs de un servicio específico
docker-compose logs -f paciente-service

# Ver logs de todos los servicios
docker-compose logs -f

# Ver estado de contenedores
docker-compose ps

# Ver la base de datos por phpMyAdmin
# Abrir http://localhost:8080 en el navegador
# Servidor: mysql-pacientes (o mysql-consultas, etc.)
# Usuario: root
# Contraseña: root123
```
