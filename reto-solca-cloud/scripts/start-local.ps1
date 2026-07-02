param([switch]$RecreateDB)

$RootDir = Split-Path -Parent $PSCommandPath
$LogDir = Join-Path $RootDir "logs"
if (-not (Test-Path $LogDir)) { New-Item -ItemType Directory -Path $LogDir -Force | Out-Null }

# Recrear bases de datos si se solicita
if ($RecreateDB) {
    $mysql = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    Write-Host "[*] Recreando bases de datos..." -ForegroundColor Cyan
    & $mysql -u root -pgalito2002 -e "DROP DATABASE IF EXISTS db_pacientes; CREATE DATABASE db_pacientes DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; DROP DATABASE IF EXISTS db_consultas; CREATE DATABASE db_consultas DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; DROP DATABASE IF EXISTS db_laboratorio; CREATE DATABASE db_laboratorio DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; DROP DATABASE IF EXISTS db_imagenologia; CREATE DATABASE db_imagenologia DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>&1 | Out-Null
    Write-Host "[OK] Bases de datos recreadas" -ForegroundColor Green
}

$Services = @(
    @{ Name = "pacientes"; Port = 8081; Dir = "microservicio-pacientes"; HasDB = $true },
    @{ Name = "consultas"; Port = 8082; Dir = "microservicio-consultas"; HasDB = $true },
    @{ Name = "laboratorio"; Port = 8083; Dir = "microservicio-laboratorio"; HasDB = $true },
    @{ Name = "imagenologia"; Port = 8084; Dir = "microservicio-imagenologia"; HasDB = $true },
    @{ Name = "repositorio"; Port = 8085; Dir = "microservicio-repositorio"; HasDB = $false }
)

# Matar procesos Java existentes
Get-Process -Name "java" -ErrorAction SilentlyContinue | Stop-Process -Force
Start-Sleep -Seconds 2

# Iniciar cada servicio
foreach ($svc in $Services) {
    $logFile = Join-Path $LogDir "$($svc.Name).log"
    $svcDir = Join-Path $RootDir $svc.Dir
    Write-Host "[*] Iniciando $($svc.Name)-service (puerto $($svc.Port))..." -ForegroundColor Cyan
    Start-Process -NoNewWindow -FilePath "cmd.exe" -ArgumentList "/c mvn spring-boot:run -q > `"$logFile`" 2>&1" -WorkingDirectory $svcDir
}

# Esperar a que cada servicio esté listo
Write-Host "`n[*] Esperando que los servicios inicien..." -ForegroundColor Yellow
foreach ($svc in $Services) {
    $url = "http://localhost:$($svc.Port)/api/actuator/health"
    $ready = $false
    for ($i = 0; $i -lt 40; $i++) {
        try {
            $response = Invoke-WebRequest -Uri $url -Method GET -UseBasicParsing -TimeoutSec 2 -ErrorAction Stop
            if ($response.StatusCode -eq 200) {
                $ready = $true
                break
            }
        } catch {}
        Start-Sleep -Seconds 2
    }
    if ($ready) {
        Write-Host "  [OK] $($svc.Name)-service listo en http://localhost:$($svc.Port)/api" -ForegroundColor Green
    } else {
        Write-Host "  [FAIL] $($svc.Name)-service NO responde. Revisa logs\$($svc.Name).log" -ForegroundColor Red
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host " MICROSERVICIOS INICIADOS" -ForegroundColor White -BackgroundColor DarkGreen
Write-Host "========================================" -ForegroundColor Cyan
Write-Host " Pacientes:    http://localhost:8081/api/pacientes" -ForegroundColor Yellow
Write-Host " Consultas:    http://localhost:8082/api/consultas" -ForegroundColor Yellow
Write-Host " Laboratorio:  http://localhost:8083/api/laboratorio" -ForegroundColor Yellow
Write-Host " Imagenologia: http://localhost:8084/api/imagenes" -ForegroundColor Yellow
Write-Host " Repositorio:  http://localhost:8085/api/repositorio" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Cyan
