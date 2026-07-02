# Reto SOLCA Cloud - Script de ejecución
# Uso: .\run.ps1 [comando]
#   build    -> Compila todos los microservicios y construye imágenes Docker
#   up       -> Inicia todos los contenedores en segundo plano
#   down     -> Detiene todos los contenedores
#   ps       -> Muestra el estado de los contenedores
#   logs     -> Muestra logs de todos los servicios
#   restart  -> Reinicia todos los servicios
#   (sin argumentos) -> Ejecuta build + up + ps

param(
    [string]$Command = ""
)

$RootDir = Split-Path -Parent $PSCommandPath
$ComposeFile = Join-Path $RootDir "docker-compose.yml"
$ScriptsDir = Join-Path $RootDir "scripts"
$BuildScript = Join-Path $ScriptsDir "build-all.ps1"

function Write-Step($msg) {
    Write-Host "`n========================================" -ForegroundColor Cyan
    Write-Host " $msg" -ForegroundColor White -BackgroundColor DarkBlue
    Write-Host "========================================" -ForegroundColor Cyan
}

function Write-OK($msg) {
    Write-Host " [OK] $msg" -ForegroundColor Green
}

function Write-Info($msg) {
    Write-Host "  -> $msg" -ForegroundColor Yellow
}

# Verificar prerequisitos
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Host "[ERROR] Docker no encontrado. Instala Docker Desktop primero." -ForegroundColor Red
    exit 1
}
if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Host "[ERROR] Maven no encontrado. Instala Maven 3.9+ primero." -ForegroundColor Red
    exit 1
}
if (-not (Get-Command java -ErrorAction SilentlyContinue) -or (java -version 2>&1) -notmatch "21") {
    Write-Host "[WARN] Se recomienda Java 21 para este proyecto." -ForegroundColor Yellow
}

Push-Location $RootDir
try {
    switch ($Command.ToLower()) {
        "build" {
            Write-Step "COMPILANDO MICROSERVICIOS CON MAVEN"
            & $BuildScript
            if ($LASTEXITCODE -ne 0) { exit 1 }

            Write-Step "CONSTRUYENDO IMÁGENES DOCKER"
            docker-compose build --parallel
            if ($LASTEXITCODE -eq 0) {
                Write-OK "Imágenes Docker construidas exitosamente"
            } else {
                Write-Host "[ERROR] Fallo al construir imágenes Docker" -ForegroundColor Red
                exit 1
            }
        }
        "up" {
            Write-Step "INICIANDO CONTENEDORES"
            docker-compose up -d
            if ($LASTEXITCODE -eq 0) {
                Write-OK "Contenedores iniciados"
                Start-Sleep -Seconds 3
                & $PSCommandPath ps
            }
        }
        "down" {
            Write-Step "DETENIENDO CONTENEDORES"
            docker-compose down
            Write-OK "Contenedores detenidos"
        }
        "ps" {
            Write-Step "ESTADO DE CONTENEDORES"
            docker-compose ps
        }
        "logs" {
            $Params = @("logs", "-f")
            if ($args.Count -gt 0) { $Params += $args[0] }
            & docker-compose $Params
        }
        "restart" {
            Write-Step "REINICIANDO SERVICIOS"
            docker-compose restart
            Start-Sleep -Seconds 3
            & $PSCommandPath ps
        }
        default {
            # Comando por defecto: build + up + ps
            & $PSCommandPath build
            if ($LASTEXITCODE -eq 0) {
                & $PSCommandPath up
                Write-Step "SERVICIOS DISPONIBLES"
                $urls = @(
                    "http://localhost:8080  -> phpMyAdmin",
                    "http://localhost:8081/api/actuator/health  -> Pacientes",
                    "http://localhost:8082/api/actuator/health  -> Consultas",
                    "http://localhost:8083/api/actuator/health  -> Laboratorio",
                    "http://localhost:8084/api/actuator/health  -> Imagenología",
                    "http://localhost:8085/api/actuator/health  -> Repositorio"
                )
                $urls | ForEach-Object { Write-Info $_ }
                Write-Host "`n Usa 'docker-compose logs -f [servicio]' para ver logs" -ForegroundColor Yellow
                Write-Host " Usa '.\run.ps1 down' para detener todo" -ForegroundColor Yellow
            }
        }
    }
} finally {
    Pop-Location
}
