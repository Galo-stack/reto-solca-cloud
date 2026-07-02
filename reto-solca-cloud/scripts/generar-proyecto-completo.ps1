# scripts\generar-proyecto-completo.ps1 - VERSIÓN CORREGIDA
Write-Host "`n==========================================" -ForegroundColor Cyan
Write-Host "🚀 GENERANDO PROYECTO COMPLETO SOLCA" -ForegroundColor Cyan
Write-Host "==========================================`n" -ForegroundColor Cyan

# ============================================
# FUNCIÓN PARA CREAR MICROSERVICIO CON BASE DE DATOS
# ============================================
function Crear-Microservicio {
    param(
        [string]$nombre,
        [string]$puerto,
        [string]$paquete,
        [string]$dbName,
        [int]$puertoMySQL
    )
    
    Write-Host "📦 Creando microservicio: $nombre (puerto $puerto)" -ForegroundColor Yellow
    
    $ruta = "microservicio-$nombre"
    New-Item -ItemType Directory -Force -Path $ruta | Out-Null
    
    # Crear pom.xml personalizado para Java 21
    $pomContent = @'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.solca</groupId>
    <artifactId>NOMBRE-service</artifactId>
    <version>1.0.0</version>
    <name>NOMBRE-service</name>
    <description>Microservicio NOMBRE</description>
    
    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
        </dependency>
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-mysql</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
'@
    
    # Reemplazar NOMBRE por el nombre del servicio
    $pomContent = $pomContent -replace 'NOMBRE', $nombre
    $pomContent | Out-File -FilePath "$ruta\pom.xml" -Encoding UTF8
    
    # Crear estructura de carpetas
    $carpetas = @(
        "src\main\java\com\solca\$paquete\config",
        "src\main\java\com\solca\$paquete\controller",
        "src\main\java\com\solca\$paquete\service",
        "src\main\java\com\solca\$paquete\repository",
        "src\main\java\com\solca\$paquete\model",
        "src\main\java\com\solca\$paquete\dto",
        "src\main\java\com\solca\$paquete\exception",
        "src\main\resources\db\migration"
    )
    
    foreach ($carpeta in $carpetas) {
        New-Item -ItemType Directory -Force -Path "$ruta\$carpeta" | Out-Null
    }
    
    # Crear application.yml
    $ymlContent = @"
server:
  port: $puerto
  servlet:
    context-path: /api

spring:
  application:
    name: ${nombre}-service
  datasource:
    url: jdbc:mysql://localhost:${puertoMySQL}/${dbName}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: root
    password: root123
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
    show-sql: false
  flyway:
    enabled: true
    baseline-on-migrate: true
    locations: classpath:db/migration

logging:
  level:
    com.solca.${paquete}: DEBUG
    org.springframework.web: INFO

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
"@
    $ymlContent | Out-File -FilePath "$ruta\src\main\resources\application.yml" -Encoding UTF8
    
    # Crear Main Application
    $mainClassContent = @"
package com.solca.${paquete};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ${nombre}ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(${nombre}ServiceApplication.class, args);
        System.out.println("✅ Microservicio ${nombre} iniciado en puerto $puerto");
    }
}
"@
    $mainClassContent | Out-File -FilePath "$ruta\src\main\java\com\solca\$paquete\${nombre}ServiceApplication.java" -Encoding UTF8
    
    # Crear Dockerfile
    $dockerContent = @"
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE $puerto
ENTRYPOINT ["java", "-jar", "app.jar"]
"@
    $dockerContent | Out-File -FilePath "$ruta\Dockerfile" -Encoding UTF8
    
    Write-Host "✅ Microservicio $nombre creado" -ForegroundColor Green
}

# ============================================
# FUNCIÓN PARA CREAR MICROSERVICIO REPOSITORIO (SIN BD)
# ============================================
function Crear-Repositorio {
    Write-Host "`n📦 Creando microservicio: repositorio (puerto 8085)" -ForegroundColor Yellow
    
    $ruta = "microservicio-repositorio"
    New-Item -ItemType Directory -Force -Path $ruta | Out-Null
    
    $pomContent = @'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.solca</groupId>
    <artifactId>repositorio-service</artifactId>
    <version>1.0.0</version>
    <name>repositorio-service</name>
    <description>Repositorio Clínico Regional</description>
    
    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
'@
    $pomContent | Out-File -FilePath "$ruta\pom.xml" -Encoding UTF8
    
    # Crear estructura
    $carpetas = @(
        "src\main\java\com\solca\repositorio\config",
        "src\main\java\com\solca\repositorio\controller",
        "src\main\java\com\solca\repositorio\service",
        "src\main\java\com\solca\repositorio\model"
    )
    
    foreach ($carpeta in $carpetas) {
        New-Item -ItemType Directory -Force -Path "$ruta\$carpeta" | Out-Null
    }
    
    # application.yml
    $ymlContent = @"
server:
  port: 8085
  servlet:
    context-path: /api

spring:
  application:
    name: repositorio-service

services:
  pacientes:
    url: http://localhost:8081
  consultas:
    url: http://localhost:8082
  laboratorio:
    url: http://localhost:8083
  imagenologia:
    url: http://localhost:8084

logging:
  level:
    com.solca.repositorio: DEBUG
"@
    $ymlContent | Out-File -FilePath "$ruta\src\main\resources\application.yml" -Encoding UTF8
    
    # Main Application
    $mainContent = @"
package com.solca.repositorio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RepositorioServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RepositorioServiceApplication.class, args);
        System.out.println("✅ Microservicio Repositorio iniciado en puerto 8085");
    }
}
"@
    $mainContent | Out-File -FilePath "$ruta\src\main\java\com\solca\repositorio\RepositorioServiceApplication.java" -Encoding UTF8
    
    # Dockerfile
    $dockerContent = @"
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
"@
    $dockerContent | Out-File -FilePath "$ruta\Dockerfile" -Encoding UTF8
    
    Write-Host "✅ Microservicio repositorio creado" -ForegroundColor Green
}

# ============================================
# CREAR TODOS LOS MICROSERVICIOS
# ============================================
Write-Host "`n📦 CREANDO MICROSERVICIOS CON BASE DE DATOS..." -ForegroundColor Cyan
Crear-Microservicio -nombre "pacientes" -puerto "8081" -paquete "pacientes" -dbName "db_pacientes" -puertoMySQL "3306"
Crear-Microservicio -nombre "consultas" -puerto "8082" -paquete "consultas" -dbName "db_consultas" -puertoMySQL "3307"
Crear-Microservicio -nombre "laboratorio" -puerto "8083" -paquete "laboratorio" -dbName "db_laboratorio" -puertoMySQL "3308"
Crear-Microservicio -nombre "imagenologia" -puerto "8084" -paquete "imagenologia" -dbName "db_imagenologia" -puertoMySQL "3309"

Write-Host "`n📦 CREANDO MICROSERVICIO REPOSITORIO..." -ForegroundColor Cyan
Crear-Repositorio

Write-Host "`n✅ TODOS LOS MICROSERVICIOS CREADOS EXITOSAMENTE!" -ForegroundColor Green

# ============================================
# CREAR LOS ARCHIVOS DE CÓDIGO PARA CADA MICROSERVICIO
# ============================================
Write-Host "`n📝 GENERANDO CÓDIGO PARA CADA MICROSERVICIO..." -ForegroundColor Cyan

# ============================================
# PACIENTES - Modelos
# ============================================
Write-Host "  📝 Generando código para pacientes..." -ForegroundColor Yellow

# Paciente.java
$pacienteContent = @'
package com.solca.pacientes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pacientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    @Id
    @Column(name = "id_paciente_regional", length = 20, nullable = false)
    private String idPacienteRegional;

    @Column(name = "cedula", length = 15, nullable = false, unique = true)
    private String cedula;

    @Column(name = "nombres", length = 100, nullable = false)
    private String nombres;

    @Column(name = "apellidos", length = 100, nullable = false)
    private String apellidos;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "genero", length = 1)
    private String genero;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "fecha_registro")
    @CreationTimestamp
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion")
    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, 
               fetch = FetchType.EAGER, orphanRemoval = true)
    @Builder.Default
    private List<HistoriaLocal> historiasLocales = new ArrayList<>();
}
'@
$pacienteContent | Out-File -FilePath "microservicio-pacientes\src\main\java\com\solca\pacientes\model\Paciente.java" -Encoding UTF8

# HistoriaLocal.java
$historiaContent = @'
package com.solca.pacientes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historias_locales")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaLocal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sede", length = 20, nullable = false)
    private String sede;

    @Column(name = "id_historia_local", length = 20, nullable = false)
    private String idHistoriaLocal;

    @Column(name = "fecha_asociacion")
    private LocalDateTime fechaAsociacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente_regional", nullable = false)
    private Paciente paciente;

    @PrePersist
    protected void onCreate() {
        fechaAsociacion = LocalDateTime.now();
    }
}
'@
$historiaContent | Out-File -FilePath "microservicio-pacientes\src\main\java\com\solca\pacientes\model\HistoriaLocal.java" -Encoding UTF8

# PacienteDTO.java
$dtoContent = @'
package com.solca.pacientes.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {
    private String idPacienteRegional;

    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 10, max = 15, message = "La cédula debe tener entre 10 y 15 caracteres")
    private String cedula;

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    private String genero;
    private String telefono;
    private String email;
    private String direccion;
    private List<HistoriaLocalDTO> historiasLocales;
}
'@
$dtoContent | Out-File -FilePath "microservicio-pacientes\src\main\java\com\solca\pacientes\dto\PacienteDTO.java" -Encoding UTF8

# HistoriaLocalDTO.java
$historiaDtoContent = @'
package com.solca.pacientes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaLocalDTO {
    @NotBlank(message = "La sede es obligatoria")
    private String sede;

    @NotBlank(message = "El ID de historia local es obligatorio")
    private String idHistoriaLocal;
}
'@
$historiaDtoContent | Out-File -FilePath "microservicio-pacientes\src\main\java\com\solca\pacientes\dto\HistoriaLocalDTO.java" -Encoding UTF8

# PacienteRepository.java
$repoContent = @'
package com.solca.pacientes.repository;

import com.solca.pacientes.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String> {
    Optional<Paciente> findByCedula(String cedula);
    boolean existsByCedula(String cedula);
}
'@
$repoContent | Out-File -FilePath "microservicio-pacientes\src\main\java\com\solca\pacientes\repository\PacienteRepository.java" -Encoding UTF8

# PacienteService.java
$serviceContent = @'
package com.solca.pacientes.service;

import com.solca.pacientes.dto.PacienteDTO;
import com.solca.pacientes.model.Paciente;
import com.solca.pacientes.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PacienteService {
    private final PacienteRepository pacienteRepository;

    public PacienteDTO registrarPaciente(PacienteDTO dto) {
        log.info("Registrando paciente: {}", dto.getCedula());
        
        if (pacienteRepository.existsByCedula(dto.getCedula())) {
            throw new RuntimeException("Ya existe un paciente con esa cédula");
        }

        long count = pacienteRepository.count() + 1;
        String idRegional = String.format("PAC-%05d", count);

        Paciente paciente = Paciente.builder()
                .idPacienteRegional(idRegional)
                .cedula(dto.getCedula())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(dto.getFechaNacimiento())
                .genero(dto.getGenero())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .direccion(dto.getDireccion())
                .activo(true)
                .build();

        Paciente saved = pacienteRepository.save(paciente);
        dto.setIdPacienteRegional(saved.getIdPacienteRegional());
        return dto;
    }

    public PacienteDTO buscarPorCedula(String cedula) {
        return pacienteRepository.findByCedula(cedula)
                .map(this::convertirADTO)
                .orElse(null);
    }

    public PacienteDTO buscarPorId(String id) {
        return pacienteRepository.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    private PacienteDTO convertirADTO(Paciente p) {
        return PacienteDTO.builder()
                .idPacienteRegional(p.getIdPacienteRegional())
                .cedula(p.getCedula())
                .nombres(p.getNombres())
                .apellidos(p.getApellidos())
                .fechaNacimiento(p.getFechaNacimiento())
                .genero(p.getGenero())
                .telefono(p.getTelefono())
                .email(p.getEmail())
                .direccion(p.getDireccion())
                .build();
    }
}
'@
$serviceContent | Out-File -FilePath "microservicio-pacientes\src\main\java\com\solca\pacientes\service\PacienteService.java" -Encoding UTF8

# PacienteController.java
$controllerContent = @'
package com.solca.pacientes.controller;

import com.solca.pacientes.dto.PacienteDTO;
import com.solca.pacientes.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@Slf4j
public class PacienteController {
    private final PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarPaciente(@Valid @RequestBody PacienteDTO dto) {
        log.info("POST /api/pacientes - Registrando paciente");
        PacienteDTO result = pacienteService.registrarPaciente(dto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Paciente registrado exitosamente");
        response.put("data", result);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Map<String, Object>> buscarPorCedula(@PathVariable String cedula) {
        log.info("GET /api/pacientes/cedula/{}", cedula);
        PacienteDTO paciente = pacienteService.buscarPorCedula(cedula);
        
        Map<String, Object> response = new HashMap<>();
        if (paciente != null) {
            response.put("status", "success");
            response.put("data", paciente);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Paciente no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable String id) {
        log.info("GET /api/pacientes/{}", id);
        PacienteDTO paciente = pacienteService.buscarPorId(id);
        
        Map<String, Object> response = new HashMap<>();
        if (paciente != null) {
            response.put("status", "success");
            response.put("data", paciente);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Paciente no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
'@
$controllerContent | Out-File -FilePath "microservicio-pacientes\src\main\java\com\solca\pacientes\controller\PacienteController.java" -Encoding UTF8

# SQL Migration
$sqlContent = @'
CREATE TABLE IF NOT EXISTS pacientes (
    id_paciente_regional VARCHAR(20) PRIMARY KEY,
    cedula VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    genero CHAR(1) CHECK (genero IN ('M', 'F')),
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS historias_locales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL,
    id_historia_local VARCHAR(20) NOT NULL,
    fecha_asociacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paciente_regional) REFERENCES pacientes(id_paciente_regional) ON DELETE CASCADE,
    UNIQUE KEY unique_historia_local (sede, id_historia_local)
);
'@
$sqlContent | Out-File -FilePath "microservicio-pacientes\src\main\resources\db\migration\V1__create_paciente_table.sql" -Encoding UTF8

# ============================================
# CONSULTAS - Código completo
# ============================================
Write-Host "  📝 Generando código para consultas..." -ForegroundColor Yellow

# Consulta.java
$content = @'
package com.solca.consultas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_paciente_regional", length = 20, nullable = false)
    private String idPacienteRegional;

    @Column(name = "sede", length = 20, nullable = false)
    private String sede;

    @Column(name = "fecha_consulta", nullable = false)
    private LocalDate fechaConsulta;

    @Column(name = "especialidad", length = 50, nullable = false)
    private String especialidad;

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "tratamiento", columnDefinition = "TEXT")
    private String tratamiento;

    @Column(name = "medico", length = 100, nullable = false)
    private String medico;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @Column(name = "fecha_registro")
    @CreationTimestamp
    private LocalDateTime fechaRegistro;
}
'@
$content | Out-File -FilePath "microservicio-consultas\src\main\java\com\solca\consultas\model\Consulta.java" -Encoding UTF8

# ConsultaDTO.java
$content = @'
package com.solca.consultas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaDTO {
    private Long id;

    @NotBlank(message = "El ID del paciente es obligatorio")
    private String idPacienteRegional;

    @NotBlank(message = "La sede es obligatoria")
    private String sede;

    @NotNull(message = "La fecha de consulta es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaConsulta;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    private String diagnostico;
    private String tratamiento;

    @NotBlank(message = "El médico es obligatorio")
    private String medico;

    private String notas;
}
'@
$content | Out-File -FilePath "microservicio-consultas\src\main\java\com\solca\consultas\dto\ConsultaDTO.java" -Encoding UTF8

# ConsultaRepository.java
$content = @'
package com.solca.consultas.repository;

import com.solca.consultas.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByIdPacienteRegionalOrderByFechaConsultaDesc(String idPacienteRegional);
}
'@
$content | Out-File -FilePath "microservicio-consultas\src\main\java\com\solca\consultas\repository\ConsultaRepository.java" -Encoding UTF8

# ConsultaService.java
$content = @'
package com.solca.consultas.service;

import com.solca.consultas.dto.ConsultaDTO;
import com.solca.consultas.model.Consulta;
import com.solca.consultas.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultaService {
    private final ConsultaRepository consultaRepository;

    public ConsultaDTO registrarConsulta(ConsultaDTO dto) {
        log.info("Registrando consulta para paciente: {}", dto.getIdPacienteRegional());
        
        Consulta consulta = Consulta.builder()
                .idPacienteRegional(dto.getIdPacienteRegional())
                .sede(dto.getSede())
                .fechaConsulta(dto.getFechaConsulta())
                .especialidad(dto.getEspecialidad())
                .diagnostico(dto.getDiagnostico())
                .tratamiento(dto.getTratamiento())
                .medico(dto.getMedico())
                .notas(dto.getNotas())
                .build();

        Consulta saved = consultaRepository.save(consulta);
        dto.setId(saved.getId());
        return dto;
    }

    public List<ConsultaDTO> obtenerConsultasPorPaciente(String idPaciente) {
        return consultaRepository.findByIdPacienteRegionalOrderByFechaConsultaDesc(idPaciente)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private ConsultaDTO convertirADTO(Consulta c) {
        return ConsultaDTO.builder()
                .id(c.getId())
                .idPacienteRegional(c.getIdPacienteRegional())
                .sede(c.getSede())
                .fechaConsulta(c.getFechaConsulta())
                .especialidad(c.getEspecialidad())
                .diagnostico(c.getDiagnostico())
                .tratamiento(c.getTratamiento())
                .medico(c.getMedico())
                .notas(c.getNotas())
                .build();
    }
}
'@
$content | Out-File -FilePath "microservicio-consultas\src\main\java\com\solca\consultas\service\ConsultaService.java" -Encoding UTF8

# ConsultaController.java
$content = @'
package com.solca.consultas.controller;

import com.solca.consultas.dto.ConsultaDTO;
import com.solca.consultas.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
@Slf4j
public class ConsultaController {
    private final ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarConsulta(@Valid @RequestBody ConsultaDTO dto) {
        log.info("POST /api/consultas");
        ConsultaDTO result = consultaService.registrarConsulta(dto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Consulta registrada exitosamente");
        response.put("data", result);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    public ResponseEntity<Map<String, Object>> consultarPorPaciente(@PathVariable String idPacienteRegional) {
        log.info("GET /api/consultas/paciente/{}", idPacienteRegional);
        List<ConsultaDTO> consultas = consultaService.obtenerConsultasPorPaciente(idPacienteRegional);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", consultas);
        
        return ResponseEntity.ok(response);
    }
}
'@
$content | Out-File -FilePath "microservicio-consultas\src\main\java\com\solca\consultas\controller\ConsultaController.java" -Encoding UTF8

# SQL
$content = @'
CREATE TABLE IF NOT EXISTS consultas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL,
    fecha_consulta DATE NOT NULL,
    especialidad VARCHAR(50) NOT NULL,
    diagnostico TEXT,
    tratamiento TEXT,
    medico VARCHAR(100) NOT NULL,
    notas TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_paciente (id_paciente_regional)
);
'@
$content | Out-File -FilePath "microservicio-consultas\src\main\resources\db\migration\V1__create_consulta_table.sql" -Encoding UTF8

# ============================================
# LABORATORIO - Código completo
# ============================================
Write-Host "  📝 Generando código para laboratorio..." -ForegroundColor Yellow

# ResultadoLaboratorio.java
$content = @'
package com.solca.laboratorio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultados_laboratorio")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoLaboratorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_paciente_regional", length = 20, nullable = false)
    private String idPacienteRegional;

    @Column(name = "sede", length = 20, nullable = false)
    private String sede;

    @Column(name = "fecha_ejecucion", nullable = false)
    private LocalDate fechaEjecucion;

    @Column(name = "tipo_examen", length = 100, nullable = false)
    private String tipoExamen;

    @Column(name = "resultado", columnDefinition = "TEXT", nullable = false)
    private String resultado;

    @Column(name = "valores_referencia", columnDefinition = "TEXT")
    private String valoresReferencia;

    @Column(name = "medico_solicitante", length = 100)
    private String medicoSolicitante;

    @Column(name = "fecha_registro")
    @CreationTimestamp
    private LocalDateTime fechaRegistro;
}
'@
$content | Out-File -FilePath "microservicio-laboratorio\src\main\java\com\solca\laboratorio\model\ResultadoLaboratorio.java" -Encoding UTF8

# LaboratorioDTO.java
$content = @'
package com.solca.laboratorio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaboratorioDTO {
    private Long id;

    @NotBlank(message = "El ID del paciente es obligatorio")
    private String idPacienteRegional;

    @NotBlank(message = "La sede es obligatoria")
    private String sede;

    @NotNull(message = "La fecha de ejecución es obligatoria")
    private LocalDate fechaEjecucion;

    @NotBlank(message = "El tipo de examen es obligatorio")
    private String tipoExamen;

    @NotBlank(message = "El resultado es obligatorio")
    private String resultado;

    private String valoresReferencia;
    private String medicoSolicitante;
}
'@
$content | Out-File -FilePath "microservicio-laboratorio\src\main\java\com\solca\laboratorio\dto\LaboratorioDTO.java" -Encoding UTF8

# LaboratorioRepository.java
$content = @'
package com.solca.laboratorio.repository;

import com.solca.laboratorio.model.ResultadoLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaboratorioRepository extends JpaRepository<ResultadoLaboratorio, Long> {
    List<ResultadoLaboratorio> findByIdPacienteRegionalOrderByFechaEjecucionDesc(String idPacienteRegional);
}
'@
$content | Out-File -FilePath "microservicio-laboratorio\src\main\java\com\solca\laboratorio\repository\LaboratorioRepository.java" -Encoding UTF8

# LaboratorioService.java
$content = @'
package com.solca.laboratorio.service;

import com.solca.laboratorio.dto.LaboratorioDTO;
import com.solca.laboratorio.model.ResultadoLaboratorio;
import com.solca.laboratorio.repository.LaboratorioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LaboratorioService {
    private final LaboratorioRepository laboratorioRepository;

    public LaboratorioDTO registrarResultado(LaboratorioDTO dto) {
        log.info("Registrando resultado de laboratorio para: {}", dto.getIdPacienteRegional());
        
        ResultadoLaboratorio resultado = ResultadoLaboratorio.builder()
                .idPacienteRegional(dto.getIdPacienteRegional())
                .sede(dto.getSede())
                .fechaEjecucion(dto.getFechaEjecucion())
                .tipoExamen(dto.getTipoExamen())
                .resultado(dto.getResultado())
                .valoresReferencia(dto.getValoresReferencia())
                .medicoSolicitante(dto.getMedicoSolicitante())
                .build();

        ResultadoLaboratorio saved = laboratorioRepository.save(resultado);
        dto.setId(saved.getId());
        return dto;
    }

    public List<LaboratorioDTO> obtenerResultadosPorPaciente(String idPaciente) {
        return laboratorioRepository.findByIdPacienteRegionalOrderByFechaEjecucionDesc(idPaciente)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private LaboratorioDTO convertirADTO(ResultadoLaboratorio r) {
        return LaboratorioDTO.builder()
                .id(r.getId())
                .idPacienteRegional(r.getIdPacienteRegional())
                .sede(r.getSede())
                .fechaEjecucion(r.getFechaEjecucion())
                .tipoExamen(r.getTipoExamen())
                .resultado(r.getResultado())
                .valoresReferencia(r.getValoresReferencia())
                .medicoSolicitante(r.getMedicoSolicitante())
                .build();
    }
}
'@
$content | Out-File -FilePath "microservicio-laboratorio\src\main\java\com\solca\laboratorio\service\LaboratorioService.java" -Encoding UTF8

# LaboratorioController.java
$content = @'
package com.solca.laboratorio.controller;

import com.solca.laboratorio.dto.LaboratorioDTO;
import com.solca.laboratorio.service.LaboratorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/laboratorio")
@RequiredArgsConstructor
@Slf4j
public class LaboratorioController {
    private final LaboratorioService laboratorioService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarResultado(@Valid @RequestBody LaboratorioDTO dto) {
        log.info("POST /api/laboratorio");
        LaboratorioDTO result = laboratorioService.registrarResultado(dto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Resultado registrado exitosamente");
        response.put("data", result);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    public ResponseEntity<Map<String, Object>> consultarPorPaciente(@PathVariable String idPacienteRegional) {
        log.info("GET /api/laboratorio/paciente/{}", idPacienteRegional);
        List<LaboratorioDTO> resultados = laboratorioService.obtenerResultadosPorPaciente(idPacienteRegional);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", resultados);
        
        return ResponseEntity.ok(response);
    }
}
'@
$content | Out-File -FilePath "microservicio-laboratorio\src\main\java\com\solca\laboratorio\controller\LaboratorioController.java" -Encoding UTF8

# SQL
$content = @'
CREATE TABLE IF NOT EXISTS resultados_laboratorio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL,
    fecha_ejecucion DATE NOT NULL,
    tipo_examen VARCHAR(100) NOT NULL,
    resultado TEXT NOT NULL,
    valores_referencia TEXT,
    medico_solicitante VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_paciente (id_paciente_regional)
);
'@
$content | Out-File -FilePath "microservicio-laboratorio\src\main\resources\db\migration\V1__create_laboratorio_table.sql" -Encoding UTF8

# ============================================
# IMAGENOLOGIA - Código completo
# ============================================
Write-Host "  📝 Generando código para imagenologia..." -ForegroundColor Yellow

# EstudioImagen.java
$content = @'
package com.solca.imagenologia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "estudios_imagen")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstudioImagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_paciente_regional", length = 20, nullable = false)
    private String idPacienteRegional;

    @Column(name = "sede", length = 20, nullable = false)
    private String sede;

    @Column(name = "fecha_estudio", nullable = false)
    private LocalDate fechaEstudio;

    @Column(name = "tipo_estudio", length = 50, nullable = false)
    private String tipoEstudio;

    @Column(name = "formato", length = 10, nullable = false)
    private String formato;

    @Column(name = "url_archivo", length = 500, nullable = false)
    private String urlArchivo;

    @Column(name = "nombre_archivo", length = 255)
    private String nombreArchivo;

    @Column(name = "medico_solicitante", length = 100)
    private String medicoSolicitante;

    @Column(name = "fecha_registro")
    @CreationTimestamp
    private LocalDateTime fechaRegistro;
}
'@
$content | Out-File -FilePath "microservicio-imagenologia\src\main\java\com\solca\imagenologia\model\EstudioImagen.java" -Encoding UTF8

# ImagenologiaDTO.java
$content = @'
package com.solca.imagenologia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagenologiaDTO {
    private Long id;

    @NotBlank(message = "El ID del paciente es obligatorio")
    private String idPacienteRegional;

    @NotBlank(message = "La sede es obligatoria")
    private String sede;

    @NotNull(message = "La fecha de estudio es obligatoria")
    private LocalDate fechaEstudio;

    @NotBlank(message = "El tipo de estudio es obligatorio")
    private String tipoEstudio;

    @NotBlank(message = "El formato es obligatorio")
    private String formato;

    @NotBlank(message = "La URL del archivo es obligatoria")
    private String urlArchivo;

    private String nombreArchivo;
    private String medicoSolicitante;
}
'@
$content | Out-File -FilePath "microservicio-imagenologia\src\main\java\com\solca\imagenologia\dto\ImagenologiaDTO.java" -Encoding UTF8

# ImagenologiaRepository.java
$content = @'
package com.solca.imagenologia.repository;

import com.solca.imagenologia.model.EstudioImagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenologiaRepository extends JpaRepository<EstudioImagen, Long> {
    List<EstudioImagen> findByIdPacienteRegionalOrderByFechaEstudioDesc(String idPacienteRegional);
}
'@
$content | Out-File -FilePath "microservicio-imagenologia\src\main\java\com\solca\imagenologia\repository\ImagenologiaRepository.java" -Encoding UTF8

# ImagenologiaService.java
$content = @'
package com.solca.imagenologia.service;

import com.solca.imagenologia.dto.ImagenologiaDTO;
import com.solca.imagenologia.model.EstudioImagen;
import com.solca.imagenologia.repository.ImagenologiaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImagenologiaService {
    private final ImagenologiaRepository imagenologiaRepository;

    public ImagenologiaDTO registrarEstudio(ImagenologiaDTO dto) {
        log.info("Registrando estudio de imagen para: {}", dto.getIdPacienteRegional());
        
        EstudioImagen estudio = EstudioImagen.builder()
                .idPacienteRegional(dto.getIdPacienteRegional())
                .sede(dto.getSede())
                .fechaEstudio(dto.getFechaEstudio())
                .tipoEstudio(dto.getTipoEstudio())
                .formato(dto.getFormato())
                .urlArchivo(dto.getUrlArchivo())
                .nombreArchivo(dto.getNombreArchivo())
                .medicoSolicitante(dto.getMedicoSolicitante())
                .build();

        EstudioImagen saved = imagenologiaRepository.save(estudio);
        dto.setId(saved.getId());
        return dto;
    }

    public List<ImagenologiaDTO> obtenerEstudiosPorPaciente(String idPaciente) {
        return imagenologiaRepository.findByIdPacienteRegionalOrderByFechaEstudioDesc(idPaciente)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private ImagenologiaDTO convertirADTO(EstudioImagen e) {
        return ImagenologiaDTO.builder()
                .id(e.getId())
                .idPacienteRegional(e.getIdPacienteRegional())
                .sede(e.getSede())
                .fechaEstudio(e.getFechaEstudio())
                .tipoEstudio(e.getTipoEstudio())
                .formato(e.getFormato())
                .urlArchivo(e.getUrlArchivo())
                .nombreArchivo(e.getNombreArchivo())
                .medicoSolicitante(e.getMedicoSolicitante())
                .build();
    }
}
'@
$content | Out-File -FilePath "microservicio-imagenologia\src\main\java\com\solca\imagenologia\service\ImagenologiaService.java" -Encoding UTF8

# ImagenologiaController.java
$content = @'
package com.solca.imagenologia.controller;

import com.solca.imagenologia.dto.ImagenologiaDTO;
import com.solca.imagenologia.service.ImagenologiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
@Slf4j
public class ImagenologiaController {
    private final ImagenologiaService imagenologiaService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarEstudio(@Valid @RequestBody ImagenologiaDTO dto) {
        log.info("POST /api/imagenes");
        ImagenologiaDTO result = imagenologiaService.registrarEstudio(dto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Estudio registrado exitosamente");
        response.put("data", result);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    public ResponseEntity<Map<String, Object>> consultarPorPaciente(@PathVariable String idPacienteRegional) {
        log.info("GET /api/imagenes/paciente/{}", idPacienteRegional);
        List<ImagenologiaDTO> estudios = imagenologiaService.obtenerEstudiosPorPaciente(idPacienteRegional);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", estudios);
        
        return ResponseEntity.ok(response);
    }
}
'@
$content | Out-File -FilePath "microservicio-imagenologia\src\main\java\com\solca\imagenologia\controller\ImagenologiaController.java" -Encoding UTF8

# SQL
$content = @'
CREATE TABLE IF NOT EXISTS estudios_imagen (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_paciente_regional VARCHAR(20) NOT NULL,
    sede VARCHAR(20) NOT NULL,
    fecha_estudio DATE NOT NULL,
    tipo_estudio VARCHAR(50) NOT NULL,
    formato VARCHAR(10) NOT NULL,
    url_archivo VARCHAR(500) NOT NULL,
    nombre_archivo VARCHAR(255),
    medico_solicitante VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_paciente (id_paciente_regional)
);
'@
$content | Out-File -FilePath "microservicio-imagenologia\src\main\resources\db\migration\V1__create_imagenologia_table.sql" -Encoding UTF8

# ============================================
# REPOSITORIO - Código completo
# ============================================
Write-Host "  📝 Generando código para repositorio..." -ForegroundColor Yellow

# WebClientConfig.java
$content = @'
package com.solca.repositorio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Value("${services.pacientes.url}")
    private String pacientesUrl;
    
    @Value("${services.consultas.url}")
    private String consultasUrl;
    
    @Value("${services.laboratorio.url}")
    private String laboratorioUrl;
    
    @Value("${services.imagenologia.url}")
    private String imagenologiaUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();
    }

    @Bean
    public WebClient pacientesWebClient(WebClient webClient) {
        return webClient.mutate().baseUrl(pacientesUrl + "/api").build();
    }

    @Bean
    public WebClient consultasWebClient(WebClient webClient) {
        return webClient.mutate().baseUrl(consultasUrl + "/api").build();
    }

    @Bean
    public WebClient laboratorioWebClient(WebClient webClient) {
        return webClient.mutate().baseUrl(laboratorioUrl + "/api").build();
    }

    @Bean
    public WebClient imagenologiaWebClient(WebClient webClient) {
        return webClient.mutate().baseUrl(imagenologiaUrl + "/api").build();
    }
}
'@
$content | Out-File -FilePath "microservicio-repositorio\src\main\java\com\solca\repositorio\config\WebClientConfig.java" -Encoding UTF8

# RepositorioResponse.java
$content = @'
package com.solca.repositorio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositorioResponse {
    private Map<String, Object> paciente;
    private List<Map<String, Object>> consultas;
    private List<Map<String, Object>> laboratorio;
    private List<Map<String, Object>> imagenes;
    private String status;
    private Map<String, String> errores;
}
'@
$content | Out-File -FilePath "microservicio-repositorio\src\main\java\com\solca\repositorio\model\RepositorioResponse.java" -Encoding UTF8

# RepositorioService.java
$content = @'
package com.solca.repositorio.service;

import com.solca.repositorio.model.RepositorioResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepositorioService {

    @Qualifier("pacientesWebClient")
    private final WebClient pacientesWebClient;

    @Qualifier("consultasWebClient")
    private final WebClient consultasWebClient;

    @Qualifier("laboratorioWebClient")
    private final WebClient laboratorioWebClient;

    @Qualifier("imagenologiaWebClient")
    private final WebClient imagenologiaWebClient;

    public RepositorioResponse obtenerInformacionConsolidada(String idPacienteRegional) {
        log.info("Consultando información consolidada para paciente: {}", idPacienteRegional);

        RepositorioResponse response = RepositorioResponse.builder()
                .status("COMPLETE")
                .errores(new HashMap<>())
                .build();

        try {
            Map<String, Object> paciente = obtenerDatosPaciente(idPacienteRegional);
            response.setPaciente(paciente);
        } catch (Exception e) {
            log.error("Error al obtener paciente: {}", e.getMessage());
            response.setStatus("PARTIAL");
            response.getErrores().put("paciente", "SERVICIO NO DISPONIBLE");
        }

        try {
            List<Map<String, Object>> consultas = obtenerConsultas(idPacienteRegional);
            response.setConsultas(consultas != null ? consultas : new ArrayList<>());
        } catch (Exception e) {
            log.error("Error al obtener consultas: {}", e.getMessage());
            response.setConsultas(new ArrayList<>());
            response.setStatus("PARTIAL");
            response.getErrores().put("consultas", "SERVICIO NO DISPONIBLE");
        }

        try {
            List<Map<String, Object>> laboratorio = obtenerLaboratorio(idPacienteRegional);
            response.setLaboratorio(laboratorio != null ? laboratorio : new ArrayList<>());
        } catch (Exception e) {
            log.error("Error al obtener laboratorio: {}", e.getMessage());
            response.setLaboratorio(new ArrayList<>());
            response.setStatus("PARTIAL");
            response.getErrores().put("laboratorio", "SERVICIO NO DISPONIBLE");
        }

        try {
            List<Map<String, Object>> imagenes = obtenerImagenes(idPacienteRegional);
            response.setImagenes(imagenes != null ? imagenes : new ArrayList<>());
        } catch (Exception e) {
            log.error("Error al obtener imágenes: {}", e.getMessage());
            response.setImagenes(new ArrayList<>());
            response.setStatus("PARTIAL");
            response.getErrores().put("imagenes", "SERVICIO NO DISPONIBLE");
        }

        return response;
    }

    private Map<String, Object> obtenerDatosPaciente(String idPacienteRegional) {
        try {
            return pacientesWebClient.get()
                    .uri("/pacientes/{id}", idPacienteRegional)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response -> {
                        log.warn("Paciente no encontrado: {}", idPacienteRegional);
                        return Mono.empty();
                    })
                    .bodyToMono(Map.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error en servicio de pacientes: {}", e.getMessage());
            throw new RuntimeException("Error al obtener paciente", e);
        }
    }

    private List<Map<String, Object>> obtenerConsultas(String idPacienteRegional) {
        try {
            return consultasWebClient.get()
                    .uri("/consultas/paciente/{id}", idPacienteRegional)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response -> Mono.empty())
                    .bodyToMono(List.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error en servicio de consultas: {}", e.getMessage());
            throw new RuntimeException("Error al obtener consultas", e);
        }
    }

    private List<Map<String, Object>> obtenerLaboratorio(String idPacienteRegional) {
        try {
            return laboratorioWebClient.get()
                    .uri("/laboratorio/paciente/{id}", idPacienteRegional)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response -> Mono.empty())
                    .bodyToMono(List.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error en servicio de laboratorio: {}", e.getMessage());
            throw new RuntimeException("Error al obtener laboratorio", e);
        }
    }

    private List<Map<String, Object>> obtenerImagenes(String idPacienteRegional) {
        try {
            return imagenologiaWebClient.get()
                    .uri("/imagenes/paciente/{id}", idPacienteRegional)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response -> Mono.empty())
                    .bodyToMono(List.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error en servicio de imagenología: {}", e.getMessage());
            throw new RuntimeException("Error al obtener imágenes", e);
        }
    }
}
'@
$content | Out-File -FilePath "microservicio-repositorio\src\main\java\com\solca\repositorio\service\RepositorioService.java" -Encoding UTF8

# RepositorioController.java
$content = @'
package com.solca.repositorio.controller;

import com.solca.repositorio.model.RepositorioResponse;
import com.solca.repositorio.service.RepositorioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/repositorio")
@RequiredArgsConstructor
@Slf4j
public class RepositorioController {
    private final RepositorioService repositorioService;

    @GetMapping("/paciente/{idPacienteRegional}")
    public ResponseEntity<RepositorioResponse> consultarPaciente(
            @PathVariable String idPacienteRegional) {
        log.info("Consulta al repositorio para paciente: {}", idPacienteRegional);
        
        RepositorioResponse response = repositorioService.obtenerInformacionConsolidada(idPacienteRegional);
        return ResponseEntity.ok(response);
    }
}
'@
$content | Out-File -FilePath "microservicio-repositorio\src\main\java\com\solca\repositorio\controller\RepositorioController.java" -Encoding UTF8

Write-Host "`n✅ TODOS LOS ARCHIVOS DE CÓDIGO GENERADOS EXITOSAMENTE!" -ForegroundColor Green
Write-Host "`n🎉 PROYECTO COMPLETO GENERADO!" -ForegroundColor Green
Write-Host "`n==========================================" -ForegroundColor Cyan
Write-Host "📋 PARA CONTINUAR, EJECUTA:" -ForegroundColor Yellow
Write-Host "   .\scripts\build-all.ps1" -ForegroundColor White
Write-Host "   .\scripts\start-all.ps1" -ForegroundColor White
Write-Host "==========================================" -ForegroundColor Cyan