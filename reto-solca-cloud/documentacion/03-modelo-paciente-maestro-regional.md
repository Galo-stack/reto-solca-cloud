# Modelo del Paciente Maestro Regional

## Descripción

El Paciente Maestro Regional es la entidad central del sistema. Define un identificador único (`idPacienteRegional`) que permite localizar a un paciente en cualquier sede de SOLCA a nivel nacional.

## Entidad: Paciente

```json
{
  "idPacienteRegional": "PAC-00001",
  "cedula": "0102030405",
  "nombres": "Juan Carlos",
  "apellidos": "Pérez González",
  "fechaNacimiento": "1990-05-15",
  "genero": "M",
  "telefono": "0999123456",
  "email": "juan.perez@email.com",
  "direccion": "Av. 12 de Abril y Loja, Cuenca",
  "historiasLocales": [
    {
      "sede": "SOLCA-CUENCA",
      "idHistoriaLocal": "HC-12345"
    }
  ]
}
```

## Atributos

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `idPacienteRegional` | `VARCHAR(20)` PK | Identificador único regional. Formato `PAC-XXXXX` |
| `cedula` | `VARCHAR(15)` UNIQUE | Cédula de identidad ecuatoriana |
| `nombres` | `VARCHAR(100)` | Nombres completos del paciente |
| `apellidos` | `VARCHAR(100)` | Apellidos completos del paciente |
| `fechaNacimiento` | `DATE` | Fecha de nacimiento |
| `genero` | `VARCHAR(1)` | 'M' o 'F' |
| `telefono` | `VARCHAR(20)` | Número de contacto |
| `email` | `VARCHAR(100)` | Correo electrónico |
| `direccion` | `TEXT` | Dirección de residencia |
| `fechaRegistro` | `TIMESTAMP` | Fecha de creación del registro |
| `fechaActualizacion` | `TIMESTAMP` | Fecha de última actualización |
| `activo` | `BOOLEAN` | Estado del registro |

## Historia Local (HistoriaLocal)

Cada paciente puede tener múltiples historias locales, una por cada sede de SOLCA donde ha sido atendido:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `BIGINT` PK auto | Identificador de la relación |
| `idPacienteRegional` | `VARCHAR(20)` FK | Referencia al paciente regional |
| `sede` | `VARCHAR(20)` | Sede donde se atiende (SOLCA-CUENCA, SOLCA-MANABI, SOLCA-QUITO) |
| `idHistoriaLocal` | `VARCHAR(20)` | Identificador local en el sistema de la sede |
| `fechaAsociacion` | `TIMESTAMP` | Fecha de asociación |

## Política de Identificación Regional

1. **Generación automática**: El ID `PAC-XXXXX` se genera secuencialmente al registrar un paciente
2. **Unicidad**: Garantizada por la combinación de ID regional único + cédula única
3. **Desduplicación**: La cédula valida que un paciente no sea registrado dos veces
4. **Trazabilidad**: Cada historia local en cada sede queda vinculada al paciente regional

## Diagrama de Clases

```
┌─────────────────────────────┐       ┌──────────────────────────────┐
│         Paciente            │       │       HistoriaLocal          │
├─────────────────────────────┤       ├──────────────────────────────┤
│ -idPacienteRegional: String │1     N│ -id: Long                   │
│ -cedula: String             │◄──────│ -sede: String               │
│ -nombres: String            │       │ -idHistoriaLocal: String    │
│ -apellidos: String          │       │ -fechaAsociacion: LocalDate │
│ -fechaNacimiento: LocalDate │       │ -paciente: Paciente         │
│ -genero: String             │       └──────────────────────────────┘
│ -telefono: String           │
│ -email: String              │
│ -direccion: String          │
│ -activo: Boolean            │
│ -historiasLocales: List     │
└─────────────────────────────┘
```
